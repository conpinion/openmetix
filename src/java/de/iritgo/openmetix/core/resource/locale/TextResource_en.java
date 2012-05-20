/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.locale;



/**
 * TextResource_en.
 *
 * @version $Id: TextResource_en.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class TextResource_en
	extends TextResource
{
	/** The resoureces. */
	static TextResource_en textResource;

	/**
	 * Create a new TextResource_en.
	 */
	public TextResource_en ()
	{
		super();
		textResource = this;
	}

	/**
	 * Get the resources.
	 *
	 * @return The resources.
	 */
	static public TextResource_en getTextResource ()
	{
		return textResource;
	}
}