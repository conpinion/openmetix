/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.simpleinstrument.command;


import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.simpleinstrument.SimpleInstrument;


/**
 * This command creates a new simple instrument and displays it's
 * configuration editor.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 * </table>
 *
 * @version $Id: CreateSimpleInstrument.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class CreateSimpleInstrument
	extends Command
{
	/**
	 * Create a new CreateSimpleInstrument command.
	 */
	public CreateSimpleInstrument ()
	{
		super("CreateSimpleInstrument");
	}

	/**
	 * Perform the command.
	 *
	 * A new simple instrument is created and it's configuration editor is
	 * displayed.
	 */
	public void perform ()
	{
		UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();
		SimpleInstrument simpleInstrument = new SimpleInstrument();

		userProfile.addInstrument (simpleInstrument);
		simpleInstrument.update ();

		CommandTools.performAsync (
			new ShowDialog("SimpleInstrumentConfigurator", simpleInstrument));
	}
}