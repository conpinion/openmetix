/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.flowcontrol;



/**
 * A counting flow rule counts calls to it's success() and failure() methods.
 * Say you create a CountingFlowRule with an initial counter value of four.
 * Then four calls to the success() method are performed before the rule is
 * removed from the flow control.
 *
 * @version $Id: CountingFlowRule.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class CountingFlowRule
	extends FlowRule
{
	/** Success counter. */
	private int counter;

	/**
	 * Create a new CountingFlowRule.
	 *
	 * @param id The id of the new rule.
	 * @param counter The initial success count.
	 */
	public CountingFlowRule (String id, int counter)
	{
		super(id);
		this.counter = counter;
	}

	/**
	 * Tell the rule that is has succeeded.
	 */
	public void success ()
	{
		--counter;
	}

	/**
	 * Tell the rule that is has failed.
	 */
	public void failure ()
	{
		++counter;
	}

	/**
	 * Check wether the rule is completed.
	 *
	 * @return True if the rule has succeeded.
	 */
	public boolean isCompleted ()
	{
		return counter <= 0;
	}
}