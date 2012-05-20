/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.flowcontrol;


import de.iritgo.openmetix.core.base.BaseObject;


/**
 * FlowRules implement the application flow logic. A rule can be checked
 * for success and can be signalled to succeed or fail.
 *
 * @version $Id: FlowRule.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public abstract class FlowRule
	extends BaseObject
{
	/**
	 * Create a new rule.
	 *
	 * @param id The id of the rule.
	 */
	public FlowRule (String id)
	{
		super(id);
	}

	/**
	 * Tell the rule that is has succeeded.
	 */
	public void success ()
	{
	}

	/**
	 * Tell the rule that is has failed.
	 */
	public void failure ()
	{
	}

	/**
	 * Tell the rule that is has succeeded.
	 *
	 * @param arg Optional success argument.
	 */
	public void success (Object arg)
	{
	}

	/**
	 * Tell the rule that is has failed.
	 *
	 * @param arg Optional failure argument.
	 */
	public void failure (Object arg)
	{
	}

	/**
	 * Check wether the rule is completed.
	 *
	 * @return True if the rule has succeeded.
	 */
	public boolean isCompleted ()
	{
		return true;
	}
}