/*
 * CLevelScanner.java
 *
 * Created on 20 maart 2007, 13:55
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.helper.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Teake Nutma
 */
public class CLevelScanner
{
    DefaultTableModel tableModel;
    int levelSign = 0;
    
    /** Creates a new instance of CLevelScanner */
    public CLevelScanner(DefaultTableModel tableModel)
    {
	this.tableModel = tableModel;
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
	int[] dynkinLabels = new int[LevelHelper.subRank];
	for (int i = 0; i < LevelHelper.subRank; i++)
	{
	    dynkinLabels[i] = 0;
	}
	LoopDynkinLabels(levels, dynkinLabels, 0, true);
    }
    
    private void LoopDynkinLabels(int[] levels, int[] dynkinLabels, int beginIndex, boolean scanFirst)
    {
	do
	{
	    if(scanFirst)
	    {
		int rootLength = LevelHelper.CalculateRootLength(levels, dynkinLabels);
		/** Only continue if the root length is not bigger than 2. */
		if(LevelHelper.CalculateRootLength(levels, dynkinLabels) <= 2 * LevelHelper.subFactor)
		{
		    /** First check if all root components are integers and non-negative. */
		    int[] rootComponents    = LevelHelper.CalculateRootComponents(levels, dynkinLabels);
		    boolean allGoodIntegers  = true;
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
			int[] coDynkinLabels = LevelHelper.CalculateCoDynkinLabels(levels,rootComponents);
			
			/** Add the data to the table */
			Object[] data = new Object[5];
			data[0] = Helper.IntArrayToString(levels);
			data[1] = Helper.IntArrayToString(dynkinLabels);
			data[2] = Helper.IntArrayToString(coDynkinLabels);
			data[3] = Helper.IntArrayToString(rootComponents);
			data[4] = rootLength / LevelHelper.subFactor;
			tableModel.addRow(data);
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
	} while( true );
    }
    
}
