/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.windroseinstrument.command;


import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.windroseinstrument.WindRoseInstrument;


/**
 * This command creates a new wind rose instrument and displays it's
 * configuration editor.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 * </table>
 *
 * @version $Id: CreateWindRoseInstrument.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class CreateWindRoseInstrument
	extends Command
{
	/**
	 * Create a new CreateWindRoseInstrument command.
	 */
	public CreateWindRoseInstrument ()
	{
		super("CreateWindRoseInstrument");
	}

	/**
	 * Perform the command.
	 *
	 * A new wind rose instrument is created and it's configuration editor is
	 * displayed.
	 */
	public void perform ()
	{
		UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();
		WindRoseInstrument windRoseInstrument = new WindRoseInstrument();

		userProfile.addInstrument (windRoseInstrument);
		windRoseInstrument.update ();

		CommandTools.performAsync (
			new ShowDialog("WindRoseInstrumentConfigurator", windRoseInstrument));
	}
}