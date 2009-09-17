/*
 * Algebra.java
 *
 * Created on 26 maart 2007, 17:17
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

import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import Jama.Matrix;
import java.util.HashMap;

/**
 * Given a Cartan matrix, this class creates an object which has most properties of a simple Lie algebra:
 * rank, dimension, and a root system, to name a few.
 *
 * @see RootSystem
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class Algebra
{
	/*********************************
	 * Public properties
	 *********************************/
	
	/** The Cartan matrix of the algebra. */
	public final int[][] A;
	/**
	 * The symmetrized Cartan matrix (the inverse of the quadratic form matrix).
	 * Note that it is not the same as the one in Fuchs & Schweigert,
	 * because they use the normalization a^2 = 2 for the <i>longest</i> root,
	 * and here it is used for the <i>shortest</i> root.
	 */

	/** The inverse of the cartan matrix. */
	public final fraction[][] invA;
	/** The metric on the root space. */
	public final int[][] B;
	/** The quadratic form matrix, which acts as a metric on the weight space. */
	public final fraction[][] G;	
	/** The determinant of the Cartan Matrix. */
	public final int	det;
	/** The rank of the Cartan matrix */
	public final int	rankA;
	/** The rank of the algebra. */
	public final int	rank;
	/** The dimension of the algebra (the number of generators). Only set for finite algebras. */
	public final int	dim;
	/** The Coxeter labels */
	public final int[] coxeterLabels;
	/** The dual Coxeter labels */
	public final int[] dualCoxeterLabels;
	/** The Coxeter number, i.e. the sum of components of the highest root + 1. */
	public final int coxeterNumber;
	/** The dual Coxeter number */
	public final int dualCoxeterNumber;
	/** String value of dim. "Infinite" if the algebra is infinite. */
	public final String	dimension;
	/** The type of the algebra ("A1", "E6", etc) */
	public final String	type;
	/** The same as "type", but now in HTML markup */
	public final String typeHTML;
	/** The same as "type", but now in TeX markup */
	public final String typeTeX;
	/** Boolean indicating whether the algebra is finite or not. */
	public final boolean finite;
	/** Boolean indicating whether the algebra is affine or not. */
	public final boolean affine;
	/** The root system */
	public final RootSystem rs;
	/** The Weyl vector of the algebra */
	public final Weight rho;
	/** Entries of a diagonal matrix that symmetrizes the Cartan matrix. */
	public final int[] d;
	/** List of matrices into which the Cartan matrix factorizes */
	private final ArrayList<CartanMatrix> directProductFactors;
		
	/**********************************
	 * Public methods
	 **********************************/
	
	/**
	 * Creates a new instance of Algebra.
	 * If the algebra is finite, the whole rootsystem is constructed.
	 *
	 * @param A    The Cartan matrix from which to construct the algebra.
	 */
	public Algebra(int[][] A)
	{
		int[] tempCoxeterLabels = new int[0];
		int[] tempDualCoxeterLabels = new int[0];

		// Convert the Cartan matrix to a matrix object.
		Matrix cartanMatrix = Helper.intArrayToMatrix(A);
		// Do some preliminary checks.
		// Assume that A is a square matrix.
		if(A.length == 0)
		{
			rank	= 0;
			rankA	= 0;
			det		= 0;
			affine	= false;
		}
		else
		{
			// If det = 0, the Cartan matrix is affine.
			// We need to enlarge it s.t. it is no longer degenerate.
			if(Math.round(cartanMatrix.det()) != 0)
			{
				affine = false;
				tempCoxeterLabels = new int[A.length];
				tempDualCoxeterLabels = new int[A.length];
			}
			else
			{
				affine = true;
				// First, find the first minimum Coxeter label,
				// which is always normalized to 1.
				tempCoxeterLabels		= Helper.nullEigenVector(cartanMatrix.transpose());
				tempDualCoxeterLabels	= Helper.nullEigenVector(cartanMatrix);
				int index;
				for(index = 0; index < tempCoxeterLabels.length; index++)
				{
					if(tempCoxeterLabels[index] == 1)
						break;
				}
				// Create a new, bigger (and way better) Cartan matrix.
				int[][] newA = new int[A.length + 1][A.length + 1];
				for (int i = 0; i < newA.length; i++)
				{
					for (int j = 0; j < newA.length; j++)
					{
						if(i == A.length || j == A.length)
							newA[i][j] = (i == index || j == index) ? -1 : 0;
						else
							newA[i][j] = A[i][j];
					}
				}
				// And, lastly, copy it onto the old one.
				A = newA;
				cartanMatrix = Helper.intArrayToMatrix(newA);
			}

			rank	= A.length;
			rankA	= cartanMatrix.rank();
			det		= (int) Math.round(cartanMatrix.det());
		}

		this.A		= Helper.cloneMatrix(A);
		this.B		= new int[rank][rank];
		this.invA	= new fraction[rank][rank];
		this.G		= new fraction[rank][rank];
		this.directProductFactors = new ArrayList<CartanMatrix>();
		
		this.d = new int[rank];
		ArrayList<ArrayList<Integer>> indecomposables = new ArrayList<ArrayList<Integer>>();
		HashMap<Integer,Float> norms = new HashMap<Integer,Float>();
				
		// Factorize the Cartan matrix.
		for(int i = 0; i < rank; i++)
		{
			// Don't do anything if this index is already done.
			if(norms.containsKey(i))
				continue;
			
			// Set the first norm to 1.
			float smallestNorm = 1.0f;
			norms.put(i, smallestNorm);
			
			// Add the first index.
			ArrayList<Integer> indecomposable = new ArrayList<Integer>();
			indecomposable.add(i);
			indecomposables.add(indecomposable);
			
			// Walk over this connected piece.
			int indexToAdd;
			do
			{
				indexToAdd = -1;
				loopToBreak:
				for(Integer j : indecomposable)
				{
					// Find every index with a connection to J.
					for(int k = 0; k < rank; k++)
					{
						if(A[j][k] != 0 && !indecomposable.contains(k))
						{
							// Found one that hasn't been added.
							// Set it's norm first.
							float norm = norms.get(j) * A[k][j] / A[j][k];
							norms.put(k, norm);
							smallestNorm = Math.min(norm, smallestNorm);
							
							// Remember to add it later on.
							indexToAdd = k;
							break loopToBreak;
						}
					}
				}
				// Add the newly found connected index.
				if(indexToAdd != -1)
					indecomposable.add(indexToAdd);

			} while(indexToAdd != -1); // Break if there's no new index found.
			
			// Add the cartan matrix of this piece to the direct product factors.
			int[][] pieceMatrix = new int[indecomposable.size()][indecomposable.size()];
			for (int j = 0; j < pieceMatrix.length; j++)
			{
				for (int k = 0; k < pieceMatrix.length; k++)
				{
					pieceMatrix[j][k] = this.A[indecomposable.get(j)][indecomposable.get(k)];
				}
			}
			directProductFactors.add(new CartanMatrix(pieceMatrix));
			
			// Lastly set the simple root norms.
			// Make sure that all the root norms are multiples of two. This is 
			// necessary for making sure that roots are integer multiples of 
			// their coroots. If this is not the case, innerproducts will not 
			// always be integers, and all hell will break loose.
			float coefficient = 1 / (2 * smallestNorm);
			boolean normsOK;
			do
			{
				normsOK = true;
				coefficient *= 2;
				for(Integer j : indecomposable)
				{
					float norm = norms.get(j) * coefficient;
					d[j] = Math.round(norm);
					if(norm % 1 != 0)
						normsOK = false;
				}
			} while(!normsOK);
		}

		// Set the type of algebra.
		String tType		= "";
		String tTypeTeX		= "";
		String tTypeHTML	= "";
		for(int i = 0; i < directProductFactors.size(); i++)
		{
			CartanMatrix cm = directProductFactors.get(i);
			tType		+= cm.getType();
			tTypeTeX	+= cm.getTypeTeX();
			tTypeHTML	+= cm.getTypeHTML();
			if(i < directProductFactors.size() - 1)
			{
				tType		+= " x ";
				tTypeTeX	+= " \\otimes ";
				tTypeHTML	+= " x ";			
			}
		}
		if(directProductFactors.size() == 0)
		{
			type = typeTeX = typeHTML = CartanMatrix.empty;
		}
		else
		{
			type		= tType;
			typeTeX		= "$" + tTypeTeX + "$";
			typeHTML	= "<html>" + tTypeHTML + "</html>";
		}
		
		// Dirty hack to check for infinite algebras that might have positive determinant
		// and infinite direct product algebras that also might have positive determinant.
		// TODO: implement this better.
		if((det > 0 || rank == 0) && !type.contains("Unknown") && !type.contains("+"))
			finite = true;
		else
			finite = false;
		
		// Construct the Weyl vector
		int[] weylLabels = new int[rank];
		for (int i = 0; i < rank; i++)
		{
			weylLabels[i] = 1;
		}
		rho = new Weight(weylLabels);
		

		// Now that the simple roots have been created,
		// we can set the metric on the root space.
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				this.B[i][j] = this.A[i][j] * d[j];
			}
		}
		
		// Set the inverse of the Cartan matrix and the quadratic form matrix if possible.
		if(rank != 0 && rankA == rank)
		{
			Matrix invAm = cartanMatrix.inverse();
			for (int i = 0; i < rank; i++)
			{
				for (int j = 0; j < rank; j++)
				{
					this.invA[i][j]	= new fraction(invAm.get(i,j));
					this.G[i][j]	= invA[i][j].times(d[j]);
				}
			}
		}
		
		// Set up the root system.
		rs = new RootSystem(this);

		// Determine the dimension and the Coxeter number
		int tempCox		= 0;
		int tempDualCox = 0;
		if(finite)
		{
			dim				= 2 * (int) rs.numPosGenerators() + rank;
			dimension		= Helper.intToString(dim);
			Root highestRoot = rs.get(rs.size()-1).iterator().next();
			for(int i = 0; i < rank; i++)
			{
				tempCoxeterLabels[i]		= highestRoot.vector[i];
				tempDualCoxeterLabels[i]	= B[i][i] * highestRoot.vector[i] / highestRoot.norm;
			}
		}
		else
		{
			dim			= 0;
			dimension	= "Infinite";
		}

		tempCox = tempDualCox = 1;
		for (int i = 0; i < tempCoxeterLabels.length; i++)
		{
			tempCox		+= tempCoxeterLabels[i];
			tempDualCox += tempDualCoxeterLabels[i];
		}

		coxeterLabels		= tempCoxeterLabels;
		dualCoxeterLabels	= tempDualCoxeterLabels;
		coxeterNumber		= tempCox;
		dualCoxeterNumber	= tempDualCox;

	}
	
	/**
	 * Determines the dimension of the representation defined by
	 * Dynkin labels associated to the given dynkinLabels.
	 * This is basically an implementation of Weyl's dimensionality formula.
	 *
	 * @param	highestWeightLabels			The Dynkin labels of the representation.
	 * @return								The dimension of the presentation.
	 * @throws	IllegalArgumentException	When the algebra is infinite
	 *										or if the number of weight labels does not match the rank.
	 */
	public long dimOfRep(int[] highestWeightLabels)
	{
		fraction	dimOfRep;
		Weight		highestWeight;
		
		// Preliminary checks.
		if(!finite)
			return 0;

		if(highestWeightLabels.length != rank)
			throw new IllegalArgumentException("Number of weight labels does not match rank.");
		
		dimOfRep		= new fraction(1);
		highestWeight	= new Weight(highestWeightLabels);
		
		for (int i = 1; i < rs.size(); i++)
		{
			Collection roots	= rs.get(i);
			Iterator iterator	= roots.iterator();
			while (iterator.hasNext())
			{
				Root root = (Root) iterator.next();
				int rhoRoot = rho(root);
				dimOfRep.multiply( innerProduct(highestWeight,root) + rhoRoot );
				dimOfRep.divide( rhoRoot );
			}
		}
		
		return dimOfRep.asLong();
	}
	
	/**
	 * Cancels every process going on,
	 * which is currently only the construction of the rootsystem.
	 */
	public void cancelEverything()
	{
		rs.cancelConstruction();
	}
	
	
	/**
	 * Calculate the innerproduct between two roots.
	 *
	 * @param	root1	Root alpha.
	 * @param	root2	Root beta.
	 * @return			The innerproduct (alpha, beta), which is an integer.
	 */
	public int innerProduct(Root root1, Root root2)
	{
		int result = 0;
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				result += B[i][j] * root1.vector[i] * root2.vector[j];
			}
		}
		return result;
	}
	
	/**
	 * Calculate the innerproduct between two weights.
	 *
	 * @param	weight1		Weight lambda.
	 * @param	weight2		Weight mu.
	 * @return				The innerproduct (lambda,mu).
	 */
	public fraction innerProduct(Weight weight1, Weight weight2)
	{
		fraction result = new fraction(0);
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				result.add( G[i][j].times(weight1.dynkinLabels[i] * weight2.dynkinLabels[j]) );
			}
		}
		return result;
	}
	
	/**
	 * Calculate the innerproduct between a weight and a root.
	 * This is the same as the action of the weight on the root, and vice versa.
	 *
	 * @param	weight	Weight lambda.
	 * @param	root	Root alpha.
	 * @return			The innerproduct (lambda,alpha) = lambda(alpha) = alpha(lambda),
	 *					which is an integer.
	 */
	public int innerProduct(Weight weight, Root root)
	{
		int result = 0;
		for (int i = 0; i < rank; i++)
		{
			result += weight.dynkinLabels[i] * root.vector[i] * d[i];
		}
		return result;
	}
	
	/**
	 * Calculate the action of the Weyl vector on a root.
	 *
	 * @param	root	Root alpha.
	 * @return			rho(alhpa), with rho the Weyl vector of this algebra.
	 */
	public int rho(Root root)
	{
		int result = 0;
		for (int i = 0; i < rank; i++)
		{
			result += root.vector[i] * d[i];
		}
		return result;
	}
	
	
	/**
	 * Calculate the height of a weight.
	 *
	 * @param	weightLabels	The dynkinlabels of the weight.
	 * @return					The height of the weight.
	 */
	public fraction weightHeight(int[] weightLabels)
	{
		fraction height = new fraction(0);
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				height.add(invA[j][i].times(weightLabels[j]));
			}
		}
		return height;
	}
	
	/**
	 * Performs a simple Weyl reflection on the dynkin labels of a weight.
	 *
	 * @param	weightLabels	The dynkin labels of the weight.
	 * @param	i				The index of the simple root with which we should reflect.
	 * @return					The dynkin labels of the reflected weight.
	 */
	public int[] simpWeylReflWeight(int[] weightLabels, int i)
	{
		// Do not reflect for imaginary simple roots.
		if(A[i][i] <= 0)
			return weightLabels;

		int[] output = new int[weightLabels.length];
		for (int j = 0; j < output.length; j++)
		{
			output[j] = weightLabels[j] - A[i][j] * weightLabels[i];
		}
		return output;
	}
	
	/**
	 * Performs a simple Weyl reflection on vector of a root.
	 *
	 * @param	rootVector	 The vector of the root we want to reflect.
	 * @param	i			 The i'th simple root in which we will reflect.
	 * @return				 The vector of the reflected root.
	 */
	public int[] simpWeylReflRoot(int[] rootVector, int i)
	{
		// Do not reflect for imaginary simple roots.
		if(A[i][i] <= 0)
			return rootVector;

		int[] output		= rootVector.clone();		
		int[] dynkinLabels	= rootToWeight(rootVector);
		
		output[i] = output[i] - dynkinLabels[i];
		
		return output;
	}

	/**
	 * Returns a matrix M_i such that M_i.v = v', where v is a root vector,
	 * and v' is the vector of the root reflected in the i'th simple root.
	 *
	 * @param	i	 The i'th simple root of which to compute the matrix.
	 * @return		 The matrix M_i as a double[][].
	 */
	public double[][] simpWeylRelfMatrix(int i)
	{
		double[][] relfMatrix = new double[rank][rank];
		for(int j = 0; j < rank; j++)
		{
			for(int k = 0; k < rank; k++)
			{
				relfMatrix[j][k] = 0.0;
				if(j == k)
				{
					relfMatrix[j][k] += 1.0;
				}
				if(j == i)
				{
					relfMatrix[j][k] -= A[k][i];
				}
			}
		}
		return relfMatrix;
	}
	
	
	/**
	 * Returns the Dynkin labels of the weight associated to a vector of the root.
	 * 
	 * @param rootVector	The root vector to be converted into Dynkin labels.
	 * @return				The Dynkin labels of the input root vector.
	 */
	public int[] rootToWeight(int[] rootVector)
	{
		int[] dynkinLabels = new int[rank];
		for (int i = 0; i < rank; i++)
		{
			dynkinLabels[i] = 0;
			for (int j = 0; j < rank; j++)
			{
				dynkinLabels[i] += A[j][i] * rootVector[j];
			}
		}
		return dynkinLabels;
	}

	/**
	 * Returns the components of a weight on the root lattice.
	 * This is the inverse of rootToWeight.
	 *
	 * @param dynkinLabels	 The Dynkin labels of the weight to convert.
	 * @return				 The components of the weight in the root lattice.
	 * @see rootToWeight
	 */
	public fraction[] weightToRoot(int[] dynkinLabels)
	{
		fraction[] result = new fraction[rank];
		for (int i = 0; i < rank; i++)
		{
			result[i] = new fraction(0);
			for (int j = 0; j < rank; j++)
			{
				result[i].add( invA[i][j].times(dynkinLabels[j]) );
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Algebra other = (Algebra) obj;
		if (this.A != other.A && (this.A == null || !Arrays.equals(this.A,other.A))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 61 * hash + (this.A != null ? this.A.hashCode() : 0);
		return hash;
	}

}
