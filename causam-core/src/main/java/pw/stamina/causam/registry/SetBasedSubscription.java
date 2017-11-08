package pw.stamina.causam.registry;

import pw.stamina.causam.select.SubscriptionSelectorService;
import pw.stamina.causam.subscribe.Subscription;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class SetBasedSubscription extends AbstractSubscriptionRegistry {
    private final Set<Subscription<?>> subscriptions;

    @Inject
    SetBasedSubscription(SubscriptionSelectorService selectorService,
                         Set<Subscription<?>> subscriptions) {
        super(selectorService);
        this.subscriptions = subscriptions;
    }

    @Override
    public boolean register(Subscription<?> subscription) {
        invalidateCache(invalidator -> invalidator.invalidateFor(subscription));
        return subscriptions.add(subscription);
    }

    @Override
    public boolean registerAll(Collection<Subscription<?>> subscriptions) {
        invalidateCache(invalidator -> invalidator.invalidateForAll(subscriptions));
        return this.subscriptions.addAll(subscriptions);
    }

    @Override
    public boolean unregister(Subscription<?> subscription) {
        invalidateCache(invalidator -> invalidator.invalidateFor(subscription));
        return subscriptions.remove(subscription);
    }

    @Override
    public boolean unregisterIf(Predicate<Subscription<?>> filter) {
        invalidateCache(invalidator -> invalidator.invalidateIf(filter));
        return subscriptions.removeIf(filter);
    }

    @Override
    public Stream<Subscription<?>> findAllSubscriptions() {
        return subscriptions.stream();
    }

    public static SubscriptionRegistry copyOnWrite(SubscriptionSelectorService selectorService) {
        return new SetBasedSubscription(selectorService, new CopyOnWriteArraySet<>());
    }

    public static SubscriptionRegistry hash(SubscriptionSelectorService selectorService) {
        return new SetBasedSubscription(selectorService, new HashSet<>());
    }
}
