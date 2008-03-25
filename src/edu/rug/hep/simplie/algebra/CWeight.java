/*
 * CWeight.java
 *
 * Created on 11 april 2007, 17:42
 *
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

package edu.rug.hep.simplie.algebra;

import edu.rug.hep.simplie.Helper;

/**
 * An object for storing weight information: 
 * the dynkin labels, its multiplicity, and its height.
 *
 * @author Teake Nutma
 */
public class CWeight
{
	/** The dynkin labels of this weight. */
	public final int[] dynkinLabels;
	/** Indicates whether this weight is domimant or not. */
	public final boolean isDominant;
	
	/** Integer array indicating how many times we can subtract simple roots from this weight. */
	private	int[]	simpRootSubtractable;
	/** The root multiplicity. */
	private long	mult;
	/** Boolean indicating wether or not the multiplicity already has been set. */
	private boolean multSet;
	/** The depth of the root. */
	private int		depth;
	
	
	/** 
	 * Creates a new instance of CWeight
	 *
	 * @param dynkinLabels	The dynkin labels of the weight.
	 */
	public CWeight(int[] dynkinLabels)
	{
		this.dynkinLabels			= dynkinLabels.clone();
		this.mult					= 1;
		this.multSet				= false;
		this.depth					= 0;
		this.simpRootSubtractable	= new int[dynkinLabels.length];
		
		boolean dominant = true;
		for (int i = 0; i < dynkinLabels.length; i++)
		{
			simpRootSubtractable[i] = Math.max(dynkinLabels[i],0);
			if(dynkinLabels[i] < 0)
				dominant = false;
		}
		this.isDominant = dominant;
	}
	
	/** Returns the multiplicity of the weight. */
	public long getMult()
	{
		return mult;
	}
	
	/** Sets the multiplicity of the weight. Can only be set once. */
	public void setMult(long mult)
	{
		if(multSet)
			return;
		this.mult = mult;
		this.multSet = true;
	}
	
	/** Returns the depth of this weight. */
	public int getDepth()
	{
		return depth;
	}
	
	/** Sets the depth of the weight. */
	public void setDepth(int depth)
	{
		this.depth = depth;
	}
	
	/**
	 * How many times can we subtract a simple root from this weight?
	 *
	 * @param	index	The index of the simple root.
	 * @return			An integer representing how many times the simple root can be subtracted.	 
	 */
	public int getSimpRootSubtractable(int index)
	{
		return simpRootSubtractable[index];
	}
	
	/** 
	 * How many times can we subtract the simple roots from this weight?
	 *
	 * @return	An integer array representing how many times the simple roots can be subtracted.
	 */
	public int[] getSimpRootSubtractable()
	{
		return simpRootSubtractable.clone();
	}
	
	/** 
	 * Sets how many times can we subtract the simple roots from this weight.
	 * 
	 * @param newMinimumValues	The new minimum values. If the old values were
	 *							bigger, they won't get overridden.
	 */
	public void setSimpRootSubtractable(int[] newMinimumValues)
	{
		for (int i = 0; i < simpRootSubtractable.length; i++)
		{
			simpRootSubtractable[i] = Math.max(simpRootSubtractable[i], newMinimumValues[i]);
		}
	}
	
	/**
	 * Overrides default equals() method.
	 * Weights are equal if their Dynkin labels are equal.
	 */
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		
		CWeight weight = (CWeight) obj;
		
		if(dynkinLabels.length != weight.dynkinLabels.length)
			return false;
		
		for (int i = 0; i < dynkinLabels.length; i++)
		{
			if(dynkinLabels[i] != weight.dynkinLabels[i])
				return false;
		}
		
		return true;
	}
	
	public String toString()
	{
		return "depth: " + getDepth() + 
				", labels: " + Helper.intArrayToString(dynkinLabels) + 
				", mult: " + getMult() +
				", simp subtractable: " + Helper.intArrayToString(simpRootSubtractable);
	}
}
