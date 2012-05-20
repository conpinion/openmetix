/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.guinetworkdisplay.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.system.infocenter.command.CommonInfoCenterCommand;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.DiskWriterDisplay;
import de.iritgo.openmetix.system.infocenter.manager.InfoCenterManager;


public class CreateDiskWriterDisplay
	extends CommonInfoCenterCommand
{
	public CreateDiskWriterDisplay ()
	{
		super("creatediskwriterdisplay");
	}

	public void perform ()
	{
		InfoCenterManager infoCenterManager =
			(InfoCenterManager) Engine.instance ().getManagerRegistry ().getManager ("infocenter");

		DiskWriterDisplay diskWriterDisplay = new DiskWriterDisplay();

		diskWriterDisplay.setInfoStoreFile ("FILE");

		infoCenterManager.getInfoCenterRegistry ().addDisplay (diskWriterDisplay, context);
		infoCenterManager.getInfoCenterRegistry ().addDisplay (
			category, diskWriterDisplay.getId (), context, null);
	}
}