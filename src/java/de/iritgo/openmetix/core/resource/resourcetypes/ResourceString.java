/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcetypes;


import de.iritgo.openmetix.core.resource.ResourceNode;


/**
 * ResourceString.
 *
 * @version $Id: ResourceString.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class ResourceString
	extends ResourceNode
{
	/** The resource string. */
	private String string;

	/**
	 * Create a new ResourceString.
	 *
	 * @param desciption The description of the resource.
	 * @param nodeName The node name.
	 */
	public ResourceString (String desciption, String nodeName)
	{
		super(desciption, nodeName);
	}

	/**
	 * Return the resource.
	 *
	 * @return The resource string.
	 */
	public String getValue ()
	{
		return getDescription ();
	}
}