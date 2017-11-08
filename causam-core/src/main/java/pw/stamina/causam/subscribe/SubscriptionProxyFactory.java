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

import pw.stamina.causam.publish.listen.decorate.ListenerDecoratorContainer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//TODO: Replace with a non-reflection based implementation instead?
public final class SubscriptionProxyFactory {

    public static <T> Subscription<T> createSubscriptionProxy(
            Subscription<T> subscription,
            ListenerDecoratorContainer<T> decorators) {

        ClassLoader classLoader = Subscription.class.getClassLoader();
        Class<?>[] interfaces = createInterfacesArray(decorators);
        Map<Class<?>, ?> interfaceToInstanceAsMap = decorators.extractDecorationsInterfaceToInstanceAsMap();

        SubscriptionProxyInvocationHandler<T> handler =
                new SubscriptionProxyInvocationHandler<>(subscription, interfaceToInstanceAsMap);

        Object proxy = Proxy.newProxyInstance(classLoader, interfaces, handler);

        @SuppressWarnings("unchecked")
        Subscription<T> proxiedSubscription = (Subscription<T>) proxy;

        return proxiedSubscription;
    }

    private static <T> Class<?>[] createInterfacesArray(ListenerDecoratorContainer<T> decorators) {
        List<Class<?>> interfaces = new LinkedList<>();

        interfaces.add(Subscription.class);
        decorators.extractInterfacesFromDecorators().forEach(interfaces::add);

        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }

    private static class SubscriptionProxyInvocationHandler<T> implements InvocationHandler {
        private final Subscription<T> subscription;
        private final Map<Class<?>, ?> interfaceToInstanceAsMap;

        private SubscriptionProxyInvocationHandler(Subscription<T> subscription,
                                                   Map<Class<?>, ?> interfaceToInstanceAsMap) {
            this.subscription = subscription;
            this.interfaceToInstanceAsMap = new IdentityHashMap<>(interfaceToInstanceAsMap);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Class<?> declaringClass = method.getDeclaringClass();
            Object handle = findHandleByDeclaringClass(declaringClass);
            assert handle != null;

            return method.invoke(handle, args);
        }

        private Object findHandleByDeclaringClass(Class<?> declaringClass) {
            if (declaringClass == Subscription.class) {
                return subscription;
            }

            return interfaceToInstanceAsMap.get(declaringClass);
        }
    }
}
