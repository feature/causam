package pw.stamina.causam;

import pw.stamina.causam.event.EventEmitter;
import pw.stamina.causam.registry.SubscriptionRegistry;

import java.util.Objects;

public interface EventBus {

    boolean register(Object subscriber);

    boolean unregister(Object subscriber);

    <T> boolean emit(T event);

    EventEmitter getEmitter();

    SubscriptionRegistry getRegistry();

    static EventBus standard(SubscriptionRegistry registry, EventEmitter emitter) {
        Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(emitter, "emitter");
        return new StandardEventBus(registry, emitter);
    }
}
