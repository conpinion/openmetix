/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.core.Engine;
import javax.swing.JPanel;


/**
 * Editor for system output sensors.
 *
 * @version $Id: DewpointSensorBehaviourEditor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class DewpointSensorBehaviourEditor
	extends TwoInputSensorBehaviourEditor
{
	/**
	 * Create a new SimplePeriodSensorBehaviourEditor.
	 */
	public DewpointSensorBehaviourEditor ()
	{
	}

	/**
	 * Create the editor panel.
	 *
	 * @return The editor panel.
	 */
	protected JPanel createEditorPanel ()
	{
		JPanel panel = super.createEditorPanel ();

		title.setText (
			Engine.instance ().getResourceService ().getStringWithoutException (
				"metix.dewpointFormula"));

		titleSensor1.setText (
			Engine.instance ().getResourceService ().getStringWithoutException (
				"metix.temperature"));

		titleSensor2.setText (
			Engine.instance ().getResourceService ().getStringWithoutException (
				"metix.relhumidity"));

		return panel;
	}
}