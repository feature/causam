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

package pw.stamina.causam.registry;

import pw.stamina.causam.scan.SubscriberScanningStrategy;
import pw.stamina.causam.subscribe.Subscription;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface SubscriptionRegistry {

    boolean register(Subscription<?> subscription);

    boolean registerAll(Collection<Subscription<?>> subscriptions);

    boolean registerWith(Object subscriber, SubscriberScanningStrategy strategy);

    boolean unregister(Subscription<?> subscription);

    boolean unregisterFor(Object subscriber);

    boolean unregisterIf(Predicate<Subscription<?>> filter);

    boolean unregisterForIf(Object subscriber, Predicate<Subscription<?>> filter);

    Stream<Subscription<?>> findSubscriptions(Object subscriber);

    Stream<Subscription<?>> findAllSubscriptions();

    <T> Collection<Subscription<T>> selectSubscriptions(Class<T> key);

    static SubscriptionRegistry nullRejecting(SubscriptionRegistry registry) {
        Objects.requireNonNull(registry, "registry");
        return new NullRejectingSubscriptionRegistryDecorator(registry);
    }
}
