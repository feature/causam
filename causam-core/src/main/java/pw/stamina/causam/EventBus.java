package pw.stamina.causam;

import pw.stamina.causam.event.EventEmitter;
import pw.stamina.causam.registry.SubscriptionRegistry;

public interface EventBus {

    boolean register(Object subscriber);

    boolean unregister(Object subscriber);

    <T> boolean emit(T event);

    EventEmitter getEmitter();

    SubscriptionRegistry getRegistry();
}
