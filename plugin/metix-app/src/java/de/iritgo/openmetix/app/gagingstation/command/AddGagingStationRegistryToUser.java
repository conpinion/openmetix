/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.command;


import de.iritgo.openmetix.app.manager.CoreManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.user.User;


/**
 * This command adds the gaging station registry to a user object.
 *
 * @version $Id: AddGagingStationRegistryToUser.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class AddGagingStationRegistryToUser
	extends Command
{
	/**
	 * Create a new AddGagingStationRegistryToUser command.
	 */
	public AddGagingStationRegistryToUser ()
	{
		super("AddGagingStationRegistryToUser");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		User user = (User) properties.get ("user");

		CoreManager coreManager =
			(CoreManager) Engine.instance ().getManagerRegistry ().getManager ("CoreManager");

		user.putNamedIritgoObject (
			"GagingStationRegistry", coreManager.getGagingStationRegistry ());
	}
}