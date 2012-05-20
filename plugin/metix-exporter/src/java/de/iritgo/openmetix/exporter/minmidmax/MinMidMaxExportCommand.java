/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.exporter.minmidmax;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.exporter.command.ExporterCommand;
import de.iritgo.openmetix.framework.action.ActionTools;
import java.util.List;


public class MinMidMaxExportCommand
	extends Command
	implements ExporterCommand
{
	private List sensors;
	private long startDate;
	private long stopDate;
	private long instrumentUniqueId;
	private long startYear;
	private long stopYear;

	public MinMidMaxExportCommand ()
	{
		super("MinMidMaxExportCommand");
	}

	public void setSensorList (List sensors)
	{
		this.sensors = sensors;
	}

	public void setStartDate (long startDate)
	{
		this.startDate = startDate;
	}

	public void setStopDate (long stopDate)
	{
		this.stopDate = stopDate;
	}

	public void setStartYear (long startYear)
	{
		this.startYear = startYear;
	}

	public void setStopYear (long stopYear)
	{
		this.stopYear = stopYear;
	}

	public void setInstrumentUniqueId (long instrumentUniqueId)
	{
		this.instrumentUniqueId = instrumentUniqueId;
	}

	/**
	 * Do whatever the command needs to do.
	 * Subclasses should override this method to provide custom command code.
	 * Override this method if you do not want to return a command result (The
	 * return value defaults to null).
	 */
	public void perform ()
	{
		MinMidMaxQueryRequest mmmqrq =
			new MinMidMaxQueryRequest(
				instrumentUniqueId, sensors, startDate, stopDate, startYear, stopYear);

		ActionTools.sendToServer (mmmqrq);
	}
}