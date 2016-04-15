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

package com.stormmq.serializers.separatedValues;

import org.jetbrains.annotations.*;
import com.stormmq.serializers.CouldNotEncodeDataException;
import com.stormmq.serializers.CouldNotWriteDataException;
import com.stormmq.serializers.separatedValues.fieldEscapers.FieldEscaper;

import java.io.Writer;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public abstract class AbstractSeparatedValuesLine implements SeparatedValuesLine
{
	protected AbstractSeparatedValuesLine()
	{
	}

	@Override
	public final void recordValue(final int index, @NotNull @NonNls final String rawValue)
	{
		if (index < 0)
		{
			throw new IllegalArgumentException(format(ENGLISH, "index %1$s is out of range (index < 0)", index));
		}
		if (isFieldAlreadyRecorded(index))
		{
			throw new IllegalArgumentException(format(ENGLISH, "index %1$s has already been recorded", index));
		}
		store(index, rawValue);
	}

	protected abstract boolean isFieldAlreadyRecorded(final int index);

	protected abstract void store(final int index, @Nullable final String rawValue);

	@Nullable
	protected abstract String load(final int index);

	protected abstract int size();

	@Override
	public final void writeLine(@NotNull final Writer writer, @NotNull final FieldEscaper fieldEscaper) throws CouldNotWriteDataException, CouldNotEncodeDataException
	{
		final int size = size();
		if (size != 0)
		{
			writeField(writer, fieldEscaper, 0);

			for (int index = 1; index < size; index++)
			{
				fieldEscaper.writeFieldSeparator(writer);
				writeField(writer, fieldEscaper, index);
			}
		}

		fieldEscaper.writeLineEnding(writer);
	}

	private void writeField(final Writer writer, final FieldEscaper fieldEscaper, final int index) throws CouldNotWriteDataException, CouldNotEncodeDataException
	{
		@Nullable final String field = load(index);
		final String actualField = field == null ? "" : field;
		fieldEscaper.escape(actualField, writer);
	}
}
