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

import pw.stamina.causam.publish.listen.Listener;
import pw.stamina.causam.publish.listen.decorate.ListenerDecorator;
import pw.stamina.causam.publish.listen.decorate.ListenerDecoratorContainer;
import pw.stamina.causam.select.KeySelector;

import java.util.Objects;

public final class SubscriptionBuilder<T> {
    private Object subscriber;
    private String identifier;
    private KeySelector keySelector;
    private boolean ignoreCancelled;
    private Listener<T> listener;
    private final ListenerDecoratorContainer<T> decorators;

    public SubscriptionBuilder() {
        decorators = new ListenerDecoratorContainer<>();
    }

    public SubscriptionBuilder<T> subscriber(Object subscriber) {
        Objects.requireNonNull(subscriber, "subscriber");
        this.subscriber = subscriber;
        return this;
    }

    public SubscriptionBuilder<T> identifier(String identifier) {
        Objects.requireNonNull(identifier, "identifier");
        this.identifier = identifier;
        return this;
    }

    public SubscriptionBuilder<T> selector(KeySelector keySelector) {
        Objects.requireNonNull(keySelector, "keySelector");
        this.keySelector = keySelector;
        return this;
    }

    public SubscriptionBuilder<T> ignoreCancelled() {
        ignoreCancelled = true;
        return this;
    }

    public SubscriptionBuilder<T> listener(Listener<T> listener) {
        Objects.requireNonNull(listener, "listener");
        this.listener = listener;
        return this;
    }

    public SubscriptionBuilder<T> decorate(ListenerDecorator<T> decorator) {
        Objects.requireNonNull(decorator, "decorator");
        decorators.add(decorator);
        return this;
    }

    public Subscription<T> build() {
        validateBuilderIsComplete();

        Listener<T> decoratedListener = decorators.applyDecorationsToListener(listener);

        Subscription<T> subscription =
                new ImmutableSubscription<>(
                        subscriber,
                        identifier,
                        keySelector,
                        ignoreCancelled,
                        decoratedListener);

        if (decorators.shouldCreateProxy()) {
            return SubscriptionProxyFactory.createSubscriptionProxy(subscription, decorators);
        } else {
            return subscription;
        }
    }

    private void validateBuilderIsComplete() {
        if (subscriber == null) {
            throw new IllegalStateException("subscriber has not been set, " +
                    "please set it using the #subscriber method");
        } else if (keySelector == null) {
            throw new IllegalStateException("keySelector has not been set, " +
                    "please set it using the #keySelector method");
        }

        validateListenerHasBeenSet();
    }

    private void validateListenerHasBeenSet() {
        if (listener == null) {
            throw new IllegalStateException("listener has not been set, " +
                    "please set it using the #listener method");
        }
    }
}
