/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.tools;



/**
 * Useful methods for working with numbers.
 *
 * @version $Revision: 1.1 $
 */
public final class NumberTools
{
	/** An array of the first 30 roman values. */
	static String[] romanValues =
	{
		"I",
		"II",
		"III",
		"IV",
		"V",
		"VI",
		"VII",
		"VIII",
		"IX",
		"X",
		"XI",
		"XII",
		"XIII",
		"XIV",
		"XV",
		"XVI",
		"XVII",
		"XVIII",
		"XIX",
		"XX",
		"XXI",
		"XXII",
		"XXIII",
		"XXIV",
		"XXV",
		"XXVI",
		"XXVII",
		"XXVIII",
		"XXIX",
		"XXX"
	};

	/** Hex digits. */
	static char[] hexDigits =
	{
		'0',
		'1',
		'2',
		'3',
		'4',
		'5',
		'6',
		'7',
		'8',
		'9',
		'A',
		'B',
		'C',
		'D',
		'E',
		'F'
	};

	/**
	 * Convert a string to an integer value.
	 *
	 * @param value The string value.
	 * @param defaultValue Integer value to return if the conversion fails.
	 */
	public static Integer toInteger (String value, int defaultValue)
	{
		if (value == null || value.length () == 0)
		{
			return new Integer(defaultValue);
		}

		try
		{
			return new Integer(value);
		}
		catch (NumberFormatException x)
		{
			return new Integer(defaultValue);
		}
	}

	/**
	 * Convert a string to an int value.
	 *
	 * @param value The string value.
	 * @param defaultValue Int value to return if the conversion fails.
	 */
	public static int toInt (String value, int defaultValue)
	{
		if (value == null || value.length () == 0)
		{
			return defaultValue;
		}

		try
		{
			return Integer.parseInt (value);
		}
		catch (NumberFormatException x)
		{
			return defaultValue;
		}
	}

	/**
	 * Convert a string to a long int value.
	 *
	 * @param value The string value.
	 * @param defaultValue Int value to return if the conversion fails.
	 */
	public static long toLong (String value, long defaultValue)
	{
		if (value == null || value.length () == 0)
		{
			return defaultValue;
		}

		try
		{
			return Long.parseLong (value);
		}
		catch (NumberFormatException x)
		{
			return defaultValue;
		}
	}

	/**
	 * Convert a string to a double value.
	 *
	 * @param value The string value.
	 * @param defaultValue Double value to return if the conversion fails.
	 */
	public static double toDouble (String value, double defaultValue)
	{
		if (value == null || value.length () == 0)
		{
			return defaultValue;
		}

		try
		{
			return Double.parseDouble (value);
		}
		catch (NumberFormatException x)
		{
			return defaultValue;
		}
	}

	/**
	 * Translate an integer to a roman number.
	 * Only the first 30 numbers are available.
	 *
	 * @param value The value to convert.
	 * @return A roman string representation of the value.
	 */
	public static String romanNumber (int value)
	{
		if (value >= 30)
		{
			return String.valueOf (value);
		}

		return romanValues[value - 1];
	}

	/**
	 * Convert an integer to a hex string.
	 *
	 * @param value The value to convert.
	 * @return The hex string.
	 */
	public static String toHex2 (int value)
	{
		return String.valueOf (hexDigits[(value & (15 << 4)) >> 4]) +
		String.valueOf (hexDigits[value & 15]);
	}
}