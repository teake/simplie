/*
 * CRootSystem.java
 *
 * Created on 19 april 2007, 12:01
 *
 */

package edu.rug.hep.simplie.group;

import edu.rug.hep.simplie.Globals;
import edu.rug.hep.simplie.math.fraction;

import java.util.Collection;
import java.util.Vector;
import java.util.Collections;
import java.io.*;

import javolution.util.FastList;
import javolution.util.FastSet;
import javolution.util.FastCollection.Record;

/**
 * Given a specific CGroup, this class creates the corresponding root system.
 *
 * @see		CGroup
 * @author	Teake Nutma
 */
public class CRootSystem
{
	/** Vector containing the norm of the simple roots divided by two. */
	public final int[] simpleRootNorms;
	
	/** The group of which this is the rootsystem. */
	private final CGroup group;
	/** The rank of the group of which this is the rootsystem. */
	private final int rank;
	/** The number of positive roots constructed so far. */
	private long numPosRoots;
	/** The number of positive generators constructed so far. */
	private long numPosGenerators;
	/** The table containing all the (positive) roots. */
	private FastList<FastList> rootSystem;
	/** The table containing all the multiples of roots that aren't roots themselves (used in the Peterson formula). */
	private FastList<FastList> rootMultiples;
	/** The simple roots. */
	private FastList<CRoot> simpleRoots;
	/** The height up to and included to which we constructed the root system. */
	private int constructedHeight;
	/** Boolean to indicate whether the root system construction should be canceled */
	private boolean cancelConstruction;
	
