// The MIT License (MIT)
//
// Copyright © 2016, Raphael Cohn <raphael.cohn@stormmq.com>
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.stormmq.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.*;

public interface ValueSerializer
{
	@SuppressWarnings("MethodCanBeVariableArityMethod")
	<S extends MapSerializable> void writeValue(@NotNull final S[] values) throws CouldNotWriteValueException;

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	<S extends ValueSerializable> void writeValue(@NotNull final S[] values) throws CouldNotWriteValueException;

	void writeValue(@NotNull final List<?> values) throws CouldNotWriteValueException;

	void writeValue(@NotNull final Set<?> values) throws CouldNotWriteValueException;

	void writeValue(final int value) throws CouldNotWriteValueException;

	void writeValue(final long value) throws CouldNotWriteValueException;

	void writeValue(final boolean value) throws CouldNotWriteValueException;

	void writeValue(@NotNull final BigDecimal value) throws CouldNotWriteValueException;

	void writeValue(@NotNull final String value) throws CouldNotWriteValueException;

	void writeValue(@NotNull final MapSerializable value) throws CouldNotWriteValueException;

	void writeValue(@NotNull final ValueSerializable value) throws CouldNotWriteValueException;

	void writeValue(@Nullable final Object value) throws CouldNotWriteValueException;

	void writeValue(@NotNull final UUID value) throws CouldNotWriteValueException;

	void writeValueNull() throws CouldNotWriteValueException;
}
