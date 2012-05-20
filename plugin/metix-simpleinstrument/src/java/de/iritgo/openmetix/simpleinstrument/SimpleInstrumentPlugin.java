/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.simpleinstrument;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.simpleinstrument.command.CreateSimpleInstrument;
import de.iritgo.openmetix.simpleinstrument.gui.SimpleInstrumentConfigurator;
import de.iritgo.openmetix.simpleinstrument.gui.SimpleInstrumentDisplay;


/**
 * The SimpleInstrumentPlugin registers all functionality of the simple
 * instrument plugin.
 *
 * @version $Id: SimpleInstrumentPlugin.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class SimpleInstrumentPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.CLIENT, new CreateSimpleInstrument());
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
		registerDataObject (new SimpleInstrument());
		registerDataObject (new SimpleInstrumentSensor());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new SimpleInstrumentDisplay());
		registerGUIPane (Plugin.CLIENT, new SimpleInstrumentConfigurator());
	}
}