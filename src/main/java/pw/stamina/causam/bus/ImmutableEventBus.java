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
import pw.stamina.causam.registry.SubscriptionRegistrationFacade;
import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.select.SubscriptionSelectorService;
import pw.stamina.causam.subscribe.Subscription;

import java.util.Collection;

final class ImmutableEventBus implements EventBus {
    private final Identifier identifier;
    private final SubscriptionRegistry registry;
    private final SubscriptionSelectorService selector;
    private final Dispatcher dispatcher;

    ImmutableEventBus(Identifier identifier,
                      SubscriptionRegistry registry,
                      SubscriptionSelectorService selector,
                      Dispatcher dispatcher) {
        this.identifier = identifier;
        this.registry = registry;
        this.dispatcher = dispatcher;
        this.selector = selector;
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public SubscriptionRegistry getRegistry() {
        return registry;
    }

    @Override
    public SubscriptionRegistrationFacade getRegistrationFacade() {
        return SubscriptionRegistrationFacade.simple(registry);
    }

    @Override
    public <T> void post(T event) {
        @SuppressWarnings("unchecked")
        Class<T> key = (Class<T>) event.getClass();

        Collection<Subscription<T>> subscriptions = selector
                .selectSubscriptions(key, this.registry::findAllSubscriptions);

        if (subscriptions.isEmpty()) {
            return;
        }

        dispatcher.dispatch(event, subscriptions);
    }
}
