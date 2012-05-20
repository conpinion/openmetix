/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.util;


import de.iritgo.openmetix.app.instrument.InstrumentDisplay;
import de.iritgo.openmetix.core.gui.swing.IMenuItem;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import javax.swing.AbstractAction;
import javax.swing.JSeparator;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


/**
 * Miscelllaneous tool methods.
 *
 * @version $Id: Tools.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class Tools
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
	 * Get the default formatter for double values without '.'
	 *
	 * @return The factory with the formatter for the JFormattedTextField.
	 */
	public static DefaultFormatterFactory getDoubleFormatter ()
	{
		return new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#")));
	}

	/**
	 * Get the default formatter for time values.
	 *
	 * @return The factory with the formatter for the JFormattedTextField.
	 */
	public static DefaultFormatterFactory getTimeFormatter ()
	{
		return new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("HH:mm")));
	}

	/**
	 * Get the default formatter for date values.
	 *
	 * @return The factory with the formatter for the JFormattedTextField.
	 */
	public static DefaultFormatterFactory getDateFormatter ()
	{
		return new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("dd.MM.yyyy")));
	}

	/**
	 * Create a chartpanel with default values for preview.
	 *
	 * @param chart The chart to be embedd.
	 * @return The chart panel without a context menu.
	 */
	public static ChartPanel createPreviewChartPanel (JFreeChart chart)
	{
		ChartPanel panel = new MetixChartPanel(chart, false, false, false, false, false);

		panel.setMinimumDrawWidth (0);
		panel.setMinimumDrawHeight (0);
		panel.setMaximumDrawWidth (9999);
		panel.setMaximumDrawHeight (9999);

		return panel;
	}

	/**
	 * Create a chartpanel with default values for instrument.
	 *
	 * @param chart The chart to be embedd.
	 * @param display The instrument for which to create the panel.
	 * @return The chart panel.
	 */
	public static ChartPanel createInstrumentChartPanel (
		JFreeChart chart, InstrumentDisplay display)
	{
		ChartPanel panel = new MetixChartPanel(chart, false, true, true, false, true);

		panel.setMinimumDrawWidth (0);
		panel.setMinimumDrawHeight (0);
		panel.setMaximumDrawWidth (9999);
		panel.setMaximumDrawHeight (9999);

		if (display.isEditable ())
		{
			IMenuItem editItem =
				new IMenuItem(
					new AbstractAction()
					{
						public void actionPerformed (ActionEvent e)
						{
							CommandTools.performAsync ("EditInstrument");
						}
					});

			editItem.setText ("metix.edit");

			panel.getPopupMenu ().insert (new JSeparator(JSeparator.HORIZONTAL), 0);
			panel.getPopupMenu ().insert (editItem, 0);
		}

		return panel;
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