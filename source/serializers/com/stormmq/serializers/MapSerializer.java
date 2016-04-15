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

import java.util.List;
import java.util.Set;

public interface MapSerializer
{
	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final String value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final String value, final boolean isMapEntry) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final MapSerializable value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final MapSerializable value, final boolean isMapEntry) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final ValueSerializable value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final ValueSerializable value, final boolean isMapEntry) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @Nullable final Object value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @Nullable final Object value, final boolean isMapEntry) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final int value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final int value, final boolean isMapEntry) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final long value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final long value, final boolean isMapEntry) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final boolean value) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final boolean value, final boolean isMapEntry) throws CouldNotWritePropertyException;

	void writePropertyNull(@FieldTokenName @NonNls @NotNull final String name) throws CouldNotWritePropertyException;

	void writePropertyNull(@FieldTokenName @NonNls @NotNull final String name, final boolean isMapEntry) throws CouldNotWritePropertyException;

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	<S extends MapSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values) throws CouldNotWritePropertyException;

	<S extends MapSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values, final boolean isMapEntry) throws CouldNotWritePropertyException;

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	<S extends ValueSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values) throws CouldNotWritePropertyException;

	<S extends ValueSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values, final boolean isMapEntry) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final List<?> values) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final List<?> values, final boolean isMapEntry) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final Set<?> values) throws CouldNotWritePropertyException;

	void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final Set<?> values, final boolean isMapEntry) throws CouldNotWritePropertyException;
}
