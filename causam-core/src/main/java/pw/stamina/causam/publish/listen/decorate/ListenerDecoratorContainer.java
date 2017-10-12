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

package pw.stamina.causam.publish.listen.decorate;

import pw.stamina.causam.publish.listen.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ListenerDecoratorContainer<T> {
    private final List<ListenerDecorator<T>> decorators;

    public ListenerDecoratorContainer() {
        this.decorators = new ArrayList<>();
    }

    public void add(ListenerDecorator<T> decorator) {
        //TODO: Disallow colliding decorators

        decorators.add(decorator);
    }

    public Listener<T> applyDecorationsToListener(Listener<T> listener) {
        for (ListenerDecorator<T> decorator : decorators) {
            listener = decorator.decorate(listener);
        }

        return listener;
    }

    public boolean shouldCreateProxy() {
        return decorators.stream().anyMatch(InstanceAssociatedListenerDecorator.class::isInstance);
    }

    public Stream<Class<?>> extractInterfacesFromDecorators() {
        return findInstanceAssociatedDecorators()
                .map(InstanceAssociatedListenerDecorator::getDecorationType);
    }

    public Map<Class<?>, Object> extractDecorationsInterfaceToInstanceAsMap() {
        return findInstanceAssociatedDecorators()
                .collect(Collectors.toMap(
                        InstanceAssociatedListenerDecorator::getDecorationType,
                        InstanceAssociatedListenerDecorator::getDecoration
                ));
    }

    private Stream<InstanceAssociatedListenerDecorator<T, Object>> findInstanceAssociatedDecorators() {
        return decorators.stream()
                .filter(InstanceAssociatedListenerDecorator.class::isInstance)
                .map(decorator -> (InstanceAssociatedListenerDecorator<T, Object>) decorator);
    }
}
