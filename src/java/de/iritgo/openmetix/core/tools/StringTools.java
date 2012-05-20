/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.tools;



/**
 * Useful string methods.
 *
 * @version $Id: StringTools.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public final class StringTools
{
	/** Random string used to encode/decode a string. */
	private static String codeKey =
		"Gss654gsf$2f24a$312GKHGskgjgcztwwugd86f3u3j3v##..,fgsf732tg2wdf" +
		"sdudufj476633sdewvnv88304kb-ndm,tnx763jnKDjhm,S,Jdzhenncgflr.md." +
		"fjczucGGshz36lcvsdfdsgh4734gcmSdfm,dhw62gcklghdks;.;sjcj/dfhd6%&" +
		"Jhggdd6%&ddhdjcbst%&ielf,b.nm/&82hrghcm:cngst2364rivn:,jnvhs63Hh";

	/** Hex characters. */
	private static String hexCodes = "0123456789ABCDEF";

	/**
	 * Check wether 'test' is a substring of 'text'.
	 *
	 * @param text The text to check.
	 * @param test The substring to find.
	 * @return True if 'test' is part of 'text'.
	 */
	public static boolean contains (String text, String test)
	{
		return text.indexOf (test) != -1;
	}

	/**
	 * Check wether a string is empty (length == 0) or not.
	 *
	 * @return True if the supplied string is empty.
	 */
	public static boolean isEmpty (String text)
	{
		return text == null || text.length () == 0;
	}

	/**
	 * Check wether a trimmed string is empty (length == 0) or not.
	 *
	 * @return True if the supplied string is empty.
	 */
	public static boolean isTrimEmpty (String text)
	{
		return text == null || text.trim ().length () == 0;
	}

	/**
	 * Returns a new string with the first character converted to uppercase.
	 *
	 * @param value The string to convert.
	 * @return The converted string.
	 */
	public static String initialUpperCase (String value)
	{
		return Character.toUpperCase (value.charAt (0)) + value.substring (1);
	}

	/**
	 * Returns a new string with the first character converted to lowercase.
	 *
	 * @param value The string to convert.
	 * @return The converted string.
	 */
	public static String initialLowerCase (String value)
	{
		return Character.toLowerCase (value.charAt (0)) + value.substring (1);
	}

	/**
	 * Encode the specified string.
	 *
	 * @param s This is the string to encode.
	 * @return The encoded string.
	 */
	public static String encode (String s)
	{
		String strCoded = new String();

		for (int i = 0; i < s.length (); ++i)
		{
			String codePart = new String();
			int code = 0;
			int keypos = 0;

			for (keypos = 0; true; ++keypos)
			{
				code = (int) s.charAt (i) ^ (int) codeKey.charAt ((i + keypos) % 256);

				if (code != 0)
				{
					break;
				}
			}

			codePart += intToHex (keypos, 2);
			codePart += intToHex (code, 4);

			strCoded += codePart;
		}

		return strCoded;
	}

	/**
	 * Decode the specified string.
	 *
	 * @param s This is the string to decode.
	 * @return The decoded string.
	 */
	public static String decode (String s)
	{
		String strDecoded = new String();

		for (int i = 0; i < s.length (); i += 6)
		{
			int code;
			int keypos;
			String codePartKey = s.substring (i, i + 2);
			String codePartCode = s.substring (i + 2, i + 6);

			keypos = hexToInt (codePartKey);
			code = hexToInt (codePartCode);

			char addChar = (char) ((int) code ^ (int) codeKey.charAt (((i / 6) + keypos) % 256));

			strDecoded += addChar;
		}

		return strDecoded;
	}

	/**
	 * Convert an int value to a hex string.
	 *
	 * @param value The value to convert.
	 * @param numChars The number of chars (nibbles) that should be converted.
	 * @return The hex string.
	 */
	public static String intToHex (int value, int numChars)
	{
		String hex = new String();

		long mask = ((1 << ((numChars) * 4)) - 1);

		value &= mask;

		for (int i = numChars - 1; i >= 0; --i)
		{
			int nibbleValue = value >> (i * 4);

			hex += hexCodes.charAt (nibbleValue);
			value &= ((1 << (i * 4)) - 1);
		}

		return hex;
	}

	/**
	 * Convert a hex string to an int value .
	 *
	 * @param s The hexString to convert.
	 * @return The int value.
	 */
	public static int hexToInt (String s)
	{
		int dez = 0;
		int value = 0;
		char code;

		s = s.toUpperCase ();

		for (int i = s.length (); i > 0; --i)
		{
			code = s.charAt (s.length () - i);

			for (dez = 0; dez < hexCodes.length (); ++dez)
			{
				if (code == hexCodes.charAt (dez))
				{
					break;
				}
			}

			if (dez >= hexCodes.length ())
			{
				return -1;
			}

			value += dez * (1 << ((i - 1) * 4));
		}

		return value;
	}
}