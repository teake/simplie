/*
 * CRoot.java
 *
 * Created on 26 maart 2007, 17:35
 *
 */

package tan.leveldecomposition.group;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

/**
 *
 * @author Teake Nutma
 */
public class CRoot
{
	/** The root vector. */
	public  int[]	vector;
	/** The root multiplicity. */
	public  long		mult;
	/** Sum over multiplicities of roots that are fractionals of this one (used in Peterson's formula). */
	public  Fraction	c_mult;
	/** The height of the root (lazily calculated). */
	private Integer	height;
	
	/**
	 * Creates a new instance of CRoot.
	 *
	 * @param	rootVector   Integer array representing the root vector
	 *			     from which we should construct the root.
	 */
	public CRoot(int[] rootVector)
	{
		vector = rootVector;
	}
	
	/**
	 * Creates a new instance of CRoot with a zero vector.
	 *
	 * @param	rank	 The size of the zero-valued vector.
	 */
	public CRoot(int rank)
	{
		vector = new int[rank];
		for (int i = 0; i < rank; i++)
		{
			vector[i] = 0;
		}
		height = 0;
	}
	
	/**
	 * Lazily determines the height of the root.
	 *
	 * @return The height of the root.
	 */
	public int height()
	{
		if(height == null)
		{
			height = 0;
			for (int i = 0; i < vector.length; i++)
			{
				height += vector[i];
			}
		}
		return height;
	}
	
	
	/**********************************
	 * "New roots from this root"
	 *  functions listed below.
	 **********************************/
	
	/**
	 * Adds the root vector of "root" to this one and returns the corresponding new root.
	 *
	 * @param	CRoot	The root whose vector we should add to this one.
	 * @return		A new root with summed vector.
	 */
	public CRoot plus(CRoot root)
	{
		int[] newVector = new int[vector.length];
		for (int i = 0; i < vector.length; i++)
		{
			newVector[i] = root.vector[i] + vector[i];
		}
		return new CRoot(newVector);
	}
	
	/**
	 * Subtracts the root vector of "root" to this one and returns the corresponding new root.
	 *
	 * @param	CRoot	The root whose vector we should subtract from this one.
	 * @return		A new root with subtracted vector.
	 */
	public CRoot minus(CRoot root)
	{
		int[] newVector = new int[vector.length];
		for (int i = 0; i < vector.length; i++)
		{
			newVector[i] = vector[i] - root.vector[i];
		}
		return new CRoot(newVector);
	}
	
	/**
	 * Divides the root vector with an integral value and returns a new root with that vector.
	 *
	 * @param	factor	The value with which to divide the root vector.
	 * @return		A new root with divided vector. Returns null if not all the root vector values are integral.
	 */
	public CRoot div(int factor)
	{
		int[] newVector = new int[vector.length];
		for (int i = 0; i < vector.length; i++)
		{
			if(vector[i] % factor != 0)
				return null;
			else
				newVector[i] = vector[i] / factor;
		}
		return new CRoot(newVector);
	}
	
	
	
	
	
	/**
	 * Overrides default equals() method.
	 * Roots are equal if their root vectors are equal.
	 */
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		
		CRoot compareRoot = (CRoot) obj;
		
		if(vector.length != compareRoot.vector.length)
			return false;
		
		for (int i = 0; i < vector.length; i++)
		{
			if(vector[i] != compareRoot.vector[i])
				return false;
		}
		
		return true;
	}
	
}
