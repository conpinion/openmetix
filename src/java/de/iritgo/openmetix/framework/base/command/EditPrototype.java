/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.command;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.action.ActionTools;
import de.iritgo.openmetix.framework.base.action.EditIObjectAction;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * EditPrototype.
 *
 * @version $Id: EditPrototype.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class EditPrototype
	extends Command
{
	private List prototypes;

	public EditPrototype ()
	{
		super("editprototype");
		prototypes = new LinkedList();
	}

	public EditPrototype (long prototype)
	{
		this();
		prototypes.add (new Long(prototype));
	}

	public EditPrototype (IObject prototype)
	{
		this();
		prototypes.add (new Long(prototype.getUniqueId ()));
	}

	public void addPrototype (IObject prototype)
	{
		prototypes.add (new Long(prototype.getUniqueId ()));
	}

	public void addPrototype (long uniqueId)
	{
		prototypes.add (new Long(uniqueId));
	}

	public void perform ()
	{
		ClientTransceiver clientTransceiver = new ClientTransceiver(0);

		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		for (Iterator i = userRegistry.userIterator (); i.hasNext ();)
		{
			User user = (User) i.next ();

			if (user.isOnline ())
			{
				clientTransceiver.addReceiver (user.getNetworkChannel ());
			}
		}

		EditIObjectAction editPrototypeAction = new EditIObjectAction(EditIObjectAction.OK);

		for (Iterator i = prototypes.iterator (); i.hasNext ();)
		{
			editPrototypeAction.addIObject ((Long) i.next ());
		}

		editPrototypeAction.setTransceiver (clientTransceiver);
		editPrototypeAction.setUniqueId (getUniqueId ());

		ActionTools.sendToClient (editPrototypeAction);
	}
}