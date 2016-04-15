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

package com.stormmq.serializers.separatedValues.fieldEscapers;

import org.jetbrains.annotations.NotNull;
import com.stormmq.serializers.CouldNotWriteDataException;

import java.io.IOException;
import java.io.Writer;

import static java.util.Arrays.copyOf;

public abstract class AbstractFieldEscaper implements FieldEscaper
{
	private final int fieldSeparator;
	@NotNull
	private final char[] endOfLineSequence;

	protected AbstractFieldEscaper(final char fieldSeparator, @NotNull final char... endOfLineSequence)
	{
		final int length = endOfLineSequence.length;
		if (length == 0)
		{
			throw new IllegalArgumentException("endOfLineSequence can not be empty");
		}
		this.fieldSeparator = fieldSeparator;
		this.endOfLineSequence = copyOf(endOfLineSequence, length);
	}

	@Override
	public final void writeFieldSeparator(@NotNull final Writer writer) throws CouldNotWriteDataException
	{
		try
		{
			writer.write(fieldSeparator);
		}
		catch (final IOException e)
		{
			throw new CouldNotWriteDataException(e);
		}
	}

	@Override
	public final void writeLineEnding(@NotNull final Writer writer) throws CouldNotWriteDataException
	{
		try
		{
			writer.write(endOfLineSequence);
		}
		catch (final IOException e)
		{
			throw new CouldNotWriteDataException(e);
		}
	}
}
