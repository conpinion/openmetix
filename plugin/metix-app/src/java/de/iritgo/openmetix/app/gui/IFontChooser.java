/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gui;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.swing.SwingDesktopManager;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.framework.client.Client;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;


/**
 * Dialog for font selection.
 *
 * @version $Id: IFontChooser.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class IFontChooser
	extends JDialog
{
	/** All available fonts. */
	Font[] availableFonts;

	/** List from which to choose the font family. */
	public JList family;

	/** List from which to choose the font style. */
	public JList style;

	/** List from which to choose the font size. */
	public JList size;

	/** The selected font. */
	private Font selectedFont;

	/** Font text sample. */
	public JTextArea sample;

	/**
	 * Create a new IFontChooser.
	 *
	 * @param owner Parent for this dialog.
	 * @param title Dialog title.
	 * @param font Current font.
	 */
	public IFontChooser (Dialog owner, String title, Font font)
	{
		super(owner, title, true);
		init (font);
	}

	/**
	 * Create a new IFontChooser.
	 *
	 * @param owner Parent for this dialog.
	 * @param title Dialog title.
	 * @param font Current font.
	 */
	public IFontChooser (Frame owner, String title, Font font)
	{
		super(owner, title, true);
		init (font);
	}

	/**
	 * Initialize the dialog.
	 *
	 * @param font Current font.
	 */
	private void init (Font font)
	{
		try
		{
			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/IFontChooser.xml"));

			setContentPane (panel);

			availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment ().getAllFonts ();

			DefaultListModel model = new DefaultListModel();

			family.setModel (model);

			String[] families =
				GraphicsEnvironment.getLocalGraphicsEnvironment ().getAvailableFontFamilyNames ();
			int selectedIndex = 0;

			for (int i = 0; i < families.length; ++i)
			{
				model.addElement (families[i]);

				if (font != null && font.getFamily ().equals (families[i]))
				{
					selectedIndex = i;
				}
			}

			family.setSelectedIndex (selectedIndex);

			family.addListSelectionListener (
				new ListSelectionListener()
				{
					public void valueChanged (ListSelectionEvent e)
					{
						if (! e.getValueIsAdjusting ())
						{
							updateSample ();
						}
					}
				});

			model = new DefaultListModel();
			style.setModel (model);
			model.addElement (
				Engine.instance ().getResourceService ().getStringWithoutException (
					"metix.fontPlain"));
			model.addElement (
				Engine.instance ().getResourceService ().getStringWithoutException (
					"metix.fontItalic"));
			model.addElement (
				Engine.instance ().getResourceService ().getStringWithoutException (
					"metix.fontBold"));
			model.addElement (
				Engine.instance ().getResourceService ().getStringWithoutException (
					"metix.fontBoldItalic"));
			setSelectedStyle (font.getStyle ());

			style.addListSelectionListener (
				new ListSelectionListener()
				{
					public void valueChanged (ListSelectionEvent e)
					{
						if (! e.getValueIsAdjusting ())
						{
							updateSample ();
						}
					}
				});

			model = new DefaultListModel();
			size.setModel (model);

			int[] sizes =
				new int[]
				{
					Integer.MIN_VALUE,
					4,
					5,
					6,
					7,
					8,
					9,
					10,
					11,
					12,
					13,
					14,
					15,
					16,
					17,
					18,
					19,
					20,
					21,
					22,
					24,
					26,
					28,
					32,
					48,
					64,
					128,
					Integer.MAX_VALUE
				};

			selectedIndex = 0;

			for (int i = 1; i < sizes.length - 1; ++i)
			{
				model.addElement (String.valueOf (sizes[i]));

				if (font.getSize () > sizes[i - 1] && font.getSize () < sizes[i + 1])
				{
					selectedIndex = i - 1;
				}
			}

			size.setSelectedIndex (selectedIndex);

			size.addListSelectionListener (
				new ListSelectionListener()
				{
					public void valueChanged (ListSelectionEvent e)
					{
						if (! e.getValueIsAdjusting ())
						{
							updateSample ();
						}
					}
				});
		}
		catch (Exception x)
		{
			Log.logError ("client", "IFontChooser.init", x.toString ());
		}
	}

	/**
	 * Show the dialog.
	 *
	 * @param component Parent for this dialog.
	 */
	public static Font showDialog (JComponent component, String title, Font font)
	{
		IFontChooser dialog = null;
		Container top = component.getTopLevelAncestor ();

		if (top instanceof Dialog)
		{
			dialog = new IFontChooser((Dialog) top, title, font);
		}
		else if (top instanceof Frame)
		{
			dialog = new IFontChooser((Frame) top, title, font);
		}

		dialog.pack ();

		Rectangle bounds = dialog.getBounds ();

		bounds.width = (int) ((double) bounds.width * 1.5);
		bounds.height = (int) ((double) bounds.height * 1.5);
		dialog.setBounds (bounds);

		dialog.sample.setText (
			Engine.instance ().getResourceService ().getStringWithoutException ("metix.fontSample"));
		dialog.sample.setFont (font);

		dialog.setLocationRelativeTo (
			((SwingDesktopManager) Client.instance ().getClientGUI ().getDesktopManager ()).getJFrame ());
		dialog.setVisible (true);

		return dialog.selectedFont;
	}

	/**
	 * Get the currently selected font family.
	 *
	 * @return The currently selected font family.
	 */
	private String getSelectedFamily ()
	{
		return (String) family.getSelectedValue ();
	}

	/**
	 * Get the currently selected font style.
	 *
	 * @return The currently selected font style.
	 */
	private int getSelectedStyle ()
	{
		switch (style.getSelectedIndex ())
		{
			case 0:
				return Font.PLAIN;

			case 1:
				return Font.ITALIC;

			case 2:
				return Font.BOLD;

			case 3:
				return Font.BOLD + Font.ITALIC;

			default:
				return Font.PLAIN;
		}
	}

	/**
	 * Set the currently selected font style.
	 *
	 * @param styleId The currently selected font style.
	 */
	private void setSelectedStyle (int styleId)
	{
		int index = 0;

		switch (styleId)
		{
			case Font.PLAIN:
				index = 0;

				break;

			case Font.ITALIC:
				index = 1;

				break;

			case Font.BOLD:
				index = 2;

				break;

			case Font.BOLD + Font.ITALIC:
				index = 3;

				break;
		}

		style.setSelectedIndex (index);
	}

	/**
	 * Get the currently selected font size.
	 *
	 * @return The currently selected font size.
	 */
	private int getSelectedSize ()
	{
		return NumberTools.toInt ((String) size.getSelectedValue (), 10);
	}

	/**
	 * Get the currently selected font.
	 *
	 * @return The currently selected font.
	 */
	private Font getSelectedFont ()
	{
		return new Font(getSelectedFamily (), getSelectedStyle (), getSelectedSize ());
	}

	/**
	 * Update the sample text.
	 */
	private void updateSample ()
	{
		sample.setFont (getSelectedFont ());
	}

	/**
	 * Create a string representation of the specified font.
	 *
	 * @param font The font to convert.
	 * @return A font string like "family-style-size".
	 */
	public static String encode (Font font)
	{
		StringBuffer fontName = new StringBuffer(font.getFamily () + "- ");

		switch (font.getStyle ())
		{
			case Font.PLAIN:
				fontName.append ("PLAIN");

				break;

			case Font.ITALIC:
				fontName.append ("ITALIC");

				break;

			case Font.BOLD:
				fontName.append ("BOLD");

				break;

			case Font.BOLD + Font.ITALIC:
				fontName.append ("BOLDITALIC");

				break;
		}

		fontName.append ("-" + String.valueOf (font.getSize ()));

		return fontName.toString ();
	}

	/**
	 * Cancel the font selection.
	 */
	public Action cancelAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				setVisible (false);
			}
		};

	/**
	 * Cancel the font selection.
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				selectedFont = getSelectedFont ();
				setVisible (false);
			}
		};
}