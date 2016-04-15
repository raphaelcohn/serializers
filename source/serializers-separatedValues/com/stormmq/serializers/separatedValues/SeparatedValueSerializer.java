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

import org.jetbrains.annotations.*;
import com.stormmq.serializers.*;
import com.stormmq.serializers.separatedValues.fieldEscapers.FieldEscaper;
import com.stormmq.serializers.separatedValues.matchers.Matcher;

import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

import static com.stormmq.functions.collections.ArraysHelper.*;
import static java.lang.System.out;
import static java.nio.charset.StandardCharsets.UTF_8;
import static com.stormmq.serializers.separatedValues.fieldEscapers.CommaSeparatedFieldEscaper.CommaSeparatedFieldEscaperInstance;
import static com.stormmq.serializers.separatedValues.fieldEscapers.SanitisingTabSeparatedFieldEscaper.SanitisingTabSeparatedFieldEscaperInstance;

@SuppressWarnings({"ClassNamePrefixedWithPackageName", "ClassWithTooManyMethods"})
public final class SeparatedValueSerializer extends AbstractSerializer
{
	@NotNull
	public static SeparatedValueSerializer tabSeparatedValueSerializer(@NotNull final Matcher root, final boolean writeHeaderLine, @NotNull final String... headings)
	{
		return new SeparatedValueSerializer(SanitisingTabSeparatedFieldEscaperInstance, root, writeHeaderLine, headings);
	}

	@NotNull
	public static SeparatedValueSerializer commaSeparatedValueSerializer(@NotNull final Matcher root, final boolean writeHeaderLine, @NotNull final String... headings)
	{
		return new SeparatedValueSerializer(CommaSeparatedFieldEscaperInstance, root, writeHeaderLine, headings);
	}

	@NotNull private Matcher current;
	private final boolean writeHeaderLine;
	@NotNull private final String[] headings;
	@NotNull private final Stack<Matcher> stack;
	private final int numberOfFields;
	@NotNull private final FieldEscaper fieldEscaper;
	@Nullable private SeparatedValuesLine separatedValuesLine;

	private SeparatedValueSerializer(@NotNull final FieldEscaper fieldEscaper, @NotNull final Matcher root, final boolean writeHeaderLine, @NotNull final String... headings)
	{
		current = root;
		this.writeHeaderLine = writeHeaderLine;
		this.headings = copyOf(headings);
		numberOfFields = headings.length;
		this.fieldEscaper = fieldEscaper;
		stack = new Stack<>();
	}

	@SafeVarargs
	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	public final <S extends Serializable> void printValuesOnStandardOut(@NotNull final S... values)
	{
		writeOut(out, values);
	}

	private <S extends Serializable> void writeOut(@NotNull final OutputStream outputStream, @NotNull final S[] values)
	{
		try
		{
			start(outputStream, UTF_8);
			writeValue(values);
			finish();
		}
		catch (CouldNotWriteDataException | CouldNotWriteValueException e)
		{
			throw new IllegalStateException("Could not write values", e);
		}
	}

	@Override
	public void start(@NotNull final OutputStream outputStream, @NotNull final Charset charset) throws CouldNotWriteDataException
	{
		super.start(outputStream, charset);
		if (writeHeaderLine)
		{
			final SeparatedValuesLine headerLine = new FixedArraySeparatedValuesLine(numberOfFields);
			for (int index = 0; index < numberOfFields; index++)
			{
				headerLine.recordValue(index, headings[index]);
			}
			try
			{
				headerLine.writeLine(writer, fieldEscaper);
			}
			catch (final CouldNotEncodeDataException e)
			{
				throw new CouldNotWriteDataException(e);
			}
		}
	}

	@Override
	public <S extends MapSerializable> void writeValue(@NotNull final S[] values) throws CouldNotWriteValueException
	{
		if (separatedValuesLine != null)
		{
			writeNestedMapSerializableValues(values);
		}
		for (final S value : values)
		{
			separatedValuesLine = new FixedArraySeparatedValuesLine(numberOfFields);

			writeValue(value);

			try
			{
				separatedValuesLine.writeLine(writer, fieldEscaper);
			}
			catch (CouldNotEncodeDataException | CouldNotWriteDataException e)
			{
				throw new CouldNotWriteValueException(value, e);
			}
			separatedValuesLine = null;
		}
	}

