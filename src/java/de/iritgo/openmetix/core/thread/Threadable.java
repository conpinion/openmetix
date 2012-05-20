/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.thread;


import de.iritgo.openmetix.core.base.BaseObject;


/**
 * Threadables are objets that can be executed by a ThreadController.
 *
 * Threadables are working units that are thrown into a thread pool to
 * find a free thread that can execute the threadable.
 *
 * @version $Id: Threadable.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public abstract class Threadable
	extends BaseObject
{
	/** Nothing to do state. */
	public static final int FREE = 0;

	/** Currently working state. */
	public static final int RUNNING = 1;

	/** Closing state. */
	public static final int CLOSING = 2;

	/** The current state of the threadable. */
	private int currentState;

	/** The thread controller. */
	protected ThreadController controller;

	/** The name of the threadable. */
	private String name;

	/**
	 * Create a new Threadable.
	 */
	public Threadable ()
	{
		currentState = FREE;
	}

	/**
	 * Create a new Threadable.
	 *
	 * @param name The name of the new threadable.
	 */
	public Threadable (String name)
	{
		super(name);
		currentState = FREE;
		this.name = name;
	}

	/**
	 * Set the thread controller.
	 *
	 * @param controller The thread controller.
	 */
	public void setThreadController (ThreadController controller)
	{
		this.controller = controller;
	}

	/**
	 * Retrieve the thread controller.
	 *
	 * @return The thread controller.
	 */
	public ThreadController getThreadController ()
	{
		return controller;
	}

	/**
	 * Set the threadable state.
	 *
	 * @param currentState The new state.
	 */
	public void setState (int currentState)
	{
		this.currentState = currentState;
	}

	/**
	 * Get the current threadable state.
	 *
	 * @return The current state.
	 */
	public int getState ()
	{
		return currentState;
	}

	/**
	 * Get the name of this threadable.
	 *
	 * @return The threadable name.
	 */
	public String getName ()
	{
		return name;
	}

	/**
	 * The work method.
	 * Subclasses should override this method to provide a threading
	 * task.
	 */
	public abstract void run ();

	/**
	 * Called from the thread controller to free all resources of
	 * this threadable.
	 */
	public void dispose ()
	{
	}
}