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

package pw.stamina.causam.select;

import pw.stamina.causam.subscribe.Subscription;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

//TODO
public final class ConcurrentCachingSubscriptionSelectorServiceDecorator implements SubscriptionSelectorService {
    private final SubscriptionSelectorService backingSelectorService;
    private final ConcurrentMap<Class<?>, Collection<Subscription<?>>>
            cachedFlattenedClassToSubscriptionsHierarchy;

    @Inject
    ConcurrentCachingSubscriptionSelectorServiceDecorator(SubscriptionSelectorService backingSelectorService) {
        this.backingSelectorService = backingSelectorService;
        cachedFlattenedClassToSubscriptionsHierarchy = new ConcurrentHashMap<>();
    }

    @Override
    public <T> Collection<Subscription<T>> selectSubscriptions(
            Class<T> key, Supplier<Stream<Subscription<?>>> subscriptions) {
        //TODO: Caching mechanism

        return backingSelectorService.selectSubscriptions(key, subscriptions);
    }

    @Override
    public Optional<SubscriptionCacheInvalidator> getCacheInvalidator() {
        return null;
    }

    /*
    @Override
    public void notifySubscriptionAdded(Subscription<?> subscription) {
        removeCachedEntries(subscription);
    }

    @Override
    public void notifySubscriptionsRemovedIf(Predicate<Subscription<?>> filter) {
        cachedFlattenedClassToSubscriptionsHierarchy.values()
                .removeIf(doesAnySubscriptionMatchFilter(filter));
    }

    private Predicate<Collection<Subscription<?>>> doesAnySubscriptionMatchFilter(
            Predicate<Subscription<?>> filter) {
        return subscriptions -> subscriptions.stream().anyMatch(filter);
    }

    private void removeCachedEntries(Subscription<?> subscription) {
        cachedFlattenedClassToSubscriptionsHierarchy.keySet()
                .removeIf(subscription.getKeySelector()::canSelect);
    }
    */
}
