package pw.stamina.causam.event;

import pw.stamina.causam.publish.Publisher;
import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.subscribe.Subscription;

import javax.inject.Inject;
import java.util.Collection;

public final class StandardEventEmitter implements EventEmitter {
    private final SubscriptionRegistry registry;
    private final Publisher publisher;

    @Inject
    StandardEventEmitter(SubscriptionRegistry registry, Publisher publisher) {
        this.registry = registry;
        this.publisher = publisher;
    }

    @Override
    public <T> boolean emit(T event) {
        @SuppressWarnings("unchecked")
        Class<T> key = (Class<T>) event.getClass();

        Collection<Subscription<T>> subscriptions = registry.selectSubscriptions(key);

        if (subscriptions.isEmpty()) {
            return false;
        }

        publisher.publish(event, subscriptions);
        return true;
    }
}
