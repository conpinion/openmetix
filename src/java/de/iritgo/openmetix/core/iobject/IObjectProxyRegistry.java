/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import java.util.HashMap;
import java.util.Iterator;


/**
 * IObjectProxyRegistry.
 *
 * @version $Id: IObjectProxyRegistry.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectProxyRegistry
{
	/** All proxies. */
	private HashMap proxys;

	/**
	 * Create a new IObjectProxyRegistry.
	 */
	public IObjectProxyRegistry ()
	{
		proxys = new HashMap();
	}

	/**
	 * Add a proxy.
	 */
	public void addProxy (IObjectProxy proxy)
	{
		proxys.put (new Long(proxy.getUniqueId ()), proxy);
	}

	/**
	 * Get a proxy.
	 *
	 * @param uniqueId The uniqueid of the proxy.
	 * @return The proxy.
	 */
	public IObjectProxy getProxy (long uniqueId)
	{
		return (IObjectProxy) proxys.get (new Long(uniqueId));
	}

	/**
	 * Remove a proxy.
	 */
	public void removeProxy (IObjectProxy proxy)
	{
		proxys.remove (new Long(proxy.getUniqueId ()));
	}

	/**
	 * Set the proxy to invalid.
	 */
	public void setInvalidState ()
	{
		for (Iterator i = proxys.values ().iterator (); i.hasNext ();)
		{
			IObjectProxy proxy = (IObjectProxy) i.next ();

			proxy.setUpToDate (false);
		}
	}

	/**
	 * Clear the proxy.
	 */
	public void clear ()
	{
		proxys.clear ();
	}
}