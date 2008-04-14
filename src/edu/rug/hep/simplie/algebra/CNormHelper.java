/*
 * CNormHelper.java
 *
 * Created on June 7, 2007, 5:22 PM
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

/**
 * Helper-object for ordering simple roots according to their norm.
 * 
 * @author Teake Nutma
 */
public class CNormHelper implements Comparable<CNormHelper>
{
	/** The norm of the root (used for ordering). */
	public double norm;
	/** Index to keep track of the original order of the root.  */ 
	public final int index;
	
	/** Creates a new instance of CNormHelper */
	public CNormHelper(int index, double norm)
	{
		this.index = index;
		this.norm = norm;
	}
	
	/** Compares normHelpers according to their norm */
	public int compareTo(CNormHelper compareNH)
	{
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		
		if(this.norm > compareNH.norm) return AFTER;
		if(this.norm < compareNH.norm) return BEFORE;
		
		return EQUAL;
	}
	
	/** NormHelpers are equal to each other when their index is the same. */
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		CNormHelper compareNH = (CNormHelper) obj;
		if(compareNH.index == this.index)
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 17 * hash + this.index;
		return hash;
	}
}
