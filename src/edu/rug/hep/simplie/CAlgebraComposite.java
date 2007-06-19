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
 * @author Teake Nutma
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
	
	public void diagramChanged()
	{
		if(locked)
			return;
		
		if(group == null || !Helper.sameMatrices(dd.cartanMatrix(), group.A))
			group = new CGroup(dd.cartanMatrix());
		
		subGroup	= new CGroup(dd.cartanSubMatrix("sub"));
		intGroup	= new CGroup(dd.cartanSubMatrix("int"));
		coGroup		= new CGroup(dd.cartanSubMatrix("co"));
	}
	
	public boolean isSignPos()
	{
		if(sign == 1)
			return true;
		else
			return false;
	}
	
	public void setSignPos(boolean positive)
	{
		if(locked)
			return;
		
		if(positive)
			this.sign = 1;
		else
			this.sign = -1;
	}
	
	public void setLocked(boolean locked)
	{
		this.locked = locked;
		dd.setLocked(locked);
	}
	
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
	public String getDecompositionType()
	{
		String output;
		
		output = subGroup.type;
		if(intGroup.rank != 0)
			output += " x " + intGroup.type;
		output += " representations in " + group.type;
		
		return output;
	}
	
	/**
	 * Returns a string representing the regular subgroup and the disconnected subgroup of the full group.
	 *
	 * @return	String of the type "fullgroup as regular subgroup x disconnected subgroup".
	 * @see		#getDecompositionType
	 */
	public String getDynkinDiagramType()
	{
		String output;
		
		output = group.type;
		if(subGroup.rank != group.rank)
			output += " as " + subGroup.type;
		if(intGroup.rank != 0)
			output += " x " + intGroup.type;
		
		return output;
	}
	
	/*********************************
	 * Methods for calculating
	 * root vectors, dynkin labels and such
	 *********************************/
	
	public int[] levels(int[] rootVector)
	{
		int[] levels = new int[group.rank - coGroup.rank];
		for (int i = 0; i < levels.length; i++)
		{
			levels[i] = rootVector[dd.translateLevel(i)];			
		}
		return levels;
	}
	
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
	
	public int[] rootVector(int[] levels, int[] subDynkinLabels, int[] intDynkinLabels)
	{
		int[] rootVector = new int[group.rank];
		
		// Construct the full labels
		int[] labels = new int[group.rank];
		for (int i = 0; i < subDynkinLabels.length; i++)
		{
			labels[dd.translateSub(i)] = subDynkinLabels[i];
		}
		for (int i = 0; i < intDynkinLabels.length; i++)
		{
			labels[dd.translateInt(i)] = intDynkinLabels[i];
		}
		int[] dynkinLabels = new int[coGroup.rank];
		for (int i = 0; i < dynkinLabels.length; i++)
		{
			dynkinLabels[i] = labels[dd.translateCo(i)];
		}
		
		fraction[] coLevels = calculateCoLevels(levels, dynkinLabels);
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
	
	/** Returns the actual root length */
	public int calculateRootLength(int[] levels, fraction[] coLevels)
	{
		fraction rootLength = new fraction(0);
		
		for(int i=0; i < coLevels.length; i++)
		{
			for(int j=0; j < coLevels.length; j++)
			{
				rootLength.add( coLevels[i].times(coLevels[j].times(group.B[dd.translateCo(i)][dd.translateCo(j)] )));
			}
		}
		for(int i=0; i < levels.length; i++)
		{
			for(int j=0; j < levels.length; j++)
			{
				rootLength.add( group.B[dd.translateLevel(i)][dd.translateLevel(j)] * levels[i] * levels[j] );
			}
		}
		for(int i=0; i < coLevels.length; i++)
		{
			for(int j=0; j < levels.length; j++)
			{
				rootLength.add( coLevels[i].times(2 * levels[j] * group.B[dd.translateCo(i)][dd.translateLevel(j)] ));
			}
		}
		
		return rootLength.asInt();
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
