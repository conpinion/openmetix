/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.barinstrument.plot;


import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.ValueAxisPlot;
import org.jfree.data.DatasetChangeEvent;
import org.jfree.data.Range;
import org.jfree.data.ValueDataset;
import org.jfree.ui.RectangleEdge;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.List;


/**
 * A custom meter chart.
 *
 * @version $Id: BarPlot.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class BarPlot
	extends Plot
	implements ValueAxisPlot
{
	/** Default spacing. */
	protected static final int GAP = 30;

	/** Default minimum axis value. */
	protected static final double DEFAULT_LOWER_BOUND = 0.0;

	/** Default maximum axis value. */
	protected static final double DEFAULT_UPPER_BOUND = 100.0;

	/** Default spacing. */
	protected static final int AXIS_GAP = 10;

	/** The lower bound for the barinstrument. */
	private double lowerBound = DEFAULT_LOWER_BOUND;

	/** The upper bound for the barinstrument. */
	private double upperBound = DEFAULT_UPPER_BOUND;

	/** The dataset for the plot. */
	private ValueDataset dataset;

	/** The range axis. */
	private ValueAxis rangeAxis;

	/** Maximum marker value. */
	private double maxMarkerValue = 70.0;

	/** If true the maximum marker is drawn. */
	private boolean maxMarkerShow = true;

	/** Minimum marker value. */
	private double minMarkerValue = 30.0;

	/** If true the minimum marker is drawn. */
	private boolean minMarkerShow = true;

	/** Color of the maximum marker. */
	private Paint maxMarkerPaint = Color.red;

	/** Color of the minimum marker. */
	private Paint minMarkerPaint = Color.green;

	/** Color of the bar. */
	private Paint barPaint = Color.blue;

	/** If true the instrument use full window size for painting. */
	private boolean fitToWindow = true;

	/**
	 * Create a new bar plot.
	 *
	 * @param dataset The chart data.
	 */
	public BarPlot (ValueDataset dataset)
	{
		this.dataset = dataset;

		if (dataset != null)
		{
			dataset.addChangeListener (this);
		}

		NumberAxis axis = new NumberAxis(null);

		axis.setStandardTickUnits (NumberAxis.createIntegerTickUnits ());
		axis.setAxisLineVisible (false);

		setRangeAxis (axis);
		setAxisRange ();
	}

	/**
	 * Returns a short string describing the type of plot.
	 *
	 * @return A short string describing the type of plot.
	 */
	public String getPlotType ()
	{
		return "Bar_Plot";
	}

	/**
	 * Returns the lower bound for the thermometer.
	 * The data value can be set lower than this, but it will not be shown in the thermometer.
	 *
	 * @return The lower bound.
	 */
	public double getLowerBound ()
	{
		return this.lowerBound;
	}

	/**
	 * Sets the lower bound for the meter.
	 *
	 * @param lower The lower bound.
	 */
	public void setLowerBound (double lower)
	{
		this.lowerBound = lower;
		setAxisRange ();
	}

	/**
	 * Returns the data range.
	 *
	 * @param axis The axis.
	 * @return The range of data displayed.
	 */
	public Range getDataRange (ValueAxis axis)
	{
		return new Range(this.lowerBound, this.upperBound);
	}

	/**
	 * Returns the primary dataset for the plot.
	 *
	 * @return The primary dataset (possibly null).
	 */
	public ValueDataset getDataset ()
	{
		return this.dataset;
	}

	/**
	 * Sets the dataset for the plot, replacing the existing dataset if there is one.
	 *
	 * @param dataset The dataset (null permitted).
	 */
	public void setDataset (ValueDataset dataset)
	{
		ValueDataset existing = this.dataset;

		if (existing != null)
		{
			existing.removeChangeListener (this);
		}

		this.dataset = dataset;

		if (dataset != null)
		{
			setDatasetGroup (dataset.getGroup ());
			dataset.addChangeListener (this);
		}

		DatasetChangeEvent event = new DatasetChangeEvent(this, dataset);

		datasetChanged (event);
	}

	/**
	 * Returns the dataset cast to {@link ValueDataset} (provided for convenience).
	 *
	 * @return The dataset for the plot, cast as a {@link ValueDataset}.
	 * @deprecated Use getDataset() instead.
	 */
	public ValueDataset getData ()
	{
		return getDataset ();
	}

	/**
	 * Sets the data for the chart, replacing any existing data.
	 * Registered listeners are notified that the plot has been modified (this will normally
	 * trigger a chart redraw).
	 *
	 * @param dataset The new dataset.
	 * @deprecated Use setDataset(...) instead.
	 */
	public void setData (ValueDataset dataset)
	{
		setDataset (dataset);
	}

	/**
	 * Returns the range axis.
	 *
	 * @return The range axis.
	 */
	public ValueAxis getRangeAxis ()
	{
		return this.rangeAxis;
	}

	/**
	 * Sets the range axis for the plot.
	 * An exception is thrown if the new axis and the plot are not mutually compatible.
	 *
	 * @param axis The new axis.
	 */
	public void setRangeAxis (ValueAxis axis)
	{
		if (axis != null)
		{
			axis.setPlot (this);
			axis.addChangeListener (this);
		}

		if (this.rangeAxis != null)
		{
			this.rangeAxis.removeChangeListener (this);
		}

		this.rangeAxis = axis;
	}

	/**
	 * Sets the lower and upper bounds for the thermometer.
	 *
	 * @param lower The lower bound.
	 * @param upper The upper bound.
	 */
	public void setRange (double lower, double upper)
	{
		this.lowerBound = lower;
		this.upperBound = upper;
		setAxisRange ();
	}

	/**
	 * Sets the axis range to the current values in the rangeInfo array.
	 */
	protected void setAxisRange ()
	{
		this.rangeAxis.setRange (this.lowerBound, this.upperBound);
	}

	/**
	 * Returns null, since the thermometer plot won't require a legend.
	 *
	 * @return null.
	 * @deprecated use getLegendItems().
	 */
	public List getLegendItemLabels ()
	{
		return null;
	}

	/**
	 * Returns the legend items for the plot.
	 *
	 * @return null.
	 */
	public LegendItemCollection getLegendItems ()
	{
		return null;
	}

	/**
	 * Returns the vertical value axis.
	 * This is required by the VerticalValuePlot interface, but not used in this class.
	 *
	 * @return The vertical value axis.
	 */
	public ValueAxis getVerticalValueAxis ()
	{
		return this.rangeAxis;
	}

	/**
	 * Multiplies the range on the horizontal axis/axes by the specified factor.
	 *
	 * @param factor The zoom factor.
	 */
	public void zoomHorizontalAxes (double factor)
	{
	}

	/**
	 * Multiplies the range on the vertical axis/axes by the specified factor.
	 *
	 * @param factor The zoom factor.
	 */
	public void zoomVerticalAxes (double factor)
	{
	}

	/**
	 * Zooms the horizontal axes.
	 *
	 * @param lowerPercent The lower percent.
	 * @param upperPercent The upper percent.
	 */
	public void zoomHorizontalAxes (double lowerPercent, double upperPercent)
	{
	}

	/**
	 * Zooms the vertical axes.
	 *
	 * @param lowerPercent The lower percent.
	 * @param upperPercent The upper percent.
	 */
	public void zoomVerticalAxes (double lowerPercent, double upperPercent)
	{
	}

	/**
	 * Get the maximum marker value.
	 *
	 * @return The maximum marker value.
	 */
	public double getMaxMarkerValue ()
	{
		return maxMarkerValue;
	}

	/**
	 * Set the maximum marker value.
	 *
	 * @param maxMarkerValue The new maximum marker value.
	 */
	public void setMaxMarkerValue (double maxMarkerValue)
	{
		this.maxMarkerValue = maxMarkerValue;
	}

	/**
	 * Show the maximum marker.
	 */
	public boolean showMaxMarker ()
	{
		return maxMarkerShow;
	}

	/**
	 * Determine wether the maximum marker should be displayed.
	 *
	 * @param show If true the maximum marker is drawn.
	 */
	public void setShowMaxMarker (boolean show)
	{
		maxMarkerShow = show;
	}

	/**
	 * Get the paint for the maximum marker.
	 *
	 * @return The maximum marker paint.
	 */
	public Paint getMaxMarkerPaint ()
	{
		return maxMarkerPaint;
	}

	/**
	 * Set the paint for the maximum marker.
	 *
	 * @param paint The new maximum marker paint.
	 */
	public void setMaxMarkerPaint (Paint paint)
	{
		maxMarkerPaint = paint;
	}

	/**
	 * Get the minimum marker value.
	 *
	 * @return The minimum marker value.
	 */
	public double getMinMarkerValue ()
	{
		return minMarkerValue;
	}

	/**
	 * Set the minimum marker value.
	 *
	 * @param minMarkerValue The new minimum marker value.
	 */
	public void setMinMarkerValue (double minMarkerValue)
	{
		this.minMarkerValue = minMarkerValue;
	}

	/**
	 * Show the minimum marker.
	 */
	public boolean showMinMarker ()
	{
		return minMarkerShow;
	}

	/**
	 * Determine wether the minimum marker should be displayed.
	 *
	 * @param show If true the minimum marker is drawn.
	 */
	public void setShowMinMarker (boolean show)
	{
		minMarkerShow = show;
	}

	/**
	 * Get the paint for the minimum marker.
	 *
	 * @return The minimum marker paint.
	 */
	public Paint getMinMarkerPaint ()
	{
		return minMarkerPaint;
	}

	/**
	 * Set the paint for the minimum marker.
	 *
	 * @param paint The new minimum marker paint.
	 */
	public void setMinMarkerPaint (Paint paint)
	{
		minMarkerPaint = paint;
	}

	/**
	 * Check wether the instrument should use the full window size or not
	 *
	 * @return True if instrument should be able to use full window size.
	 */
	public boolean isFitToWindow ()
	{
		return fitToWindow;
	}

	/**
	 * Set state of the instrument window
	 *
	 * @param fitToWindow True if the Window size is full useable for instrument painting.
	 */
	public void setFitToWindow (boolean fitToWindow)
	{
		this.fitToWindow = fitToWindow;
	}

	/**
	 * Get the paint for the bar.
	 *
	 * @return The bar paint.
	 */
	public Paint getBarPaint ()
	{
		return barPaint;
	}

	/**
	 * Set the paint for the bar.
	 *
	 * @param paint The new bar paint.
	 */
	public void setBarPaint (Paint paint)
	{
		barPaint = paint;
	}

	/**
	 * Draws the plot on a Java 2D graphics device (such as the screen or a printer).
	 *
	 * @param g2  the graphics device.
	 * @param plotArea  the area within which the plot should be drawn.
	 * @param parentState  the state from the parent plot, if there is one.
	 * @param info  collects info about the drawing.
	 */
	public void draw (
		Graphics2D g2, Rectangle2D plotArea, PlotState parentState, PlotRenderingInfo info)
	{
		FontMetrics fm = g2.getFontMetrics ();
		Rectangle2D labelBounds = getTextBounds ("M", g2, fm);

		Area tempArea = null;

		if (info != null)
		{
			info.setPlotArea (plotArea);
		}

		if (getRangeAxis () == null)
		{
			return;
		}

		int midX;
		double barWidth;

		if (this.isFitToWindow ())
		{
			midX = (int) (plotArea.getX () + 100) + (int) labelBounds.getHeight ();
			barWidth = plotArea.getWidth () - 150;
		}
		else
		{
			midX =
				(int) (plotArea.getX () + (plotArea.getWidth () / 2)) +
				(int) labelBounds.getHeight ();
			barWidth = Math.min (3 * plotArea.getWidth () / 4, 50);
		}

		int midY = (int) (plotArea.getY () + (plotArea.getHeight () / 2));
		int stemTop = (int) (plotArea.getMinY ());
		int stemBottom = (int) (plotArea.getMaxY ());
		int rangeStartY = stemTop + 10;
		int rangeHeight = (stemBottom - stemTop - 19);
		Rectangle2D dataArea =
			new Rectangle2D.Double(
				plotArea.getX (), plotArea.getY (), plotArea.getWidth (), plotArea.getHeight ());

		double cursor = AXIS_GAP + GAP;
		Rectangle2D drawArea =
			new Rectangle2D.Double(midX - (cursor / 2), rangeStartY, barWidth, rangeHeight);
		Rectangle2D outerBounds =
			new Rectangle2D.Double(
				midX - (cursor / 2) - 1, rangeStartY - 1, barWidth + 1, rangeHeight + 1);

		drawBackground (g2, drawArea);

		g2.setPaint (Color.black);
		g2.draw (outerBounds);

		NumberAxis axis = new NumberAxis(null);

		axis.setAxisLineVisible (false);
		getRangeAxis ().draw (
			g2, midX - (cursor / 2) - 1, plotArea, drawArea, RectangleEdge.LEFT, null);

		if ((getDataset () != null) && (getDataset ().getValue () != null))
		{
			double current = getDataset ().getValue ().doubleValue ();

			current = Math.max (current, lowerBound);
			current = Math.min (current, upperBound);

			double ds = getRangeAxis ().valueToJava2D (current, drawArea, RectangleEdge.LEFT);

			Rectangle2D barRect = new Rectangle2D.Float();

			barRect.setRect (midX - (cursor / 2), ds, barWidth, rangeStartY + rangeHeight - ds);

			g2.setPaint (getBarPaint ());
			g2.fill (barRect);
		}

		if (showMaxMarker ())
		{
			g2.setPaint (getMaxMarkerPaint ());

			double ds =
				getRangeAxis ().valueToJava2D (getMaxMarkerValue (), drawArea, RectangleEdge.LEFT);
			double x = midX - (cursor / 2);
			double y = ds;
			Rectangle2D rect = new Rectangle2D.Float();

			rect.setRect (x, y, barWidth, 1);
			g2.fill (rect);
		}

		if (showMinMarker ())
		{
			g2.setPaint (getMinMarkerPaint ());

			double ds =
				getRangeAxis ().valueToJava2D (getMinMarkerValue (), drawArea, RectangleEdge.LEFT);
			double x = midX - (cursor / 2);
			double y = ds;
			Rectangle2D rect = new Rectangle2D.Float();

			rect.setRect (x, y, barWidth, 1);
			g2.fill (rect);
		}
	}

	/**
	 * Returns the bounds for the specified text.
	 *
	 * @param text  the text.
	 * @param g2  the graphics context.
	 * @param fm  the font metrics.
	 *
	 * @return The text bounds.
	 */
	public static Rectangle2D getTextBounds (String text, Graphics2D g2, FontMetrics fm)
	{
		Rectangle2D bounds = null;
		double width = fm.stringWidth (text);
		double height = fm.getHeight ();

		bounds = new Rectangle2D.Double(0.0, -fm.getAscent (), width, height);

		return bounds;
	}
}