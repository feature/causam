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

package pw.stamina.causam;

import pw.stamina.causam.publish.PublicationCommand;
import pw.stamina.causam.publish.exception.PublicationExceptionHandler;
import pw.stamina.causam.registry.SubscriptionRegistrationFacade;
import pw.stamina.causam.registry.SubscriptionRegistry;

import java.util.concurrent.TimeUnit;

public final class ImmutableEventBus implements EventBus {
    private final String identifier;
    private final SubscriptionRegistry subscriptions;
    private final PublicationExceptionHandler exceptionHandler;

    ImmutableEventBus(String identifier,
                      SubscriptionRegistry subscriptions,
                      PublicationExceptionHandler exceptionHandler) {
        this.identifier = identifier;
        this.subscriptions = subscriptions;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public PublicationExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public SubscriptionRegistrationFacade getRegistrationFacade() {
        return null;
    }

    @Override
    public <T> void now(T event) {

    }

    @Override
    public <T> void async(T event) {

    }

    @Override
    public <T> void async(T event, long timeout, TimeUnit unit) {

    }

    @Override
    public <T> PublicationCommand<T> publish(T event) {
        return null;
    }
}
