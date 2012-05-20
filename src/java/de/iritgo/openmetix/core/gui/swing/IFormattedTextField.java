/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import javax.swing.JFormattedTextField;
import java.awt.event.FocusEvent;
import java.text.Format;


/**
 * An enhanced JFormattedTextField.
 *
 * @version $Id: IFormattedTextField.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IFormattedTextField
	extends JFormattedTextField
{
	/** If true, the whole text is selected if the component gains the focus. */
	protected boolean autoSelect = false;

	/**
	 * Creates a JFormattedTextField with no AbstractFormatterFactory. Use setMask
	 * or setFormatterFactory to configure the JFormattedTextField to edit a particular
	 * type of value.
	 */
	public IFormattedTextField ()
	{
		super();
	}

	/**
	 * Creates a JFormattedTextField with the specified value. This will create an
	 * AbstractFormatterFactory based on the type of value.
	 *
	 * @param value Initial value for the JFormattedTextField.
	 */
	public IFormattedTextField (Object value)
	{
		super(value);
	}

	/**
	 * Creates a JFormattedTextField. format is wrapped in an appropriate AbstractFormatter
	 * which is then wrapped in an AbstractFormatterFactory.
	 *
	 * @param format Format used to look up an AbstractFormatter.
	 */
	public IFormattedTextField (Format format)
	{
		super(format);
	}

	/**
	 * Creates a JFormattedTextField with the specified AbstractFormatter. The AbstractFormatter
	 * is placed in an AbstractFormatterFactory.
	 *
	 * @param formatter AbstractFormatter to use for formatting.
	 */
	public IFormattedTextField (JFormattedTextField.AbstractFormatter formatter)
	{
		super(formatter);
	}

	/**
	 * Creates a JFormattedTextField with the specified AbstractFormatterFactory.
	 *
	 * @param factory AbstractFormatterFactory used for formatting.
	 */
	public IFormattedTextField (JFormattedTextField.AbstractFormatterFactory factory)
	{
		super(factory);
	}

	/**
	 * Creates a JFormattedTextField with the specified AbstractFormatterFactory and
	 * initial value.
	 *
	 * @param factory AbstractFormatterFactory used for formatting.
	 * @param currentValue Initial value to use.
	 */
	public IFormattedTextField (
		JFormattedTextField.AbstractFormatterFactory factory, Object currentValue)
	{
		super(factory, currentValue);
	}

	/**
	 * Specifiy wether the whole text should be automatically selected if the
	 * text field gains the focus.
	 *
	 * @param autoSelect If true the text is selected if the text field is
	 *   activated.
	 */
	public void setAutoSelect (boolean autoSelect)
	{
		this.autoSelect = autoSelect;
	}

	/**
	 * @see javax.swing.JFormattedTextField#processFocusEvent(java.awt.event.FocusEvent)
	 */
	protected void processFocusEvent (FocusEvent e)
	{
		super.processFocusEvent (e);

		if (autoSelect)
		{
			if (e.getID () == FocusEvent.FOCUS_GAINED)
			{
				this.selectAll ();
			}
			else
			{
				this.select (0, 0);
			}
		}
	}
}