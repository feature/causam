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

import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.subscribe.Subscription;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

final class ModificationNotifyingSubscriptionRegistryDecorator
        implements SubscriptionRegistry {
    private final SubscriptionRegistry registry;
    private final CachingSubscriptionSelectorService selectorService;

    private ModificationNotifyingSubscriptionRegistryDecorator(
            SubscriptionRegistry registry,
            CachingSubscriptionSelectorService selectorService) {
        this.registry = registry;
        this.selectorService = selectorService;
    }

    @Override
    public boolean register(Subscription<?> subscription) {
        boolean registered = registry.register(subscription);

        if (registered) {
            selectorService.notifySubscriptionAdded(subscription);
        }

        return registered;
    }

    @Override
    public boolean registerAll(Collection<Subscription<?>> subscriptions) {
        boolean registered = registry.registerAll(subscriptions);

        if (registered) {
            subscriptions.forEach(selectorService::notifySubscriptionAdded);
        }

        return registered;
    }

    @Override
    public boolean unregisterIf(Predicate<Subscription<?>> filter) {
        boolean unregistered = registry.unregisterIf(filter);

        if (unregistered) {
            selectorService.notifySubscriptionsRemovedIf(filter);
        }

        return unregistered;
    }

    @Override
    public Stream<Subscription<?>> findSubscriptions(Object subscriber) {
        return registry.findSubscriptions(subscriber);
    }

    @Override
    public Stream<Subscription<?>> findAllSubscriptions() {
        return registry.findAllSubscriptions();
    }

    static ModificationNotifyingSubscriptionRegistryDecorator of(
            SubscriptionRegistry registry,
            CachingSubscriptionSelectorService selectorService) {
        Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(selectorService, "selectorService");
        return new ModificationNotifyingSubscriptionRegistryDecorator(registry, selectorService);
    }
}
