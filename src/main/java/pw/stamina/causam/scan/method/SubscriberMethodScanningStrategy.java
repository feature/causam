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

import pw.stamina.causam.listen.Listener;
import pw.stamina.causam.scan.SubscriberScanningStrategy;
import pw.stamina.causam.scan.result.ScanResult;
import pw.stamina.causam.scan.result.ScanResultCollector;
import pw.stamina.causam.subscribe.Subscription;
import pw.stamina.causam.subscribe.SubscriptionBuilder;

import java.lang.reflect.Method;
import java.util.Arrays;

public final class SubscriberMethodScanningStrategy
        implements SubscriberScanningStrategy {

    @Override
    public ScanResult scan(Object subscriber)
            throws IllegalSubscriberMethodException {
        Method[] methods = subscriber.getClass().getDeclaredMethods();

        return Arrays.stream(methods)
                .filter(this::isSubscriberMethod)
                .peek(this::assureAccessible)
                .peek(this::validateSubscriberMethod)
                .map(method -> this.createSubscription(subscriber, method))
                .collect(ScanResultCollector.INSTANCE);
    }

    private boolean isSubscriberMethod(Method method) {
        return method.isAnnotationPresent(Subscriber.class);
    }

    private void assureAccessible(Method method) {
        if (method.isAccessible()) {
            return;
        }

        method.setAccessible(true);
    }

    private void validateSubscriberMethod(Method method)
            throws IllegalSubscriberMethodException {
        if (!methodHasOneParameter(method)) {
            throw new IllegalSubscriberMethodException(method,
                    "An annotated subscriber method may only have exactly " +
                            "1 parameter. Method: " + method.getName() + " in " +
                            "class: " + method.getDeclaringClass());
        }

        //TODO: Check other method conditions?
        //TODO: Disallow synchronized methods, use annotation instead
    }

    private Subscription<?> createSubscription(Object subscriber,
                                               Method listener) {
        SubscriptionBuilder<?> builder = new SubscriptionBuilder<>();

        if (this.shouldSynchronizeListener(listener)) {
            builder.synchronizing();
        }

        return builder
                .subscriber(subscriber)
                .listener(this.createListener(subscriber, listener))
                .build();
    }

    private boolean shouldSynchronizeListener(Method method) {
        return method.isAnnotationPresent(Synchronize.class);
    }

    private <T> Listener<T> createListener(Object subscriber, Method listener) {
        return new MethodInvokingListener<>(subscriber, listener);
    }

    private boolean methodHasOneParameter(Method method) {
        return method.getParameterCount() != 1;
    }
}
