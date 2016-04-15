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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class FixedArraySeparatedValuesLine extends AbstractSeparatedValuesLine
{
	private final int length;
	@NotNull
	private final String[] fields;

	public FixedArraySeparatedValuesLine(final int length)
	{
		this.length = length;
		fields = new String[length];
	}

	@Override
	protected boolean isFieldAlreadyRecorded(final int index)
	{
		if (index >= length)
		{
			throw new IllegalArgumentException(format(ENGLISH, "index %1$s is out of range (index < %2$s)", index, length));
		}
		return fields[index] != null;
	}

	@Override
	protected void store(final int index, @Nullable final String rawValue)
	{
		fields[index] = rawValue;
	}

	@Override
	@Nullable
	protected String load(final int index)
	{
		return fields[index];
	}

	@Override
	protected int size()
	{
		return length;
	}
}
