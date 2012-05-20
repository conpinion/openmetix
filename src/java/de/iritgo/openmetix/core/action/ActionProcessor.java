/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.action;


import de.iritgo.openmetix.core.base.Transceiver;


/**
 * ActionProcessors are used to perform actions. Actions are not executed directly,
 * because we want some basic conditions to hold, for example we want to
 * synchronize the actions among the clients.
 *
 * @version $Id: ActionProcessor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public interface ActionProcessor
{
	/**
	 * Get the id of the action processor
	 *
	 * @return The processor type id
	 */
	public String getTypeId ();

	/**
	 * Perform an action.
	 *
	 * @param action The action to perform.
	 */
	public void perform (Action action);

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public void perform (Action action, Transceiver transceiver);

	/**
	 * Close a action processor
	 */
	public void close ();

	/**
	 * Clone a processor
	 */
	public Object clone ();
}