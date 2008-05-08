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

package edu.rug.hep.simplie.algebra;

import edu.rug.hep.simplie.Helper;
import Jama.Matrix;
import java.util.Arrays;

/**
 * Given a Cartan matrix, this class creates an object which has most properties of a simple Lie algebra:
 * rank, dimension, and a root system, to name a few.
 *
 * @see CRootSystem
 * @author Teake Nutma
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

	
	public CartanMatrix(int[][] A)
	{
		this.A = Helper.cloneMatrix(A);
		
		// Convert the Cartan matrix to a matrix object.
		Matrix matrix = new Matrix(A.length,A.length);
		for(int i=0; i<A.length; i++)
		{
			for(int j=0; j<A.length; j++)
			{
				matrix.set(i,j,A[i][j]);
			}
		}
		// Do some preliminary checks.
		// Assume that A is a square matrix.
		if(A.length == 0)
		{
			rank	= 0;
			rankA	= 0;
			det		= 0;
		}
		else
		{
			rank	= A.length;
			rankA	= matrix.rank();
			det		= (int) Math.round(matrix.det());
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
					if(sameCartanMatrices(this.A,generateTypeMatrix(rank,j,serie)))
					{
						tType = serie;
						tExtended = j;
						break loopToBreak;
					}
				}
			}
		}
		type		= tType;
		extended	= tExtended;
		simpleRank	= rank - extended;
	}
	
	public int get(int i, int j)
	{
		return A[i][j];
	}
	
	private String getType(int i)
	{
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
	/**
	 * Determines whether two Cartan matrix are equivalent.
	 * That is, after arbitrary permutations of rows and columns,
	 * the matrices should be identical.
	 * Instead of doing the actual permutations, a couple of permutation-invariant
	 * characteristics of both matrices are calculated and compared.
	 *
	 * @param	matrix1	The first Cartan matrix to compare against the second.
	 * @param	matrix2	The second Cartan matrix to compare against the first.
	 * @return			True is the Cartan matrices are equivalent, false otherwise.
	 */
	public static boolean sameCartanMatrices(int[][] matrix1, int[][] matrix2)
	{
		if(matrix1 == null || matrix2 == null)
			return false;
		if(matrix1.length != matrix2.length)
			return false;
		
		Matrix A = new Matrix(matrix1.length,matrix1.length);
		Matrix B = new Matrix(matrix1.length,matrix1.length);
		
		// Calculate the norms (the sum of all matrix entries)
		int normA = 0;
		int normB = 0;
		// Calculate the number of nodes which have a given number of connections
		int[] nodeConnA = new int[matrix1.length];
		int[] nodeConnB = new int[matrix1.length];
		for (int i = 0; i < nodeConnB.length; i++)
			nodeConnB[i] = nodeConnA[i] = 0;
		
		// Calculate the sum of each column and sort these later on
		int[] columnSums1 = new int[matrix1.length];
		int[] columnSums2 = new int[matrix1.length];

		for (int i = 0; i < matrix1.length; i++)
		{
			int connA = 0;
			int connB = 0;
			
			columnSums1[i] = 0;
			columnSums2[i] = 0;
			
			for (int j = 0; j < matrix1.length; j++)
			{
				A.set(i,j,matrix1[i][j]);
				B.set(i,j,matrix2[i][j]);
				normA += matrix1[i][j];
				normB += matrix2[i][j];
				if(i != j)
				{
					if(matrix1[i][j] != 0)
						connA++;
					if(matrix2[i][j] != 0)
						connB++;
					columnSums1[i] += matrix1[i][j];
					columnSums2[i] += matrix2[i][j];
				}
			}
			nodeConnA[connA]++;
			nodeConnB[connB]++;
			
		}
		
		Arrays.sort(columnSums1);
		Arrays.sort(columnSums2);
		
		if(!Arrays.equals(columnSums1, columnSums2))
			return false;
		
		if(normA != normB)
			return false;
		
		for (int i = 0; i < nodeConnB.length; i++)
		{
			if(nodeConnB[i] != nodeConnA[i])
				return false;			
		}

		
		if(Math.round(A.det()) != Math.round(B.det()))
			return false;
		
		if(A.rank() != B.rank())
			return false;
		
		if(Math.round(100 * A.normF()) != Math.round(100 * B.normF()))
			return false;
		
		return true;
	}	
}
