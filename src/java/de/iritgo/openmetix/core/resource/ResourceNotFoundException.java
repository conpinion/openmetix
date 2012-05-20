/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource;



/**
 * ResourceNotFoundException.
 *
 * @version $Id: ResourceNotFoundException.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class ResourceNotFoundException
	extends Exception
{
	/** The missing resource name. */
	private String resourceName;

	/**
	 * Create a new ResourceNotFoundException.
	 *
	 * @param resourceName
	 */
	public ResourceNotFoundException (String resourceName)
	{
		this.resourceName = resourceName;
	}

	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage ()
	{
		return resourceName;
	}
}