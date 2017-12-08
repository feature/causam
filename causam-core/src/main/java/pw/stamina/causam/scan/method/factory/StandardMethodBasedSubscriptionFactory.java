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

package pw.stamina.causam.scan.method.factory;

import pw.stamina.causam.publish.listen.Listener;
import pw.stamina.causam.publish.listen.decorate.ListenerDecorator;
import pw.stamina.causam.scan.method.model.RejectSubtypes;
import pw.stamina.causam.scan.method.model.Synchronize;
import pw.stamina.causam.select.KeySelector;
import pw.stamina.causam.subscribe.Subscription;
import pw.stamina.causam.subscribe.SubscriptionBuilder;

import java.lang.reflect.Method;

final class StandardMethodBasedSubscriptionFactory implements MethodBasedSubscriptionFactory {

    @Override
    public <T> Subscription<T> createSubscription(Object subscriber, Method method, Class<T> eventType) {
        assureAccessible(method);

        SubscriptionBuilder<T> builder = new SubscriptionBuilder<>();

        if (shouldSynchronizeListener(method)) {
            builder.decorate(ListenerDecorator.synchronizing());
        }

        builder.subscriber(subscriber)
                .selector(createSelectorFromMethod(method, eventType))
                .listener(createListener(subscriber, method));

        createDecorateAndSetListener(builder, subscriber, method);

        return builder.build();
    }

    private static void assureAccessible(Method method) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    private <T> KeySelector createSelectorFromMethod(Method method, Class<T> eventType) {
        if (doesListenerRejectSubtypes(method)) {
            return KeySelector.exact(eventType);
        } else {
            return KeySelector.acceptsSubtypes(eventType);
        }
    }

    private boolean doesListenerRejectSubtypes(Method method) {
        return method.isAnnotationPresent(RejectSubtypes.class);
    }

    private static <T> void createDecorateAndSetListener(SubscriptionBuilder<T> builder,
                                                         Object subscriber,
                                                         Method method) {
        builder.listener(createListener(subscriber, method));
    }

    private static <T> Listener<T> createListener(Object subscriber, Method target) {
        return new MethodInvokingListener<>(subscriber, target);
    }

    private static boolean shouldSynchronizeListener(Method method) {
        return method.isAnnotationPresent(Synchronize.class);
    }
}
