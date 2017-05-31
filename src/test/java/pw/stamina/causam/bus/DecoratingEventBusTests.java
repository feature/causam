/*
 * Copyright (c) 2017 Abstraction
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package pw.stamina.causam.bus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pw.stamina.causam.Identifier;
import pw.stamina.causam.publish.dispatch.Dispatcher;
import pw.stamina.causam.registry.SubscriptionRegistrationFacade;
import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.select.SubscriptionSelectorService;
import pw.stamina.causam.subscribe.Subscription;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class DecoratingEventBusTests {

    private EventBus bus;
    private final Identifier busIdentifier = Identifier.of("test-bus");

    @Mock private SubscriptionRegistry registry;
    @Mock private SubscriptionSelectorService selector;
    @Mock private Dispatcher dispatcher;
    @Mock private Subscription<Object> subscription;

    @Before
    public void setupBus() {
        bus = new ImmutableEventBus(
                busIdentifier,
                registry,
                selector,
                dispatcher);
    }

    @Test
    public void post_selectorReturnsEmptySubscriptions_shouldCallSelectorExactlyOnceAndNeverDispatcher() {
        bus.post(new Object());

        verify(selector).selectSubscriptions(notNull(), notNull());
        verify(dispatcher, never()).dispatch(notNull(), notNull());
    }

    @Test
    public void post_selectorReturnsSingletonListOfSubscription_shouldCallSelectorAndDispatcherExactlyOnce() {
        List<Subscription<Object>> subscriptions = Collections.singletonList(subscription);
        when(selector.selectSubscriptions(notNull(), notNull())).thenReturn(subscriptions);

        bus.post(new Object());

        verify(selector).selectSubscriptions(notNull(), notNull());
        verify(dispatcher).dispatch(notNull(), notNull());
    }

    @Test
    public void getIdentifier_shouldReturnIdentifierSpecifiedInConstructor() {
        assertThat(bus.getIdentifier(), sameInstance(busIdentifier));
    }

    @Test
    public void getRegistry_shouldReturnRegistrySpecifiedInConstructor() {
        assertThat(bus.getRegistry(), sameInstance(registry));
    }

    @Test
    public void getRegistrationFacade_createRegistrationFacadeOfRegistrySpecifiedInBusConstructor_shouldReturnSimpleRegistrationFacadeOfRegistry() {
        SubscriptionRegistrationFacade facade = bus.getRegistrationFacade();
        String facadeClassSimpleName = facade.getClass().getSimpleName();

        assertThat(facadeClassSimpleName, is("SimpleSubscriptionRegistrationFacade"));
    }
}
