/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.thread;


import de.iritgo.openmetix.core.logger.Log;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * ThreadController.
 *
 * @version $Id: ThreadController.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ThreadController
	extends Thread
{
	/** All thread slots. */
	private LinkedList threadSlots;

	/** All threadables. */
	private LinkedList threadables;

	/** True on shutdown. */
	private boolean release = false;

	/** Num slots. */
	private int slots;

	/** Lock object. */
	private String lock = "LockObject";

	/** Num free slots. */
	private int freeSlots;

	/**
	 * Create a new ThreadController.
	 */
	public ThreadController ()
	{
		threadSlots = new LinkedList();
		threadables = new LinkedList();
		slots = 0;
		release = false;
		freeSlots = 0;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run ()
	{
		ThreadSlot slot = null;
		Threadable threadable = null;

		while (! release)
		{
			synchronized (lock)
			{
				if (slot == null)
				{
					slot = getFreeSlot ();
				}

				if (threadable == null)
				{
					threadable = getWaitingThreadable ();
				}

				if (slot == null || threadable == null)
				{
					try
					{
						{
							lock.wait ();
						}
					}
					catch (Exception e)
					{
					}

					continue;
				}
			}

			synchronized (slot.lock)
			{
				--freeSlots;
				slot.init ();
				slot.setBlock ();
				threadable.setState (Threadable.RUNNING);
				slot.setThreadable (threadable);
				slot.lock.notify ();
				slot = null;
				threadable = null;
			}
		}

		Log.logInfo ("thread", "ThreadController.run", "ThreadController shutdown.");
		releaseAllThreadSlots ();
	}

	/**
	 * Add a threaded object to pool.
	 *
	 * @param threadable The threadable object.
	 */
	public void add (Threadable threadable)
	{
		synchronized (lock)
		{
			Log.logVerbose (
				"thread", "ThreadController.add",
				"ThreadAble added: " + threadable.getName () + " #ThreadAbles: " +
				threadables.size ());
			threadable.setThreadController (this);

			threadables.add (threadable);

			lock.notify ();
		}
	}

	/**
	 * Add a existing threaded object to pool.
	 *
	 * @param threadable The threadable object.
	 */
	public void addFreeThreadable (Threadable threadable)
	{
		synchronized (threadables)
		{
			Log.logVerbose (
				"thread", "ThreadController.addFreeThreadable",
				"ThreadAble added: " + threadable.getName () + " #ThreadAbles: " +
				threadables.size ());
			threadables.add (threadable);
		}
	}

	/**
	 * Remove a threadable.
	 */
	public void remove (Threadable threadable)
	{
		Log.logVerbose (
			"thread", "ThreadController.remove",
			"ThreadAble removed: " + threadable.getName () + " #ThreadAbles: " +
			threadables.size ());

		synchronized (threadables)
		{
			threadables.remove (threadable);
		}
	}

	/**
	 * Kill all threads.
	 *
	 * @return True if all threads are successfully killed.
	 */
	public boolean release ()
	{
		release = true;

		synchronized (lock)
		{
			lock.notifyAll ();
		}

		int numTries = 50;

		for (; numTries > 0 && threadSlots.size () > 0; --numTries)
		{
			try
			{
				Thread.sleep (100);
			}
			catch (Exception x)
			{
			}
		}

		if (numTries > 0)
		{
			Log.logDebug (
				"thread", "ThreadController.release", "All threads are terminated and removed.");

			return true;
		}
		else
		{
			Log.logDebug ("thread", "ThreadController.release", "Some threads are still running.");

			return false;
		}
	}

	/**
	 * Return a free ThreadSlot.
	 *
	 * @return The ThreadSlot.
	 */
	private ThreadSlot getFreeSlot ()
	{
		synchronized (threadSlots)
		{
			ThreadSlot slot;

			for (int i = 0; i < threadSlots.size (); ++i)
			{
				slot = (ThreadSlot) threadSlots.get (i);

				if (! slot.isRunning ())
				{
					return slot;
				}
			}

			return null;
		}
	}

	/**
	 * A slot was freed.
	 */
	public void notifySlotFree ()
	{
		synchronized (lock)
		{
			++freeSlots;
			lock.notify ();
		}
	}

	/**
	 * Return a waiting threadable.
	 *
	 * @return The threadable.
	 */
	private Threadable getWaitingThreadable ()
	{
		synchronized (threadables)
		{
			Threadable threadable;

			if (threadables.size () == 0)
			{
				return null;
			}

			threadable = (Threadable) threadables.get (0);
			threadables.remove (threadable);

			Log.logVerbose (
				"thread", "ThreadController.getWaitingThreadAble",
				"Number of Threadables: " + threadables.size ());

			return threadable;
		}
	}

	/**
	 * Release all ThreadSlots.
	 */
	private void releaseAllThreadSlots ()
	{
		LinkedList threadSlotsCopy;

		synchronized (threadSlots)
		{
			threadSlotsCopy = new LinkedList(threadSlots);
		}

		ThreadSlot slot;

		for (int i = 0; i < threadSlotsCopy.size (); ++i)
		{
			slot = (ThreadSlot) threadSlotsCopy.get (i);

			while (slot.isActive ())
			{
				slot.release ();

				synchronized (slot.lock)
				{
					slot.lock.notify ();
				}
			}

			removeSlot (slot);
		}
	}

	/**
	 * Stop a threadable.
	 */
	public void stopThreadable (Threadable threadable)
	{
		LinkedList threadSlotsCopy;

		synchronized (threadSlots)
		{
			threadSlotsCopy = new LinkedList(threadSlots);
		}

		ThreadSlot slot;
		boolean threadableKilled = false;

		for (int i = 0; i < threadSlotsCopy.size (); ++i)
		{
			slot = (ThreadSlot) threadSlotsCopy.get (i);

			if ((slot.getThreadable () == threadable) && slot.isActive ())
			{
				removeSlot (slot);
				remove (slot.getThreadable ());

				while (slot.isActive ())
				{
					slot.release ();

					synchronized (slot.lock)
					{
						slot.lock.notify ();
					}

					threadableKilled = true;
				}
			}

			if (threadableKilled)
			{
				addSlot ();

				break;
			}
		}
	}

	/**
	 * Add a ThreadSlot.
	 */
	public void addSlot ()
	{
		synchronized (threadSlots)
		{
			Log.logDebug ("thread", "ThreadController.addSlot", "ThreadSlot add: " + slots);

			ThreadSlot slot = new ThreadSlot(slots++, this);

			threadSlots.add (slot);
			slot.start ();
		}
	}

	/**
	 * Remove a ThreadSlot.
	 */
	public void removeSlot (ThreadSlot threadSlot)
	{
		synchronized (threadSlots)
		{
			--freeSlots;
			Log.logDebug (
				"thread", "ThreadController.removeSlot",
				"ThreadSlot remove: " + threadSlot.toString ());
			threadSlots.remove (threadSlot);
		}
	}

	/**
	 * Get an iterator over all thread slots.
	 *
	 * @return A thread slot iterator.
	 */
	public Iterator threadSlotIterator ()
	{
		return threadSlots.iterator ();
	}

	/**
	 * Return the free slots size
	 *
	 * @return The free slots size
	 */
	public int getFreeSlots ()
	{
		return freeSlots;
	}

	/**
	 * Return the available slots
	 *
	 * @return The available slots
	 */
	public int getAvailableSlots ()
	{
		return threadSlots.size ();
	}
}