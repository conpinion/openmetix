/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.listinstrument;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.instrument.Instrument;


/**
 * Data object that represents a list instrument.
 *
 * @version $Id: ListInstrument.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ListInstrument
	extends Instrument
{
	/** The default empty sensor. */
	private ListInstrumentSensor emptySensor = new ListInstrumentSensor();

	/**
	 * Create a new ListInstrument.
	 */
	public ListInstrument ()
	{
		super("ListInstrument");

		putAttribute ("showStationColumn", new Integer(1));
		putAttribute ("showDateColumn", new Integer(1));
	}

	/**
	 * Do we show the station column?
	 *
	 * @return True if we show the station column.
	 */
	public boolean showStationColumn ()
	{
		return getIntAttribute ("showStationColumn") == 1;
	}

	/**
	 * Determine wether to show the station column or not.
	 *
	 * @param showStationColumn True if we should show the station column.
	 */
	public void setShowStationColumn (boolean showStationColumn)
	{
		putAttribute ("showStationColumn", showStationColumn ? 1 : 0);
	}

	/**
	 * Do we show the date column?
	 *
	 * @return True if we show the date column.
	 */
	public boolean showDateColumn ()
	{
		return getIntAttribute ("showDateColumn") == 1;
	}

	/**
	 * Determine wether to show the date column or not.
	 *
	 * @param showDateColumn True if we should show the date column.
	 */
	public void setShowDateColumn (boolean showDateColumn)
	{
		putAttribute ("showDateColumn", showDateColumn ? 1 : 0);
	}

	/**
	 * Get the default empty sensor.
	 *
	 * @return The empty sensor.
	 */
	public ConfigurationSensor getTmpSensor ()
	{
		return emptySensor;
	}
}