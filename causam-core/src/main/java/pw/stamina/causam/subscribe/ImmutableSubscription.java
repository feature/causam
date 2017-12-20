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

import pw.stamina.causam.publish.exception.PublicationException;
import pw.stamina.causam.publish.listen.Listener;
import pw.stamina.causam.select.KeySelector;

import java.util.Optional;

final class ImmutableSubscription<T> implements Subscription<T> {
    private final Object subscriber;
    private final String identifier;
    private final KeySelector keySelector;
    private final boolean ignoreCancelled;
    private final Listener<T> listener;

    ImmutableSubscription(Object subscriber,
                          String identifier,
                          KeySelector keySelector,
                          boolean ignoreCancelled,
                          Listener<T> listener) {
        this.subscriber = subscriber;
        this.identifier = identifier;
        this.keySelector = keySelector;
        this.ignoreCancelled = ignoreCancelled;
        this.listener = listener;
    }

    @Override
    public Object getSubscriber() {
        return subscriber;
    }

    @Override
    public Optional<String> getIdentifier() {
        return Optional.ofNullable(identifier);
    }

    @Override
    public KeySelector getKeySelector() {
        return keySelector;
    }

    @Override
    public boolean ignoreCancelled() {
        return ignoreCancelled;
    }

    @Override
    public void publish(T event) throws PublicationException {
        listener.publish(event);
    }
}
