/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user;


import de.iritgo.openmetix.core.logger.Log;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


/**
 * The user registry conatins all known users.
 *
 * @version $Id: UserRegistry.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class UserRegistry
{
	/** All users currently known to the system. */
	private Map users;

	/**
	 * Create a new empty user registry.
	 */
	public UserRegistry ()
	{
		users = new TreeMap();
	}

	/**
	 * Add the a user to the registry under a specific id.
	 *
	 * @param id The id under which the user should be stored.
	 * @param user The user to add.
	 */
	synchronized public void addUser (Long id, User user)
	{
		if (users.get (id) == null)
		{
			users.put (id, user);

			Log.logDebug (
				"system", "UserRegistry", "Added a new user: " + user.getName () + ":" + id);
		}
	}

	/**
	 * Add the a user to the registry.
	 *
	 * @param user The user to add.
	 */
	synchronized public void addUser (User user)
	{
		if (users.get (new Long(user.getUniqueId ())) == null)
		{
			users.put (new Long(user.getUniqueId ()), user);

			Log.logDebug (
				"system", "UserRegistry",
				"Added a new user " + user.getName () + ":" + user.getUniqueId ());
		}
	}

	/**
	 * Remove a user from the registry.
	 *
	 * @param user The user to remove.
	 */
	synchronized public void removeUser (User user)
	{
		Log.logDebug (
			"system", ".UserRegistry", "Removed user " + user.getName () + ":" +
			user.getUniqueId ());

		users.remove (new Long(user.getUniqueId ()));
	}

	/**
	 * Retrieve a user with a specific id.
	 *
	 * @param id The id of the user to retrieve.
	 * @return The user or null if none was found.
	 */
	synchronized public User getUser (long id)
	{
		return (User) users.get (new Long(id));
	}

	/**
	 * Retrieve a user with a specific id.
	 *
	 * @param id The id of the user to retrieve.
	 * @return The user or null if none was found.
	 */
	synchronized public User getUser (Long id)
	{
		return (User) users.get (id);
	}

	/**
	 * Retrieve a user with a specific name.
	 *
	 * @param name The name of the user to retrieve.
	 * @return The user or null if none was found.
	 */
	synchronized public User getUser (String name)
	{
		for (Iterator i = users.values ().iterator (); i.hasNext ();)
		{
			User user = (User) i.next ();

			if (user.getName ().equals (name))
			{
				return user;
			}
		}

		return null;
	}

	/**
	 * Retrieve a user with a specific email.
	 *
	 * @param email The email of the user to retrieve.
	 * @return The user or null if none was found.
	 */
	synchronized public User getUserByEMail (String email)
	{
		for (Iterator i = users.values ().iterator (); i.hasNext ();)
		{
			User user = (User) i.next ();

			if (user.getEmail ().equals (email))
			{
				return user;
			}
		}

		return null;
	}

	/**
	 * Retrieve a user by its assigned network channel.
	 *
	 * @param networkchannel The network channel of the user to retrieve.
	 * @return The user or null if none was found.
	 */
	synchronized public User getUserByNetworkChannel (double networkchannel)
	{
		for (Iterator i = users.values ().iterator (); i.hasNext ();)
		{
			User user = (User) i.next ();

			if (user.getNetworkChannel () == networkchannel)
			{
				return user;
			}
		}

		return null;
	}

	/**
	 * Get an iterator over all user names.
	 *
	 * @return An user name iterator.
	 */
	synchronized public Iterator nameIterator ()
	{
		return users.keySet ().iterator ();
	}

	/**
	 * Get an iterator over all users.
	 *
	 * @return An user iterator.
	 */
	synchronized public Iterator userIterator ()
	{
		return users.values ().iterator ();
	}

	/**
	 * Check wether this registry contains any users or not.
	 *
	 * @return True if the registry contains users.
	 */
	synchronized public boolean isEmpty ()
	{
		return users.isEmpty ();
	}

	/**
	 * Return the number of user
	 */
	synchronized public int getUserSize ()
	{
		return users.size ();
	}

	/**
	 * Remove all user from the registry.
	 */
	synchronized public void clear ()
	{
		users.clear ();
	}
}