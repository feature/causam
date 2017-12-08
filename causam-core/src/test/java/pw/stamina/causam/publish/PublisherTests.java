package pw.stamina.causam.publish;

import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import pw.stamina.causam.publish.exception.PublicationExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
final class PublisherTests {

    private @Mock Publisher publisher;
    private @Mock PublicationExceptionHandler exceptionHandler;

    @Test
    @DisplayName("Publisher.immediate() instanceof ImmediatePublisher")
    void immediate_shouldReturnInstanceOfImmediatePublisher() {
        Publisher publisher = Publisher.immediate();
        assertTrue(publisher instanceof ImmediatePublisher);
    }

    @Test
    @DisplayName("Publisher.exceptionHandling(null, null) throws NullPointerException")
    void exceptionHandling() {
        assertThrows(NullPointerException.class, () -> Publisher.exceptionHandling(null, exceptionHandler));
        assertThrows(NullPointerException.class, () -> Publisher.exceptionHandling(publisher, null));
    }

    @Test
    @DisplayName("Publisher.exceptionHandling(publisher, exceptionHandler) instanceof ExceptionHandlingPublisherDecorator")
    void exceptionHandling_() {
        Publisher publisherDecorator = Publisher.exceptionHandling(publisher, exceptionHandler);
        assertTrue(publisherDecorator instanceof ExceptionHandlingPublisherDecorator);
    }
}
