/*
 * CAutoLevelScanner.java
 *
 * Created on 20 maart 2007, 14:16
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.dynkindiagram.*;
import javax.swing.SwingWorker;

/**
 *
 * @author Teake Nutma
 */
public class CAutoLevelScanner extends SwingWorker<Void,Void>
{
    CLevelScanner levelScanner;
    int minLevel;
    int maxLevel;
    
    /** Creates a new instance of CAutoLevelScanner */
    public CAutoLevelScanner(CLevelScanner levelScanner, int minLevel, int maxLevel)
    {
	this.levelScanner = levelScanner;
	this.minLevel = minLevel;
	this.maxLevel = maxLevel;
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
    
    /** Iterates through all possible levels for which levels[i] <= maxLevel and scans them. */
    private void LoopLevels(int[] levels, int beginIndex, int maxLevel, boolean scanFirst)
    {
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
		levelScanner.Scan(levels);
	    }
	    
	    /** Loop through the remaining indices */
	    if(beginIndex + 1 < levels.length)
		LoopLevels(levels.clone(), beginIndex + 1, maxLevel, false);
	    
	    /** Increase the current level and scan it the next loop. */
	    levels[beginIndex]++;
	    scanFirst = true;
	    
	} while(levels[beginIndex] <= maxLevel);
    }
    
}
