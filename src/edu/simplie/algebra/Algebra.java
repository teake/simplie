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
	/** The symmetrized Cartan matrix (the inverse of the quadratic form matrix) */
	public final fraction[][] symA;
	/** The inverse of the cartan matrix. */
	public final fraction[][] invA;
	/** The metric on the root space. */
	public final int[][] B;
	/** The quadratic form matrix, which acts as a metric on the weight space. */
	public final fraction[][] G;
	
	/**
	 * Note that the the symmetrized Cartan matrix is not the same as
	 * the one in Fuchs & Schweigert, because they use the normalization
	 * a^2 = 2 for the *longest* root, and I use it for the *shortest* root.
	 */
	
	/** The determinant of the Cartan Matrix. */
	public final int	det;
	/** The rank of the Cartan matrix */
	public final int	rankA;
	/** The rank of the algebra. */
	public final int	rank;
	/** The dimension of the algebra (the number of generators). Only set for finite algebras. */
	public final int	dim;
	/** String value of dim. "Infinite" if the algebra is infinite. */
	public final String	dimension;
	/** The type of the algebra ("A1", "E6", etc) */
	public final String	type;
	/** The same as "type", but now in HTML markup */
	public final String typeHTML;
	/** The same as "type", but now in TeX markup */
	public final String typeTeX;
	/** Boolean indicating whethet the algebra is finite or not. */
	public final boolean finite;
	/** The root system */
	public final RootSystem rs;
	/** The Weyl vector of the algebra */
	public final Weight rho;
	
	/** Vector containing the norm of the simple roots divided by two. */
	public final int[] halfNorms;
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
		// Convert the Cartan matrix to a matrix object.
		Matrix cartanMatrix = Helper.intArrayToMatrix(A);
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
			rankA	= cartanMatrix.rank();
			det		= (int) Math.round(cartanMatrix.det());
		}
		
		this.A		= Helper.cloneMatrix(A);
		this.B		= new int[rank][rank];
		this.symA	= new fraction[rank][rank];
		this.invA	= new fraction[rank][rank];
		this.G		= new fraction[rank][rank];
		this.directProductFactors = new ArrayList<CartanMatrix>();
		
		this.halfNorms = new int[rank];
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
					halfNorms[j] = Math.round(norm);
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
		// we can set the symmetrized Cartan matrix
		// and the metric on the root space.
		Matrix symAm	= new Matrix(rank,rank);
		Matrix Bm		= new Matrix(rank,rank);
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				this.symA[i][j] = new fraction(this.A[i][j], halfNorms[i]);
				this.B[i][j]	= this.A[i][j] * halfNorms[j];
				symAm.set(i,j,this.symA[i][j].asDouble());
				Bm.set(i,j,this.B[i][j]);
			}
		}
		
		// Set the inverse of the Cartan matrix and the quadratic form matrix if possible.
		if(rank != 0 && rankA == rank)
		{
			Matrix invAm	= cartanMatrix.inverse();
			Matrix Gm		= symAm.inverse();
			for (int i = 0; i < rank; i++)
			{
				for (int j = 0; j < rank; j++)
				{
					this.invA[i][j]	= new fraction(invAm.get(i,j));
					this.G[i][j]	= new fraction(Gm.get(i,j));
				}
			}
		}
		
		// Set up the root system.
		rs = new RootSystem(this);
		
		// Determine the dimension.
		if(finite)
		{
			dim			= 2 * (int) rs.numPosGenerators() + rank;
			dimension	= Helper.intToString(dim);
		}
		else
		{
			dim			= 0;
			dimension	= "Infinite";
		}
		
		
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
			throw new IllegalArgumentException("No representations of infinite algebras.");

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
			result += weight.dynkinLabels[i] * root.vector[i] * halfNorms[i];
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
			result += root.vector[i] * halfNorms[i];
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
		int[] output = new int[weightLabels.length];
		for (int j = 0; j < output.length; j++)
		{
			output[j] = weightLabels[j] - A[i][j] * weightLabels[i];
		}
		return output;
	}
	
	/**
	 * Performs a simple Weyl reflection on vector of a root.
	 * @param	rootVector	 The vector of the root we want to reflect.
	 * @param	i			 The i'th simple root in which we will reflect.
	 * @return				 The vector of the reflected root.
	 */
	public int[] simpWeylReflRoot(int[] rootVector, int i)
	{
		int[] output		= rootVector.clone();		
		int[] dynkinLabels	= rootToWeight(rootVector);
		
		output[i] = output[i] - dynkinLabels[i];
		
		return output;
	}
	
	
	/** Returns the Dynkin labels of the weight associated to a vector of the root. */
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
}
