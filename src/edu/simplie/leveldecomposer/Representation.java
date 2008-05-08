/*
 * Representation.java
 *
 * Created on 10 april 2007, 10:49
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

package edu.simplie.leveldecomposer;

import edu.simplie.*;
import edu.simplie.algebra.HighestWeightRep;
import edu.simplie.math.fraction;

/**
 * A class for storing valid regular subalgebra representations.
 * This class is used in CAutoLevelScanner before sending the data to the table.
 *
 * @see		CAutoLevelScanner
 * @author	Teake Nutma
 * @version $Revision$, $Date$
 */
public class Representation implements Comparable<Representation>
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
	public final int[] intDynkinLabels;
	
	/** The length of the associated root (i.e. the innerproduct with itself). */
	public final int length;
	/** The height of the root of the heighest weight. */
	public final int height;
	/** The height of the highest weight state */
	public final fraction weightHeight;
	
	private long outerMult;
	private long rootMult;
	
	private final HighestWeightRep hwRep;
	
	private final AlgebraComposite algebras;
	
	/**
	 * Creates a new instance of Representation
	 *
	 * @param	algebras		The AlgebraComposite object for which this is a rep at a level.
	 * @param	dynkinLabels	The full dynkinlabels of the highest weight state.
	 * @param	levels			The part of the rootvector corresponding to the level nodes.
	 * @param	length			The length of the associated root (i.e. the innerproduct with itself).
	 */
	public Representation(AlgebraComposite algebras, int[] dynkinLabels, int[] levels, int length)
	{
		this.algebras		= algebras;
		this.dynkinLabels	= dynkinLabels.clone();
		this.levels			= levels.clone();
		this.length			= length;
		this.rootMult		= 0;
		this.outerMult		= 0;
		
		// Construct the whole root vector.
		this.rootVector	= algebras.rootVector(levels, dynkinLabels);
		
		// Get the Dynkin labels of the internal- and sub-algebra.
		subDynkinLabels = algebras.subDynkinLabels(rootVector);
		intDynkinLabels = algebras.intDynkinLabels(rootVector);
		
		// Instantiate the highest weight rep.
		hwRep = new HighestWeightRep(algebras.coAlgebra, dynkinLabels);
		weightHeight = hwRep.highestHeight;
		
		// Calculate the height.
		int tHeight = 0;
		for (int i = 0; i < rootVector.length; i++)
		{
			tHeight += rootVector[i];
		}
		this.height = algebras.isSignPos() ? tHeight : tHeight + weightHeight.times(2).asInt();
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
		return hwRep.getWeightMult(weightLabels);
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
	
	/** Sets the multiplicity of the associated root in the full algebra. */
	public void setRootMult(long rootMult)
	{
		this.rootMult = rootMult;
	}
	
	/** Gets the multiplicity of the associated root in the full algebra. */
	public long getRootMult()
	{
		return rootMult;
	}
	
	/** Sort representations by means of the height of their associated roots. */
	public int compareTo(Representation rep)
	{
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		
		if(height < rep.height)
			return AFTER;
		if(height > rep.height)
			return BEFORE;
		
		return EQUAL;
	}
}
