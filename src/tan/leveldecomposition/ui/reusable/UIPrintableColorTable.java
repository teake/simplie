/*
 * UIPrintableColorTable.java
 *
 * Created on 17 april 2007, 17:15
 *
 */

package tan.leveldecomposition.ui.reusable;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableCellRenderer;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.print.PrinterException;

public class UIPrintableColorTable extends JTable
{
	private boolean printing = false;
	
	public UIPrintableColorTable()
	{
	}
	public void print(Graphics g)
	{
		printing = true;
		try
		{
			super.print(g);
		}
		finally
		{
			printing = false;
		}
	}
	
	public Component prepareRenderer(
			TableCellRenderer renderer,
			int row, int col)
	{
		Component c = super.prepareRenderer(renderer,
				row, col);
		// if printing, only use plain background
		if (printing)
		{
			c.setBackground(getBackground());
		}
		else
		{
			if (row % 2 == 0 && !isCellSelected(row,col))
			{
				c.setBackground(new Color(244, 246, 248));
				// cell is selected, use the highlight color
			}
			else if (isCellSelected(row, col))
			{
				c.setBackground(getSelectionBackground());
			}
			else
			{
				c.setBackground(getBackground());
			}
		}
		return c;
	}
}
