/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.locale;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.logger.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;


/**
 * TextResource.
 *
 * @version $Id: TextResource.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class TextResource
	extends ResourceBundle
{
	/** The strings. */
	private Properties localisations;

	/**
	 * Create a new TextResource.
	 */
	public TextResource ()
	{
		super();
		localisations = new Properties();
	}

	/**
	 * @see java.util.ResourceBundle#getKeys()
	 */
	public Enumeration getKeys ()
	{
		return localisations.keys ();
	}

	/**
	 * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
	 */
	public Object handleGetObject (String key)
	{
		return localisations.get (key);
	}

	/**
	 * Get the parent resources.
	 *
	 * @return The parent resources.
	 */
	public ResourceBundle getParent ()
	{
		return parent;
	}

	/**
	 * Add a resource value.
	 *
	 * @param key
	 * @param value
	 */
	public void addResource (String key, String value)
	{
		localisations.put (key, value);
	}

	/**
	 * Load resources from a jar file.
	 *
	 * @param directory The directory.
	 * @param fileName The filename of the jar file.
	 * @param resourceName The resource name.
	 */
	public void loadFromJarFile (String directory, String fileName, String resourceName)
	{
		try
		{
			loadFromProperties (
				getInputStreamFromJarFile (
					directory + Engine.instance ().getFileSeparator () + fileName, resourceName));
		}
		catch (IOException x)
		{
			Log.logFatal (
				"system", "TextResource.loadFromJarFile",
				"Unable to unload property file " + fileName + "!" + resourceName + ": " +
				x.getMessage ());
		}
	}

	/**
	 * Load resources from file.
	 *
	 * @param directory The directory.
	 * @param fileName The filename.
	 */
	public void loadFromFile (String directory, String fileName)
	{
		try
		{
			File file = new File(directory + Engine.instance ().getFileSeparator () + fileName);

			loadFromProperties (new FileInputStream(file));
		}
		catch (Exception x)
		{
			Log.logFatal (
				"system", "TextResource.loadFromFile",
				"Unable to load property file " + fileName + ": " + x);
		}
	}

	/**
	 * Unload resources.
	 */
	public void unloadFromFile (String directory, String fileName)
	{
		try
		{
			File file = new File(directory + Engine.instance ().getFileSeparator () + fileName);

			unloadFromProperties (new FileInputStream(file));
		}
		catch (Exception x)
		{
			Log.logFatal (
				"system", "TextResource.unloadFromFile",
				"Unable to unload property file " + fileName + ": " + x);
		}
	}

	/**
	 * Unload resources.
	 */
	public void unloadFromJarFile (String directory, String fileName, String resourceName)
	{
		try
		{
			unloadFromProperties (
				getInputStreamFromJarFile (
					directory + Engine.instance ().getFileSeparator () + fileName, resourceName));
		}
		catch (IOException x)
		{
			Log.logFatal (
				"system", "TextResource.unloadFromJarFile",
				"Unable to unload property file " + fileName + "!" + resourceName + ": " +
				x.getMessage ());
		}
	}

	/**
	 * Get an input stream from a jar file.
	 *
	 * @param file
	 * @param resourceName
	 * @return
	 */
	public InputStream getInputStreamFromJarFile (String file, String resourceName)
	{
		try
		{
			JarFile jarFile = new JarFile(file);
			ZipEntry entry = jarFile.getEntry (resourceName);

			if (entry != null)
			{
				return jarFile.getInputStream (entry);
			}
			else
			{
				throw new Exception("entry not found.");
			}
		}
		catch (Exception x)
		{
			Log.logFatal (
				"system", "TextResource.getInputStreamFromJarFile",
				"Unable to load property file " + file + "!" + resourceName + ": " +
				x.getMessage ());
		}

		return null;
	}

	/**
	 * Load localizations from an input stream.
	 *
	 * @param is
	 * @throws IOException
	 */
	public void loadFromProperties (InputStream is)
		throws IOException
	{
		localisations.load (is);
	}

	/**
	 * Unload resources.
	 *
	 * @param is
	 * @throws IOException
	 */
	public void unloadFromProperties (InputStream is)
		throws IOException
	{
		Properties properties = new Properties();

		properties.load (is);

		for (Iterator i = properties.values ().iterator (); i.hasNext ();)
		{
			localisations.remove ((String) i.next ());
		}
	}

	/**
	 * Load resources from an input stream.
	 *
	 * @param in The input stream.
	 */
	public void loadFromInputStream (InputStream in)
		throws IOException
	{
		localisations.load (in);
	}

	/**
	 * Unload resources from an input stream.
	 *
	 * @param is The input stream.
	 */
	public void unloadFromInputStream (InputStream is)
		throws IOException
	{
		Properties properties = new Properties();

		properties.load (is);

		for (Iterator i = properties.values ().iterator (); i.hasNext ();)
		{
			localisations.remove ((String) i.next ());
		}
	}

	/**
	 * Load the translations with a class loader.
	 *
	 * @param klass The class which class loader should be used.
	 * @param resourceName The name of the resource file.
	 */
	public void loadWithClassLoader (Class klass, String resourceName)
	{
		try
		{
			loadFromInputStream (klass.getResourceAsStream (resourceName));
		}
		catch (Exception x)
		{
			Log.logFatal (
				"system", "TextResource.loadWithClassLoader",
				"Unable to load resources " + klass.getName () + "!" + resourceName + ": " +
				x.getMessage ());
		}
	}

	/**
	 * Unload the translations with a class loader.
	 *
	 * @param klass The class which class loader should be used.
	 * @param resourceName The name of the resource file.
	 */
	public void unloadWithClassLoader (Class klass, String resourceName)
	{
		try
		{
			unloadFromInputStream (klass.getResourceAsStream (resourceName));
		}
		catch (Exception x)
		{
			Log.logFatal (
				"system", "TextResource.unloadWithClassLoader",
				"Unable to unload resources " + klass.getName () + "!" + resourceName + ": " +
				x.getMessage ());
		}
	}
}