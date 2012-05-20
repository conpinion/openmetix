/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.user.command;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.user.MetixUser;


/**
 * This command adds the Metix user object to a user.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>user</td><td>User</td><td>The user to add the Metix user to.</td></tr>
 *   <tr><td>metixUser</td><td>MetixUser</td><td>The Metix user to add.</td></tr>
 * </table>
 *
 * @version $Id: AddMetixUserToUser.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class AddMetixUserToUser
	extends Command
{
	/**
	 * Create a new AddMetixUserToUser command.
	 */
	public AddMetixUserToUser ()
	{
		super("AddMetixUserToUser");
	}

	/**
	 * Execute the command.
	 */
	public void perform ()
	{
		User user = (User) properties.get ("user");
		MetixUser metixUser = (MetixUser) properties.get ("metixUser");

		user.putNamedIritgoObject("MetixUser", metixUser);
	}
}