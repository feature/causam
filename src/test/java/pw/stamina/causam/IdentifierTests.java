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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

public final class IdentifierTests {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final String identifierValue = "identifier_value";
    private final Identifier identifier = Identifier.of(identifierValue);

    @Test
    public void empty_shouldReturnTheSameIdentifierObject() {
        assertThat(Identifier.empty(), sameInstance(Identifier.empty()));
    }

    @Test
    public void of_inputValueIsNull_ThrowsException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Identifier value cannot be null");

        Identifier.of(null);
    }

    @Test
    public void of_inputValueIsEmpty_ThrowsExceptionAndSuggestsEmptyIdentifierSingleton() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage( "Identifier value is empty. Use Identifier.empty() instead");

        Identifier.of("");
    }

    @Test
    public void equals_parameterIsSameIdentifierInstance_shouldReturnTrue() {
        assertThat(identifier.equals(identifier), is(true));
    }

    @Test
    public void equals_otherIdentifierWithEqualValue_shouldReturnTrue() {
        Identifier otherIdentifier = Identifier.of(identifierValue);

        assertThat(identifier.equals(otherIdentifier), is(true));
    }

    @Test
    public void equals_parameterIsNull_shouldReturnFalse() {
        assertThat(identifier.equals(null), is(false));
    }

    @Test
    public void equals_objectTypeOtherThanIdentifier_shouldReturnFalse() {
        assertThat(identifier.equals(new Object()), is(false));
        assertThat(identifier.equals(identifierValue), is(false));
    }

    @Test
    public void equals_IdentifierWithDifferentValue_shouldReturnFalse() {
        Identifier otherIdentifier = Identifier.of("other");

        assertThat(identifier.equals(otherIdentifier), is(false));
    }

    @Test
    public void hashCode_shouldReturnHashCodeOfIdentifierValue() {
        assertThat(identifier.hashCode(), is(identifierValue.hashCode()));
    }

    @Test
    public void toString_shouldReturnSameStringAsSpecifiedInConstructor() {
        assertThat(identifier.toString(), sameInstance(identifierValue));
    }
}
