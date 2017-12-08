package pw.stamina.causam.event;

import pw.stamina.causam.publish.Publisher;
import pw.stamina.causam.registry.SubscriptionRegistry;

import java.util.Objects;

public interface EventEmitter {

    <T> boolean emit(T event);

    static EventEmitter standard(SubscriptionRegistry registry, Publisher publisher) {
        Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(publisher, "publisher");
        return new StandardEventEmitter(registry, publisher);
    }
}
