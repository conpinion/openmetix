/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.link;


import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Serial communication link.
 *
 * @version $Id: SerialInterfaceDriver.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class SerialInterfaceDriver
	extends InterfaceDriver
{
	/** Comm port id. */
	protected CommPortIdentifier portId;

	/** Serial port. */
	protected SerialPort serialPort;

	/** Output stream. */
	protected OutputStream output;

	/** Input stream. */
	protected InputStream input;

	/** Poll intervall in ms. */
	protected int poll;

	/** Timeout in ms. */
	protected int timeout;

	/** Input buffer. */
	protected byte[] buffer;

	/**
	 * Create a new SerialInterfaceDriver.
	 */
	public SerialInterfaceDriver ()
	{
	}

	/**
	 * Open the communication link.
	 */
	public synchronized void open ()
	{
		if (openCount == 0)
		{
			String portName = config.getProperty ("port");

			int baudRate = NumberTools.toInt (config.getProperty ("baudRate"), 300);

			int dataBits = SerialPort.DATABITS_8;
			String dataBitsStr = config.getProperty ("dataBits");

			if ("8".equals (dataBitsStr))
			{
				dataBits = SerialPort.DATABITS_8;
			}
			else if ("7".equals (dataBitsStr))
			{
				dataBits = SerialPort.DATABITS_7;
			}
			else if ("6".equals (dataBitsStr))
			{
				dataBits = SerialPort.DATABITS_6;
			}
			else if ("5".equals (dataBitsStr))
			{
				dataBits = SerialPort.DATABITS_5;
			}

			int stopBits = SerialPort.STOPBITS_1;
			String stopBitsStr = config.getProperty ("stopBits");

			if ("1".equals (stopBitsStr))
			{
				stopBits = SerialPort.STOPBITS_1;
			}
			else if ("2".equals (stopBitsStr))
			{
				stopBits = SerialPort.STOPBITS_2;
			}

			int parity = SerialPort.PARITY_NONE;
			String parityStr = config.getProperty ("parity");

			if (parityStr.startsWith ("N"))
			{
				parity = SerialPort.PARITY_NONE;
			}
			else if (parityStr.startsWith ("E"))
			{
				parity = SerialPort.PARITY_EVEN;
			}
			else if (parityStr.startsWith ("O"))
			{
				parity = SerialPort.PARITY_ODD;
			}

			poll = NumberTools.toInt ((String) config.get ("poll"), 1);
			poll = Math.min (Math.max (poll, 1), 50) * 10;

			timeout = NumberTools.toInt ((String) config.get ("timeout"), 1);
			timeout = Math.min (Math.max (timeout, 1), 50) * 100;

			buffer = new byte[4096];

			try
			{
				portId = CommPortIdentifier.getPortIdentifier (portName);
				serialPort = (SerialPort) portId.open ("metix", 2000);

				serialPort.setSerialPortParams (baudRate, dataBits, stopBits, parity);

				output = serialPort.getOutputStream ();
				input = serialPort.getInputStream ();
			}
			catch (Exception x)
			{
				Log.logError (
					"server", "SerialInterfaceDriver",
					"Error while opening port " + portName + ": " + x.toString ());
			}
		}

		super.open ();
	}

	/**
	 * Close the communication link.
	 */
	public void close ()
	{
		super.close ();

		if (openCount == 0)
		{
			serialPort.close ();
		}
	}

	/**
	 * Send a string to the communication link and wait for a
	 * response.
	 *
	 * @param value The value to send.
	 * @return The response.
	 */
	public synchronized String comm (String value)
	{
		return comm (value, '\000');
	}

	/**
	 * Send a string to the communication link and wait for a
	 * response.
	 *
	 * @param value The value to send.
	 * @param nakChar Charackter to wait for in case of a communication error.
	 * @return The response.
	 */
	public synchronized String comm (String value, char nakChar)
	{
		try
		{
			output.write (value.getBytes ());

			int deadline = timeout;

			while (deadline > 0)
			{
				int available = input.available ();

				if (available > 0)
				{
					input.read (buffer);

					if (buffer[0] == nakChar)
					{
						return null;
					}

					return new String(buffer, 0, available);
				}

				try
				{
					Thread.sleep (poll);
				}
				catch (Exception x)
				{
				}

				deadline -= poll;
			}

			return null;
		}
		catch (IOException x)
		{
			Log.logError (
				"server", "SerialInterfaceDriver", "Communication error: " + x.toString ());

			return "";
		}
	}
}