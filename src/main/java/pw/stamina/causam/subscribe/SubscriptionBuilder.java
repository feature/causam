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

import pw.stamina.causam.listen.Listener;
import pw.stamina.causam.listen.SynchronizingListenerDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class SubscriptionBuilder<T> {
    private Object subscriber;
    private Listener<T> listener;
    private List<Predicate<T>> filters;

    private boolean synchronizing;

    public SubscriptionBuilder<T> subscriber(Object subscriber) {
        Objects.requireNonNull(subscriber, "subscriber");

        //TODO: Disallow setting another subscriber
        this.subscriber = subscriber;
        return this;
    }

    public SubscriptionBuilder<T> listener(Listener<T> listener) {
        Objects.requireNonNull(listener, "listener");

        //TODO: Disallow setting another listener
        this.listener = listener;
        return this;
    }

    public SubscriptionBuilder<T> addFilter(Predicate<T> filter) {
        Objects.requireNonNull(filter, "filter");

        if (this.filters == null) {
            this.filters = new ArrayList<>();
        }

        this.filters.add(filter);
        return this;
    }

    public SubscriptionBuilder<T> synchronizing() {
        if (this.synchronizing) {
            //TODO: Illegal state
        }

        this.synchronizing = true;
        return this;
    }

    public Subscription<T> build() {
        //TODO: Validate state

        return new ImmutableSubscription<>(
                this.subscriber,
                this.prepareListener(),
                this.filters);
    }

    private Listener<T> prepareListener() {
        if (this.synchronizing) {
            return SynchronizingListenerDecorator.of(this.listener);
        }

        return this.listener;
    }
}
