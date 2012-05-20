/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.shutdown;


import de.iritgo.openmetix.framework.user.User;


/**
 * ShutdownObserver.
 *
 * @version $Id: ShutdownObserver.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public interface ShutdownObserver
{
	public String getTypeId ();

	public void onUserLogoff (User user);

	public void onShutdown ();
}