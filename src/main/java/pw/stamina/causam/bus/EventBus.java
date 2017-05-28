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

import pw.stamina.causam.Identifier;
import pw.stamina.causam.publish.dispatch.Dispatcher;
import pw.stamina.causam.publish.exception.PublicationExceptionHandler;
import pw.stamina.causam.registry.SubscriptionRegistrationFacade;
import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.select.SubscriptionSelectorService;

import java.util.Objects;
import java.util.function.Supplier;

public interface EventBus {

    Identifier getIdentifier();

    SubscriptionRegistry getRegistry();

    SubscriptionRegistrationFacade getRegistrationFacade();

    <T> void post(T event);

    interface Builder {

        Builder registry(Supplier<SubscriptionRegistry> registry);

        Builder exceptionHandler(Supplier<PublicationExceptionHandler> exceptionHandler);

        Builder selector(Supplier<SubscriptionSelectorService> selector);

        Builder dispatcher(Supplier<Dispatcher> dispatcher);

        EventBus build();
    }

    static Builder builder(Identifier identifier) {
        Objects.requireNonNull(identifier, "identifier");
        return new LazyEventBusBuilder(identifier);
    }
}
