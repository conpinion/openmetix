/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.command;


import de.iritgo.openmetix.core.base.BaseObject;
import java.util.HashMap;


/**
 * This registry contains all known command processors.
 *
 * @version $Id: CommandProcessorRegistry.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class CommandProcessorRegistry
	extends BaseObject
{
	/** All known command processors. */
	private HashMap commandProcessors;

	/**
	 * Create a new CommandProcessorRegistry.
	 */
	public CommandProcessorRegistry ()
	{
		super("commandprocessorregistry");
		commandProcessors = new HashMap();
	}

	/**
	 * Add a command processor.
	 *
	 * @param commandProcessor The command processor to add.
	 */
	public void add (CommandProcessor commandProcessor)
	{
		commandProcessors.put (commandProcessor.getTypeId (), commandProcessor);
	}

	/**
	 * Get a command processor by it's id.
	 *
	 * @param id The id of the command processor.
	 */
	public CommandProcessor get (String id)
	{
		return (CommandProcessor) commandProcessors.get (id);
	}

	/**
	 * Remove a command processor.
	 *
	 * @param commandProcessor The command processor to add.
	 */
	public void remove (CommandProcessor commandProcessor)
	{
		commandProcessors.remove (commandProcessor.getTypeId ());
	}

	/**
	 * Checks the exitence of a command processor.
	 *
	 * @param id The id of the command processor to check.
	 * @return True if the command processor exists.
	 */
	public boolean exists (String id)
	{
		return commandProcessors.containsKey (id);
	}

	/**
	 * Clear the registry.
	 */
	public void clear ()
	{
		commandProcessors.clear ();
	}
}