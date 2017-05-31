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

package pw.stamina.causam.bus;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pw.stamina.causam.Identifier;

import java.util.UUID;

public final class EventBusTests {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void builder_IdentifierInputIsNull_shouldThrownNullPointerException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("identifier");

        EventBus.builder(null);
    }

    @Test
    public void builder_identifierWithEmptyValue_shouldCreateTestWithSpecifiedIdentifier() {
        EventBus.builder(Identifier.empty());
    }

    @Test
    public void builder_identifierWithRandomUUIDAsValue_shouldCreateTestWithSpecifiedIdentifier() {
        EventBus.builder(Identifier.of(UUID.randomUUID().toString()));
    }
}
