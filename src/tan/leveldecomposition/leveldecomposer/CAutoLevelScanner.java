/*
 * CAutoLevelScanner.java
 *
 * Created on 20 maart 2007, 14:16
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.dynkindiagram.*;
import tan.leveldecomposition.helper.*;

import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.util.List;


/**
 *
 * @author Teake Nutma
 */
public class CAutoLevelScanner extends SwingWorker<Void,Object[]>
{
    int minLevel;
    int maxLevel;
    int levelSign;
    DefaultTableModel tableModel;
    
    /** Creates a new instance of CAutoLevelScanner */
    public CAutoLevelScanner(DefaultTableModel tableModel, int minLevel, int maxLevel)
    {
	this.tableModel = tableModel;
	this.minLevel = minLevel;
	this.maxLevel = maxLevel;
	
	this.levelSign = 0;
    }
    
    @Override
    public Void doInBackground()
    {
	if(minLevel > maxLevel)
	    return null;
	if(DynkinDiagram.GetRank() == DynkinDiagram.GetSubRank())
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
			
			/** Calculate the remaining Dynkin labels */
			int[] coDynkinLabels = LevelHelper.CalculateCoDynkinLabels(levels,rootComponents);
			
			/** Calculate the number of indices of the subalgebra representation. */
			int numIndices = 0;
			for (int i = 0; i < dynkinLabels.length; i++)
			{
			    numIndices += dynkinLabels[i] * (i+1);
			}
			
			/** Add the data to the table */
			Object[] rowData = new Object[6];
			rowData[0] = Helper.IntArrayToString(levels);
			rowData[1] = Helper.IntArrayToString(dynkinLabels);
			rowData[2] = Helper.IntArrayToString(coDynkinLabels);
			rowData[3] = Helper.IntArrayToString(rootComponents);
			rowData[4] = rootLength / LevelHelper.subFactor;
			rowData[5] = numIndices;
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
