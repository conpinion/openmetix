/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcetypes;


import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.resource.ResourceNotFoundException;
import de.iritgo.openmetix.core.resource.ResourcePersistent;


/**
 * ResourceObject.
 *
 * @version $Id: ResourceObject.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class ResourceObject
	extends ResourcePersistent
{
	/** The resource. */
	private String string;

	/**
	 * Create a new ResourceObject.
	 *
	 * @param name The description of the resource.
	 * @param nodeName The node Name
	 */
	public ResourceObject (String name, String nodeName, String resourcePath)
	{
		super(name, nodeName, resourcePath);
	}

	/**
	 * Return the object.
	 *
	 * @return The object.
	 */
	public Object getClone ()
		throws ResourceNotFoundException
	{
		Object object = null;

		try
		{
			object = ((Class) Class.forName ((String) getResourcePath ())).newInstance ();
		}
		catch (Exception e)
		{
			e.printStackTrace ();

			Log.log (
				"resource", "ResourceService.addNode",
				"ResourceLoader not found:(C)" + e.getMessage (), 5);
			throw new ResourceNotFoundException(
				("ClassNotFoundException: " + (String) getResourcePath ()));
		}

		return object;
	}
}