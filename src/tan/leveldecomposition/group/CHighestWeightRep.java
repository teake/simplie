/*
 * CHighestWeightRep.java
 *
 * Created on 19 april 2007, 13:43
 *
 */

package tan.leveldecomposition.group;

import tan.leveldecomposition.math.*;
import tan.leveldecomposition.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Teake Nutma
 */
public class CHighestWeightRep
{
	/** The group of which this is a weight system. */
	private final CGroup	group;
	/** The rank of the group of which this is a weight system. */
	private final int		rank;
	/** The heighest weight of the representation. */
	private final CWeight	highestWeight;
	/** A constant used in the Freudenthal formula. */
	private final fraction	highestWeightFactor;
	
	/** The internal table containing the weights. */
	private ArrayList<ArrayList> weightSystem;
	/** Integer specifying how deep we constructed the weight system. */
	private int		constructedDepth;
	/** Cancel the construction or not? */
	private boolean	cancelConstruction;
	
	/**
	 * Creates a new instance of CHighestWeightRep
	 */
	public CHighestWeightRep(CGroup group, int[] highestWeightLabels)
	{
		ArrayList zeroDepthWeight;
		
		this.group	= group;
		this.rank	= group.rank;
		
		/** Add the highest weight (construct to depth 0) */
		weightSystem	= new ArrayList<ArrayList>();
		highestWeight	= new CWeight(highestWeightLabels);
		zeroDepthWeight = new ArrayList<CWeight>();
		zeroDepthWeight.add(highestWeight);
		
		weightSystem.add(0,zeroDepthWeight);
		constructedDepth = 0;
		
		/** Calculate a common factor in the freudenthal formula */
		highestWeightFactor = group.innerProduct(highestWeight,highestWeight);
		highestWeightFactor.add(group.innerProduct(highestWeight, group.weylVector).times(2));
	}
	
	public void cancelConstruction()
	{
		cancelConstruction = true;
	}
	
	/**
	 * Determines the multiplicity of a weight that sits in the representation
	 * given by heighestWeight. Basically an implementation of the Freudenthal
	 * recursion formula.
	 *
	 * @param	weightLabels		The Dynkin labels of the weight for which the multiplicity is calculated.
	 * @return						The multiplicity of the weight, 0 if something's wrong.
	 */
	public long weightMultiplicity(int[] weightLabels)
	{
		System.out.println(
				"Calculating the multiplicity of " + Globals.intArrayToString(weightLabels) +
				" in " + Globals.intArrayToString(highestWeight.dynkinLabels) + " of group " + group.type + ".");
		
		fraction[]	rootHighest;
		fraction[]	rootWeight;
		int			wantedDepth;
		CWeight		wantedWeight;
		
		/** Preliminary checks */
		if(!group.finite || weightLabels.length != rank)
			return 0;
		
		rootHighest = group.weightToRoot(highestWeight.dynkinLabels);
		rootWeight	= group.weightToRoot(weightLabels);
		
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
		construct(wantedDepth);
		
		/** Fetch the weight we wanted and return its multiplicity */
		ArrayList<CWeight> wantedWeights = weightSystem.get(wantedDepth);
		int wantedIndex = wantedWeights.indexOf(wantedWeight);
		if(wantedIndex == -1)
			/** It's not here, return 0. */
			return 0;
		
		return wantedWeights.get(wantedIndex).getMult();
	}
	
	
	/** Construct the weight system down to the given depth */
	private void construct(int maxDepth)
	{
		int		newDepth;
		boolean	addedSomething;
		int[]	newSubtractable;
		
		cancelConstruction = false;
		
		/** Do the construction. */
		while(constructedDepth < maxDepth && !cancelConstruction)
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
							newDynkinLabels[j] = oldWeight.dynkinLabels[j] - group.cartanMatrix[j][i];
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
						setWeightMult(newWeight);
						
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
		
	}
	
	private void setWeightMult(CWeight weight)
	{
		fraction	denominator;
		long		numerator;
		int			maxHeight;
		
		numerator = 0;
		
		/** First calculate the denominator */
		denominator = highestWeightFactor.minus(group.innerProduct(weight,weight));
		denominator.subtract(group.innerProduct(weight,group.weylVector).times(2));
		
		maxHeight = Math.min(weight.getDepth(), group.rs.size()-1);
		
		/** Now sum over all positive roots */
		for (int height = 1; height <= maxHeight; height++)
		{
			Collection roots	= group.rs.get(height);
			Iterator iterator	= roots.iterator();
			int maxK = (int) Math.floor(weight.getDepth()/height);
			while(iterator.hasNext())
			{
				CRoot root = (CRoot) iterator.next();
				int rootNorm		= group.innerProduct(root,root);
				int rootDotWeight	= group.innerProduct(weight,root);
				for (int k = 1; k <= maxK; k++)
				{
					/** Construct the weight "lambda + k*alpha" */
					int[] summedLabels = weight.dynkinLabels.clone();
					for (int i = 0; i < rank; i++)
					{
						for (int j = 0; j < rank; j++)
						{
							summedLabels[i] += k * group.cartanMatrix[i][j] * root.vector[j];
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
}
