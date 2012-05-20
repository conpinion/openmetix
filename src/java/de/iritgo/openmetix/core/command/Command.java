/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.command;


import de.iritgo.openmetix.core.base.BaseObject;
import java.util.Properties;


/**
 * This class implements the command pattern.
 *
 * @version $Id: Command.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class Command
	extends BaseObject
{
	/** Command execution properties. */
	protected Properties properties;

	/**
	 * Create a new anonymous command.
	 */
	public Command ()
	{
	}

	/**
	 * Create a new command with a specific type id.
	 *
	 * @param typeId The command id.
	 */
	public Command (String typeId)
	{
		super(typeId);
	}

	/**
	 * Set the command properties.
	 *
	 * @param properties The new command properties.
	 */
	public void setProperties (Properties properties)
	{
		this.properties = properties;
	}

	/**
	 * Get the command properties.
	 *
	 * @return The command properties.
	 */
	public Properties getProperties ()
	{
		return properties;
	}

	/**
	 * Do whatever the command needs to do.
	 * Subclasses should override this method to provide custom command code.
	 * Override this method if you do not want to return a command result (The
	 * return value defaults to null).
	 */
	public void perform ()
	{
	}

	/**
	 * Do whatever the command needs to do.
	 * Subclasses should override this method to provide custom command code.
	 * Override this method if you want to return a command result.
	 *
	 * @return The command results.
	 */
	public Object performWithResult ()
	{
		perform ();

		return null;
	}

	/**
	 * Check wether the command can currently be executed or not.
	 * By default commands are executable. Subclasses should provide a
	 * reasonable implementation of this method.
	 *
	 * @return True if the command can be executed.
	 */
	public boolean canPerform ()
	{
		return true;
	}
}