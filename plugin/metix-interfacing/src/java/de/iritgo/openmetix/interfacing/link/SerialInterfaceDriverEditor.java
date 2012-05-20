/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.link;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.iobject.IObjectComboBoxModel;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.interfacing.InterfacingPlugin;
import de.iritgo.openmetix.interfacing.interfaceregistry.InterfaceRegistry;
import org.swixml.SwingEngine;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Editor for serial drivers.
 *
 * @version $Id: SerialInterfaceDriverEditor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class SerialInterfaceDriverEditor
	extends InterfaceDriverEditor
{
	/** Comm comPort. */
	public JComboBox comPort;

	/** Comm ports model. */
	private IObjectComboBoxModel comPortModel;

	/** Baud rate. */
	public JComboBox baudRate;

	/** Parity. */
	public JComboBox parity;

	/** Number of data bits. */
	public JComboBox dataBits;

	/** Number of stop bits. */
	public JComboBox stopBits;

	/** The timout. */
	public JSlider timeout;

	/** The timout display value. */
	public JLabel timeoutValue;

	/** The poll intervall. */
	public JSlider poll;

	/** The poll intervall display value. */
	public JLabel pollValue;

	/**
	 * Create a new SerialInterfaceDriverEditor.
	 */
	public SerialInterfaceDriverEditor ()
	{
	}

	/**
	 * Create the editor panel.
	 *
	 * @return The editor panel.
	 */
	protected JPanel createEditorPanel ()
	{
		JPanel panel = null;

		try
		{
			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader (InterfacingPlugin.class.getClassLoader ());

			panel =
				(JPanel) swingEngine.render (
					getClass ().getResource ("/swixml/SerialInterfaceDriverEditor.xml"));

			baudRate.addItem ("300");
			baudRate.addItem ("1200");
			baudRate.addItem ("2400");
			baudRate.addItem ("4800");
			baudRate.addItem ("9600");
			baudRate.addItem ("19200");
			baudRate.addItem ("38400");
			baudRate.addItem ("57600");
			baudRate.addItem ("115200");
			baudRate.addItem ("230400");

			dataBits.addItem ("5");
			dataBits.addItem ("6");
			dataBits.addItem ("7");
			dataBits.addItem ("8");

			stopBits.addItem ("1");
			stopBits.addItem ("2");

			parity.addItem (
				"N (" +
				Engine.instance ().getResourceService ().getStringWithoutException (
					"metix-interfacing.parityNone") + ")");
			parity.addItem (
				"E (" +
				Engine.instance ().getResourceService ().getStringWithoutException (
					"metix-interfacing.parityEven") + ")");
			parity.addItem (
				"O (" +
				Engine.instance ().getResourceService ().getStringWithoutException (
					"metix-interfacing.parityOdd") + ")");

			comPortModel = new IObjectComboBoxModel();
			comPort.setModel (comPortModel);

			timeout.addChangeListener (
				new ChangeListener()
				{
					public void stateChanged (ChangeEvent e)
					{
						timeoutValue.setText (String.valueOf (timeout.getValue () * 100));
					}
				});

			poll.addChangeListener (
				new ChangeListener()
				{
					public void stateChanged (ChangeEvent e)
					{
						pollValue.setText (String.valueOf (poll.getValue () * 10));
					}
				});
		}
		catch (Exception x)
		{
			Log.logError ("client", "SerialInterfaceDriverEditor.createEditorPanel", x.toString ());
		}

		return panel;
	}

	/**
	 * Load the configuration properties into the gui.
	 */
	public void loadFromProperties ()
	{
		try
		{
			InterfaceRegistry ifaceRegistry =
				(InterfaceRegistry) AppContext.instance ().getUser ().getNamedIritgoObject (
					"InterfaceRegistry", InterfaceRegistry.class);

			comPortModel.update (ifaceRegistry.getSerialLinks ());

			String paramPort = (String) configProperties.get ("port");

			for (int i = 0; i < comPortModel.getSize (); ++i)
			{
				SerialLink link = (SerialLink) comPortModel.getElementAt (i);

				if (link.getDevice ().equals (paramPort))
				{
					comPort.setSelectedIndex (i);

					break;
				}
			}
		}
		catch (NoSuchIObjectException x)
		{
		}

		String paramBaudRate = (String) configProperties.get ("baudRate");

		for (int i = 0; i < baudRate.getItemCount (); ++i)
		{
			if (baudRate.getItemAt (i).toString ().equals (paramBaudRate))
			{
				baudRate.setSelectedIndex (i);

				break;
			}
		}

		String paramDataBits = (String) configProperties.get ("dataBits");

		for (int i = 0; i < dataBits.getItemCount (); ++i)
		{
			if (dataBits.getItemAt (i).toString ().equals (paramDataBits))
			{
				dataBits.setSelectedIndex (i);

				break;
			}
		}

		String paramStopBits = (String) configProperties.get ("stopBits");

		for (int i = 0; i < stopBits.getItemCount (); ++i)
		{
			if (stopBits.getItemAt (i).toString ().equals (paramStopBits))
			{
				stopBits.setSelectedIndex (i);

				break;
			}
		}

		String paramParity = (String) configProperties.get ("parity");

		for (int i = 0; i < parity.getItemCount (); ++i)
		{
			if (parity.getItemAt (i).toString ().equals (paramParity))
			{
				parity.setSelectedIndex (i);

				break;
			}
		}

		if (configProperties.get ("timeout") != null)
		{
			int val = NumberTools.toInt ((String) configProperties.get ("timeout"), 1);

			val = Math.min (Math.max (val, 1), 50);
			timeout.setValue (val);
		}
		else
		{
			timeout.setValue (1);
		}

		timeoutValue.setText (String.valueOf (timeout.getValue () * 100));

		if (configProperties.get ("poll") != null)
		{
			int val = NumberTools.toInt ((String) configProperties.get ("poll"), 1);

			val = Math.min (Math.max (val, 1), 50);
			poll.setValue (val);
		}
		else
		{
			poll.setValue (1);
		}

		pollValue.setText (String.valueOf (poll.getValue () * 10));
	}

	/**
	 * Store the gui values to the configuration properties.
	 */
	public void storeToProperties ()
	{
		configProperties.put ("port", comPort.getSelectedItem ().toString ());
		configProperties.put ("baudRate", baudRate.getSelectedItem ().toString ());
		configProperties.put ("dataBits", dataBits.getSelectedItem ().toString ());
		configProperties.put ("stopBits", stopBits.getSelectedItem ().toString ());
		configProperties.put ("parity", parity.getSelectedItem ().toString ());
		configProperties.put ("timeout", String.valueOf (timeout.getValue ()));
		configProperties.put ("poll", String.valueOf (poll.getValue ()));
	}
}