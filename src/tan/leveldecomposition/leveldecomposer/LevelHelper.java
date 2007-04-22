/*
 * LevelHelper.java
 *
 * Created on 14 maart 2007, 11:12
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.dynkindiagram.*;
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
	
	/**
	 * The inverse of the Cartan matrix gets multiplied with this value in order to make all entries integers.
	 * Thus also the coLevels and the rootLength get multiplied with it.
	 * Don't forget to divide these with this factor at the end of the day!
	 */
	static int subFactor;
	static int levelRank;
	
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
	public static void setup()
	{
		LevelHelper.subFactor	= Globals.coGroup.det;
		LevelHelper.levelRank	= Globals.group.rank - Globals.coGroup.rank;
		LevelHelper.S			= new int[Globals.coGroup.rank][Globals.coGroup.rank];
		
		/**
		 * Copy both matrices into integer arrays.
		 * NOTE: All values of (cartanMatrixSubInverse * subFactor) of course have to be integers.
		 */
		for(int i=0; i<S.length; i++)
		{
			for(int j=0; j<S.length; j++)
			{
				LevelHelper.S[i][j] = Globals.coGroup.cartanMatrixInv[i][j].times(subFactor).asInt();
			}
		}
	}
	
	/** Returns the actual root length times the subfactor */
	public static int calculateRootLength(int[] levels, int[] dynkinLabels)
	{
		int[] levelComponents = calculateLevelComponents(levels);
		int rootLength = 0;
		
		for(int i=0; i < Globals.coGroup.rank; i++)
		{
			for(int j=0; j < Globals.coGroup.rank; j++)
			{
				rootLength += S[i][j] * ( (dynkinLabels[i] * dynkinLabels[j]) - (levelComponents[i] * levelComponents[j]) );
			}
		}
		for(int i=0; i < levelRank; i++)
		{
			for(int j=0; j < levelRank; j++)
			{
				rootLength += subFactor * Globals.group.cartanMatrix[Globals.dd.translateLevel(i)][Globals.dd.translateLevel(j)] * levels[i] * levels[j];
			}
		}
		
		return rootLength;
	}
	
	/** Returns the root components times the subfactor. */
	public static int[] calculateCoLevels(int[] levels, int[] dynkinLabels)
	{
		int[] rootLabels	= new int[Globals.coGroup.rank];
		int[] levelComponents	= calculateLevelComponents(levels);
		
		for(int i=0; i < rootLabels.length; i++)
		{
			rootLabels[i] = 0;
			for(int j=0; j < Globals.coGroup.rank; j++)
			{
				rootLabels[i] += S[i][j] * ( dynkinLabels[j] - levelComponents[j] );
			}
		}
		
		return rootLabels;
	}
	
	/** Calculates the contraction of the levels with the Cartan matrix. */
	public static int[] calculateLevelComponents(int[] levels)
	{
		int[] levelComponents = new int[Globals.coGroup.rank];
		
		for(int i=0; i < levelComponents.length; i++)
		{
			levelComponents[i] = 0;
			for(int j=0; j < levelRank; j++)
			{
				levelComponents[i] += Globals.group.cartanMatrix[Globals.dd.translateCo(i)][Globals.dd.translateLevel(j)] * levels[j];
			}
		}
		return levelComponents;
	}
	
}
