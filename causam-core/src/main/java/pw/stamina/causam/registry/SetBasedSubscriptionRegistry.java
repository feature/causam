package pw.stamina.causam.registry;

import pw.stamina.causam.select.SubscriptionSelectorService;
import pw.stamina.causam.subscribe.Subscription;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Stream;

public final class SetBasedSubscriptionRegistry extends AbstractSubscriptionRegistry {
    private final Set<Subscription<?>> subscriptions;

    @Inject
    SetBasedSubscriptionRegistry(SubscriptionSelectorService selectorService,
                                 Set<Subscription<?>> subscriptions) {
        super(selectorService);
        this.subscriptions = subscriptions;
    }

    @Override
    public boolean register(Subscription<?> subscription) {
        boolean registered = subscriptions.add(subscription);
        invalidateCache(invalidator -> invalidator.invalidate(subscription));
        return registered;
    }

    @Override
    public boolean registerAll(Collection<Subscription<?>> subscriptions) {
        boolean registeredAny = this.subscriptions.addAll(subscriptions);
        invalidateCache(invalidator -> invalidator.invalidateAll(subscriptions));
        return registeredAny;
    }

    @Override
    public boolean unregister(Subscription<?> subscription) {
        boolean unregistered = subscriptions.remove(subscription);
        invalidateCache(invalidator -> invalidator.invalidate(subscription));
        return unregistered;
    }

    @Override
    public boolean unregisterAll(Object subscriber) {
        boolean unregisteredAny = subscriptions.removeIf(subscription -> subscription.getSubscriber() == subscriber);
        invalidateCache(invalidator -> invalidator.invalidate(subscriber));
        return unregisteredAny;
    }

    @Override
    public Stream<Subscription<?>> findAllSubscriptions() {
        return subscriptions.stream();
    }

    public static SubscriptionRegistry copyOnWrite(SubscriptionSelectorService selectorService) {
        return new SetBasedSubscriptionRegistry(selectorService, new CopyOnWriteArraySet<>());
    }

    public static SubscriptionRegistry concurrentHash(SubscriptionSelectorService selectorService) {
        return new SetBasedSubscriptionRegistry(selectorService, ConcurrentHashMap.newKeySet());
    }

    public static SubscriptionRegistry hash(SubscriptionSelectorService selectorService) {
        return new SetBasedSubscriptionRegistry(selectorService, new HashSet<>());
    }
}
