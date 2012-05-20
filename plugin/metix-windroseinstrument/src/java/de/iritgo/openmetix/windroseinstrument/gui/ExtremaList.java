/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.windroseinstrument.gui;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;


/**
 * @version $Id: ExtremaList.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ExtremaList
{
	/** Maximum extrema value. */
	private double extremaMax;

	/** Minimum extrema value. */
	private double extremaMin;

	/** Store the extrema values*/
	private LinkedList extremaLinkedList;

	public ExtremaList ()
	{
		extremaLinkedList = new LinkedList();
	}

	/**
	 * Add an extrema value in the linkedlist. Adding a new value invokes a deletion of first value in the linkedlist.
	 *
	 * @param timestamp
	 * @param value
	 * @param minExtrema True if 10-Minutes Extrema is used
	 * @param hourExtrema True if 1-Hour Extrema is used
	 */
	public void addValue (
		Timestamp timestamp, double value, boolean minExtrema, boolean hourExtrema)
	{
		Hashtable valueHashtable = new Hashtable();

		valueHashtable.put (timestamp, new Double(value));
		extremaLinkedList.add (valueHashtable);
		deleteFirstValue (minExtrema, hourExtrema);
	}

	/**
	 * Delete the first value in the linkedlist
	 *
	 * @param minExtrema True if 10-Minutes Extrema is used
	 * @param hourExtrema True if 1-Hour Extrema is used
	 */
	public void deleteFirstValue (boolean minExtrema, boolean hourExtrema)
	{
		Calendar calendar = new GregorianCalendar();

		if (minExtrema == true)
		{
			calendar.add (Calendar.MINUTE, -10);
		}

		if (hourExtrema == true)
		{
			calendar.add (Calendar.HOUR_OF_DAY, -1);
		}

		Date currentDate = calendar.getTime ();
		boolean valueRemove = false;
		Hashtable valueHastable = (Hashtable) extremaLinkedList.getFirst ();
		Enumeration e = valueHastable.keys ();

		while (e.hasMoreElements ())
		{
			Timestamp alias = (Timestamp) e.nextElement ();

			if (currentDate.getTime () > alias.getTime ())
			{
				valueRemove = true;
			}
		}

		if (valueRemove == true)
		{
			extremaLinkedList.remove (extremaLinkedList.getFirst ());
		}
	}

	/**
	 * Get an extrema value which is depend on the timestamp
	 *
	 * @param table
	 * @return Extrema value dependent on the timestamp
	 */
	public double getValue (Hashtable table)
	{
		Enumeration e = table.keys ();
		double value = 0;

		while (e.hasMoreElements ())
		{
			Timestamp alias = (Timestamp) e.nextElement ();
			Double d = (Double) table.get (alias);

			value = d.doubleValue ();
		}

		return value;
	}

	/**
	 * Compute the maximum extrema value.
	 *
	 */
	public void computeMax ()
	{
		int pos = extremaLinkedList.indexOf (extremaLinkedList.getFirst ());
		ListIterator i = extremaLinkedList.listIterator (pos);
		double tempMax = 0;

		while (i.hasNext ())
		{
			Hashtable table = (Hashtable) i.next ();
			double v = getValue (table);

			tempMax = Math.max (tempMax, v);
		}

		extremaMax = tempMax;
	}

	/**
	 * Compute the minimum exrema value.
	 *
	 */
	public void computeMin ()
	{
		int pos = extremaLinkedList.indexOf (extremaLinkedList.getFirst ());
		ListIterator i = extremaLinkedList.listIterator (pos);
		double tempMin = 360;

		while (i.hasNext ())
		{
			Hashtable table = (Hashtable) i.next ();
			double v = getValue (table);

			tempMin = Math.min (tempMin, v);
		}

		extremaMin = tempMin;
	}

	/**
	 * Get the maximum extrema value.
	 *
	 * @return
	 */
	public double getExtremaMax ()
	{
		return extremaMax;
	}

	/**
	 * Get the minimum extrema value.
	 *
	 * @return
	 */
	public double getExtremaMin ()
	{
		return extremaMin;
	}
}