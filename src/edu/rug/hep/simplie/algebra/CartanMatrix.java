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
import edu.rug.hep.simplie.math.fraction;

import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import Jama.Matrix;

/**
 * Given a Cartan matrix, this class creates an object which has most properties of a simple Lie algebra:
 * rank, dimension, and a root system, to name a few.
 *
 * @see CRootSystem
 * @author Teake Nutma
 */
public class CartanMatrix
{
	public final int det;
	public final int rank;
	public final int rankA;
	public final int[] norms;
	
	private float[] D;
	
	private final int[][] A;
	
	public CartanMatrix(int[][] A)
	{
		this.A = A.clone();
		
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
		
		// Set D, the diagonal matrix that symmetrizes A.
		D = new float[rank];
		float smallest = Float.MAX_VALUE;
		for(int i = 0 ; i < rank; i++)
		{
			for(int j = 0; j < rank; j++)
			{
				if(A[i][j] != 0 && j != i)
				{
					if(D[j] == 0)
					{
						if(D[i] == 0) D[i] = 1;
						D[j] = D[i] * A[j][i] / A[i][j];
					}
					if(D[i] == 0)
					{
						if(D[j] == 0) D[j] = 1;
						D[i] = D[j] * A[i][j] / A[j][i];
					}
				}
			}
			if(D[i] == 0) D[i] = 1;
			smallest = (smallest > D[i]) ? D[i] : smallest;
		}

		// Set the norms correctly normalized.
		norms = new int[rank];
		float coefficient = 1 / smallest;
		boolean normsOK;
		do
		{
			normsOK = true;
			coefficient *= 2;
			for(int i = 0; i < rank; i++)
			{
				// Make sure that all the root norms are multiples of two. This is 
				// necessary for making sure that roots are integer multiples of 
				// their coroots. If this is not the case, innerproducts will not 
				// always be integers, and all hell will break loose.
				if(D[i] * coefficient % 2 != 0)
					normsOK = false;
				norms[i] = (int) (D[i] * coefficient / 2);
			}
		} while(!normsOK);

		for(int i = 0; i < rank; i++)
		{
			System.out.println(norms[i]);
		}
	
	}
	
	public int get(int i, int j)
	{
		return A[i][j];
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
	public static int[][] getType(int rank, int extended, String type)
	{
		int[][] matrix = new int[rank][rank];
		int simpleRank = rank - extended;
		
		if(simpleRank <= 0)
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
				if(simpleRank == 1)
					matrix[0][1] = matrix[1][0] = -2;
				else
					matrix[0][simpleRank] = matrix[simpleRank][0] = -1;
			}
			return matrix;
		}
		
		if(type.equals("B") && simpleRank > 1)
		{
			matrix[1][0] = -2;
			if(extended > 0)
			{
				matrix[simpleRank][simpleRank-1] = matrix[simpleRank-1][simpleRank] = 0;
				matrix[simpleRank][simpleRank-2] = matrix[simpleRank-2][simpleRank] = -1;
			}
			return matrix;
		}
		
		if(type.equals("C") && simpleRank > 1)
		{
			matrix[0][1] = -2;
			if(extended > 0)
				matrix[simpleRank][simpleRank-1] = -2;
			return matrix;
		}
		
		if(type.equals("D") && simpleRank > 3)
		{
			matrix[0][1] = matrix[1][0] = 0;
			matrix[0][2] = matrix[2][0] = -1;
			if(extended > 0)
			{
				matrix[simpleRank][simpleRank-1] = matrix[simpleRank-1][simpleRank] = 0;
				matrix[simpleRank][simpleRank-2] = matrix[simpleRank-2][simpleRank] = -1;
			}
			return matrix;
		}
		
		if(type.equals("E") && simpleRank > 5 && simpleRank < 9)
		{
			matrix[0][3] = matrix[3][0] = -1;
			matrix[0][1] = matrix[1][0] = 0;
			if(extended > 0)
			{
				if(simpleRank == 6)
				{
					matrix[0][simpleRank] = matrix[simpleRank][0] = -1;
					matrix[simpleRank][simpleRank-1] = matrix[simpleRank-1][simpleRank] = 0;
				}
				if(simpleRank == 7)
				{
					matrix[1][simpleRank] = matrix[simpleRank][1] = -1;
					matrix[simpleRank][simpleRank-1] = matrix[simpleRank-1][simpleRank] = 0;
				}
				
			}
			return matrix;
		}
		
		if(type.equals("F") && simpleRank == 4)
		{
			matrix[1][2] = -2;
			return matrix;
		}
		
		if(type.equals("G") && simpleRank == 2)
		{
			matrix[1][0] = -3;
			return matrix;
		}
		
		return null;
	}
	
}
