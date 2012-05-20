/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.event.Event;


/**
 * NewUserEvent.
 *
 * @version $Id: NewUserEvent.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class NewUserEvent
	extends BaseObject
	implements Event
{
	private User user;

	public NewUserEvent (User user)
	{
		this.user = user;
	}

	public User getUser ()
	{
		return user;
	}
}