/*
 * UISpinner.java
 *
 * Created on 22 maart 2007, 10:01
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
package edu.simplie.ui.reusable;

/**
 *
 * @author  Teake Nutma
 * @version $Revision$, $Date$
 */
public class UISpinner extends javax.swing.JSpinner
{
	private Integer minValue = null;
	private Integer maxValue = null;
	
	public UISpinner()
	{
		setModel();
	}
	
	@Override
	public Integer getValue()
	{
		return (Integer) super.getValue();
	}
	
	public void setValue(int value)
	{
		super.setValue(value);
	}
	
	public void setMinValue(int value)
	{
		minValue = value;
		setModel();
	}
	
	public void setMaxValue(int value)
	{
		maxValue = value;
		setModel();
	}
	
	private void setModel()
	{
		super.setModel(new javax.swing.SpinnerNumberModel(getValue(),minValue,maxValue,new Integer(1)));
	}
	
}
