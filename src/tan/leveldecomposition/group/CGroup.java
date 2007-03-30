/*
 * CGroup.java
 *
 * Created on 26 maart 2007, 17:17
 *
 */

package tan.leveldecomposition.group;

import tan.leveldecomposition.*;
import java.util.ArrayList;
import java.text.DecimalFormat;
import Jama.Matrix;
import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

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
	/** The determinant of the Cartan Matrix. */
	public int	    det;
	/** The rank of the group. */
	public int	    rank;
	/** The dimension of the group (i.e. the number of generators). Only set for finite groups. */
	public int	    dim;
	/** String value of dim. "Infinite" if the group is infinite. */
	public String   dimension;
	/** The type of the group (e.g. "A1", "E6", etc) */
	public String   type;
	/** Boolean indicating whethet the group is finite or not. */
	public boolean  finite;
	
	/*********************************
	 * Private properties
	 *********************************/
	
	/** The height up to and included to which we constructed the root system. */
	private int		    constructedHeight;
	/** The number of positive roots constructed so far. */
	private int		    numPosRoots;
	/** The table containing all the (positive) roots. */
	private ArrayList<ArrayList>  rootTable;
	
	
	
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
		ArrayList<CRoot> csaRoots;
		CRoot		 simpleRoot;
		Matrix		 compareMatrix;
		
		/** Do some preliminary checks */
		if(cartanMatrix.getColumnDimension() != cartanMatrix.getRowDimension())
			return;
		if(cartanMatrix.getColumnDimension() == 0)
		{
			type	= "Trivial";
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
		
		rootTable   = new ArrayList<ArrayList>();
		
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
		constructedHeight = 1;
		
		/** If the group is finite, we can construct the root system to all heights. */
		if(finite)
		{
			constructRootSystem(0);
			dim		= 2 * numPosRoots + rank;
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
	 * @param dynkinLabels  The Dynkin labels of the representation.
	 * @return		    The dimension of the presentation.
	 */
	public int dimOfRep(int[] dynkinLabels)
	{
		float	dim;
		float	p_dot_m;
		CRoot	weight;
		
		/** Preliminary checks. */
		if(!finite || dynkinLabels.length != rank)
			return 0;
		
		dim	= 1;
		weight	= new CRoot(dynkinLabels);
		
		for (ArrayList<CRoot> roots : rootTable)
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
					dim = dim * ( ( p_dot_m / root.height() ) + 1);
				}
			}
		}
		
		return Math.round(dim);
	}
	
	/**
	 * Get a root by its root vector.
	 *
	 * @param vector	 The root vector of the root we should get.
	 * @return		 A pointer to the root if found, and null if not found.
	 */
	public CRoot getRoot(int[] vector)
	{
		/** Dirty hack to check for negative roots. */
		// TODO: implement this better.
		if(vector.length != 0 && vector[vector.length-1] < 0)
		{
			for (int i = 0; i < vector.length; i++)
			{
				vector[i] = -vector[i];
			}
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
		if(rootTable.size() > rootHeight)
		{
			roots = rootTable.get(rootHeight);
			index = roots.indexOf(rootToGet);
			if(index != -1)
			{
				return roots.get(index);
			}
		}
		return null;
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
		ArrayList<CRoot> simpleRoots = rootTable.get(1);
		ArrayList<CRoot> prevRoots;
		CRoot		oldRoot;
		CRoot		newRoot;
		int		prevNumPosRoots;
		int		innerProduct;
		int		n_minus;
		int		newHeight;
		int		oldHeight;
		
		while(constructedHeight < maxHeight || maxHeight == 0)
		{
			prevRoots	    = rootTable.get(constructedHeight);
			prevNumPosRoots = numPosRoots;
			newHeight	    = constructedHeight + 1;
			
			/**
			 * Try to add the simple roots to all the previous roots.
			 * The main formula employed here is
			 *
			 *	    <alpha, beta> = n_minus - n_plus ,
			 *
			 * where alpha and beta are roots.
			 * For simple roots beta this reduces to
			 *
			 *	    (alpha, beta) = n_minus - n_plus .
			 *
			 * If n_plus is positive, then alpha + beta is again a root.
			 */
			for(CRoot root : prevRoots)
			{
				for(CRoot simpleRoot : simpleRoots)
				{
					newRoot = root.plus(simpleRoot);
					
					/** First check if the new root isn't already present. */
					if(rootTable.size() > newHeight)
					{
						if(rootTable.get(newHeight).contains(newRoot))
							continue;
					}
					
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
						n_minus	    = 0;
						oldHeight   = constructedHeight - 1;
						oldRoot	    = getRoot(root.minus(simpleRoot), oldHeight);
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
		if(rootTable.size() > root.height())
		{
			roots = rootTable.get(root.height());
			if(roots.contains(root))
				return false;
		}
		else
		{
			roots = new ArrayList<CRoot>();
			rootTable.add(root.height(),roots);
		}
		
		if(root.height() > 0)
		{
			/** Increment numPosRoots */
			numPosRoots++;
			
			if(root.height() == 1)
			{
				/** We don't need to calculate these for the simple roots. */
				root.mult   = 1;
				root.c_mult = new Fraction(1,1);
			}
			else
			{
				/** Determine its c_mult minus the root multiplicity. */
				// First see how far we should sum.
				int highestK = 0;
				for (int i = 0; i < rank; i++)
				{
					if(root.vector[i] > highestK)
						highestK = root.vector[i];
				}
				// Now sum over all fractional roots.
				Fraction c_mult	= new Fraction(0,1);
				for (int i = 2; i < highestK + 1; i++)
				{
					CRoot divRoot = root.div(i);
					if(divRoot != null)
					{
						CRoot alpha = getRoot(divRoot);
						if(alpha != null)
						{
							c_mult = c_mult.plus(new Fraction(alpha.mult,i));
						}
					}
				}
				
				/** Determine its multiplicity. */
				Fraction petersonNumerator = new Fraction(0,1);
				for(int i = 1; i < root.height(); i++)
				{
					ArrayList<CRoot> betas	= rootTable.get(i);
					ArrayList<CRoot> gammas	= rootTable.get(root.height() - i);
					for(CRoot beta : betas)
					{
						for(CRoot gamma : gammas)
						{
							if(beta.plus(gamma).equals(root))
							{
								Fraction c_betagamma = beta.c_mult.times(gamma.c_mult);
								Fraction part = c_betagamma.times(innerProduct(beta,gamma));
								petersonNumerator = petersonNumerator.plus(part);
							}
						}
					}
				}
				Fraction mult = (petersonNumerator.dividedBy( innerProduct(root,root) - (2 * root.height()) )).minus(c_mult);
				//System.out.println(mult.toString());
				root.mult = Math.round(mult.asDouble());
				//System.out.println(root.mult);
				root.c_mult = c_mult.plus(root.mult);
			}
		}
		/** And add it to the table */
		roots.add(root);
		return true;
	}
	
	private void printRootTable()
	{
		boolean first;
		
		for(ArrayList<CRoot> roots : rootTable)
		{
			first = true;
			for(CRoot root : roots)
			{
				if(first)
				{
					System.out.println("====== height: " + root.height() + " ========");
					first = false;
				}
				System.out.println(
						"vector: " + Globals.intArrayToString(root.vector) +
						", mult: " + root.mult +
						", c_mult: " + root.c_mult
						);
			}
		}
	}
}
