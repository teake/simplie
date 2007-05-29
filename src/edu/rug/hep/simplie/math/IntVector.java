/*
 * IntVector.java
 *
 * Created on 29 mei 2007, 11:14
 *
 */

package edu.rug.hep.simplie.math;

/**
 *
 * @author Teake Nutma
 */
public class IntVector
{
	public final int[] vector;
	
	/** Creates a new instance of IntVector */
	public IntVector(int[] vector)
	{
		this.vector = vector.clone();
	}
	
	public IntVector(int rank)
	{
		int[] tempVector = new int[rank];
		for (int i = 0; i < rank; i++)
		{
			tempVector[i] = 0;
		}
		vector = tempVector;
	}
	
	public IntVector plus(IntVector intVector)
	{
		int[] newVector = new int[vector.length];
		for (int i = 0; i < vector.length; i++)
		{
			newVector[i] = vector[i] + intVector.vector[i];
		}
		return new IntVector(newVector);
	}
	
	public IntVector minus(IntVector intVector)
	{
		int[] newVector = new int[vector.length];
		for (int i = 0; i < vector.length; i++)
		{
			newVector[i] = vector[i] - intVector.vector[i];
		}
		return new IntVector(newVector);
	}
	
	public IntVector div(int factor)
	{
		int[] newVector = new int[vector.length];
		for (int i = 0; i < vector.length; i++)
		{
			if(vector[i] % factor != 0)
				return null;
			else
				newVector[i] = vector[i] / factor;
		}
		return new IntVector(newVector);
	}
	
	public IntVector times(int factor)
	{
		int[] newVector = new int[vector.length];
		for (int i = 0; i < vector.length; i++)
		{
			newVector[i] = vector[i] * factor;
		}
		return new IntVector(newVector);
	}
	
	
	/**
	 * Overrides default equals() method.
	 */
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		
		IntVector compareVector = (IntVector) obj;
		
		if(vector.length != compareVector.vector.length)
			return false;
		
		for (int i = 0; i < vector.length; i++)
		{
			if(vector[i] != compareVector.vector[i])
				return false;
		}
		
		return true;
	}
	
	public String toString()
	{
		String output = new String();
		for (int i = 0; i < vector.length; i++)
		{
			output += vector[i];
			if(i != vector.length - 1)
				output += " ";
		}
		return output;
	}
}
