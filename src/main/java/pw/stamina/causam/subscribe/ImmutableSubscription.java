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

import pw.stamina.causam.listen.Listener;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

class ImmutableSubscription<T> implements Subscription<T> {
    protected final Object subscriber;
    private final Listener<T> listener;
    private boolean paused;

    private final List<Predicate<T>> filters;

    ImmutableSubscription(Object subscriber,
                          Listener<T> listener,
                          List<Predicate<T>> filters) {
        this.subscriber = subscriber;
        this.listener = listener;
        this.filters = filters;
    }

    @Override
    public Object getSubscriber() {
        return this.subscriber;
    }

    @Override
    public void call(T event) throws Exception {
        if (this.paused || !this.passesFilters(event)) {
            return;
        }

        this.listener.call(event);
    }

    private boolean passesFilters(T event) {
        if (!this.hasFilters()) {
            return true;
        }

        for (Predicate<T> filter : this.filters) {
            if (!filter.test(event)) {
                return false;
            }
        }

        return true;
    }

    private boolean hasFilters() {
        return this.filters != null;
    }

    @Override
    public List<Predicate<T>> getFilters() {
        return (this.filters != null)
                ? Collections.unmodifiableList(this.filters)
                : Collections.emptyList();
    }

    @Override
    public void pause() {
        this.paused = true;
    }

    @Override
    public void resume() {
        this.paused = false;
    }

    @Override
    public boolean isPaused() {
        return this.paused;
    }
}
