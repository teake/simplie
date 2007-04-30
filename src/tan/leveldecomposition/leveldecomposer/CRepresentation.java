/*
 * CRepresentation.java
 *
 * Created on 10 april 2007, 10:49
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.*;
import tan.leveldecomposition.dynkindiagram.*;
import tan.leveldecomposition.group.CHighestWeightRep;
import tan.leveldecomposition.group.CWeight;

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
	/** The part of the rootvector corresponding to the regular subalgebra nodes. */
	public final int[] coLevels;
	/** The part of the rootvector corresponding to the disconnected disabled nodes. */
	public final int[] disLevels;
	
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
	
	private long outerSubMult;
	private long outerMult;
	private long rootMult;
	
	private final CHighestWeightRep disHwRep;
	private final CHighestWeightRep subHwRep;
	
	
	/**
	 * Creates a new instance of CRepresentation
	 *
	 * @param	dynkinLabels	The full dynkinlabels of the highest weight state.
	 * @param	levels			The part of the rootvector corresponding to the level nodes.
	 * @param	coLevels		The part of the rootvector corresponding to the regular subalgebra nodes.
	 * @param	length			The length of the associated root (i.e. the innerproduct with itself).
	 */
	public CRepresentation(int[] dynkinLabels, int[] levels, int[] coLevels, int length)
	{
		this.dynkinLabels	= dynkinLabels.clone();
		this.levels			= levels.clone();
		this.coLevels		= coLevels.clone();
		this.length			= length;
		this.rootMult		= 0;
		this.outerSubMult	= 0;
		this.outerMult		= 0;
		
		// Construct the whole root vector.
		this.rootVector	= new int[Globals.group.rank];
		for (int i = 0; i < coLevels.length; i++)
			rootVector[Globals.dd.translateCo(i)] = coLevels[i];
		for (int i = 0; i < levels.length; i++)
			rootVector[Globals.dd.translateLevel(i)] = levels[i];
		
		// Set the disconnected levels.
		this.disLevels = new int[Globals.disGroup.rank];
		for (int i = 0; i < disLevels.length; i++)
			disLevels[i] = rootVector[Globals.dd.translateDis(i)];
		
		// Calculate the height.
		int tHeight = 0;
		for (int i = 0; i < rootVector.length; i++)
		{
			tHeight += rootVector[i];
		}
		this.height = tHeight;
		
		// Split the dynkinlabels into labels of the regular subalgebra and
		// labels of the disconnected subalgebra.
		int[] translatedLabels	= new int[Globals.group.rank];
		this.subDynkinLabels	= new int[Globals.subGroup.rank];
		this.disDynkinLabels	= new int[Globals.disGroup.rank];
		for (int i = 0; i < dynkinLabels.length; i++)
			translatedLabels[Globals.dd.translateCo(i)] = dynkinLabels[i];
		for (int i = 0; i < disDynkinLabels.length; i++)
			disDynkinLabels[i] = translatedLabels[Globals.dd.translateDis(i)];
		for (int i = 0; i < subDynkinLabels.length; i++)
			subDynkinLabels[i] = translatedLabels[Globals.dd.translateSub(i)];
		
		// Instantiate the highest weight reps.
		subHwRep = new CHighestWeightRep(Globals.subGroup, subDynkinLabels);
		disHwRep = new CHighestWeightRep(Globals.disGroup, disDynkinLabels);
	}
	
	/**
	 * Returns the multiplicity of a weight that sits in the representation
	 * given by the subalgebra dynkin labels of this representation.
	 *
	 * @param	weightLabels	The dynkinlabels of the weight we want to know the multiplicity of.
	 * @return					Its multplicity, or 0 if it isn't weight in the subalgebra representation.
	 */
	public long getSubWeightMult(int[] weightLabels)
	{
		CWeight weight = subHwRep.getWeight(weightLabels);
		if(weight != null)
			return weight.getMult();
		else
			return 0;
	}
	
	/**
	 * Returns the multiplicity of a weight that sits in the representation
	 * given by the disconnected disabled dynkin labels of this representation.
	 *
	 * @param	weightLabels	The dynkinlabels of the weight we want to know the multiplicity of.
	 * @return					Its multplicity, or 0 if it isn't weight in the disconnected disabled representation.
	 */
	public long getDisWeightMult(int[] weightLabels)
	{
		CWeight weight = disHwRep.getWeight(weightLabels);
		if(weight != null)
			return weight.getMult();
		else
			return 0;
	}
	
	/** Sets the final outer multiplicty of this representation. */
	public void setOuterMult(long outerMult)
	{
		this.outerMult = outerMult;
	}
	
	/** Gets the final outer multiplicity of this representation. */
	public long getOuterMult()
	{
		return outerMult;
	}
	
	/**
	 * Sets the outer multiplicity of this representation, not taking its multiplicity
	 * of being a weight in another disconnected disabled representation into account.
	 */
	public void setOuterSubMult(long outerSubMult)
	{
		this.outerSubMult = outerSubMult;
	}
	
	/**
	 * Gets the outer multiplicity of this representation, not taking its multiplicity
	 * of being a weight in another disconnected disabled representation into account.
	 */
	public long getOuterSubMult()
	{
		return outerSubMult;
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
