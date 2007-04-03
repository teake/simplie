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
	private int	numPosRoots;
	/** The table containing all the (positive) roots. */
	private ArrayList<ArrayList> rootTable;
	
	
	
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
		CRoot	simpleRoot;
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
		
		rootTable = new ArrayList<ArrayList>();
		
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
			dim			= 2 * numPosRoots + rank;
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
	public int dimOfRep(int[] dynkinLabels)
	{
		fraction	dim;
		int			p_dot_m;
		
		/** Preliminary checks. */
		if(!finite || dynkinLabels.length != rank)
			return 0;
		
		dim	= new fraction(1);
		
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
					dim.multiply( p_dot_m + root.height() );
					dim.divide( root.height() );
				}
			}
		}
		
		return dim.asInt();
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
		ArrayList<CRoot> rootCache;
		CRoot	oldRoot;
		CRoot	newRoot;
		int		prevNumPosRoots;
		int		innerProduct;
		int		n_minus;
		int		newHeight;
		int		oldHeight;
		
		while(constructedHeight < maxHeight || maxHeight == 0)
		{
			prevRoots	    = rootTable.get(constructedHeight);
			rootCache		= new ArrayList<CRoot>();
			prevNumPosRoots = numPosRoots;
			newHeight	    = constructedHeight + 1;
			
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
		return innerProduct(root1.vector, root2.vector);
	}
	
	private int innerProduct(int[] vector1, int[] vector2)
	{
		int result = 0;
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				result += cartanMatrix[i][j] * vector1[i] * vector2[j];
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
		
		switch(root.height())
		{
			case 0:
				return true;
			case 1:
			{
				/** We don't need to calculate these for the simple roots. */
				root.mult	= 1;
				root.c_mult	= new fraction(1);
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
				{
					ArrayList<CRoot> betas	= rootTable.get(i);
					ArrayList<CRoot> gammas	= rootTable.get(root.height() - i);
					for(CRoot beta : betas)
					{
						for(CRoot gamma : gammas)
						{
							if(beta.plus(gamma).equals(root))
							{
								fraction part = beta.c_mult.times(gamma.c_mult);
								part.multiply(innerProduct(beta,gamma));
								multiplicity.add(part);
							}
						}
					}
				}
				multiplicity.multiply(2);
				if(root.height() % 2 == 0)
				{
					ArrayList<CRoot> betas	= rootTable.get(root.height() / 2);
					ArrayList<CRoot> gammas	= rootTable.get(root.height() / 2);
					for(CRoot beta : betas)
					{
						for(CRoot gamma : gammas)
						{
							if(beta.plus(gamma).equals(root))
							{
								fraction part = beta.c_mult.times(gamma.c_mult);
								part.multiply(innerProduct(beta,gamma));
								multiplicity.add(part);
							}
						}
					}
				}
				
				multiplicity.divide( innerProduct(root,root) - (2 * root.height() ) );
				multiplicity.subtract(coMult);
				root.mult	= multiplicity.asLong();
				root.c_mult = coMult.plus(root.mult);
				//System.out.println(multiplicity.toString());
				//System.out.println(multiplicity.asDouble());
				
			}
			
		}
		
		/** And add it to the table */
		roots.add(root);
		
		/** Increment numPosRoots */
		numPosRoots++;
		
		return true;
	}
	
	/**
	 * Calculates the "co-multiplicity", i.e. the fractional sum of multiplicities
	 * of all fractional roots. Used in the Peterson formula.
	 *
	 * @param	root	The root whose co-multiplicity we should calculate.
	 * @return			The co-multiplicity.
	 */
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
	
	private void printRootTable()
	{
		for(ArrayList<CRoot> roots : rootTable)
		{
			for(CRoot root : roots)
			{
				System.out.println(root.toString());
			}
		}
	}
}
