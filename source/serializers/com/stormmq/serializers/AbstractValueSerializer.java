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

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

import static com.stormmq.string.StringConstants._false;
import static com.stormmq.string.StringConstants._true;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public abstract class AbstractValueSerializer implements ValueSerializer, StartFinish
{
	@NotNull
	private static final String TRUE = _true;

	@NotNull
	private static final String FALSE = _false;

	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@NotNull
	protected Charset charset;

	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@NotNull
	protected Writer writer;

	protected AbstractValueSerializer()
	{
	}

	@Override
	public void start(@NotNull final OutputStream outputStream, @NotNull final Charset charset) throws CouldNotWriteDataException
	{
		this.charset = charset;
		writer = new OutputStreamWriter(outputStream, charset);
	}

	@Override
	public void finish() throws CouldNotWriteDataException
	{
		try
		{
			writer.flush();
		}
		catch (final IOException e)
		{
			throw new CouldNotWriteDataException(e);
		}
	}

	@SuppressWarnings("ConditionalExpression")
	@Override
	public void writeValue(final boolean value) throws CouldNotWriteValueException
	{
		writeValue(convertBooleanToString(value));
	}

	@NotNull
	protected static String convertBooleanToString(final boolean value)
	{
		return value ? TRUE : FALSE;
	}

	// TODO: Needs to be be pulled out and abstracted so that different rules can apply for different serialisations
	@Override
	public void writeValue(@Nullable final Object value) throws CouldNotWriteValueException
	{
		if (value == null)
		{
			writeValueNull();
			return;
		}

		if (value instanceof MapSerializable)
		{
			writeValue((MapSerializable) value);
			return;
		}

		if (value instanceof ValueSerializable)
		{
			writeValue((ValueSerializable) value);
			return;
		}

		if (value instanceof MapSerializable[])
		{
			writeValue((MapSerializable[]) value);
			return;
		}

		if (value instanceof ValueSerializable[])
		{
			writeValue((ValueSerializable[]) value);
			return;
		}

		if (value instanceof Integer)
		{
			writeValue((int) value);
			return;
		}

		if (value instanceof Long)
		{
			writeValue((long) value);
			return;
		}

		if (value instanceof BigDecimal)
		{
			writeValue((BigDecimal) value);
			return;
		}

		if (value instanceof String)
		{
			writeValue((String) value);
			return;
		}

		if (value instanceof Boolean)
		{
			writeValue((boolean) value);
			return;
		}

		if (value instanceof Enum)
		{
			writeValue(((Enum<?>) value).name());
			return;
		}

		if (value instanceof List)
		{
			writeValue((List<?>) value);
		}

		if (value instanceof Set)
		{
			writeValue((Set<?>) value);
		}

		if (value instanceof Map)
		{
			writeValue(new GenericMapSerializable((Map<?, ?>) value));
		}

		throw new CouldNotWriteValueException(value, format(ENGLISH, "do not know how to write values for this class %1$s", value.getClass().getSimpleName()));
	}
}
