/*
 * Root.java
 *
 * Created on 26 maart 2007, 17:35
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
import edu.simplie.math.fraction;
import java.io.Serializable;

/**
 * A class for storing root information:
 * the root vector, its (co-)multiplicity, and its height.
 *
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class Root implements Serializable
{
	//TODO: make variables mult & coMult private and get/settable only via method
	
	/** The root vector. */
	public final int[] vector;
	/** The norm of the root (the innerproduct with itself). */
	public short norm;
	/** The root multiplicity. */
	public long	mult;
	/** Sum over multiplicities of roots that are fractionals of this one (used in Peterson's formula). */
	public fraction	coMult;
	
	/**
	 * Creates a new instance of Root.
	 *
	 * @param	rootVector	Integer array representing the root vector
	 *						from which we should construct the root.
	 */
	public Root(int[] rootVector)
	{
		vector	= rootVector.clone();
		mult	= 0;
	}
	
	/**
	 * Returns the height of the root,
	 * that is the sum of the components of its vector.
	 *
	 * @return The height of the root.
	 */
	public int height()
	{
		int height = 0;
		for (int i = 0; i < vector.length; i++)
		{
			height += vector[i];
		}
		return height;
	}
	
	/**
	 * Returns the highest (biggest) component of the root vector.
	 *
	 * @return The highest component of the root.
	 */
	public int highest()
	{
		int	highest = 0;
		for (int i = 0; i < vector.length; i++)
		{
			if(vector[i] > highest)
				highest = vector[i];
		}
		return highest;
	}
	
	
	/**********************************
	 * "New roots from this root"
	 *  functions listed below.
	 **********************************/
	

	/**
	 * Divides the root vector with an integral value and returns a new root with that vector.
	 *
	 * @param	factor	The value with which to divide the root vector.
	 * @return			A new root with divided vector. Returns null if not all the root vector values are integral.
	 */
	public Root div(int factor)
	{
		int[] newVector = new int[vector.length];
		for (int i = 0; i < vector.length; i++)
		{
			if(vector[i] % factor != 0)
				return null;
			else
				newVector[i] = vector[i] / factor;
		}
		return new Root(newVector);
	}
	
	/**
	 * Multiplies the root vector with an integral value and returns a new root with that vector.
	 *
	 * @param	factor	The value with which to multiply the root vector.
	 * @return			A new root with multiplied vector.
	 */
	public Root times(int factor)
	{
		int[] newVector = new int[vector.length];
		for (int i = 0; i < vector.length; i++)
		{
			newVector[i] = vector[i] * factor;
		}
		return new Root(newVector);
	}
	
	
	/**
	 * Overrides default equals() method.
	 * Roots are equal if their root vectors are equal.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		
		Root compareRoot = (Root) obj;
		
		if(vector.length != compareRoot.vector.length)
			return false;
		
		for (int i = 0; i < vector.length; i++)
		{
			if(vector[i] != compareRoot.vector[i])
				return false;
		}
		
		return true;
	}
	
	/** Returns a hashcode based on the vector of the root. */
	@Override
	public int hashCode()
	{
		int hash = 0;
		int len = vector.length;
		for ( int i=0; i<len; i++ )
		{
			hash <<= 1;
			if ( hash < 0 )
			{
				hash |= 1;
			}
			hash ^= vector[ i ];
		}
		return hash;
	}
	
	/** Overrides default toString method */
	@Override
	public String toString()
	{
		String output = "height: " + height() +
				", vector: " + Helper.intArrayToString(vector) +
				", norm: " + norm +
				", mult: " + mult;
		
		if(coMult != null)
			output += ", coMult: " + coMult.toString();
		
		return output;
	}
	
}
