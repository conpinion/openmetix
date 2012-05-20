/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.plugin;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.logger.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;


/**
 * The plugin manager handles loading and unloading of Iritgo plugins.
 *
 * @version $Id: PluginManager.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class PluginManager
	extends BaseObject
{
	/** List of all plugins. */
	private List plugins;

	/** The Iritgo engine. */
	private Engine engine;

	/**
	 * Crate a new PluginManager.
	 *
	 * @param engine The Iritgo engine.
	 */
	public PluginManager (Engine engine)
	{
		this.engine = engine;
		plugins = new LinkedList();
	}

	/**
	 * Load all plugins.
	 */
	public void loadPlugins ()
	{
		InputStream in = Engine.class.getResourceAsStream ("/plugin.properties");

		if (in != null)
		{
			Properties pluginProperties = new Properties();

			try
			{
				pluginProperties.load (in);

				if (pluginProperties.get ("plugin.names") != null)
				{
					loadPluginsFromClassPath (pluginProperties);
				}
			}
			catch (IOException x)
			{
				Log.log (
					"plugin", "PluginManager.loadPlugins",
					"Unable to load plugin descriptor /plugin.properties: " + x, Log.ERROR);
			}
		}

		String directory =
			Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator () +
			"plugins" + Engine.instance ().getFileSeparator ();

		File dir = new File(directory);

		if (dir.exists ())
		{
			loadPluginsFromFile (directory);
		}
	}

	/**
	 * Load all plugins from the file system.
	 *
	 * @param directory The directory containing the plugins.
	 */
	public void loadPluginsFromFile (String directory)
	{
		try
		{
			final File pluginDir = new File(Engine.instance ().getSystemDir (), "plugins");

			File[] pluginFiles =
				pluginDir.listFiles (
					new FilenameFilter()
					{
						public boolean accept (File dir, String name)
						{
							return name.endsWith (".jar");
						}
					});

			if (pluginFiles == null)
			{
				Log.log (
					"plugin", "PluginManager", "No plugins found in directory " + directory,
					Log.WARN);

				return;
			}

			for (int i = 0; i < pluginFiles.length; ++i)
			{
				String name = "";
				String dependency = "";
				String pluginClass = "";

				String fileName = pluginFiles[i].getName ();

				Properties properties = getPluginPropertiesFromFile (directory, fileName);

				if (properties == null)
				{
					continue;
				}

				for (Enumeration e = properties.propertyNames (); e.hasMoreElements ();)
				{
					dependency = "";
					pluginClass = "";
					name = "";

					String key = (String) e.nextElement ();

					if (key.startsWith ("name"))
					{
						name = properties.getProperty (key);
					}

					dependency = properties.getProperty (name + ".dependency");
					pluginClass = properties.getProperty (name + ".pluginclass");

					if (name.length () != 0)
					{
						loadPluginFromFile (directory, fileName, pluginClass, name, dependency);
					}
				}
			}
		}
		catch (IOException x)
		{
			Log.log (
				"plugin", "PluginManager",
				"Error while loading plugins from directory " + directory + ": " + x, Log.ERROR);
		}
	}

	/**
	 * Load a plugin from the file system.
	 *
	 * @param directory The directory containing the plugin.
	 * @param fileName The file name of the plugin.
	 * @param pluginName The name of the plugin.
	 * @param name The display name of the plugin.
	 * @param dependency Dependencies to other plugins.
	 */
	public void loadPluginFromFile (
		String directory, String fileName, String pluginName, String name, String dependency)
		throws IOException
	{
		Log.log ("plugin", "PluginManager", "Loading plugin: " + fileName, Log.INFO);

		File dir = new File(directory + fileName);

		if (pluginName.length () == 0)
		{
			Log.log (
				"plugin", "PluginManager", "No plugin name specified in descriptor " + fileName,
				Log.ERROR);

			return;
		}

		try
		{
			URLClassLoader loader =
				new URLClassLoader(
					new URL[]
					{
						dir.toURL ()
					}, Thread.currentThread ().getContextClassLoader ());

			Plugin plugin = (Plugin) (loader.loadClass (pluginName).newInstance ());

			plugin.setClassName (pluginName);
			plugin.setName (name);
			plugin.setDependency (dependency);

			plugins.add (new PluginContext(plugin, loader));
		}
		catch (Exception x)
		{
			Log.log (
				"plugin", "PluginManager.loadPluginFromFile",
				"Unable to load plugin " + pluginName + ": " + x, Log.ERROR);
		}
	}

	/**
	 * Read the plugin descriptor from the jar file.
	 *
	 * @param directory The directory containing the plugin.
	 * @param fileName The name of the jar file.
	 */
	public Properties getPluginPropertiesFromFile (String directory, String fileName)
	{
		try
		{
			JarFile jarFile = new JarFile(directory + fileName);
			ZipEntry entry = jarFile.getEntry ("resources/plugin.properties");

			if (entry != null)
			{
				Properties properties = new Properties();
				InputStream is = jarFile.getInputStream (entry);

				properties.load (is);

				return properties;
			}
		}
		catch (Exception x)
		{
			Log.log (
				"plugin", "PluginManager.getPluginPropertiesFromFile",
				"Unable to read plugin descriptor " + fileName + ": " + x, Log.ERROR);
		}

		return null;
	}

	/**
	 * Load all plugins from the class path.
	 *
	 * @param pluginProperties Propterties containing the plugin descriptor.
	 */
	public void loadPluginsFromClassPath (Properties pluginProperties)
	{
		String pluginNames = pluginProperties.getProperty ("plugin.names");

		StringTokenizer st = new StringTokenizer(pluginNames, ",");

		while (st.hasMoreTokens ())
		{
			String pluginName = st.nextToken ();

			Log.log ("plugin", "PluginManager", "Loading plugin " + pluginName, Log.INFO);

			try
			{
				Properties properties = getPluginPropertiesFromClassPath (pluginName);

				if (properties == null)
				{
					continue;
				}

				String name = properties.getProperty ("name");
				String className = properties.getProperty (name + ".pluginclass");
				String dependency = properties.getProperty (name + ".dependency");

				Plugin plugin =
					(Plugin) (getClass ().getClassLoader ().loadClass (className).newInstance ());

				plugin.setClassName (pluginName);
				plugin.setName (name);
				plugin.setDependency (dependency);

				plugins.add (new PluginContext(plugin, getClass ().getClassLoader ()));
			}
			catch (Exception x)
			{
				Log.log (
					"plugin", "PluginManager.loadPluginsFromClassPath",
					"Unable to load plugin " + pluginName + ": " + x, Log.ERROR);
			}
		}
	}

	/**
	 * Read the plugin descriptor via the plugins class loader.
	 *
	 * @param pluginName The plugin name.
	 */
	public Properties getPluginPropertiesFromClassPath (String pluginName)
	{
		try
		{
			Properties properties = new Properties();

			properties.load (
				getClass ().getResourceAsStream ("/resources/" + pluginName + "-plugin.properties"));

			return properties;
		}
		catch (Exception x)
		{
			Log.log (
				"plugin", "PluginManager",
				"Unable to read descriptor of plugin " + pluginName + ": " + x, Log.ERROR);
		}

		return null;
	}

	/**
	 * Initialize all plugins.
	 */
	public void initPlugins ()
	{
		PluginProcessor processor = new PluginProcessor(plugins);

		processor.doPlugins (
			new PluginProcess()
			{
				public void doPlugin (Plugin plugin)
				{
					Log.log (
						"plugin", "PluginManager", "Initializing plugin: " +
						plugin.getClassName (), Log.INFO);

					plugin.init (engine);
				}
			}, PluginProcessor.FORWARD);
	}

	/**
	 * Unload all plugins
	 */
	public void unloadPlugins ()
	{
		PluginProcessor processor = new PluginProcessor(plugins);

		processor.doPlugins (
			new PluginProcess()
			{
				public void doPlugin (Plugin plugin)
				{
					Log.log (
						"plugin", "PluginManager", "Unloading plugin: " + plugin.getClassName (),
						Log.INFO);
					plugin.unloadPlugin (engine);
				}
			}, PluginProcessor.BACKWARD);

		plugins.clear ();
	}

	/**
	 * Load the text resources of all plugins.
	 */
	public void loadTranslationResources ()
	{
		PluginProcessor processor = new PluginProcessor(plugins);

		processor.doPlugins (
			new PluginProcess()
			{
				public void doPlugin (Plugin plugin)
				{
					Log.log (
						"plugin", "PluginManager",
						"Loading plugin text resources: " + plugin.getClassName (), Log.INFO);
					plugin.loadTranslationResources ();
				}
			}, PluginProcessor.FORWARD);
	}

	/**
	 * Unload the text resources of all plugins.
	 */
	public void unloadTranslationResources ()
	{
		PluginProcessor processor = new PluginProcessor(plugins);

		processor.doPlugins (
			new PluginProcess()
			{
				public void doPlugin (Plugin plugin)
				{
					Log.log (
						"plugin", "PluginManager",
						"Unload plugin text resources: " + plugin.getClassName (), Log.INFO);
					plugin.unloadTranslationResources ();
				}
			}, PluginProcessor.BACKWARD);
	}

	/**
	 * Retrieve a plugin.
	 *
	 * @param name The plugin name.
	 * @return The plugin or null if it wasn't found.
	 */
	public Plugin getPlugin (String name)
	{
		for (Iterator i = plugins.iterator (); i.hasNext ();)
		{
			PluginContext context = (PluginContext) i.next ();

			if (context.getPlugin ().getName ().equals (name))
			{
				return context.getPlugin ();
			}
		}

		return null;
	}
}