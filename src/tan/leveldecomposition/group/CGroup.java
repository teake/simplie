/*
 * CGroup.java
 *
 * Created on 26 maart 2007, 17:17
 *
 */

package tan.leveldecomposition.group;

import tan.leveldecomposition.*;
import tan.leveldecomposition.math.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.text.DecimalFormat;
import Jama.Matrix;

/**
 *
 * @author Teake Nutma
 */
public class CGroup
{
	//TODO: convert rs & weightSystem into classes of their own.
	
	/*********************************
	 * Public properties
	 *********************************/
	
	/** The Cartan matrix of the group. */
	public int[][]  cartanMatrix;
	/** The inverse of the cartan matrix. */
	public fraction[][] cartanMatrixInv;
	/**
	 * The quadratic form matrix. Because we only work with simply-laced algebras,
	 * this is the same as the inverse of the cartan matrix.
	 */
	public fraction[][] qFormMatrix;
	/** The determinant of the Cartan Matrix. */
	public int		det;
	/** The rank of the group. */
	public int		rank;
	/** The dimension of the group (i.e. the number of generators). Only set for finite groups. */
	public int		dim;
	/** String value of dim. "Infinite" if the group is infinite. */
	public String	dimension;
	/** The type of the group (e.g. "A1", "E6", etc) */
	public String	type;
	/** Boolean indicating whethet the group is finite or not. */
	public boolean	finite;
	/** The root system */
	public CRootSystem rs;
	
	/*********************************
	 * Private properties
	 *********************************/
	
	/** The Weyl vector of the group */
	private CWeight	weylVector;
	
	
	/**********************************
	 * Public methods
	 **********************************/
	
	/**
	 * Creates a new instance of CGroup.
	 *
	 * @param cartanMatrix    The Cartan matrix from which to construct the group.
	 */
	public CGroup(Matrix cartanMatrix)
	{
		Matrix	compareMatrix;
		
		/** Do some preliminary checks */
		if(cartanMatrix.getColumnDimension() != cartanMatrix.getRowDimension())
			return;
		if(cartanMatrix.getColumnDimension() == 0)
		{
			type		= "Trivial";
			dimension	= "0";
			return;
		}
		
		/** Set the rank, the cartan matrix, the determinant, and the finite property */
		rank = cartanMatrix.getColumnDimension();
		this.cartanMatrix = new int[rank][rank];
		for(int i=0; i<rank; i++)
		{
			for(int j=0; j<rank; j++)
			{
				this.cartanMatrix[i][j] = (int) Math.round(cartanMatrix.get(i,j));
			}
		}
		det    = (int) Math.round(cartanMatrix.det());
		finite = (det > 0) ? true : false;
		
		/** Set the inverse of the Cartan matrix if possible. */
		this.cartanMatrixInv = new fraction[rank][rank];
		if(cartanMatrix.rank() == rank && det != 0)
		{
			Matrix cmInv = cartanMatrix.inverse();
			for (int i = 0; i < rank; i++)
			{
				for (int j = 0; j < rank; j++)
				{
					cartanMatrixInv[i][j] = new fraction( Math.round( det * cmInv.get(i,j) ), det);
				}
			}
		}
		/** Set a pointer to the inverse of the cartan matrix. */
		this.qFormMatrix = cartanMatrixInv;
		
		/** Try to determine the group type */
		// TODO: make this algorithm find more types
		compareMatrix = Globals.regularMatrix(rank);
		if(Globals.sameMatrices(compareMatrix,cartanMatrix))
			type = "A";
		else if(rank > 4)
		{
			compareMatrix.set(0,3,-1);
			compareMatrix.set(3,0,-1);
			compareMatrix.set(0,1,0);
			compareMatrix.set(1,0,0);
			if(Globals.sameMatrices(compareMatrix,cartanMatrix))
				type = "E";
			else
			{
				compareMatrix = Globals.regularMatrix(rank);
				compareMatrix.set(rank-1,2,-1);
				compareMatrix.set(2,rank-1,-1);
				compareMatrix.set(rank-2,rank-1,0);
				compareMatrix.set(rank-1,rank-2,0);
				if(Globals.sameMatrices(compareMatrix,cartanMatrix))
					type = "E";
			}
		}
		if(type == null)
			type = "?";
		else
			type += rank;
		
		
		/** Construct the Weyl vector */
		int[] weylLabels = new int[rank];
		for (int i = 0; i < rank; i++)
		{
			
			weylLabels[i] = 1;
		}
		weylVector = new CWeight(weylLabels);
		
		/** Set up the root system */
		rs = new CRootSystem(this);
		
		/** If the group is finite, we can construct the root system to all heights. */
		if(finite)
		{
			dim			= 2 * (int) rs.numPosGenerators() + rank;
			dimension	= Globals.intToString(dim);
		}
		else
		{
			dimension	= "Infinite";
		}
	}
	
