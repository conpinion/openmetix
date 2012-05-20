/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.manager;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.gui.GUIDisplay;


public class InfoCenterClientManager
	extends BaseObject
	implements Manager
{
	private GUIDisplay guiDisplay;

	public InfoCenterClientManager ()
	{
		super("system-infocenterclient");
	}

	public void init ()
	{
	}

	public void unload ()
	{
	}

	public GUIDisplay getGUIDisplay ()
	{
		return guiDisplay;
	}

	public void setGUIDisplay (GUIDisplay guiDisplay)
	{
		this.guiDisplay = guiDisplay;
	}
}