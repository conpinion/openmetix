/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.user.command;


import de.iritgo.openmetix.app.userprofile.Preferences;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.user.manager.MetixUserManager;
import java.util.Properties;


/**
 * This command adds a user profile object to a user.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>user</td><td>User</td><td>The user to add the profile to.</td></tr>
 * </table>
 *
 * @version $Id: AddUserProfileToUser.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class AddUserProfileToUser extends Command{
	/**
	 * Create a new AddUserProfileToUser command.
	 */
	public AddUserProfileToUser(){
		super("AddUserProfileToUser");
	}

	/**
	 * Execute the command.
	 */
	public void perform(){
		User user = (User) properties.get("user");

		UserProfile userProfile = new UserProfile();

		userProfile.setUniqueId(Engine.instance ().getPersistentIDGenerator ()
									  .createId ());

		MetixUserManager metixUserManager = (MetixUserManager) Engine.instance ()
																	 .getManagerRegistry ()
																	 .getManager ("MetixUser");

		metixUserManager.getMetixUserRegistry ().addProfile (userProfile);

		Properties props = new Properties();

		props.put ("dataobject", userProfile);
		props.put ("owner", metixUserManager.getMetixUserRegistry ());
		props.put ("listattribute", "profiles");
		CommandTools.performSimple ("persist.StoreObject", props);

		Preferences preferences = new Preferences();

		preferences.setUniqueId (Engine.instance ().getPersistentIDGenerator ().createId ());
		userProfile.addPreferences (preferences);

		props = new Properties();
		props.put ("dataobject", preferences);
		props.put ("owner", userProfile);
		props.put ("listattribute", "preferences");
		CommandTools.performSimple ("persist.StoreObject", props);

		user.putNamedIritgoObject ("UserProfile", userProfile);
		user.putNamedIritgoObject ("Preferences", preferences);
	}
}
