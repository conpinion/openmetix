/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;


/**
 * This is a custom internal frame manager that is restricts the window
 * positions to a specific raster grid.
 *
 * @version $Id: MetixDesktopManager.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class MetixDesktopManager
	extends DefaultDesktopManager
{
	/** The grid spacing. */
	private int rasterSize;

	/**
	 * Create a new MetixDesktopManager.
	 */
	public MetixDesktopManager ()
	{
		rasterSize = 20;
	}

	/**
	 * Retrieve the current raster size.
	 *
	 * @return The current raster size.
	 */
	public int getRasterSize ()
	{
		return rasterSize;
	}

	/**
	 * Set the raster size.
	 *
	 * @param rasterSize The new raster size.
	 */
	public void setRasterSize (int rasterSize)
	{
		this.rasterSize = rasterSize;
	}

	/**
	 * Called to set the bounds of an internal frame.
	 *
	 * @param component The internal frame.
	 * @param x The new x position.
	 * @param y The new y position.
	 * @param w The new width.
	 * @param h The new height.
	 */
	public void setBoundsForFrame (JComponent component, int x, int y, int w, int h)
	{
		if (component.getClass ().equals (JInternalFrame.class) || rasterSize == 0)
		{
			super.setBoundsForFrame (component, x, y, w, h);
		}

		super.setBoundsForFrame (
			component, (x / rasterSize) * rasterSize, (y / rasterSize) * rasterSize,
			(w / rasterSize) * rasterSize, (h / rasterSize) * rasterSize);
	}

	/**
	 * Called during dragging of an internal frame.
	 *
	 * @param component The internal frame.
	 * @param x The new x position.
	 * @param y The new y position.
	 */
	public void dragFrame (JComponent component, int x, int y)
	{
		if (component.getClass ().equals (JInternalFrame.class) || rasterSize == 0)
		{
			super.dragFrame (component, x, y);
		}

		super.dragFrame (component, (x / rasterSize) * rasterSize, (y / rasterSize) * rasterSize);
	}
}