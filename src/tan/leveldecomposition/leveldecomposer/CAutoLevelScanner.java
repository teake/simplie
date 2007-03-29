/*
 * CAutoLevelScanner.java
 *
 * Created on 20 maart 2007, 14:16
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.dynkindiagram.*;
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
	this.tableModel	    = tableModel;
	this.minLevel	    = minLevel;
	this.maxLevel	    = maxLevel;
	this.multiplicities = multiplicities;
	
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
	
	int[] levels = new int[LevelHelper.coRank];
	for (int i = 0; i < LevelHelper.coRank; i++)
	{
	    levels[i] = minLevel;
	}
	LoopLevels(levels.clone(),0,maxLevel, true);
	
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
    
    /** Iterates through all possible levels for which levels[i] <= maxLevel and scans them. */
    private void LoopLevels(int[] levels, int beginIndex, int maxLevel, boolean scanFirst)
    {
	if(isCancelled())
	    return;
	
	do
	{
	    /**
	     * Keep the triangular decomposition in mind:
	     * if levels[i] < 0 then levels[j] > 0 can not be for any i and j.
	     */
	    for (int i = 0; i < levels.length - 1; i++)
	    {
		if(i < beginIndex && levels[i] < 0 && levels[beginIndex] > 0)
		    /** We can safely abort this line. */
		    return;
		
		if(levels[i] > 0 && levels[i+1] < 0)
		{
		    /** Increase the value of the remaining levels to at least 0. */
		    for(int j = i+1; j < levels.length; j++ )
		    {
			levels[j] = 0;
		    }
		    /** We changed the levels, so scan it. */
		    scanFirst = true;
		    break;
		}
	    }
	    
	    /** Only scan the level if we haven't scanned it already. */
	    if(scanFirst)
	    {
		Scan(levels);
	    }
	    
	    /** Loop through the remaining indices */
	    if(beginIndex + 1 < levels.length)
		LoopLevels(levels.clone(), beginIndex + 1, maxLevel, false);
	    
	    /** Increase the current level and scan it the next loop. */
	    levels[beginIndex]++;
	    scanFirst = true;
	    
	} while(levels[beginIndex] <= maxLevel && !isCancelled());
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
	boolean allGoodIntegers;
	int[]	rootComponents;
	int[]	coDynkinLabels;
	int	numIndices;
	int	mult = 0;
	int[]	rootVector;
	
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
			
			if(multiplicities)
			{
			    /** Construct the whole root vector and see if it's present */
			    rootVector =  new int[LevelHelper.rank];
			    for (int i = 0; i < LevelHelper.subRank; i++)
			    {
				rootVector[LevelHelper.TranslateSubIndex(i)] = rootComponents[i];
			    }
			    for (int i = 0; i < LevelHelper.coRank; i++)
			    {
				rootVector[LevelHelper.TranslateCoIndex(i)] = levels[i];
			    }
			    if( Globals.group.getRoot(rootVector) != null )
				mult = 1;
			}
			
			/** Add the data to the table */
			Object[] rowData = new Object[7];
			rowData[0] = Globals.intArrayToString(levels);
			rowData[1] = Globals.intArrayToString(dynkinLabels);
			rowData[2] = Globals.intArrayToString(coDynkinLabels);
			rowData[3] = Globals.intArrayToString(rootComponents);
			rowData[4] = rootLength / LevelHelper.subFactor;
			rowData[5] = mult;
			rowData[6] = numIndices;
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
