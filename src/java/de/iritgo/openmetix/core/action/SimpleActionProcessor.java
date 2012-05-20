/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.action;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.base.Transceiver;


/**
 * This is an action processor that directly executes actions.
 *
 * @version $Id: SimpleActionProcessor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class SimpleActionProcessor
	extends BaseObject
	implements ActionProcessor
{
	/**
	 * Create a new SimpleActionProcessor.
	 */
	public SimpleActionProcessor ()
	{
		super("SimpleActionProcessor");
	}

	/**
	 * Perform an action.
	 *
	 * @param action The action to perform.
	 */
	public void perform (Action action)
	{
		if (action.canPerform ())
		{
			action.perform ();
		}
	}

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public void perform (Action action, Transceiver transceiver)
	{
		perform (action);
	}

	/**
	 * Clone a new instance from this processor.
	 *
	 * @return The clone.
	 */
	public Object clone ()
	{
		return new SimpleActionProcessor();
	}

	/**
	 * Close this action processor
	 */
	public void close ()
	{
	}
}