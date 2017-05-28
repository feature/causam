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
import pw.stamina.causam.publish.dispatch.ExceptionHandlingDispatcherDecorator;
import pw.stamina.causam.publish.exception.PublicationExceptionHandler;
import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.select.SubscriptionSelectorService;
import pw.stamina.causam.select.caching.CachingSubscriptionSelectorService;

import java.util.Objects;
import java.util.function.Supplier;

final class LazyEventBusBuilder implements EventBus.Builder {
    private static final Supplier<PublicationExceptionHandler>
            DEFAULT_NULL_RETURNING_EXCEPTION_HANDLER_SUPPLIER = () -> null;

    private final Identifier identifier;
    private Supplier<SubscriptionRegistry> registry;
    private Supplier<SubscriptionSelectorService> selector;
    private Supplier<Dispatcher> dispatcher;
    private Supplier<PublicationExceptionHandler> exceptionHandler =
            DEFAULT_NULL_RETURNING_EXCEPTION_HANDLER_SUPPLIER;

    public LazyEventBusBuilder(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public EventBus.Builder registry(Supplier<SubscriptionRegistry> registry) {
        Objects.requireNonNull(registry, "registry");
        this.registry = registry;
        return this;
    }

    @Override
    public EventBus.Builder selector(Supplier<SubscriptionSelectorService> selector) {
        Objects.requireNonNull(registry, "registry");
        this.selector = selector;
        return this;
    }

    @Override
    public EventBus.Builder dispatcher(Supplier<Dispatcher> dispatcher) {
        Objects.requireNonNull(dispatcher, "registry");
        this.dispatcher = dispatcher;
        return this;
    }

    @Override
    public EventBus.Builder exceptionHandler(
            Supplier<PublicationExceptionHandler> exceptionHandler) {
        Objects.requireNonNull(registry, "registry");
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    public EventBus build() {
        validateBuilderVariablesHasBeenSet();

        return new LazyEventBusBuilderWorker(
                registry.get(),
                selector.get(),
                dispatcher.get(),
                exceptionHandler.get()
        ).build();
    }

    private void validateBuilderVariablesHasBeenSet() {
        //TODO: Implement validation
        Objects.requireNonNull(registry, "register must be defined before the build method is called");
    }

    private final class LazyEventBusBuilderWorker {
        private final SubscriptionRegistry registry;
        private final SubscriptionSelectorService selector;
        private final Dispatcher dispatcher;
        private final PublicationExceptionHandler exceptionHandler;

        LazyEventBusBuilderWorker(SubscriptionRegistry registry,
                                  SubscriptionSelectorService selector,
                                  Dispatcher dispatcher,
                                  PublicationExceptionHandler exceptionHandler) {
            this.registry = registry;
            this.selector = selector;
            this.dispatcher = dispatcher;
            this.exceptionHandler = exceptionHandler;
        }

        private EventBus build() {

            return new ImmutableEventBus(
                    identifier,
                    prepareRegistry(),
                    selector,
                    prepareDispatcher());
        }

        private SubscriptionRegistry prepareRegistry() {

            if (isCachingSelectorService()) {
                return applyCachingSelectorServiceRegistryDecoration();
            } else {
                return registry;
            }
        }

        private boolean isCachingSelectorService() {
            return selector instanceof CachingSubscriptionSelectorService;
        }

        private SubscriptionRegistry applyCachingSelectorServiceRegistryDecoration() {

            CachingSubscriptionSelectorService cachingSelector =
                    (CachingSubscriptionSelectorService) selector;

            return cachingSelector.decorateRegistry(registry);
        }

        private Dispatcher prepareDispatcher() {
            if (isExceptionHandlerPresent()) {
                return applyExceptionHandlingDispatcherDecoration();
            } else {
                return dispatcher;
            }
        }

        private boolean isExceptionHandlerPresent() {
            return exceptionHandler != null;
        }

        private Dispatcher applyExceptionHandlingDispatcherDecoration() {
            return ExceptionHandlingDispatcherDecorator.of(dispatcher, exceptionHandler);
        }
    }
}
