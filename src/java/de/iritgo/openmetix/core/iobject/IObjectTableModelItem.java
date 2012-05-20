/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;



/**
 * IObjectTableModelItem.
 *
 * @version $Id: IObjectTableModelItem.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectTableModelItem
{
	/** Cell position. */
	public int x;

	/** Cell position. */
	public int y;

	/** Unique id. */
	public long uniqueId;

	/**
	 * Create a new IObjectTableModelItem.
	 *
	 * @param x
	 * @param y
	 * @param uniqueId
	 */
	public IObjectTableModelItem (int x, int y, long uniqueId)
	{
		this.x = x;
		this.y = y;
		this.uniqueId = uniqueId;
	}
}