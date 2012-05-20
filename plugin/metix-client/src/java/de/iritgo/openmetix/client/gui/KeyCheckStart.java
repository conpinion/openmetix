/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentConfigurator;
import de.iritgo.openmetix.client.command.KeyCheckCommand;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IButton;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.client.Client;
import de.iritgo.openmetix.framework.client.command.CloseDisplay;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * This gui pane is used to configure simple instruments.
 *
 * @version $Id: KeyCheckStart.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class KeyCheckStart
	extends InstrumentConfiguratorGUIPane
	implements InstrumentConfigurator
{
	/** Licensing name */
	public JTextField licensing;

	/** Part one of the serial. */
	public JTextField serial_1;

	/** Part two of the serial. */
	public JTextField serial_2;

	/** Part three of the serial. */
	public JTextField serial_3;

	/** Part four of the serial. */
	public JTextField serial_4;
	public IButton buttonOK;
	public String licensingName;
	public String key;

	/**
	 * Create a new KeyCheckConfigurator
	 */
	public KeyCheckStart ()
	{
		super("KeyCheckStart");
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return null;
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new KeyCheckStart();
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		super.initGUI ();

		try
		{
			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/KeyCheckConfigurator.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			CommandTools.performAsync (new CloseDisplay("KeyCheckResultDisplay"));

			buttonOK.setEnabled (false);

			serial_1.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent e)
					{
						if (checkSerialType ())
						{
							buttonOK.setEnabled (true);
						}
						else
						{
							buttonOK.setEnabled (false);
						}
					}
				});

			serial_2.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent e)
					{
						if (checkSerialType ())
						{
							buttonOK.setEnabled (true);
						}
						else
						{
							buttonOK.setEnabled (false);
						}
					}
				});

			serial_3.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent e)
					{
						if (checkSerialType ())
						{
							buttonOK.setEnabled (true);
						}
						else
						{
							buttonOK.setEnabled (false);
						}
					}
				});

			serial_4.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent e)
					{
						if (checkSerialType ())
						{
							buttonOK.setEnabled (true);
						}
						else
						{
							buttonOK.setEnabled (false);
						}
					}
				});
		}
		catch (Exception x)
		{
			Log.logError ("client", "KeyCheckStart.initGUI", x.toString ());
		}

		super.loadFromObject ();
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		super.loadFromObject ();
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
	}

	private boolean checkSerialType ()
	{
		if (
			serial_1.getText ().length () == 4 && serial_2.getText ().length () == 4 &&
			serial_3.getText ().length () == 4 && serial_4.getText ().length () == 4)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Save all modifications to the instrument, close the configurator
	 * and open the instrument display.
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				try
				{
					licensingName = licensing.getText ();
					key = serial_1.getText () + serial_2.getText () + serial_3.getText () +
						serial_4.getText ();
				}
				catch (NullPointerException npe)
				{
					npe.printStackTrace ();
				}

				CommandTools.performAsync (new ShowDialog("KeyCheckResultDisplay"));

				KeyCheckCommand kcc = new KeyCheckCommand();

				kcc.setLicensingName (licensingName);
				kcc.setKey (key);
				kcc.setPerformName ("update");
				kcc.setCheckStart (true);
				CommandTools.performAsync (kcc);
				display.close ();
			}
		};

	/**
	 * Cancel the instrument configuration and close the
	 * configurator.
	 */
	public Action cancelAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				final MetixClientGUI gui = (MetixClientGUI) Client.instance ().getClientGUI ();

				gui.getDesktopManager ().closeAllDisplays ();
				Client.instance ().getNetworkService ().closeAllChannel ();
			}
		};
}