	@Override
	public <S extends ValueSerializable> void writeValue(@NotNull final S[] values) throws CouldNotWriteValueException
	{
		if (separatedValuesLine != null)
		{
			writeNestedValueSerializableValues(values);
			return;
		}
		for (final S value : values)
		{
			separatedValuesLine = new FixedArraySeparatedValuesLine(numberOfFields);

			writeValue(value);

			try
			{
				separatedValuesLine.writeLine(writer, fieldEscaper);
			}
			catch (CouldNotEncodeDataException | CouldNotWriteDataException e)
			{
				throw new CouldNotWriteValueException(value, e);
			}
			separatedValuesLine = null;
		}
	}

	@Override
	public void writeValue(@NotNull final List<?> values) throws CouldNotWriteValueException
	{
		if (separatedValuesLine != null)
		{
			writeNestedValueObjectValues(values);
			return;
		}
		for (final Object value : values)
		{
			separatedValuesLine = new FixedArraySeparatedValuesLine(numberOfFields);

			writeValue(value);

			try
			{
				separatedValuesLine.writeLine(writer, fieldEscaper);
			}
			catch (CouldNotEncodeDataException | CouldNotWriteDataException e)
			{
				throw new CouldNotWriteValueException(value, e);
			}
			separatedValuesLine = null;
		}
	}

	@Override
	public void writeValue(@NotNull final Set<?> values) throws CouldNotWriteValueException
	{
		if (separatedValuesLine != null)
		{
			writeNestedValueObjectValues(values);
			return;
		}
		for (final Object value : values)
		{
			separatedValuesLine = new FixedArraySeparatedValuesLine(numberOfFields);

			writeValue(value);

			try
			{
				separatedValuesLine.writeLine(writer, fieldEscaper);
			}
			catch (CouldNotEncodeDataException | CouldNotWriteDataException e)
			{
				throw new CouldNotWriteValueException(value, e);
			}
			separatedValuesLine = null;
		}
	}

	@SafeVarargs
	@Override
	public final <S extends Serializable> void writeValue(@NotNull final S... values) throws CouldNotWriteValueException
	{
		if (separatedValuesLine != null)
		{
			writeNestedValueObjectValues(values);
			return;
		}
		for (final Object value : values)
		{
			separatedValuesLine = new FixedArraySeparatedValuesLine(numberOfFields);

			writeValue(value);

			try
			{
				separatedValuesLine.writeLine(writer, fieldEscaper);
			}
			catch (CouldNotEncodeDataException | CouldNotWriteDataException e)
			{
				throw new CouldNotWriteValueException(value, e);
			}
			separatedValuesLine = null;
		}
	}

	private <S extends MapSerializable> void writeNestedMapSerializableValues(final S[] values, final char... separator) throws CouldNotWriteValueException
	{
		final FlatteningValueSerializer flatteningValueSerializer = new FlatteningValueSerializer(separator);
		final StringWriter writer1 = new StringWriter(100);
		flatteningValueSerializer.start(writer1, UTF_8);
		flatteningValueSerializer.writeValue(values);
		final String flattenedValue = writer1.toString();
		writeValue(flattenedValue);
	}

	private <S extends ValueSerializable> void writeNestedValueSerializableValues(final S[] values, final char... separator) throws CouldNotWriteValueException
	{
		final FlatteningValueSerializer flatteningValueSerializer = new FlatteningValueSerializer(separator);
		final StringWriter writer1 = new StringWriter(100);
		flatteningValueSerializer.start(writer1, UTF_8);
		flatteningValueSerializer.writeValue(values);
		final String flattenedValue = writer1.toString();
		writeValue(flattenedValue);
	}

