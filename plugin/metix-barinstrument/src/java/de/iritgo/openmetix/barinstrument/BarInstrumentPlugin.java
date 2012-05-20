/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.barinstrument;


import de.iritgo.openmetix.barinstrument.command.CreateBarInstrument;
import de.iritgo.openmetix.barinstrument.gui.BarInstrumentConfigurator;
import de.iritgo.openmetix.barinstrument.gui.BarInstrumentDisplay;
import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;


/**
 * The BarInstrumentPlugin registers all functionality of the bar
 * instrument plugin.
 *
 * @version $Id: BarInstrumentPlugin.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class BarInstrumentPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.CLIENT, new CreateBarInstrument());
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
		registerDataObject (new BarInstrument());
		registerDataObject (new BarInstrumentSensor());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new BarInstrumentDisplay());
		registerGUIPane (Plugin.CLIENT, new BarInstrumentConfigurator());
	}
}