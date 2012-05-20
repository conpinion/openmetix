/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.roundinstrument.command;


import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.roundinstrument.RoundInstrument;


/**
 * This command creates a new round instrument and displays it's
 * configuration editor.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 * </table>
 *
 * @version $Id: CreateRoundInstrument.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class CreateRoundInstrument
	extends Command
{
	/**
	 * Create a new CreateRoundInstrument command.
	 */
	public CreateRoundInstrument ()
	{
		super("CreateRoundInstrument");
	}

	/**
	 * Perform the command.
	 *
	 * A new round instrument is created and it's configuration editor is
	 * displayed.
	 */
	public void perform ()
	{
		UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();
		RoundInstrument roundInstrument = new RoundInstrument();

		userProfile.addInstrument (roundInstrument);
		roundInstrument.update ();

		CommandTools.performAsync (new ShowDialog("RoundInstrumentConfigurator", roundInstrument));
	}
}