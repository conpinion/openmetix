/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.guinetworkdisplay.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.system.infocenter.command.CommonInfoCenterCommand;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.NetworkDisplay;
import de.iritgo.openmetix.system.infocenter.manager.InfoCenterManager;


public class RemoveGUINetworkDisplay
	extends CommonInfoCenterCommand
{
	public RemoveGUINetworkDisplay ()
	{
		super("removeguinetworkdisplay");
	}

	public void perform ()
	{
		InfoCenterManager infoCenterManager =
			(InfoCenterManager) Engine.instance ().getManagerRegistry ().getManager ("infocenter");

		NetworkDisplay networkDisplay = new NetworkDisplay();

		infoCenterManager.getInfoCenterRegistry ().removeDisplay (networkDisplay.getId (), context);
	}
}