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

import pw.stamina.causam.select.SubscriptionSelectorService;
import pw.stamina.causam.subscribe.Subscription;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class CopyOnWriteSubscriptionRegistry
        implements SubscriptionRegistry {

    private final Set<Subscription<?>> subscriptions;
    private final SubscriptionSelectorService selectorService;

    public CopyOnWriteSubscriptionRegistry(SubscriptionSelectorService selectorService) {
        this.selectorService = selectorService;
        subscriptions = new CopyOnWriteArraySet<>();
    }

    @Override
    public boolean register(Subscription<?> subscription) {
        if (subscriptions.add(subscription)) {
            selectorService.notifySubscriptionAdded(subscription);
            return true;
        }

        return false;
    }

    @Override
    public boolean registerAll(Collection<Subscription<?>> subscriptions) {
        validateSubscriptionsInput(subscriptions);

        subscriptions.removeAll(this.subscriptions);
        if (subscriptions.isEmpty()) {
            return false;
        }

        this.subscriptions.addAll(subscriptions);
        subscriptions.forEach(selectorService::notifySubscriptionAdded);
        return true;
    }

    private void validateSubscriptionsInput(
            Collection<Subscription<?>> subscriptions) {
        Objects.requireNonNull(subscriptions, "subscriptions");
        subscriptions.forEach(subscription ->
                Objects.requireNonNull(subscription, "subscription is null"));
    }

    @Override
    public boolean unregisterIf(Predicate<Subscription<?>> filter) {

        return false;
    }

    @Override
    public Stream<Subscription<?>> findSubscriptions(Object subscriber) {
        Objects.requireNonNull(subscriber, "subscriber");

        return subscriptions.stream()
                .filter(subscription ->
                        subscription.getSubscriber() == subscriber);
    }

    @Override
    public <T> Collection<Subscription<T>> selectSubscriptions(Class<T> key) {
        return selectorService.selectSubscriptions(key, () ->
                Collections.unmodifiableSet(subscriptions));
    }
}
