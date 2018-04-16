package pw.stamina.causam.publish;

import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import pw.stamina.causam.publish.exception.PublicationException;
import pw.stamina.causam.publish.exception.PublicationExceptionHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
final class ExceptionHandlingPublisherDecoratorTests {

    private Publisher publisherDecorator;

    private @Mock Publisher publisher;
    private @Mock PublicationExceptionHandler exceptionHandler;

    @BeforeEach
    void setupExceptionHandlingPublisherDecorator() {
        publisherDecorator = new ExceptionHandlingPublisherDecorator(publisher, exceptionHandler);
    }

    @Test
    void publish_givenPublisherDoesNotThrowException_shouldExceptionHandlerNotThrowException() {
        publisherDecorator.publish(null, null);
        verify(publisher).publish(isNull(), isNull());
    }

    @Test
    void publish() {//Incomplete test, therefore the name might as well be incomplete :(
        doThrow(PublicationException.class).when(publisher).publish(any(), any());

        publisherDecorator.publish(null, null);
        verify(publisher).publish(isNull(), isNull());

        //TODO: Validate information in PublicationExceptionContext is correct
        //TODO: Last ArgumentMatcher is isNull because we don't yet provide context
        verify(exceptionHandler).handleException(notNull(), isNull());
    }
}
