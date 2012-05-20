/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.framework.base.DataObject;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import java.util.Iterator;


/**
 * IObjectComboBoxModel.
 *
 * @version $Id: IObjectComboBoxModel.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectComboBoxModel
	extends DefaultComboBoxModel
	implements IObjectProxyListener
{
	/** Name of the object list. */
	private String iObjectListName;

	/** Owning object. */
	private IObject owner;

	/**
	 * Create a new IObjectComboBoxModel.
	 */
	public IObjectComboBoxModel ()
	{
		super();
	}

	/**
	 * Update the model.
	 *
	 * @param list The object list to display.
	 */
	public void update (IObjectList list)
	{
		owner = list.getOwner ();
		Engine.instance ().getProxyEventRegistry ().addEventListener (owner, this);
		iObjectListName = list.getAttributeName ();

		Object selectedObject = getSelectedItem ();
		boolean selectedFound = false;

		removeAllElements ();

		for (Iterator i = list.iterator (); i.hasNext ();)
		{
			Object element = i.next ();

			if (element == selectedObject)
			{
				selectedFound = true;
			}

			addElement (element);
		}

		if (! selectedFound)
		{
			if (getSize () > 0)
			{
				selectedObject = list.get (0);
			}
			else
			{
				selectedObject = null;
			}
		}

		setSelectedItem (selectedObject);
	}

	/**
	 * Sets the value in the cell at <code>columnIndex</code> and
	 * <code>rowIndex</code> to <code>aValue</code>.
	 *
	 * @param aValue The new value
	 */
	public void addElement (Object aValue)
	{
		IObject prototypeable = (IObject) aValue;

		if (prototypeable == null)
		{
			return;
		}

		long uniqueId = prototypeable.getUniqueId ();

		Engine.instance ().getProxyEventRegistry ().addEventListener (prototypeable, this);

		super.addElement (aValue);
	}

	/**
	 * The ProxyListener, called if the basicobject is a fresh object or the update process is working.
	 *
	 * @param event The EventOject.
	 */
	public void proxyEvent (IObjectProxyEvent event)
	{
		if (event.isWaitingForNewObject ())
		{
			return;
		}

		long uniqueId = event.getUniqueId ();
		final int size = getSize ();
		final IObject iobject = event.getObject ();
		final DataObject dataObject = (DataObject) iobject;

		if (owner == dataObject)
		{
			update (dataObject.getIObjectListAttribute (iObjectListName));
		}

		try
		{
			SwingUtilities.invokeLater (
				new Runnable()
				{
					public void run ()
					{
						fireContentsChanged (iobject, 0, size);
					}
				});
		}
		catch (Exception x)
		{
		}
	}

	/**
	 * Free all resources.
	 */
	public void close ()
	{
	}
}