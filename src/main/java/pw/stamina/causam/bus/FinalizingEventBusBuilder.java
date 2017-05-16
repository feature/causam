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
import pw.stamina.causam.publish.exception.PublicationExceptionHandler;
import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.select.SubscriptionSelectorService;
import pw.stamina.causam.select.caching.CachingSubscriptionSelectorService;

import java.util.Objects;

/**
 * Thus the class name, this implementation has nothing to do with
 * Java {@link Object#finalize() finalizers}. The name refers to
 * the behavior of this builder. Once a component has been declared
 * through its respective method, it may not be changed. Additional
 * to that, this builder is non-reusable.
 */
final class FinalizingEventBusBuilder implements EventBus.Builder {
    private Identifier identifier;
    private PublicationExceptionHandler exceptionHandler;
    private SubscriptionRegistry registry;
    private boolean built;

    private SubscriptionSelectorService selector;
    private boolean caching;

    public FinalizingEventBusBuilder() {
        this.identifier = Identifier.empty();
    }

    @Override
    public EventBus.Builder identifier(String identifier) {
        //TODO: Enforce no re-setting fields behavior
        this.identifier = Identifier.of(identifier);
        return this;
    }

    @Override
    public EventBus.Builder registry(SubscriptionRegistry registry) {
        //TODO: Enforce no re-setting fields behavior
        //TODO: Validate input
        this.registry = registry;
        return this;
    }

    @Override
    public EventBus.Builder exceptionHandler(PublicationExceptionHandler exceptionHandler) {
        //TODO: Enforce no re-setting fields behavior
        //TODO: Validate input
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    public EventBus.Builder selector(SubscriptionSelectorService selector) {
        //TODO: Enforce no re-setting fields behavior
        //TODO: Validate input
        this.selector = selector;
        return this;
    }

    @Override
    public EventBus.Builder cachingSelector(CachingSubscriptionSelectorService selector) {
        //TODO: Enforce no re-setting fields behavior
        //TODO: Validate input
        this.selector = selector;
        caching = true;
        return this;
    }

    @Override
    public EventBus build() {
        enforceNonReusability();
        validateBuilderVariablesHasBeenSet();

        decorateRegistryIfUsingCachingSelectorService();

        return new ImmutableEventBus(
                identifier,
                registry,
                selector);
    }

    private void enforceNonReusability() {
        if (built) {
            throw new IllegalStateException(
                    "This EventBus builder is not " +
                            "reusable. Please create a new builder instead.");
        } else {
            built = true;
        }
    }

    private void validateBuilderVariablesHasBeenSet() {
        //TODO: Implement validation
        Objects.requireNonNull(registry, "register must be defined before the build method is called");
    }

    private void decorateRegistryIfUsingCachingSelectorService() {
        if (caching) {
            CachingSubscriptionSelectorService cachingSelector =
                    (CachingSubscriptionSelectorService) selector;

            registry = cachingSelector.decorateRegistry(registry);
        }
    }
}
