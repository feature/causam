package pw.stamina.causam.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pw.stamina.causam.publish.Publisher;
import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.subscribe.Subscription;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
final class StandardEventEmitterTests {

    @Mock private SubscriptionRegistry registry;
    @Mock private Publisher publisher;

    @Mock private Object event;
    @Mock private Collection<Subscription<Object>> subscriptions;

    private EventEmitter emitter;

    @BeforeEach
    void setupEmitter() {
        emitter = new StandardEventEmitter(registry, publisher);
    }

    @Test
    @DisplayName("emit(null) throws NullPointerException")
    void emit_givenNullEvent_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> emitter.emit(null));
    }

    @Test
    void emit() {
        Class<?> eventClass = event.getClass();

        when(registry.selectSubscriptions(notNull())).thenReturn(subscriptions);
        when(subscriptions.isEmpty()).thenReturn(true);

        boolean emitted = emitter.emit(event);
        assertFalse(emitted);

        verify(registry).selectSubscriptions(eventClass);
        verify(subscriptions).isEmpty();

        verify(publisher, never()).publish(any(), any());
    }
}
