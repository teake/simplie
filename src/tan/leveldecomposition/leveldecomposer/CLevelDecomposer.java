/*
 * CLevelDecomposer.java
 *
 * Created on 14 maart 2007, 11:12
 *
 */

package tan.leveldecomposition.leveldecomposer;

import Jama.Matrix;
import java.util.Vector;

/**
 *
 * @author Teake Nutma
 */
public class CLevelDecomposer
{
    int rank;
    int subRank;
    int coRank;
    /** The factor with which the inverse of the cartan sub matrix gets multiplied. */
    int subFactor; 
    
    /** Array of which nodes are enabled */
    boolean[] enabledNodes;
    
    /** The full cartan matrix */
    int[][] cartanMatrix;
    /** The inverse of the subalgebra matrix */
    int[][] S;
    
    /** Creates a new instance of CLevelDecomposer */
    public CLevelDecomposer()
    {
    }
    
    /** Sets it all up */
    public void Setup(int rank, int subRank, Matrix cartanMatrix, Matrix cartanMatrixSubInverse, boolean[] enabledNodes)
    {
	this.rank	= rank;
	this.subRank	= subRank;
	this.coRank	= rank - subRank;
	this.subFactor	= subRank + 1;	// Everythings gets multiplied with this factor in order to get integer values.
	// Make sure we divide with this factor at the end of the day!
	
	this.enabledNodes = enabledNodes;
	
	this.cartanMatrix   = new int[rank][rank];
	this.S		    = new int[subRank][subRank];
	
	// Copy both matrices into integer arrays.
	// NOTE: All values of (cartanMatrixSubInverse * subFactor) of course have to be integers.
	for(int i=0; i<rank; i++)
	{
	    for(int j=0; j<rank; j++)
	    {
		this.cartanMatrix[i][j] = (int) Math.round(cartanMatrix.get(i,j));
	    }
	}
	cartanMatrixSubInverse.times(subFactor);
	for(int i=0; i<subRank; i++)
	{
	    for(int j=0; j<subRank; j++)
	    {
		this.S[i][j] = (int) Math.round(cartanMatrixSubInverse.get(i,j));
	    }
	}
	
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
    
    /** Returns the actual root labels time the subfactor */
    private int[] CalculateRootLabels(int[] dynkinLabels, int[] levels)
    {
	int[] rootLabels	= new int[subRank];
	int[] levelComponents	= CalculateLevelComponents(levels);
	
	for(int i=0; i < subRank; i++)
	{
	    rootLabels[i] = 0;
	    for(int j=0; j < subRank; j++)
	    {
		rootLabels[i] += S[i][j] * ( dynkinLabels[j] - levelComponents[j] );
	    }
	}
	
	return rootLabels;
    }
    
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
    public Vector<CRepresentation> ScanLevel(int[] levels)
    {
	Vector<CRepresentation> reps = new Vector<CRepresentation>();
	// DO MORE STUFF
	return reps;
    }
}
