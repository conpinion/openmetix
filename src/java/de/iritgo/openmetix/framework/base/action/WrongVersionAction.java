/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.Engine;
import java.io.IOException;


/**
 * WrongVersionAction.
 *
 * @version $Id: WrongVersionAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class WrongVersionAction
	extends FrameworkAction
{
	public WrongVersionAction ()
	{
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
	}

	public void perform ()
	{
		Engine.instance ().getFlowControl ().ruleSuccess ("WrongVersion");
	}
}