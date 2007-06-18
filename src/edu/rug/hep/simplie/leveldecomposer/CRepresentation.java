/*
 * CRepresentation.java
 *
 * Created on 10 april 2007, 10:49
 *
 */

package edu.rug.hep.simplie.leveldecomposer;

import edu.rug.hep.simplie.*;
import edu.rug.hep.simplie.group.CHighestWeightRep;
import edu.rug.hep.simplie.group.CWeight;

/**
 * A class for storing valid regular subalgebra representations.
 * This class is used in CAutoLevelScanner before sending the data to the table.
 *
 * @see		CAutoLevelScanner
 * @author	Teake Nutma
 */
public class CRepresentation implements Comparable<CRepresentation>
{
	/** The vector of the root associated to the highest weight state of this representation. */
	public final int[] rootVector;
	/** The part of the rootvector corresponding to the level nodes. */
	public final int[] levels;
	
	/** The full dynkinlabels of the highest weight state. */
	public final int[] dynkinLabels;
	/** The regular subalgebra part of the full dynkinlabels. */
	public final int[] subDynkinLabels;
	/** The disconnected disabled part of the full dynkinlabels. */
	public final int[] disDynkinLabels;
	
	/** The length of the associated root (i.e. the innerproduct with itself). */
	public final int length;
	/** The height of the associated root. */
	public final int height;
	/** The height of the highest weight state */
	public final int weightHeight;
	
	/** The number of indices */
	public final int numIndices;
	
	private long outerMult;
	private long rootMult;
	
	private final CHighestWeightRep hwRep;
	
	private final CAlgebraComposite algebras;
	
	/**
	 * Creates a new instance of CRepresentation
	 *
	 * @param	dynkinLabels	The full dynkinlabels of the highest weight state.
	 * @param	levels			The part of the rootvector corresponding to the level nodes.
	 * @param	coLevels		The part of the rootvector corresponding to the regular subalgebra nodes.
	 * @param	length			The length of the associated root (i.e. the innerproduct with itself).
	 */
	public CRepresentation(CAlgebraComposite algebras, int[] dynkinLabels, int[] levels, int[] coLevels, int length, boolean posSignConvention)
	{
		this.algebras		= algebras;
		this.dynkinLabels	= dynkinLabels.clone();
		this.levels			= levels.clone();
		this.length			= length;
		this.rootMult		= 0;
		this.outerMult		= 0;
		
		// Construct the whole root vector.
		this.rootVector	= new int[algebras.group.rank];
		for (int i = 0; i < coLevels.length; i++)
			rootVector[algebras.dd.translateCo(i)] = coLevels[i];
		for (int i = 0; i < levels.length; i++)
			rootVector[algebras.dd.translateLevel(i)] = levels[i];
		
		// Calculate the height.
		int tHeight = 0;
		for (int i = 0; i < rootVector.length; i++)
		{
			tHeight += rootVector[i];
		}
		this.height = tHeight;
		
		// Split the dynkinlabels into labels of the regular subalgebra and
		// labels of the disconnected subalgebra.
		int[] translatedLabels	= new int[algebras.group.rank];
		this.subDynkinLabels	= new int[algebras.subGroup.rank];
		this.disDynkinLabels	= new int[algebras.intGroup.rank];
		for (int i = 0; i < dynkinLabels.length; i++)
			translatedLabels[algebras.dd.translateCo(i)] = dynkinLabels[i];
		for (int i = 0; i < disDynkinLabels.length; i++)
			disDynkinLabels[i] = translatedLabels[algebras.dd.translateDis(i)];
		for (int i = 0; i < subDynkinLabels.length; i++)
			subDynkinLabels[i] = translatedLabels[algebras.dd.translateSub(i)];
		
		// Instantiate the highest weight rep.
		hwRep = new CHighestWeightRep(algebras.coGroup, dynkinLabels);
		weightHeight = hwRep.highestHeight;
		
		// Calculate the number of indices.
		int tempNumIndices = 0;
		for (int i = 0; i < subDynkinLabels.length; i++)
		{
			int j = i;
			if(posSignConvention)
				j = subDynkinLabels.length - i - 1;
			tempNumIndices += subDynkinLabels[j] * (i+1);
		}
		numIndices = tempNumIndices;
	}
	
	/**
	 * Returns the multiplicity of a weight that sits in the representation
	 * given by the dynkin labels of this representation.
	 *
	 * @param	weightLabels	The dynkinlabels of the weight we want to know the multiplicity of.
	 * @return					Its multplicity, or 0 if it isn't weight in this representation.
	 */
	public long getWeightMult(int[] weightLabels)
	{
		CWeight weight = hwRep.getWeight(weightLabels);
		if(weight != null)
			return weight.getMult();
		else
			return 0;
	}
	
	/** Sets the outer multiplicty of this representation. */
	public void setOuterMult(long outerMult)
	{
		this.outerMult = outerMult;
	}
	
	/** Gets the outer multiplicity of this representation. */
	public long getOuterMult()
	{
		return outerMult;
	}
	
	/** Sets the multiplicity of the associated root in the full group. */
	public void setRootMult(long rootMult)
	{
		this.rootMult = rootMult;
	}
	
	/** Gets the multiplicity of the associated root in the full group. */
	public long getRootMult()
	{
		return rootMult;
	}
	
	/** Sort representations by means of the height of their associated roots. */
	public int compareTo(CRepresentation rep)
	{
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		
		if(height > rep.height)
			return AFTER;
		if(height < rep.height)
			return BEFORE;
		
		return EQUAL;
	}
}
