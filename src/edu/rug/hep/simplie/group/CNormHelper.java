/*
 * CNormHelper.java
 *
 * Created on June 7, 2007, 5:22 PM
 *
 */

package edu.rug.hep.simplie.group;

/**
 *
 * @author Teake Nutma
 */
public class CNormHelper implements Comparable<CNormHelper>
{
	public final double norm;
	public final int index;
	
	/** Creates a new instance of CNormHelper */
	public CNormHelper(int index, double norm)
	{
		this.index = index;
		this.norm = norm;
	}
	
	public int compareTo(CNormHelper compareNH)
	{
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		
		if(this.norm > compareNH.norm) return AFTER;
		if(this.norm < compareNH.norm) return BEFORE;
		
		return EQUAL;
	}
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
}
