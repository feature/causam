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

import pw.stamina.causam.EventBus;
import pw.stamina.causam.event.EventEmitter;
import pw.stamina.causam.publish.Publisher;
import pw.stamina.causam.registry.SetBasedSubscriptionRegistry;
import pw.stamina.causam.registry.SubscriptionRegistry;
import pw.stamina.causam.scan.method.model.Subscriber;
import pw.stamina.causam.select.CachingSubscriptionSelectorServiceDecorator;
import pw.stamina.causam.select.SubscriptionSelectorService;

public final class Demo {

    public static void main(String[] args) {
        EventBus eventBus = createEventBus();

        eventBus.emit("Test message 1");
        Demo object = new Demo();
        eventBus.register(object);

        eventBus.emit("Test message 1");
        eventBus.emit("Test message 2");
    }

    private static EventBus createEventBus() {
        SubscriptionSelectorService selectorService =
                CachingSubscriptionSelectorServiceDecorator.standard(SubscriptionSelectorService.simple());
        SubscriptionRegistry registry = SetBasedSubscriptionRegistry.concurrentHash(selectorService);

        Publisher publisher = Publisher.immediate();
        EventEmitter emitter = EventEmitter.standard(registry, publisher);

        return EventBus.standard(registry, emitter);
    }

    @Subscriber
    private void printString(Object message) {
        System.out.println(message);
    }
}
