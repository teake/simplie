/*
 * CLevel.java
 * 
 * Created on 8 april 2008, 12:01
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

package edu.rug.hep.simplie.leveldecomposer;

import edu.rug.hep.simplie.CAlgebraComposite;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A class for storing subalgebra representations at a given level.
 * 
 * @author Teake Nutma
 */
public class CLevel implements Comparable<CLevel>
{
	/** The actual levels */
	public final int[] levelVector;
	/** The height of the heightest root at this level */
	private int heighest;
	
	private final CAlgebraComposite algebras;
	private ArrayList<CRepresentation> repContainer;
	
	/**
	 * Constructs a level container.
	 * 
	 * @param vector	 The actual level.
	 * @param algebras	 The algebra composite for which we're doing the level decomposition.
	 */
	public CLevel(int[] vector, CAlgebraComposite algebras)
	{
		this.levelVector= vector.clone();
		this.algebras	= algebras;
		this.heighest	= 0;
		repContainer	= new ArrayList<CRepresentation>();
	}
	
	/**
	 * Adds a (possibly valid) subalgebra representation to this level.
	 * 
	 * @param dynkinLabels	 The Dynkin labels of the representation.
	 * @param rootLength	 The root length of the associated root.
	 */ 
	public void addRep(int[] dynkinLabels, int rootLength)
	{
		CRepresentation rep = new CRepresentation(
									algebras,
									dynkinLabels,
									levelVector,
									rootLength																		
									);
		repContainer.add(rep);
		Collections.sort(repContainer);
		heighest = Math.max(heighest, rep.height);
	}
	
	/**
	 * @return The number of subalgebra representations at this level.
	 */
	public int size()
	{
		return repContainer.size();
	}

	/**
	 * @return The height of the highest root at this level.
	 */
	public int heighest()
	{
		return heighest;
	}
	
	/**
	 * Gets a subalgebra representation at this level.
	 * 
	 * @param index		 The index of the subalgebra representation.
	 * @return			 A subalgebra representation.
	 */
	public CRepresentation get(int index)
	{
		return repContainer.get(index);
	}
	
	/** 
	 * Compares levels to each other based on first a partial ordering 
	 * and next the height of their highest root. 
	 */
	public int compareTo(CLevel level)
	{
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		
		// First try a partial ordering.
		if(level.levelVector.length == levelVector.length)
		{
			boolean bigger	= false;
			boolean smaller = false;
			for (int i = 0; i < levelVector.length; i++)
			{
				int diff = levelVector[i] - level.levelVector[i];
				if(diff > 0) bigger	 = true;
				if(diff < 0) smaller = true;
			}
			if(bigger && !smaller)
				return AFTER;
			if(smaller && !bigger)
				return BEFORE;
		}
		
		// If the partial ordering didn't work sort by height.
		if(heighest > level.heighest)
			return AFTER;
		if(heighest < level.heighest)
			return BEFORE;
		
		return EQUAL;
	}

}
