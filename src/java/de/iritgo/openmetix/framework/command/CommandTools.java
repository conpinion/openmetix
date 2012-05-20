/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.command.CommandProcessor;
import java.util.Properties;


/**
 * Utility methods for easier command handling.
 *
 * @version $Id: CommandTools.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class CommandTools
{
	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 */
	public static Object performSimple (String commandId, Properties properties)
	{
		return Engine.instance ().getCommandRegistry ().perform (
			CommandProcessor.SIMPLE, commandId, properties);
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performSimple (String commandId, Object[] properties)
	{
		return Engine.instance ().getCommandRegistry ().perform (
			CommandProcessor.SIMPLE, commandId, properties);
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performSimple (Command command, Properties properties)
	{
		return Engine.instance ().getCommandRegistry ().perform (
			CommandProcessor.SIMPLE, command, properties);
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param command The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performSimple (Command command, Object[] properties)
	{
		return Engine.instance ().getCommandRegistry ().perform (
			CommandProcessor.SIMPLE, command, properties);
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @return The command results.
	 */
	public static Object performSimple (String commandId)
	{
		return Engine.instance ().getCommandRegistry ().perform (
			CommandProcessor.SIMPLE, commandId);
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param command The command to execute.
	 * @return The command results.
	 */
	public static Object performSimple (Command command)
	{
		return Engine.instance ().getCommandRegistry ().perform (CommandProcessor.SIMPLE, command);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 */
	public static Object performAsync (String commandId, Properties properties)
	{
		return Engine.instance ().getCommandRegistry ().perform (
			CommandProcessor.ASYNC, commandId, properties);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performAsync (String commandId, Object[] properties)
	{
		return Engine.instance ().getCommandRegistry ().perform (
			CommandProcessor.ASYNC, commandId, properties);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performAsync (Command command, Properties properties)
	{
		return Engine.instance ().getCommandRegistry ().perform (
			CommandProcessor.ASYNC, command, properties);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param command The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performAsync (Command command, Object[] properties)
	{
		return Engine.instance ().getCommandRegistry ().perform (
			CommandProcessor.ASYNC, command, properties);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @return The command results.
	 */
	public static Object performAsync (String commandId)
	{
		return Engine.instance ().getCommandRegistry ().perform (CommandProcessor.ASYNC, commandId);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param command The command to execute.
	 * @return The command results.
	 */
	public static Object performAsync (Command command)
	{
		return Engine.instance ().getCommandRegistry ().perform (CommandProcessor.ASYNC, command);
	}
}