/*
 * CGroup.java
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

package edu.rug.hep.simplie.group;

import edu.rug.hep.simplie.Helper;
import edu.rug.hep.simplie.math.fraction;

import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import Jama.Matrix;

/**
 * Given a Cartan matrix, this class creates an object which has most properties of a simple Lie group:
 * rank, dimension, and a root system, to name a few.
 *
 * @see CRootSystem
 * @author Teake Nutma
 */
public class CGroup
{
	/*********************************
	 * Public properties
	 *********************************/
	
	/** The Cartan matrix of the group. */
	public final int[][] A;
	/** The symmetrized Cartan matrix (the inverse of the quadratic form matrix) */
	public final fraction[][] symA;
	/** The inverse of the cartan matrix. */
	public final fraction[][] invA;
	/** The metric on the root space. */
	public final int[][] B;
	/** The inverse of the metric on the root space. */
	public final fraction[][] invB;
	/** The quadratic form matrix, which acts as a metric on the weight space. */
	public final fraction[][] G;
	
	/**
	 * Note that the the symmetrized Cartan matrix is not the same as
	 * the one in Fuchs & Schweigert, because they use the normalization
	 * a^2 = 2 for the *longest* root, and I use it for the *shortest* root.
	 */
	
	/** The simple roots */
	public final ArrayList<CRoot> simpleRoots;
	/** The maximum root norm. */
	public final int maxNorm;
	
	/** The determinant of the Cartan Matrix. */
	public final int	det;
	/** The rank of the Cartan matrix */
	public final int	rankA;
	/** The rank of the group. */
	public final int	rank;
	/** The dimension of the group (the number of generators). Only set for finite groups. */
	public final int	dim;
	/** String value of dim. "Infinite" if the group is infinite. */
	public final String	dimension;
	/** The type of the group ("A1", "E6", etc) */
	public final String	type;
	/** The same as "type", but now in HTML markup */
	public final String typeHTML;
	/** The same as "type", but now in TeX markup */
	public final String typeTeX;
	/** Boolean indicating whethet the group is finite or not. */
	public final boolean finite;
	/** The root system */
	public final CRootSystem rs;
	/** The Weyl vector of the group */
	public final CWeight rho;
	
	/** Vector containing the norm of the simple roots divided by two. */
	public final int[] simpleRootNorms;
	/** List of matrices into which the Cartan matrix factorizes */
	private final ArrayList<int[][]> directProductFactors;
	
	/**********************************
	 * Public methods
	 **********************************/
	
