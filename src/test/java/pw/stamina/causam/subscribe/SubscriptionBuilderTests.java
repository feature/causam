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

import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import pw.stamina.causam.publish.listen.Listener;
import pw.stamina.causam.select.KeySelector;

@ExtendWith(MockitoExtension.class)
final class SubscriptionBuilderTests {

    @Mock private Listener<Object> listener;
    @Mock private KeySelector keySelector;
    @Mock private Object subscriber;

    //@Mock
    //private ListenerDecorator<Object, Object> dummyListenerDecorator;

    private SubscriptionBuilder<Object> builder;

    @BeforeEach
    void setupBuilderAndListenerDecorator() {
        builder = new SubscriptionBuilder<>();
        //when(dummyListenerDecorator.getDecorationType()).thenReturn(Object.class);
        //when(dummyListenerDecorator.getDecoration()).thenReturn(new Object());
    }

    //TODO: Test decorateListener
    //TODO: Test build method

//    @Test
//    public void decorateListener_inputDecoratorIsNull_shouldThrowExceptionIndicatingDecoratorIsNull() {
//        thrown.expect(NullPointerException.class);
//        thrown.expectMessage("decorator");
//
//        builder.listener(listener);
//        builder.decorateListener(null);
//    }

//    @Test
//    public void decorateListener_decoratorDecorationTypeIsNull_shouldThrowExceptionIndicatingTheDecoratorDecorationTypeIsNull() {
//        thrown.expect(NullPointerException.class);
//        thrown.expectMessage("decorationType");
//
//        when(dummyListenerDecorator.getDecorationType()).thenReturn(null);
//
//        builder.listener(listener);
//        builder.decorateListener(dummyListenerDecorator);
//    }

//    @Test
//    public void decorateListener_decoratorDecorationIsNull_shouldThrowExceptionIndicatingTheDecoratorDecorationIsNull() {
//        thrown.expect(NullPointerException.class);
//        thrown.expectMessage("decoration");
//
//        when(dummyListenerDecorator.getDecoration()).thenReturn(null);
//
//        builder.listener(listener);
//        builder.decorateListener(dummyListenerDecorator);
//    }

//    @Test
//    public void decorateListener_withoutDeclaredListener_shouldThrowExceptionStatingTheListenerHasNotBeenDeclared() {
//        thrown.expect(IllegalStateException.class);
//        thrown.expectMessage("listener has not been set, " +
//                "please set it using the #listener method.");
//
//        builder.decorateListener(dummyListenerDecorator);
//    }

//    @Test
//    public void subscriber_inputSubscriberIsNull_shouldThrowExceptionIndicatingTheSubscriberIsNull() {
//        thrown.expect(NullPointerException.class);
//        thrown.expectMessage("subscriber");
//
//        builder.subscriber(null);
//    }

    @Test
    void subscriber_inputSubscriberIsNotNull_shouldNotThrowAnyException() {
        builder.subscriber(subscriber);
    }

    //@Test
    //public void identifier_inputIdentifierIsNull_shouldThrowExceptionIndicatingTheIdentifierIsNull() {
    //    thrown.expect(NullPointerException.class);
    //    thrown.expectMessage("identifier");
//
    //    builder.identifier(null);
    //}

    @Test
    void identifier_inputIdentifierIsNotNull_shouldNotThrowAnyException() {
        builder.identifier("");
    }

//    @Test
//    public void selector_inputSelectorIsNull_shouldThrowExceptionIndicatingTheSelectorIsNull() {
//        thrown.expect(NullPointerException.class);
//        thrown.expectMessage("keySelector");
//
//        builder.keySelector(null);
//    }

    @Test
    void selector_inputSelectorIsNotNull_shouldNotThrowAnyException() {
        builder.selector(keySelector);
    }

//    @Test
//    public void listener_inputListenerIsNull_shouldThrowExceptionIndicatingTheListenerIsNull() {
//        thrown.expect(NullPointerException.class);
//        thrown.expectMessage("listener");
//
//        builder.listener(null);
//    }

    @Test
    void listener_inputListenerIsNotNull_shouldNotThrowAnyException() {
        builder.listener(listener);
    }
}
