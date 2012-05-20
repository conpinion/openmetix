/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.command;



/**
 * Commands are executed indirectly by telling <code>CommandProcessors</code>
 * to do so. This separates 'how' they are executed from the point at which
 * they are called. For example an asynchronous command processor performs its
 * commands by putting them into the thread pool for later execution, whereas
 * a simple command processor directly executes the commands in the current
 * thread.
 *
 * @version $Id: CommandProcessor.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public interface CommandProcessor
{
	/** Id of the default simple command processor. */
	public static final String SIMPLE = "SimpleCommandProcessor";

	/** Id of the default asynchronous command processor. */
	public static final String ASYNC = "AsyncCommandProcessor";

	/**
	 * Get the id of the command processor.
	 *
	 * @return The command processor id.
	 */
	public String getTypeId ();

	/**
	 * Perform a command.
	 *
	 * @param command The command to execute.
	 * @return The command results.
	 */
	public Object perform (Command command);
}