	/**
	 * Creates a new instance of CGroup.
	 * If the group is finite, the whole rootsystem is constructed.
	 *
	 * @param A    The Cartan matrix from which to construct the group.
	 */
	public CGroup(Matrix A)
	{
		// Do some preliminary checks
		if(A.getColumnDimension() != A.getRowDimension() || A.getColumnDimension() == 0)
		{
			rank	= 0;
			rankA	= 0;
			det		= 0;
		}
		else
		{
			rank	= A.getColumnDimension();
			rankA	= A.rank();
			det		= (int) Math.round(A.det());
		}
		if(det > 0)
			finite = true;
		else
			finite = false;
		
		this.A		= new int[rank][rank];
		this.B		= new int[rank][rank];
		this.symA	= new fraction[rank][rank];
		this.invA	= new fraction[rank][rank];
		this.invB	= new fraction[rank][rank];
		this.G		= new fraction[rank][rank];
		this.directProductFactors = new ArrayList<int[][]>();
		
		// Set the cartan matrix
		for(int i=0; i<rank; i++)
		{
			for(int j=0; j<rank; j++)
			{
				this.A[i][j] = (int) Math.round(A.get(i,j));
			}
		}
		
		// Set the simple roots
		simpleRoots = new ArrayList<CRoot>();
		for (int i = 0; i < rank; i++)
		{
			int[] rootVector = new int[rank];
			for (int j = 0; j < rank; j++)
			{
				rootVector[j] = ( i == j) ? 1 : 0;
			}
			CRoot simpleRoot	= new CRoot(rootVector);
			simpleRoot.mult		= 1;
			simpleRoot.coMult	= new fraction(1);
			simpleRoot.norm		= 0; // these will be set later on.
			simpleRoots.add(simpleRoot);
		}
		
		// Set the root norms for every disconnected piece.
		// Set the highest norm simultaneously.
		int tempMaxNorm = 0;
		while(true)
		{
			int startIndex = -1;
			
			// Grab the first simple root that hasn't been set yet.
			for (int i = 0; i < rank; i++)
			{
				CRoot simpleRoot = simpleRoots.get(i);
				if(simpleRoot.norm == 0)
				{
					startIndex = i;
					break;
				}
			}
			
			// Are we done?
			if(startIndex == -1)
				break;
			
			// If we're still here then we're not done.
			// First detect all the simple roots in this piece.
			ArrayList<CNormHelper> piece = new ArrayList<CNormHelper>();
			piece.add(new CNormHelper(startIndex,1));
			while(true)
			{
				CNormHelper add	 = null;
				CNormHelper from = null;
				// Try to see if this piece has connections to roots we've missed so far.
				loopToBreak:
					for(CNormHelper nh : piece)
					{
						from = nh;
						for (int i = 0; i < rank; i++)
						{
							if(nh.index != i && this.A[i][nh.index] != 0)
							{
								CNormHelper normHelper = new CNormHelper(i,1);
								if(!piece.contains(normHelper))
								{
									add = normHelper;
									break loopToBreak;
								}
							}
						}
					}
					
					if(add != null)
					{
						double norm = from.norm * this.A[add.index][from.index] / this.A[from.index][add.index];
						piece.add(new CNormHelper(add.index,norm));
					}
					else
						break;
			}
			
			// Add the cartan matrix of this piece to the direct product factors.
			int[][] pieceMatrix = new int[piece.size()][piece.size()];
			for (int i = 0; i < pieceMatrix.length; i++)
			{
				for (int j = 0; j < pieceMatrix.length; j++)
				{
					pieceMatrix[i][j] = this.A[piece.get(i).index][piece.get(j).index];
				}
			}
			directProductFactors.add(pieceMatrix);
			
			// Sort this piece according to norm.
			Collections.sort(piece);
			
			// Set the simple root norms.
			double	coefficient	= 2 / piece.get(0).norm;
			for(CNormHelper nh : piece)
			{
				CRoot root = simpleRoots.get(nh.index);
				root.norm = (int) Math.round(nh.norm * coefficient);
				tempMaxNorm = Math.max(tempMaxNorm, root.norm);
			}
			
		}
		maxNorm = tempMaxNorm;
		simpleRootNorms	= new int[rank];
		for (int i = 0; i < rank; i++)
		{
			// The norm of the simple roots are always a multiple of 2.
			simpleRootNorms[i] = simpleRoots.get(i).norm / 2;
		}
		
		// Construct the Weyl vector
		int[] weylLabels = new int[rank];
		for (int i = 0; i < rank; i++)
		{
			weylLabels[i] = 1;
		}
		rho = new CWeight(weylLabels);
		
		// Detect the type of Lie group.
		String tType	= "";
		String tTypeTeX	= "";
		String tTypeHTML= "";
		String[] series	= { "A", "B", "C", "D", "E", "F", "G" };
		for(int i = 0; i < directProductFactors.size(); i++)
		{
			int[][]	matrix	= directProductFactors.get(i);
			String	ttType		= null;
			String	ttTypeHTML	= null;
			String	ttTypeTeX	= null;
			
			int size		= matrix.length;
			int extended	= 0;
			
			{
				loopToBreak:
					for (int j = 0; j < size; j++)
					{
						for(String serie : series)
						{
							if(Helper.sameCartanMatrices(matrix,Helper.cartanMatrix(size,j,serie)))
							{
								ttType = ttTypeHTML = ttTypeTeX = serie;
								extended = j;
								break loopToBreak;
							}
						}
					}
			}
			
			if(ttType == null)
				ttType = ttTypeHTML = ttTypeTeX = "Unknown";
			else
			{
				if(size != 0)
				{
					int simpleRank = size - extended;
					ttType		+= simpleRank;
					ttTypeHTML	+= "<sub>" + simpleRank + "</sub>";
					ttTypeTeX	+= "_{" + simpleRank + "}";
				}
				ttTypeHTML	+= "<sup>";
				ttTypeTeX	+= "^{";
				for (int j = 0; j < extended; j++)
				{
					ttType		+= "+";
					ttTypeHTML	+= "+";
					ttTypeTeX	+= "+";
				}
				ttTypeHTML	+= "</sup>";
				ttTypeTeX	+= "}";
				
			}
			tType		+= ttType;
			tTypeHTML	+= ttTypeHTML;
			tTypeTeX	+= ttTypeTeX;
			if(i != directProductFactors.size() - 1)
			{
				tType		+= " x ";
				tTypeHTML	+= " x ";
				tTypeTeX	+= " \\otimes ";
			}
		}
		if(tType == "")
			type = typeHTML = typeTeX = "Empty";
		else
		{
			type		= tType;
			typeTeX		= "$" + tTypeTeX + "$";
			typeHTML	= "<html>" + tTypeHTML + "</html>";
		}
		
		// Set up the root system.
		rs = new CRootSystem(this);
		
		// Now that the simple roots have been created,
		// we can set the symmetrized Cartan matrix
		// and the metric on the root space.
		Matrix symA	= new Matrix(rank,rank);
		Matrix B	= new Matrix(rank,rank);
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				this.symA[i][j] = new fraction(this.A[i][j], simpleRootNorms[i]);
				this.B[i][j]	= this.A[i][j] * simpleRootNorms[j];
				symA.set(i,j,this.symA[i][j].asDouble());
				B.set(i,j,this.B[i][j]);
			}
		}
		
		// Set the inverse of the Cartan matrix and the quadratic form matrix if possible.
		if(rank != 0 && A.rank() == rank)
		{
			Matrix invA	= A.inverse();
			Matrix invB	= B.inverse();
			Matrix G	= symA.inverse();
			for (int i = 0; i < rank; i++)
			{
				for (int j = 0; j < rank; j++)
				{
					this.invA[i][j]	= new fraction(invA.get(i,j));
					this.invB[i][j]	= new fraction(invB.get(i,j));
					this.G[i][j]	= new fraction(G.get(i,j));
				}
			}
		}
		
		// If the group is finite, we can construct the root system to all heights.
		if(finite)
			rs.construct(0);
		
		// Determine the dimension.
		if(det > 0 || rank == 0)
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
	 * @param	highestWeightLabels	The Dynkin labels of the representation.
	 * @return						The dimension of the presentation, 0 if something's wrong.
	 */
	public long dimOfRep(int[] highestWeightLabels)
	{
		fraction	dimOfRep;
		CWeight		highestWeight;
		
		// Preliminary checks.
		if(!finite || highestWeightLabels.length != rank)
			return 0;
		
		dimOfRep				= new fraction(1);
		highestWeight	= new CWeight(highestWeightLabels);
		
		for (int i = 1; i < rs.size(); i++)
		{
			Collection roots	= rs.get(i);
			Iterator iterator	= roots.iterator();
			while (iterator.hasNext())
			{
				CRoot root = (CRoot) iterator.next();
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
	public int innerProduct(CRoot root1, CRoot root2)
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
	public fraction innerProduct(CWeight weight1, CWeight weight2)
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
	public int innerProduct(CWeight weight, CRoot root)
	{
		int result = 0;
		for (int i = 0; i < rank; i++)
		{
			result += weight.dynkinLabels[i] * root.vector[i] * simpleRootNorms[i];
		}
		return result;
	}
	
	/**
	 * Calculate the action of the Weyl vector on a root.
	 *
	 * @param	root	Root alpha.
	 * @return			rho(alhpa), with rho the Weyl vector of this group.
	 */
	public int rho(CRoot root)
	{
		int result = 0;
		for (int i = 0; i < rank; i++)
		{
			result += root.vector[i] * simpleRootNorms[i];
		}
		return result;
	}
	
	
	/**
	 * Calculate the height of a weight.
	 *
	 * @param	weightLabels	The dynkinlabels of the weight.
	 * @return					The height of the weight.
	 */
	public int weightHeight(int[] weightLabels)
	{
		fraction height = new fraction(0);
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				height.add(invA[j][i].times(weightLabels[j]));
			}
		}
		return height.asInt();
	}
	
	/**
	 * Performs a simple Weyl reflection on the dynkin labels of a weight.
	 *
	 * @param	weightLabels	The dynkin labels of the weight.
	 * @param	i				The index of the simple root with which we should reflect.
	 * @return					The dynkin labels of the reflected weight.
	 */
	public int[] simpWeylRefl(int[] weightLabels, int i)
	{
		int[] output = new int[weightLabels.length];
		for (int j = 0; j < output.length; j++)
		{
			output[j] = weightLabels[j] - A[i][j] * weightLabels[i];
		}
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
