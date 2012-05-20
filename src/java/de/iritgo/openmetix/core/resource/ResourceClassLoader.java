/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource;



/**
 * ResourceClassLoader.
 *
 * @version $Id: ResourceClassLoader.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class ResourceClassLoader
	extends ClassLoader
{
	/**
	 * Create a new ResourceClassLoader.
	 */
	public ResourceClassLoader (ClassLoader cl)
	{
		super(cl);
	}

	/**
	 * Create a new object.
	 *
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Object createObject (String name)
		throws Exception
	{
		Class foundClass = super.findClass (name);

		return foundClass.newInstance ();
	}
}