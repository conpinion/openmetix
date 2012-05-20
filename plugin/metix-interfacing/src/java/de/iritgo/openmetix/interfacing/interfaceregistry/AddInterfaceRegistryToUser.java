/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.interfaceregistry;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.interfacing.InterfacingManager;


/**
 * This command adds the interface registry object to a user.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>user</td><td>User</td><td>The user to add the registry to.</td></tr>
 * </table>
 *
 * @version $Id: AddInterfaceRegistryToUser.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class AddInterfaceRegistryToUser
	extends Command
{
	/**
	 * Create a new AddInterfaceRegistryToUser command.
	 */
	public AddInterfaceRegistryToUser ()
	{
		super("AddInterfaceRegistryToUser");
	}

	/**
	 * Execute the command.
	 */
	public void perform ()
	{
		User user = (User) properties.get ("user");

		InterfacingManager interfacingManager =
			(InterfacingManager) Engine.instance ().getManagerRegistry ().getManager (
				"InterfacingManager");

		user.putNamedIritgoObject ("InterfaceRegistry", interfacingManager.getInterfaceRegistry ());
	}
}