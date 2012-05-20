/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcexmlparser;


import java.util.Iterator;


/**
 * MethodIterator.
 *
 * @version $Id: MethodIterator.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class MethodIterator
	implements Iterator
{
	private int pos = 0;
	private Object[] methodList;

	public MethodIterator (Object[] methodList)
	{
		this.methodList = methodList;
	}

	public boolean hasNext ()
	{
		return pos < methodList.length;
	}

	public Object next ()
	{
		return methodList[pos++];
	}

	public void remove ()
	{
	}
}