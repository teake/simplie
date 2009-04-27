/*
 * Weight.java
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

package edu.simplie.algebra;

import edu.simplie.Helper;

/**
 * An object for storing weight information: 
 * the dynkin labels, its multiplicity, and its height.
 *
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class Weight
{
	/** The dynkin labels of this weight. */
	public final int[] dynkinLabels;
	/** Indicates whether this weight is domimant or not. */
	public final boolean isDominant;
	/** The root multiplicity. */
	private long	mult;
	/** Boolean indicating wether or not the multiplicity already has been set. */
	private boolean multSet;
	/** The depth of the root. */
	private int		depth;
	
	
	/** 
	 * Creates a new instance of Weight
	 *
	 * @param dynkinLabels	The dynkin labels of the weight.
	 */
	public Weight(int[] dynkinLabels)
	{
		this.dynkinLabels			= dynkinLabels.clone();
		this.mult					= 1;
		this.multSet				= false;
		this.depth					= 0;
		
		boolean dominant = true;
		for (int i = 0; i < dynkinLabels.length; i++)
		{
			if(dynkinLabels[i] < 0)
			{
				dominant = false;
				break;
			}			
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
	 * Overrides default equals() method.
	 * Weights are equal if their Dynkin labels are equal.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		
		Weight weight = (Weight) obj;
		
		if(dynkinLabels.length != weight.dynkinLabels.length)
			return false;
		
		for (int i = 0; i < dynkinLabels.length; i++)
		{
			if(dynkinLabels[i] != weight.dynkinLabels[i])
				return false;
		}
		
		return true;
	}
	
	/** Returns a hashcode based on the dynkin labels. */
	@Override
	public int hashCode()
	{
		int hash = 0;
		int len = dynkinLabels.length;
		for ( int i=0; i<len; i++ )
		{
			hash <<= 1;
			if ( hash < 0 )
			{
				hash |= 1;
			}
			hash ^= dynkinLabels[ i ];
		}
		return hash;
	}
	
	@Override
	public String toString()
	{
		return "depth: " + getDepth() + 
				", labels: " + Helper.intArrayToString(dynkinLabels) + 
				", mult: " + getMult();
	}
}
