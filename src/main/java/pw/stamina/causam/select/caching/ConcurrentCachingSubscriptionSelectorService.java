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

package pw.stamina.causam.select.caching;

import pw.stamina.causam.subscribe.Subscription;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ConcurrentCachingSubscriptionSelectorService
        extends AbstractCachingSubscriptionSelectorService {
    private final ConcurrentMap<Class<?>, List<Subscription<?>>>
            cachedFlattenedClassToSubscriptionsHierarchy;

    ConcurrentCachingSubscriptionSelectorService() {
        this.cachedFlattenedClassToSubscriptionsHierarchy = new ConcurrentHashMap<>();
    }

    @Override
    public <T> List<Subscription<T>> selectSubscriptions(
            Class<T> key, Supplier<Stream<Subscription<?>>> subscriptions) {

        List<?> selected = cachedFlattenedClassToSubscriptionsHierarchy
                .computeIfAbsent(key, k -> selectSubscriptions(subscriptions.get(), k));

        return (List<Subscription<T>>) selected;
    }

    private List<Subscription<?>> selectSubscriptions(
            Stream<Subscription<?>> subscriptions,
            Class<?> key) {

        List<Subscription<?>> selectedSubscriptions = subscriptions
                        .filter(canSubscriberSelect(key))
                        .collect(Collectors.toList());

        return wrapSelectedSubscriptions(selectedSubscriptions);
    }

    private List<Subscription<?>> wrapSelectedSubscriptions(
            List<Subscription<?>> selectedSubscriptions) {
        return selectedSubscriptions.isEmpty()
                ? Collections.emptyList()
                : Collections.unmodifiableList(selectedSubscriptions);
    }

    private Predicate<Subscription<?>> canSubscriberSelect(Class<?> key) {
        return subscription -> subscription.getSelector().canSelect(key);
    }

    @Override
    public void notifySubscriptionAdded(Subscription<?> subscription) {
        removeCachedEntries(subscription);
    }

    @Override
    public void notifySubscriptionRemoved(Subscription<?> subscription) {
        removeCachedEntries(subscription);
    }

    private void removeCachedEntries(Subscription<?> subscription) {
        cachedFlattenedClassToSubscriptionsHierarchy.keySet()
                .removeIf(subscription.getSelector()::canSelect);
    }
}
