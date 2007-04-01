/*
 * LevelHelper.java
 *
 * Created on 14 maart 2007, 11:12
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.dynkindiagram.DynkinDiagram;
import tan.leveldecomposition.*;

import Jama.Matrix;
import java.util.Vector;
import javax.swing.table.*;

/**
 *
 * @author Teake Nutma
 */
public class LevelHelper
{
	private static LevelHelper _instance = new LevelHelper();
	
	static int rank;
	static int subRank;
	static int coRank;
	/**
	 * The inverse of the Cartan matrix gets multiplied with this value in order to make all entries integers.
	 * Thus also the rootComponents and the rootLength get multiplied with it.
	 * Don't forget to divide these with this factor at the end of the day!
	 */
	static int subFactor;
	
	/** Array of which nodes are enabled */
	static boolean[] enabledNodes;
	
	static int signConvention	= 1;
	
	/** The full cartan matrix */
	static int[][] cartanMatrix;
	/** The inverse of the Cartan matrix multiplied with the subFactor */
	static int[][] S;
	
	
	/**
	 * Creates a new instance of LevelHelper
	 */
	private LevelHelper()
	{
	}
	
	public static LevelHelper getInstance()
	{
		return _instance;
	}
	
	/** Sets it all up */
	public static void Setup()
	{
		Matrix cM		= DynkinDiagram.GetCartanMatrix();
		Matrix subInv	= DynkinDiagram.GetCartanSubMatrix().inverse();
		
		LevelHelper.rank		= Globals.group.rank;
		LevelHelper.subRank		= Globals.subGroup.rank;
		LevelHelper.coRank		= LevelHelper.rank - LevelHelper.subRank;
		LevelHelper.subFactor	= LevelHelper.subRank + 1;
		
		LevelHelper.enabledNodes = DynkinDiagram.GetEnabledNodes();
		
		LevelHelper.cartanMatrix	= new int[rank][rank];
		LevelHelper.S				= new int[subRank][subRank];
		
		/**
		 * Copy both matrices into integer arrays.
		 * NOTE: All values of (cartanMatrixSubInverse * subFactor) of course have to be integers.
		 */
		for(int i=0; i<rank; i++)
		{
			for(int j=0; j<rank; j++)
			{
				LevelHelper.cartanMatrix[i][j] = (int) Math.round(cM.get(i,j));
			}
		}
		
		for(int i=0; i<subRank; i++)
		{
			for(int j=0; j<subRank; j++)
			{
				LevelHelper.S[i][j] = (int) Math.round( subFactor * subInv.get(i,j) );
			}
		}
	}
	
	
	public static void SetSignConvention(int sign)
	{
		if(!(sign == 1 || sign == -1))
			return;
		signConvention = sign;
	}
	
	/** Translates an index of the submatrix into an index of the full matrix */
	public static int TranslateSubIndex(int index)
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
	public static int TranslateCoIndex(int index)
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
	public static int CalculateRootLength(int[] levels, int[] dynkinLabels)
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
	public static int[] CalculateRootComponents(int[] levels, int[] dynkinLabels)
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
	public static int[] CalculateLevelComponents(int[] levels)
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
	
	/**
	 * Calculates the co-Dynkin labels (i.e. the dynkin labels of the deleted nodes)
	 * NOTE: the rootComponents should not have the subFactor!
	 **/
	public static int[] CalculateCoDynkinLabels(int[] levels, int[] rootComponents)
	{
		int[] coDynkinLabels = new int[coRank];
		
		for (int i = 0; i < coRank; i++)
		{
			coDynkinLabels[i] = 0;
			for (int j = 0; j < coRank; j++)
			{
				coDynkinLabels[i] += cartanMatrix[TranslateCoIndex(i)][TranslateCoIndex(j)] * levels[j];
			}
			for (int j = 0; j < subRank; j++)
			{
				coDynkinLabels[i] += cartanMatrix[TranslateCoIndex(i)][TranslateSubIndex(j)] * rootComponents[j];
			}
		}
		return coDynkinLabels;
	}
	
}
