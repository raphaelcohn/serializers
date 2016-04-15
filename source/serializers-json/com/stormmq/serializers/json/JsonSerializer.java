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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.stormmq.serializers.*;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

import static com.stormmq.string.StringConstants._null;

@SuppressWarnings({"ClassNamePrefixedWithPackageName", "ClassWithTooManyMethods"})
public class JsonSerializer extends AbstractSerializer
{
	private static final int DoubleQuote = '\"';
	private static final int Comma = ',';
	private static final char[] DoubleQuoteColonDoubleQuote = "\":\"".toCharArray();
	private static final char[] CommaDoubleQuote = ",\"".toCharArray();
	private static final char[] DoubleQuoteColon = "\":".toCharArray();
	private static final int OpenObject = '{';
	private static final int CloseObject = '}';
	private static final int OpenArray = '[';
	private static final int CloseArray = ']';
	private static final char[] Null = _null.toCharArray();

	@NotNull
	private final Stack<JsonNodeState> depth;

	@NotNull
	private JsonNodeState current;

	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@NotNull
	private JsonStringWriter jsonStringWriter;

	protected JsonSerializer()
	{
		depth = new Stack<>();
		current = new JsonNodeState();
	}

	@Override
	public void start(@NotNull final OutputStream outputStream, @NotNull final Charset charset) throws CouldNotWriteDataException
	{
		super.start(outputStream, charset);
		jsonStringWriter = new JsonStringWriter(writer);
	}

	@Override
	public void writeProperty(@NotNull final String name, @NotNull final String value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			if (current.hasSubsequentProperty())
			{
				write(CommaDoubleQuote);
			}
			else
			{
				write(DoubleQuote);
				current.setHasSubsequentProperty();
			}
			jsonStringWriter.writeString(name);
			write(DoubleQuoteColonDoubleQuote);
			jsonStringWriter.writeString(value);
			write(DoubleQuote);
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
	}

	@Override
	public void writeProperty(@NotNull final String name, @NotNull final MapSerializable value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			if (current.hasSubsequentProperty())
			{
				write(CommaDoubleQuote);
			}
			else
			{
				write(DoubleQuote);
				current.setHasSubsequentProperty();
			}
			jsonStringWriter.writeString(name);
			write(DoubleQuoteColon);
			writeValue(value);
		}
		catch (CouldNotWriteDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
	}

	@Override
	public void writeProperty(@NotNull final String name, @NotNull final ValueSerializable value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			if (current.hasSubsequentProperty())
			{
				write(CommaDoubleQuote);
			}
			else
			{
				write(DoubleQuote);
				current.setHasSubsequentProperty();
			}
			jsonStringWriter.writeString(name);
			write(DoubleQuoteColon);
			writeValue(value);
		}
		catch (CouldNotWriteDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
	}

