/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user;


import de.iritgo.openmetix.core.event.Event;


/**
 * UserEvent.
 *
 * @version $Id: UserEvent.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class UserEvent
	implements Event
{
	private User user;
	private boolean loggedIn = false;

	public UserEvent (User user)
	{
		this.user = user;
	}

	public UserEvent (User user, boolean loggedIn)
	{
		this.user = user;
		this.loggedIn = loggedIn;
	}

	public boolean isLoggedIn ()
	{
		return loggedIn;
	}

	public User getUser ()
	{
		return user;
	}
}