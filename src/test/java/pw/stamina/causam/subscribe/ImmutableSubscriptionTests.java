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

package pw.stamina.causam.subscribe;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pw.stamina.causam.Identifier;
import pw.stamina.causam.select.Selector;
import pw.stamina.causam.subscribe.listen.Listener;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class ImmutableSubscriptionTests {

    private ImmutableSubscription<?> subscription;

    private final Identifier identifier = Identifier.of("immutable_subscription");

    @Mock private Object subscriber;
    @Mock private Selector selector;
    @Mock private Listener<?> listener;
    @Mock private Map<Class<?>, ?> decorations;

    @Before
    public void setupImmutableSubscription() {
        subscription = new ImmutableSubscription<>(
                subscriber,
                identifier,
                selector,
                listener,
                decorations);
    }

    @Test
    public void getSubscriber_shouldReturnSameSubscriberAsSpecifiedInConstructor() {
        assertThat(subscription.getSubscriber(), sameInstance(subscriber));
    }

    @Test
    public void getIdentifier_shouldReturnSameIdentifierAsSpecifiedInConstructor() {
        assertThat(subscription.getIdentifier(), sameInstance(identifier));
    }

    @Test
    public void getSelector_shouldReturnSameSelectorAsSpecifiedInConstructor() {
        assertThat(subscription.getSelector(), sameInstance(selector));
    }

    @Test
    public void call_eventParameterIsNull_shouldListenerBeCalledExactlyOnceWithEventAsNull() {
        subscription.call(null);
        verify(listener).publish(isNull());
    }

    @Test
    public void hasDecoration_passedClassOfTypeObject_shouldDecorationsContainsKeyHaveBeenCalledExactlyOnceWithTheClassOfTypeObject() {
        assertThat(subscription.hasDecoration(Object.class), is(false));
        verify(decorations).containsKey(same(Object.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDecorations_shouldReturnSameDecorationsAsSpecifiedInConstructorWrappedInAnUnmodifiableMap() {
        subscription.getDecorations().clear();
    }
}
