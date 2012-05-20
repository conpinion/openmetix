/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.betwixt.manager;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;


public class BetwixtManager
	extends BaseObject
	implements Manager
{
	public BetwixtManager ()
	{
		super("persist-betwixt");
	}

	public void init ()
	{
	}

	public void unload ()
	{
	}
}