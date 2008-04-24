/*
 * CRootSystem.java
 *
 * Created on 19 april 2007, 12:01
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

import java.io.*;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
/**
 * Given a specific CAlgebra, this class creates the corresponding root system.
 *
 * @see		CAlgebra
 * @author	Teake Nutma
 */
public class CRootSystem
{
	/** The algebra of which this is the rootsystem. */
	private final CAlgebra algebra;
	/** The rank of the algebra of which this is the rootsystem. */
	private final int rank;
	/** The number of positive roots constructed so far. */
	private long numPosRoots;
	/** The number of positive generators constructed so far. */
	private long numPosGenerators;
	/** The table containing all the (positive) roots. */
	private ArrayList<HashSet<CRoot>> rootSystem;
	/** The table containing all the multiples of roots that aren't roots themselves (used in the Peterson formula). */
	private ArrayList<ArrayList<CRoot>> rootMultiples;
	/** The simple roots. */
	private HashSet<CRoot> simpleRoots;
	/** The height up to and included to which we constructed the root system. */
	private int constructedHeight;
	/** Boolean to indicate whether the root system construction should be canceled */
	private boolean cancelConstruction;
	/** Indicates whether the root system is fully constructed or not. */
	private boolean fullyConstructed;
	/** The maximum root norm */
	private int maxNorm;
	/** The minimum root norm (as constructed so far) */
	private int minNorm;
	
	/** Creates a new instance of CRootSystem and constructs up to height 1. */
	public CRootSystem(CAlgebra algebra)
	{
		this.algebra	= algebra;
		this.rank		= algebra.rank;
		rootSystem		= new ArrayList<HashSet<CRoot>>();
	
		if(rank==0)
		{
			return;
		}
		
		// Add the CSA to the root table.
		HashSet<CRoot> csa = new HashSet<CRoot>();
		int[] csaVector		= new int[rank];
		for (int i = 0; i < rank; i++)
		{
			csaVector[i] = 0;
		}
		CRoot csaRoot	= new CRoot(csaVector);
		csaRoot.mult	= rank;
		csaRoot.coMult	= new fraction(0);
		csaRoot.norm	= 0;
		csa.add(csaRoot);
		rootSystem.add(0,csa);
		
		// Add the simple roots and set the max and min norms.
		simpleRoots = new HashSet<CRoot>();
		maxNorm = 0;
		minNorm = Integer.MAX_VALUE;
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
			simpleRoot.norm		= (short) (2 * algebra.halfNorms[i]);
			simpleRoots.add(simpleRoot);
			maxNorm = Math.max(simpleRoot.norm, maxNorm);
			minNorm = Math.min(simpleRoot.norm, minNorm);
		}
		rootSystem.add(1, simpleRoots);
		numPosGenerators	= rank;
		numPosRoots			= rank;
		
		// And we've constructed to height 1.
		constructedHeight	= 1;
		fullyConstructed	= false;
		
		// Set the table of root multiples.
		rootMultiples = new ArrayList<ArrayList<CRoot>>();
		rootMultiples.add(0,new ArrayList<CRoot>());
		rootMultiples.add(1,new ArrayList<CRoot>());
		
