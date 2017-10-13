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

package pw.stamina.causam.publish.listen.decorate.pause;

import pw.stamina.causam.publish.listen.Listener;
import pw.stamina.causam.publish.listen.decorate.AbstractInstanceAssociatedListenerDecorator;

/**
 * Decorates a {@link Listener} to adopt pausable functionality.
 *
 * @param <T> the event type accept by the decorated listener
 */
public final class PausableInstanceAssociatedListenerDecorator<T>
        extends AbstractInstanceAssociatedListenerDecorator<T, Pausable> {

    private PausableInstanceAssociatedListenerDecorator(Pausable pausable) {
        super(Pausable.class, pausable);
    }

    @Override
    public Listener<T> decorate(Listener<T> decorating) {
        return event -> {
            if (decoration.isPaused()) {
                return;
            }

            decorating.publish(event);
        };
    }

    public static <T> PausableInstanceAssociatedListenerDecorator<T> simple() {
        return new PausableInstanceAssociatedListenerDecorator<>(Pausable.simple());
    }

    public static <T> PausableInstanceAssociatedListenerDecorator<T> atomic() {
        return new PausableInstanceAssociatedListenerDecorator<>(Pausable.atomic());
    }
}