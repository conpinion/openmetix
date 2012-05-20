/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource;



/**
 * ResourcePersistent.
 *
 * @version $Id: ResourcePersistent.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class ResourcePersistent
	extends ResourceNode
{
	/** The resource path. */
	private Object resourcePath;

	/** The resource loader. */
	private ResourceLoader resourceLoader;

	/**
	 * Create a new ResourcePersistent.
	 *
	 * @param name The description of the resource.
	 * @param nodeName The node name.
	 */
	public ResourcePersistent (String name, String nodeName, Object resourcePath)
	{
		super(name, nodeName);
		this.resourcePath = resourcePath;
	}

	/**
	 * Set the resource loader.
	 *
	 * @param resourceLoader The resource loader.
	 */
	public void setResourceLoader (ResourceLoader resourceLoader)
	{
		this.resourceLoader = resourceLoader;
	}

	/**
	 * Get the resource loader.
	 *
	 * @return The resource loader.
	 */
	public ResourceLoader getResourceLoader ()
	{
		return resourceLoader;
	}

	/**
	 * Get the resource path.
	 *
	 * @return The resource path.
	 */
	public Object getResourcePath ()
	{
		return resourcePath;
	}
}