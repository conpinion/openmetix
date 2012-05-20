/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gui;


import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;


/**
 * A text field that displays the current time.
 *
 * @version $Id: IClockTextField.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class IClockTextField
	extends JTextField
	implements ActionListener
{
	/** The current time. */
	private Date currentTime;

	/** Timer. */
	private Timer timer;

	/**
	 * Create a new IClockTextField.
	 */
	public IClockTextField ()
	{
		currentTime = new Date();
		actionPerformed (null);
		timer = new Timer(1000, this);
	}

	/**
	 * Start the clock.
	 */
	public void start ()
	{
		timer.start ();
	}

	/**
	 * Stop the clock.
	 */
	public void stop ()
	{
		timer.stop ();
	}

	/**
	 * Called every second.
	 *
	 * @param e The action event.
	 */
	public void actionPerformed (ActionEvent e)
	{
		currentTime.setTime (System.currentTimeMillis ());
		setText (String.valueOf (currentTime));
	}
}