/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gui;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.resource.ResourceService;
import javax.swing.JComboBox;


/**
 * A time combobox which lets the user select a time interval.
 *
 * @version $Id: ITimeComboBox.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class ITimeComboBox
	extends JComboBox
{
	/** Display periods as singular+plural. */
	public static final int FORMAT_SINGULAR_PLURAL = 0;

	/** Display periods as singular. */
	public static final int FORMAT_SINGULAR = 1;

	/** Display periods as plural. */
	public static final int FORMAT_PLURAL = 2;

	/** Display format. */
	private int format = FORMAT_SINGULAR_PLURAL;

	/** Interval type for years. */
	public static final int PERIOD_YEAR = 0;

	/** Interval type for months. */
	public static final int PERIOD_MONTH = 1;

	/** Interval type for weeks. */
	public static final int PERIOD_WEEK = 2;

	/** Interval type for days. */
	public static final int PERIOD_DAY = 3;

	/** Interval type for hours. */
	public static final int PERIOD_HOUR = 4;

	/** Interval type for minutes. */
	public static final int PERIOD_MINUTE = 5;

	/** Interval type for seconds. */
	public static final int PERIOD_SECOND = 6;

	/** Pre defined time intervals. */
	private static long[] milliSeconds =
	{
		(long) (365 * 24 * 60 * 60) * 1000,
		(long) 30.5 * 7 * 24 * 60 * 60 * 1000,
		7 * 24 * 60 * 60 * 1000,
		24 * 60 * 60 * 1000,
		60 * 60 * 1000,
		60 * 1000,
		1000
	};

	/**
	 * Create a new ITimeComboBox.
	 */
	public ITimeComboBox ()
	{
		createItems ();
	}

	/**
	 * Convert the index to a long time value
	 *
	 * @param index Model index.
	 * @return The time interval in milli seconds.
	 */
	public static long convert (int index)
	{
		if (index < 0 || index > milliSeconds.length)
		{
			return 1;
		}

		return milliSeconds[index];
	}

	/**
	 * Set the display format.
	 *
	 * @param format The new format;
	 */
	public void setFormat (int format)
	{
		this.format = format;
		createItems ();
	}

	/**
	 * Add period items to the combobox.
	 */
	private void createItems ()
	{
		removeAllItems ();

		String extension;

		switch (format)
		{
			case FORMAT_SINGULAR:
				extension = ".singular";

				break;

			case FORMAT_PLURAL:
				extension = ".plural";

				break;

			default:
				extension = "";

				break;
		}

		ResourceService res = Engine.instance ().getResourceService ();

		addItem (res.getStringWithoutException ("metix.years" + extension));
		addItem (res.getStringWithoutException ("metix.months" + extension));
		addItem (res.getStringWithoutException ("metix.weeks" + extension));
		addItem (res.getStringWithoutException ("metix.days" + extension));
		addItem (res.getStringWithoutException ("metix.hours" + extension));
		addItem (res.getStringWithoutException ("metix.minutes" + extension));
		addItem (res.getStringWithoutException ("metix.seconds" + extension));
	}
}