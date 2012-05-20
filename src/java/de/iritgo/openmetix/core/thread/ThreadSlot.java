/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.thread;


import de.iritgo.openmetix.core.logger.Log;


/**
 * ThreadSlot.
 *
 * @version $Id: ThreadSlot.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ThreadSlot
	extends Thread
{
	/** The current work object. */
	private Threadable threadable;

	/** True on shutdown. */
	private boolean release = false;

	/** True while running. */
	private boolean running = false;

	/** True if active. */
	private boolean active = true;

	/** Lock object. */
	public Object lock = new Object();

	/** Num slots. */
	public int slot;

	/** The thread controller. */
	public ThreadController threadController;

	/**
	 * Create a new ThreadSlot.
	 *
	 * @param slot
	 * @param threadController
	 */
	public ThreadSlot (int slot, ThreadController threadController)
	{
		this.slot = slot;
		this.threadController = threadController;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run ()
	{
		synchronized (lock)
		{
			while (! release)
			{
				if (threadable != null)
				{
					Log.logDebug (
						"thread", "ThreadSlot.run",
						"(Slot:" + slot + ") Working on Threadable:" + threadable.toString ());
					threadable.setState (Threadable.RUNNING);

					String lastThreadname = getName ();

					setName (threadable.getTypeId ());

					int state = Threadable.RUNNING;

					while (! release)
					{
						threadable.run ();

						state = threadable.getState ();

						if (state == Threadable.CLOSING)
						{
							break;
						}

						if (state == Threadable.FREE)
						{
							threadController.addFreeThreadable (threadable);

							break;
						}
					}

					threadable = null;
					running = false;

					setName (lastThreadname);
				}

				try
				{
					Log.logDebug (
						"thread", "ThreadSlot.run", "(Slot:" + slot + ") Waiting for Threadable");

					threadController.notifySlotFree ();

					lock.wait ();
				}
				catch (InterruptedException e)
				{
				}
			}

			Log.logDebug ("thread", "ThreadSlot.run", "Thread killed (Slot:" + slot + ").");
			active = false;
		}
	}

	/**
	 * Set the threadable.
	 *
	 * @param threadable The new threadable.
	 */
	public synchronized void setThreadable (Threadable threadable)
	{
		this.threadable = threadable;
	}

	/**
	 * Get the current threadable.
	 *
	 * @return The threadable.
	 */
	public Threadable getThreadable ()
	{
		return threadable;
	}

	/**
	 * Initialization.
	 */
	public synchronized void init ()
	{
		release = false;
		threadable = null;
		running = false;
	}

	/**
	 * Are we running?
	 *
	 * @return True if running.
	 */
	public boolean isRunning ()
	{
		return running;
	}

	/**
	 * Are we active?
	 *
	 * @return True if active.
	 */
	public boolean isActive ()
	{
		return active;
	}

	/**
	 * Set blocking.
	 */
	public void setBlock ()
	{
		running = true;
	}

	/**
	 * Start release.
	 */
	public void release ()
	{
		release = true;

		if (threadable != null)
		{
			threadable.dispose ();
		}
	}
}