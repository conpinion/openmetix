/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;



/**
 * IObjectProxyEventCache.
 *
 * @version $Id: IObjectProxyEventCache.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectProxyEventCache
{
	/** Number of unfired events. */
	private int unfired;

	/**
	 * Create a new IObjectProxyEventCache.
	 */
	public IObjectProxyEventCache ()
	{
	}

	/**
	 * Add an unfired event.
	 */
	public void add ()
	{
		++unfired;
	}

	/**
	 * Get the number of unfired events.
	 *
	 * @return The number of unfired events.
	 */
	public int get ()
	{
		return unfired;
	}
}