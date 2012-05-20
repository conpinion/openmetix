/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.guinetworkdisplay.gui;


import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;


public class InfoCellRenderer
	implements TableCellRenderer
{
	private JPanel panel;

	public Component getTableCellRendererComponent (
		JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		InfoItem infoItem = (InfoItem) value;

		panel = infoItem.getPanel ();

		if (isSelected)
		{
			panel.setBackground (table.getSelectionBackground ());
			panel.setForeground (table.getSelectionForeground ());
		}
		else
		{
			panel.setBackground (table.getBackground ());
			panel.setForeground (table.getForeground ());
		}

		panel.setEnabled (table.isEnabled ());
		panel.setFont (table.getFont ());
		panel.setOpaque (true);

		return panel;
	}
}