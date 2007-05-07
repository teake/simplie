/*
 * CGroup.java
 *
 * Created on 26 maart 2007, 17:17
 *
 */

package edu.rug.hep.simplie.group;

import edu.rug.hep.simplie.Globals;
import edu.rug.hep.simplie.math.fraction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.text.DecimalFormat;
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
	public final int[][]  cartanMatrix;
	/** The inverse of the cartan matrix. */
	public final fraction[][] cartanMatrixInv;
	/**
	 * The quadratic form matrix. Because we only work with simply-laced algebras,
	 * this is the same as the inverse of the cartan matrix.
	 */
	public final fraction[][] qFormMatrix;
	/** The determinant of the Cartan Matrix. */
	public final int		det;
	/** The rank of the group. */
	public final int		rank;
	/** The dimension of the group (i.e. the number of generators). Only set for finite groups. */
	public final int		dim;
	/** String value of dim. "Infinite" if the group is infinite. */
	public final String		dimension;
	/** The type of the group (e.g. "A1", "E6", etc) */
	public final String		type;
	/** Boolean indicating whethet the group is finite or not. */
	public final boolean	finite;
	/** The root system */
	public final CRootSystem rs;
	/** The Weyl vector of the group */
	public final CWeight weylVector;
	
	
	/*********************************
	 * Private properties
	 *********************************/
	
	private CHighestWeightRep hwRep;
	
	
	/**********************************
	 * Public methods
	 **********************************/
	
	/**
	 * Creates a new instance of CGroup.
	 * If the group is finite, the whole rootsystem is constructed.
	 *
	 * @param cartanMatrix    The Cartan matrix from which to construct the group.
	 */
	public CGroup(Matrix cartanMatrix)
	{
		Matrix	compareMatrix;
		String	tempType = null;
		
		// Do some preliminary checks
		if(cartanMatrix.getColumnDimension() != cartanMatrix.getRowDimension() || cartanMatrix.getColumnDimension() == 0)
		{
			rank	= 0;
			det		= 0;
		}
		else
		{
			rank	= cartanMatrix.getColumnDimension();
			det		= (int) Math.round(cartanMatrix.det());
		}
		if(det > 0)
			finite = true;
		else
			finite = false;
		
		this.cartanMatrix		= new int[rank][rank];
		this.cartanMatrixInv	= new fraction[rank][rank];
		this.qFormMatrix		= this.cartanMatrixInv;
		// Set the cartan matrix
		for(int i=0; i<rank; i++)
		{
			for(int j=0; j<rank; j++)
			{
				this.cartanMatrix[i][j] = (int) Math.round(cartanMatrix.get(i,j));
			}
		}
		// Set the inverse of the Cartan matrix if possible.
		if(rank != 0 && cartanMatrix.rank() == rank && det != 0)
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
		
		// Construct the Weyl vector
		int[] weylLabels = new int[rank];
		for (int i = 0; i < rank; i++)
		{
			weylLabels[i] = 1;
		}
		weylVector = new CWeight(weylLabels);
		
		// Try to determine the group type
		// TODO: make this algorithm find more types
		compareMatrix = Globals.regularMatrix(rank);
		if(rank == 0)
			tempType = "Empty";
		else if(Globals.sameMatrices(compareMatrix,cartanMatrix))
			tempType = "A";
		else if(rank > 4)
		{
			compareMatrix.set(0,3,-1);
			compareMatrix.set(3,0,-1);
			compareMatrix.set(0,1,0);
			compareMatrix.set(1,0,0);
			if(Globals.sameMatrices(compareMatrix,cartanMatrix))
				tempType = "E";
			else
			{
				compareMatrix = Globals.regularMatrix(rank);
				compareMatrix.set(rank-1,2,-1);
				compareMatrix.set(2,rank-1,-1);
				compareMatrix.set(rank-2,rank-1,0);
				compareMatrix.set(rank-1,rank-2,0);
				if(Globals.sameMatrices(compareMatrix,cartanMatrix))
					tempType = "E";
			}
		}
		if(tempType == null)
			tempType = "Unknown";
		else if(rank != 0)
			tempType += rank;
		type = tempType;
		
		// Set up the root system
		rs = new CRootSystem(this);
		
		// Determine the dimension.
		if(det > 0 || rank == 0)
		{
			dim			= 2 * (int) rs.numPosGenerators() + rank;
			dimension	= Globals.intToString(dim);
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
		fraction	dim;
		CWeight		highestWeight;
		
		// Preliminary checks.
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
	 * Cancels every process going on,
	 * which is currently only the construction of the rootsystem.
	 */
	public void cancelEverything()
	{
		rs.cancelConstruction();
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
				height.add(cartanMatrixInv[i][j].times(weightLabels[j]));
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
			output[j] = weightLabels[j] - cartanMatrix[j][i] * weightLabels[i];
		}
		return output;
	}
}
