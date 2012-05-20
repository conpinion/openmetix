/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.exporter.command;


import java.util.List;


public interface ExporterCommand
{
	public void setSensorList (List sensors);

	public void setStartDate (long startDate);

	public void setStopDate (long endDate);

	public void setStartYear (long startYear);

	public void setStopYear (long endYear);
}