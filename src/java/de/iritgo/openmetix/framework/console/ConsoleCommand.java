/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.console;


import de.iritgo.openmetix.core.command.Command;


/**
 * ConsoleCommand.
 *
 * @version $Id: ConsoleCommand.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class ConsoleCommand
{
	private String commandId;
	private String helpId;
	private int numParam;
	private Command command;

	public ConsoleCommand (String commandId, Command command, String helpId, int numParam)
	{
		this.commandId = commandId;
		this.command = command;
		this.helpId = helpId;
		this.numParam = numParam;
	}

	public ConsoleCommand (String commandId, Command command, String helpId)
	{
		this(commandId, command, helpId, -1);
	}

	public String getCommandId ()
	{
		return commandId;
	}

	public Command getCommand ()
	{
		return command;
	}

	public String getHelpId ()
	{
		return helpId;
	}

	public int getNumParam ()
	{
		return numParam;
	}
}