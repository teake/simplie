/*
 * CHighestWeightRep.java
 *
 * Created on 19 april 2007, 13:43
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

import java.util.Collection;
import java.util.Iterator;

import javolution.util.FastList;
import javolution.util.FastCollection.Record;

/**
 * Given a specific CAlgebra and a highest weight state, this class creates an object representing
 * the whole representation. Its main purpose is to determine weight multiplicities.
 *
 * @see CAlgebra
 * @see CWeight
 * @author Teake Nutma
 */
public class CHighestWeightRep
{
	/** The height of the highest weight. */
	public final int		highestHeight;
	/** The dimension of this representation. */
	public final long dim;
	/** The algebra of which this is a weight system. */
	private final CAlgebra	algebra;
	/** The rank of the algebra of which this is a weight system. */
	private final int		rank;
	/** The heighest weight of the representation. */
	private final CWeight	highestWeight;
	/** A constant used in the Freudenthal formula. */
	private final fraction	highestWeightFactor;
	
	/** The internal table containing the weights. */
	private FastList<FastList> weightSystem;
	/** Integer specifying how deep we constructed the weight system. */
	private int		constructedDepth;
	/** Cancel the construction or not? */
	private boolean	cancelConstruction;
	
	/**
	 * Creates a new instance of CHighestWeightRep
	 *
	 * @param	algebra				The algebra of which this is a representation.
	 * @param	highestWeightLabels	The dynkin labels of the highest weight state.
	 */
	public CHighestWeightRep(CAlgebra algebra, int[] highestWeightLabels)
	{
		FastList zeroDepthWeight;
		
		this.algebra= algebra;
		this.rank	= algebra.rank;
		
		// Get the dimension of this rep.
		dim = algebra.dimOfRep(highestWeightLabels);
		
		// Add the highest weight (construct to depth 0)
		weightSystem	= new FastList<FastList>();
		highestWeight	= new CWeight(highestWeightLabels);
		zeroDepthWeight = new FastList<CWeight>();
		zeroDepthWeight.add(highestWeight);
		
		weightSystem.add(0,zeroDepthWeight);
		constructedDepth = 0;
		
		// Calculate a common factor in the freudenthal formula
		highestWeightFactor = algebra.innerProduct(highestWeight,highestWeight);
		highestWeightFactor.add(algebra.innerProduct(highestWeight, algebra.rho).times(2));
		
		// Calculate the height of the highest weight
		highestHeight = algebra.weightHeight(highestWeightLabels);
	}
	
	/** Cancels the construction of the weight system. */
	public void cancelConstruction()
	{
		cancelConstruction = true;
	}
	
	/** Returns the size of the weight system, i.e. the depth to which we have constructed so far + 1 */
	public int size()
	{
		return weightSystem.size();
	}
	
	/** Returns the weights in this rep at the given index, which is equal to the depth. */
	public Collection<CWeight> get(int index)
	{
		return weightSystem.get(index);
	}
	
	/**
	 * Fetch a weight by its dynkin labels.
	 * Useful for getting a weight multiplicity.
	 *
	 * @param	weightLabels		The Dynkin labels of the weight for which the multiplicity is calculated.
	 * @return						The weight, or null if something's wrong.
	 */
	public CWeight getWeight(int[] weightLabels)
	{
		int			wantedDepth;
		CWeight		wantedWeight;
		
		// Preliminary checks
		if(!algebra.finite || weightLabels.length != rank)
			return null;
		
		wantedDepth = highestHeight - algebra.weightHeight(weightLabels);
		
		if(wantedDepth < 0)
			// Do not try to get a weight that is outside the weight system.
			return null;
		
		wantedWeight = new CWeight(weightLabels);
		
		// Construct the weight system down to the depth just calculated.
		if(wantedDepth > constructedDepth)
			construct(wantedDepth);
		
		// Fetch the weight we wanted and return it.
		FastList<CWeight> wantedWeights = weightSystem.get(wantedDepth);
		int wantedIndex = wantedWeights.indexOf(wantedWeight);
		if(wantedIndex == -1)
			// It's not here, return 0.
			return null;
		
		return wantedWeights.get(wantedIndex);
	}
	
