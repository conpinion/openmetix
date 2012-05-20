/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.windroseinstrument.gui;


import org.jfree.chart.needle.MeterNeedle;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;


/**
 * @version $Id: ExtremaNeedle.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ExtremaNeedle
	extends MeterNeedle
	implements Serializable
{
	/** Startpoint for the circle arc */
	private double startPoint;

	/** Endpoint for the circle arc */
	private double endPoint;

	/**
	 * Default constructor.
	 */
	public ExtremaNeedle ()
	{
		super();
	}

	/**
	 * Set the startpoint for the circle arc
	 *
	 * @param value
	 */
	protected void setStartPoint (double value)
	{
		startPoint = value;
	}

	/**
	 * Set the endpoint for the circle arc
	 *
	 * @param value
	 */
	protected void setEndPoint (double value)
	{
		endPoint = value;
	}

	/**
	 * Draws the needle.
	 *
	 * @param g2  the graphics device.
	 * @param plotArea  the plot area.
	 * @param rotate  the rotation point.
	 * @param angle  the angle.
	 */
	protected void drawNeedle (Graphics2D g2, Rectangle2D plotArea, Point2D rotate, double angle)
	{
		double circle = (plotArea.getMaxY () / 100) * 70;
		double halfcirlce = circle / 2;
		GeneralPath shape = new GeneralPath();

		shape.append (
			new Arc2D.Double(
				rotate.getX () - halfcirlce, rotate.getY () - halfcirlce, circle, circle,
				-90 - (startPoint), endPoint, Arc2D.OPEN), true);
		shape.lineTo ((float) rotate.getX (), (float) rotate.getY ());
		shape.append (
			new Arc2D.Double(
				rotate.getX () - halfcirlce, rotate.getY () - (halfcirlce), circle, circle,
				-90 - (startPoint), -endPoint, Arc2D.OPEN), true);
		shape.lineTo ((float) rotate.getX (), (float) rotate.getY ());
		shape.closePath ();
		defaultDisplay (g2, shape);
	}

	/**
	 * Tests another object for equality with this object.
	 *
	 * @param object  the object to test.
	 *
	 * @return A boolean.
	 */
	public boolean equals (Object object)
	{
		if (object == null)
		{
			return false;
		}

		if (object == this)
		{
			return true;
		}

		if (super.equals (object) && object instanceof ExtremaNeedle)
		{
			return true;
		}

		return false;
	}
}