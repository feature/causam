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

/**
 * Provides pausable behavior to an object. How the
 * behavior is affected by the paused state, should
 * be specified in the documentation of the implementing
 * class.
 */
public interface Pausable {

    /**
     * Indicates if this object is paused.
     *
     * @return <tt>true</tt> if paused, otherwise returns <tt>false</tt>
     */
    boolean isPaused();

    /**
     * Pauses this object.
     */
    void pause();

    /**
     * Resumes this object.
     */
    void resume();

    /**
     * Creates a new simple {@code Pausable}. This implementation
     * is based on a primitive {@code boolean}, and therefore does
     * not guarantee to be safe in multi-threaded environments. If
     * a thread safe implementation is required please use {@link
     * #atomic()} instead.
     *
     * @return a new simple {@code Pausable}
     * @see Pausable#atomic()
     */
    static Pausable simple() {
        return new SimplePausable();
    }

    /**
     * Creates a new atomic {@code Pausable}. This guarantees
     * thread safe behavior. If this behavior is not necessary
     * please use {@link #simple()} instead for improved
     * performance.
     *
     * @return a new atomic {@code Pausable}
     * @see Pausable#simple()
     */
    static Pausable atomic() {
        return new AtomicPausable();
    }
}
