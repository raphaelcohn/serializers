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

import org.jetbrains.annotations.NotNull;
import com.stormmq.serializers.CouldNotWriteDataException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public final class JsonPSerializer extends JsonSerializer
{
	private static final int OpenBrace = '(';
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final char[] Ending = ");\n".toCharArray();

	@NotNull
	private final String jsonPPrefix;

	public JsonPSerializer(@NotNull final String jsonPPrefix)
	{
		if (jsonPPrefix.isEmpty())
		{
			throw new IllegalArgumentException("jsonPPrefix must be at least one character");
		}
		this.jsonPPrefix = jsonPPrefix;
	}

	@Override
	public void start(@NotNull final OutputStream outputStream, @NotNull final Charset charset) throws CouldNotWriteDataException
	{
		super.start(outputStream, charset);
		try
		{
			writer.write(jsonPPrefix);
			writer.write(OpenBrace);
		}
		catch (final IOException e)
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
			writer.write(Ending);
		}
		catch (final IOException e)
		{
			throw new CouldNotWriteDataException(e);
		}
		finally
		{
			super.finish();
		}
	}
}
