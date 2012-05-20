/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.barinstrument.command;


import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.barinstrument.BarInstrument;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;


/**
 * This command creates a new bar instrument and displays it's
 * configuration editor.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 * </table>
 *
 * @version $Id: CreateBarInstrument.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class CreateBarInstrument
	extends Command
{
	/**
	 * Create a new CreateBarInstrument command.
	 */
	public CreateBarInstrument ()
	{
		super("CreateBarInstrument");
	}

	/**
	 * Perform the command.
	 *
	 * A new bar instrument is created and it's configuration editor is
	 * displayed.
	 */
	public void perform ()
	{
		UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();

		BarInstrument barInstrument = new BarInstrument();

		userProfile.addInstrument (barInstrument);
		barInstrument.update ();

		CommandTools.performAsync (new ShowDialog("BarInstrumentConfigurator", barInstrument));
	}
}