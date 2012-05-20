/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcexmlparser;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.resource.ResourceNode;
import de.iritgo.openmetix.core.resource.ResourceNotFoundException;
import de.iritgo.openmetix.core.resource.ResourceService;
import de.iritgo.openmetix.core.resource.resourcexmlparser.creator.DefaultCreator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;


/**
 * Resource XML-Parser.
 *
 * @version $Id: XMLParser.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class XMLParser
	extends BaseObject
{
	HashMap classMap = new HashMap();

	/**
	 * Create a new XMLParser.
	 *
	 * @param resourceXMLFile The XML-Description file.
	 * @param resourceService The resource generator.
	 */
	public XMLParser (String resourceXMLFile, ResourceService resourceService)
	{
		try
		{
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build (new File(resourceXMLFile));

			transform (doc, resourceService);
		}
		catch (JDOMException e)
		{
			Log.log (
				"resource", "XMLParser.XMLParser", "ResourceDescription-File not found", Log.FATAL);
			e.printStackTrace ();
		}
		catch (IOException e)
		{
			Log.log (
				"resource", "XMLParser.XMLParser", "ResourceDescription-File not found", Log.FATAL);
			e.printStackTrace ();
		}
	}

	/**
	 * Create a new XMLParser.
	 *
	 * @param resourceXMLURL The XML-Description accessed by URL.
	 * @param resourceService The resource generator.
	 */
	public XMLParser (URL resourceXMLURL, ResourceService resourceService)
	{
		try
		{
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build (resourceXMLURL);

			transform (doc, resourceService);
		}
		catch (JDOMException e)
		{
			Log.log (
				"resource", "XMLParser.XMLParser", "ResourceDescription-File not found", Log.FATAL);
			e.printStackTrace ();
		}
		catch (IOException e)
		{
			Log.log (
				"resource", "XMLParser.XMLParser", "ResourceDescription-File not found", Log.FATAL);
			e.printStackTrace ();
		}
	}

	/**
	 * Transform the XML document.
	 *
	 * @param doc The XML document.
	 * @param resourceService The resource generator.
	 */
	private void transform (Document doc, ResourceService resourceService)
	{
		Element element = doc.getRootElement ();

		transformToNode (element, resourceService.getBaseNode ());
	}

	/**
	 * Transform the XML-Tree to a ResourceNodetree.
	 */
	public void transformToNode (Element element, ResourceNode node)
	{
		ElementIterator i = new ElementIterator(element);
		ResourceService resourceBase = new ResourceService(node);
		NodeContainer nodeContainer = new NodeContainer();

		nodeContainer.setNode (node);

		DefaultCreator defaultCreater = new DefaultCreator();

		for (; i.hasNext (); i.next ())
		{
			ElementContainer elementContainer = (ElementContainer) i.current ();

			try
			{
				try
				{
					if (resourceBase.getNode (elementContainer.getPath ()) != null)
					{
						nodeContainer.setNode (resourceBase.getNode (elementContainer.getPath ()));

						continue;
					}
				}
				catch (ResourceNotFoundException x)
				{
				}

				defaultCreater.work (nodeContainer, i);
			}
			catch (ContinueException x)
			{
			}
		}
	}
}