	private <S extends ValueSerializable> void writeNestedValueObjectValues(final List<?> values, final char... separator) throws CouldNotWriteValueException
	{
		final FlatteningValueSerializer flatteningValueSerializer = new FlatteningValueSerializer(separator);
		final StringWriter writer1 = new StringWriter(100);
		flatteningValueSerializer.start(writer1, UTF_8);
		flatteningValueSerializer.writeValue(values);
		final String flattenedValue = writer1.toString();
		writeValue(flattenedValue);
	}

	private <S extends ValueSerializable> void writeNestedValueObjectValues(final Set<?> values, final char... separator) throws CouldNotWriteValueException
	{
		final FlatteningValueSerializer flatteningValueSerializer = new FlatteningValueSerializer(separator);
		final StringWriter writer1 = new StringWriter(100);
		flatteningValueSerializer.start(writer1, UTF_8);
		flatteningValueSerializer.writeValue(values);
		final String flattenedValue = writer1.toString();
		writeValue(flattenedValue);
	}

	private <S extends Serializable> void writeNestedValueObjectValues(final S[] values, final char... separator) throws CouldNotWriteValueException
	{
		final FlatteningValueSerializer flatteningValueSerializer = new FlatteningValueSerializer(separator);
		final StringWriter writer1 = new StringWriter(100);
		flatteningValueSerializer.start(writer1, UTF_8);
		flatteningValueSerializer.writeValue(values);
		final String flattenedValue = writer1.toString();
		writeValue(flattenedValue);
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final String value, final boolean isMapEntry)
	{
		final Matcher matcher = current.matchChild(name);
		matcher.recordValue(value, separatedValuesLine);
	}

	@Override
	public void writePropertyNull(@FieldTokenName @NonNls @NotNull final String name, final boolean isMapEntry)
	{
		writeProperty(name, "", isMapEntry);
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final MapSerializable value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		final Matcher matcher = current.matchChild(name);
		stack.push(current);
		current = matcher;
		try
		{
			writeValue(value);
		}
		catch (final CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
		current = stack.pop();
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final ValueSerializable value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		final Matcher matcher = current.matchChild(name);
		stack.push(current);
		current = matcher;
		try
		{
			writeValue(value);
		}
		catch (final CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
		current = stack.pop();
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final int value, final boolean isMapEntry)
	{
		writeProperty(name, Integer.toString(value), isMapEntry);
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final long value, final boolean isMapEntry)
	{
		writeProperty(name, Long.toString(value), isMapEntry);
	}

	@Override
	public <S extends MapSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		final Matcher matcher = current.matchChild(name);
		stack.push(current);
		current = matcher;
		try
		{
			writeNestedMapSerializableValues(values, current.separator());
		}
		catch (final CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
		current = stack.pop();
	}

	@Override
	public <S extends ValueSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		final Matcher matcher = current.matchChild(name);
		stack.push(current);
		current = matcher;
		try
		{
			writeNestedValueSerializableValues(values, current.separator());
		}
		catch (final CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
		current = stack.pop();
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final List<?> values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		final Matcher matcher = current.matchChild(name);
		stack.push(current);
		current = matcher;
		try
		{
			writeNestedValueObjectValues(values, current.separator());
		}
		catch (final CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
		current = stack.pop();
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final Set<?> values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		final Matcher matcher = current.matchChild(name);
		stack.push(current);
		current = matcher;
		try
		{
			writeNestedValueObjectValues(values, current.separator());
		}
		catch (final CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
		current = stack.pop();
	}

	@Override
	public void writeValue(@NotNull final String value)
	{
		current.recordValue(value, separatedValuesLine);
	}

	@Override
	public void writeValue(final int value)
	{
		writeValue(Integer.toString(value));
	}

	@Override
	public void writeValue(final long value)
	{
		writeValue(Long.toString(value));
	}

	@Override
	public void writeValue(@NotNull final BigDecimal value)
	{
		writeValue(value.toString());
	}

	@Override
	public void writeValueNull()
	{
		writeValue("");
	}

	@Override
	public void writeValue(@NotNull final MapSerializable value) throws CouldNotWriteValueException
	{
		try
		{
			value.serialiseMap(this);
		}
		catch (final CouldNotSerializeMapException e)
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
	public void writeValue(@NotNull final UUID value)
	{
		writeValue(value.toString());
	}
}