	/** Creates a new instance of CRootSystem and constructs up to height 1. */
	public CRootSystem(CGroup group)
	{
		this.group	= group;
		this.rank	= group.rank;
		
		rootSystem			= new FastList<FastList>();
		numPosRoots			= 0;
		numPosGenerators	= 0;
		constructedHeight	= 0;
		simpleRootNorms		= new int[rank];
		
		if(rank==0)
			return;
		
		// Add the CSA to the root table.
		FastList<CRoot> csa = new FastList<CRoot>();
		CRoot csaRoot	= new CRoot(rank);
		csaRoot.mult	= rank;
		csaRoot.coMult	= new fraction(0);
		csaRoot.norm	= 0;
		csa.add(csaRoot);
		rootSystem.add(0,csa);
		
		// Add the simple roots to the root table.
		simpleRoots = new FastList<CRoot>();
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
			Vector<CNormHelper> piece = new Vector<CNormHelper>();
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
							if(nh.index != i && group.A[i][nh.index] != 0)
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
						double norm = from.norm * group.A[add.index][from.index] / group.A[from.index][add.index];
						piece.add(new CNormHelper(add.index,norm));
					}
					else
						break;
			}
			
			Collections.sort(piece);
			
			Integer	shortIndex	= piece.get(0).index;
			CRoot	shortRoot	= simpleRoots.get(shortIndex);
			double	coefficient	= 2 / piece.get(0).norm;
			for(CNormHelper nh : piece)
			{
				CRoot root = simpleRoots.get(nh.index);
				root.norm = (int) Math.round(nh.norm * coefficient);
			}
			
		}
		
		rootSystem.add(1,simpleRoots);
		numPosGenerators	= rank;
		numPosRoots			= rank;
		
		// And we've constructed to height 1.
		constructedHeight = 1;
		
		for (int i = 0; i < rank; i++)
		{
			// The norm of the simple roots are always a multiple of 2.
			simpleRootNorms[i] = simpleRoots.get(i).norm / 2;
		}
		
		// Set the table of root multiples.
		rootMultiples = new FastList<FastList>();
		rootMultiples.add(0,new FastList<CRoot>());
		rootMultiples.add(1,new FastList<CRoot>());
	}
	

	/** Cancels the construction of the rootsystem. */
	public void cancelConstruction()
	{
		cancelConstruction = true;
	}
	
	/** Represents the rootsystem in a string. */
	public String toString()
	{
		String output = new String();
		for (int i = 0; i < rootSystem.size(); i++)
		{
			FastList<CRoot> roots = rootSystem.get(i);
			for (Record r = roots.head(), end = roots.tail(); (r = r.getNext()) != end;)
			{
				output += "Index: " + i + ", " + roots.valueOf(r) + "\n";
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
				FastList<CRoot> roots = rootSystem.get(i);
				for (Record r = roots.head(), end = roots.tail(); (r = r.getNext()) != end;)
				{
					out.println(roots.valueOf(r));
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
	 * Get a root by its root vector.
	 *
	 * @param	vector	The root vector of the root we should get.
	 * @return			A pointer to the root if found, and null if not found.
	 */
	public CRoot getRoot(int[] vector)
	{
		// Dirty hack to check for negative roots.
		// TODO: implement this better.
		for (int i = 0; i < vector.length; i++)
		{
			vector[i] = Math.abs(vector[i]);
		}
		return getRoot(new CRoot(vector));
	}
	
	/**
	 * Check if a root is in the root system.
	 *
	 * @param rootToGet		The root of which we should check if it's in the root system.
	 * @return				A pointer to the root if found, and null if not found.
	 */
	public CRoot getRoot(CRoot rootToGet)
	{
		return getRoot(rootToGet, rootToGet.height());
	}
	
	
	/**
	 * Check if a root is in the root system.
	 *
	 * @param rootToGet		The root of which we should check if it's in the root system.
	 * @param rootHeight	The height of the root.
	 * @return				A pointer to the root if found, and null if not found.
	 */
	public CRoot getRoot(CRoot rootToGet, int rootHeight)
	{
		FastList<CRoot> roots;
		int		 index;
		
		if(rootHeight < 0)
		{
			return null;
		}
		
		// If we haven't constructed the root system this far, do so now.
		if(rootHeight > constructedHeight)
			construct(rootHeight);
		
		// Try to fetch the root.
		if(rootSystem.size() > rootHeight)
		{
			roots = rootSystem.get(rootHeight);
			index = roots.indexOf(rootToGet);
			if(index != -1)
			{
				return roots.get(index);
			}
		}
		return null;
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
			out.writeObject(group.A);
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
			if(Globals.sameMatrices(savedCM, group.A))
			{
				constructedHeight	= in.readInt();
				numPosRoots			= in.readLong();
				numPosGenerators	= in.readLong();
				rootSystem			= (FastList<FastList>) in.readObject();
				rootMultiples		= (FastList<FastList>) in.readObject();
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
	public void construct(int maxHeight)
	{
		FastList<CRoot> prevRoots;
		FastSet<CRoot> rootCache;
		CRoot	root;
		CRoot	simpleRoot;
		CRoot	oldRoot;
		CRoot	newRoot;
		long	prevNumPosRoots;
		int		innerProduct;
		int		n_minus;
		int		newHeight;
		int		oldHeight;
		
		cancelConstruction = false;
		
		System.out.println("Constructing roots of " + group.type + " to height " + maxHeight + ".");
		
		while(constructedHeight < maxHeight || maxHeight == 0)
		{
			prevRoots	    = rootSystem.get(constructedHeight);
			rootCache		= new FastSet<CRoot>();
			prevNumPosRoots = numPosRoots;
			newHeight	    = constructedHeight + 1;
			
			System.out.println("... height: " + newHeight);
			
			//
			// Try to add the simple roots to all the previous roots.
			// The main formula employed here is
			//
			//		<alpha, beta> = n_minus - n_plus ,
			//
			// where alpha and beta are roots.
			// If n_plus is positive, then alpha + beta is again a root.
			//
			for (Record r1 = prevRoots.head(), end1 = prevRoots.tail(); (r1 = r1.getNext()) != end1;)
			{
				root = prevRoots.valueOf(r1);
				for (Record r2 = simpleRoots.head(), end2 = simpleRoots.tail(); (r2 = r2.getNext()) != end2;)
				{
					simpleRoot = simpleRoots.valueOf(r2);
					if(cancelConstruction)
						return;
					
					newRoot = root.plus(simpleRoot);
					
					// First check if we didn't do this root before.
					if(!rootCache.add(newRoot))
						continue;
					
					innerProduct = group.innerProduct(root,simpleRoot);
					if(innerProduct < 0)
					{
						// n_plus is always positive, thus (root + simpleRoot) is a root.
						// Add it to the root table.
						addRoot(newRoot);
					}
					
					// Multiply by 2 and divide by the norm of the simple root
					// to get the correct value.
					// Because this is equal to (n_minus - n_plus), it is always an integer.
					innerProduct = innerProduct * 2 / simpleRoot.norm;
					
					if(innerProduct >= 0)
					{
						// Iterate through the previous heights to obtain n_minus.
						n_minus		= 0;
						oldHeight	= constructedHeight - 1;
						oldRoot		= getRoot(root.minus(simpleRoot), oldHeight);
						while(oldRoot != null)
						{
							n_minus++;
							if(n_minus > innerProduct)
							{
								// n_plus is positive.
								addRoot(newRoot);
								break;
							}
							oldHeight--;
							oldRoot = getRoot(oldRoot.minus(simpleRoot), oldHeight);
						}
					}
				}
			}
			
			if(numPosRoots > prevNumPosRoots)
			{
				constructedHeight++;
				
				//
				// Construct all the root multiples of this height
				//
				FastList multiplesList	= new FastList<CRoot>();
				FastList properList	= rootSystem.get(newHeight);
				for (int i = 1; i < Math.floor(newHeight / 2) + 1; i++)
				{
					// We're only interested in i's with zero divisor.
					if(newHeight % i != 0)
						continue;
					int factor = newHeight / i;
					FastList<CRoot> roots = rootSystem.get(i);
					for (Record r = roots.head(), end = roots.tail(); (r = r.getNext()) != end;)
					{
						root = roots.valueOf(r);
						CRoot rootMultiple = root.times(factor);
						// Don't add it if it's already in the 'proper' root list.
						// Else we would count this one double.
						if(properList.contains(rootMultiple))
							continue;
						rootMultiple.coMult	= calculateCoMult(rootMultiple,false);
						multiplesList.add(rootMultiple);
					}
				}
				rootMultiples.add(newHeight,multiplesList);
				
			}
			else
			{
				// We did nothing, and thus reached the highest root.
				break;
			}
		}
	}
	
	/**
	 * Adds a root to the root table and increments numPosRoots.
	 *
	 * @param	root	The root to add.
	 * @return			True if succesfull, false if it already was present.
	 */
	private boolean addRoot(CRoot root)
	{
		FastList<CRoot> roots;
		
		// Where should we add it, if we shoud add it at all?
		if(rootSystem.size() > root.height())
		{
			roots = rootSystem.get(root.height());
			if(roots.contains(root))
				return false;
		}
		else
		{
			roots = new FastList<CRoot>();
			rootSystem.add(root.height(),roots);
		}
		
		// TODO: possibly move this to CRoot
		
		// Determine its coMult minus the root multiplicity.
		fraction coMult = calculateCoMult(root,false);
		
		root.mult	= calculateMult(root,coMult);
		root.coMult = coMult.plus(root.mult);
		root.norm	= group.innerProduct(root,root);
		
		// Increment numPosRoots.
		numPosRoots++;
		numPosGenerators += root.mult;
		
		// Add finally it to the table.
		roots.add(root);
		
		return true;
	}
	
	
	
	/********************************
	 * Multiplicity functions below *
	 ********************************/
	
	
	
	/**
	 * Calculates the "co-multiplicity", i.e. the fractional sum of multiplicities
	 * of all fractional roots. Used in the Peterson formula.
	 *
	 * @param	root	The root whose co-multiplicity we should calculate.
	 * @return			The co-multiplicity.
	 */
	private fraction calculateCoMult(CRoot root, boolean includeRoot)
	{
		int offset		= ( includeRoot ) ? 1 : 2;
		fraction coMult	= new fraction(0);
		
		for (int i = offset; i < root.highest() + 1; i++)
		{
			CRoot divRoot = root.div(i);
			if(divRoot != null)
			{
				CRoot alpha = getRoot(divRoot);
				if(alpha != null)
				{
					coMult.add(new fraction(alpha.mult,i));
				}
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
		fraction multiplicity	= new fraction(0);
		
		// We split the Peterson formula into two symmetric halves,
		// plus a remainder if the root height is even.
		
		int halfHeight = (int) Math.ceil(((float) root.height()) / 2);
		for(int i = 1; i < halfHeight; i++)
			multiplicity.add(petersonPart(root, i));
		
		multiplicity.multiply(2);
		
		if(root.height() % 2 == 0)
			multiplicity.add(petersonPart(root, root.height() / 2));
		
		multiplicity.divide( group.innerProduct(root,root) - (2 * group.rho(root) ) );
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
		FastList<CRoot> betas	= rootSystem.get(height);
		FastList<CRoot> gammas	= rootSystem.get(root.height() - height);
		
		fraction multiplicity = petersonSubPart(root, betas, gammas);
		
		FastList<CRoot> betaMultiples	= rootMultiples.get(height);
		FastList<CRoot> gammaMultiples	= rootMultiples.get(root.height() - height);
		
		multiplicity.add(petersonSubPart(root,betaMultiples,gammaMultiples));
		multiplicity.add(petersonSubPart(root,betas,gammaMultiples));
		multiplicity.add(petersonSubPart(root,betaMultiples,gammas));
		
		return multiplicity;
	}
	
	private fraction petersonSubPart(CRoot root, FastList<CRoot> list1, FastList<CRoot> list2)
	{
		fraction multiplicity = new fraction(0);
		CRoot beta;
		CRoot gamma;
		for (Record r1 = list1.head(), end1 = list1.tail(); (r1 = r1.getNext()) != end1;)
		{
			beta = list1.valueOf(r1);
			innerRootLoop:
				for (Record r2 = list2.head(), end2 = list2.tail(); (r2 = r2.getNext()) != end2;)
				{
					gamma = list2.valueOf(r2);
					for (int i = 0; i < rank; i++)
					{
						if(beta.vector[i] + gamma.vector[i] != root.vector[i])
							continue innerRootLoop;
					}
					fraction part = beta.coMult.times(gamma.coMult);
					part.multiply(group.innerProduct(beta,gamma));
					multiplicity.add(part);
				}
		}
		return multiplicity;
	}
	
}
