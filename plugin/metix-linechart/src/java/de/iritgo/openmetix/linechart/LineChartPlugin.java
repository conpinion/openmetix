/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.linechart;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.linechart.command.CreateLineChart;
import de.iritgo.openmetix.linechart.gui.LineChartConfigurator;
import de.iritgo.openmetix.linechart.gui.LineChartDisplay;


/**
 * The BarInstrumentPlugin registers all functionality of the bar
 * instrument plugin.
 *
 * @version $Id: LineChartPlugin.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class LineChartPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.CLIENT, new CreateLineChart());
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
		registerDataObject (new LineChart());
		registerDataObject (new LineChartSensor());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new LineChartDisplay());
		registerGUIPane (Plugin.CLIENT, new LineChartConfigurator());
	}
}