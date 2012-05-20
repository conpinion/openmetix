/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.webinstrument;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.webinstrument.command.CreateWebInstrument;
import de.iritgo.openmetix.webinstrument.gui.WebInstrumentConfigurator;
import de.iritgo.openmetix.webinstrument.gui.WebInstrumentDisplay;


/**
 * The WebInstrumentPlugin registers all functionality of the web
 * instrument plugin.
 *
 * @version $Id: WebInstrumentPlugin.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class WebInstrumentPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.CLIENT, new CreateWebInstrument());
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
		registerDataObject (new WebInstrument());
		registerDataObject (new WebInstrumentSensor());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new WebInstrumentDisplay());
		registerGUIPane (Plugin.CLIENT, new WebInstrumentConfigurator());
	}
}