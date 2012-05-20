/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.interfaceregistry;


import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingSystem;
import de.iritgo.openmetix.interfacing.link.Interface;
import de.iritgo.openmetix.interfacing.link.SerialLink;
import java.util.Iterator;


/**
 * This registry contains all gaging systems.
 *
 * @version $Id: InterfaceRegistry.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class InterfaceRegistry
	extends DataObject
{
	/**
	 * Create a new InterfaceRegistry.
	 */
	public InterfaceRegistry ()
	{
		super("InterfaceRegistry");

		putAttribute (
			"gagingSystems",
			new IObjectList("gagingSystems", new FrameworkProxy(new GagingSystem()), this));

		putAttribute (
			"interfaces", new IObjectList("interfaces", new FrameworkProxy(new Interface()), this));

		putAttribute (
			"serialLinks",
			new IObjectList("serialLinks", new FrameworkProxy(new SerialLink()), this));
	}

	/**
	 * Add a gaging system to the registry.
	 *
	 * @param gagingSystem The system to add.
	 */
	public void addGagingSystem (GagingSystem gagingSystem)
	{
		getIObjectListAttribute ("gagingSystems").add (gagingSystem);
	}

	/**
	 * Remove a gaging system from the registry.
	 *
	 * @param gagingSystem The system to remove.
	 */
	public void removeGagingSystem (GagingSystem gagingSystem)
	{
		getIObjectListAttribute ("gagingSystems").remove (gagingSystem);
	}

	/**
	 * Retrieve a specific gaging system.
	 *
	 * @param index The index of the system to retrieve.
	 * @return The gaging system.
	 */
	public GagingSystem getGagingSystem (int index)
	{
		return (GagingSystem) getIObjectListAttribute ("gagingSystems").get (index);
	}

	/**
	 * Get an iterator over all gaging systems in the registry.
	 *
	 * @return A gaging system iterator.
	 */
	public Iterator getGagingSystemIterator ()
	{
		return getIObjectListAttribute ("gagingSystems").iterator ();
	}

	/**
	 * Get the gaging system list.
	 *
	 * @return The gaging system list.
	 */
	public IObjectList getGagingSystems ()
	{
		return getIObjectListAttribute ("gagingSystems");
	}

	/**
	 * Get the number of gaging system stored in the registry.
	 *
	 * @return The number of gaging systems.
	 */
	public int getGagingSystemCount ()
	{
		return getIObjectListAttribute ("gagingSystems").size ();
	}

	/**
	 * Add an interface to the registry.
	 *
	 * @param iface The interface to add.
	 */
	public void addInterface (Interface iface)
	{
		getIObjectListAttribute ("interfaces").add (iface);
	}

	/**
	 * Remove an interface from the registry.
	 *
	 * @param iface The interface to remove.
	 */
	public void removeInterface (Interface iface)
	{
		getIObjectListAttribute ("interfaces").remove (iface);
	}

	/**
	 * Retrieve an interface.
	 *
	 * @param index The index of the interface to retrieve.
	 * @return The interface.
	 */
	public Interface getInterface (int index)
	{
		return (Interface) getIObjectListAttribute ("interfaces").get (index);
	}

	/**
	 * Get an iterator over all interfaces in the registry.
	 *
	 * @return An interface iterator.
	 */
	public Iterator getInterfaceIterator ()
	{
		return getIObjectListAttribute ("interfaces").iterator ();
	}

	/**
	 * Get the interface list.
	 *
	 * @return The interface list.
	 */
	public IObjectList getInterfaces ()
	{
		return getIObjectListAttribute ("interfaces");
	}

	/**
	 * Get the number of interfaces stored in the registry.
	 *
	 * @return The number of interfaces.
	 */
	public int getInterfaceCount ()
	{
		return getIObjectListAttribute ("interfaces").size ();
	}

	/**
	 * Add a serial link to the registry.
	 *
	 * @param link The serial link to add.
	 */
	public void addSerialLink (SerialLink link)
	{
		getIObjectListAttribute ("serialLinks").add (link);
	}

	/**
	 * Remove a serial link from the registry.
	 *
	 * @param iface The serial link to remove.
	 */
	public void removeSerialLink (SerialLink link)
	{
		getIObjectListAttribute ("serialLinks").remove (link);
	}

	/**
	 * Retrieve a serial link.
	 *
	 * @param index The index of the serial link to retrieve.
	 * @return The serial link.
	 */
	public SerialLink getSerialLink (int index)
	{
		return (SerialLink) getIObjectListAttribute ("serialLinks").get (index);
	}

	/**
	 * Get an iterator over all serial links in the registry.
	 *
	 * @return A serial link iterator.
	 */
	public Iterator getSerialLinkIterator ()
	{
		return getIObjectListAttribute ("serialLinks").iterator ();
	}

	/**
	 * Get the serial link list.
	 *
	 * @return The serial link list.
	 */
	public IObjectList getSerialLinks ()
	{
		return getIObjectListAttribute ("serialLinks");
	}

	/**
	 * Get the number of serial links stored in the registry.
	 *
	 * @return The number of serial links.
	 */
	public int getSerialLinkCount ()
	{
		return getIObjectListAttribute ("serialLinks").size ();
	}
}