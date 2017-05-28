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

package pw.stamina.causam;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public final class IdentifierTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final String identifierValue = "identifier_value";
    private final Identifier identifier = Identifier.of(identifierValue);

    @Test
    public void testEmptyReturnsSameInstance() {
        assertSame(Identifier.empty(), Identifier.empty());
    }

    @Test
    public void testOfThrowsExceptionIfInputIsNull() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Identifier value cannot be null");

        Identifier.of(null);
    }

    @Test
    public void testOfThrowsExceptionIfInputIsEmpty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage( "Identifier value is empty. Use Identifier.empty() instead");

        Identifier.of("");
    }

    @Test
    public void testEqualsOnSameInstance() {
        assertEquals(identifier, identifier);
    }

    @Test
    public void testEqualsOnIdentifierWithEqualValue() {
        Identifier otherIdentifier = Identifier.of(identifierValue);

        assertEquals(identifier, otherIdentifier);
    }

    @Test
    public void testEqualsRejectsNulls() {
        assertNotEquals(identifier, null);
    }

    @Test
    public void testEqualsOnObjectInstance() {
        assertNotEquals(identifier, new Object());
    }

    @Test
    public void testEqualsOnOtherIdentifier() {
        Identifier otherIdentifier = Identifier.of("other");

        assertNotEquals(identifier, otherIdentifier);
    }

    @Test
    public void testHashCodeReturnsHashCodeOfIdentifierValue() {
        assertEquals(identifier.hashCode(), identifierValue.hashCode());
    }

    @Test
    public void testToStringReturnsIdentifierValue() {
        assertSame(identifier.toString(), identifierValue);
    }
}
