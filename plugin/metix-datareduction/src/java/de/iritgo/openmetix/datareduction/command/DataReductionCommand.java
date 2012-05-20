/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


/*
 * Created on 14.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.iritgo.openmetix.datareduction.command;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.datareduction.reduction.DataReductionRequest;
import de.iritgo.openmetix.framework.action.ActionTools;


/**
 * @author Khuong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataReductionCommand
	extends Command
{
	private String timeNumber = new String();
	private int timeIndex;
	private long instrumentUniqueId;
	private long stationID;
	private long sensorID;
	private boolean deleteAllParameters;

	public DataReductionCommand ()
	{
		super("DataReductionCommand");
	}

	public void setTimeNumber (String timeNumber)
	{
		this.timeNumber = timeNumber;
	}

	public void setTimeIndex (int timeIndex)
	{
		this.timeIndex = timeIndex;
	}

	public void setDeleteAllParameters (boolean param)
	{
		this.deleteAllParameters = param;
	}

	public void setInstrumentUniqueId (long instrumentUniqueId)
	{
		this.instrumentUniqueId = instrumentUniqueId;
	}

	public void setStationID (long id)
	{
		this.stationID = id;
	}

	public void setSensorID (long id)
	{
		this.sensorID = id;
	}

	/**
	 * Do whatever the command needs to do.
	 * Subclasses should override this method to provide custom command code.
	 * Override this method if you do not want to return a command result (The
	 * return value defaults to null).
	 */
	public void perform ()
	{
		DataReductionRequest drr =
			new DataReductionRequest(
				instrumentUniqueId, timeNumber, timeIndex, stationID, sensorID, deleteAllParameters);

		ActionTools.sendToServer (drr);
	}
}