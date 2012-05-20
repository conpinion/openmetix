/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.tools;


import java.util.Iterator;
import java.util.StringTokenizer;


/**
 * NamePartIterator.
 *
 * @version $Id: NamePartIterator.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class NamePartIterator
	implements Iterator
{
	private StringTokenizer tokens;

	public NamePartIterator (String name)
	{
		tokens = new StringTokenizer(name, ".");
	}

	public boolean hasNext ()
	{
		return tokens.hasMoreTokens ();
	}

	public Object next ()
	{
		return tokens.nextToken ();
	}

	public void remove ()
	{
	}
}