	/**
	 * Given the Dynkin labels of a non-dominant weight, this function
	 * returns the dynkin labels of the weight after it has been
	 * reflected into the dominant chamber.
	 *
	 * @param	weightLabels	The dynkin labels of the weight.
	 * @return					The dynkin labels of the reflected dominant weight.
	 */
	public int[] makeDominant(int[] weightLabels)
	{
		makeItSo:
			while(true)
			{
				for (int i = 0; i < weightLabels.length; i++)
				{
					if(weightLabels[i] < 0)
					{
						weightLabels = algebra.simpWeylRefl(weightLabels, i);
						break;
					}
					if(i == weightLabels.length - 1)
						break makeItSo;
				}
			}
		
		return weightLabels;
	}
	
	
	/** Construct the weight system down to the given depth */
	public void construct(int maxDepth)
	{
		int		newDepth;
		boolean	addedSomething;
		int[]	newSubtractable;
		CWeight	oldWeight;
		
		cancelConstruction = false;
		
		// Print some info to sout.
		System.out.println(
			"Constructing highest weight rep " + Helper.intArrayToString(highestWeight.dynkinLabels) +
			" to depth " + maxDepth + " of algebra " + algebra.type + ".");
		
		// Do the construction.
		while((constructedDepth < maxDepth || maxDepth == 0) && !cancelConstruction)
		{
			addedSomething	= false;
			newDepth		= constructedDepth + 1;
			
			// Print some info to sout.
			System.out.println("... depth: " + newDepth);
			
			FastList<CWeight> prevDepthWeights = weightSystem.get(constructedDepth);
			FastList<CWeight> thisDepthWeights = new FastList<CWeight>();
			for (Record r = prevDepthWeights.head(), end = prevDepthWeights.tail(); (r = r.getNext()) != end;)
			{
				oldWeight = prevDepthWeights.valueOf(r);
				// See if the we can subtract a simple root from this weight.
				for (int i = 0; i < rank; i++)
				{
					if(oldWeight.getSimpRootSubtractable(i) > 0)
					{
						// Ok we can. What are the new dynkin labels?
						int[] newDynkinLabels = new int[rank];
						for (int j = 0; j < rank; j++)
						{
							newDynkinLabels[j] = oldWeight.dynkinLabels[j] - algebra.A[i][j];
						}
						CWeight newWeight = new CWeight(newDynkinLabels);
						
						// How many times can we subtract a simple root from this weight?
						newSubtractable = oldWeight.getSimpRootSubtractable();
						newSubtractable[i]--;
						newWeight.setSimpRootSubtractable(newSubtractable);
						
						// Only possibly increase simpRootSubtractable if this root is already present.
						int existingIndex = thisDepthWeights.indexOf(newWeight);
						if(existingIndex != -1)
						{
							thisDepthWeights.get(existingIndex).setSimpRootSubtractable(newWeight.getSimpRootSubtractable());
							continue;
						}
						
						// Set the depth and the multiplicity
						newWeight.setDepth(newDepth);
						
						// If the new weight is not dominant, we do not have to calculate its multiplicity.
						// Just simply get the multiplicity of the closest dominant weight.
						if(!newWeight.isDominant)
						{
							int[]	dominantLabels	= makeDominant(newWeight.dynkinLabels);
							CWeight dominantWeight	= getWeight(dominantLabels);
							if(dominantWeight != null)
								newWeight.setMult(dominantWeight.getMult());
							else
								// If the closest dominant weight is not part of the root system,
								// then the new weight also isn't part of the root system.
								continue;
						}
						else
						{
							newWeight.setMult(calculateMult(newWeight));
						}
						
						// And add it.
						thisDepthWeights.add(newWeight);
						addedSomething = true;
					}
				}
			}
			
			if(!addedSomething)
				// If we didn't add something we should break, avoiding infinite loops.
				break;
			
			weightSystem.add(newDepth, thisDepthWeights);
			constructedDepth++;
		}
		
	}
	
	/**
	 * Sets the multiplicity of a new weight.
	 * Basically an implementation of the Freudenthal recursion formula.
	 */
	private long calculateMult(CWeight weight)
	{
		fraction	denominator;
		long		numerator;
		int			maxHeight;
		
		numerator = 0;
		
		// First calculate the denominator.
		denominator = highestWeightFactor.minus(algebra.innerProduct(weight,weight));
		denominator.subtract(algebra.innerProduct(weight,algebra.rho).times(2));
		
		maxHeight = Math.min(weight.getDepth(), algebra.rs.size()-1);
		
		// Now sum over all positive roots.
		for (int height = 1; height <= maxHeight; height++)
		{
			Collection roots	= algebra.rs.get(height);
			Iterator iterator	= roots.iterator();
			int maxK = (int) Math.floor(weight.getDepth()/height);
			while(iterator.hasNext())
			{
				CRoot root = (CRoot) iterator.next();
				int rootDotWeight	= algebra.innerProduct(weight,root);
				for (int k = 1; k <= maxK; k++)
				{
					// Construct the weight "lambda + k*alpha"
					int[] summedLabels = weight.dynkinLabels.clone();
					for (int i = 0; i < rank; i++)
					{
						for (int j = 0; j < rank; j++)
						{
							summedLabels[i] += k * algebra.A[j][i] * root.vector[j];
						}
					}
					CWeight summedWeight = new CWeight(summedLabels);
					FastList<CWeight> summedWeights = weightSystem.get(weight.getDepth()-height*k);
					int summedIndex = summedWeights.indexOf(summedWeight);
					if(summedIndex == -1)
						// It's not a weight
						continue;
					numerator += (k*root.norm + rootDotWeight) * summedWeights.get(summedIndex).getMult();
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
		return mult.asLong();
	}
	
}
