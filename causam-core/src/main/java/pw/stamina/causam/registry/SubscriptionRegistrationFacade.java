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
import pw.stamina.causam.scan.result.ScanResult;
import pw.stamina.causam.subscribe.Subscription;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public final class SubscriptionRegistrationFacade {
    private final SubscriptionRegistry registry;

    @Inject
    public SubscriptionRegistrationFacade(SubscriptionRegistry registry) {
        this.registry = registry;
    }

    public boolean register(Subscription<?> subscription) {
        return registry.register(subscription);
    }

    public boolean registerAll(Collection<Subscription<?>> subscriptions) {
        return registry.registerAll(subscriptions);
    }

    public boolean registerWith(Object subscriber, SubscriberScanningStrategy strategy) {
        Objects.requireNonNull(subscriber, "subscriber");
        Objects.requireNonNull(strategy, "strategy");

        ScanResult scan = strategy.scan(subscriber);
        Set<Subscription<?>> subscriptions = scan.getSubscriptions();

        return registry.registerAll(subscriptions);
    }

    public boolean unregister(Subscription<?> subscription) {
        Objects.requireNonNull(subscription, "subscription");
        return registry.unregisterIf(Predicate.isEqual(subscription));
    }

    public boolean unregisterFor(Object subscriber) {
        Objects.requireNonNull(subscriber, "subscriber");
        return registry.unregisterIf(doesSubscriberMatch(subscriber));
    }

    public boolean unregisterForIf(Object subscriber, Predicate<Subscription<?>> filter) {
        Objects.requireNonNull(subscriber, "subscriber");
        Objects.requireNonNull(filter, "filter");

        return registry.unregisterIf(doesSubscriberMatch(subscriber).and(filter));
    }

    private static Predicate<Subscription<?>> doesSubscriberMatch(Object subscriber) {
        return subscription -> subscription.getSubscriber().equals(subscriber);
    }
}
