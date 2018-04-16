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

package pw.stamina.causam.scan.result;

import pw.stamina.causam.subscribe.Subscription;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class ScanResultCollector implements Collector<Subscription<?>, ScanResultBuilder, ScanResult> {

    @Override
    public Supplier<ScanResultBuilder> supplier() {
        return ScanResultBuilder::new;
    }

    @Override
    public BiConsumer<ScanResultBuilder, Subscription<?>> accumulator() {
        return ScanResultBuilder::addSubscription;
    }

    @Override
    public BinaryOperator<ScanResultBuilder> combiner() {
        return (left, right) -> {
            ScanResult rightResult = right.build();
            rightResult.getSubscriptions().forEach(left::addSubscription);
            return left;
        };
    }

    @Override
    public Function<ScanResultBuilder, ScanResult> finisher() {
        return ScanResultBuilder::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}