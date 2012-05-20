/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcexmlparser.creator;


import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.resource.ResourceNode;
import de.iritgo.openmetix.core.resource.ResourceService;
import de.iritgo.openmetix.core.resource.resourcexmlparser.BaseCreator;
import de.iritgo.openmetix.core.resource.resourcexmlparser.ContinueException;
import de.iritgo.openmetix.core.resource.resourcexmlparser.ElementContainer;
import de.iritgo.openmetix.core.resource.resourcexmlparser.ElementIterator;
import de.iritgo.openmetix.core.resource.resourcexmlparser.MethodIterator;
import de.iritgo.openmetix.core.resource.resourcexmlparser.NodeContainer;
import org.jdom.Attribute;
import org.jdom.Element;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * CreateNode.
 *
 * @version $Id: CreateNode.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class CreateNode
	extends BaseCreator
{
	public CreateNode ()
	{
	}

	public void work (NodeContainer nodeContainer, ElementIterator i)
		throws ContinueException
	{
		Object node = (ResourceNode) nodeContainer.getNode ();

		Element element = ((ElementContainer) i.current ()).getElement ();
		String path = ((ElementContainer) i.current ()).getPath ();

		String tagName = element.getName ();
		List attributeList = element.getAttributes ();

		String nodeClass = (String) classMap.get (tagName);

		if (nodeClass != null)
		{
			ResourceService resourceService = new ResourceService((ResourceNode) node);

			try
			{
				Class classObject = Class.forName (nodeClass);
				Object[] params = generateConstructorParams (classObject, tagName, attributeList);
				Class[] classes = generateConstructorClasses (classObject, tagName, attributeList);

				resourceService = new ResourceService((ResourceNode) node);
				node = ((Constructor) classObject.getConstructor (classes)).newInstance (params);
				resourceService.addNode ("", (ResourceNode) node);
				nodeContainer.setNode (node);
				Log.log (
					"resource", "[XMLParser] CreateNode.work",
					"create node '" + tagName + "'-'" +
					(String) element.getAttribute ("treename").getValue () + "'.", Log.INFO);
			}
			catch (NoSuchMethodException e)
			{
				Log.log (
					"resource", "[XMLParser] CreateNode.work", "NoSuchMethodException", Log.FATAL);
			}
			catch (InvocationTargetException e)
			{
				Log.log (
					"resource", "[XMLParser] CreateNode.work", "InvocationTargetException",
					Log.FATAL);
			}
			catch (ClassNotFoundException e)
			{
				Log.log (
					"resource", "[XMLParser] CreateNode.work", "ClassNotFoundException", Log.FATAL);
			}
			catch (IllegalAccessException e)
			{
				Log.log (
					"resource", "[XMLParser] CreateNode.work", "IllegalAccessException", Log.FATAL);
			}
			catch (InstantiationException e)
			{
				Log.log (
					"resource", "[XMLParser] CreateNode.work", "InstantiationException", Log.FATAL);
			}

			throw new ContinueException();
		}
	}

	public Object[] generateConstructorParams (
		Class classObject, String methodName, List attributeList)
	{
		ArrayList objectParams = new ArrayList();
		MethodIterator mi = new MethodIterator(classObject.getConstructors ());

		while (mi.hasNext ())
		{
			Constructor constructor = (Constructor) mi.next ();

			Class[] params = constructor.getParameterTypes ();

			if (params.length != attributeList.size ())
			{
				continue;
			}

			for (int i = 0; i < params.length; ++i)
			{
				Object o = getObject (params[i].getName (), (Attribute) attributeList.get (i));

				objectParams.add (o);
			}
		}

		return objectParams.toArray ();
	}

	public Class[] generateConstructorClasses (
		Class classObject, String methodName, List attributeList)
	{
		Object objectParams = null;

		try
		{
			objectParams =
				Array.newInstance (Class.forName ("java.lang.Class"), attributeList.size ());
		}
		catch (ClassNotFoundException e)
		{
			Log.log (
				"resource", "[XMLParser] CreateNode.generateConstructorClasses",
				"ClassNotFoundException", Log.FATAL);
		}

		MethodIterator mi = new MethodIterator(classObject.getConstructors ());

		while (mi.hasNext ())
		{
			Constructor constructor = (Constructor) mi.next ();

			Class[] params = constructor.getParameterTypes ();

			if (params.length != attributeList.size ())
			{
				continue;
			}

			for (int i = 0; i < params.length; ++i)
			{
				Object o = getObject (params[i].getName (), (Attribute) attributeList.get (i));

				Array.set (objectParams, i, o.getClass ());
			}
		}

		return (Class[]) objectParams;
	}
}