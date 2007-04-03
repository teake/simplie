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
		for (long i = 0; i < num; i++)
		{
			Scan(Globals.numberToVector(i,base,Globals.delGroup.rank,minLevel));
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
		LoopDynkinLabels(levels, dynkinLabels, 0, true);
	}
	
	private void LoopDynkinLabels(int[] levels, int[] dynkinLabels, int beginIndex, boolean scanFirst)
	{
		boolean allGoodIntegers;
		int[]	rootComponents;
		int[]	coDynkinLabels;
		int		numIndices;
		long	mult = 0;
		int[]	rootVector;
		int		height;
		
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
						/** Divide all the root components by the subfactor. */
						for (int i = 0; i < rootComponents.length; i++)
						{
							rootComponents[i] = rootComponents[i] / LevelHelper.subFactor;
						}
						
						/** Calculate the remaining Dynkin labels */
						coDynkinLabels = LevelHelper.CalculateCoDynkinLabels(levels,rootComponents);
						
						/** Calculate the number of indices of the subalgebra representation. */
						numIndices = 0;
						for (int i = 0; i < dynkinLabels.length; i++)
						{
							numIndices += dynkinLabels[i] * (i+1);
						}
						
						/** Construct the whole root vector and see if it's present */
						height		= 0;
						rootVector	= new int[Globals.group.rank];
						for (int i = 0; i < Globals.subGroup.rank; i++)
						{
							rootVector[LevelHelper.TranslateSubIndex(i)] = rootComponents[i];
							height += rootComponents[i];
						}
						for (int i = 0; i < Globals.delGroup.rank; i++)
						{
							rootVector[LevelHelper.TranslateCoIndex(i)] = levels[i];
							height += levels[i];
						}
						if(multiplicities)
						{
							
							CRoot root = Globals.group.getRoot(rootVector);
							if(root != null)
								mult = root.mult;
						}
						
						/** Add the data to the table */
						Object[] rowData = new Object[9];
						rowData[0] = Globals.intArrayToString(levels);
						rowData[1] = Globals.intArrayToString(dynkinLabels);
						rowData[2] = Globals.intArrayToString(coDynkinLabels);
						rowData[3] = Globals.intArrayToString(rootComponents);
						rowData[4] = rootLength / LevelHelper.subFactor;
						rowData[5] = Globals.subGroup.dimOfRep(dynkinLabels);
						rowData[6] = mult;
						rowData[7] = height;
						rowData[8] = numIndices;
						publish(rowData);
					}
				}
				else
				{
					/** The root length is bigger than 2, so abort this line. */
					break;
				}
			}
			if(beginIndex + 1 < dynkinLabels.length)
				LoopDynkinLabels(levels, dynkinLabels.clone(), beginIndex + 1, false);
			dynkinLabels[beginIndex]++;
			scanFirst = true;
		} while( !isCancelled() );
	}
	
}
