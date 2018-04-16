package pw.stamina.causam.publish;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pw.stamina.causam.subscribe.Subscription;

import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
final class ImmediatePublisherTest<T> {

    private Publisher publisher;

    @Mock private T event;

    @BeforeEach
    void setupPublisher() {
        publisher = new ImmediatePublisher();
    }

    @Test
    void publish_givenEmptyIterableOfSubscriptions_shouldCallForEachOnSubscriptions(@Mock Iterable<Subscription<T>> subscriptions) {
        publisher.publish(event, subscriptions);
        verify(subscriptions).forEach(notNull());
    }

    @Test
    void publish(@Mock Subscription<T> subscription) {
        Set<Subscription<T>> subscriptions = Collections.singleton(subscription);

        publisher.publish(event, subscriptions);
        verify(subscription).publish(event);
    }
}
