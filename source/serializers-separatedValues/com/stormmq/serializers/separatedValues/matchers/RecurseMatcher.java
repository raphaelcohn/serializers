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

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class RecurseMatcher extends AbstractMatcher
{
	@NotNull
	public static RecurseMatcher rootMatcher(@NotNull final Matcher... children)
	{
		return new RecurseMatcher("", children);
	}

	@NotNull
	public static Matcher recurse(@FieldTokenName @NotNull @NonNls final String name, @NotNull final Matcher... children)
	{
		return new RecurseMatcher(name, children);
	}

	@NotNull
	private final Map<String, Matcher> children;

	@SuppressWarnings("ForLoopReplaceableByForEach")
	private RecurseMatcher(@FieldTokenName @NotNull @NonNls final String name, @NotNull final Matcher... children)
	{
		super(name);
		final int length = children.length;
		this.children = new HashMap<>(length);
		for (int index = 0; index < length; index++)
		{
			final Matcher child = children[index];
			child.register(this.children);
		}
	}

	@Override
	@NotNull
	public Matcher matchChild(@NotNull @NonNls final String name)
	{
		@Nullable final Matcher child = children.get(name);
		if (child == null)
		{
			throw new IllegalArgumentException(format(ENGLISH, "no matcher known for name %1$s", name));
		}
		return child;
	}

	@Override
	public void recordValue(@NotNull final String rawValue, @NotNull final SeparatedValuesLine separatedValuesLine)
	{
		throw new IllegalArgumentException(format(ENGLISH, "This matcher is not a leaf and can not record the value %1$s", rawValue));
	}

	@NotNull
	@Override
	public char[] separator()
	{
		throw new IllegalArgumentException("This matcher is not a leaf and can not have a separator");
	}
}
