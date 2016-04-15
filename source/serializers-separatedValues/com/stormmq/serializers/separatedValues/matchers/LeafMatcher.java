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

package com.stormmq.serializers.separatedValues.matchers;

import org.jetbrains.annotations.*;
import com.stormmq.serializers.FieldTokenName;
import com.stormmq.serializers.separatedValues.SeparatedValuesLine;

import static java.lang.String.format;
import static java.util.Arrays.copyOf;
import static java.util.Locale.ENGLISH;

public final class LeafMatcher extends AbstractMatcher
{
	@NotNull
	private final char[] separator;

	@NotNull
	public static Matcher leaf(@FieldTokenName @NonNls @NotNull final String name, final int fieldIndex, @NotNull final char... separator)
	{
		return new LeafMatcher(name, fieldIndex, separator);
	}

	private final int fieldIndex;

	private LeafMatcher(@FieldTokenName @NonNls @NotNull final String name, final int fieldIndex, @NotNull final char... separator)
	{
		super(name);
		this.fieldIndex = fieldIndex;
		this.separator = copyOf(separator, separator.length);
	}

	@NotNull
	@Override
	public Matcher matchChild(@FieldTokenName @NotNull @NonNls final String name)
	{
		throw new IllegalArgumentException(format(ENGLISH, "This is a LeafMatcher, there is not a child %1$s", name));
	}

	@Override
	public void recordValue(@NotNull final String rawValue, @NotNull final SeparatedValuesLine separatedValuesLine)
	{
		separatedValuesLine.recordValue(fieldIndex, rawValue);
	}

	@SuppressWarnings("ReturnOfCollectionOrArrayField")
	@NotNull
	@Override
	public char[] separator()
	{
		return separator;
	}

	@Override
	public boolean equals(@Nullable final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}
		if (!super.equals(obj))
		{
			return false;
		}

		final LeafMatcher that = (LeafMatcher) obj;

		return fieldIndex == that.fieldIndex;

	}

	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + fieldIndex;
		return result;
	}
}
