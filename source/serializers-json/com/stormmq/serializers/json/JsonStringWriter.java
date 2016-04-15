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

package com.stormmq.serializers.json;

import com.stormmq.string.Padding;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.stormmq.serializers.CouldNotWriteDataException;

import java.io.IOException;
import java.io.Writer;

@SuppressWarnings("ConstantNamingConvention")
public final class JsonStringWriter
{
	private static final char[] x00 = replacement(0x00);
	private static final char[] x01 = replacement(0x01);
	private static final char[] x02 = replacement(0x02);
	private static final char[] x03 = replacement(0x03);
	private static final char[] x04 = replacement(0x04);
	private static final char[] x05 = replacement(0x05);
	private static final char[] x06 = replacement(0x06);
	private static final char[] x07 = replacement(0x07);
	private static final char[] x08 = replacement('b');
	private static final char[] x09 = replacement('t');
	private static final char[] x0A = replacement('n');
	private static final char[] x0B = replacement(0x0B);
	private static final char[] x0C = replacement('f');
	private static final char[] x0D = replacement('r');
	private static final char[] x0E = replacement(0x0E);
	private static final char[] x0F = replacement(0x0F);
	private static final char[] x10 = replacement(0x10);
	private static final char[] x11 = replacement(0x11);
	private static final char[] x12 = replacement(0x12);
	private static final char[] x13 = replacement(0x13);
	private static final char[] x14 = replacement(0x14);
	private static final char[] x15 = replacement(0x15);
	private static final char[] x16 = replacement(0x16);
	private static final char[] x17 = replacement(0x17);
	private static final char[] x18 = replacement(0x18);
	private static final char[] x19 = replacement(0x19);
	@SuppressWarnings("HardcodedFileSeparator") private static final char[] ReverseSolidus = replacement('/');
	@SuppressWarnings("HardcodedFileSeparator") private static final char[] Solidus = replacement('\\');
	private static final char[] DoubleQuote = replacement('"');

	@SuppressWarnings("HardcodedFileSeparator")
	private static char[] replacement(final int controlCode)
	{
		return replacement("\\u" + Padding.padAsHexadecimal(controlCode, 4));
	}

	@SuppressWarnings("HardcodedFileSeparator")
	private static char[] replacement(final char symbol)
	{
		return replacement("\\" + symbol);
	}

	private static char[] replacement(@NonNls final String value)
	{
		return value.toCharArray();
	}

	@NotNull
	private final Writer writer;

	public JsonStringWriter(@NotNull final Writer writer)
	{
		this.writer = writer;
	}

	public void writeString(@NotNull final CharSequence value) throws CouldNotWriteDataException
	{
		// JSON encodes strings as UTF-16 surrogates pairs, not code points
		final int length = value.length();
		for (int index = 0; index < length; index++)
		{
			final char character = value.charAt(index);
			writeCharacter(character);
		}
	}

	@SuppressWarnings({
	"SwitchStatementWithTooManyBranches",
	"OverlyComplexMethod",
	"OverlyLongMethod",
	"MagicNumber",
	"MagicCharacter"
	, "HardcodedFileSeparator"
	})
	private void writeCharacter(final char character) throws CouldNotWriteDataException
	{
		final char[] replacement;
		switch (character)
		{
			case 0x00:
				replacement = x00;
				break;

			case 0x01:
				replacement = x01;
				break;

			case 0x02:
				replacement = x02;
				break;

			case 0x03:
				replacement = x03;
				break;

			case 0x04:
				replacement = x04;
				break;

			case 0x05:
				replacement = x05;
				break;

			case 0x06:
				replacement = x06;
				break;

			case 0x07:
				replacement = x07;
				break;

			case 0x08:
				replacement = x08;
				break;

			case 0x09:
				replacement = x09;
				break;

			case 0x0A:
				replacement = x0A;
				break;

			case 0x0B:
				replacement = x0B;
				break;

			case 0x0C:
				replacement = x0C;
				break;

			case 0x0D:
				replacement = x0D;
				break;

			case 0x0E:
				replacement = x0E;
				break;

			case 0x0F:
				replacement = x0F;
				break;

			case 0x10:
				replacement = x10;
				break;

			case 0x11:
				replacement = x11;
				break;

			case 0x12:
				replacement = x12;
				break;

			case 0x13:
				replacement = x13;
				break;

			case 0x14:
				replacement = x14;
				break;

			case 0x15:
				replacement = x15;
				break;

			case 0x16:
				replacement = x16;
				break;

			case 0x17:
				replacement = x17;
				break;

			case 0x18:
				replacement = x18;
				break;

			case 0x19:
				replacement = x19;
				break;

			case '\\':
				replacement = Solidus;
				break;

			case '"':
				replacement = DoubleQuote;
				break;

			case '/':
				replacement = ReverseSolidus;
				break;

			default:
				try
				{
					writer.write(character);
				}
				catch (final IOException e)
				{
					throw new CouldNotWriteDataException(e);
				}
				return;
		}
		try
		{
			writer.write(replacement);
		}
		catch (final IOException e)
		{
			throw new CouldNotWriteDataException(e);
		}
	}
}
