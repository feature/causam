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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pw.stamina.causam.publish.Publisher;
import pw.stamina.causam.registry.SubscriptionRegistrationFacade;
import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.select.SubscriptionSelectorService;
import pw.stamina.causam.subscribe.Subscription;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

public final class StandardEventBusTests {

    private EventBus bus;
    private final String busIdentifier = "test-bus";

    @Mock private SubscriptionRegistry registry;
    @Mock private SubscriptionRegistrationFacade registrationFacade;
    @Mock private SubscriptionSelectorService selector;
    @Mock private Publisher publisher;
    @Mock private Subscription<Object> subscription;

    @BeforeEach
    public void setupFacadeProviderAndCreateImmutableEventBus() {
        //TODO
        MockitoAnnotations.initMocks(this);

        bus = new StandardEventBus(
                busIdentifier,
                registry,
                publisher);
    }

    @Test
    public void post_selectorReturnsEmptySubscriptions_shouldCallSelectorExactlyOnceAndNeverDispatcher() {
        bus.post(new Object());

        verify(selector).selectSubscriptions(notNull(), notNull());
        verify(publisher, never()).publish(notNull(), notNull());
    }

    @Test
    public void post_selectorReturnsSingletonListOfSubscription_shouldCallSelectorAndDispatcherExactlyOnce() {
        List<Subscription<Object>> subscriptions = Collections.singletonList(subscription);
        when(selector.selectSubscriptions(notNull(), notNull())).thenReturn(subscriptions);

        bus.post(new Object());

        verify(selector).selectSubscriptions(notNull(), notNull());
        verify(publisher).publish(notNull(), notNull());
    }

    @Test
    public void getIdentifier_shouldReturnIdentifierSpecifiedInConstructor() {
        assertSame(bus.getIdentifier(), busIdentifier);
    }

    @Test
    public void getRegistry_shouldReturnRegistrySpecifiedInConstructor() {
        assertSame(bus.getRegistry(), registry);
    }
}
