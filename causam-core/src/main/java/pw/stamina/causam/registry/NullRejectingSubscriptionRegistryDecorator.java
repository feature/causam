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

package pw.stamina.causam.registry;

import pw.stamina.causam.subscribe.Subscription;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class NullRejectingSubscriptionRegistryDecorator implements SubscriptionRegistry {

    private final SubscriptionRegistry delegate;

    @Inject
    NullRejectingSubscriptionRegistryDecorator(SubscriptionRegistry delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean register(Subscription<?> subscription) {
        Objects.requireNonNull(subscription, "subscription");
        return delegate.register(subscription);
    }

    @Override
    public boolean registerAll(Collection<Subscription<?>> subscriptions) {
        validateSubscriptionsInput(subscriptions);
        return delegate.registerAll(subscriptions);
    }

    private void validateSubscriptionsInput(Collection<Subscription<?>> subscriptions) {
        Objects.requireNonNull(subscriptions, "subscriptions");
        subscriptions.forEach(subscription -> Objects.requireNonNull(subscription, "subscription is null"));
    }

    @Override
    public boolean unregisterIf(Predicate<Subscription<?>> filter) {
        Objects.requireNonNull(filter, "filter");
        return delegate.unregisterIf(filter);
    }

    @Override
    public Stream<Subscription<?>> findSubscriptions(Object subscriber) {
        Objects.requireNonNull(subscriber, "subscriber");
        return delegate.findSubscriptions(subscriber);
    }

    @Override
    public Stream<Subscription<?>> findAllSubscriptions() {
        return delegate.findAllSubscriptions();
    }

    @Override
    public <T> Collection<Subscription<T>> selectSubscriptions(Class<T> key) {
        return delegate.selectSubscriptions(key);
    }
}