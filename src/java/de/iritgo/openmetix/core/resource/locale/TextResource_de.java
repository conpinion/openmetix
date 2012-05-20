/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.locale;



/**
 * TextResource_de.
 *
 * @version $Id: TextResource_de.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class TextResource_de
	extends TextResource
{
	/** The resources. */
	static TextResource_de textResource;

	/**
	 * Create a new TextResource_de.
	 */
	public TextResource_de ()
	{
		super();
		textResource = this;
	}

	/**
	 * Get the resources.
	 *
	 * @return The resources.
	 */
	static public TextResource_de getTextResource ()
	{
		return textResource;
	}
}