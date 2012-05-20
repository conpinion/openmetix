/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR

    Copyright (C) 2008 Iritgo Technologies*/
package de.iritgo.openmetix.listinstrument;

import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.listinstrument.command.CreateListInstrument;
import de.iritgo.openmetix.listinstrument.gui.ListInstrumentConfigurator;
import de.iritgo.openmetix.listinstrument.gui.ListInstrumentDisplay;


/**
 * The ListInstrumentPlugin registers all functionality of the list
 * instrument plugin.
 *
 * @version $Id: ListInstrumentPlugin.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ListInstrumentPlugin extends FrameworkPlugin{
	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands(){
		registerCommand(Plugin.CLIENT, new CreateListInstrument());
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects(){
		registerDataObject(new ListInstrument());
		registerDataObject (new ListInstrumentSensor());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new ListInstrumentDisplay());
		registerGUIPane (Plugin.CLIENT, new ListInstrumentConfigurator());
	}
}