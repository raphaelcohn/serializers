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

package com.stormmq.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Map.Entry;

public final class GenericMapSerializable implements MapSerializable
{
	@NotNull private final Map<?, ?> value;

	@SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
	public GenericMapSerializable(@NotNull final Map<?, ?> value)
	{
		this.value = value;
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

		final GenericMapSerializable that = (GenericMapSerializable) obj;

		return value.equals(that.value);

	}

	@Override
	public int hashCode()
	{
		return value.hashCode();
	}

	@Override
	public void serialiseMap(@NotNull final MapSerializer mapSerializer) throws CouldNotSerializeMapException
	{
		serialiseMapGenerically(mapSerializer, value);
	}

	private static void serialiseMapGenerically(@NotNull final MapSerializer mapSerializer, @NotNull final Map<?, ?> value) throws CouldNotSerializeMapException
	{
		for (final Entry<?, ?> entry : value.entrySet())
		{
			try
			{
				final Object key = entry.getKey();
				mapSerializer.writeProperty(key instanceof PropertyNameSerializable ? ((PropertyNameSerializable) key).serialiseToPropertyName() : key.toString(), entry.getValue(), true);
			}
			catch (final CouldNotWritePropertyException e)
			{
				throw new CouldNotSerializeMapException(new GenericMapSerializable(value), e);
			}
		}
	}
}
