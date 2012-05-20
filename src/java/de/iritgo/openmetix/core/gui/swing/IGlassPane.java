/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;


/**
 * This component is used as a glass pane in windows and dialogs to enable/disable
 * the whole window/dialog.
 *
 * @version $Id: IGlassPane.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IGlassPane
	extends JComponent
{
	/** A do nothing mouse listener. */
	private MouseAdapter nullMouseAdapter;

	/** A do nothing mouse motion listener. */
	private MouseMotionAdapter nullMouseMotionAdapter;

	/** A do nothing key listener. */
	private KeyAdapter nullKeyAdapter;

	/** If true the underlying display will be enabled. */
	private boolean enabled;

	/** Texture image. */
	private BufferedImage texture;

	/** Texture pain. */
	private TexturePaint texturePaint;

	/**
	 * Create a new LessonDisplayGlassPane.
	 */
	public IGlassPane ()
	{
		nullMouseAdapter = new MouseAdapter()
				{
				};
		nullMouseMotionAdapter = new MouseMotionAdapter()
				{
				};
		nullKeyAdapter = new KeyAdapter()
				{
				};

		texture = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = (Graphics2D) texture.getGraphics ();

		g2.setPaint (new Color(255, 255, 255, 64));
		g2.fillRect (0, 0, 2, 2);
		g2.setPaint (new Color(0, 0, 0, 64));
		g2.fillRect (0, 0, 1, 1);
		g2.fillRect (1, 1, 1, 1);

		texturePaint = new TexturePaint(texture, new Rectangle(0, 0, 2, 2));

		setEnabled (true);
		setVisible (true);
	}

	/**
	 * Paint the glass pane.
	 *
	 * @param g The graphics context.
	 */
	public void paint (Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		Rectangle bounds = getBounds ();

		if (enabled)
		{
		}
		else
		{
			Component topLevel = getTopLevelAncestor ();
			Color bgColor = topLevel.getBackground ();

			g2.setPaint (
				new Color(bgColor.getRed (), bgColor.getGreen (), bgColor.getBlue (), 128));
			g2.fillRect (0, 0, bounds.width, bounds.height);
		}
	}

	/**
	 * Enable / disable the glass pane.
	 *
	 * @param enabled If true the glass pane will be enabled.
	 */
	public void setEnabled (boolean enabled)
	{
		this.enabled = enabled;

		if (enabled)
		{
			removeMouseListener (nullMouseAdapter);
			removeMouseMotionListener (nullMouseMotionAdapter);
			removeKeyListener (nullKeyAdapter);
		}
		else
		{
			addMouseListener (nullMouseAdapter);
			addMouseMotionListener (nullMouseMotionAdapter);
			addKeyListener (nullKeyAdapter);
		}

		repaint ();
	}

	/**
	 * Check wether the glass pane is enabled or not.
	 *
	 * @return True if the pane is enabled.
	 */
	public boolean isEnabled ()
	{
		return enabled;
	}
}