		// If the algebra is finite, we can construct the root system to all heights.
		if(algebra.finite)
			construct(0);

	}
	

	/** Cancels the construction of the rootsystem. */
	public void cancelConstruction()
	{
		cancelConstruction = true;
	}
	
	/** Represents the rootsystem in a string. */
	@Override
	public String toString()
	{
		String output = new String();
		for (int i = 0; i < rootSystem.size(); i++)
		{
			HashSet<CRoot> roots = rootSystem.get(i);
			for (Iterator it = roots.iterator(); it.hasNext();)
			{
				output += "Index: " + i + ", " + it.next() + "\n";
			}
		}
		return output;
	}
	
	/**
	 * Writes the root system constructed thus far to a text file.
	 *
	 * @param	filename	The file to which to save the data.
	 */
	public void writeTxtFile(String filename)
	{
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter(filename));
			for (int i = 0; i < rootSystem.size(); i++)
			{
				HashSet<CRoot> roots = rootSystem.get(i);
				for (Iterator it = roots.iterator(); it.hasNext();)
				{
					out.println(it.next());
				}
			}
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Returns the number of positive generators.
	 * That is, the number of roots times their multiplicity, plus the rank.
	 */
	public long numPosGenerators()
	{
		return numPosGenerators;
	}
	
	/**
	 * Returns the number of postive roots.
	 */
	public long numPosRoots()
	{
		return numPosRoots;
	}
	
	/** Returns the size of the root system, that is the height to which we have constructed so far + 1. */
	public int size()
	{
		return rootSystem.size();
	}
	
	/** Returns the height to which we so far have constructed the rootsystem. */
	public int constructedHeight()
	{
		return constructedHeight;
	}
	
	/** Returns the maximum norm of the roots */
	public int maxNorm()
	{
		return maxNorm;
	}
	
	/** Returns the minimum norm of the roots constructed thus far */
	public int minNorm()
	{
		return minNorm;
	}	
	
	/**
	 * Returns the roots of a given height.
	 *
	 * @param	index	The height of the roots to get.
	 * @return			A collection containing the roots of that height.
	 */
	public Collection<CRoot> get(int index)
	{
		return rootSystem.get(index);
	}
	
	/**
	 * Get a root multiplicity by its root vector.
	 *
	 * @param	vector	The root vector of the root we should get.
	 * @return			The root multiplicity, 0 if it's not a root.
	 */
	public long getRootMult(int[] vector)
	{
		// Dirty hack to check for negative roots.
		// TODO: implement this better.
		for (int i = 0; i < vector.length; i++)
		{
			vector[i] = Math.abs(vector[i]);
		}
		return getRootMult(new CRoot(vector));
	}
	
	/**
	 * Get a root multiplicity.
	 *
	 * @param rootToGet		The root of which the multiplicity should be returned.
	 * @return				The root multiplicity, 0 if it's not a root.
	 */
	public long getRootMult(CRoot rootToGet)
	{
		int rootHeight = rootToGet.height();
		HashSet<CRoot> roots;
		
		if(rootHeight < 0)
		{
			return 0;
		}
		
		// If we haven't constructed the root system this far, do so now.
		if(rootHeight > constructedHeight && !fullyConstructed)
			construct(rootHeight);
		
		// Try to fetch the root.
		if(rootSystem.size() > rootHeight)
		{
			roots = rootSystem.get(rootHeight);
			if(roots.contains(rootToGet))
			{
				for(Iterator it = roots.iterator(); it.hasNext();)
				{
					CRoot root = (CRoot) it.next();
					if(root.equals(rootToGet))
						return root.mult;
				}
			}
		}
		
		// The root is not in the root system, so return null. 
		return 0;
	}
	
	/**
	 * Saves the root system to file.
	 *
	 * @param	filename	Where to save the file.
	 * @return				True upon succes, false on failure.
	 */
	public boolean saveTo(String filename)
	{
		filename.trim();
		FileOutputStream fos	= null;
		ObjectOutputStream out	= null;
		try
		{
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(algebra.A);
			out.writeInt(constructedHeight);
			out.writeLong(numPosRoots);
			out.writeLong(numPosGenerators);
			out.writeObject(rootSystem);
			out.writeObject(rootMultiples);
			out.close();
		}
		catch(IOException ex)
		{
			return false;
		}
		return true;
	}
	
	
	/**
	 * Loads the root system from a file.
	 *
	 * @param	filename	The file from which to load the root system.
	 * @return				True on succes, false on failure.
	 */
	@SuppressWarnings("unchecked")
	public boolean loadFrom(String filename)
	{
		filename.trim();
		FileInputStream fis		= null;
		ObjectInputStream in	= null;
		try
		{
			fis = new FileInputStream(filename);
			in	= new ObjectInputStream(fis);
			int[][] savedCM	= (int[][]) in.readObject();
			if(Helper.sameMatrices(savedCM, algebra.A))
			{
				constructedHeight	= in.readInt();
				numPosRoots			= in.readLong();
				numPosGenerators	= in.readLong();
				rootSystem			= (ArrayList<HashSet<CRoot>>) in.readObject();
				rootMultiples		= (ArrayList<ArrayList<CRoot>>) in.readObject();
			}
			else
			{
				in.close();
				return false;
			}
			in.close();
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Construct the root system up to the given height.
	 *
	 * @param maxHeight	The height up to and including which we should construct the root system.
	 *					Construct the whole root system if maxHeight == 0.
	 */
	private void construct(int maxHeight)
	{
		// If the root system is already fully constructed, just do nothing and return.
		if(fullyConstructed)
			return;
		
		HashSet<CRoot> prevRoots;
		HashSet<CRoot> newRoots;
		CRoot	root;
		CRoot	newRoot;
		int[]	newVector;
		int[]	dynkinLabels;
		int		nextHeight;
		int		newHeight;
		
		cancelConstruction = false;
		
		System.out.println("Constructing roots of " + algebra.type + " to height " + maxHeight + ".");
		
		while(constructedHeight < maxHeight || maxHeight == 0)
		{
			prevRoots	= rootSystem.get(constructedHeight);
			nextHeight	= constructedHeight + 1;
			
			System.out.println("... height: " + nextHeight);
			
			// First determine all the possible new roots that can be obtained from the
			// roots at the old height by simple Weyl reflections.
			for (Iterator it = prevRoots.iterator(); it.hasNext();)
			{
				if(cancelConstruction)
					return;
				root = (CRoot) it.next();
				// Calculate the Dynkin labels
				dynkinLabels = algebra.rootToWeight(root.vector);
				for (int i = 0; i < rank; i++)
				{
					// For every negative Dynkin label we can add 
					// a (partial) root string to the root table.
					if(dynkinLabels[i] >= 0)
						continue;
					
					// The root string stops at \gamma = \beta + pMax \apha_i,
					// with \gamma being the new root, \beta the old, and
					// pMax equal to -p_i.
					int pMax = -1 * dynkinLabels[i];
					for(int j = 1; j <= pMax; j++)
					{
						if(rootSystem.size() - 1 < constructedHeight + j)
						{
							// This will be the first time this height will be reached,
							// so create a new container for these roots.
							rootSystem.add(constructedHeight + j,new HashSet<CRoot>());	
						}
						newVector		= root.vector.clone();
						newVector[i]	= newVector[i] + j;
						newRoot			= new CRoot(newVector);
						newHeight		= constructedHeight + j;
						if(j == pMax)
						{
							// This is the Weyl reflection of the old root.
							// Thus they have the same multiplicity.
							newRoot.mult = root.mult;
							// The other multiplicities will be calculated below.
						}
						// Add the new root to the root table if it isn't there already.
						newRoots = rootSystem.get(newHeight);
						if(newRoots.add(newRoot))
						{
							newRoot.norm = (short) algebra.innerProduct(newRoot,newRoot);
							minNorm = Math.min(minNorm, newRoot.norm);
						}
					}
				} // ... for(i<rank)
			} // ... for(all roots @ this height)
			
			if(nextHeight > rootSystem.size() - 1)
			{
				// We did nothing, and thus reached the highest root.
				// Make a note that we constructed the root system fully, and return.
				fullyConstructed = true;
				return;
			}
			
			// Calculate the coMult and the mult for all the added roots
			// at the first new height.
			newRoots = rootSystem.get(nextHeight);

			for (Iterator it = newRoots.iterator(); it.hasNext();)
			{
				root = (CRoot) it.next();

				// Determine its coMult minus the root multiplicity.
				fraction coMult = calculateCoMult(root);

				// Only calculate the mult if it hasn't been set before.
				if(root.mult == 0)
				{
					// First try to get the multiplicity from another root in 
					// this roots Weyl-orbit. We only need to do one simple Weyl-reflection 
					// down, as all the roots below this height have been calculated before.

					// First determine the first positive Dynkin label.
					// We will do a simple Weyl reflection in this index later.
					dynkinLabels = algebra.rootToWeight(root.vector);
					int reflectIndex;
					boolean canReflect	= false;
					for(reflectIndex = 0; reflectIndex < rank; reflectIndex++)
					{
						if(dynkinLabels[reflectIndex] > 0)
						{
							canReflect	= true;
							break;
						}
					}

					if(canReflect)
					{
						// We can reflect down, so do it.
						int[] reflectedVector = algebra.simpWeylReflRoot(root.vector, reflectIndex);
						// Get the multiplicity.
						root.mult = getRootMult(reflectedVector);
					}
					else
					{
						// We couldn't reflect down, so calculate the multiplicity by hand.
						root.mult = calculateMult(root,coMult);
					}
				} // ... if(root.mult == 0)

				root.coMult = coMult.plus(root.mult);

				// Increment numPosRoots.
				numPosRoots++;
				numPosGenerators += root.mult;
			} // ... for(all new roots)

			//
			// Construct all the root multiples of the roots at the new height
			//
			ArrayList<CRoot> multiplesList = new ArrayList<CRoot>();
			for (int i = 1; i < Math.floor(nextHeight / 2) + 1; i++)
			{
				// We're only interested in i's with zero divisor.
				if(nextHeight % i != 0)
					continue;
				int factor = nextHeight / i;
				HashSet<CRoot> roots = rootSystem.get(i);
				for (Iterator it = roots.iterator(); it.hasNext();)
				{
					root = (CRoot) it.next();
					CRoot rootMultiple = root.times(factor);
					// Don't add it if it's already in the 'proper' root list.
					// Else we would count this one double.
					if(newRoots.contains(rootMultiple))
						continue;
					rootMultiple.coMult	= calculateCoMult(rootMultiple);
					multiplesList.add(rootMultiple);
				}
			}
			rootMultiples.add(nextHeight,multiplesList);

			// Finally bump the constructed height number.
			constructedHeight++;	
		} // ... while(constructedHeight < maxHeight)
	}
	

	/********************************
	 * Multiplicity functions below *
	 ********************************/
	
	
	
	/**
	 * Calculates the "co-multiplicity", i.e. the fractional sum of multiplicities
	 * of all fractional roots. Used in the Peterson formula.
	 *
	 * @return			The co-multiplicity.
	 */
	private fraction calculateCoMult(CRoot root)
	{
		fraction coMult	= new fraction(0);
		
		// There are no root multiples if the root is real
		if(root.norm > 0)
			return coMult;
		
		for (int i = 2; i < root.highest() + 1; i++)
		{
			CRoot divRoot = root.div(i);
			if(divRoot != null)
			{
				coMult.add(new fraction(getRootMult(divRoot),i));
			}
		}
		return coMult;
	}
	
	/**
	 * Calculates the multiplicity of a root.
	 * Based on the Peterson formula. Note that it is necessary to give the co-multiplicity
	 * in advance.
	 *
	 * @param	root	The root whose multiplicity is calculated.
	 * @param	coMult	The co-multiplicity of that root.
	 * @return			The multiplicity of the root.
	 */
	private long calculateMult(CRoot root, fraction coMult)
	{
		fraction multiplicity = new fraction(0);

		// We split the Peterson formula into two symmetric halves,
		// plus a remainder if the root height is even.
		
		int halfHeight = (int) Math.ceil(((float) root.height()) / 2);
		for(int i = 1; i < halfHeight; i++)
			multiplicity.add(petersonPart(root, i));

		multiplicity.multiply(2);

		if(root.height() % 2 == 0)
			multiplicity.add(petersonPart(root, root.height() / 2));

		multiplicity.divide( algebra.innerProduct(root,root) - (2 * algebra.rho(root) ) );
		multiplicity.subtract(coMult);

		if(!multiplicity.isInt())
		{
			System.out.println("*WARNING*: fractional multiplicity of root " + root.toString());
			System.out.println("*WARNING*: actual mult: " + multiplicity.toString());
		}

		return multiplicity.asLong();
	}
	
	/**
	 * Calculate a part of the Peterson formula (in particular the r.h.s. for
	 * a given value of the height of one of the decomposition parts).
	 *
	 * @param	root 		The root for which we are calculating the multiplicity.
	 * @param	height 		The height of one of the decomposition parts.
	 * @return				A part of the Peterson formula, which is to be summed over.
	 */
	private fraction petersonPart(CRoot root, int height)
	{
		HashSet<CRoot> betas	= rootSystem.get(height);
		HashSet<CRoot> gammas	= rootSystem.get(root.height() - height);
		
		fraction multiplicity = petersonSubPart(root, betas, gammas);
		
		ArrayList<CRoot> betaMultiples	= rootMultiples.get(height);
		ArrayList<CRoot> gammaMultiples	= rootMultiples.get(root.height() - height);
		
		multiplicity.add(petersonSubPart(root,betaMultiples,gammaMultiples));
		multiplicity.add(petersonSubPart(root,betas,gammaMultiples));
		multiplicity.add(petersonSubPart(root,betaMultiples,gammas));
		
		return multiplicity;
	}
	
	private fraction petersonSubPart(CRoot root, Collection<CRoot> list1, Collection<CRoot> list2)
	{
		fraction multiplicity = new fraction(0);
		CRoot beta;
		CRoot gamma;
		for (Iterator it1 = list1.iterator(); it1.hasNext();)
		{
			beta = (CRoot) it1.next();
			innerRootLoop:
				for (Iterator it2 = list2.iterator(); it2.hasNext();)
				{
					gamma = (CRoot) it2.next();
					for (int i = 0; i < rank; i++)
					{
						if(beta.vector[i] + gamma.vector[i] != root.vector[i])
							continue innerRootLoop;
					}
					fraction part = beta.coMult.times(gamma.coMult);
					part.multiply(algebra.innerProduct(beta,gamma));
					multiplicity.add(part);
				}
		}
		return multiplicity;
	}
	
}
