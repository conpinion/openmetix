/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;



/**
 * NoSuchIObjectException.
 *
 * @version $Id: NoSuchIObjectException.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class NoSuchIObjectException
	extends Exception
{
	/**
	 * Create a new NoSuchIObjectException.
	 *
	 * @param classID The id of the missing object.
	 */
	public NoSuchIObjectException (String classID)
	{
		super("Prototype " + classID + " not found.");
	}
}