package pw.stamina.causam.publish.exception;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class CompositePublicationExceptionHandler implements PublicationExceptionHandler {
    private final List<PublicationExceptionHandler> exceptionHandlers;

    @Inject
    CompositePublicationExceptionHandler(List<PublicationExceptionHandler> exceptionHandlers) {
        this.exceptionHandlers = exceptionHandlers;
    }

    @Override
    public void handleException(PublicationException exception,
                                PublicationExceptionContext context) {
        exceptionHandlers.forEach(handler -> handler.handleException(exception, context));
    }

    public static CompositePublicationExceptionHandler from(PublicationExceptionHandler... handlers) {
        Objects.requireNonNull(handlers, "handlers");
        Arrays.stream(handlers).forEach(Objects::requireNonNull);

        return new CompositePublicationExceptionHandler(Arrays.asList(handlers));
    }
}
