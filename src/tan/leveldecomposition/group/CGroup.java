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
import java.text.DecimalFormat;
import java.io.*;
import Jama.Matrix;

/**
 *
 * @author Teake Nutma
 */
public class CGroup
{
	/*********************************
	 * Public properties
	 *********************************/
	
	/** The Cartan matrix of the group. */
	public int[][]  cartanMatrix;
	/** The inverse of the cartan matrix. */
	public fraction[][] cartanMatrixInv;
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
	
	/*********************************
	 * Private properties
	 *********************************/
	
	/** The height up to and included to which we constructed the root system. */
	private int constructedHeight;
	/** The number of positive roots constructed so far. */
	private long numPosRoots;
	/** The number of positive generators constructed so far. */
	private long numPosGenerators;
	/** The table containing all the (positive) roots. */
	private ArrayList<ArrayList> rootSystem;
	/** The table containing all the multiples of roots that aren't roots themselves (used in the Peterson formula). */
	private ArrayList<ArrayList> rootMultiples;
	/** The simple roots. */
	private ArrayList<CRoot> simpleRoots;
	/** Boolean to indicate whether the root system construction should be canceled */
	private boolean cancelConstruction;
	
	
	
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
		
		rootSystem = new ArrayList<ArrayList>();
		numPosRoots = 0;
		numPosGenerators = 0;
		
		/** Add the CSA to the root table */
		addRoot(new CRoot(rank));
		
		/** Add the simple roots to the root table */
		for (int i = 0; i < rank; i++)
		{
			int[] rootVector = new int[rank];
			for (int j = 0; j < rank; j++)
			{
				rootVector[j] = ( i == j) ? 1 : 0;
			}
			addRoot(new CRoot(rootVector));
		}
		simpleRoots = rootSystem.get(1);
		constructedHeight = 1;
		
		/** Set the table of root multiples. */
		rootMultiples = new ArrayList<ArrayList>();
		rootMultiples.add(0,new ArrayList<CRoot>());
		rootMultiples.add(1,new ArrayList<CRoot>());
		
