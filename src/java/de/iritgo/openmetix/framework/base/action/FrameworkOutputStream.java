/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import java.io.DataOutputStream;
import java.io.OutputStream;


/**
 * FrameworkOutputStream.
 *
 * @version $Id: FrameworkOutputStream.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class FrameworkOutputStream
	extends DataOutputStream
{
	public FrameworkOutputStream (OutputStream stream)
	{
		super(stream);
	}
}