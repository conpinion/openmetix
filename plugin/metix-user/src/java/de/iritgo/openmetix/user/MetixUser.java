/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.user;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.framework.base.DataObject;


/**
 * Data object that implements a metix application user.
 *
 * @version $Id: MetixUser.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class MetixUser
	extends DataObject
{
	/** User role for normal application usage. */
	public static final int ROLE_NORMAL = 0;

	/** This role only allows a read only view of the instruments. */
	public static final int ROLE_READONLY = 1;

	/** This role is reserved to administration tasks. */
	public static final int ROLE_ADMIN = 2;

	/**
	 * Create a new MetixUser.
	 */
	public MetixUser ()
	{
		super("MetixUser");

		putAttribute ("name", "");
		putAttribute ("password", "");
		putAttribute ("email", "");
		putAttribute ("role", ROLE_NORMAL);
	}

	/**
	 * Create a new MetixUser.
	 *
	 * @param uniqueId The user's unique id.
	 */
	public MetixUser (long uniqueId)
	{
		this();
		setUniqueId (uniqueId);
	}

	/**
	 * Get the user name.
	 *
	 * @return The user name.
	 */
	public String getName ()
	{
		return getStringAttribute ("name");
	}

	/**
	 * Set the user name.
	 *
	 * @param name The new user name.
	 */
	public void setName (String name)
	{
		putAttribute ("name", name);
	}

	/**
	 * Get the user password.
	 *
	 * @return The user password.
	 */
	public String getPassword ()
	{
		return getStringAttribute ("password");
	}

	/**
	 * Set the user password.
	 *
	 * @param password The new user password.
	 */
	public void setPassword (String password)
	{
		putAttribute ("password", password);
	}

	/**
	 * Get the user's email.
	 *
	 * @return The user's email.
	 */
	public String getEmail ()
	{
		return getStringAttribute ("email");
	}

	/**
	 * Set the user's email.
	 *
	 * @param email The new user email.
	 */
	public void setEmail (String email)
	{
		putAttribute ("email", email);
	}

	/**
	 * Get the user's role.
	 *
	 * @return The user's role.
	 */
	public int getRole ()
	{
		return getIntAttribute ("role");
	}

	/**
	 * Get the user's role as a string.
	 *
	 * @return The user's role as a string.
	 */
	public String getRoleString ()
	{
		switch (getRole ())
		{
			case ROLE_NORMAL:
				return Engine.instance ().getResourceService ().getStringWithoutException (
					"metix-user.roleNormal");

			case ROLE_READONLY:
				return Engine.instance ().getResourceService ().getStringWithoutException (
					"metix-user.roleReadOnly");

			case ROLE_ADMIN:
				return Engine.instance ().getResourceService ().getStringWithoutException (
					"metix-user.roleAdmin");

			default:
				return "---";
		}
	}

	/**
	 * Set the user's role.
	 *
	 * @param role The new user role.
	 */
	public void setRole (int role)
	{
		putAttribute ("role", role);
	}
}