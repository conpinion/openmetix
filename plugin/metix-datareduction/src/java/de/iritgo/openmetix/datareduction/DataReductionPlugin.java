/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.datareduction;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.datareduction.command.DataReductionCommand;
import de.iritgo.openmetix.datareduction.gui.DataReductionConfigurator;
import de.iritgo.openmetix.datareduction.gui.DataReductionDisplay;
import de.iritgo.openmetix.datareduction.reduction.DataReductionRequest;
import de.iritgo.openmetix.datareduction.reduction.DataReductionResponse;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;


/**
 * The SimpleInstrumentPlugin registers all functionality of the simple
 * instrument plugin.
 *
 * @version $Id: DataReductionPlugin.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class DataReductionPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.CLIENT, new DataReductionCommand());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new DataReductionConfigurator());
		registerGUIPane (Plugin.CLIENT, new DataReductionDisplay());
	}

	/**
	 * Register all commands in this method.
	 */
	protected void registerActions ()
	{
		registerAction (new DataReductionRequest());
		registerAction (new DataReductionResponse());
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
		registerDataObject (new DataReduction());
	}
}