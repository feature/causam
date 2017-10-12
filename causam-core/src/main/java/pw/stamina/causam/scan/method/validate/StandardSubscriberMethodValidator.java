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

package pw.stamina.causam.scan.method.validate;

import java.lang.reflect.Method;

enum StandardSubscriberMethodValidator implements SubscriberMethodValidator {
    INSTANCE;

    @Override
    public void validate(Method method) throws IllegalSubscriberMethodException {
        if (!methodHasOneParameter(method)) {
            throw new IllegalSubscriberMethodException(method,
                    "An annotated subscriber method may only have exactly " +
                            "1 parameter. Method: " + method);
        }

        //TODO: Check other method conditions?
        //TODO: Disallow synchronized methods, use annotation instead
    }

    private static boolean methodHasOneParameter(Method method) {
        return method.getParameterCount() == 1;
    }
}
