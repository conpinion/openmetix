/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user;


import de.iritgo.openmetix.core.event.EventListener;


/**
 * NewUserListener.
 *
 * @version $Id: NewUserListener.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public interface NewUserListener
	extends EventListener
{
	public void newUserEvent (NewUserEvent event);
}