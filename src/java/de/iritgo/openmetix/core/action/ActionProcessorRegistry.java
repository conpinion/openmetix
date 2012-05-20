/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.action;


import java.util.HashMap;
import java.util.Map;


/**
 * A registry of action processors.
 *
 * @version $Id: ActionProcessorRegistry.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class ActionProcessorRegistry
{
	/** Action processors */
	private Map actionProcessors;

	/**
	 * Create a new ActionProcessorRegistry.
	 */
	public ActionProcessorRegistry ()
	{
		actionProcessors = new HashMap();
	}

	/**
	 * Add an action processor.
	 *
	 * @param processor The action processor.
	 */
	public void put (ActionProcessor processor)
	{
		actionProcessors.put (processor.getTypeId (), processor);
	}

	/**
	 * Get an action processor.
	 *
	 * @param id The id of the action processor.
	 * @return The action processor
	 */
	public ActionProcessor get (String id)
	{
		return (ActionProcessor) actionProcessors.get (id);
	}

	/**
	 * Remove an action processor.
	 *
	 * @param category The category under which the processor was registered.
	 * @param processor Type action processor to remove.
	 */
	public void remove (ActionProcessor processor)
	{
		actionProcessors.remove (processor.getTypeId ());
		processor.close ();
	}
}