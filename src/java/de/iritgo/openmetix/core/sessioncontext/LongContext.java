/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.sessioncontext;


import de.iritgo.openmetix.core.base.BaseObject;


/**
 * LongContext.
 *
 * @version $Id: LongContext.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class LongContext
	extends BaseObject
{
	private Long longValue;

	public LongContext (Long longValue)
	{
		super("longcontext");
		this.longValue = longValue;
	}

	public LongContext (long longValue)
	{
		super("longcontext");
		this.longValue = new Long(longValue);
	}

	public LongContext (String id, Long longValue)
	{
		super(id);
		this.longValue = longValue;
	}

	public LongContext (String id, long longValue)
	{
		super(id);
		this.longValue = new Long(longValue);
	}

	public Long getLong ()
	{
		return longValue;
	}
}