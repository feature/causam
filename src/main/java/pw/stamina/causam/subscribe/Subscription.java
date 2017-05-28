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

package pw.stamina.causam.subscribe;

import pw.stamina.causam.Identifier;
import pw.stamina.causam.publish.exception.PublicationException;
import pw.stamina.causam.select.Selector;

import java.util.Map;
import java.util.Optional;

/**
 * Defines an event subscription, its properties
 * and how events should be published to the
 * subscription listener.
 *
 * @param <T> the accepted event type
 */
public interface Subscription<T> {

    Object getSubscriber();

    Optional<Identifier> getIdentifier();

    Selector getSelector();

    void call(T event) throws PublicationException;

    <R> R getDecoration(Class<R> decorationType);

    <R> boolean hasDecoration(Class<R> decorationType);

    Map<Class<?>, ?> getDecorations();
}
