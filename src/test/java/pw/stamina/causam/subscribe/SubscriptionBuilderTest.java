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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pw.stamina.causam.subscribe.listen.Listener;

@RunWith(MockitoJUnitRunner.class)
public final class SubscriptionBuilderTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    @Mock
    private Listener<Object> listener;

    private SubscriptionBuilder<?> builder;

    @Before
    public void setupBuilder() {
        builder = new SubscriptionBuilder<>();
    }

    //TODO: Test decorateListener
    //TODO: Test build method


    @Test
    public void testSubscriberThrowsExceptionIfInputIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("subscriber");

        builder.subscriber(null);
    }

    @Test
    public void testIdentifierThrowsExceptionIfInputIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("identifier");

        builder.identifier(null);
    }

    @Test
    public void testSelectorThrowsExceptionIfInputIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("selector");

        builder.selector(null);
    }

    @Test
    public void testListenerThrowsExceptionIfInputIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("listener");

        builder.listener(null);
    }
}
