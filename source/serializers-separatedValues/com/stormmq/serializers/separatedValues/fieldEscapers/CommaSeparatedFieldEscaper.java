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

public final class CommaSeparatedFieldEscaper extends AbstractFieldEscaper
{
	private static final int DoubleQuote = '"';

	@NotNull
	public static final FieldEscaper CommaSeparatedFieldEscaperInstance = new CommaSeparatedFieldEscaper();

	@SuppressWarnings({"MagicCharacter", "HardcodedLineSeparator"})
	private CommaSeparatedFieldEscaper()
	{
		super(',', '\r', '\n');
	}

	@Override
	public void escape(@NotNull final String field, @NotNull final Writer writer) throws CouldNotWriteDataException
	{
		try
		{
			writer.write(DoubleQuote);
			for (int index = 0; index < field.length(); index++)
			{
				final int character = field.charAt(index);
				if (character == DoubleQuote)
				{
					writer.write(DoubleQuote);
				}
				writer.write(character);
			}
			writer.write(DoubleQuote);
		}
		catch (final IOException e)
		{
			throw new CouldNotWriteDataException(e);
		}
	}
}
