/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.server.command;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import java.util.Iterator;


/**
 * ShowUsers.
 *
 * @version $Id: ShowUsers.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ShowUsers
	extends Command
{
	public ShowUsers ()
	{
	}

	public void perform ()
	{
		UserRegistry users = Server.instance ().getUserRegistry ();

		for (Iterator i = users.userIterator (); i.hasNext ();)
		{
			User user = (User) i.next ();

			char stateChar = '-';

			if (user.isOnline ())
			{
				stateChar = '*';
			}

			System.out.println ("(" + stateChar + ") " + user.getName ());
		}
	}
}