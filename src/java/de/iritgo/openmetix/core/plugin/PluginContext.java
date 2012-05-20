/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.plugin;


import de.iritgo.openmetix.core.base.BaseObject;


/**
 * Environmnent information for each plugin.
 *
 * @version $Id: PluginContext.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class PluginContext
	extends BaseObject
{
	/** The class loader for the plugin. */
	private ClassLoader classLoader;

	/** The plugin itself. */
	private Plugin plugin;

	/**
	 * Create a new PluginContext.
	 *
	 * @param plugin The plugin.
	 * @param classLoader The class loader.
	 */
	public PluginContext (Plugin plugin, ClassLoader classLoader)
	{
		this.plugin = plugin;
		this.classLoader = classLoader;
	}

	/**
	 * Get the class loader.
	 *
	 * @return The class loader.
	 */
	public ClassLoader getClassLoader ()
	{
		return classLoader;
	}

	/**
	 * Get the plugin.
	 *
	 * @return The plugin.
	 */
	public Plugin getPlugin ()
	{
		return plugin;
	}
}