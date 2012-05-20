/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcexmlparser;


import org.jdom.Element;


/**
 * Element Container.
 *
 * @version $Id: ElementContainer.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ElementContainer
{
	/** Element position. */
	private int pos = 0;

	/** Element. */
	private Element element;

	/** Element path. */
	private String path;

	/**
	 * Create a new ElementContainer.
	 */
	public ElementContainer (Element element, int pos, String path)
	{
		this.element = element;
		this.pos = pos;
		this.path = path;
	}

	/**
	 * Get the element position.
	 *
	 * @return The position.
	 */
	public int getPos ()
	{
		return pos;
	}

	/**
	 * Get the element.
	 *
	 * @return The element.
	 */
	public Element getElement ()
	{
		return element;
	}

	/**
	 * Get the element path.
	 *
	 * @return The element path.
	 */
	public String getPath ()
	{
		return path;
	}
}