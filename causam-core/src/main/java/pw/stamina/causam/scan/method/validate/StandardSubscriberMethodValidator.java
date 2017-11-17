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
import java.lang.reflect.Modifier;

final class StandardSubscriberMethodValidator implements SubscriberMethodValidator {

    @Override
    public void validate(Method method) throws IllegalSubscriberMethodException {
        String exceptionMessage = _validate(method);

        if (exceptionMessage != null) {
            throw new IllegalSubscriberMethodException(method, exceptionMessage);
        }
    }

    public String _validate(Method method) {//TODO: Refactor method name
        if (!methodHasOneParameter(method)) {
            return "subscriber methods must have exactly 1 parameter";
        } else if (isStaticMethod(method)) {
            return "subscriber methods cannot be static";
        } else if (isSynchronizedMethod(method)) {
            return "subscriber methods cannot be synchronized, use the @Synchronize annotation instead";
        }

        return null;
    }

    private static boolean methodHasOneParameter(Method method) {
        return method.getParameterCount() == 1;
    }

    private static boolean isStaticMethod(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    private static boolean isSynchronizedMethod(Method method) {
        return Modifier.isSynchronized(method.getModifiers());
    }
}
