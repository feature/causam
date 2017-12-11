package pw.stamina.causam.registry;

import pw.stamina.causam.scan.SubscriberScanningStrategy;
import pw.stamina.causam.scan.result.ScanResult;
import pw.stamina.causam.select.SubscriptionCacheInvalidator;
import pw.stamina.causam.select.SubscriptionSelectorService;
import pw.stamina.causam.subscribe.Subscription;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractSubscriptionRegistry implements SubscriptionRegistry {
    private final SubscriptionSelectorService selectorService;

    protected AbstractSubscriptionRegistry(SubscriptionSelectorService selectorService) {
        Objects.requireNonNull(selectorService, "selectorService");
        this.selectorService = selectorService;
    }

    @Override
    public boolean registerWith(Object subscriber, SubscriberScanningStrategy strategy) {
        ScanResult scan = strategy.scan(subscriber);
        Set<Subscription> subscriptions = scan.getSubscriptions();

        return registerAll(subscriptions);
    }

    @Override
    public Stream<Subscription> findSubscriptions(Object subscriber) {
        return findAllSubscriptions().filter(doesSubscriberMatch(subscriber));
    }

    @Override
    public <T> Collection<Subscription<T>> selectSubscriptions(Class<T> key) {
        return selectorService.selectSubscriptions(key, this::findAllSubscriptions);
    }

    private static Predicate<Subscription> doesSubscriberMatch(Object subscriber) {
        return subscription -> subscription.getSubscriber() == subscriber;
    }

    protected void invalidateCache(Consumer<SubscriptionCacheInvalidator> invalidatorAction) {
        selectorService.getCacheInvalidator().ifPresent(invalidatorAction);
    }
}
