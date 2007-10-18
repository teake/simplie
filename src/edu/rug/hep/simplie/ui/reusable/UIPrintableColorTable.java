/*
 * UIPrintableColorTable.java
 *
 * Created on 17 april 2007, 17:15
 *
 * This file is part of SimpLie.
 * 
 * SimpLie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SimpLie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SimpLie.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package edu.rug.hep.simplie.ui.reusable;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.text.JTextComponent;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class UIPrintableColorTable extends JTable
{
	private boolean printing = false;
	private String titleTeX = "";
	
	/** Creates a printable color table that automatically resizes its columns. */
	public UIPrintableColorTable()
	{
		super.setShowGrid(false);
		super.setAutoCreateRowSorter(true);
	}
	public void print(Graphics g)
	{
		printing = true;
		try
		{
			super.setShowHorizontalLines(true);
			super.print(g);
		}
		finally
		{
			super.setShowHorizontalLines(false);
			printing = false;
		}
	}
	
	public void setTitleTeX(String title)
	{
		this.titleTeX = title;
	}
	
	public String getTitleTeX()
	{
		return this.titleTeX;
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
	
	public String toTeX(boolean includeCaption, int[] columns, int extraColumns)
	{
		String output = new String();
		
		if(super.getRowCount() == 0)
		{
			return output;
		}
		
		int totalColumns = columns.length + extraColumns;
		
		/** First determine which columns we should split into multicolumns. */
		int[] multiColumns = new int[columns.length];
		for (int i = 0; i < columns.length; i++)
		{
			multiColumns[i] = 1;
			Object data = super.getValueAt(0,columns[i]);
			if(data.getClass() != String.class)
				continue;
			
			String dataString = (String) data;
			int offset = 0;
			while(dataString.indexOf(" ",offset) != -1)
			{
				offset = dataString.indexOf(" ",offset) + 1;
				multiColumns[i]++;
			}
		}
		
		/** Write the header. */
		output += "\\begin{longtable}{";
		for (int i = 0; i < columns.length; i++)
		{
			output += "|";
			for (int j = 1; j < multiColumns[i]; j++)
			{
				output += "r@{\\ }";
			}
			output += "r";
		}
		for (int i = 0; i < extraColumns; i++)
		{
			output += "|r";
		}
		output += "|} \n";
		if(includeCaption)
			output += "\\caption{" + titleTeX + "} \\\\ \n";
		output += "\\hline \n";
		for (int i = 0; i < totalColumns; i++)
		{
			output += "\\multicolumn{";
			if(i < columns.length)
				output += multiColumns[i];
			else
				output += "1";
			output += "}{|c|}{$";
			if(i < columns.length)
				output +=  super.getColumnName(columns[i]);
			output += "$}";
			if(i != totalColumns - 1)
				output += " & \n";
			else
				output += "\\\\ \n";
		}
		output += "\\hline \n";
		
		/** Write the content */
		Object oldLevel = null;
		for (int i = 0; i < super.getRowCount(); i++)
		{
			if(i == 0 || !oldLevel.equals(super.getValueAt(i,0)))
			{
				output += "\\hline \n";
				oldLevel = super.getValueAt(i,0);
			}
			for (int j = 0; j < totalColumns; j++)
			{
				if(j < columns.length)
				{
					if(multiColumns[j] > 1)
					{
						String dataString = (String) super.getValueAt(i,columns[j]);
						output += dataString.replace(" ", " & ");
					}
					else
						output += super.getValueAt(i,columns[j]);
				}
				
				if(j != totalColumns - 1)
					output += " & ";
				else
					output += "\\\\ \n";
			}
		}
		
		/** Write the footer */
		output += "\\hline \n";
		output += "\\end{longtable}\n";
		
		return output;
	}
}
