/*
 * CRepresentation.java
 *
 * Created on 10 april 2007, 10:49
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.*;
import tan.leveldecomposition.dynkindiagram.*;

/**
 *
 * @author Teake Nutma
 */
public class CRepresentation implements Comparable<CRepresentation>
{
	public final int[] rootVector;
	public final int[] dynkinLabels;
	public final int[] subDynkinLabels;
	public final int[] disDynkinLabels;
	public final int[] disLevels;
	public final int[] levels;
	public final int[] coLevels;
	public final int length;
	public final int height;
	private long outerSubMult;
	private long outerMult;
	private long rootMult;
	
	
	/** Creates a new instance of CRepresentation */
	public CRepresentation(int[] dynkinLabels, int[] levels, int[] coLevels, int length)
	{
		this.dynkinLabels	= dynkinLabels.clone();
		this.levels			= levels.clone();
		this.coLevels		= coLevels.clone();
		this.length			= length;
		this.rootMult		= 0;
		this.outerSubMult	= 0;
		this.outerMult		= 0;
		
		/** Construct the whole root vector */
		this.rootVector	= new int[Globals.group.rank];
		for (int i = 0; i < Globals.coGroup.rank; i++)
			rootVector[DynkinDiagram.translateCo(i)] = coLevels[i];
		for (int i = 0; i < LevelHelper.levelRank; i++)
			rootVector[DynkinDiagram.translateLevel(i)] = levels[i];
		
		/** Set the disconnected levels. */
		this.disLevels = new int[Globals.disGroup.rank];
		for (int i = 0; i < disLevels.length; i++)
			disLevels[i] = rootVector[DynkinDiagram.translateDis(i)];
		
		/** Calculate the height */
		int tHeight = 0;
		for (int i = 0; i < rootVector.length; i++)
		{
			tHeight += rootVector[i];
		}
		this.height = tHeight;
		
		/**
		 * Split the dynkinlabels into labels of the regular subalgebra and
		 * labels of the disconnected subalgebra.
		 */
		int[] translatedLabels	= new int[Globals.group.rank];
		this.subDynkinLabels	= new int[Globals.subGroup.rank];
		this.disDynkinLabels	= new int[Globals.disGroup.rank];
		for (int i = 0; i < dynkinLabels.length; i++)
			translatedLabels[DynkinDiagram.translateCo(i)] = dynkinLabels[i];
		for (int i = 0; i < disDynkinLabels.length; i++)
			disDynkinLabels[i] = translatedLabels[DynkinDiagram.translateDis(i)];
		for (int i = 0; i < subDynkinLabels.length; i++)
			subDynkinLabels[i] = translatedLabels[DynkinDiagram.translateSub(i)];
	}
	
	public void setOuterMult(long outerMult)
	{
		this.outerMult = outerMult;
	}
	
	public long getOuterMult()
	{
		return outerMult;
	}
	
	public void setOuterSubMult(long outerSubMult)
	{
		this.outerSubMult = outerSubMult;
	}
	
	public long getOuterSubMult()
	{
		return outerSubMult;
	}
	
	public void setRootMult(long rootMult)
	{
		this.rootMult = rootMult;
	}
	
	public long getRootMult()
	{
		return rootMult;
	}
	
	/** Sort representations by means of their associated roots. */
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
