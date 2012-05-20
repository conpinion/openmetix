/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.shutdown.ShutdownManager;


/**
 * UserLogoffAction.
 *
 * @version $Id: UserLogoffAction.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class UserLogoffAction
	extends FrameworkAction
{
	public UserLogoffAction ()
	{
	}

	public void perform ()
	{
		((ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown")).userLogoffActionPerformend ();
	}
}