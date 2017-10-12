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

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import java.util.stream.Stream;

//TODO: clean up invalidation methods?
public final class CopyOnWriteSubscriptionRegistry implements SubscriptionRegistry {
    private final Set<Subscription<?>> subscriptions;
    private final SubscriptionSelectorService selectorService;

    @Inject
    CopyOnWriteSubscriptionRegistry(SubscriptionSelectorService selectorService) {
        this.subscriptions = new CopyOnWriteArraySet<>();
        this.selectorService = selectorService;
    }

    @Override
    public boolean register(Subscription<?> subscription) {
        selectorService.getCacheInvalidator().ifPresent(invalidator -> invalidator.invalidateFor(subscription));
        return subscriptions.add(subscription);
    }

    @Override
    public boolean registerAll(Collection<Subscription<?>> subscriptions) {
        selectorService.getCacheInvalidator().ifPresent(invalidator -> invalidator.invalidateForAll(subscriptions));
        return this.subscriptions.addAll(subscriptions);
    }

    @Override
    public boolean unregisterIf(Predicate<Subscription<?>> filter) {
        selectorService.getCacheInvalidator().ifPresent(invalidator -> invalidator.invalidateIf(filter));
        return subscriptions.removeIf(filter);
    }

    @Override
    public Stream<Subscription<?>> findSubscriptions(Object subscriber) {
        //TODO: Considering extracting
        return findAllSubscriptions().filter(subscription -> subscription.getSubscriber() == subscriber);
    }

    @Override
    public Stream<Subscription<?>> findAllSubscriptions() {
        return subscriptions.stream();
    }

    @Override
    public <T> Collection<Subscription<T>> selectSubscriptions(Class<T> key) {
        return selectorService.selectSubscriptions(key, this::findAllSubscriptions);
    }
}
