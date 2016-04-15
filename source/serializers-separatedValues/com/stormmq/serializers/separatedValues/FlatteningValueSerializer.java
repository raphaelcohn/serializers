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

import com.stormmq.serializers.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

import static java.util.Arrays.copyOf;

public final class FlatteningValueSerializer extends AbstractValueSerializer
{
	@NotNull
	private final char[] separator;

	public FlatteningValueSerializer(@NotNull final char... separator)
	{
		this.separator = copyOf(separator, separator.length);
	}

	public void start(@NotNull final Writer writer, @NotNull final Charset charset)
	{
		this.writer = writer;
		this.charset = charset;
	}

	@Override
	public <S extends MapSerializable> void writeValue(@NotNull final S[] values) throws CouldNotWriteValueException
	{
		boolean afterFirst = false;
		for (final S value : values)
		{
			if (afterFirst)
			{
				try
				{
					writer.write(separator);
				}
				catch (final IOException e)
				{
					throw new CouldNotWriteValueException(values, new CouldNotWriteDataException(e));
				}
			}
			else
			{
				afterFirst = true;
			}
			writeValue(value);
		}
	}

	@Override
	public <S extends ValueSerializable> void writeValue(@NotNull final S[] values) throws CouldNotWriteValueException
	{
		boolean afterFirst = false;
		for (final S value : values)
		{
			if (afterFirst)
			{
				try
				{
					writer.write(separator);
				}
				catch (final IOException e)
				{
					throw new CouldNotWriteValueException(values, new CouldNotWriteDataException(e));
				}
			}
			else
			{
				afterFirst = true;
			}
			writeValue(value);
		}
	}

	@Override
	public void writeValue(@NotNull final List<?> values) throws CouldNotWriteValueException
	{
		boolean afterFirst = false;
		for (final Object value : values)
		{
			if (afterFirst)
			{
				try
				{
					writer.write(separator);
				}
				catch (final IOException e)
				{
					throw new CouldNotWriteValueException(values, new CouldNotWriteDataException(e));
				}
			}
			else
			{
				afterFirst = true;
			}
			writeValue(value);
		}
	}

	@Override
	public void writeValue(@NotNull final Set<?> values) throws CouldNotWriteValueException
	{
		boolean afterFirst = false;
		for (final Object value : values)
		{
			if (afterFirst)
			{
				try
				{
					writer.write(separator);
				}
				catch (final IOException e)
				{
					throw new CouldNotWriteValueException(values, new CouldNotWriteDataException(e));
				}
			}
			else
			{
				afterFirst = true;
			}
			writeValue(value);
		}
	}

	@Override
	public void writeValue(final int value) throws CouldNotWriteValueException
	{
		writeValue(Integer.toString(value));
	}

	@Override
	public void writeValue(final long value) throws CouldNotWriteValueException
	{
		writeValue(Long.toString(value));
	}

	@Override
	public void writeValue(@NotNull final BigDecimal value) throws CouldNotWriteValueException
	{
		writeValue(value.toString());
	}

	@Override
	public void writeValue(@NotNull final String value) throws CouldNotWriteValueException
	{
		try
		{
			writer.write(value);
		}
		catch (final IOException e)
		{
			throw new CouldNotWriteValueException(value, new CouldNotWriteDataException(e));
		}
	}

	@Override
	public void writeValue(@NotNull final MapSerializable value) throws CouldNotWriteValueException
	{
		writeValue(value.toString());
	}

	@Override
	public void writeValue(@NotNull final ValueSerializable value) throws CouldNotWriteValueException
	{
		try
		{
			value.serialiseValue(this);
		}
		catch (final CouldNotSerializeValueException e)
		{
			throw new CouldNotWriteValueException(value, e);
		}
	}

	@Override
	public void writeValue(@NotNull final UUID value) throws CouldNotWriteValueException
	{
		writeValue(value.toString());
	}

	@Override
	public void writeValueNull() throws CouldNotWriteValueException
	{
		writeValue("");
	}
}
