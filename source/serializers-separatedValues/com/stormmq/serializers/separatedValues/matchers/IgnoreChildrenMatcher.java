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

public final class IgnoreChildrenMatcher extends AbstractMatcher
{
	@NotNull
	private static final char[] Empty = new char[0];

	@NotNull
	public static IgnoreChildrenMatcher ignoreChildren(@FieldTokenName @NotNull @NonNls final String name, final int fieldIndex)
	{
		return new IgnoreChildrenMatcher(name, fieldIndex);
	}

	private final int fieldIndex;

	private IgnoreChildrenMatcher(@FieldTokenName @NotNull @NonNls final String name, final int fieldIndex)
	{
		super(name);
		this.fieldIndex = fieldIndex;
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

		final IgnoreChildrenMatcher that = (IgnoreChildrenMatcher) obj;

		return fieldIndex == that.fieldIndex;

	}

	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + fieldIndex;
		return result;
	}

	@Override
	@NotNull
	public Matcher matchChild(@NotNull @NonNls final String name)
	{
		return this;
	}

	@Override
	public void recordValue(@NotNull final String rawValue, @NotNull final SeparatedValuesLine separatedValuesLine)
	{
	}

	@NotNull
	@Override
	public char[] separator()
	{
		return Empty;
	}
}
