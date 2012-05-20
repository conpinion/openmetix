/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.user;


import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import java.util.Iterator;


/**
 * This registry contains all users and their profiles.
 *
 * @version $Id: MetixUserRegistry.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class MetixUserRegistry
	extends DataObject
{
	/**
	 * Create a new MetixUserRegistry.
	 */
	public MetixUserRegistry ()
	{
		super("MetixUserRegistry");

		putAttribute ("users", new IObjectList("users", new FrameworkProxy(new MetixUser()), this));
		putAttribute (
			"profiles", new IObjectList("profiles", new FrameworkProxy(new UserProfile()), this));
	}

	/**
	 * Create a new MetixUserRegistry.
	 *
	 * @param uniqueId The registry's unique id.
	 */
	public MetixUserRegistry (long uniqueId)
	{
		this();
		setUniqueId (uniqueId);
	}

	/**
	 * Add a user to the registry.
	 *
	 * @param user The user to add.
	 */
	public void addUser (MetixUser user)
	{
		getIObjectListAttribute ("users").add (user);
	}

	/**
	 * Remove a user from the registry.
	 *
	 * @param user The user to remove.
	 */
	public void removeUser (MetixUser user)
	{
		getIObjectListAttribute ("users").remove (user);
	}

	/**
	 * Retrieve the user at a specified index.
	 *
	 * @param index The index of the user to retrieve.
	 * @return The user at a specified index.
	 */
	public MetixUser getUser (int index)
	{
		return (MetixUser) getIObjectListAttribute ("users").get (index);
	}

	/**
	 * Get an iterator over all users.
	 *
	 * @return A user iterator.
	 */
	public Iterator getUserIterator ()
	{
		return getIObjectListAttribute ("users").iterator ();
	}

	/**
	 * Find a user by name.
	 *
	 * @param name The name of the user to find.
	 * @return The user or null if no user with the given name exists.
	 */
	public MetixUser findUserByName (String name)
	{
		for (Iterator i = getUserIterator (); i.hasNext ();)
		{
			MetixUser user = (MetixUser) i.next ();

			if (user.getName ().equals (name))
			{
				return user;
			}
		}

		return null;
	}

	/**
	 * Get the number of users currently stored in the registry.
	 *
	 * @return The user count.
	 */
	public int getUserCount ()
	{
		return getIObjectListAttribute ("users").size ();
	}

	/**
	 * Add a user profile to the registry.
	 *
	 * @param profile The profile to add.
	 */
	public void addProfile (UserProfile profile)
	{
		getIObjectListAttribute ("profiles").add (profile);
	}

	/**
	 * Retrieve the user profile at a specified index.
	 *
	 * @param index The index of the user profile to retrieve.
	 * @return The user profile at a specified index.
	 */
	public UserProfile getProfile (int index)
	{
		return (UserProfile) getIObjectListAttribute ("profiles").get (index);
	}

	/**
	 * Get an iterator over all user profiles.
	 *
	 * @return A user profile iterator.
	 */
	public Iterator getProfileIterator ()
	{
		return getIObjectListAttribute ("profiles").iterator ();
	}

	/**
	 * Get the number of user profiles currently stored in the registry.
	 *
	 * @return The user profile count.
	 */
	public int getProfileCount ()
	{
		return getIObjectListAttribute ("profiles").size ();
	}
}