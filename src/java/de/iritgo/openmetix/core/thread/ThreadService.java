/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.thread;


import de.iritgo.openmetix.core.base.BaseObject;


/**
 * ThreadService.
 *
 * @version $Id: ThreadService.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ThreadService
	extends BaseObject
{
	/** The thread controller. */
	private ThreadController controller;

	/**
	 * Create a new ThreadService.
	 */
	public ThreadService ()
	{
		controller = new ThreadController();
		controller.start ();
	}

	/**
	 * Add a threaded object to pool.
	 *
	 * @param threadObject The threadable object.
	 */
	public void add (Threadable threadObject)
	{
		controller.add (threadObject);
	}

	/**
	 * Add a ThreadSlot.
	 */
	public void addThreadSlot ()
	{
		controller.addSlot ();
	}

	/**
	 * Stop a threadable.
	 */
	public void stopThreadable (Threadable threadable)
	{
		controller.stopThreadable (threadable);
	}

	/**
	 * Shutdown.
	 *
	 * @return True if all threads are successfully killed.
	 */
	public boolean stopThreadEngine ()
	{
		return controller.release ();
	}

	/**
	 * Get the thread controller.
	 *
	 * @return The thread controller.
	 */
	public ThreadController getThreadController ()
	{
		return controller;
	}

	/**
	 * The size of working slots
	 *
	 * @return the number of working slots
	 */
	public int getWorkingSlots ()
	{
		return controller.getAvailableSlots () - controller.getFreeSlots ();
	}

	/**
	 * The size of free slots
	 *
	 * @return the number of free slots
	 */
	public int getFreeSlots ()
	{
		return controller.getFreeSlots ();
	}
}