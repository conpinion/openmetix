/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.sessioncontext;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.logger.Log;
import java.util.HashMap;
import java.util.Iterator;


/**
 * SessionContext.
 *
 * @version $Id: SessionContext.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class SessionContext
	extends BaseObject
{
	private HashMap sessionContexts;

	public SessionContext (String id)
	{
		super(id);
		sessionContexts = new HashMap();
	}

	public void add (BaseObject baseObject)
	{
		sessionContexts.put (baseObject.getTypeId (), baseObject);
	}

	public void add (String id, BaseObject baseObject)
	{
		sessionContexts.put (id, baseObject);
	}

	public BaseObject get (String id)
	{
		BaseObject baseObject = (BaseObject) sessionContexts.get (id);

		if (baseObject == null)
		{
			Log.log ("system", "SessionContext.get", "Key not found: " + id, Log.WARN);
		}

		return baseObject;
	}

	public void remove (BaseObject baseObject)
	{
		sessionContexts.remove (baseObject.getTypeId ());
	}

	public void remove (String id)
	{
		sessionContexts.remove (id);
	}

	public Iterator getValueIterator ()
	{
		return sessionContexts.values ().iterator ();
	}

	public boolean baseObjectExists (String id)
	{
		return sessionContexts.containsKey (id);
	}

	public boolean contains (String id)
	{
		return sessionContexts.containsKey (id);
	}

	public void cleanSessionContext ()
	{
		sessionContexts.clear ();
	}
}