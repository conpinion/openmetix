/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcetypes;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.resource.ImageComponent;
import de.iritgo.openmetix.core.resource.ResourceNotFoundException;
import de.iritgo.openmetix.core.resource.ResourcePersistent;
import java.awt.Image;


/**
 * ResourceImage.
 *
 * @version $Id: ResourceImage.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class ResourceImage
	extends ResourcePersistent
{
	/** The image. */
	private ImageComponent imageComponent;

	/**
	 * Create a new ResourceImage.
	 *
	 * @param name The description of the resource.
	 * @param nodeName The node name.
	 */
	public ResourceImage (String name, String nodeName, String resourcePath)
	{
		super(name, nodeName, resourcePath);
	}

	/**
	 * Return the Image.
	 *
	 * @return The Object.
	 */
	public Image getImage ()
		throws ResourceNotFoundException
	{
		if (imageComponent == null)
		{
			Log.log ("resources", "getImage", "Loading Image: " + getResourcePath (), Log.INFO);

			imageComponent =
				new ImageComponent(
					Engine.instance ().getSystemDir () + "/" + (String) getResourcePath ());
		}

		return imageComponent.getImage ();
	}

	/**
	 * Get the image.
	 *
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public ImageComponent getImageComponent ()
		throws ResourceNotFoundException
	{
		if (imageComponent == null)
		{
			Log.log ("resources", "getImage", "Loading Image: " + getResourcePath (), Log.INFO);

			imageComponent = new ImageComponent((String) getResourcePath ());
		}

		return imageComponent;
	}
}