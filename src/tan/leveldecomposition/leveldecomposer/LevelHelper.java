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
	
	/**
	 * The inverse of the Cartan matrix gets multiplied with this value in order to make all entries integers.
	 * Thus also the rootComponents and the rootLength get multiplied with it.
	 * Don't forget to divide these with this factor at the end of the day!
	 */
	static int subFactor;
	
	static int signConvention	= 1;
	
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
		LevelHelper.subFactor		= Globals.subGroup.det;
		LevelHelper.S				= new int[Globals.subGroup.rank][Globals.subGroup.rank];
		
		/**
		 * Copy both matrices into integer arrays.
		 * NOTE: All values of (cartanMatrixSubInverse * subFactor) of course have to be integers.
		 */
		for(int i=0; i<Globals.subGroup.rank; i++)
		{
			for(int j=0; j<Globals.subGroup.rank; j++)
			{
				LevelHelper.S[i][j] = Globals.subGroup.cartanMatrixInv[i][j].times(subFactor).asInt();
			}
		}
	}
	
	
	public static void SetSignConvention(int sign)
	{
		if(!(sign == 1 || sign == -1))
			return;
		signConvention = sign;
	}
	
	/** Returns the actual root length times the subfactor */
	public static int CalculateRootLength(int[] levels, int[] dynkinLabels)
	{
		int[] levelComponents = CalculateLevelComponents(levels);
		int rootLength = 0;
		
		for(int i=0; i < Globals.subGroup.rank; i++)
		{
			for(int j=0; j < Globals.subGroup.rank; j++)
			{
				rootLength += S[i][j] * ( (dynkinLabels[i] * dynkinLabels[j]) - (levelComponents[i] * levelComponents[j]) );
			}
		}
		for(int i=0; i < Globals.delGroup.rank; i++)
		{
			for(int j=0; j < Globals.delGroup.rank; j++)
			{
				rootLength += subFactor * Globals.group.cartanMatrix[DynkinDiagram.translateCoIndex(i)][DynkinDiagram.translateCoIndex(j)] * levels[i] * levels[j];
			}
		}
		
		return rootLength;
	}
	
	/** Returns the root components times the subfactor. */
	public static int[] CalculateRootComponents(int[] levels, int[] dynkinLabels)
	{
		int[] rootLabels	= new int[Globals.subGroup.rank];
		int[] levelComponents	= CalculateLevelComponents(levels);
		
		for(int i=0; i < Globals.subGroup.rank; i++)
		{
			rootLabels[i] = 0;
			for(int j=0; j < Globals.subGroup.rank; j++)
			{
				rootLabels[i] += S[i][j] * ( signConvention * dynkinLabels[j] - levelComponents[j] );
			}
		}
		
		return rootLabels;
	}
	
	/** Calculates the contraction of the levels with the Cartan matrix. */
	public static int[] CalculateLevelComponents(int[] levels)
	{
		int[] levelComponents = new int[Globals.subGroup.rank];
		
		for(int i=0; i < Globals.subGroup.rank; i++)
		{
			levelComponents[i] = 0;
			for(int j=0; j < Globals.delGroup.rank; j++)
			{
				levelComponents[i] += Globals.group.cartanMatrix[DynkinDiagram.translateSubIndex(i)][DynkinDiagram.translateCoIndex(j)] * levels[j];
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
		int[] coDynkinLabels = new int[Globals.delGroup.rank];
		
		for (int i = 0; i < Globals.delGroup.rank; i++)
		{
			coDynkinLabels[i] = 0;
			for (int j = 0; j < Globals.delGroup.rank; j++)
			{
				coDynkinLabels[i] += Globals.group.cartanMatrix[DynkinDiagram.translateCoIndex(i)][DynkinDiagram.translateCoIndex(j)] * levels[j];
			}
			for (int j = 0; j < Globals.subGroup.rank; j++)
			{
				coDynkinLabels[i] += Globals.group.cartanMatrix[DynkinDiagram.translateCoIndex(i)][DynkinDiagram.translateSubIndex(j)] * rootComponents[j];
			}
		}
		return coDynkinLabels;
	}
	
}
