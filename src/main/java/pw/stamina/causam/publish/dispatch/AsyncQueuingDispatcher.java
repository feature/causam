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

package pw.stamina.causam.publish.dispatch;

import pw.stamina.causam.subscribe.Subscription;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

final class AsyncQueuingDispatcher implements Dispatcher {
    private final Queue<SubscriptionWithEventContainer<?>> queued;

    AsyncQueuingDispatcher() {
        queued = new ConcurrentLinkedQueue<>();
    }

    @Override
    public <T> void dispatch(T event, Iterable<Subscription<T>> subscriptions) {
        subscriptions.forEach(subscription ->
                queued.offer(new SubscriptionWithEventContainer<>(subscription, event)));

        SubscriptionWithEventContainer<?> e;
        while ((e = queued.poll()) != null) {
            e.call();
        }
    }

    public void shutdown() {

    }

    private static final class SubscriptionWithEventContainer<T> {
        private final Subscription<T> subscription;
        private final T event;

        public SubscriptionWithEventContainer(Subscription<T> subscription,
                                              T event) {
            this.subscription = subscription;
            this.event = event;
        }

        void call() {
            subscription.call(event);
        }
    }
}
