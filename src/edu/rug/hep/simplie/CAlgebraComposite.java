/*
 * CAlgebraComposite.java
 *
 * Created on 18-jun-2007, 14:47:23
 *
 */

package edu.rug.hep.simplie;

import edu.rug.hep.simplie.dynkindiagram.*;
import edu.rug.hep.simplie.group.*;
import edu.rug.hep.simplie.math.*;

/**
 * The core SimpLie object. It stores the group disintegration
 * information from a decomposed Dynkin diagram. It also stores
 * the Dynkin diagram itself.
 * Implements DiagramListener to obtain the diagram information.
 * 
 * @author  Teake Nutma
 */
public class CAlgebraComposite implements DiagramListener
{
	/** CGroup object for the full group */
	public CGroup group;
	/** CGroup object for the regular subgroup */
	public CGroup subGroup;
	/** CGroup object for the internal subgroup */
	public CGroup intGroup;
	/** CGroup object for the non-level subgroup (which is equal to the direct product of subGroup x intGroup) */
	public CGroup coGroup;
	/** CDynkinDiagram object */
	public CDynkinDiagram dd;
	
	private int sign;
	private boolean locked;
	
	public CAlgebraComposite()
	{
		dd = new CDynkinDiagram();
		dd.addListener(this);
		this.sign = 1;
		this.locked = false;
	}
	
	@Override
	public void diagramChanged()
	{
		// Don't do anything if the algebras are locked.
		if(locked)
			return;
		
		// Only construct the full group if the new matrix is different.
		if(group == null || !Helper.sameMatrices(dd.cartanMatrix(), group.A))
			group = new CGroup(dd.cartanMatrix());
		
		// Only construct the cogroup if it is new and different from the full group.
		if(coGroup == null || !Helper.sameMatrices(dd.cartanSubMatrix("co"), coGroup.A))
		{
			if(Helper.sameMatrices(dd.cartanSubMatrix("co"),group.A))
				coGroup = group;
			else
				coGroup	= new CGroup(dd.cartanSubMatrix("co"));
		}
		
		// Only construct the subgroup if it is new and different from the cogroup.
		if(subGroup == null || !Helper.sameMatrices(dd.cartanSubMatrix("sub"), subGroup.A))
		{
			if(Helper.sameMatrices(dd.cartanSubMatrix("sub"),coGroup.A))
				subGroup = coGroup;
			else
				subGroup = new CGroup(dd.cartanSubMatrix("sub"));
		}
		
		// Only construct the intgroup if it is new and different from the cogroup.
		if(intGroup == null || !Helper.sameMatrices(dd.cartanSubMatrix("int"), intGroup.A))
		{
			if(Helper.sameMatrices(dd.cartanSubMatrix("int"),coGroup.A))
				intGroup = coGroup;
			else
				intGroup = new CGroup(dd.cartanSubMatrix("int"));
		}
		
		dd.setTitle(getDynkinDiagramType(false));
		dd.setTitleTeX(getDynkinDiagramType(true));
		
	}
	
	/**
	 * Checks if the sign used in the calculations (lowest / highest weights)
	 * is positive (highest weights) or negative (lowest weights).
	 * 
	 * @return  True if positive, false if negative.
	 */
	public boolean isSignPos()
	{
		if(sign == 1)
			return true;
		else
			return false;
	}
	
	/**
	 * Sets the sign used in the calculations (lowest / highest weights).
	 * 
	 * @param   positive	True for positive, false for negative.
	 */
	public void setSignPos(boolean positive)
	{
		if(locked)
			return;
		
		if(positive)
			this.sign = 1;
		else
			this.sign = -1;
	}
	
	/**
	 * Locks everything (eg the sign and the diagram cannot be changed),
	 * useful for when we are in the middle of a long calculation.
	 * 
	 * @param   locked  True: locks everything, false: unlock everything.
	 */
	public void setLocked(boolean locked)
	{
		this.locked = locked;
		dd.setLocked(locked);
	}
	
	/**
	 * Is the algebraComposite locked or not?
	 * 
	 * @return  True if it is locked, false otherwise.
	 */
	public boolean isLocked()
	{
		return this.locked;
	}
	
	/**
	 * Returns a string representing the type of decomposition of the full group into
	 * the regular subgroup and the disconnected subgroup.
	 *
	 * @return	String of the type "regular subgroup x disconnected subgroup representations in fullgroup".
	 * @see		#getDynkinDiagramType
	 */
	public String getDecompositionType(boolean TeX)
	{
		if(TeX)
			return coGroup.typeTeX + " representations in " + group.typeTeX;
		else
			return coGroup.type + " representations in " + group.type;
	}
	
	/**
	 * Returns a string representing the regular subgroup and the disconnected subgroup of the full group.
	 *
	 * @return	String of the type "fullgroup as regular subgroup x disconnected subgroup".
	 * @see		#getDecompositionType
	 */
	public String getDynkinDiagramType(boolean TeX)
	{
		String output = "";
		
		if(TeX)
		{
			output += group.typeTeX;
			if(group.rank > coGroup.rank)
				output += " decomposed as " + coGroup.typeTeX;
		}
		else
		{
			output += group.type;
			if(group.rank > coGroup.rank)
				output += " decomposed as " + coGroup.type;
		}
		return output;
	}
	
