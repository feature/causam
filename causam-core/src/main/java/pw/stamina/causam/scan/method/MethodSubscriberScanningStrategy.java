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

import pw.stamina.causam.scan.SubscriberScanningStrategy;
import pw.stamina.causam.scan.method.factory.MethodBasedSubscriptionFactory;
import pw.stamina.causam.scan.method.model.Subscriber;
import pw.stamina.causam.scan.method.validate.IllegalSubscriberMethodException;
import pw.stamina.causam.scan.method.validate.SubscriberMethodValidator;
import pw.stamina.causam.scan.result.ScanResult;
import pw.stamina.causam.scan.result.ScanResultBuilder;
import pw.stamina.causam.subscribe.Subscription;

import java.lang.reflect.Method;
import java.util.Objects;

public final class MethodSubscriberScanningStrategy implements SubscriberScanningStrategy {
    private static final MethodSubscriberScanningStrategy STANDARD =
            new MethodSubscriberScanningStrategy(
                    SubscriberMethodValidator.standard(),
                    MethodBasedSubscriptionFactory.standard());

    private final SubscriberMethodValidator validator;
    private final MethodBasedSubscriptionFactory subscriptionFactory;

    private MethodSubscriberScanningStrategy(SubscriberMethodValidator validator,
                                             MethodBasedSubscriptionFactory subscriptionFactory) {
        this.validator = validator;
        this.subscriptionFactory = subscriptionFactory;
    }

    @Override
    public ScanResult scan(Object subscriber) {
        Objects.requireNonNull(subscriber, "subscriber input cannot be null");
        Method[] methods = subscriber.getClass().getDeclaredMethods();

        // Is marked as Subscriber method
        // Is a valid Subscriber method
        // Extract event type parameter
        // Create subscription
        // Collect to ScanResult

        ScanResultBuilder resultBuilder = new ScanResultBuilder();
        for (Method method : methods) {
            if (isSubscriberMethod(method)) {
                continue;
            }

            try {
                validator.validate(method);
            } catch (IllegalSubscriberMethodException e) {
                e.printStackTrace();
                continue;
                //TODO: Handle
            }

            Class<?> eventType = method.getParameterTypes()[0];

            Subscription<?> subscription = subscriptionFactory.createSubscription(subscriber, method, eventType);
            resultBuilder.addSubscription(subscription);
        }

        return resultBuilder.build();
    }

    private boolean isSubscriberMethod(Method method) {
        return method.isAnnotationPresent(Subscriber.class);
    }

    public static MethodSubscriberScanningStrategy standard() {
        return MethodSubscriberScanningStrategy.STANDARD;
    }
}
