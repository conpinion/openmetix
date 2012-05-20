/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import de.iritgo.openmetix.core.event.EventListener;


/**
 * IObjectListListener.
 *
 * @version $Id: IObjectListListener.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public interface IObjectListListener
	extends EventListener
{
	/**
	 * Event method.
	 *
	 * @param event The event.
	 */
	public void iObjectListEvent (IObjectListEvent event);
}