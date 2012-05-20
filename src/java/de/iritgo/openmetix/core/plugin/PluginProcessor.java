/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.plugin;


import de.iritgo.openmetix.core.logger.Log;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;


/**
 * PluginProcessor.
 *
 * @version $Id: PluginProcessor.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class PluginProcessor
{
	/** Process forward. */
	public static int FORWARD = 0;

	/** Process backward. */
	public static int BACKWARD = 1;

	/** List of all plugins. */
	private List plugins;

	/** List of all initialized plugins. */
	private List initedPlugins;

	/** List of all sorted plugins. */
	private List sortedPlugins;

	/**
	 * Create a new PluginProcessor.
	 *
	 * @param plugins
	 */
	public PluginProcessor (List plugins)
	{
		this.plugins = plugins;
		initedPlugins = new LinkedList();
	}

	/**
	 * Process the plugins.
	 *
	 * @param process
	 * @param direction
	 */
	public void doPlugins (PluginProcess process, int direction)
	{
		sortedPlugins = generateSortedList ();

		if (direction == FORWARD)
		{
			Iterator i = sortedPlugins.iterator ();

			while (i.hasNext ())
			{
				doPlugin (process, (PluginContext) i.next ());
			}
		}
		else
		{
			ListIterator i = sortedPlugins.listIterator (sortedPlugins.size ());

			while (i.hasPrevious ())
			{
				doPlugin (process, (PluginContext) i.previous ());
			}
		}
	}

	/**
	 * Process the plugins.
	 *
	 * @param process
	 * @param pluginContext
	 */
	public void doPlugin (PluginProcess process, PluginContext pluginContext)
	{
		Thread.currentThread ().setContextClassLoader (pluginContext.getClassLoader ());

		Plugin plugin = pluginContext.getPlugin ();

		process.doPlugin (plugin);
		initedPlugins.add (plugin.getName ());

		ClassLoader parentloader = pluginContext.getClassLoader ().getParent ();

		Thread.currentThread ().setContextClassLoader (parentloader);
	}

	/**
	 * Create a dependency sorted plugin list.
	 *
	 * @return The sorted plugins.
	 */
	public LinkedList generateSortedList ()
	{
		int lastDiv = -1;

		initedPlugins.add ("FIRST");

		int pluginsSize = plugins.size () + 1;
		LinkedList sortedList = new LinkedList();

		while (true)
		{
			if (lastDiv == (pluginsSize - (initedPlugins.size ())))
			{
				initedPlugins.add ("LAST");
			}

			lastDiv = pluginsSize - (initedPlugins.size ());

			if (pluginsSize == initedPlugins.size ())
			{
				return sortedList;
			}

			Iterator i = plugins.iterator ();

			while (i.hasNext ())
			{
				PluginContext pluginContext = (PluginContext) i.next ();

				Thread.currentThread ().setContextClassLoader (pluginContext.getClassLoader ());

				Plugin plugin = pluginContext.getPlugin ();

				if (plugin.getDependency () == null)
				{
					Log.log (
						"plugin", "PluginProcessor",
						"No dependencies defined for plugin '" + plugin.getName () + "'", Log.ERROR);

					continue;
				}

				if (! checkIsDependencyOK (plugin.getDependency ()))
				{
					continue;
				}

				if (initedPlugins.contains (plugin.getName ()))
				{
					continue;
				}

				sortedList.add (pluginContext);
				initedPlugins.add (plugin.getName ());

				ClassLoader parentloader = pluginContext.getClassLoader ().getParent ();

				Thread.currentThread ().setContextClassLoader (parentloader);
			}
		}
	}

	/**
	 * Check the dependencies.
	 *
	 * @param dependency Dependency to chek.
	 * @return True if the dependencies are resolved.
	 */
	public boolean checkIsDependencyOK (String dependency)
	{
		StringTokenizer st = new StringTokenizer(dependency, ",");

		while (st.hasMoreTokens ())
		{
			if (! initedPlugins.contains (st.nextToken ()))
			{
				return false;
			}
		}

		return true;
	}
}