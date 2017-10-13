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

package pw.stamina.causam.publish;

import pw.stamina.causam.publish.exception.PublicationException;
import pw.stamina.causam.publish.exception.PublicationExceptionHandler;
import pw.stamina.causam.subscribe.Subscription;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

public final class ExceptionHandlingPublisherDecorator implements Publisher {
    private final Publisher publisher;
    private final List<PublicationExceptionHandler> exceptionHandlers;

    @Inject
    ExceptionHandlingPublisherDecorator(Publisher publisher,
                                        PublicationExceptionHandler exceptionHandler) {
        this.publisher = publisher;
        this.exceptionHandlers = new LinkedList<>();
        addExceptionHandler(exceptionHandler);
    }

    @Override
    public <T> void publish(T event, Iterable<Subscription<T>> subscriptions) {
        try {
            publisher.publish(event, subscriptions);
        } catch (PublicationException e) {
            handleException(e);
        }
    }

    public void addExceptionHandler(PublicationExceptionHandler exceptionHandler) {
        exceptionHandlers.add(exceptionHandler);
    }

    private void handleException(PublicationException e) {
        //TODO: Provide PublicationExceptionContext
        exceptionHandlers.forEach(handler -> handler.handleException(e, null));
    }
}