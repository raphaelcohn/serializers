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

import org.jetbrains.annotations.*;
import com.stormmq.serializers.unknown.IsUnknown;

import java.util.*;

import static com.stormmq.serializers.ValueSerializable.NullNumber;

public abstract class AbstractSerializer extends AbstractValueSerializer implements Serializer
{
	public static <V extends ValueSerializable & IsUnknown> void writePropertyIfKnown(@NotNull final MapSerializer mapSerializer, @FieldTokenName @NotNull @NonNls final String name, @NotNull final V value) throws CouldNotWritePropertyException
	{
		if (value.isKnown())
		{
			mapSerializer.writeProperty(name, value);
		}
	}

	public static <V extends MapSerializable & IsUnknown> void writePropertyIfKnown(@NotNull final MapSerializer mapSerializer, @NonNls @NotNull @FieldTokenName final String name, @NotNull final V value) throws CouldNotWritePropertyException
	{
		if (value.isKnown())
		{
			mapSerializer.writeProperty(name, value);
		}
	}

	public static void writeNullableProperty(@NotNull final MapSerializer mapSerializer, @FieldTokenName @NotNull @NonNls final String field, @Nullable final String value) throws CouldNotWritePropertyException
	{
		if (value == null)
		{
			mapSerializer.writePropertyNull(field);
		}
		else
		{
			mapSerializer.writeProperty(field, value);
		}
	}

	public static void writeNullableProperty(@NotNull final MapSerializer mapSerializer, @NotNull @NonNls @FieldTokenName final String name, final long valueOrMinusOne) throws CouldNotWritePropertyException
	{
		if (valueOrMinusOne == NullNumber)
		{
			mapSerializer.writePropertyNull(name);
		}
		else
		{
			mapSerializer.writeProperty(name, valueOrMinusOne);
		}
	}

	public static void writeNullableProperty(@NotNull final MapSerializer mapSerializer, @NotNull @NonNls @FieldTokenName final String name, @Nullable final Boolean value) throws CouldNotWritePropertyException
	{
		if (value == null)
		{
			mapSerializer.writePropertyNull(name);
		}
		else
		{
			mapSerializer.writeProperty(name, (boolean) value);
		}
	}

	public static void writeNullableProperty(@NotNull final MapSerializer mapSerializer, @NotNull @NonNls @FieldTokenName final String name, @Nullable final ValueSerializable valueSerializable) throws CouldNotWritePropertyException
	{
		if (valueSerializable == null)
		{
			return;
		}
		mapSerializer.writeProperty(name, valueSerializable);
	}

	@Override
	public final void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final String value) throws CouldNotWritePropertyException
	{
		writeProperty(name, value, false);
	}

	@Override
	public final void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final MapSerializable value) throws CouldNotWritePropertyException
	{
		writeProperty(name, value, false);
	}

	@Override
	public final void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final ValueSerializable value) throws CouldNotWritePropertyException
	{
		writeProperty(name, value, false);
	}

	@Override
	public final void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final int value) throws CouldNotWritePropertyException
	{
		writeProperty(name, value, false);
	}

	@Override
	public final void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final long value) throws CouldNotWritePropertyException
	{
		writeProperty(name, value, false);
	}

	@Override
	public final void writePropertyNull(@FieldTokenName @NonNls @NotNull final String name) throws CouldNotWritePropertyException
	{
		writePropertyNull(name, false);
	}

	@Override
	public final <S extends MapSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values) throws CouldNotWritePropertyException
	{
		writeProperty(name, values, false);
	}

	@Override
	public final <S extends ValueSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values) throws CouldNotWritePropertyException
	{
		writeProperty(name, values, false);
	}

	@Override
	public final void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final List<?> values) throws CouldNotWritePropertyException
	{
		writeProperty(name, values, false);
	}

	@Override
	public final void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final Set<?> values) throws CouldNotWritePropertyException
	{
		writeProperty(name, values, false);
	}

	@Override
	public final void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final boolean value) throws CouldNotWritePropertyException
	{
		writeProperty(name, value, false);
	}

	@Override
	public final void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final boolean value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		writeProperty(name, convertBooleanToString(value), isMapEntry);
	}

	@Override
	public final void writeProperty(@NonNls @NotNull final String name, @Nullable final Object value) throws CouldNotWritePropertyException
	{
		writeProperty(name, value, false);
	}

	@SuppressWarnings("MethodWithMultipleReturnPoints")
	@Override
	public final void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @Nullable final Object value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		if (value == null)
		{
			writePropertyNull(name, isMapEntry);
			return;
		}

		if (value instanceof MapSerializable)
		{
			writeProperty(name, (MapSerializable) value, isMapEntry);
			return;
		}

		if (value instanceof ValueSerializable)
		{
			writeProperty(name, (ValueSerializable) value, isMapEntry);
			return;
		}

		if (value instanceof MapSerializable[])
		{
			writeProperty(name, (MapSerializable[]) value, isMapEntry);
			return;
		}

		if (value instanceof ValueSerializable[])
		{
			writeProperty(name, (ValueSerializable[]) value, isMapEntry);
			return;
		}

		if (value instanceof Integer)
		{
			writeProperty(name, (int) value, isMapEntry);
			return;
		}

		if (value instanceof Long)
		{
			writeProperty(name, (long) value, isMapEntry);
			return;
		}

		if (value instanceof String)
		{
			writeProperty(name, (String) value, isMapEntry);
			return;
		}

		if (value instanceof Boolean)
		{
			writeProperty(name, (boolean) value, isMapEntry);
			return;
		}

		if (value instanceof UUID)
		{
			writeProperty(name, value, isMapEntry);
			return;
		}

		if (value instanceof Map)
		{
			writeProperty(name, new GenericMapSerializable((Map<?, ?>) value), isMapEntry);
			return;
		}

		if (value instanceof List)
		{
			writeProperty(name, (List<?>) value, isMapEntry);
			return;
		}

		if (value instanceof Set)
		{
			writeProperty(name, (Set<?>) value, isMapEntry);
			return;
		}

		throw new CouldNotWritePropertyException(name, value, "do not know how to write properties for this class");
	}

	@Override
	public final void writeValue(@Nullable final Object value) throws CouldNotWriteValueException
	{
		if (value == null)
		{
			writeValueNull();
			return;
		}

		if (value instanceof Serializable)
		{
			writeValue((Serializable) value);
			return;
		}

		if (value instanceof Serializable[])
		{
			writeValue((Serializable[]) value);
			return;
		}

		super.writeValue(value);
	}

	@Override
	public final <S extends Serializable> void writeValue(@NotNull final S value) throws CouldNotWriteValueException
	{
		value.serialise(this);
	}
}
