/*
 * CartanMatrix.java
 *
 * Created on 26 april 2008, 16:18
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
import Jama.Matrix;
import java.util.Arrays;

/**
 * Given a Cartan matrix, this class creates an object which has most properties of a simple Lie algebra:
 * size, dimension, and a root system, to name a few.
 *
 * @see RootSystem
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class CartanMatrix
{
	/** All the classical series of Lie algebras. */
	public static final String[] series	= { "A", "B", "C", "D", "E", "F", "G" };
	/** If a Cartan matrix does not belong to a series, then it's unknown. */
	public static final String unknown	= "Unknown";
	/** The empty Cartan matrix. */
	public static final String empty	= "Empty";
	
	/** The determinant of the Cartan matrix. */
	public final int det;
	/** The size of the Cartan matrix, i.e. the rank of the associated Lie algebra. */
	public final int size;
	/** The rank of the Cartan matrix as a matrix. */
	public final int rank;

	/** The type of Cartan matrix. */
	private String	type;
	/** The number of times this is an extended matrix (1 = extended, 2 = over extended, 3 = very extended, etc). */
	private int extended;
	/** The simple rank equals size minus extended. */
	private int simpleRank;
	
	/** The actual Cartan matrix. */
	private final int[][] A;
	/** The charateristic matrix. */
	private final int[][] C;
	
	public CartanMatrix(int[][] A)
	{
		this.A = Helper.cloneMatrix(A);
		this.C = characteristicMatrix(A);
		
		// Do some preliminary checks.
		// Assume that A is a square matrix.
		if(A.length == 0)
		{
			this.size	= 0;
			this.rank	= 0;
			this.det	= 0;
		}
		else
		{
			Matrix matrix = Helper.intArrayToMatrix(A);
			this.size	= A.length;
			this.rank	= matrix.rank();
			this.det	= (int) Math.round(matrix.det());
		}
	}
	
	/**
	 * Returns the (i,j)th component of the Cartan matrix.
	 * 
	 * @param i	 The row.
	 * @param j	 The column
	 * @return	 The value at (i,j).
	 */
	public int get(int i, int j)
	{
		return A[i][j];
	}
	
	private String getType(int i)
	{
		if(type == null)
		{
			// Determine the type etc.
			if(size == 0)
			{
				type = empty;
			}
			else
			{
				type = unknown;
				loopToBreak:
				for (int j = 0; j < size; j++)
				{
					for(String serie : series)
					{
						try
						{
							if(equivalentTo(generateTypeMatrix(size,j,serie)))
							{
								type = serie;
								extended = j;
								break loopToBreak;
							}
						}
						catch(Exception e)
						{
							continue;
						}

					}
				}
			}
			simpleRank = size - extended;
		}
		
		if(type.equals(empty) || type.equals(unknown))
			return type;
		
		String output = type;
		if(i == 1) output += "_{";
		if(i == 2) output += "<sub>";
		output += simpleRank;
		if(i == 1) output += "}^{";
		if(i == 2) output += "</sub><sup>";
		for(int j = 0; j < extended; j++)
		{
			output += "+";			
		}
		if(i == 1) output += "}";
		if(i == 2) output += "</sup>";
		return output;
	}
	
	/**
	 * Returns the type of the Cartan matrix, e.g. "A10++".
	 * 
	 * @return	 A string representing the type.
	 */
	public String getType()
	{
		return getType(0);
	}

	/**
	 * The same as getType(), only now with TeX markup, e.g. "A_{10}^{++}"
	 * 
	 * @return	 A string with TeX markup.
	 */
	public String getTypeTeX()
	{
		return getType(1);
	}
	
	/**
	 * The same as getType(), only now with HTML markup, e.g. "A<sub>10</sub><sup>++</sup>"
	 * @return
	 */
	public String getTypeHTML()
	{
		return getType(2);
	}
	
	/**
	 * Returns a Cartan matrix of the given size and type.
	 *
	 * @param	 rank		 The rank of the matrix to be returned.
	 * @param	 extended	 The number of times the matrix is extended.
	 *						 (1=affine extension,2=over extended, 3=very extended, etc).
	 * @param	 type		 The type of the Cartan matrix, "A", "B", etc.
	 * @return				 The cartan matrix as specified by rank, extended, and type.
	 * @throws	 IllegalArgumentException
	 *						 For any incorrect combination of rank, extended, and type.
	 */
	public int[][] generateTypeMatrix(int rank, int extended, String type)
	{
		int[][] matrix = new int[rank][rank];
		int simpRank = rank - extended;
		
		if(simpRank <= 0)
			throw new IllegalArgumentException("Extended >= rank is not valid.");
		
		// First do the regular A-series matrix.
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				if(i == j)
					matrix[i][j] = 2;
				else if(j == i-1 || j == i+1)
					matrix[i][j] = -1;
				else
					matrix[i][j] = 0;
			}
		}
		
		// And now the deviations from it.
		
		if(type.equals("A"))
		{
			if(extended > 0)
			{
				if(simpRank == 1)
					matrix[0][1] = matrix[1][0] = -2;
				else
					matrix[0][simpRank] = matrix[simpRank][0] = -1;
			}
			return matrix;
		}
		
		if(type.equals("B") && simpRank > 1)
		{
			matrix[1][0] = -2;
			if(extended > 0)
			{
				matrix[simpRank][simpRank-1] = matrix[simpRank-1][simpRank] = 0;
				matrix[simpRank][simpRank-2] = matrix[simpRank-2][simpRank] = -1;
			}
			return matrix;
		}
		
		if(type.equals("C") && simpRank > 1)
		{
			matrix[0][1] = -2;
			if(extended > 0)
				matrix[simpRank][simpRank-1] = -2;
			return matrix;
		}
		
		if(type.equals("D") && simpRank > 3)
		{
			matrix[0][1] = matrix[1][0] = 0;
			matrix[0][2] = matrix[2][0] = -1;
			if(extended > 0)
			{
				matrix[simpRank][simpRank-1] = matrix[simpRank-1][simpRank] = 0;
				matrix[simpRank][simpRank-2] = matrix[simpRank-2][simpRank] = -1;
			}
			return matrix;
		}
		
		if(type.equals("E") && simpRank > 5 && simpRank < 9)
		{
			matrix[0][3] = matrix[3][0] = -1;
			matrix[0][1] = matrix[1][0] = 0;
			if(extended > 0)
			{
				if(simpRank == 6)
				{
					matrix[0][simpRank] = matrix[simpRank][0] = -1;
					matrix[simpRank][simpRank-1] = matrix[simpRank-1][simpRank] = 0;
				}
				if(simpRank == 7)
				{
					matrix[1][simpRank] = matrix[simpRank][1] = -1;
					matrix[simpRank][simpRank-1] = matrix[simpRank-1][simpRank] = 0;
				}
				
			}
			return matrix;
		}
		
		if(type.equals("F") && simpRank == 4)
		{
			matrix[1][2] = -2;
			return matrix;
		}
		
		if(type.equals("G") && simpRank == 2)
		{
			matrix[1][0] = -3;
			return matrix;
		}
		
		throw new IllegalArgumentException(
				"No proper combination of type (" + type + 
				"), rank (" + rank + 
				"), and extended (" + extended + ")");
	}
	
	/**
	 * Calculates the "characteristic matrix" of a given Cartan matrix.
	 * This characteristic matrix is independent of the row and column permutations
	 * of the original Cartan matrix, and is thus a good object for comparing two
	 * Cartan matrices on equivalency.
	 *  
	 * @param cm	 The Cartan matrix for which to calculate the characteristic matrix.
	 * @return		 The characteristic matrix of the Cartan matrix.
	 */
	public int[][] characteristicMatrix(int[][] cm)
	{
		int[][] charM = new int[cm.length][cm.length];
		int[] columnSums = new int[cm.length];
		
		for(int i = 0; i < cm.length; i++)
		{
			int numNB = 0;
			for(int j = 0; j < cm.length; j++)
			{
				columnSums[i] += cm[i][j];
				if(i != j && cm[i][j] != 0)
					numNB++;
			}
			for(int j = 0; j < cm.length; j++)
			{
				if(i != j && cm[i][j] != 0)
				{
					int numNBsNB = 0;
					for(int k = 0; k < cm.length; k++)
					{
						if(j != k && cm[j][k] != 0)
							numNBsNB++;
					}
					charM[numNB][numNBsNB]++;
				}
			}
		}
		Arrays.sort(columnSums);
		charM[0] = columnSums;	
		
		return charM;
	}
	
	/**
	 * <p>This method tries to determine whether the Cartan matrix of this object is
	 * equivalent to another Cartan matrix. The proper way to do is would be to
	 * consider all to permutations of the row and columns of one matrix, and compare
	 * them to the other. Unfortunately this becomes unpractical for big Cartan
	 * matrix, as the number of permutations grows with n! (n being the size of
	 * the matrix).<p>
	 * 
	 * <p>Here instead certain derived objects of the Cartan matrix will be compared,
	 * that are independant of the row and column permutations. They are the size
	 * of the matrix, its determinant, its rank, and its characteristic matrix.<p>
	 * 
	 * @param cm	 The second Cartan matrix to compare this object to.
	 * @return		 True if the both are equivalent (i.e. equal up to permutations),
	 *				 and false if they are not equivalent.
	 */
	public boolean equivalentTo(int[][] cm)
	{
		if(cm == null || size != cm.length)
			return false;
		
		Matrix m = Helper.intArrayToMatrix(cm);
		if(det != Math.round(m.det()) || rank != m.rank())
			return false;
		
		if(!Helper.sameMatrices(C,characteristicMatrix(cm)))
			return false;
		
		return true;
	}	
}
