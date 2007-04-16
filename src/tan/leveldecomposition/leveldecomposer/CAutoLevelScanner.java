/*
 * CAutoLevelScanner.java
 *
 * Created on 20 maart 2007, 14:16
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.dynkindiagram.*;
import tan.leveldecomposition.group.*;
import tan.leveldecomposition.*;

import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


/**
 *
 * @author Teake Nutma
 */
public class CAutoLevelScanner extends SwingWorker<Void,Object[]>
{
	boolean multiplicities;
	int minLevel;
	int maxLevel;
	int levelSign;
	DefaultTableModel tableModel;
	
	ArrayList<CRepresentation> reps;
	
	/** Creates a new instance of CAutoLevelScanner */
	public CAutoLevelScanner(boolean multiplicities, DefaultTableModel tableModel, int minLevel, int maxLevel)
	{
		this.tableModel		= tableModel;
		this.minLevel		= minLevel;
		this.maxLevel		= maxLevel;
		this.multiplicities	= multiplicities;
		
		this.levelSign = 0;
	}
	
	@Override
	public Void doInBackground()
	{
		if(minLevel > maxLevel)
			return null;
		if(Globals.group.rank == Globals.subGroup.rank)
			return null;
		
		LevelHelper.Setup();
		
		int base	= maxLevel + 1 - minLevel;
		long num	= (long) Math.pow(base, Globals.delGroup.rank);
		try
		{
			for (long i = 0; i < num; i++)
			{
				Scan(Globals.numberToVector(i,base,Globals.delGroup.rank,minLevel));
			}
			
		}
		catch (Exception e)
		{
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
	
	/** Scans all the possible highest weight representations at a given level */
	public void Scan(int[] levels)
	{
		/** Are the levels all positive or all negative? */
		levelSign = 0;
		boolean positive = false;
		boolean negative = false;
		for (int i = 0; i < levels.length; i++)
		{
			if(levels[i] < 0)
				negative = true;
			if(levels[i] > 0)
				positive = true;
		}
		if(positive && negative)
			/** This cannot be, thanks to the triangular decomposition. */
			return;
		if(positive)
			levelSign = 1;
		if(negative)
			levelSign = -1;
		
		/** Set up the Dynkin labels */
		int[] dynkinLabels = new int[Globals.subGroup.rank];
		for (int i = 0; i < Globals.subGroup.rank; i++)
		{
			dynkinLabels[i] = 0;
		}
		
		reps = new ArrayList<CRepresentation>();
		LoopDynkinLabels(levels, dynkinLabels, 0, true);
		processRepresentations();
	}
	
	private void LoopDynkinLabels(int[] levels, int[] dynkinLabels, int beginIndex, boolean scanFirst)
	{
		boolean allGoodIntegers;
		int[]	rootComponents;
		
		if (isCancelled())
			return;
		
		do
		{
			if(scanFirst)
			{
				int rootLength = LevelHelper.CalculateRootLength(levels, dynkinLabels);
				/** Only continue if the root length is not bigger than 2. */
				if(LevelHelper.CalculateRootLength(levels, dynkinLabels) <= 2 * LevelHelper.subFactor)
				{
					/** First check if all root components are integers and non-negative. */
					rootComponents  = LevelHelper.CalculateRootComponents(levels, dynkinLabels);
					allGoodIntegers = true;
					for (int i = 0; i < rootComponents.length; i++)
					{
						if(rootComponents[i] % LevelHelper.subFactor != 0 || rootComponents[i] * levelSign < 0)
						{
							allGoodIntegers = false;
							break;
						}
					}
					/** If we found a valid representation, add it. */
					if(allGoodIntegers)
					{
						addRepresentation(levels, dynkinLabels.clone(), rootComponents, rootLength);
					}
				}
				else
				{
					/** The root length is bigger than 2, so abort this line. */
					return;
				}
			}
			if(beginIndex + 1 < dynkinLabels.length)
				LoopDynkinLabels(levels, dynkinLabels.clone(), beginIndex + 1, false);
			dynkinLabels[beginIndex]++;
			scanFirst = true;
		} while( !isCancelled() );
	}
	
	private void addRepresentation(int[] levels, int[] dynkinLabels, int[] rootComponents, int rootLength)
	{
		int[]			rootVector;
		CRepresentation rep;
		
		/** Divide all the root components by the subfactor. */
		for (int i = 0; i < rootComponents.length; i++)
			rootComponents[i] = rootComponents[i] / LevelHelper.subFactor;
		
		/** Construct the whole root vector and see if it's present */
		rootVector	= new int[Globals.group.rank];
		for (int i = 0; i < Globals.subGroup.rank; i++)
			rootVector[DynkinDiagram.translateSubIndex(i)] = rootComponents[i];
		for (int i = 0; i < Globals.delGroup.rank; i++)
			rootVector[DynkinDiagram.translateCoIndex(i)] = levels[i];
		
		/** Add the representation. */
		rep = new CRepresentation(
				rootVector,
				dynkinLabels,
				levels,
				rootComponents,
				rootLength / LevelHelper.subFactor
				);
		reps.add(rep);
	}
	
	private void processRepresentations()
	{
		int[]	coDynkinLabels;
		int		numIndices;
		long	outerMult;
		CRepresentation repI;
		CRepresentation repJ;
		
		Collections.sort(reps);
		
		if(multiplicities)
		{
			//TODO: also account for the other sign of LevelHelper
			
			for (int i = 0; i < reps.size(); i++)
			{
				repI = reps.get(i);
				
				/** Get and set the root multiplicities */
				CRoot root = Globals.group.getRoot(repI.rootVector);
				if(root != null)
					repI.setRootMult(root.mult);
				else
					continue;
				
				/** Calculate the outer multiplicities */
				outerMult = repI.getRootMult();
				for (int j = 0; j < i; j++)
				{
					repJ = reps.get(j);
					if(repJ.length <= repI.length)
						continue;
					outerMult -= repJ.getOuterMult() * Globals.subGroup.weightMultiplicity(
							Globals.flipIntArray(repJ.dynkinLabels),
							Globals.flipIntArray(repI.dynkinLabels)
							);
				}
				repI.setOuterMult(outerMult);
			}
		}
		
		/** Publish the representations to the output table. */
		for(CRepresentation rep : reps)
		{
			numIndices	= 0;
			
			/** Calculate the remaining Dynkin labels */
			coDynkinLabels = LevelHelper.CalculateCoDynkinLabels(rep.levels,rep.rootComponents);
			
			/** Calculate the number of indices of the subalgebra representation. */
			for (int i = 0; i < rep.dynkinLabels.length; i++)
			{
				numIndices += rep.dynkinLabels[i] * (i+1);
			}
			
			/** Add the data to the table */
			Object[] rowData = new Object[10];
			rowData[0] = Globals.intArrayToString(rep.levels);
			rowData[1] = Globals.intArrayToString(rep.dynkinLabels);
			rowData[2] = Globals.intArrayToString(coDynkinLabels);
			rowData[3] = Globals.intArrayToString(rep.rootComponents);
			rowData[4] = rep.length;
			rowData[5] = (long) Globals.subGroup.dimOfRep(rep.dynkinLabels);
			rowData[6] = rep.getRootMult();
			rowData[7] = rep.getOuterMult();
			rowData[8] = rep.height;
			rowData[9] = numIndices;
			publish(rowData);
		}
	}
	
}