	/**
	 * Determines the dimension of the representation defined by
	 * Dynkin labels associated to the given dynkinLabels.
	 * Basically an implementation of Weyl's dimensionality formula.
	 *
	 * @param	dynkinLabels	The Dynkin labels of the representation.
	 * @return					The dimension of the presentation, 0 if something's wrong.
	 */
	public long dimOfRep(int[] highestWeightLabels)
	{
		fraction	dim;
		CWeight		highestWeight;
		
		/** Preliminary checks. */
		if(!finite || highestWeightLabels.length != rank)
			return 0;
		
		dim				= new fraction(1);
		highestWeight	= new CWeight(highestWeightLabels);
		
		for (int i = 1; i < rs.size(); i++)
		{
			Collection roots	= rs.get(i);
			Iterator iterator	= roots.iterator();
			while (iterator.hasNext())
			{
				CRoot root = (CRoot) iterator.next();
				dim.multiply( innerProduct(highestWeight,root) + root.height() );
				dim.divide( root.height() );
			}
		}
		
		return dim.asLong();
	}
	
	/**
	 * Determines the multiplicity of a weight that sits in the representation
	 * given by heighestWeight. Basically an implementation of the Freudenthal
	 * recursion formula.
	 *
	 * @param	highestWeightLabels	The Dynkin labels of the highest weight of the representation.
	 * @param	weightLabels		The Dynkin labels of the weight for which the multiplicity is calculated.
	 * @return						The multiplicity of the weight, 0 if something's wrong.
	 */
	public long weightMultiplicity(int[] highestWeightLabels, int[] weightLabels)
	{
		System.out.println(
				"Calculating the multiplicity of " + Globals.intArrayToString(weightLabels) +
				" in " + Globals.intArrayToString(highestWeightLabels) + " of group " + type + ".");
		fraction[]	rootHighest;
		fraction[]	rootWeight;
		int			wantedDepth;
		CWeight		wantedWeight;
		ArrayList<ArrayList> weightSystem;
		
		/** Preliminary checks */
		if(!finite || highestWeightLabels.length != rank || weightLabels.length != rank)
			return 0;
		
		rootHighest = weightToRoot(highestWeightLabels);
		rootWeight	= weightToRoot(weightLabels);
		
		/** Calculate the depth of the weight */
		wantedDepth = 0;
		for (int i = 0; i < rank; i++)
		{
			fraction depthPart = rootHighest[i].minus(rootWeight[i]);
			if(!depthPart.isInt())
				/** Then weight is not part of the highest weight rep of 'highest weight' */
				return 0;
			wantedDepth += rootHighest[i].minus(rootWeight[i]).asInt();
		}
		if(wantedDepth <= 0)
			/** We don't calculate the multiplicity of the highest weight itself */
			return 0;
		
		wantedWeight = new CWeight(weightLabels);
		
		/** Construct the weight system down to the depth just calculated. */
		weightSystem = constructWeightSystem(highestWeightLabels, wantedDepth);
		
		/** Fetch the weight we wanted and return its multiplicity */
		ArrayList<CWeight> wantedWeights = weightSystem.get(wantedDepth);
		int wantedIndex = wantedWeights.indexOf(wantedWeight);
		if(wantedIndex == -1)
			/** It's not here, return 0. */
			return 0;
		
		return wantedWeights.get(wantedIndex).getMult();
	}
	
	public void cancelEverything()
	{
		rs.cancelConstruction();
	}
	
	/**********************************
	 * Private methods
	 **********************************/
	
	
	private ArrayList<ArrayList> constructWeightSystem(int[] highestWeightLabels, int maxDepth)
	{
		ArrayList<ArrayList>	weightSystem;
		ArrayList<CWeight>		zeroDepthWeight;
		fraction				highestWeightFactor;
		int						constructedDepth;
		int						newDepth;
		CWeight					highestWeight;
		boolean					addedSomething;
		int[]					newSubtractable;
		
		
		/** First add the highest weight */
		weightSystem	= new ArrayList<ArrayList>();
		highestWeight	= new CWeight(highestWeightLabels);
		zeroDepthWeight	= new ArrayList<CWeight>();
		zeroDepthWeight.add(highestWeight);
		
		weightSystem.add(0,zeroDepthWeight);
		constructedDepth = 0;
		
		/**
		 * This factor appears in all denominators of the Freudenthal formula,
		 * so calculate it once and for all.
		 */
		highestWeightFactor = innerProduct(highestWeight,highestWeight);
		highestWeightFactor.add(innerProduct(highestWeight, weylVector).times(2));
		
		/** Do the construction. */
		while(constructedDepth < maxDepth)
		{
			addedSomething	= false;
			newDepth		= constructedDepth + 1;
			
			ArrayList<CWeight> prevDepthWeights = weightSystem.get(constructedDepth);
			ArrayList<CWeight> thisDepthWeights = new ArrayList<CWeight>();
			for(CWeight oldWeight : prevDepthWeights)
			{
				/** See if the we can subtract a simple root from this weight */
				for (int i = 0; i < rank; i++)
				{
					if(oldWeight.getSimpRootSubtractable(i) > 0)
					{
						/** Ok we can. What are the new dynkin labels? */
						int[] newDynkinLabels = new int[rank];
						for (int j = 0; j < rank; j++)
						{
							newDynkinLabels[j] = oldWeight.dynkinLabels[j] - cartanMatrix[j][i];
						}
						CWeight newWeight = new CWeight(newDynkinLabels);
						
						/** How many times can we subtract a simple root from this weight? */
						newSubtractable = oldWeight.getSimpRootSubtractable();
						newSubtractable[i]--;
						newWeight.setSimpRootSubtractable(newSubtractable);
						
						/** Only possibly increase simpRootSubtractable if this root is already present. */
						int existingIndex = thisDepthWeights.indexOf(newWeight);
						if(existingIndex != -1)
						{
							thisDepthWeights.get(existingIndex).setSimpRootSubtractable(newWeight.getSimpRootSubtractable());
							continue;
						}
						
						/** Set the depth and the multiplicity */
						newWeight.setDepth(newDepth);
						setWeightMult(weightSystem, newWeight, highestWeightFactor);
						
						/** And add it. */
						thisDepthWeights.add(newWeight);
						addedSomething = true;
					}
				}
			}
			
			if(!addedSomething)
				/** If we didn't add something we should break, avoiding infinite loops. */
				break;
			
			weightSystem.add(newDepth, thisDepthWeights);
			constructedDepth++;
		}
		
		return weightSystem;
	}
	
