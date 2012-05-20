/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.linechart.command;


import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.linechart.LineChart;


/**
 * This command creates a new line chart and displays it's
 * configuration editor.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 * </table>
 *
 * @version $Id: CreateLineChart.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class CreateLineChart
	extends Command
{
	/**
	 * Create a new CreateLineChart command.
	 */
	public CreateLineChart ()
	{
		super("CreateLineChart");
	}

	/**
	 * Perform the command.
	 *
	 * A new line chart is created and it's configuration editor is
	 * displayed.
	 */
	public void perform ()
	{
		UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();
		LineChart linechart = new LineChart();

		userProfile.addInstrument (linechart);
		linechart.update ();
		CommandTools.performAsync (new ShowDialog("LineChartConfigurator", linechart));
	}
}