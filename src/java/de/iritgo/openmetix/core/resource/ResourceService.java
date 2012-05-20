/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.resource.locale.TextResource;
import de.iritgo.openmetix.core.resource.resourcetypes.ResourceImage;
import de.iritgo.openmetix.core.resource.resourcetypes.ResourceObject;
import de.iritgo.openmetix.core.resource.resourcetypes.ResourceString;
import de.iritgo.openmetix.core.resource.resourcexmlparser.XMLParser;
import de.iritgo.openmetix.core.tools.NamePartIterator;
import de.iritgo.openmetix.core.tools.StringTools;
import java.awt.Image;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * ResourceService.
 *
 * @version $Id: ResourceService.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class ResourceService
	extends BaseObject
{
	/** The root node. */
	private ResourceNode baseNode;

	/** The current locale. */
	private Locale locale;

	/** The resource bundle. */
	private ResourceBundle resourceBundle;

	/**
	 * Create a new ResourceService.
	 *
	 * @param baseNode The root node.
	 */
	public ResourceService (ResourceNode baseNode)
	{
		this.baseNode = baseNode;
		locale = Locale.GERMAN;
		resourceBundle = getResourceBundle ();
	}

	/**
	 * Load the XML desciption from an URL.
	 *
	 * @param url The URL to load.
	 */
	public void loadResources (URL url)
	{
		XMLParser parser = new XMLParser(url, this);

		Log.logInfo (
			"system", "ResourceService.loadResources", "Resources loaded from URL '" + url + "'");
	}

	/**
	 * Load the XML-Desciption from a file.
	 *
	 * @param fileName The filename to load.
	 */
	public void loadResources (String fileName)
	{
		XMLParser parser = new XMLParser(fileName, this);

		Log.logInfo (
			"system", "ResourceService.loadResources",
			"Resources loaded from file '" + fileName + "'");
	}

	/**
	 * Return the base node.
	 *
	 * @return The base node.
	 */
	public ResourceNode getBaseNode ()
	{
		return baseNode;
	}

	/**
	 * Add a node to the resources.
	 *
	 * @param name The node name.
	 * @param resourceLoader The resource loader for the resource.
	 * @param newNode The node.
	 */
	public void addNode (String name, String resourceLoader, ResourcePersistent newNode)
	{
		try
		{
			newNode.setResourceLoader (getResourceLoader (resourceLoader));
		}
		catch (ResourceNotFoundException e)
		{
		}

		addNode (name, newNode);
	}

	/**
	 * Add node to the resources
	 *
	 * @param treePos The absloute position of the node.
	 * @param newNode The node.
	 */
	public void addNode (String treePos, ResourceNode newNode)
	{
		NamePartIterator i = new NamePartIterator(treePos);
		ResourceNode node = baseNode;
		ResourceNode lastNode = baseNode;

		while (i.hasNext ())
		{
			String partName = (String) i.next ();

			lastNode = node;
			node = node.getNodeByName (partName);

			if (node == null)
			{
				if (i.hasNext ())
				{
					node = new ResourceNode("directory." + partName, partName);
					lastNode.addNode (node);

					continue;
				}
			}
		}

		lastNode.addNode (newNode);
	}

	/**
	 * Get a node by name.
	 */
	public ResourceNode getNode (String name)
		throws ResourceNotFoundException
	{
		NamePartIterator i = new NamePartIterator(name);
		ResourceNode node = baseNode;

		while (i.hasNext ())
		{
			node = node.getNodeByName ((String) i.next ());

			if (node == null)
			{
				throw new ResourceNotFoundException(name);
			}
		}

		return node;
	}

	/**
	 * Get a resource description.
	 */
	public String getResourceDescription (String name)
		throws ResourceNotFoundException
	{
		return getNode (name).getDescription ();
	}

	/**
	 * Get a resource value.
	 */
	public String getString (String name, boolean fireException)
		throws ResourceNotFoundException
	{
		try
		{
			return resourceBundle.getString (name);
		}
		catch (MissingResourceException x)
		{
		}

		try
		{
			return ((ResourceString) getNode (name)).getValue ();
		}
		catch (ResourceNotFoundException x)
		{
			if (fireException)
			{
				throw new ResourceNotFoundException(name);
			}
		}

		return "!" + name + "!";
	}

	/**
	 * Get a resource value.
	 */
	public String getString (String name)
		throws ResourceNotFoundException
	{
		return getString (name, true);
	}

	/**
	 * Get a resource value.
	 */
	public int getInt (String name)
		throws ResourceNotFoundException
	{
		return Integer.parseInt (getString (name, true));
	}

	/**
	 * Get a resource value.
	 */
	public String getStringWithoutException (String name)
	{
		if (StringTools.isEmpty (name))
		{
			return "";
		}

		try
		{
			return getString (name, false);
		}
		catch (ResourceNotFoundException x)
		{
		}

		return "!FATAL-ERROR (String not found)!";
	}

	/**
	 * Get the resource loader.
	 *
	 * @return The resource loader.
	 */
	public ResourceLoader getResourceLoader (String name)
		throws ResourceNotFoundException
	{
		return (ResourceLoader) ((ResourceObject) getNode (name)).getClone ();
	}

	/**
	 * Get a resource value.
	 */
	public Object getObject (String name)
		throws ResourceNotFoundException
	{
		return ((ResourceObject) getNode (name)).getClone ();
	}

	/**
	 * Get a resource value.
	 */
	public Image getImage (String name)
		throws ResourceNotFoundException
	{
		return ((ResourceImage) getNode (name)).getImage ();
	}

	/**
	 * Get a resource value.
	 */
	public ImageComponent getImageComponent (String name)
		throws ResourceNotFoundException
	{
		return ((ResourceImage) getNode (name)).getImageComponent ();
	}

	/**
	 * Set the Locale.
	 *
	 * @param locale The locale.
	 */
	public void setLocale (Locale locale)
	{
		this.locale = locale;
	}

	/**
	 * Get the resource bundle.
	 */
	public ResourceBundle getResourceBundle ()
	{
		if (resourceBundle == null)
		{
			resourceBundle =
				ResourceBundle.getBundle (
					"de.iritgo.openmetix.core.resource.locale.TextResource", locale);
		}

		return resourceBundle;
	}

	/**
	 * Set the Locale and create a new resource bundle.
	 *
	 * @param locale The new Locale.
	 */
	public void updateResourceBundle (Locale locale)
	{
		setLocale (locale);
		resourceBundle =
			ResourceBundle.getBundle (
				"de.iritgo.openmetix.core.resource.locale.TextResource", locale);
	}

	/**
	 * Load the translations from a File
	 */
	public void loadTranslationsFromFile (String resourceDir, String filePrefix)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource.loadFromFile (
			resourceDir, filePrefix + "_" + locale.getLanguage () + ".properties");
	}

	/**
	 * Unload the translations.
	 */
	public void unloadTranslationsFromFile (String resourceDir, String filePrefix)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource.unloadFromFile (
			resourceDir, filePrefix + "_" + locale.getLanguage () + ".properties");
	}

	/**
	 * Load the translations form a jar file.
	 */
	public void loadTranslationsFromJarFile (
		String resourceDir, String filename, String resourceName)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource.loadFromJarFile (
			resourceDir, filename, resourceName + "_" + locale.getLanguage () + ".properties");
	}

	/**
	 * Unload the translations.
	 */
	public void unloadTranslationsFromJarFile (
		String resourceDir, String filename, String resourceName)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource.unloadFromJarFile (
			resourceDir, filename, resourceName + "_" + locale.getLanguage () + ".properties");
	}

	/**
	 * Load the translations with a class loader.
	 *
	 * @param klass The class which class loader should be used.
	 * @param resourceName The name of the resource file.
	 */
	public void loadTranslationsWithClassLoader (Class klass, String resourceName)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource.loadWithClassLoader (
			klass, resourceName + "_" + locale.getLanguage () + ".properties");
	}

	/**
	 * Unload the translations.
	 *
	 * @param klass The class which class loader should be used.
	 * @param resourceName The name of the resource file.
	 */
	public void unloadTranslationsWithClassLoader (Class klass, String resourceName)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource.unloadWithClassLoader (
			klass, resourceName + "_" + locale.getLanguage () + ".properties");
	}
}