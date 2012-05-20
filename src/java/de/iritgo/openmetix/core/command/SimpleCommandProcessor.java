/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.command;



/**
 * A simple command processor directly executes commands in the current
 * thread.
 *
 * @version $Id: SimpleCommandProcessor.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class SimpleCommandProcessor
	implements CommandProcessor
{
	/**
	 * Get the type id.
	 *
	 * @return The type id.
	 */
	public String getTypeId ()
	{
		return "SimpleCommandProcessor";
	}

	/**
	 * Perform a command.
	 *
	 * @param command The command to execute.
	 * @return The command results.
	 */
	public Object perform (Command command)
	{
		if (command.canPerform ())
		{
			return command.performWithResult ();
		}

		return null;
	}
}