/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.sessioncontext;


import de.iritgo.openmetix.core.base.BaseObject;


/**
 * StringContext.
 *
 * @version $Id: StringContext.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class StringContext
	extends BaseObject
{
	private String string;

	public StringContext (String id, String string)
	{
		super(id);
		this.string = string;
	}

	public String getString ()
	{
		return string;
	}
}