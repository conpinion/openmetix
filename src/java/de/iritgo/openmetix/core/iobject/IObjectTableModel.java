/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import de.iritgo.openmetix.core.Engine;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * IObjectTableModel.
 *
 * @version $Id: IObjectTableModel.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public abstract class IObjectTableModel
	extends AbstractTableModel
	implements IObjectProxyListener
{
	/** The object map. */
	private Map mapping;

	/** All model listeners. */
	private List modelListeners;

	/**
	 * Create a new IObjectTableModel.
	 */
	public IObjectTableModel ()
	{
		super();
		mapping = new HashMap();

		modelListeners = new LinkedList();
	}

	/**
	 * Add a listener to the list that is notified each time a change
	 * to the data model occurs.
	 *
	 * @param l the TableModelListener
	 */
	public void addTableModelListener (TableModelListener l)
	{
		modelListeners.add (l);
	}

	/**
	 * Remove a listener from the list that is notified each time a
	 * change to the data model occurs.
	 *
	 * @param l The TableModelListener
	 */
	public void removeTableModelListener (TableModelListener l)
	{
		modelListeners.remove (l);
	}

	/**
	 * Sets the value in the cell at <code>columnIndex</code> and
	 * <code>rowIndex</code> to <code>aValue</code>.
	 *
	 * @param aValue the new value
	 * @param rowIndex the row whose value is to be changed
	 * @param columnIndex the column whose value is to be changed
	 * @see #getValueAt
	 * @see #isCellEditable
	 */
	public void setValueAt (Object aValue, int rowIndex, int columnIndex)
	{
		IObject prototypeable = (IObject) aValue;

		if (prototypeable == null)
		{
			return;
		}

		long uniqueId = prototypeable.getUniqueId ();

		mapping.put (prototypeable, new IObjectTableModelItem(columnIndex, rowIndex, uniqueId));
		Engine.instance ().getProxyEventRegistry ().addEventListener (prototypeable, this);
	}

	/**
	 * The IObjectProxyListener, called if the basicobject is a fresh object or the update
	 * process is working.
	 *
	 * @param event The EventOject.
	 */
	public void proxyEvent (IObjectProxyEvent event)
	{
		if (event == null)
		{
			return;
		}

		if (event.isWaitingForNewObject ())
		{
			return;
		}

		long uniqueId = event.getUniqueId ();

		final IObjectTableModelItem item = (IObjectTableModelItem) mapping.get (event.getObject ());

		if (item != null)
		{
			for (Iterator j = modelListeners.iterator (); j.hasNext ();)
			{
				final TableModelEvent tableModelEvent = new TableModelEvent(this, item.y);
				final TableModelListener listener = (TableModelListener) j.next ();

				listener.tableChanged (tableModelEvent);
			}
		}
	}

	/**
	 * Free all resources.
	 */
	public void close ()
	{
		for (Iterator i = mapping.keySet ().iterator (); i.hasNext ();)
		{
			IObject prototypeable = (IObject) i.next ();

			Engine.instance ().getProxyEventRegistry ().removeEventListener (prototypeable, this);
		}
	}
}