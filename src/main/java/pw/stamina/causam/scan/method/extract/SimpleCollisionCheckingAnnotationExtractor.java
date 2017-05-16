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

package pw.stamina.causam.scan.method.extract;

import pw.stamina.causam.scan.method.model.Subscriber;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Predicate;

public class SimpleCollisionCheckingAnnotationExtractor<T extends Annotation>
        implements CollisionCheckingAnnotationExtractor<T> {
    private final Class<T> extractionTarget;
    private final Function<Subscriber, T> subscriberExtractor;
    private final Predicate<Subscriber> collisionChecker;

    SimpleCollisionCheckingAnnotationExtractor(
            Class<T> extractionTarget,
            Function<Subscriber, T> subscriberExtractor,
            Predicate<Subscriber> collisionChecker) {

        this.extractionTarget = extractionTarget;
        this.subscriberExtractor = subscriberExtractor;
        this.collisionChecker = collisionChecker;
    }

    @Override
    public final T extract(Method method) {
        Subscriber subscriber = method.getAnnotation(Subscriber.class);
        T extracted = method.getAnnotation(getExtractionTarget());

        if (extracted != null) {
            if (checkCollision(subscriber)) {
                //TODO: Collision happened
            }

            return extracted;
        }

        return extractFromSubscriber(subscriber);
    }

    @Override
    public final Class<T> getExtractionTarget() {
        return extractionTarget;
    }

    @Override
    public final T extractFromSubscriber(Subscriber subscriber) {
        return subscriberExtractor.apply(subscriber);
    }

    @Override
    public final boolean checkCollision(Subscriber subscriber) {
        return collisionChecker.test(subscriber);
    }
}
