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

package pw.stamina.causam.scan.method;

import pw.stamina.causam.subscribe.listen.Listener;
import pw.stamina.causam.scan.method.model.Synchronize;
import pw.stamina.causam.subscribe.Subscription;
import pw.stamina.causam.subscribe.SubscriptionBuilder;
import pw.stamina.causam.subscribe.listen.decorate.SynchronizingListenerDecorator;
import pw.stamina.causam.subscribe.listen.decorate.pause.PausableSubscriptionListenerDecorator;

import java.lang.reflect.Method;

enum StandardMethodBasedSubscriptionFactory
        implements MethodBasedSubscriptionFactory {
    INSTANCE;

    @Override
    public Subscription<?> createSubscription(Object subscriber,
                                              Method method) {
        assureAccessible(method);

        SubscriptionBuilder<?> builder = new SubscriptionBuilder<>();

        if (shouldSynchronizeListener(method)) {
            builder.decorate(SynchronizingListenerDecorator.get());
        }

        return builder
                .subscriber(subscriber)
                .listener(createListener(subscriber, method))
                .build();
    }

    private static void assureAccessible(Method method) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    private static boolean shouldSynchronizeListener(Method method) {
        return method.isAnnotationPresent(Synchronize.class);
    }

    private static <T> Listener<T> createListener(Object subscriber, Method target) {
        return new MethodInvokingListener<>(subscriber, target);
    }
}