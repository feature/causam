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

package pw.stamina.causam.scan.method.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class SimpleFilterCreationService implements FilterCreationService {

    //TODO: Validate filters can be applied

    @Override
    public <T> List<Predicate<T>> createFilters(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();

        List<?> filters = Arrays.stream(annotations)
                .filter(this::isAnnotationAnEventFilter)
                .map(this::getFilterAnnotationFromAnnotation)
                .map(this::createFilterFactoryFromClass)
                .filter(Objects::nonNull)
                .map(factory -> factory.createFilter(method))
                .collect(Collectors.toList());

        @SuppressWarnings("unchecked")
        List<Predicate<T>> castedFilters = (List<Predicate<T>>) filters;

        return castedFilters;
    }

    private boolean isAnnotationAnEventFilter(Annotation annotation) {
        return annotation.annotationType().isAnnotationPresent(Filter.class);
    }

    private Filter getFilterAnnotationFromAnnotation(Annotation annotation) {
        return annotation.annotationType().getAnnotation(Filter.class);
    }

    private FilterFactory<?> createFilterFactoryFromClass(Filter filter) {
        Class<? extends FilterFactory<?>> factoryClass = filter.value();

        try {
            return factoryClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }
}
