/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.roundinstrument.plot;


import de.iritgo.openmetix.roundinstrument.RoundInstrument;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.data.DatasetChangeEvent;
import org.jfree.data.Range;
import org.jfree.data.ValueDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.util.ObjectUtils;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;


/**
* A plot that displays a single value in the context of several ranges ('normal', 'warning'
* and 'critical').
*
* @version $Id: NewMeterPlot.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
*/
public class NewMeterPlot
	extends Plot
	implements Serializable, Cloneable
{
	/** A constant representing the 'normal' level. */
	public static final int NORMAL_DATA = 0;

	/** A constant representing the 'warning' level. */
	public static final int WARNING_DATA = 1;

	/** A constant representing the 'critical' level. */
	public static final int CRITICAL_DATA = 2;

	/** A constant representing the full data range. */
	public static final int FULL_DATA = 3;

	/** The default text for the normal level. */
	public static final String NORMAL_TEXT = "Normal";

	/** The default text for the warning level. */
	public static final String WARNING_TEXT = "Warning";

	/** The default text for the critical level. */
	public static final String CRITICAL_TEXT = "Critical";

	/** The default 'normal' level color. */
	static final Paint DEFAULT_NORMAL_PAINT = Color.green;

	/** The default 'warning' level color. */
	static final Paint DEFAULT_WARNING_PAINT = Color.yellow;

	/** The default 'critical' level color. */
	static final Paint DEFAULT_CRITICAL_PAINT = Color.red;

	/** The default background paint. */
	static final Paint DEFAULT_DIAL_BACKGROUND_PAINT = Color.white;

	/** The default needle paint. */
	static final Paint DEFAULT_NEEDLE_PAINT = Color.blue;

	/** The default value font. */
	static final Font DEFAULT_VALUE_FONT = new Font("Verdana", Font.BOLD, 10);

	/** The default value paint. */
	static final Paint DEFAULT_VALUE_PAINT = Color.black;

	/** The default meter angle. */
	public static final int DEFAULT_METER_ANGLE = 270;

	/** The default border size. */
	public static final float DEFAULT_BORDER_SIZE = 3f;

	/** The default circle size. */
	public static final float DEFAULT_CIRCLE_SIZE = 10f;

	/** The default background color. */
	public static final Paint DEFAULT_BACKGROUND_PAINT = Color.lightGray;

	/** The default label font. */
	public static final Font DEFAULT_LABEL_FONT = new Font("Verdana", Font.BOLD, 10);

	/** Constant for the label type. */
	public static final int NO_LABELS = 0;

	/** Constant for the label type. */
	public static final int VALUE_LABELS = 1;

	/** The resourceBundle for the localization. */
	protected static ResourceBundle localizationResources =
		ResourceBundle.getBundle ("org.jfree.chart.plot.LocalizationBundle");

	/** Constant for meter type 'pie'. */
	public static final int DIALTYPE_PIE = 0;

	/** Constant for meter type 'circle'. */
	public static final int DIALTYPE_CIRCLE = 1;

	/** Constant for meter type 'chord'. */
	public static final int DIALTYPE_CHORD = 2;

	/** Constant for the label type. */
	public int DrawqTypeVALUE_LABELS = 1;

	/** The dataset. */
	private ValueDataset dataset;

	/** The units displayed on the dial. */
	private String units;

	/** The position if the digitals. */
	private String digitalPos;

	/** The overall range. */
	private Range range;

	/** The normal range. */
	private Range normalRange;

	/** The warning range. */
	private Range warningRange;

	/** The critical range. */
	private Range criticalRange;

	/** The outline paint. */
	private Paint dialOutlinePaint;

	/** The 'normal' level color. */
	private transient Paint normalPaint = DEFAULT_NORMAL_PAINT;

	/** The 'warning' level color. */
	private transient Paint warningPaint = DEFAULT_WARNING_PAINT;

	/** The 'critical' level color. */
	private transient Paint criticalPaint = DEFAULT_CRITICAL_PAINT;

	/** The dial shape (background shape). */
	private DialShape shape = DialShape.CIRCLE;

	/** The paint for the dial background. */
	private transient Paint dialBackgroundPaint;

	/** The paint for the needle. */
	private transient Paint needlePaint;

	/** The font for the value displayed in the center of the dial. */
	private Font valueFont;

	/** The paint for the value displayed in the center of the dial. */
	private transient Paint valuePaint;

	/** The tick label type (NO_LABELS, VALUE_LABELS). */
	private int tickLabelType;

	/** The tick label font. */
	private Font tickLabelFont;

	/** A flag that controls whether or not the border is drawn. */
	private boolean drawBorder;

	/** A flag that controls whether or not the digit is drawn. */
	private boolean drawDigital;

	/** If true the instrument use full window size for painting. */
	private boolean fitToWindow = true;

	/** Division size. */
	private double division;

	/** Division type. */
	private int divisionType;

	/** ??? */
	private int meterCalcAngle = -1;

	/** ??? */
	private double meterRange = -1;

	/** The dial extent. */
	private int meterAngle = DEFAULT_METER_ANGLE;

	/** The dial tip. */
	private int meterTipAngle = 90;

	/** The minimum meter value. */
	private double minMeterValue = 0.0;

	/**
	 * Default constructor.
	 *
	 * @param dataset  the dataset.
	 */
	public NewMeterPlot (ValueDataset dataset)
	{
		super();

		division = 10;
		divisionType = RoundInstrument.DIVISION_NUM;
		drawDigital = true;
		units = "";
		digitalPos = "Down";
		range = new Range(0.0, 100.0);
		normalRange = new Range(0.0, 60.0);
		warningRange = new Range(60.0, 90.0);
		criticalRange = new Range(90.0, 100.0);
		tickLabelType = NewMeterPlot.VALUE_LABELS;
		tickLabelFont = NewMeterPlot.DEFAULT_LABEL_FONT;

		dialBackgroundPaint = NewMeterPlot.DEFAULT_DIAL_BACKGROUND_PAINT;
		needlePaint = NewMeterPlot.DEFAULT_NEEDLE_PAINT;
		valueFont = NewMeterPlot.DEFAULT_VALUE_FONT;
		valuePaint = NewMeterPlot.DEFAULT_VALUE_PAINT;

		setDataset (dataset);
	}

	/**
	 * Returns the units for the dial.
	 *
	 * @return The units.
	 */
	public String getUnits ()
	{
		return this.units;
	}

	/**
	 * Sets the units for the dial.
	 *
	 * @param units  the units.
	 */
	public void setUnits (String units)
	{
		this.units = units;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the digital position for the dial.
	 *
	 * @return The digital position.
	 */
	public String getDigitalPos ()
	{
		return this.digitalPos;
	}

	/**
	 * Sets the digital position for the dial.
	 *
	 * @param digitalPos digital position
	 */
	public void setDigitalPos (String digitalPos)
	{
		this.digitalPos = digitalPos;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the number of ticks for the axis
	 */
	public int getNumberOfTicks ()
	{
		return 10;
	}

	/**
	 * Get the division interval.
	 *
	 * @return The division interval.
	 */
	public double getDivisionInterval ()
	{
		if (divisionType == RoundInstrument.DIVISION_NUM)
		{
			return ((double) range.getUpperBound () - (double) range.getLowerBound ()) / division;
		}
		else
		{
			return division;
		}
	}

	/**
	 * Sets the division size.
	 *
	 * @param division The new division size.
	 */
	public void setDivision (double division)
	{
		this.division = division;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Sets the division type.
	 *
	 * @param divisionType The new division type.
	 */
	public void setDivisionType (int divisionType)
	{
		this.divisionType = divisionType;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the overall range for the dial.
	 *
	 * @return The overall range.
	 */
	public Range getRange ()
	{
		return this.range;
	}

	/**
	 * Sets the overall range for the dial.
	 *
	 * @param range  the range.
	 */
	public void setRange (Range range)
	{
		this.range = range;
	}

	/**
	 * Returns the normal range for the dial.
	 *
	 * @return The normal range.
	 */
	public Range getNormalRange ()
	{
		return this.normalRange;
	}

	/**
	 * Sets the normal range for the dial.
	 *
	 * @param range  the range.
	 */
	public void setNormalRange (Range range)
	{
		this.normalRange = range;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the warning range for the dial.
	 *
	 * @return The warning range.
	 */
	public Range getWarningRange ()
	{
		return this.warningRange;
	}

	/**
	 * Sets the warning range for the dial.
	 *
	 * @param range  the range.
	 */
	public void setWarningRange (Range range)
	{
		this.warningRange = range;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the critical range for the dial.
	 *
	 * @return The critical range.
	 */
	public Range getCriticalRange ()
	{
		return this.criticalRange;
	}

	/**
	 * Sets the critical range for the dial.
	 *
	 * @param range  the range.
	 */
	public void setCriticalRange (Range range)
	{
		this.criticalRange = range;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the dial shape.
	 *
	 * @return The dial shape.
	 */
	public DialShape getDialShape ()
	{
		return this.shape;
	}

	/**
	 * Sets the dial shape.
	 *
	 * @param shape  the shape.
	 */
	public void setDialShape (DialShape shape)
	{
		this.shape = shape;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the paint for the dial background.
	 *
	 * @return The paint (possibly <code>null</code>).
	 */
	public Paint getDialBackgroundPaint ()
	{
		return this.dialBackgroundPaint;
	}

	/**
	 * Sets the paint used to fill the dial background.
	 *
	 * @param paint  the paint (<code>null</code> permitted).
	 */
	public void setDialBackgroundPaint (Paint paint)
	{
		this.dialBackgroundPaint = paint;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the paint for the needle.
	 *
	 * @return The paint.
	 */
	public Paint getNeedlePaint ()
	{
		return this.needlePaint;
	}

	/**
	 * Sets the paint used to display the needle.
	 * <P>
	 * If you set this to null, it will revert to the default color.
	 *
	 * @param paint The paint.
	 */
	public void setNeedlePaint (Paint paint)
	{
		this.needlePaint = (paint == null) ? DEFAULT_NEEDLE_PAINT : paint;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the font for the value label.
	 *
	 * @return The font.
	 */
	public Font getValueFont ()
	{
		return this.valueFont;
	}

	/**
	 * Sets the font used to display the value label.
	 * <P>
	 * If you set this to null, it will revert to the default font.
	 *
	 * @param font The font.
	 */
	public void setValueFont (Font font)
	{
		this.valueFont = (font == null) ? DEFAULT_VALUE_FONT : font;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the paint for the value label.
	 *
	 * @return The paint.
	 */
	public Paint getValuePaint ()
	{
		return this.valuePaint;
	}

	/**
	 * Sets the paint used to display the value label.
	 * <P>
	 * If you set this to null, it will revert to the default paint.
	 *
	 * @param paint The paint.
	 */
	public void setValuePaint (Paint paint)
	{
		this.valuePaint = (paint == null) ? DEFAULT_VALUE_PAINT : paint;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the paint for the 'normal' level.
	 *
	 * @return The paint.
	 */
	public Paint getNormalPaint ()
	{
		return this.normalPaint;
	}

	/**
	 * Sets the paint used to display the 'normal' range.
	 * <P>
	 * If you set this to null, it will revert to the default color.
	 *
	 * @param paint The paint.
	 */
	public void setNormalPaint (Paint paint)
	{
		this.normalPaint = (paint == null) ? DEFAULT_NORMAL_PAINT : paint;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the paint used to display the 'warning' range.
	 *
	 * @return The paint.
	 */
	public Paint getWarningPaint ()
	{
		return this.warningPaint;
	}

	/**
	 * Sets the paint used to display the 'warning' range.
	 * <P>
	 * If you set this to null, it will revert to the default color.
	 *
	 * @param paint The paint.
	 */
	public void setWarningPaint (Paint paint)
	{
		this.warningPaint = (paint == null) ? DEFAULT_WARNING_PAINT : paint;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the paint used to display the 'critical' range.
	 *
	 * @return The paint.
	 */
	public Paint getCriticalPaint ()
	{
		return this.criticalPaint;
	}

	/**
	 * Sets the paint used to display the 'critical' range.
	 * <P>
	 * If you set this to null, it will revert to the default color.
	 *
	 * @param paint The paint.
	 */
	public void setCriticalPaint (Paint paint)
	{
		this.criticalPaint = (paint == null) ? DEFAULT_CRITICAL_PAINT : paint;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the tick label type.  Defined by the constants: NO_LABELS,
	 * VALUE_LABELS.
	 *
	 * @return The tick label type.
	 */
	public int getTickLabelType ()
	{
		return this.tickLabelType;
	}

	/**
	 * Sets the tick label type.
	 *
	 * @param type  the type of tick labels - either <code>NO_LABELS</code> or
	 *      <code>VALUE_LABELS</code>
	 */
	public void setTickLabelType (int type)
	{
		if ((type != NO_LABELS) && (type != VALUE_LABELS))
		{
			throw new IllegalArgumentException(
				"NewMeterPlot.setLabelType(int): unrecognised type.");
		}

		if (this.tickLabelType != type)
		{
			this.tickLabelType = type;
			notifyListeners (new PlotChangeEvent(this));
		}
	}

	/**
	 * Returns the tick label font.
	 *
	 * @return The tick label font.
	 */
	public Font getTickLabelFont ()
	{
		return this.tickLabelFont;
	}

	/**
	 * Sets the tick label font and notifies registered listeners that the plot has been changed.
	 *
	 * @param font  The new tick label font.
	 */
	public void setTickLabelFont (Font font)
	{
		if (font == null)
		{
			throw new IllegalArgumentException(
				"NewMeterPlot.setTickLabelFont(...): null font not allowed.");
		}

		if (! this.tickLabelFont.equals (font))
		{
			this.tickLabelFont = font;
			notifyListeners (new PlotChangeEvent(this));
		}
	}

	/**
	 * Returns a flag that controls whether or not a rectangular border is drawn around the plot
	 * area.
	 *
	 * @return A flag.
	 */
	public boolean getDrawBorder ()
	{
		return this.drawBorder;
	}

	/**
	 * Sets the flag that controls whether or not a rectangular border is drawn around the plot
	 * area.
	 * <P>
	 * Note:  it looks like the true setting needs some work to provide some insets.
	 *
	 * @param draw  the flag.
	 */
	public void setDrawBorder (boolean draw)
	{
		this.drawBorder = draw;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns a flag that controls whether or not the digits are drawn
	 *
	 * @return A flag.
	 */
	public boolean getDrawDigital ()
	{
		return this.drawDigital;
	}

	/**
	 * Sets the flag that controls whether or not the digits are drawn.
	 *
	 * @param draw  the flag.
	 */
	public void setDrawDigital (boolean draw)
	{
		this.drawDigital = draw;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the meter tip angle.
	 *
	 * @return the meter tip angle.
	 */
	public int getMeterTipAngle ()
	{
		return this.meterTipAngle;
	}

	/**
	 * Sets the range through which the dial's needle is free to rotate.
	 *
	 * @param meterTipAngle The angle.
	 */
	public void setMeterTipAngle (int meterTipAngle)
	{
		this.meterTipAngle = meterTipAngle;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the meter angle.
	 *
	 * @return the meter angle.
	 */
	public int getMeterAngle ()
	{
		return this.meterAngle;
	}

	/**
	 * Sets the range through which the dial's needle is free to rotate.
	 *
	 * @param angle  the angle.
	 */
	public void setMeterAngle (int angle)
	{
		this.meterAngle = angle;
		notifyListeners (new PlotChangeEvent(this));
	}

	/**
	 * Returns the dial outline paint.
	 *
	 * @return The paint.
	 */
	public Paint getDialOutlinePaint ()
	{
		return this.dialOutlinePaint;
	}

	/**
	 * Sets the dial outline paint.
	 *
	 * @param paint  the paint.
	 */
	public void setDialOutlinePaint (Paint paint)
	{
		this.dialOutlinePaint = paint;
	}

	/**
	 * Returns the primary dataset for the plot.
	 *
	 * @return The primary dataset (possibly <code>null</code>).
	 */
	public ValueDataset getDataset ()
	{
		return this.dataset;
	}

	/**
	 * Sets the dataset for the plot, replacing the existing dataset if there is one.
	 *
	 * @param dataset  the dataset (<code>null</code> permitted).
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
	 * Returns a list of legend item labels.
	 *
	 * @return the legend item labels.
	 *
	 * @deprecated use getLegendItems().
	 */
	public List getLegendItemLabels ()
	{
		return null;
	}

	/**
	 * Returns null.
	 *
	 * @return null.
	 */
	public LegendItemCollection getLegendItems ()
	{
		return null;
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
		double meterX = plotArea.getX ();
		double meterY = plotArea.getY ();
		double meterW = plotArea.getWidth ();
		double meterH = plotArea.getHeight ();

		if (info != null)
		{
			info.setPlotArea (plotArea);
		}

		if (digitalPos == "Down")
		{
			if (this.isFitToWindow ())
			{
			}
			else
			{
				meterW = Math.min (meterW, meterH);
				meterH = meterW;
				meterX += (plotArea.getWidth () - meterW) / 2;
				meterY += (plotArea.getHeight () - meterH) / 2;
			}
		}
		else if (digitalPos == "Left")
		{
			if (this.isFitToWindow ())
			{
				meterW = plotArea.getWidth () * 2 - 50;
			}
			else
			{
				if (plotArea.getWidth () >= plotArea.getHeight () / 2 + 24)
				{
					meterW = plotArea.getHeight ();
				}
				else
				{
					meterW = 2 * plotArea.getWidth () - 48;
					meterH = meterW;
				}

				meterX += (plotArea.getWidth () - meterW / 2 - 24) / 2;
				meterY += (plotArea.getHeight () - meterH) / 2;
			}
		}
		else if (digitalPos == "Right")
		{
			if (this.isFitToWindow ())
			{
				meterW = plotArea.getWidth () * 2 - 50;
				meterX -= plotArea.getWidth () - 48;
			}
			else
			{
				if (plotArea.getWidth () >= plotArea.getHeight () / 2 + 24)
				{
					meterW = plotArea.getHeight ();
				}
				else
				{
					meterW = 2 * plotArea.getWidth () - 48;
					meterH = meterW;
				}

				meterX += (plotArea.getWidth () - meterW / 2 - 24) / 2 - meterW / 2 + 24;
				meterY += (plotArea.getHeight () - meterH) / 2;
			}
		}
		else if (digitalPos == "Up")
		{
			if (this.isFitToWindow ())
			{
				meterH = plotArea.getHeight () * 2 - 50;
			}
			else
			{
				if (plotArea.getHeight () >= plotArea.getWidth () / 2 + 24)
				{
					meterH = plotArea.getWidth ();
				}
				else
				{
					meterH = 2 * plotArea.getHeight () - 48;
					meterW = meterH;
				}

				meterX += (plotArea.getWidth () - meterW) / 2;
				meterY += (plotArea.getHeight () - 24 - meterH / 2) / 2;
			}
		}

		if (this.drawBorder)
		{
			drawBackground (g2, plotArea);
		}

		Rectangle2D meterArea =
			new Rectangle2D.Double(meterX + 2, meterY + 2, meterW - 4, meterH - 4);

		Rectangle2D.Double originalArea =
			new Rectangle2D.Double(
				meterArea.getX () - 1, meterArea.getY () - 1, meterArea.getWidth () + 1,
				meterArea.getHeight () + 1);

		double meterMiddleX = meterArea.getCenterX ();
		double meterMiddleY = meterArea.getCenterY ();

		Shape shapeborder =
			new Rectangle2D.Double((meterMiddleX - (meterW / 2)), meterMiddleY, meterW, 25);
		Shape shape =
			new Rectangle2D.Double((meterMiddleX - (meterW / 2)) + 1, meterMiddleY, meterW - 2, 24);

		if (this.digitalPos == "Left")
		{
			shapeborder =
				new Rectangle2D.Double(meterMiddleX, (meterMiddleY - (meterH / 2)), 25, meterH);
			shape =
				new Rectangle2D.Double(
					meterMiddleX, (meterMiddleY - (meterH / 2)) + 1, 24, meterH - 2);
		}

		if (this.digitalPos == "Right")
		{
			shapeborder =
				new Rectangle2D.Double(
					meterMiddleX - 25, (meterMiddleY - (meterH / 2)), 25, meterH);
			shape =
				new Rectangle2D.Double(
					meterMiddleX - 24, (meterMiddleY - (meterH / 2)) + 1, 24, meterH - 2);
		}

		if (this.digitalPos != "Down")
		{
			g2.setPaint (Color.black);
			g2.draw (shapeborder);

			g2.setPaint (Color.white);
			g2.draw (shape);
			g2.fill (shape);
		}

		ValueDataset data = getDataset ();

		if (data != null)
		{
			double dataMin = this.range.getLowerBound ();
			double dataMax = this.range.getUpperBound ();

			this.minMeterValue = dataMin;

			this.meterCalcAngle = 180 + ((this.meterAngle - 180) / 2);
			this.meterRange = dataMax - dataMin;

			Shape savedClip = g2.getClip ();

			g2.clip (originalArea);

			Composite originalComposite = g2.getComposite ();

			g2.setComposite (
				AlphaComposite.getInstance (AlphaComposite.SRC_OVER, getForegroundAlpha ()));

			if (this.dialBackgroundPaint != null)
			{
				drawArc (g2, meterArea, dataMin, dataMax, this.dialBackgroundPaint, 1);
			}

			drawScaleTicks (g2, meterArea, dataMin, dataMax);
			drawTicks (g2, meterArea, dataMin, dataMax);
			drawArcFor (g2, meterArea, data, FULL_DATA);

			double minValue = this.range.getLowerBound ();
			double maxValue = this.range.getUpperBound ();
			double diff = getDivisionInterval ();

			for (double i = minValue; i <= maxValue; i += diff)
			{
				drawTick (g2, meterArea, i, true, Color.black);
			}

			if (this.normalRange != null)
			{
				drawTick (
					g2, meterArea, this.normalRange.getLowerBound (), false, getNormalPaint ());
			}

			if (this.warningRange != null)
			{
				drawArcFor (g2, meterArea, data, WARNING_DATA);
			}

			if (this.criticalRange != null)
			{
				drawTick (
					g2, meterArea, this.criticalRange.getLowerBound (), false, getCriticalPaint ());
			}

			if (data.getValue () != null)
			{
				if (this.drawDigital)
				{
					String u = " " + getUnits ();
					double x = 0;
					double y = 0;

					if (this.digitalPos == "Down")
					{
						x = meterArea.getCenterX () - 20;
						y = meterArea.getCenterY () + 20;
					}

					if (this.digitalPos == "Up")
					{
						x = meterArea.getCenterX () - 20;
						y = meterArea.getCenterY () - 40;
					}

					if (this.digitalPos == "Left")
					{
						x = meterArea.getCenterX () - 75;
						y = meterArea.getCenterY () - 8;
					}

					if (this.digitalPos == "Right")
					{
						x = meterArea.getCenterX () + 35;
						y = meterArea.getCenterY () - 8;
					}

					DecimalFormat df = new DecimalFormat("#,###,###,##0.00");

					g2.setStroke (new BasicStroke(2.0f));
					g2.setPaint (Color.black);
					g2.setFont (getValueFont ());
					g2.drawString (
						df.format (data.getValue ().doubleValue ()) + u, (float) x, (float) y);

					if (this.normalRange != null)
					{
						y += 14;
						g2.setPaint (Color.green);
						g2.drawString (
							df.format (this.normalRange.getLowerBound ()) + u, (float) x, (float) y);
					}

					if (this.criticalRange != null)
					{
						y += 14;
						g2.setPaint (Color.red);
						g2.drawString (
							df.format (this.criticalRange.getLowerBound ()) + u, (float) x,
							(float) y);
					}
				}

				g2.setPaint (this.needlePaint);
				g2.setStroke (new BasicStroke(2.0f));

				double radius = (meterArea.getWidth () / 2) + DEFAULT_BORDER_SIZE + 15;
				double valueAngle = calculateAngle (data.getValue ().doubleValue ());
				double valueP1 = meterMiddleX + (radius * Math.cos (Math.PI * (valueAngle / 180)));
				double valueP2 = meterMiddleY - (radius * Math.sin (Math.PI * (valueAngle / 180)));

				Polygon arrow = new Polygon();

				if (
					((valueAngle > 135) && (valueAngle < 225)) ||
					((valueAngle < 45) && (valueAngle > -45)))
				{
					double valueP3 = (meterMiddleY - (DEFAULT_CIRCLE_SIZE / 4));
					double valueP4 = (meterMiddleY + (DEFAULT_CIRCLE_SIZE / 4));

					arrow.addPoint ((int) meterMiddleX, (int) valueP3);
					arrow.addPoint ((int) meterMiddleX, (int) valueP4);
				}
				else
				{
					arrow.addPoint (
						(int) (meterMiddleX - (DEFAULT_CIRCLE_SIZE / 4)), (int) meterMiddleY);
					arrow.addPoint (
						(int) (meterMiddleX + (DEFAULT_CIRCLE_SIZE / 4)), (int) meterMiddleY);
				}

				arrow.addPoint ((int) valueP1, (int) valueP2);

				Ellipse2D circle =
					new Ellipse2D.Double(
						meterMiddleX - (DEFAULT_CIRCLE_SIZE / 2),
						meterMiddleY - (DEFAULT_CIRCLE_SIZE / 2), DEFAULT_CIRCLE_SIZE,
						DEFAULT_CIRCLE_SIZE);

				g2.fill (arrow);
				g2.fill (circle);
			}

			if (dataMin <= 0 && dataMax >= 0)
			{
				drawZeroTick (g2, meterArea, dataMin, dataMax);
			}

			g2.clip (savedClip);
			g2.setComposite (originalComposite);
		}

		if (this.drawBorder)
		{
			drawOutline (g2, plotArea);
		}
	}

	/**
	 * Draws a colored range (arc) for one level.
	 *
	 * @param g2 The graphics device.
	 * @param meterArea The drawing area.
	 * @param data The dataset.
	 * @param type The level.
	 */
	protected void drawArcFor (Graphics2D g2, Rectangle2D meterArea, ValueDataset data, int type)
	{
		double minValue = 0.0;
		double maxValue = 0.0;
		Paint paint = null;

		switch (type)
		{
			case NORMAL_DATA:
				minValue = this.normalRange.getLowerBound ();
				maxValue = this.normalRange.getUpperBound ();
				paint = getNormalPaint ();

				break;

			case WARNING_DATA:
				minValue = this.warningRange.getLowerBound ();
				maxValue = this.warningRange.getUpperBound ();
				paint = getWarningPaint ();

				break;

			case CRITICAL_DATA:
				minValue = this.criticalRange.getLowerBound ();
				maxValue = this.criticalRange.getUpperBound ();
				paint = getCriticalPaint ();

				break;

			case FULL_DATA:
				minValue = this.range.getLowerBound ();
				maxValue = this.range.getUpperBound ();
				paint = Color.black;

				break;

			default:
				return;
		}

		drawArc (g2, meterArea, minValue, maxValue, paint);
	}

	/**
	 * Draws an arc.
	 *
	 * @param g2  the graphics device.
	 * @param area  the plot area.
	 * @param minValue  the minimum value.
	 * @param maxValue  the maximum value.
	 * @param paint  the paint.
	 */
	protected void drawArc (
		Graphics2D g2, Rectangle2D area, double minValue, double maxValue, Paint paint)
	{
		drawArc (g2, area, minValue, maxValue, paint, 0);
	}

	/**
	 * Draws an arc.
	 *
	 * @param g2  the graphics device.
	 * @param area  the plot area.
	 * @param minValue  the minimum value.
	 * @param maxValue  the maximum value.
	 * @param paint  the paint.
	 * @param outlineType  the outline type.
	 */
	protected void drawArc (
		Graphics2D g2, Rectangle2D area, double minValue, double maxValue, Paint paint,
		int outlineType)
	{
		double startAngle = calculateAngle (maxValue);
		double endAngle = calculateAngle (minValue);
		double extent = endAngle - startAngle;

		double x = area.getX ();
		double y = area.getY ();
		double w = area.getWidth ();
		double h = area.getHeight ();

		g2.setPaint (paint);

		if (outlineType > 0)
		{
			g2.setStroke (new BasicStroke(10.0f));
		}
		else
		{
			g2.setStroke (new BasicStroke(1.0f));
		}

		int joinType = Arc2D.OPEN;

		if (outlineType > 0)
		{
			if (this.shape == DialShape.PIE)
			{
				joinType = Arc2D.PIE;
			}
			else if (this.shape == DialShape.CHORD)
			{
				if (this.meterAngle > 180)
				{
					joinType = Arc2D.CHORD;
				}
				else
				{
					joinType = Arc2D.PIE;
				}
			}
			else if (this.shape == DialShape.CIRCLE)
			{
				joinType = Arc2D.PIE;
				extent = 360;
			}
			else
			{
				throw new IllegalStateException(
					"NewMeterPlot.drawArc(...): " + "dialType not recognised.");
			}
		}

		Arc2D.Double arc = new Arc2D.Double(x, y, w, h, startAngle, extent, joinType);

		if (outlineType > 0)
		{
			g2.fill (arc);
		}
		else
		{
			g2.draw (arc);
		}
	}

	/**
	 * Calculate an angle.
	 *
	 * @param value  the value.
	 *
	 * @return the result.
	 */
	double calculateAngle (double value)
	{
		double dataVal = value;

		if (dataVal < this.range.getLowerBound ())
		{
			dataVal = this.range.getLowerBound ();
		}

		if (dataVal > this.range.getUpperBound ())
		{
			dataVal = this.range.getUpperBound ();
		}

		dataVal -= this.minMeterValue;

		double ret = this.meterCalcAngle - ((dataVal / this.meterRange) * this.meterAngle);

		return ret - this.meterTipAngle;
	}

	/**
	 * Draws the ticks.
	 *
	 * @param g2  the graphics device.
	 * @param meterArea  the meter area.
	 * @param minValue  the minimum value.
	 * @param maxValue  the maximum value.
	 */
	protected void drawTicks (
		Graphics2D g2, Rectangle2D meterArea, double minValue, double maxValue)
	{
	}

	/**
	 *  Draws 2 inner Scales.
	 */
	protected void drawZeroTick (
		Graphics2D g2, Rectangle2D meterArea, double minValue, double maxValue)
	{
		double valueAngle = calculateAngle (0);

		double meterMiddleX = meterArea.getCenterX ();
		double meterMiddleY = meterArea.getCenterY ();

		g2.setStroke (new BasicStroke(2.0f));

		double valueP2X = 0;
		double valueP2Y = 0;

		double radiusX = (meterArea.getWidth () / 2);
		double radiusY = (meterArea.getHeight () / 2);

		double radiusX2 = radiusX - 12;
		double radiusY2 = radiusY - 12;

		double valueP1X = meterMiddleX + (radiusX * Math.cos (Math.PI * (valueAngle / 180)));
		double valueP1Y = meterMiddleY - (radiusY * Math.sin (Math.PI * (valueAngle / 180)));

		valueP2X = meterMiddleX + (radiusX2 * Math.cos (Math.PI * (valueAngle / 180)));
		valueP2Y = meterMiddleY - (radiusY2 * Math.sin (Math.PI * (valueAngle / 180)));

		Line2D.Double line = new Line2D.Double(valueP1X, valueP1Y, valueP2X, valueP2Y);

		g2.setPaint (Color.blue);
		g2.draw (line);
	}

	/**
	 *  Draws 2 inner Scales.
	 */
	protected void drawScaleTicks (
		Graphics2D g2, Rectangle2D meterArea, double minValue, double maxValue)
	{
		double outerDiff = getDivisionInterval () / 2;

		for (double i = minValue; i <= maxValue; i += outerDiff)
		{
			double valueAngle = calculateAngle (i);

			double meterMiddleX = meterArea.getCenterX ();
			double meterMiddleY = meterArea.getCenterY ();

			g2.setStroke (new BasicStroke(1.0f));
			g2.setPaint (Color.DARK_GRAY);

			double valueP2X = 0;
			double valueP2Y = 0;

			double radiusX = (meterArea.getWidth () / 2);
			double radiusY = (meterArea.getHeight () / 2);

			double radiusX2 = radiusX - 5;
			double radiusY2 = radiusY - 5;

			double valueP1X = meterMiddleX + (radiusX * Math.cos (Math.PI * (valueAngle / 180)));
			double valueP1Y = meterMiddleY - (radiusY * Math.sin (Math.PI * (valueAngle / 180)));

			valueP2X = meterMiddleX + (radiusX2 * Math.cos (Math.PI * (valueAngle / 180)));
			valueP2Y = meterMiddleY - (radiusY2 * Math.sin (Math.PI * (valueAngle / 180)));

			Line2D.Double line = new Line2D.Double(valueP1X, valueP1Y, valueP2X, valueP2Y);

			g2.setPaint (Color.lightGray);
			g2.draw (line);
		}

		double innerDiff = getDivisionInterval () / 2;

		for (double i = minValue; i <= maxValue; i += innerDiff)
		{
			double valueAngle = calculateAngle (i);

			double meterMiddleX = meterArea.getCenterX ();
			double meterMiddleY = meterArea.getCenterY ();

			g2.setStroke (new BasicStroke(1.0f));
			g2.setPaint (Color.LIGHT_GRAY);

			double valueP2X = 0;
			double valueP2Y = 0;

			double radiusX = (meterArea.getWidth () / 4.0f);
			double radiusY = (meterArea.getHeight () / 4.0f);
			double radiusX2 = radiusX - 8;
			double radiusY2 = radiusY - 8;

			double valueP1X = meterMiddleX + (radiusX * Math.cos (Math.PI * (valueAngle / 180)));
			double valueP1Y = meterMiddleY - (radiusY * Math.sin (Math.PI * (valueAngle / 180)));

			valueP2X = meterMiddleX + (radiusX2 * Math.cos (Math.PI * (valueAngle / 180)));
			valueP2Y = meterMiddleY - (radiusY2 * Math.sin (Math.PI * (valueAngle / 180)));

			Line2D.Double line = new Line2D.Double(valueP1X, valueP1Y, valueP2X, valueP2Y);

			g2.setPaint (Color.lightGray);
			g2.draw (line);
		}
	}

	/**
	 * Draws a tick.
	 *
	 * @param g2  the graphics device.
	 * @param meterArea  the meter area.
	 * @param value  the value.
	 */
	protected void drawTick (Graphics2D g2, Rectangle2D meterArea, double value)
	{
		drawTick (g2, meterArea, value, false, null, false, null, false);
	}

	/**
	 * Draws a tick.
	 *
	 * @param g2  the graphics device.
	 * @param meterArea  the meter area.
	 * @param value  the value.
	 * @param label  the label.
	 * @param color  the color.
	 */
	protected void drawTick (
		Graphics2D g2, Rectangle2D meterArea, double value, boolean label, Paint color)
	{
		drawTick (g2, meterArea, value, label, color, false, null, false);
	}

	/**
	 * Draws a tick on the chart (also handles a special case [curValue=true] that draws the
	 * value in the middle of the dial).
	 *
	 * @param g2  the graphics device.
	 * @param meterArea  the meter area.
	 * @param value  the tick value.
	 * @param label  a flag that controls whether or not a value label is drawn.
	 * @param labelPaint  the label color.
	 * @param curValue  a flag for the special case of the current value.
	 * @param units  the unit-of-measure for the dial.
	 * @param fullPrecise Sollen die Nachkommastellen voll gezeigt werden?
	 */
	protected void drawTick (
		Graphics2D g2, Rectangle2D meterArea, double value, boolean label, Paint labelPaint,
		boolean curValue, String units, boolean fullPrecise)
	{
		FontMetrics fm = g2.getFontMetrics ();
		Rectangle2D tickLabelBounds = getTextBounds ("0", g2, fm);

		double valueAngle = calculateAngle (value);
		double meterMiddleX = meterArea.getCenterX ();
		double meterMiddleY = meterArea.getCenterY ();

		if (labelPaint == null)
		{
			labelPaint = Color.black;
		}

		g2.setPaint (labelPaint);
		g2.setStroke (new BasicStroke(2.0f));

		double radiusX = (meterArea.getWidth () / 2);
		double radiusY = (meterArea.getHeight () / 2);
		double radiusX2 = radiusX - tickLabelBounds.getHeight () / 2;
		double radiusY2 = radiusY - tickLabelBounds.getHeight () / 2;
		double radiusX3 = radiusX - 2 * tickLabelBounds.getHeight ();
		double radiusY3 = radiusY - 2 * tickLabelBounds.getHeight ();

		double valueP1X = meterMiddleX + (radiusX * Math.cos (Math.PI * (valueAngle / 180)));
		double valueP1Y = meterMiddleY - (radiusY * Math.sin (Math.PI * (valueAngle / 180)));

		double valueP2X = meterMiddleX + (radiusX2 * Math.cos (Math.PI * (valueAngle / 180)));
		double valueP2Y = meterMiddleY - (radiusY2 * Math.sin (Math.PI * (valueAngle / 180)));

		double valueP3X = meterMiddleX + (radiusX3 * Math.cos (Math.PI * (valueAngle / 180)));
		double valueP3Y = meterMiddleY - (radiusY3 * Math.sin (Math.PI * (valueAngle / 180)));

		Line2D.Double line = new Line2D.Double(valueP1X, valueP1Y, valueP2X, valueP2Y);

		g2.draw (line);

		if ((this.tickLabelType == VALUE_LABELS) && label)
		{
			DecimalFormat df;

			if (fullPrecise)
			{
				df = new DecimalFormat("#,###,###,##0.00");
			}
			else
			{
				df = new DecimalFormat("#,###,###,##0.0");
			}

			String tickLabel = df.format (value);

			if (curValue && units != null)
			{
				tickLabel += " " + units;
			}

			if (curValue)
			{
				g2.setFont (getValueFont ());
			}
			else
			{
				if (this.tickLabelFont != null)
				{
					g2.setFont (this.tickLabelFont);
				}
			}

			tickLabelBounds = getTextBounds (tickLabel, g2, fm);

			double x = valueP3X - tickLabelBounds.getWidth () / 2;
			double y = valueP3Y + tickLabelBounds.getHeight () / 2;

			g2.drawString (tickLabel, (float) x, (float) y);
		}
	}

	/**
	 * Returns a short string describing the type of plot.
	 *
	 * @return always <i>Meter Plot</i>.
	 */
	public String getPlotType ()
	{
		return localizationResources.getString ("Meter_Plot");
	}

	/**
	 * A zoom method that does nothing.  Plots are required to support the zoom operation.  In the
	 * case of a meter plot, it doesn't make sense to zoom in or out, so the method is empty.
	 *
	 * @param percent   The zoom percentage.
	 */
	public void zoom (double percent)
	{
	}

	/**
	 * Tests an object for equality with this plot.
	 *
	 * @param object  the object.
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

		if (object instanceof NewMeterPlot && super.equals (object))
		{
			NewMeterPlot p = (NewMeterPlot) object;

			boolean b0 = ObjectUtils.equal (this.units, p.units);
			boolean b1 = ObjectUtils.equal (this.range, p.range);
			boolean b2 = ObjectUtils.equal (this.normalRange, p.normalRange);
			boolean b3 = ObjectUtils.equal (this.warningRange, p.warningRange);
			boolean b4 = ObjectUtils.equal (this.criticalRange, p.criticalRange);
			boolean b5 = ObjectUtils.equal (this.dialOutlinePaint, p.dialOutlinePaint);
			boolean b6 = ObjectUtils.equal (this.normalPaint, p.normalPaint);
			boolean b7 = ObjectUtils.equal (this.warningPaint, p.warningPaint);
			boolean b8 = ObjectUtils.equal (this.criticalPaint, p.criticalPaint);
			boolean b9 = (this.shape == p.shape);
			boolean b10 = ObjectUtils.equal (this.dialBackgroundPaint, p.dialBackgroundPaint);
			boolean b11 = ObjectUtils.equal (this.needlePaint, p.needlePaint);
			boolean b12 = ObjectUtils.equal (this.valueFont, p.valueFont);
			boolean b13 = ObjectUtils.equal (this.valuePaint, p.valuePaint);

			return b0 && b1 && b2 && b3 && b4 && b5 && b6 && b7 && b8 && b9 && b10 && b11 && b12 &&
			b13;
		}

		return false;
	}

	/**
	 * Provides serialization support.
	 *
	 * @param stream  the output stream.
	 *
	 * @throws IOException  if there is an I/O error.
	 */
	private void writeObject (ObjectOutputStream stream)
		throws IOException
	{
		stream.defaultWriteObject ();
		SerialUtilities.writePaint (this.criticalPaint, stream);
		SerialUtilities.writePaint (this.dialBackgroundPaint, stream);
		SerialUtilities.writePaint (this.needlePaint, stream);
		SerialUtilities.writePaint (this.normalPaint, stream);
		SerialUtilities.writePaint (this.valuePaint, stream);
		SerialUtilities.writePaint (this.warningPaint, stream);
	}

	/**
	 * Provides serialization support.
	 *
	 * @param stream  the input stream.
	 *
	 * @throws IOException  if there is an I/O error.
	 * @throws ClassNotFoundException  if there is a classpath problem.
	 */
	private void readObject (ObjectInputStream stream)
		throws IOException, ClassNotFoundException
	{
		stream.defaultReadObject ();
		this.criticalPaint = SerialUtilities.readPaint (stream);
		this.dialBackgroundPaint = SerialUtilities.readPaint (stream);
		this.needlePaint = SerialUtilities.readPaint (stream);
		this.normalPaint = SerialUtilities.readPaint (stream);
		this.valuePaint = SerialUtilities.readPaint (stream);
		this.warningPaint = SerialUtilities.readPaint (stream);

		if (this.dataset != null)
		{
			this.dataset.addChangeListener (this);
		}
	}

	/**
	 * Returns the type of dial (DIALTYPE_PIE, DIALTYPE_CIRCLE, DIALTYPE_CHORD).
	 *
	 * @return The dial type.
	 *
	 * @deprecated Use getDialShape().
	 */
	public int getDialType ()
	{
		if (this.shape == DialShape.CIRCLE)
		{
			return NewMeterPlot.DIALTYPE_CIRCLE;
		}
		else if (this.shape == DialShape.CHORD)
		{
			return NewMeterPlot.DIALTYPE_CHORD;
		}
		else if (this.shape == DialShape.PIE)
		{
			return NewMeterPlot.DIALTYPE_PIE;
		}
		else
		{
			throw new IllegalStateException("NewMeterPlot.getDialType: unrecognised dial type.");
		}
	}

	/**
	 * Sets the dial type (background shape).
	 * <P>
	 * This controls the shape of the dial background.  Use one of the constants:
	 * DIALTYPE_PIE, DIALTYPE_CIRCLE, or DIALTYPE_CHORD.
	 *
	 * @param type The dial type.
	 *
	 * @deprecated Use setDialShape(...).
	 */
	public void setDialType (int type)
	{
		switch (type)
		{
			case NewMeterPlot.DIALTYPE_CIRCLE:
				setDialShape (DialShape.CIRCLE);

				break;

			case NewMeterPlot.DIALTYPE_CHORD:
				setDialShape (DialShape.CHORD);

				break;

			case NewMeterPlot.DIALTYPE_PIE:
				setDialShape (DialShape.PIE);

				break;

			default:
				throw new IllegalArgumentException("NewMeterPlot.setDialType: unrecognised type.");
		}
	}

	/**
	 * Correct cloning support, management of deeper copies and listeners
	 * @see Plot#clone()
	 */
	public Object clone ()
		throws CloneNotSupportedException
	{
		NewMeterPlot clone = (NewMeterPlot) super.clone ();

		if (clone.dataset != null)
		{
			clone.dataset.addChangeListener (clone);
		}

		return clone;
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