	/*********************************
	 * Methods for calculating
	 * root vectors, dynkin labels and such
	 *********************************/
	
	
	/**
	 * Returns the level-part of a root vector
	 * 
	 * @param   rootVector	The root vector of which the level part is to be returned.
	 * @return		The level part of the root vector.
	 */
	public int[] levels(int[] rootVector)
	{
		int[] levels = new int[group.rank - coGroup.rank];
		for (int i = 0; i < levels.length; i++)
		{
			levels[i] = rootVector[dd.translateLevel(i)];
		}
		return levels;
	}
	
	/**
	 * Calculates Dynkin labels of the regular subalgebra
	 * given a root vector.
	 * 
	 * @param   rootVector	The root vector of the weight.
	 * @return		The subalgebra part of the associated Dynkin labels.
	 */
	public int[] subDynkinLabels(int[] rootVector)
	{
		int[] subDynkinLabels = new int[subGroup.rank];
		int[] fullLabels = group.rootToWeight(rootVector);
		for (int i = 0; i < subDynkinLabels.length; i++)
		{
			subDynkinLabels[i] = sign * fullLabels[dd.translateSub(i)];
		}
		return subDynkinLabels;
	}
	
	/** 
	 * Calculates Dynkin labels of the internal algebra
	 * given a root vector.
	 * 
	 * @param   rootVector	The root vector of the weight.
	 * @return		The internal algebra part of the associated Dynkin labels.
	 */
	public int[] intDynkinLabels(int[] rootVector)
	{
		int[] intDynkinLabels = new int[intGroup.rank];
		int[] fullLabels = group.rootToWeight(rootVector);
		for (int i = 0; i < intDynkinLabels.length; i++)
		{
			intDynkinLabels[i] = sign * fullLabels[dd.translateInt(i)];
		}
		return intDynkinLabels;
	}
	
	/**
	 * Calculates Dynkin labels of the co-algebra
	 * given a root vector.
	 * 
	 * @param   rootVector	The root vector of the weight.
	 * @return		The co-algebra part of the associated Dynkin labels.
	 */
	public int[] coDynkinLabels(int[] rootVector)
	{
		int[] coDynkinLabels = new int[coGroup.rank];
		int[] fullLabels = group.rootToWeight(rootVector);
		for (int i = 0; i < coDynkinLabels.length; i++)
		{
			coDynkinLabels[i] = sign * fullLabels[dd.translateCo(i)];
		}
		return coDynkinLabels;
	}
	
	/**
	 * Returns the full root vector from the levels and the co-Dynkin labels.
	 * 
	 * @param   levels	    The levels-part of the root vector.
	 * @param   coDynkinLabels  Co-algebra Dynkin labels of the associated root vector.
	 * @return		    The full root vector calculated from the two parameters.
	 */ 
	public int[] rootVector(int[] levels, int[] coDynkinLabels)
	{
		int[] rootVector = new int[group.rank];
		
		fraction[] coLevels = calculateCoLevels(levels, coDynkinLabels);
		for (int i = 0; i < coLevels.length; i++)
		{
			rootVector[dd.translateCo(i)] = coLevels[i].asInt();
		}
		for (int i = 0; i < levels.length; i++)
		{
			rootVector[dd.translateLevel(i)] = levels[i];
		}
		return rootVector;
	}
	
	/********************************
	 * Methods for representation
	 * calculation
	 ********************************/
	
	/** Returns the root length */
	public fraction calculateRootLength(int[] levels, int[] dynkinLabels)
	{
		fraction rootLength = new fraction(0);
		int[] levelComponents	= calculateLevelComponents(levels);
		
		for(int i=0; i < dynkinLabels.length; i++)
		{
			for(int j=0; j < dynkinLabels.length; j++)
			{
				fraction contribution = new fraction(group.simpleRootNorms[dd.translateCo(j)],coGroup.simpleRootNorms[j]);
				contribution.multiply(dynkinLabels[i] * dynkinLabels[j] - levelComponents[i] * levelComponents[j]);
				rootLength.add( coGroup.G[i][j].times(contribution) );
			}
		}
		
		for(int i=0; i < levels.length; i++)
		{
			for(int j=0; j < levels.length; j++)
			{
				rootLength.add( group.B[dd.translateLevel(i)][dd.translateLevel(j)] * levels[i] * levels[j] );
			}
		}
		
		return rootLength;		
	}
	
	/** Returns the levels of the co-algebra. */
	public fraction[] calculateCoLevels(int[] levels, int[] dynkinLabels)
	{
		fraction[] coLevels		= new fraction[coGroup.rank];
		int[] levelComponents	= calculateLevelComponents(levels);
		
		for(int i=0; i < coLevels.length; i++)
		{
			coLevels[i] = new fraction(0);
			for(int j=0; j < coGroup.rank; j++)
			{
				coLevels[i].add(coGroup.invA[j][i].times(sign * dynkinLabels[j] - levelComponents[j]));
			}
		}
		
		return coLevels;
	}
	
	/** Calculates the contraction of the levels with the Cartan matrix. */
	public int[] calculateLevelComponents(int[] levels)
	{
		int[] levelComponents = new int[coGroup.rank];
		
		for(int i=0; i < levelComponents.length; i++)
		{
			levelComponents[i] = 0;
			for(int j=0; j < levels.length; j++)
			{
				levelComponents[i] += group.A[dd.translateLevel(j)][dd.translateCo(i)] * levels[j];
			}
		}
		return levelComponents;
	}
	
}