	private void setWeightMult(ArrayList<ArrayList> weightSystem, CWeight weight, fraction highestWeightFactor)
	{
		fraction	denominator;
		long		numerator;
		int			maxHeight;
		
		numerator = 0;
		
		/** First calculate the denominator */
		denominator = highestWeightFactor.minus(innerProduct(weight,weight));
		denominator.subtract(innerProduct(weight,weylVector).times(2));
		
		maxHeight = Math.min(weight.getDepth(), rs.size()-1);
		
		/** Now sum over all positive roots */
		for (int height = 1; height <= maxHeight; height++)
		{
			Collection roots	= rs.get(height);
			Iterator iterator	= roots.iterator();
			int maxK = (int) Math.floor(weight.getDepth()/height);
			while(iterator.hasNext())
			{
				CRoot root = (CRoot) iterator.next();
				int rootNorm		= innerProduct(root,root);
				int rootDotWeight	= innerProduct(weight,root);
				for (int k = 1; k <= maxK; k++)
				{
					/** Construct the weight "lambda + k*alpha" */
					int[] summedLabels = weight.dynkinLabels.clone();
					for (int i = 0; i < rank; i++)
					{
						for (int j = 0; j < rank; j++)
						{
							summedLabels[i] += k * cartanMatrix[i][j] * root.vector[j];
						}
					}
					CWeight summedWeight = new CWeight(summedLabels);
					ArrayList<CWeight> summedWeights = weightSystem.get(weight.getDepth()-height*k);
					int summedIndex = summedWeights.indexOf(summedWeight);
					if(summedIndex == -1)
						/** It's not a weight */
						continue;
					numerator += (k*rootNorm + rootDotWeight) * summedWeights.get(summedIndex).getMult();
				}
			}
		}
		fraction mult = new fraction(2 * numerator);
		mult.divide(denominator);
		if(!mult.isInt())
		{
			System.out.println("*WARNING*: fractional multplicity of weight " + weight);
			System.out.println("*WARNING*: calculated mult: " + mult);
		}
		weight.setMult(2 * numerator / denominator.asInt());
	}
	
	
	
	
	/**
	 * Calculate the innerproduct between two roots.
	 */
	public int innerProduct(CRoot root1, CRoot root2)
	{
		int result = 0;
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				result += cartanMatrix[i][j] * root1.vector[i] * root2.vector[j];
			}
		}
		return result;
	}
	
	/**
	 * Calculate the innerproduct between two weights.
	 */
	public fraction innerProduct(CWeight weight1, CWeight weight2)
	{
		fraction result = new fraction(0);
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				result.add( qFormMatrix[i][j].times(weight1.dynkinLabels[i] * weight2.dynkinLabels[j]) );
			}
		}
		return result;
	}
	
	/**
	 * Calculate the innerproduct between a weight and a root.
	 */
	public int innerProduct(CWeight weight, CRoot root)
	{
		int result = 0;
		for (int i = 0; i < rank; i++)
		{
			result += weight.dynkinLabels[i] * root.vector[i];
		}
		return result;
	}
	
	/**
	 * Convert a weight to a root
	 */
	public fraction[] weightToRoot(int[] weight)
	{
		fraction[] rootVector = new fraction[rank];
		for (int i = 0; i < rank; i++)
		{
			rootVector[i] = new fraction(0);
			for (int j = 0; j < rank; j++)
			{
				rootVector[i].add(cartanMatrixInv[i][j].times(weight[j]));
			}
		}
		return rootVector;
	}
	
}
