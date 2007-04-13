/*
 * CWeight.java
 *
 * Created on 11 april 2007, 17:42
 *
 */

package tan.leveldecomposition.group;

import tan.leveldecomposition.math.*;
import tan.leveldecomposition.*;

/**
 *
 * @author Teake Nutma
 */
public class CWeight
{
	public final int[] dynkinLabels;
	
	private	int[]	simpRootSubtractable;
	private long	mult;
	private int		depth;
	
	
	/** Creates a new instance of CWeight */
	public CWeight(int[] dynkinLabels)
	{
		this.dynkinLabels			= dynkinLabels.clone();
		this.simpRootSubtractable	= new int[dynkinLabels.length];
		this.mult					= 1;
		this.depth					= 0;
	}
	
	public long getMult()
	{
		return mult;
	}
	public void setMult(long mult)
	{
		this.mult = mult;
	}
	
	public int getDepth()
	{
		return depth;
	}
	public void setDepth(int depth)
	{
		this.depth = depth;
	}
	
	public int getSimpRootSubtractable(int index)
	{
		return simpRootSubtractable[index];
	}
	public void setSimpRootSubtractable(int index, int times)
	{
		simpRootSubtractable[index] = times;
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
		return "depth: " + getDepth() + ", labels: " + Globals.intArrayToString(dynkinLabels) + ", mult: " + getMult();
	}
}
