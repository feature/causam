/*
 * Copyright (c) 2017 Abstraction
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package pw.stamina.causam.bus;

import pw.stamina.causam.publish.ExceptionHandlingPublisherDecorator;
import pw.stamina.causam.publish.Publisher;
import pw.stamina.causam.publish.exception.PublicationExceptionHandler;
import pw.stamina.causam.publish.listen.Listener;
import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.select.KeySelector;
import pw.stamina.causam.subscribe.Subscription;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;

public final class StandardEventBus implements EventBus {

    private final String identifier;
    private final SubscriptionRegistry registry;

    private Publisher publisher;

    private ExceptionHandlingPublisherDecorator exceptionHandlingDecorator;

    @Inject
    StandardEventBus(String identifier,
                     SubscriptionRegistry registry,
                     Publisher publisher) {
        this.identifier = identifier;
        this.registry = registry;
        this.publisher = publisher;
    }

    @Override
    public Optional<String> getIdentifier() {
        return Optional.ofNullable(identifier);
    }

    @Override
    public SubscriptionRegistry getRegistry() {
        return registry;
    }

    //@Override
    public void addExceptionHandler(PublicationExceptionHandler handler) {
        if (exceptionHandlingDecorator == null) {
            exceptionHandlingDecorator = Publisher.exceptionHandling(publisher, handler);
        }

    }

    @Override
    public <T> boolean post(T event) {
        @SuppressWarnings("unchecked")
        Class<T> key = (Class<T>) event.getClass();

        Collection<Subscription<T>> subscriptions = registry.selectSubscriptions(key);

        if (subscriptions.isEmpty()) {
            return false;
        }

        publisher.publish(event, subscriptions);
        return true;
    }

    //@Override
    public <T> void on(Object subscriber, Class<T> eventType, Listener<T> listener) {
        registry.register(Subscription.<T>builder()
                .selector(KeySelector.exact(eventType))
                .subscriber(subscriber)
                .listener(listener)
                .build());
    }
}
