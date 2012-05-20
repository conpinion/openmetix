/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.guinetworkdisplay.gui;


import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


public class InfoItem
{
	private JPanel panel;
	private String guiPaneId;
	private long uniqueId;

	public InfoItem (
		String category, String icon, String message, String guiPaneId, long uniqueId, int level)
	{
		init (category, icon, message, guiPaneId, uniqueId, level);
	}

	public InfoItem (String message)
	{
		init ("", "", message, "", 0, 0);
	}

	public void init (
		String category, String icon, String message, String guiPaneId, long uniqueId, int level)
	{
		this.guiPaneId = guiPaneId;
		this.uniqueId = uniqueId;

		panel = new JPanel();

		GridBagLayout gridBagLayout = new GridBagLayout();

		panel.setLayout (gridBagLayout);

		int row = 0;

		JLabel label = new JLabel("IC");

		panel.add (
			label,
			getConstraints (
				0, row, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, new Insets(5, 15, 5, 15)));

		JEditorPane messagePane = new JEditorPane("text/html", message);

		panel.add (
			messagePane,
			getConstraints (
				1, row, 1, 1, GridBagConstraints.BOTH, 100, 100, new Insets(5, 15, 5, 15)));
	}

	public JPanel getPanel ()
	{
		return panel;
	}

	public String getGuiPaneId ()
	{
		return guiPaneId;
	}

	public long getUniqueId ()
	{
		return uniqueId;
	}

	protected GridBagConstraints getConstraints (
		int x, int y, int width, int height, int fill, int wx, int wy, Insets insets)
	{
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		if (insets == null)
		{
			gbc.insets = new Insets(0, 0, 0, 0);
		}
		else
		{
			gbc.insets = insets;
		}

		return gbc;
	}
}