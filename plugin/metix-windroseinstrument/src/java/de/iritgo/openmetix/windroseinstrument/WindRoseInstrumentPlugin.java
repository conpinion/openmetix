/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.windroseinstrument;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.windroseinstrument.command.CreateWindRoseInstrument;
import de.iritgo.openmetix.windroseinstrument.gui.WindRoseInstrumentConfigurator;
import de.iritgo.openmetix.windroseinstrument.gui.WindRoseInstrumentDisplay;


/**
 * The WindRoseInstrumentPlugin registers all functionality of the wind rose
 * instrument plugin.
 *
 * @version $Id: WindRoseInstrumentPlugin.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class WindRoseInstrumentPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.CLIENT, new CreateWindRoseInstrument());
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
		registerDataObject (new WindRoseInstrument());
		registerDataObject (new WindRoseInstrumentSensor());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new WindRoseInstrumentDisplay());
		registerGUIPane (Plugin.CLIENT, new WindRoseInstrumentConfigurator());
	}
}