	@Override
	public <S extends MapSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			if (current.hasSubsequentProperty())
			{
				write(CommaDoubleQuote);
			}
			else
			{
				write(DoubleQuote);
				current.setHasSubsequentProperty();
			}
			jsonStringWriter.writeString(name);
			write(DoubleQuoteColon);
			writeValue(values);
		}
		catch (CouldNotWriteDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
	}

	@Override
	public <S extends ValueSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			if (current.hasSubsequentProperty())
			{
				write(CommaDoubleQuote);
			}
			else
			{
				write(DoubleQuote);
				current.setHasSubsequentProperty();
			}
			jsonStringWriter.writeString(name);
			write(DoubleQuoteColon);
			writeValue(values);
		}
		catch (CouldNotWriteDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final List<?> values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			if (current.hasSubsequentProperty())
			{
				write(CommaDoubleQuote);
			}
			else
			{
				write(DoubleQuote);
				current.setHasSubsequentProperty();
			}
			jsonStringWriter.writeString(name);
			write(DoubleQuoteColon);
			writeValue(values);
		}
		catch (CouldNotWriteDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final Set<?> values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			if (current.hasSubsequentProperty())
			{
				write(CommaDoubleQuote);
			}
			else
			{
				write(DoubleQuote);
				current.setHasSubsequentProperty();
			}
			jsonStringWriter.writeString(name);
			write(DoubleQuoteColon);
			writeValue(values);
		}
		catch (CouldNotWriteDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final int value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			if (current.hasSubsequentProperty())
			{
				write(CommaDoubleQuote);
			}
			else
			{
				write(DoubleQuote);
				current.setHasSubsequentProperty();
			}
			jsonStringWriter.writeString(name);
			write(DoubleQuoteColon);
			writeValue(value);
		}
		catch (CouldNotWriteDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final long value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			if (current.hasSubsequentProperty())
			{
				write(CommaDoubleQuote);
			}
			else
			{
				write(DoubleQuote);
				current.setHasSubsequentProperty();
			}
			jsonStringWriter.writeString(name);
			write(DoubleQuoteColon);
			writeValue(value);
		}
		catch (CouldNotWriteDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
	}

	@Override
	public void writePropertyNull(@NonNls @NotNull final String name, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			if (current.hasSubsequentProperty())
			{
				write(CommaDoubleQuote);
			}
			else
			{
				write(DoubleQuote);
				current.setHasSubsequentProperty();
			}
			jsonStringWriter.writeString(name);
			write(DoubleQuoteColon);
			writeValueNull();
		}
		catch (CouldNotWriteDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, e);
		}
	}

	@Override
	public <S extends MapSerializable> void writeValue(@NotNull final S[] values) throws CouldNotWriteValueException
	{
		depth.push(current);
		try
		{
			current = new JsonNodeState();
			write(OpenArray);
			final int length = values.length;
			if (length != 0)
			{
				writeValue(values[0]);
				for (int index = 1; index < length; index++)
				{
					write(Comma);
					writeValue(values[index]);
				}
			}
			write(CloseArray);
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotWriteValueException(values, e);
		}
		current = depth.pop();
	}

	@Override
	public <S extends ValueSerializable> void writeValue(@NotNull final S[] values) throws CouldNotWriteValueException
	{
		depth.push(current);
		try
		{
			current = new JsonNodeState();
			write(OpenArray);
			final int length = values.length;
			if (length != 0)
			{
				writeValue(values[0]);
				for (int index = 1; index < length; index++)
				{
					write(Comma);
					writeValue(values[index]);
				}
			}
			write(CloseArray);
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotWriteValueException(values, e);
		}
		current = depth.pop();
	}

	@Override
	public void writeValue(@NotNull final List<?> values) throws CouldNotWriteValueException
	{
		depth.push(current);
		try
		{
			current = new JsonNodeState();
			write(OpenArray);
			final int length = values.size();
			if (length != 0)
			{
				writeValue(values.get(0));
				for (int index = 1; index < length; index++)
				{
					write(Comma);
					writeValue(values.get(index));
				}
			}
			write(CloseArray);
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotWriteValueException(values, e);
		}
		current = depth.pop();
	}

	@Override
	public void writeValue(@NotNull final Set<?> values) throws CouldNotWriteValueException
	{
		depth.push(current);
		try
		{
			current = new JsonNodeState();
			write(OpenArray);
			boolean afterFirst = false;
			for (final Object value : values)
			{
				if (afterFirst)
				{
					write(Comma);
				}
				else
				{
					afterFirst = true;
				}
				writeValue(value);
			}
			write(CloseArray);
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotWriteValueException(values, e);
		}
		current = depth.pop();
	}

	@SuppressWarnings("OverloadedVarargsMethod")
	@SafeVarargs
	@Override
	public final <S extends Serializable> void writeValue(@NotNull final S... values) throws CouldNotWriteValueException
	{
		depth.push(current);
		try
		{
			current = new JsonNodeState();
			write(OpenArray);
			boolean afterFirst = false;
			for (final Object value : values)
			{
				if (afterFirst)
				{
					write(Comma);
				}
				else
				{
					afterFirst = true;
				}
				writeValue(value);
			}
			write(CloseArray);
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotWriteValueException(values, e);
		}
		current = depth.pop();
	}

	@Override
	public void writeValue(final int value) throws CouldNotWriteValueException
	{
		try
		{
			write(Integer.toString(value));
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotWriteValueException(value, e);
		}
	}

	@Override
	public void writeValue(final long value) throws CouldNotWriteValueException
	{
		try
		{
			write(Long.toString(value));
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotWriteValueException(value, e);
		}
	}

	@Override
	public void writeValue(@NotNull final BigDecimal value) throws CouldNotWriteValueException
	{
		try
		{
			write(value.toString());
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotWriteValueException(value, e);
		}
	}

	@Override
	public void writeValue(@NotNull final String value) throws CouldNotWriteValueException
	{
		try
		{
			write(DoubleQuote);
			jsonStringWriter.writeString(value);
			write(DoubleQuote);
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotWriteValueException(value, e);
		}
	}

	@Override
	public void writeValue(@NotNull final MapSerializable value) throws CouldNotWriteValueException
	{
		try
		{
			depth.push(current);
			current = new JsonNodeState();
			write(OpenObject);
			value.serialiseMap(this);
			write(CloseObject);
			depth.pop();
		}
		catch (CouldNotWriteDataException | CouldNotSerializeMapException e)
		{
			throw new CouldNotWriteValueException(value, e);
		}
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
		try
		{
			write(Null);
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotWriteValueException(_null, e);
		}
	}

	private void write(final int character) throws CouldNotWriteDataException
	{
		try
		{
			writer.write(character);
		}
		catch (final IOException e)
		{
			throw new CouldNotWriteDataException(e);
		}
	}

	private void write(final String value) throws CouldNotWriteDataException
	{
		try
		{
			writer.write(value);
		}
		catch (final IOException e)
		{
			throw new CouldNotWriteDataException(e);
		}
	}

	private void write(final char[] character) throws CouldNotWriteDataException
	{
		try
		{
			writer.write(character);
		}
		catch (final IOException e)
		{
			throw new CouldNotWriteDataException(e);
		}
	}
}
