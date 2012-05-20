/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource;


import de.iritgo.openmetix.core.logger.Log;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;


/**
 * ImageComponent.
 *
 * @version $Id: ImageComponent.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class ImageComponent
	extends Canvas
{
	/** The image. */
	private Image image;

	/**
	 * Create a new ImageComponent.
	 */
	public ImageComponent (String fname)
	{
		super();

		image = getToolkit ().getImage (fname);

		MediaTracker mt = new MediaTracker(this);

		mt.addImage (image, 0);

		try
		{
			mt.waitForAll ();
		}
		catch (InterruptedException e)
		{
			Log.log (
				"resources", "ImageComponent.ImageComponent", "Image: " + fname + " not found!",
				Log.WARN);
		}
	}

	/**
	 * Return the Image.
	 *
	 * @return The Image.
	 */
	public Image getImage ()
	{
		return image;
	}

	/**
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint (Graphics g)
	{
		g.drawImage (image, 1, 1, this);
	}

	/**
	 * @see java.awt.Component#getPreferredSize()
	 */
	public Dimension getPreferredSize ()
	{
		return new Dimension(image.getWidth (this), image.getHeight (this));
	}

	/**
	 * @see java.awt.Component#getMinimumSize()
	 */
	public Dimension getMinimumSize ()
	{
		return new Dimension(image.getWidth (this), image.getHeight (this));
	}
}