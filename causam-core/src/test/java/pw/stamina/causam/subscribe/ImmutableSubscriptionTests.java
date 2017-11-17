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

import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import pw.stamina.causam.publish.listen.Listener;
import pw.stamina.causam.select.KeySelector;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
final class ImmutableSubscriptionTests {

    private ImmutableSubscription<Object> subscription;

    private final String identifier = "immutable_subscription";
    @Mock private Object subscriber;
    @Mock private KeySelector keySelector;
    @Mock private Listener<Object> listener;

    @BeforeEach
    void setupSubscription() {
        subscription = new ImmutableSubscription<>(
                subscriber,
                identifier,
                keySelector,
                listener);
    }

    @Test
    void getSubscriber_shouldReturnSubscriberAsSpecifiedInConstructor() {
        assertSame(subscriber, subscription.getSubscriber());
    }

    @Test
    void getIdentifier_shouldReturnIdentifierAsSpecifiedInConstructor() {
        Optional<String> subscriptionIdentifier = subscription.getIdentifier();

        assertTrue(subscriptionIdentifier.isPresent());
        assertSame(identifier, subscriptionIdentifier.get());
    }

    @Test
    void getSelector_shouldReturnSelectorAsSpecifiedInConstructor() {
        assertSame(keySelector, subscription.getKeySelector());
    }

    @Test
    void publish_givenInputIsNull_shouldListenerBeCalledOnceWithNull() {
        subscription.publish(null);
        verify(listener).publish(isNull());
    }

    @Test
    void publish_givenNonNullInput_shouldListenerBeCalledOnceWithEvent() {
        Object event = new Object();

        subscription.publish(event);
        verify(listener).publish(event);
    }
}
