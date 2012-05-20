/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.roundinstrument;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.roundinstrument.command.CreateRoundInstrument;
import de.iritgo.openmetix.roundinstrument.gui.RoundInstrumentConfigurator;
import de.iritgo.openmetix.roundinstrument.gui.RoundInstrumentDisplay;


/**
 * The RoundInstrumentPlugin registers all functionality of the round
 * instrument plugin.
 *
 * @version $Id: RoundInstrumentPlugin.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class RoundInstrumentPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.CLIENT, new CreateRoundInstrument());
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
		registerDataObject (new RoundInstrument());
		registerDataObject (new RoundInstrumentSensor());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new RoundInstrumentDisplay());
		registerGUIPane (Plugin.CLIENT, new RoundInstrumentConfigurator());
	}
}