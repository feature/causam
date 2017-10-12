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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pw.stamina.causam.publish.listen.Listener;
import pw.stamina.causam.select.KeySelector;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;

public final class ImmutableSubscriptionTests {

    private ImmutableSubscription<?> subscription;

    private final String identifier = "immutable_subscription";

    @Mock private Object subscriber;
    @Mock private KeySelector keySelector;
    @Mock private Listener<?> listener;

    @BeforeAll
    public void setupImmutableSubscription() {
        //TODO
        MockitoAnnotations.initMocks(this);

        subscription = new ImmutableSubscription<>(
                subscriber,
                identifier,
                keySelector,
                listener);
    }

    @Test
    public void getSubscriber_shouldReturnSameSubscriberAsSpecifiedInConstructor() {
        assertSame(subscription.getSubscriber(), subscriber);
    }

    @Test
    public void getIdentifier_shouldReturnSameIdentifierAsSpecifiedInConstructor() {
        assertSame(subscription.getIdentifier(), identifier);
    }

    @Test
    public void getSelector_shouldReturnSameSelectorAsSpecifiedInConstructor() {
        assertSame(subscription.getKeySelector(), keySelector);
    }

    @Test
    public void publish_eventParameterIsNull_shouldListenerBeCalledExactlyOnceWithEventAsNull() {
        subscription.publish(null);
        verify(listener).publish(isNull());
    }
}
