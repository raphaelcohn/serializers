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

package com.stormmq.serializers.xml;

import com.stormmq.tuples.Pair;
import org.jetbrains.annotations.*;
import com.stormmq.serializers.*;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

import static com.stormmq.functions.collections.ArraysHelper.of;
import static com.stormmq.string.StringConstants._true;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.ENGLISH;

@SuppressWarnings({"ClassNamePrefixedWithPackageName", "ClassWithTooManyMethods"})
public final class XmlSerializer extends AbstractSerializer
{
	@NotNull @NonNls private static final String XmlSchemaInstanceNamespace = "http://www.w3.org/2001/XMLSchema-instance";
	@SuppressWarnings("unchecked") @NotNull private static final Pair<String, String>[] Empty = new Pair[0];
	@NonNls
	@NotNull
	private static final String XmlnsPrefixColon = "xmlns:";
	private static final int Space = ' ';
	private static final int DoubleQuote = '"';
	private static final char[] EqualsDoubleQuote = {'=', '"'};
	private static final int LessThan = '<';
	private static final int GreaterThan = '>';
	private static final char[] LessThanSlash = characters("</");
	private static final char[] SlashGreaterThan = characters("/>");
	private static final String ListElementNodeName = "element";

	@SuppressWarnings("OverloadedVarargsMethod")
	@SafeVarargs
	public static void serialise(@NonNls @NotNull final String rootNodeName, @NotNull final Serializable graph, @NotNull final OutputStream outputStream, @NotNull final Pair<String, String>... rootAttributes) throws CouldNotSerialiseException
	{
		serialise(true, rootNodeName, graph, outputStream, UTF_8, of(new Pair<>(XmlSchemaInstanceNamespace, "xsi")), rootAttributes);
	}

	private static void serialise(final boolean xmlDeclaration, @NotNull final String rootNodeName, @NotNull final Serializable graph, @NotNull final OutputStream outputStream, @NotNull final Charset charset, @NotNull final Pair<String, String>[] namespaceUriToPrefixes, @NotNull final Pair<String, String>[] rootAttributes) throws CouldNotSerialiseException
	{
		final XmlSerializer xmlSerializer = new XmlSerializer(xmlDeclaration, rootNodeName, namespaceUriToPrefixes, rootAttributes);
		xmlSerializer.serialise(graph, outputStream, charset);
	}

	private static char[] characters(final String value)
	{
		return value.toCharArray();
	}

	@NotNull
	private final String rootNodeName;
	private final boolean xmlDeclaration;
	@Nullable
	private final Pair<String, String> xsiNilAttribute;
	@NotNull
	private final Pair<String, String>[] rootAttributes;

	@SuppressWarnings("InstanceVariableMayNotBeInitialized")
	@NotNull
	private XmlStringWriter xmlStringWriter;

	@SafeVarargs
	public XmlSerializer(final boolean xmlDeclaration, @NonNls @NotNull final String rootNodeName, @NotNull final Pair<String, String>... namespaceUriToPrefixes)
	{
		this(xmlDeclaration, rootNodeName, namespaceUriToPrefixes, Empty);
	}

	@SuppressWarnings("unchecked")
	@SafeVarargs
	private XmlSerializer(final boolean xmlDeclaration, @NonNls @NotNull final String rootNodeName, @NotNull final Pair<String, String>[] namespaceUriToPrefixes, @NotNull final Pair<String, String>... rootAttributes)
	{
		this.rootNodeName = rootNodeName;
		this.xmlDeclaration = xmlDeclaration;
		@Nullable String xmlSchemaInstancePrefix = null;
		final Map<String, String> rootNodeAttributes = new HashMap<>(namespaceUriToPrefixes.length + rootAttributes.length);
		for (final Pair<String, String> namespaceUriToPrefix : namespaceUriToPrefixes)
		{
			final String uri = namespaceUriToPrefix.a;
			final String prefix = namespaceUriToPrefix.b;
			if (prefix.startsWith("xml"))
			{
				throw new IllegalArgumentException("namespace prefixes can not start with xml");
			}
			if (rootNodeAttributes.put(XmlnsPrefixColon + uri, prefix) != null)
			{
				throw new IllegalArgumentException("Duplicate namespace");
			}
			if (uri.equals(XmlSchemaInstanceNamespace))
			{
				xmlSchemaInstancePrefix = prefix;
			}
		}
		for (final Pair<String, String> rootAttribute : rootAttributes)
		{
			rootAttribute.putOnce(rootNodeAttributes);
		}
		final int size = rootNodeAttributes.size();
		this.rootAttributes = new Pair[size];
		int index = 0;
		for (final Entry<String, String> toPair : rootNodeAttributes.entrySet())
		{
			this.rootAttributes[index] = new Pair<>(toPair);
			index++;
		}
		xsiNilAttribute = xmlSchemaInstancePrefix == null ? null : new Pair<>(xmlSchemaInstancePrefix + ":nil", _true);
	}

