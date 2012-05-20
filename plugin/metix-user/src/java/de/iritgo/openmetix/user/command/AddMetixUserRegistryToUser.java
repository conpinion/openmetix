/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.user.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.user.manager.MetixUserManager;


/**
 * This command adds the user registry object to a user.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>user</td><td>User</td><td>The user to add the registry to.</td></tr>
 * </table>
 *
 * @version $Id: AddMetixUserRegistryToUser.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class AddMetixUserRegistryToUser
	extends Command
{
	/**
	 * Create a new AddMetixUserRegistryToUser command.
	 */
	public AddMetixUserRegistryToUser ()
	{
		super("AddMetixUserRegistryToUser");
	}

	/**
	 * Execute the command.
	 */
	public void perform ()
	{
		User user = (User) properties.get ("user");

		MetixUserManager metixUserManager =
			(MetixUserManager) Engine.instance ().getManagerRegistry ().getManager ("MetixUser");

		user.putNamedIritgoObject ("MetixUserRegistry", metixUserManager.getMetixUserRegistry ());
	}
}