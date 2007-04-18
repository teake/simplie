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
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.print.PrinterException;

import javax.swing.text.JTextComponent;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class UIPrintableColorTable extends JTable
{
	private boolean printing = false;
	
	/** Creates a printable color table that automatically resizes its columns. */
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
	
	public void tableChanged(TableModelEvent e)
	{
		super.tableChanged(e);
		
		if(e.getType() != e.INSERT)
			return;
		
		/** The following code auto-resizes the column of the table to fit their data. */
		
		Component comp = null;
		TableCellRenderer tableCellRenderer;
		int maxWidth;
		TableColumnModel tableColumnModel = super.getColumnModel();
		TableColumn tableColumn;
		
		for (int i = 0; i < super.getColumnCount(); i++)
		{
			tableCellRenderer = super.getCellRenderer( e.getFirstRow(), i );
			comp = tableCellRenderer.getTableCellRendererComponent( this, super.getValueAt( e.getFirstRow(), i ), false, false, e.getFirstRow(), i );
			
			if ( comp instanceof JTextComponent )
			{
				JTextComponent jtextComp = (JTextComponent)comp;
				
				String text = jtextComp.getText();
				Font font = jtextComp.getFont();
				FontMetrics fontMetrics = jtextComp.getFontMetrics( font );
				
				maxWidth = SwingUtilities.computeStringWidth( fontMetrics, text );
			}
			else
			{
				maxWidth = comp.getPreferredSize().width;
			}
			tableColumn = tableColumnModel.getColumn( i );
			if(e.getFirstRow() != 0)
				maxWidth = Math.max(tableColumn.getPreferredWidth(), maxWidth);
			tableColumn.setPreferredWidth(  maxWidth );
		}
	}
}
