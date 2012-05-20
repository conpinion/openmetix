/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.command;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.user.User;


/**
 * PingPong.
 *
 * @version $Id: PingPong.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class PingPong
	extends Command
{
	public PingPong ()
	{
		super("pingpong");
	}

	public void perform ()
	{
		User user = AppContext.instance ().getUser ();
	}
}