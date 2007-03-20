/*
 * CLevelDecomposer.java
 *
 * Created on 14 maart 2007, 11:12
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.helper.*;
import Jama.Matrix;
import java.util.Vector;
import javax.swing.table.*;

/**
 *
 * @author Teake Nutma
 */
public class CLevelDecomposer
{
    int rank;
    int subRank;
    int coRank;
    /**
     * The inverse of the Cartan matrix gets multiplied with this value in order to make all entries integers.
     * Thus also the rootComponents and the rootLength get multiplied with it.
     * Don't forget to divide these with this factor at the end of the day!
     */
    int subFactor;
    
    /** Array of which nodes are enabled */
    boolean[] enabledNodes;
    
    int signConvention = 1;
    
    /** The full cartan matrix */
    int[][] cartanMatrix;
    /** The inverse of the Cartan matrix multiplied with the subFactor */
    int[][] S;
    
    /** Creates a new instance of CLevelDecomposer */
    public CLevelDecomposer()
    {
    }
    
    /** Sets it all up */
    public void Initialize(int rank, int subRank, Matrix cartanMatrix, Matrix cartanMatrixSubInverse, boolean[] enabledNodes)
    {
	this.rank	= rank;
	this.subRank	= subRank;
	this.coRank	= rank - subRank;
	this.subFactor	= subRank + 1;
	
	this.enabledNodes = enabledNodes;
	
	this.cartanMatrix   = new int[rank][rank];
	this.S		    = new int[subRank][subRank];
	
	/**
	 * Copy both matrices into integer arrays.
	 * NOTE: All values of (cartanMatrixSubInverse * subFactor) of course have to be integers.
	 */
	for(int i=0; i<rank; i++)
	{
	    for(int j=0; j<rank; j++)
	    {
		this.cartanMatrix[i][j] = (int) Math.round(cartanMatrix.get(i,j));
	    }
	}
	for(int i=0; i<subRank; i++)
	{
	    for(int j=0; j<subRank; j++)
	    {
		this.S[i][j] = (int) Math.round( subFactor * cartanMatrixSubInverse.get(i,j) );
	    }
	}
    }
    
    public void SetSignConvention(int sign)
    {
	if(!(sign == 1 || sign == -1))
	    return;
	signConvention = sign;
    }
    
    /** Translates an index of the submatrix into an index of the full matrix */
    private int TranslateSubIndex(int index)
    {
	int subIndex = 0;
	for(int i=0; i<rank; i++)
	{
	    if(enabledNodes[i])
		subIndex++;
	    if(subIndex == index + 1)
		return i;
	}
	return -1; // not found
    }
    
    /** Translates a co-index into an index of the full matrix */
    private int TranslateCoIndex(int index)
    {
	int subIndex = 0;
	for(int i=0; i<rank; i++)
	{
	    if(!enabledNodes[i])
		subIndex++;
	    if(subIndex == index + 1)
		return i;
	}
	return -1; // not found
    }
    
    /** Returns the actual root length times the subfactor */
    private int CalculateRootLength(int[] dynkinLabels, int[] levels)
    {
	int[] levelComponents = CalculateLevelComponents(levels);
	int rootLength = 0;
	
	for(int i=0; i < subRank; i++)
	{
	    for(int j=0; j < subRank; j++)
	    {
		rootLength += S[i][j] * ( (dynkinLabels[i] * dynkinLabels[j]) - (levelComponents[i] * levelComponents[j]) );
	    }
	}
	for(int i=0; i < coRank; i++)
	{
	    for(int j=0; j < coRank; j++)
	    {
		rootLength += subFactor * cartanMatrix[TranslateCoIndex(i)][TranslateCoIndex(j)] * levels[i] * levels[j];
	    }
	}
	
	return rootLength;
    }
    
    /** Returns the root components times the subfactor. */
    private int[] CalculateRootComponents(int[] dynkinLabels, int[] levels)
    {
	int[] rootLabels	= new int[subRank];
	int[] levelComponents	= CalculateLevelComponents(levels);
	
	for(int i=0; i < subRank; i++)
	{
	    rootLabels[i] = 0;
	    for(int j=0; j < subRank; j++)
	    {
		rootLabels[i] += S[i][j] * ( signConvention * dynkinLabels[j] - levelComponents[j] );
	    }
	}
	
	return rootLabels;
    }
    
    /** Calculates the contraction of the levels with the Cartan matrix. */
    private int[] CalculateLevelComponents(int[] levels)
    {
	int[] levelComponents = new int[subRank];
	
	for(int i=0; i < subRank; i++)
	{
	    levelComponents[i] = 0;
	    for(int j=0; j < coRank; j++)
	    {
		levelComponents[i] += cartanMatrix[TranslateSubIndex(i)][TranslateCoIndex(j)] * levels[j];
	    }
	}
	return levelComponents;
    }
    
    /** Scans all the possible highest weight representations at a given level */
    public void ScanLevel(int[] levels, DefaultTableModel tableModel)
    {
	/** Are the levels all positive or all negative? */
	int levelSign = 0;
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
	int[] dynkinLabels = new int[subRank];
	for (int i = 0; i < subRank; i++)
	{
	    dynkinLabels[i] = 0;
	}
	
	/** Do the scan. */
	LoopDynkinLabels(dynkinLabels, levels, 0, true, levelSign, tableModel);
    }
    
    private void LoopDynkinLabels(int[] dynkinLabels, int[] levels, int beginIndex, boolean scanFirst, int levelSign, DefaultTableModel tableModel)
    {
	do
	{
	    if(scanFirst)
	    {
		int rootLength = CalculateRootLength(dynkinLabels,levels);
		/** Only continue if the root length is not bigger than 2. */
		if(CalculateRootLength(dynkinLabels,levels) <= 2 * subFactor)
		{
		    /** First check if all root components are integers and non-negative. */
		    int[] rootComponents    = CalculateRootComponents(dynkinLabels, levels);
		    boolean allGoodIntegers  = true;
		    for (int i = 0; i < rootComponents.length; i++)
		    {
			if(rootComponents[i] % subFactor != 0 || rootComponents[i] * levelSign < 0)
			{
			    allGoodIntegers = false;
			    break;
			}
		    }
		    /** If we found a valid representation, add it. */
		    if(allGoodIntegers)
		    {
			/** First divide all the root components by the subfactor. */
			for (int i = 0; i < rootComponents.length; i++)
			{
			    rootComponents[i] = rootComponents[i] / subFactor;
			}
			/** Add the data to the table */
			Object[] data = new Object[4];
			data[0] = Helper.IntArrayToString(levels);
			data[1] = Helper.IntArrayToString(dynkinLabels);
			data[2] = Helper.IntArrayToString(rootComponents);
			data[3] = rootLength / subFactor;
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
		LoopDynkinLabels(dynkinLabels.clone(), levels, beginIndex + 1, false, levelSign, tableModel);
	    dynkinLabels[beginIndex]++;
	    scanFirst = true;
	} while( true );
    }
    
}
