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

package pw.stamina.causam;

import pw.stamina.causam.publish.listen.Listener;

/**
 * An event may implement this interface to signal that is
 * it cancellable. If an event is cancelled, it should not
 * be {@link Listener#publish(Object) published} to listeners,
 * unless it is explicitly stated that that the listener
 * should ignores the cancelled state.
 */
public interface Cancellable {

    /**
     * Sets the cancelled state for the event.
     *
     * @param cancelled new cancelled state
     */
    void setCancelled(boolean cancelled);

    /**
     * Indicates the cancelled state of the event.
     *
     * @return <tt>true</tt> if the event is cancelled
     */
    boolean isCancelled();

    /**
     * Cancels the event.
     */
    void cancel();
}
