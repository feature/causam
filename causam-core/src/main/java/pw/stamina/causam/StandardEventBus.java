package pw.stamina.causam;

import pw.stamina.causam.event.EventEmitter;
import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.scan.method.MethodSubscriberScanningStrategy;

import javax.inject.Inject;

public final class StandardEventBus implements EventBus {
    private final SubscriptionRegistry registry;
    private final EventEmitter emitter;

    @Inject
    StandardEventBus(SubscriptionRegistry registry, EventEmitter emitter) {
        this.registry = registry;
        this.emitter = emitter;
    }

    @Override
    public boolean register(Object subscriber) {
        return registry.registerWith(subscriber, MethodSubscriberScanningStrategy.standard());
    }

    @Override
    public boolean unregister(Object subscriber) {
        return registry.unregisterAll(subscriber);
    }

    @Override
    public <T> boolean emit(T event) {
        return emitter.emit(event);
    }

    @Override
    public EventEmitter getEmitter() {
        return emitter;
    }

    @Override
    public SubscriptionRegistry getRegistry() {
        return registry;
    }
}
