/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.exporter;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.exporter.gui.ExporterWizard;
import de.iritgo.openmetix.exporter.manager.ExporterManager;
import de.iritgo.openmetix.exporter.minmidmax.MinMidMaxExportCommand;
import de.iritgo.openmetix.exporter.minmidmax.MinMidMaxQueryRequest;
import de.iritgo.openmetix.exporter.minmidmax.MinMidMaxQueryResponse;
import de.iritgo.openmetix.exporter.minmidmax.MinMidMaxResultGUIPane;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;


public class ExporterPlugin
	extends FrameworkPlugin
{
	protected void registerDataObjects ()
	{
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new ExporterWizard());
		registerGUIPane (Plugin.CLIENT, new MinMidMaxResultGUIPane());
	}

	/**
	 * Register all commands in this method.
	 */
	protected void registerActions ()
	{
		registerAction (new MinMidMaxQueryRequest());
		registerAction (new MinMidMaxQueryResponse());
	}

	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.CLIENT, new MinMidMaxExportCommand());
	}

	/**
	 * Register all managers in this method.
	 */
	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new ExporterManager());
	}
}