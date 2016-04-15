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

package com.stormmq.serializers.unknown;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public class AbstractIsUnknown implements IsUnknown
{
	private final boolean isUnknown;

	protected AbstractIsUnknown(final boolean isUnknown)
	{
		this.isUnknown = isUnknown;
	}

	@Override
	@NotNull
	public String toString()
	{
		if (isUnknown)
		{
			return format(ENGLISH, "Unknown%1$s", getClass().getSimpleName());
		}
		return super.toString();
	}

	@Override
	public final boolean isUnknown()
	{
		return isUnknown;
	}

	@Override
	public final boolean isKnown()
	{
		return !isUnknown;
	}

	@Override
	public final boolean isSameKnownessAs(@NotNull final IsUnknown that)
	{
		return isUnknown ? that.isUnknown() : that.isKnown();
	}

	@Override
	public final boolean isDifferentKnownessAs(@NotNull final IsUnknown that)
	{
		return !isSameKnownessAs(that);
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

		final AbstractIsUnknown that = (AbstractIsUnknown) obj;

		return isUnknown == that.isUnknown;

	}

	@Override
	public int hashCode()
	{
		return isUnknown ? 1 : 0;
	}
}
