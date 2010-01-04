/*
 * HighestWeightRep.java
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

package edu.simplie.algebra;

import edu.simplie.math.fraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Given a specific Algebra and a highest weight state, this class creates an object representing
 * the whole representation. Its main purpose is to determine weight multiplicities.
 *
 * @see Algebra
 * @see Weight
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class HighestWeightRep
{
	/** The height of the highest weight. */
	public final fraction	highestHeight;
	/** The dimension of this representation. */
	public final long		dim;
	/** A string representation of the dimension. */
	public final String		dimension;
	/** The outer multiplicity of this representation in a reducible representation. Defaults to 1 */
	private long			outerMult;
	/** The algebra of which this is a weight system. */
	public final Algebra	algebra;
	/** The rank of the algebra of which this is a weight system. */
	private final int		rank;
	/** The heighest weight of the representation. */
	public final Weight		highestWeight;
	/** A constant used in the Freudenthal formula. */
	private final fraction	highestWeightFactor;

	/** The internal table containing the weights. */
	private ArrayList<HashSet<Weight>> weightSystem;
	/** Integer specifying how deep we constructed the weight system. */
	private int		constructedDepth;
	/** Cancel the construction or not? */
	private boolean	cancelConstruction;
	
	/**
	 * Creates a new instance of HighestWeightRep
	 *
	 * @param	algebra						The algebra of which this is a representation.
	 * @param	highestWeightLabels			The dynkin labels of the highest weight state.
	 * @throws	IllegalArgumentException	When the rank of the algebra and the number of 
	 *										weight labels do not coincide.
	 */
	public HighestWeightRep(Algebra algebra, int[] highestWeightLabels)
	{
		HashSet<Weight> zeroDepthWeight;
		
		this.algebra		= algebra;
		this.rank			= algebra.rank;
		this.highestWeight	= new Weight(highestWeightLabels);
		if(rank != highestWeightLabels.length)
			throw new IllegalArgumentException("Rank and number of weight labels do no match.");
		if(!highestWeight.isDominant)
			throw new IllegalArgumentException("Highest weight not dominant.");

		// Get the dimension of this rep.
		if(algebra.finite)
		{
			dim			= algebra.dimOfRep(highestWeightLabels);
			dimension	= (new Long(dim)).toString();
		}
		else
		{
			dim = 0;
			dimension	= "Infinite";
		}

		outerMult = 1;

		// Add the highest weight (construct to depth 0)
		weightSystem	= new ArrayList<HashSet<Weight>>();
		zeroDepthWeight = new HashSet<Weight>();
		zeroDepthWeight.add(highestWeight);
		
		weightSystem.add(0,zeroDepthWeight);
		constructedDepth = 0;
		
		// Calculate a common factor in the freudenthal formula
		highestWeightFactor = algebra.innerProduct(highestWeight,highestWeight);
		highestWeightFactor.add(algebra.innerProduct(highestWeight, algebra.rho).times(2));
		
		// Calculate the height of the highest weight
		highestHeight = algebra.weightHeight(highestWeightLabels);
	}

	public long getOuterMult()
	{
		return outerMult;
	}

	public void setOuterMult(long outerMult)
	{
		this.outerMult = outerMult;
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
	public Collection<Weight> get(int index)
	{
		return weightSystem.get(index);
	}
	
	/**
	 * Fetch the multiplicity of a weight by its dynkin labels.
	 *
	 * @param	weightLabels		The Dynkin labels of the weight for which the multiplicity is calculated.
	 * @return						The multiplicity of the weight, or 0 if it's not a weight in the representation.
	 * @throws  IllegalArgumentException	When the rank and number of weight labels do not match.
	 */
	public long getWeightMult(int[] weightLabels)
	{
		fraction	wantedDepthF;
		int			wantedDepth;
		Weight		wantedWeight;
		
		if(rank != weightLabels.length)
			throw new IllegalArgumentException("Rank and number of weight labels do no match");
	
		wantedDepthF = highestHeight.minus(algebra.weightHeight(weightLabels));
		
		if(!wantedDepthF.isInt())
			return 0;
		
		wantedDepth = wantedDepthF.asInt();
		
		if(wantedDepth < 0)
			// Do not try to get a weight that is outside the weight system.
			return 0;
		
		wantedWeight = new Weight(weightLabels);
		
		// Construct the weight system down to the depth just calculated.
		if(wantedDepth > constructedDepth)
			construct(wantedDepth);
		
		// Fetch the weight we wanted and return it.
		HashSet<Weight> wantedWeights = weightSystem.get(wantedDepth);
		if(!wantedWeights.contains(wantedWeight))
			// It's not here, return 0.
			return 0;
		
		for(Iterator it = wantedWeights.iterator(); it.hasNext();)
		{
			Weight weight = (Weight) it.next();
			if(weight.equals(wantedWeight))
			{
				return weight.getMult();
			}
		}
		
		// We shouldn't get here, but return null anyway.
		return 0;
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
		int[] reflected = weightLabels.clone();
		for (int i = 0; i < reflected.length; i++)
		{
			if(reflected[i] < 0)
			{
				reflected = algebra.simpWeylReflWeight(reflected, i);
				i = -1;
			}
		}	
		return reflected;
	}
	
	
	/** Construct the weight system down to the given depth */
	public void construct(int maxDepth)
	{
		// Don't construct fully for infinite algebras.
		if(maxDepth == 0 && !algebra.finite)
			return;
		
		HashSet<Weight> prevDepthWeights;
		HashSet<Weight> newWeights;
		int		nextDepth;
		Weight	oldWeight;
		Weight newWeight;
		
		cancelConstruction = false;
		
		// Print some info to sout.
		System.out.println(
			"Constructing highest weight rep " + dim + " of algebra " + algebra.type +
			" to depth " + maxDepth  + ".");
		
		// Do the construction.
		while((constructedDepth < maxDepth || maxDepth == 0) && !cancelConstruction)
		{
			nextDepth = constructedDepth + 1;
			
			// Print some info to sout.
			System.out.println("... depth: " + nextDepth);
			
			prevDepthWeights = weightSystem.get(constructedDepth);
			for (Iterator it = prevDepthWeights.iterator(); it.hasNext();)
			{
				oldWeight = (Weight) it.next();
				// See if the we can subtract a simple root from this weight.
				for (int i = 0; i < rank; i++)
				{
					// Don't do this for imaginary simple roots
					if(algebra.A[i][i] <= 0)
						continue;

					if(oldWeight.dynkinLabels[i] <= 0)
						continue;
					
					int pMax = oldWeight.dynkinLabels[i];
					// Ok we can do it pMax times.
					for(int j = 1; j <= pMax; j++)
					{
						if(weightSystem.size() - 1 < constructedDepth + j)
						{
							// This will be the first time this depth will be reached,
							// so create a new container for these weights.
							weightSystem.add(constructedDepth + j, new HashSet<Weight>());
						}
						// What are the new dynkin labels?
						int[] newDynkinLabels = new int[rank];
						for (int k = 0; k < rank; k++)
						{
							newDynkinLabels[k] = oldWeight.dynkinLabels[k] -  j * algebra.A[i][k];
						}
						newWeight = new Weight(newDynkinLabels);

						// Set the depth and the multiplicity
						newWeight.setDepth(constructedDepth + j);

						// If the new weight is not dominant, we do not have to calculate its multiplicity.
						// Just simply get the multiplicity of the closest dominant weight.
						if(j == pMax)
						{
							// This is the Weyl reflection of the old weight.
							// Thus they have the same multiplicity.
							newWeight.setMult(oldWeight.getMult());
						}
						
						// And add it.
						newWeights = weightSystem.get(constructedDepth + j);						
						newWeights.add(newWeight);
					}
				}
			}
			
			// If we didn't add something we should break, avoiding infinite loops.
			if(nextDepth > weightSystem.size() - 1)
				break;

			// Calculate the multiplicities of all the weights at the next height.
			newWeights = weightSystem.get(nextDepth);
			for(Iterator it = newWeights.iterator(); it.hasNext();)
			{
				newWeight = (Weight) it.next();
				if(newWeight.isDominant)
				{
					newWeight.setMult(calculateMult(newWeight));
				}
				else
				{
					newWeight.setMult(getWeightMult(makeDominant(newWeight.dynkinLabels)));
				}
			}
			constructedDepth++;
		}
		
	}
	
	/**
	 * Sets the multiplicity of a new weight.
	 * Basically an implementation of the Freudenthal recursion formula.
	 */
	private long calculateMult(Weight weight)
	{
		fraction	denominator;
		long		numerator;
		int			maxHeight;
		
		numerator = 0;
		
		// First calculate the denominator.
		denominator = highestWeightFactor.minus(algebra.innerProduct(weight,weight));
		denominator.subtract(algebra.innerProduct(weight,algebra.rho).times(2));

		if(algebra.finite)
		{
			maxHeight = Math.min(weight.getDepth(), algebra.rs.size()-1);
		}
		else
		{
			maxHeight = weight.getDepth();
			algebra.rs.construct(maxHeight);
		}
		
		// Now sum over all positive roots.
		for (int height = 1; height <= maxHeight; height++)
		{
			Collection roots	= algebra.rs.get(height);
			Iterator iterator	= roots.iterator();
			int maxK = (int) Math.floor(weight.getDepth()/height);
			while(iterator.hasNext())
			{
				Root root = (Root) iterator.next();
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
					numerator += getWeightMult(summedLabels) * (k*root.norm + rootDotWeight) * root.mult;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HighestWeightRep other = (HighestWeightRep) obj;
		if (this.algebra != other.algebra && (this.algebra == null || !this.algebra.equals(other.algebra))) {
			return false;
		}
		if (this.highestWeight != other.highestWeight && (this.highestWeight == null || !this.highestWeight.equals(other.highestWeight))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 59 * hash + (this.highestWeight != null ? this.highestWeight.hashCode() : 0);
		return hash;
	}

	
}