	private void serialise(@NotNull final Serializable graph, @NotNull final OutputStream outputStream, @NotNull final Charset charset) throws CouldNotSerialiseException
	{
		try
		{
			start(outputStream, charset);
			graph.serialise(this);
			finish();
		}
		catch (final CouldNotWriteDataException e)
		{
			throw new CouldNotSerialiseException(graph, e);
		}
	}

	@Override
	public void start(@NotNull final OutputStream outputStream, @NotNull final Charset charset) throws CouldNotWriteDataException
	{
		super.start(outputStream, charset);
		xmlStringWriter = new XmlStringWriter(writer);
		if (xmlDeclaration)
		{
			try
			{
				writer.write(format(ENGLISH, "<?xml version=\"1.0\" encoding=\"%1$s\" standalone=\"yes\"?>", charset.name().toUpperCase(ENGLISH)));
			}
			catch (final IOException e)
			{
				throw new CouldNotWriteDataException(e);
			}
		}
		else if (!charset.equals(UTF_8))
		{
			throw new IllegalStateException("If a XML declaration is omitted, then the charset must be UTF-8");
		}
		try
		{
			writeOpen(rootNodeName, false, rootAttributes);
		}
		catch (final CouldNotEncodeDataException e)
		{
			throw new CouldNotWriteDataException(e);
		}
	}

	@SuppressWarnings("ThrowFromFinallyBlock")
	@Override
	public void finish() throws CouldNotWriteDataException
	{
		try
		{
			writeClose(rootNodeName, false);
		}
		catch (final CouldNotEncodeDataException e)
		{
			throw new CouldNotWriteDataException(e);
		}
		finally
		{
			super.finish();
		}
	}

