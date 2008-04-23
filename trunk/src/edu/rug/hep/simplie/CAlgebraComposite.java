/*
 * CAlgebraComposite.java
 *
 * Created on 18-jun-2007, 14:47:23
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

package edu.rug.hep.simplie;

import edu.rug.hep.simplie.dynkindiagram.*;
import edu.rug.hep.simplie.algebra.*;
import edu.rug.hep.simplie.math.*;

/**
 * The core SimpLie object. It stores the algebra disintegration
 * information from a decomposed Dynkin diagram. It also stores
 * the Dynkin diagram itself.
 * Implements DiagramListener to obtain the diagram information.
 * 
 * @author  Teake Nutma
 */
public class CAlgebraComposite implements DiagramListener
{
	/** CAlgebra object for the full algebra */
	public CAlgebra algebra;
	/** CAlgebra object for the regular subalgebra */
	public CAlgebra subAlgebra;
	/** CAlgebra object for the internal subalgebra */
	public CAlgebra intAlgebra;
	/** CAlgebra object for the non-level subalgebra (which is equal to the direct product of subAlgebra x intAlgebra) */
	public CAlgebra coAlgebra;
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
		diagramChanged();
	}
	
	public void diagramChanged()
	{
		// Don't do anything if the algebras are locked.
		if(locked)
			return;
		
		// Only construct the full algebra if the new matrix is different.
		if(algebra == null || !Helper.sameMatrices(dd.cartanMatrix(), algebra.A))
			algebra = new CAlgebra(dd.cartanMatrix());
		
		// Only construct the coalgebra if it is new and different from the full algebra.
		if(coAlgebra == null || !Helper.sameMatrices(dd.cartanSubMatrix("co"), coAlgebra.A))
		{
			if(Helper.sameMatrices(dd.cartanSubMatrix("co"),algebra.A))
				coAlgebra = algebra;
			else
				coAlgebra	= new CAlgebra(dd.cartanSubMatrix("co"));
		}
		
		// Only construct the subalgebra if it is new and different from the coalgebra.
		if(subAlgebra == null || !Helper.sameMatrices(dd.cartanSubMatrix("sub"), subAlgebra.A))
		{
			if(Helper.sameMatrices(dd.cartanSubMatrix("sub"),coAlgebra.A))
				subAlgebra = coAlgebra;
			else
				subAlgebra = new CAlgebra(dd.cartanSubMatrix("sub"));
		}
		
		// Only construct the intalgebra if it is new and different from the coalgebra.
		if(intAlgebra == null || !Helper.sameMatrices(dd.cartanSubMatrix("int"), intAlgebra.A))
		{
			if(Helper.sameMatrices(dd.cartanSubMatrix("int"),coAlgebra.A))
				intAlgebra = coAlgebra;
			else
				intAlgebra = new CAlgebra(dd.cartanSubMatrix("int"));
		}
		
		dd.setTitle("Dynkin diagram of " + getDynkinDiagramType(false));
		dd.setTitleTeX("Dynkin diagram of " + getDynkinDiagramType(true));
		
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
	 * Returns a string representing the type of decomposition of the full algebra into
	 * the regular subalgebra and the disconnected subalgebra.
	 *
	 * @return	String of the type "regular subalgebra x disconnected subalgebra representations in fullalgebra".
	 * @see		#getDynkinDiagramType
	 */
	public String getDecompositionType(boolean TeX)
	{
		if(TeX)
			return coAlgebra.typeTeX + " representations in " + algebra.typeTeX;
		else
			return coAlgebra.type + " representations in " + algebra.type;
	}
	
	/**
	 * Returns a string representing the regular subalgebra and the disconnected subalgebra of the full algebra.
	 *
	 * @return	String of the type "fullalgebra as regular subalgebra x disconnected subalgebra".
	 * @see		#getDecompositionType
	 */
	public String getDynkinDiagramType(boolean TeX)
	{
		String output = "";
		
		if(TeX)
		{
			output += algebra.typeTeX;
			if(algebra.rank > coAlgebra.rank)
				output += " decomposed as " + coAlgebra.typeTeX;
		}
		else
		{
			output += algebra.type;
			if(algebra.rank > coAlgebra.rank)
				output += " decomposed as " + coAlgebra.type;
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
		int[] levels = new int[algebra.rank - coAlgebra.rank];
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
		int[] subDynkinLabels = new int[subAlgebra.rank];
		int[] fullLabels = algebra.rootToWeight(rootVector);
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
		int[] intDynkinLabels = new int[intAlgebra.rank];
		int[] fullLabels = algebra.rootToWeight(rootVector);
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
		int[] coDynkinLabels = new int[coAlgebra.rank];
		int[] fullLabels = algebra.rootToWeight(rootVector);
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
		int[] rootVector = new int[algebra.rank];
		
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
				fraction contribution = new fraction(algebra.halfNorms[dd.translateCo(j)],coAlgebra.halfNorms[j]);
				contribution.multiply(dynkinLabels[i] * dynkinLabels[j] - levelComponents[i] * levelComponents[j]);
				rootLength.add( coAlgebra.G[i][j].times(contribution) );
			}
		}
		
		for(int i=0; i < levels.length; i++)
		{
			for(int j=0; j < levels.length; j++)
			{
				rootLength.add( algebra.B[dd.translateLevel(i)][dd.translateLevel(j)] * levels[i] * levels[j] );
			}
		}
		
		return rootLength;		
	}
	
	/** Returns the levels of the co-algebra. */
	public fraction[] calculateCoLevels(int[] levels, int[] dynkinLabels)
	{
		fraction[] coLevels		= new fraction[coAlgebra.rank];
		int[] levelComponents	= calculateLevelComponents(levels);
		
		for(int i=0; i < coLevels.length; i++)
		{
			coLevels[i] = new fraction(0);
			for(int j=0; j < coAlgebra.rank; j++)
			{
				coLevels[i].add(coAlgebra.invA[j][i].times(sign * dynkinLabels[j] - levelComponents[j]));
			}
		}
		
		return coLevels;
	}
	
	/** Calculates the contraction of the levels with the Cartan matrix. */
	public int[] calculateLevelComponents(int[] levels)
	{
		int[] levelComponents = new int[coAlgebra.rank];
		
		for(int i=0; i < levelComponents.length; i++)
		{
			levelComponents[i] = 0;
			for(int j=0; j < levels.length; j++)
			{
				levelComponents[i] += algebra.A[dd.translateLevel(j)][dd.translateCo(i)] * levels[j];
			}
		}
		return levelComponents;
	}
	
}
