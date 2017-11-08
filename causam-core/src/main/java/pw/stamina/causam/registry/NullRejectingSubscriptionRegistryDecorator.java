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

import pw.stamina.causam.scan.SubscriberScanningStrategy;
import pw.stamina.causam.subscribe.Subscription;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class NullRejectingSubscriptionRegistryDecorator implements SubscriptionRegistry {
    private final SubscriptionRegistry registry;

    @Inject
    NullRejectingSubscriptionRegistryDecorator(SubscriptionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public boolean register(Subscription<?> subscription) {
        Objects.requireNonNull(subscription, "subscription");
        return registry.register(subscription);
    }

    @Override
    public boolean registerAll(Collection<Subscription<?>> subscriptions) {
        validateSubscriptionsInput(subscriptions);
        return registry.registerAll(subscriptions);
    }

    private void validateSubscriptionsInput(Collection<Subscription<?>> subscriptions) {
        Objects.requireNonNull(subscriptions, "subscriptions");
        subscriptions.forEach(Objects::requireNonNull);
    }

    @Override
    public boolean registerWith(Object subscriber, SubscriberScanningStrategy strategy) {
        Objects.requireNonNull(subscriber, "subscriber");
        Objects.requireNonNull(strategy, "strategy");

        return registry.registerWith(subscriber, strategy);
    }

    @Override
    public boolean unregister(Subscription<?> subscription) {
        Objects.requireNonNull(subscription, "subscription");
        return registry.register(subscription);
    }

    @Override
    public boolean unregisterFor(Object subscriber) {
        Objects.requireNonNull(subscriber, "subscriber");
        return registry.unregisterFor(subscriber);
    }

    @Override
    public boolean unregisterIf(Predicate<Subscription<?>> filter) {
        Objects.requireNonNull(filter, "filter");
        return registry.unregisterIf(filter);
    }

    @Override
    public boolean unregisterForIf(Object subscriber, Predicate<Subscription<?>> filter) {
        Objects.requireNonNull(subscriber, "subscriber");
        Objects.requireNonNull(filter, "filter");
        return registry.unregisterForIf(subscriber, filter);
    }

    @Override
    public Stream<Subscription<?>> findSubscriptions(Object subscriber) {
        Objects.requireNonNull(subscriber, "subscriber");
        return registry.findSubscriptions(subscriber);
    }

    @Override
    public Stream<Subscription<?>> findAllSubscriptions() {
        return registry.findAllSubscriptions();
    }

    @Override
    public <T> Collection<Subscription<T>> selectSubscriptions(Class<T> key) {
        // Not validate for optimization reasons
        return registry.selectSubscriptions(key);
    }
}
