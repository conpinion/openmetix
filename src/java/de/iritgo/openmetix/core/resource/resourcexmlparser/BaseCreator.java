/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcexmlparser;


import de.iritgo.openmetix.core.logger.Log;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import java.util.HashMap;


/**
 * BaseCreator.
 *
 * @version $Id: BaseCreator.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class BaseCreator
	implements NodeCreator
{
	/** Class map. */
	protected static HashMap classMap = new HashMap();

	/**
	 * Create a new BaseCreator.
	 */
	public BaseCreator ()
	{
	}

	/**
	 * @see de.iritgo.openmetix.core.resource.resourcexmlparser.NodeCreator#work(de.iritgo.openmetix.core.resource.resourcexmlparser.NodeContainer, de.iritgo.openmetix.core.resource.resourcexmlparser.ElementIterator)
	 */
	public void work (NodeContainer node, ElementIterator i)
		throws ContinueException
	{
		Element element = ((ElementContainer) i.current ()).getElement ();

		String tagName = element.getName ();
		String body = element.getTextTrim ();

		if (tagName.equals ("classdef"))
		{
			classMap.put (element.getAttribute ("class").getValue (), body);
			Log.log (
				"resource", "[XMLParser] BaseCreater.work",
				"adding classdef '" + element.getAttribute ("class").getValue () + "'.", Log.INFO);
			throw new ContinueException();
		}
	}

	/**
	 * @param classType
	 * @param attribute
	 * @return
	 */
	public Object getObject (String classType, Attribute attribute)
	{
		try
		{
			if (classType.equals ("java.lang.Boolean"))
			{
				return new Boolean(attribute.getBooleanValue ());
			}

			if (classType.equals ("java.lang.Integer"))
			{
				return new Integer(attribute.getIntValue ());
			}

			if (classType.equals ("java.lang.Float"))
			{
				return new Float(attribute.getFloatValue ());
			}

			if (classType.equals ("java.lang.Double"))
			{
				return new Double(attribute.getDoubleValue ());
			}

			if (classType.equals ("java.lang.String"))
			{
				return new String(attribute.getValue ());
			}

			if (classType.equals ("java.lang.Object"))
			{
				return new String(attribute.getValue ());
			}
		}
		catch (DataConversionException e)
		{
		}

		return null;
	}
}