/*
 * AutoLevelScanner.java
 *
 * Created on 20 maart 2007, 14:16
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
import edu.simplie.math.fraction;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.SwingWorker;


/**
 * A SwingWorker for scanning regular subalgebra representations within a algebra.
 *
 * @author	Teake Nutma
 * @version $Revision$, $Date$
 */
public class AutoLevelScanner extends SwingWorker<Void,Object[]>
{
	private final AlgebraComposite algebras;
	private final boolean calcRootMult;
	private final boolean calcRepMult;
	private final boolean showZeroMultRoot;
	private final boolean showZeroMultRep;
	private final int minLevel;
	private final int maxLevel;
	private final DefaultTableModel tableModel;
	
	private int levelSign;

	/**
	 * Creates a new instance of AutoLevelScanner.
	 *
	 * @param	algebras			The AlgebraComposite object for which to do the level decomposition.
	 * @param	calcRootMult		If true, the multiplicity of the roots will be calculate. This will take longer.
	 * @param	calcRepMult			If true, the multiplicity of the subalgebra representations will be calculated.
	 *								This again will take longer.
	 * @param	showZeroMultRoot	Indicates wethere or not to show reps that correspond to non-existing roots.
	 * @param	showZeroMultRep		In case this and the above two parameters are true,
	 *								representations with zero outer multiplicity will not be shown.
	 * @param	tableModel			The DefaultTableModel in which the results of the scan should be put.
	 * @param	minLevel			The minimum value of the levels.
	 * @param	maxLevel			The maximum value of the levels.
	 */
	public AutoLevelScanner(
			AlgebraComposite algebras,
			boolean calcRootMult,
			boolean calcRepMult,
			boolean showZeroMultRoot,
			boolean showZeroMultRep,
			DefaultTableModel tableModel,
			int minLevel,
			int maxLevel)
	{
		this.algebras		= algebras;
		this.tableModel		= tableModel;
		this.minLevel		= minLevel;
		this.maxLevel		= maxLevel;
		this.calcRootMult	= calcRootMult;
		this.calcRepMult	= calcRepMult;
		this.showZeroMultRoot	= showZeroMultRoot;
		this.showZeroMultRep	= showZeroMultRep;
		
		this.levelSign = 0;
	}
	