		/** If the group is finite, we can construct the root system to all heights. */
		if(finite)
		{
			constructRootSystem(0);
			dim			= 2 * (int) numPosGenerators + rank;
			dimension	= Globals.intToString(dim);
		}
		else
		{
			dimension	= "Infinite";
		}
		
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
	}
	
	/**
	 * Determines the dimension of the representation defined by
	 * Dynkin labels associated to the given dynkinLabels.
	 *
	 * @param	dynkinLabels	The Dynkin labels of the representation.
	 * @return					The dimension of the presentation.
	 */
	public long dimOfRep(int[] dynkinLabels)
	{
		fraction	dim;
		int			p_dot_m;
		
		/** Preliminary checks. */
		if(!finite || dynkinLabels.length != rank)
			return 0;
		
		dim	= new fraction(1);
		
		for (ArrayList<CRoot> roots : rootSystem)
		{
			for(CRoot root : roots)
			{
				if(root.height() != 0)
				{
					p_dot_m = 0;
					for (int i = 0; i < rank; i++)
					{
						p_dot_m += dynkinLabels[i] * root.vector[i];
					}
					dim.multiply( p_dot_m + root.height() );
					dim.divide( root.height() );
				}
			}
		}
		
		return dim.asLong();
	}
	
	/**
	 * Get a root by its root vector.
	 *
	 * @param	vector	The root vector of the root we should get.
	 * @return			A pointer to the root if found, and null if not found.
	 */
	public CRoot getRoot(int[] vector)
	{
		/** Dirty hack to check for negative roots. */
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
		ArrayList<CRoot> roots;
		int		 index;
		
		if(rootHeight < 0)
		{
			return null;
		}
		
		/** If we haven't constructed the root system this far, do so now. */
		if(rootHeight > constructedHeight)
			constructRootSystem(rootHeight);
		
		/** Try to fetch the root. */
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
			out.writeObject(cartanMatrix);
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
	 * Returns true on succes, false on failure.
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
			if(Globals.sameMatrices(savedCM, cartanMatrix))
			{
				constructedHeight	= in.readInt();
				numPosRoots			= in.readLong();
				numPosGenerators	= in.readLong();
				rootSystem			= (ArrayList<ArrayList>) in.readObject();
				rootMultiples		= (ArrayList<ArrayList>) in.readObject();
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
	
	public void cancelRootSystemConstruction()
	{
		cancelConstruction = true;
	}
	
	/**********************************
	 * Private methods
	 **********************************/
	
	/**
	 * Construct the root system up to the given height.
	 *
	 * @param maxHeight	The height up to and including which we should construct the root system.
	 *					Construct the whole root system if maxHeight == 0.
	 */
	private void constructRootSystem(int maxHeight)
	{
		ArrayList<CRoot> prevRoots;
		ArrayList<CRoot> rootCache;
		CRoot	oldRoot;
		CRoot	newRoot;
		long	prevNumPosRoots;
		int		innerProduct;
		int		n_minus;
		int		newHeight;
		int		oldHeight;
		
		cancelConstruction = false;
		
		while(constructedHeight < maxHeight || maxHeight == 0)
		{
			prevRoots	    = rootSystem.get(constructedHeight);
			rootCache		= new ArrayList<CRoot>();
			prevNumPosRoots = numPosRoots;
			newHeight	    = constructedHeight + 1;
			
			System.out.println("constructing height " + newHeight);
			
			/**
			 * Try to add the simple roots to all the previous roots.
			 * The main formula employed here is
			 *
			 *		<alpha, beta> = n_minus - n_plus ,
			 *
			 * where alpha and beta are roots.
			 * For simple roots beta this reduces to
			 *
			 *		(alpha, beta) = n_minus - n_plus .
			 *
			 * If n_plus is positive, then alpha + beta is again a root.
			 */
			for(CRoot root : prevRoots)
			{
				for(CRoot simpleRoot : simpleRoots)
				{
					if(cancelConstruction)
						return;
					
					newRoot = root.plus(simpleRoot);
					
					/** First check if didn't do this root before. */
					if(rootCache.contains(newRoot))
						continue;
					else
						rootCache.add(newRoot);
					
					innerProduct = innerProduct(root,simpleRoot);
					if(innerProduct < 0)
					{
						/**
						 * n_plus is always positive, thus (root + simpleRoot) is a root
						 * Add it to the root table.
						 */
						addRoot(newRoot);
					}
					
					if(innerProduct >= 0)
					{
						/** Iterate through the previous heights to obtain n_minus. */
						n_minus		= 0;
						oldHeight	= constructedHeight - 1;
						oldRoot		= getRoot(root.minus(simpleRoot), oldHeight);
						while(oldRoot != null)
						{
							n_minus++;
							if(n_minus > innerProduct)
							{
								/** n_plus is positive. */
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
				
				/**
				 * Construct all the root multiples of this height
				 */
				ArrayList multiplesList	= new ArrayList<CRoot>();
				ArrayList properList	= rootSystem.get(newHeight);
				for (int i = 1; i < Math.floor(newHeight / 2) + 1; i++)
				{
					/** We're only interested in i's with zero divisor. */
					if(newHeight % i != 0)
						continue;
					int factor = newHeight / i;
					ArrayList<CRoot> roots = rootSystem.get(i);
					for(CRoot root : roots)
					{
						CRoot rootMultiple = root.times(factor);
						/**
						 * Don't add it if it's already in the 'proper' root list.
						 * Else we would count this one double.
						 */
						if(properList.contains(rootMultiple))
							continue;
						rootMultiple.coMult	= coMult(rootMultiple,false);
						multiplesList.add(rootMultiple);
					}
				}
				rootMultiples.add(newHeight,multiplesList);
				
			}
			else
			{
				/** We did nothing, and thus reached the highest root */
				break;
			}
		}
	}
	
	
	private int innerProduct(CRoot root1, CRoot root2)
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
	 * Adds a root to the root table and increments numPosRoots.
	 *
	 * @param	root	The root to add.
	 * @return			True if succesfull, false if it already was present.
	 */
	private boolean addRoot(CRoot root)
	{
		ArrayList<CRoot> roots;
		
		/** Where should we add it, if we shoud add it at all? */
		if(rootSystem.size() > root.height())
		{
			roots = rootSystem.get(root.height());
			if(roots.contains(root))
				return false;
		}
		else
		{
			roots = new ArrayList<CRoot>();
			rootSystem.add(root.height(),roots);
		}
		
		switch(root.height())
		{
			case 0:
				return true;
			case 1:
			{
				/** We don't need to calculate these for the simple roots. */
				root.mult	= 1;
				root.coMult	= new fraction(1);
				break;
			}
			default:
			{
				/** Determine its c_mult minus the root multiplicity. */
				fraction coMult = coMult(root,false);
				
				/**
				 * Determine its multiplicity.
				 *
				 * We split the Peterson formula into two symmetric halves,
				 * plus a remainder if the root height is even.
				 * Note that this only works because the Cartan matrix is symmetric!
				 */
				fraction multiplicity	= new fraction(0);
				int halfHeight			= (int) Math.ceil(((float) root.height()) / 2);
				for(int i = 1; i < halfHeight; i++)
					multiplicity.add(petersonPart(root, i));
				
				multiplicity.multiply(2);
				
				if(root.height() % 2 == 0)
					multiplicity.add(petersonPart(root, root.height() / 2));
				
				multiplicity.divide( innerProduct(root,root) - (2 * root.height() ) );
				multiplicity.subtract(coMult);
				root.mult	= multiplicity.asLong();
				root.coMult = coMult.plus(root.mult);
				if(multiplicity.asDouble() != (double) multiplicity.asInt() )
				{
					System.out.println(root.toString());
					System.out.println("actual mult: " + multiplicity.toString());
				}
				
			}
			
		}
		
		/** And add it to the table */
		roots.add(root);
		
		/** Increment numPosRoots */
		numPosRoots++;
		numPosGenerators += root.mult;
		
		return true;
	}
	
	/**
	 * Calculates the "co-multiplicity", i.e. the fractional sum of multiplicities
	 * of all fractional roots. Used in the Peterson formula.
	 *
	 * @param	root	The root whose co-multiplicity we should calculate.
	 * @return			The co-multiplicity.
	 */
	// TODO : possible move this to CRoot.
	private fraction coMult(CRoot root, boolean includeRoot)
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
	 * Calculate a part of the Peterson formula (in particular the r.h.s. for
	 * a given value of the height of one of the decomposition parts).
	 *
	 * @param	root 		The root for which we are calculating the multiplicity.
	 * @param	height 		The height of one of the decomposition parts.
	 * @return				A part of the Peterson formula, which is to be summed over.
	 */
	private fraction petersonPart(CRoot root, int height)
	{
		ArrayList<CRoot> betas	= rootSystem.get(height);
		ArrayList<CRoot> gammas	= rootSystem.get(root.height() - height);
		
		fraction multiplicity = petersonSubPart(root, betas, gammas);
		
		ArrayList<CRoot> betaMultiples	= rootMultiples.get(height);
		ArrayList<CRoot> gammaMultiples	= rootMultiples.get(root.height() - height);
		
		multiplicity.add(petersonSubPart(root,betaMultiples,gammaMultiples));
		multiplicity.add(petersonSubPart(root,betas,gammaMultiples));
		multiplicity.add(petersonSubPart(root,betaMultiples,gammas));
		
		return multiplicity;
	}
	
	private fraction petersonSubPart(CRoot root, ArrayList<CRoot> list1, ArrayList<CRoot> list2)
	{
		fraction multiplicity = new fraction(0);
		for(CRoot beta : list1)
		{
			innerRootLoop:
				for(CRoot gamma : list2)
				{
					for (int i = 0; i < rank; i++)
					{
						if(beta.vector[i] + gamma.vector[i] != root.vector[i])
							continue innerRootLoop;
					}
					fraction part = beta.coMult.times(gamma.coMult);
					part.multiply(innerProduct(beta,gamma));
					multiplicity.add(part);
				}
		}
		return multiplicity;
	}
	
	private void printRootTable(boolean multiples)
	{
		ArrayList<ArrayList> table;
		String type;
		if(!multiples)
		{
			table = rootSystem;
			type = "proper";
		}
		else
		{
			table = rootMultiples;
			type = "multiple";
		}
		
		for (int i = 0; i < table.size(); i++)
		{
			ArrayList<CRoot> roots = table.get(i);
			for(CRoot root : roots)
			{
				System.out.println(type + ", index: " + i + ", " + root.toString());
			}
		}
	}
}
