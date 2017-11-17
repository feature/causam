package pw.stamina.causam.select;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;

final class CachingSubscriptionSelectorServiceDecoratorTests {

    @Mock
    private SubscriptionSelectorService mockedService;
    private SubscriptionSelectorService cachingServiceDecorator;

    @BeforeEach
    void setupService() {
        MockitoAnnotations.initMocks(this);

        cachingServiceDecorator = CachingSubscriptionSelectorServiceDecorator.standard(mockedService);
    }

    @Test
    void getCacheInvalidator_shouldReturnNonEmptyOptional() {
        assertTrue(cachingServiceDecorator.getCacheInvalidator().isPresent());
    }
}
