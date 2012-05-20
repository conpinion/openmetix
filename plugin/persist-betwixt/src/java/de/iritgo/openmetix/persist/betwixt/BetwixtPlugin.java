/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.betwixt;


import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.persist.betwixt.command.LoadBaseObjects;
import de.iritgo.openmetix.persist.betwixt.command.LoadBetwixtBean;
import de.iritgo.openmetix.persist.betwixt.command.SaveBaseObjects;
import de.iritgo.openmetix.persist.betwixt.command.SaveBetwixtBean;


public class BetwixtPlugin extends FrameworkPlugin{
	protected void registerDataObjects (){
	}

	protected void registerActions (){
	}

	protected void registerGUIPanes (){
	}

	protected void registerManagers (){
	}

	protected void registerCommands ()
	{
		registerCommand (new LoadBetwixtBean());
		registerCommand (new SaveBetwixtBean());
		registerCommand (new LoadBaseObjects());
		registerCommand (new SaveBaseObjects());
	}

	protected void registerConsoleCommands ()
	{
	}
}