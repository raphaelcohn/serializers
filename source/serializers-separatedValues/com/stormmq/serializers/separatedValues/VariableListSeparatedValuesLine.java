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

import java.util.ArrayList;
import java.util.List;

public final class VariableListSeparatedValuesLine extends AbstractSeparatedValuesLine
{
	@NotNull
	private final List<String> fields;

	public VariableListSeparatedValuesLine()
	{
		fields = new ArrayList<>(10);
	}

	@SuppressWarnings("SimplifiableIfStatement")
	@Override
	protected boolean isFieldAlreadyRecorded(final int index)
	{
		if (index >= size())
		{
			return false;
		}
		return fields.get(index) != null;
	}

	@Override
	protected void store(final int index, @Nullable final String rawValue)
	{
		final int size = size();
		if (index < size)
		{
			fields.add(index, rawValue);
			return;
		}
		if (index == size)
		{
			fields.add(rawValue);
			return;
		}
		final int emptyElementsToAdd = index - size;
		for (int count = 0; count < emptyElementsToAdd; count++)
		{
			fields.add(null);
		}
		fields.add(rawValue);
	}

	@Override
	@Nullable
	protected String load(final int index)
	{
		return fields.get(index);
	}

	@Override
	protected int size()
	{
		return fields.size();
	}
}
