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

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

import static com.stormmq.string.StringConstants.*;
import static java.lang.Character.*;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;

public final class JsonPFunctionNameValidator
{
	@NonNls
	private static final Collection<String> ReservedWords = new HashSet<>(asList(new String[]
	{
		"break",
		"do",
		"instanceof",
		"typeof",
		"case",
		"else",
		"new",
		"var",
		"catch",
		"finally",
		"return",
		_void,
		"continue",
		"for",
		"switch",
		"while",
		"debugger",
		"function",
		"this",
		"with",
		_default,
		"if",
		"throw",
		"delete",
		"in",
		"try",
		_class,
		_enum,
		"extends",
		_super,
		"const",
		"export",
		"import",
		"implements",
		"let",
		_private,
		"public", "yield",
		_interface,
		"package",
		_protected,
		"static",
		_null,
		_true,
		_false,
	}));
	private static final int Underscore = '_';
	private static final int Dollar = '$';
	private static final int ZeroWidthNonJoiner = 0x200C;
	private static final int ZeroWidthJoiner = 0x200D;
	private static final int MinimumSurrogate = 0xD800;
	private static final int MaximumSurrogatePlusOne = 0xDFFF + 1;
	private static final Pattern Dot = compile("\\.");

	@NotNull
	public static String validateJsonPFunctionName(@NotNull final String jsonPFunctionName) throws JsonFunctionNameInvalidException
	{
		if (jsonPFunctionName.isEmpty())
		{
			throw new JsonFunctionNameInvalidException(jsonPFunctionName);
		}
		final String[] functionNames = Dot.split(jsonPFunctionName);
		for (final String functionName : functionNames)
		{
			final int length = functionName.length();
			if (length == 0)
			{
				throw new JsonFunctionNameInvalidException("zero-length function names are invalid");
			}
			if (ReservedWords.contains(functionName))
			{
				throw new JsonFunctionNameInvalidException("there is a reserved word in the function name");
			}

			int index = 0;
			while (index < length)
			{
				final int codePoint = jsonPFunctionName.codePointAt(index);
				if (codePoint >= MinimumSurrogate && codePoint < MaximumSurrogatePlusOne)
				{
					throw new JsonFunctionNameInvalidException("of an invalid surrogate");
				}
				final int type = getType(codePoint);
				final boolean valid = index == 0 ? isValidCodePointForIndex0(codePoint, type) : isValidCodePointForSubsequentIndex(codePoint, type);
				if (!valid)
				{
					throw new JsonFunctionNameInvalidException("there is an invalid character in the function name");
				}
				index += isBmpCodePoint(codePoint) ? 1 : 2;
			}
			if (index == length + 1)
			{
				throw new JsonFunctionNameInvalidException("there is an unbalanced surrogate pair");
			}
		}
		return jsonPFunctionName;
	}

	private static boolean isValidCodePointForSubsequentIndex(final int codePoint, final int type)
	{
		return isValidCodePointForIndex0(codePoint, type) || is(codePoint, ZeroWidthNonJoiner) || is(codePoint, ZeroWidthJoiner) || isType(codePoint, NON_SPACING_MARK) || isType(codePoint, COMBINING_SPACING_MARK) || isType(codePoint, DECIMAL_DIGIT_NUMBER) || isType(codePoint, CONNECTOR_PUNCTUATION);
	}

	private static boolean isValidCodePointForIndex0(final int codePoint, final int type)
	{
		return is(codePoint, Dollar) || is(codePoint, Underscore) || isType(type, UPPERCASE_LETTER) || isType(type, LOWERCASE_LETTER) || isType(type, TITLECASE_LETTER) || isType(type, MODIFIER_LETTER) || isType(type, OTHER_LETTER) || isType(type, LETTER_NUMBER);
	}

	private static boolean is(final int codePoint, final int match)
	{
		return codePoint == match;
	}

	private static boolean isType(final int type, final byte category)
	{
		return type == category;
	}

	private JsonPFunctionNameValidator()
	{
	}
}
