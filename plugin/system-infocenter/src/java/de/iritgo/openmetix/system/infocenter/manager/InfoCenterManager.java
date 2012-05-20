/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.manager;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.system.infocenter.infocenter.InfoCenterRegistry;


public class InfoCenterManager
	extends BaseObject
	implements Manager
{
	private InfoCenterRegistry infoCenterRegistry;

	public InfoCenterManager ()
	{
		super("infocenter");
	}

	public void init ()
	{
		infoCenterRegistry = new InfoCenterRegistry();
	}

	public void unload ()
	{
		infoCenterRegistry.close ();
	}

	public InfoCenterRegistry getInfoCenterRegistry ()
	{
		return infoCenterRegistry;
	}
}