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
 * rank, dimension, and a root system, to name a few.
 *
 * @see CRootSystem
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class CartanMatrix
{
	public static final String[] series	= { "A", "B", "C", "D", "E", "F", "G" };
	public static final String unknown	= "Unknown";
	public static final String empty	= "Empty";
	
	public final int det;
	public final int rank;
	public final int rankA;

	public final String	type;
	public final int extended;
	public final int simpleRank;
	
	private final int[][] A;
	private final int[][] C;
	
	public CartanMatrix(int[][] A)
	{
		this.A = Helper.cloneMatrix(A);
		this.C = characteristicMatrix(A);
		
		// Do some preliminary checks.
		// Assume that A is a square matrix.
		if(A.length == 0)
		{
			this.rank	= 0;
			this.rankA	= 0;
			this.det	= 0;
		}
		else
		{
			Matrix matrix = Helper.intArrayToMatrix(A);
			this.rank	= A.length;
			this.rankA	= matrix.rank();
			this.det	= (int) Math.round(matrix.det());
		}
		
		// Determine the type etc.
		int	tExtended = 0;
		String tType;
		if(rank == 0)
		{
			tType = empty;
		}
		else
		{
			tType = unknown;
			loopToBreak:
			for (int j = 0; j < rank; j++)
			{
				for(String serie : series)
				{
					if(equivalentTo(generateTypeMatrix(rank,j,serie)))
					{
						tType = serie;
						tExtended = j;
						break loopToBreak;
					}
				}
			}
		}
		this.type		= tType;
		this.extended	= tExtended;
		this.simpleRank	= rank - extended;
	}
	
	public int get(int i, int j)
	{
		return A[i][j];
	}
	
	private String getType(int i)
	{
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
	
	public String getType()
	{
		return getType(0);
	}

	public String getTypeTeX()
	{
		return getType(1);
	}
	
	public String getTypeHTML()
	{
		return getType(2);
	}
	
	/**
	 * Returns a Cartan matrix of the given rank and type.
	 *
	 * @param	 rank		The rank of the matrix to be returned.
	 * @param	 extended	The number of times the matrix is extended.
	 *						(1=affine extension,2=over extended, 3=very extended, etc).
	 * @param	 type		The type of the Cartan matrix, "A", "B", etc.
	 * @return				The cartan matrix of the given rank, or null if the type is illegal.
	 */
	public int[][] generateTypeMatrix(int rank, int extended, String type)
	{
		int[][] matrix = new int[rank][rank];
		int simpRank = rank - extended;
		
		if(simpRank <= 0)
			return null;
		
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
		
		return null;
	}
	
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
	
	
	public boolean equivalentTo(int[][] cm)
	{
		if(cm == null || rank != cm.length)
			return false;
		
		Matrix m = Helper.intArrayToMatrix(cm);
		if(det != Math.round(m.det()) || rankA != m.rank())
			return false;
		
		if(!Helper.sameMatrices(C,characteristicMatrix(cm)))
			return false;
		
		return true;
	}	
}
