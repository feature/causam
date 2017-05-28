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

import pw.stamina.causam.Identifier;
import pw.stamina.causam.select.Selector;
import pw.stamina.causam.subscribe.listen.Listener;
import pw.stamina.causam.subscribe.listen.decorate.SubscriptionListenerDecorator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class SubscriptionBuilder<T> {
    private Object subscriber;
    private Identifier identifier;
    private Listener<T> listener;
    private Map<Class<?>, Object> decorations;
    private Selector selector;

    public SubscriptionBuilder() {
        identifier = Identifier.empty();
        decorations = new HashMap<>();
    }

    public SubscriptionBuilder<T> subscriber(Object subscriber) {
        Objects.requireNonNull(subscriber, "subscriber");
        this.subscriber = subscriber;
        return this;
    }

    public SubscriptionBuilder<T> identifier(Identifier identifier) {
        Objects.requireNonNull(identifier, "identifier");
        this.identifier = identifier;
        return this;
    }

    public SubscriptionBuilder<T> selector(Selector selector) {
        Objects.requireNonNull(selector, "selector");
        this.selector = selector;
        return this;
    }

    public SubscriptionBuilder<T> listener(Listener<T> listener) {
        Objects.requireNonNull(listener, "listener");
        this.listener = listener;
        return this;
    }

    public <R> SubscriptionBuilder<T> decorateListener(
            SubscriptionListenerDecorator<T, R> decorator) {
        //TODO: Verify listener has been set
        Class<R> decorationType = decorator.getDecorationType();
        R decoration = decorator.getDecoration();

        Objects.requireNonNull(decorationType, "decorationType");
        Objects.requireNonNull(decoration, "decoration");

        if (decorations.containsKey(decorationType)) {
            //TODO: Throw exception
        }

        listener = decorator.decorate(listener);
        decorations.put(decorationType, decoration);

        return this;
    }

    public Subscription<T> build() {
        validateBuilderIsComplete();

        return new ImmutableSubscription<>(
                subscriber,
                identifier,
                selector,
                listener,
                decorations);
    }

    private void validateBuilderIsComplete() {
        if (subscriber == null) {
            throw new IllegalStateException("subscriber has not been set, " +
                    "please set it using the #subscriber method.");
        } else if (selector == null) {
            throw new IllegalStateException("selector has not been set, " +
                    "please set it using the #selector method.");
        } else if (listener == null) {
            throw new IllegalStateException("listener has not been set, " +
                    "please set it using the #listener method.");
        }
    }
}
