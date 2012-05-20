/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.userprofile;


import de.iritgo.openmetix.framework.base.DataObject;


/**
 * Data object that represents the user preferences.
 *
 * @version $Id: Preferences.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class Preferences
	extends DataObject
{
	/**
	 * Create a new Preferences.
	 */
	public Preferences ()
	{
		super("Preferences");

		putAttribute ("language", "de");
		putAttribute ("lookAndFeel", "Metix");
		putAttribute ("alwaysDrawWindowContents", new Integer(1));
		putAttribute ("drawAntiAliased", new Integer(0));
		putAttribute ("alignWindowsToRaster", new Integer(1));
		putAttribute ("rasterSize", new Integer(20));
	}

	/**
	 * Create a new Preferences.
	 *
	 * @param uniqueId The unique id.
	 */
	public Preferences (long uniqueId)
	{
		this();
		setUniqueId (uniqueId);
	}

	/**
	 * Set the interface language.
	 *
	 * @param language The language specified by a locale id.
	 */
	public void setLanguage (String language)
	{
		putAttribute ("language", language);
	}

	/**
	 * Get the interface language.
	 *
	 * @return The language specified by a locale id.
	 */
	public String getLanguage ()
	{
		return getStringAttribute ("language");
	}

	/**
	 * Set the name of the look and feel to use.
	 *
	 * @param lookAndFeel The look and feel name.
	 */
	public void setLookAndFeel (String lookAndFeel)
	{
		putAttribute ("lookAndFeel", lookAndFeel);
	}

	/**
	 * Get the name of the look and feel to use.
	 *
	 * @return The look and feel name.
	 */
	public String getLookAndFeel ()
	{
		return getStringAttribute ("lookAndFeel");
	}

	/**
	 * Determine wether to always draw window contents.
	 *
	 * @param alwaysDrawWindowContents True if the windows contents should always be drawn.
	 */
	public void setAlwaysDrawWindowContents (boolean alwaysDrawWindowContents)
	{
		putAttribute ("alwaysDrawWindowContents", new Integer(alwaysDrawWindowContents ? 1 : 0));
	}

	/**
	 * Check wether to always draw window contents.
	 *
	 * @return True if the windows contents should always be drawn.
	 */
	public boolean getAlwaysDrawWindowContents ()
	{
		return getIntAttribute ("alwaysDrawWindowContents") != 0;
	}

	/**
	 * Determine wether to draw antialiased.
	 *
	 * @param drawAntiAliased True if to draw antialiased.
	 */
	public void setDrawAntiAliased (boolean drawAntiAliased)
	{
		putAttribute ("drawAntiAliased", new Integer(drawAntiAliased ? 1 : 0));
	}

	/**
	 * Check wether to draw antialiased.
	 *
	 * @return True if to draw antialiased.
	 */
	public boolean getDrawAntiAliased ()
	{
		return getIntAttribute ("drawAntiAliased") != 0;
	}

	/**
	 * Determine wether to align the windows to a raster.
	 *
	 * @param alignWindowsToRaster True if the windows should be aligned.
	 */
	public void setAlignWindowsToRaster (boolean alignWindowsToRaster)
	{
		putAttribute ("alignWindowsToRaster", new Integer(alignWindowsToRaster ? 1 : 0));
	}

	/**
	 * Check wether to align the windows to a raster.
	 *
	 * @return True if the windows should be aligned.
	 */
	public boolean getAlignWindowsToRaster ()
	{
		return getIntAttribute ("alignWindowsToRaster") != 0;
	}

	/**
	 * Set the grid raster size.
	 *
	 * @param rasterSize The new raster size.
	 */
	public void setRasterSize (int rasterSize)
	{
		putAttribute ("rasterSize", rasterSize);
	}

	/**
	 * Get the grid raster size.
	 *
	 * @return The raster size.
	 */
	public int getRasterSize ()
	{
		return getIntAttribute ("rasterSize");
	}
}