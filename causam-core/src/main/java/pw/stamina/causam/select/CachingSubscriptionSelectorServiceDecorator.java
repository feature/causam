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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class CachingSubscriptionSelectorServiceDecorator implements SubscriptionSelectorService {
    private final SubscriptionSelectorService backingSelectorService;
    private final SubscriptionCacheInvalidatorImpl cacheInvalidator;
    private final Map<Class<?>, Collection<Subscription<?>>> cachedSubscriptions;

    @Inject
    CachingSubscriptionSelectorServiceDecorator(SubscriptionSelectorService backingSelectorService,
                                                Map<Class<?>, Collection<Subscription<?>>> cachedSubscriptions) {
        this.backingSelectorService = backingSelectorService;
        this.cachedSubscriptions = cachedSubscriptions;
        this.cacheInvalidator = new SubscriptionCacheInvalidatorImpl();
    }

    @Override
    public <T> Collection<Subscription<T>> selectSubscriptions(
            Class<T> key, Supplier<Stream<Subscription<?>>> subscriptions) {
        return uncheckedCast(cachedSubscriptions.computeIfAbsent(key, selectSubscriptions(subscriptions)));
    }

    private Function<Class<?>, Collection<Subscription<?>>> selectSubscriptions(
            Supplier<Stream<Subscription<?>>> subscriptions) {
        return key -> uncheckedCast(backingSelectorService.selectSubscriptions(key, subscriptions));
    }

    @SuppressWarnings("unchecked")
    private <T> T uncheckedCast(Object obj) {
        return (T) obj;
    }

    @Override
    public Optional<SubscriptionCacheInvalidator> getCacheInvalidator() {
        return Optional.of(cacheInvalidator);
    }

    private final class SubscriptionCacheInvalidatorImpl implements SubscriptionCacheInvalidator {

        @Override
        public void invalidateFor(Subscription<?> subscription) {
            cachedSubscriptions.values().removeIf(contains(subscription));
        }

        @Override
        public void invalidateForAll(Collection<Subscription<?>> subscriptions) {
            cachedSubscriptions.values().removeIf(containsAny(subscriptions));
        }

        @Override
        public void invalidateIf(Predicate<Subscription<?>> filter) {
            cachedSubscriptions.values().removeIf(anyMatch(filter));
        }

    }

    private static Predicate<Collection<Subscription<?>>> contains(Subscription<?> subscription) {
        return subscriptions -> subscriptions.contains(subscription);
    }

    private static Predicate<Collection<Subscription<?>>> containsAny(Collection<Subscription<?>> subscriptions) {
        return cachedSubscriptionsValue -> !Collections.disjoint(cachedSubscriptionsValue, subscriptions);
    }

    private static Predicate<Collection<Subscription<?>>> anyMatch(Predicate<Subscription<?>> filter) {
        return subscriptions -> subscriptions.stream().anyMatch(filter);
    }

    public static SubscriptionSelectorService concurrent(SubscriptionSelectorService backingSelectorService) {
        return new CachingSubscriptionSelectorServiceDecorator(backingSelectorService, new ConcurrentHashMap<>());
    }

    public static SubscriptionSelectorService standard(SubscriptionSelectorService backingSelectorService) {
        return new CachingSubscriptionSelectorServiceDecorator(backingSelectorService, new HashMap<>());
    }
}
