/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.flowcontrol.FlowRule;
import de.iritgo.openmetix.framework.IritgoEngine;


/**
 * FrameworkFlowRule.
 *
 * @version $Id: FrameworkFlowRule.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class FrameworkFlowRule
	extends FlowRule
{
	private Command successCommand;
	private Command failureCommand;

	public FrameworkFlowRule (String id, Command successCommand, Command failureCommand)
	{
		super(id);
		this.successCommand = successCommand;
		this.failureCommand = failureCommand;
	}

	public void success ()
	{
		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (successCommand);
	}

	public void failure ()
	{
		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (failureCommand);
	}
}