	@Override
	public Void doInBackground()
	{
		ArrayList<Level> levels;
		
		int levelRank;
		int base;
		int num;
		
		// Some preliminary checks.
		if(minLevel > maxLevel)
			return null;
		if(algebras.algebra.rank == algebras.coAlgebra.rank)
			return null;
		
		// How many possibilities of level combinations are there?
		levelRank	= algebras.algebra.rank - algebras.coAlgebra.rank;
		base		= maxLevel + 1 - minLevel;
		num			= (int) Math.pow(base, levelRank);
		try
		{
			levels = new ArrayList<Level>();
			
			// Perform the scan.
			for (int i = 0; i < num; i++)
			{
				Level level = new Level(Helper.numberToVector(i,base,levelRank,minLevel), algebras);
				System.out.println("Scanning levels " + Helper.arrayToString(level.levelVector));
				Scan(level);
				levels.add(level);
				System.out.println("... max height: " + level.heighest() + ", number of reps: " + level.size());
			}
			
			// Sort the levels
			Collections.sort(levels);
			
			// Set outer mults etc.
			for (Level level : levels)
			{
				processRepresentations(level);
			}
			
		}
		catch (Exception e)
		{
			// Because we are in a seperate thread, all exceptions have to be manually printed to sout.
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void process(List<Object[]> chunks)
	{
		for(Object[] rowData : chunks)
		{
			tableModel.addRow(rowData);
		}
	}
	
	
	/********************************
	 * Methods for scanning reps
	 ********************************/
	
	
	/** Scans all the possible highest weight representations at a given level */
	private void Scan(Level level)
	{
		// Are the levels all positive or all negative?
		levelSign = 0;
		boolean positive = false;
		boolean negative = false;
		for (int i = 0; i < level.levelVector.length; i++)
		{
			if(level.levelVector[i] < 0)
				negative = true;
			if(level.levelVector[i] > 0)
				positive = true;
		}
		if(positive && negative)
			// This cannot be, thanks to the triangular decomposition.
			return;
		if(positive)
			levelSign = 1;
		if(negative)
			levelSign = -1;
		
		// Set up the Dynkin labels
		int[] dynkinLabels = new int[algebras.coAlgebra.rank];
		for (int i = 0; i < dynkinLabels.length; i++)
		{
			dynkinLabels[i] = 0;
		}
		
		LoopDynkinLabels(level, dynkinLabels, 0, true);
	}
	
	/** Loops through every possible dynkin label. */
	private void LoopDynkinLabels(Level level, int[] dynkinLabels, int beginIndex, boolean scanFirst)
	{
		boolean		allGoodIntegers;
		fraction[]	coLevels;
		
		if (isCancelled())
			return;
		
		do
		{
			if(scanFirst)
			{
				fraction rootLength = algebras.calculateRootLength(level.levelVector, dynkinLabels);
				// Only continue if the root length is not bigger than the maximum root length.
				if(rootLength.asDouble() <= algebras.algebra.rs.maxNorm())
				{
					if(rootLength.isInt())
					{
						// First check if all root components are integers and non-negative.
						allGoodIntegers = true;
						coLevels = algebras.calculateCoLevels(level.levelVector, dynkinLabels);
						for (int i = 0; i < coLevels.length; i++)
						{
							if(!coLevels[i].isInt() || coLevels[i].asDouble() * levelSign < 0)
							{
								allGoodIntegers = false;
								break;
							}
						}
						// If we found a valid representation, add it.
						if(allGoodIntegers)
						{
							// Add the representation.
							level.addRep(dynkinLabels, rootLength.asInt());
						}
					}
				}
				else
				{
					// The root length is bigger than 2, so abort this line.
					return;
				}
			}
			if(beginIndex + 1 < dynkinLabels.length)
				LoopDynkinLabels(level, dynkinLabels.clone(), beginIndex + 1, false);
			dynkinLabels[beginIndex]++;
			scanFirst = true;
		} while( !isCancelled() );
	}
	
	
	/** Processes all the representations contained in "reps" */
	private void processRepresentations(Level level)
	{
		long outerMult;
		Representation repI;
		Representation repJ;
		
		if(calcRootMult)
		{
			for (int i = 0; i < level.size(); i++)
			{
				repI = level.get(i);
				
				// Get and set the root multiplicities.
				long mult = algebras.algebra.rs.getRootMult(repI.rootVector.clone());
				if(mult != 0)
					repI.setRootMult(mult);
				else
					continue;
				
				//
				// Calculate the outer multiplicities
				//
				
				if(!calcRepMult)
					continue;
				
				// First the outer multiplicities of the regular subalgebra representation.
				outerMult = repI.getRootMult();
				for (int j = 0; j < i; j++)
				{
					repJ = level.get(j);
					
					if(repJ.length <= repI.length)
						continue;
					
					outerMult -= repJ.getOuterMult() * repJ.getWeightMult(repI.dynkinLabels);
				}
				repI.setOuterMult(outerMult);
			}
		}
		
		// Publish the representations to the output table.
		for(int i = level.size() - 1; i > -1; i--)
		{
			Representation rep = level.get(i);
			// Don't add this representation if its root has zero multiplicity and we don't show those
			if(calcRootMult && !showZeroMultRoot && rep.getRootMult() == 0)
				continue;
			
			// Don't add this representation if it has zero outer multplicity and we don't display zero mults.
			if(calcRootMult && calcRepMult && !showZeroMultRep && rep.getOuterMult() == 0)
				continue;
			
			// Add the data to the table.
			Object[] rowData = new Object[12];
			rowData[0] = Helper.arrayToString(rep.levels);
			rowData[1] = Helper.arrayToString(rep.subDynkinLabels);
			rowData[2] = Helper.arrayToString(rep.intDynkinLabels);
			rowData[3] = Helper.arrayToString(rep.rootVector);
			rowData[4] = rep.length;
			rowData[5] = (long) algebras.subAlgebra.dimOfRep(rep.subDynkinLabels);
			rowData[6] = (long) algebras.intAlgebra.dimOfRep(rep.intDynkinLabels);
			rowData[7] = rep.getRootMult();
			rowData[8] = rep.getOuterMult();
			rowData[9] = rep.height;
			publish(rowData);
		}
	}
}