	@Override
	public void writeProperty(@NotNull final String name, @NotNull final String value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		if (value.isEmpty())
		{
			writeEmptyProperty(name, isMapEntry);
			return;
		}
		try
		{
			writeOpen(name, isMapEntry);
			writeText(value);
			writeClose(name, isMapEntry);
		}
		catch (CouldNotWriteDataException | CouldNotEncodeDataException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
	}

	@Override
	public void writeProperty(@NotNull final String name, @NotNull final MapSerializable value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			writeOpen(name, isMapEntry);
			writeValue(value);
			writeClose(name, isMapEntry);
		}
		catch (CouldNotWriteDataException | CouldNotEncodeDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
	}

	@Override
	public void writeProperty(@NotNull final String name, @NotNull final ValueSerializable value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			writeOpen(name, isMapEntry);
			writeValue(value);
			writeClose(name, isMapEntry);
		}
		catch (CouldNotWriteDataException | CouldNotEncodeDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final int value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			writeOpen(name, isMapEntry);
			writeValue(value);
			writeClose(name, isMapEntry);
		}
		catch (CouldNotWriteDataException | CouldNotEncodeDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, final long value, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			writeOpen(name, isMapEntry);
			writeValue(value);
			writeClose(name, isMapEntry);
		}
		catch (CouldNotWriteDataException | CouldNotEncodeDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, value, e);
		}
	}

	@Override
	public void writePropertyNull(@NonNls @NotNull final String name, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		if (xsiNilAttribute == null)
		{
			writeEmptyProperty(name, isMapEntry);
		}
		else
		{
			writeEmptyProperty(name, isMapEntry, xsiNilAttribute);
		}
	}

	@Override
	public <S extends MapSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			writeOpen(name, isMapEntry);
			writeValue(values);
			writeClose(name, isMapEntry);
		}
		catch (CouldNotWriteDataException | CouldNotEncodeDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
	}

	@Override
	public <S extends ValueSerializable> void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final S[] values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			writeOpen(name, isMapEntry);
			writeValue(values);
			writeClose(name, isMapEntry);
		}
		catch (CouldNotWriteDataException | CouldNotEncodeDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final List<?> values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			writeOpen(name, isMapEntry);
			writeValue(values);
			writeClose(name, isMapEntry);
		}
		catch (CouldNotWriteDataException | CouldNotEncodeDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
	}

	@Override
	public void writeProperty(@FieldTokenName @NonNls @NotNull final String name, @NotNull final Set<?> values, final boolean isMapEntry) throws CouldNotWritePropertyException
	{
		try
		{
			writeOpen(name, isMapEntry);
			writeValue(values);
			writeClose(name, isMapEntry);
		}
		catch (CouldNotWriteDataException | CouldNotEncodeDataException | CouldNotWriteValueException e)
		{
			throw new CouldNotWritePropertyException(name, values, e);
		}
	}

	@Override
	public <S extends MapSerializable> void writeValue(@NotNull final S[] values) throws CouldNotWriteValueException
	{
		try
		{
			for (final S value : values)
			{
				if (value == null)
				{
					writeEmptyProperty(ListElementNodeName, false);
				}
				else
				{
					writeProperty(ListElementNodeName, value);
				}
			}
		}
		catch (final CouldNotWritePropertyException e)
		{
			throw new CouldNotWriteValueException(values, e);
		}
	}

	@Override
	public <S extends ValueSerializable> void writeValue(@NotNull final S[] values) throws CouldNotWriteValueException
	{
		try
		{
			for (final S value : values)
			{
				if (value == null)
				{
					writeEmptyProperty(ListElementNodeName, false);
				}
				else
				{
					writeProperty(ListElementNodeName, value);
				}
			}
		}
		catch (final CouldNotWritePropertyException e)
		{
			throw new CouldNotWriteValueException(values, e);
		}
	}

	@Override
	public void writeValue(@NotNull final List<?> values) throws CouldNotWriteValueException
	{
		try
		{
			for (final Object value : values)
			{
				if (value == null)
				{
					writeEmptyProperty(ListElementNodeName, false);
				}
				else
				{
					writeProperty(ListElementNodeName, value);
				}
			}
		}
		catch (final CouldNotWritePropertyException e)
		{
			throw new CouldNotWriteValueException(values, e);
		}
	}

	@Override
	public void writeValue(@NotNull final Set<?> values) throws CouldNotWriteValueException
	{
		try
		{
			for (final Object value : values)
			{
				if (value == null)
				{
					writeEmptyProperty(ListElementNodeName, false);
				}
				else
				{
					writeProperty(ListElementNodeName, value);
				}
			}
		}
		catch (final CouldNotWritePropertyException e)
		{
			throw new CouldNotWriteValueException(values, e);
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
			writeText(value);
		}
		catch (CouldNotWriteDataException | CouldNotEncodeDataException e)
		{
			throw new CouldNotWriteValueException(value, e);
		}
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
	public void writeValue(@NotNull final UUID value) throws CouldNotWriteValueException
	{
		writeValue(value.toString());
	}

	@Override
	public void writeValueNull()
	{
	}

	@SafeVarargs
	@Override
	public final <S extends Serializable> void writeValue(@NotNull final S... values) throws CouldNotWriteValueException
	{
		try
		{
			for (final Object value : values)
			{
				if (value == null)
				{
					writeEmptyProperty(ListElementNodeName, false);
				}
				else
				{
					writeProperty(ListElementNodeName, value);
				}
			}
		}
		catch (final CouldNotWritePropertyException e)
		{
			throw new CouldNotWriteValueException(values, e);
		}
	}

	// final is required for @SafeVarargs
	@SafeVarargs
	private final void writeOpen(final CharSequence name, final boolean isMapEntry, final Pair<String, String>... attributes) throws CouldNotWriteDataException, CouldNotEncodeDataException
	{
		write(LessThan);
		writeNodeName(name, isMapEntry);
		writeAttributes(name, isMapEntry, attributes);
		write(GreaterThan);
	}

	private void writeClose(final CharSequence name, final boolean isMapEntry) throws CouldNotWriteDataException, CouldNotEncodeDataException
	{
		write(LessThanSlash);
		writeNodeName(name, isMapEntry);
		write(GreaterThan);
	}

	// final is required for @SafeVarargs
	@SafeVarargs
	private final void writeEmpty(final CharSequence name, final boolean isMapEntry, final Pair<String, String>... attributes) throws CouldNotWriteDataException, CouldNotEncodeDataException
	{
		write(LessThan);
		writeNodeName(name, isMapEntry);
		writeAttributes(name, isMapEntry, attributes);
		write(SlashGreaterThan);
	}

	@SuppressWarnings("FeatureEnvy") // final is required for @SafeVarargs
	@SafeVarargs
	private final void writeAttributes(final CharSequence name, final boolean isMapEntry, final Pair<String, String>... attributes) throws CouldNotWriteDataException, CouldNotEncodeDataException
	{
		if (isMapEntry)
		{
			write(Space);
			xmlStringWriter.writeAttributeName("key");
			write(EqualsDoubleQuote);
			xmlStringWriter.writeAttributeValue(name);
			write(DoubleQuote);
		}

		for (final Pair<String, String> attribute : attributes)
		{
			write(Space);
			@NonNls final String attributeName = attribute.a;
			if (isMapEntry && "key".equals(attributeName))
			{
				throw new CouldNotEncodeDataException("An attribute 'key' is present, which is reserved by the serializer for map entries (which this is)");
			}
			xmlStringWriter.writeAttributeName(attributeName);
			write(EqualsDoubleQuote);
			xmlStringWriter.writeAttributeValue(attribute.b);
			write(DoubleQuote);
		}
	}

	// final is required for @SafeVarargs
	@SafeVarargs
	private final void writeEmptyProperty(final String name, final boolean isMapEntry, final Pair<String, String>... attributes) throws CouldNotWritePropertyException
	{
		try
		{
			writeEmpty(name, isMapEntry, attributes);
		}
		catch (CouldNotWriteDataException | CouldNotEncodeDataException e)
		{
			throw new CouldNotWritePropertyException(name, "", e);
		}
	}

	private void writeNodeName(final CharSequence name, final boolean isMapEntry) throws CouldNotWriteDataException, CouldNotEncodeDataException
	{
		xmlStringWriter.writeNodeName(isMapEntry ? "map-entry" : name);
	}

	private void writeText(final CharSequence value) throws CouldNotWriteDataException, CouldNotEncodeDataException
	{
		xmlStringWriter.writeText(value);
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
}
