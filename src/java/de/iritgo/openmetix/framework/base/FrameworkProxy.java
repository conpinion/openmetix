/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base;


import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectProxy;


/**
 * FrameworkProxy.
 *
 * @version $Id: FrameworkProxy.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class FrameworkProxy
	extends IObjectProxy
{
	/**
	 * Create a new FrameworkProxy.
	 */
	public FrameworkProxy ()
	{
	}

	/**
	 * Create a new FrameworkProxy.
	 */
	public FrameworkProxy (IObject prototypeable)
	{
		super(prototypeable);
	}

	/**
	 * Create a instance of the iritgo object.
	 */
	public IObject create ()
	{
		return new FrameworkProxy();
	}

	/**
	 * Create a new instance of the Proxy.
	 */
	public IObjectProxy createProxy ()
	{
		return new FrameworkProxy();
	}
}