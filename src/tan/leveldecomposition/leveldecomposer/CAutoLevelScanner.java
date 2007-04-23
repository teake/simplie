/*
 * CAutoLevelScanner.java
 *
 * Created on 20 maart 2007, 14:16
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.dynkindiagram.*;
import tan.leveldecomposition.group.*;
import tan.leveldecomposition.math.*;
import tan.leveldecomposition.*;

import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


/**
 *
 * @author Teake Nutma
 */
public class CAutoLevelScanner extends SwingWorker<Void,Object[]>
{
	boolean calcRootMult;
	boolean calcRepMult;
	boolean showZeroMult;
	boolean flipDynkinLabels;
	int minLevel;
	int maxLevel;
	int levelSign;
	DefaultTableModel tableModel;
	
	ArrayList<CRepresentation> reps;
	
	/** Creates a new instance of CAutoLevelScanner */
	public CAutoLevelScanner(
			boolean calcRootMult,
			boolean calcRepMult,
			boolean showZeroMult,
			boolean flipDynkinLabels,
			DefaultTableModel tableModel,
			int minLevel,
			int maxLevel)
	{
		this.tableModel		= tableModel;
		this.minLevel		= minLevel;
		this.maxLevel		= maxLevel;
		this.calcRootMult	= calcRootMult;
		this.calcRepMult	= calcRepMult;
		this.showZeroMult	= showZeroMult;
		this.flipDynkinLabels = flipDynkinLabels;
		
		this.levelSign = 0;
	}
	
	@Override
	public Void doInBackground()
	{
		if(minLevel > maxLevel)
			return null;
		if(Globals.group.rank == Globals.coGroup.rank)
			return null;
		
		int levelRank = Globals.group.rank - Globals.coGroup.rank;
		
		int base	= maxLevel + 1 - minLevel;
		long num	= (long) Math.pow(base, levelRank);
		try
		{
			for (long i = 0; i < num; i++)
			{
				Scan(Globals.numberToVector(i,base,levelRank,minLevel));
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
	
	
	/********************************
	 * Methods for scanning reps
	 ********************************/
	
	
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
		int[] dynkinLabels = new int[Globals.coGroup.rank];
		for (int i = 0; i < dynkinLabels.length; i++)
		{
			dynkinLabels[i] = 0;
		}
		
		reps = new ArrayList<CRepresentation>();
		LoopDynkinLabels(levels, dynkinLabels, 0, true);
		processRepresentations();
	}
	
	private void LoopDynkinLabels(int[] levels, int[] dynkinLabels, int beginIndex, boolean scanFirst)
	{
		boolean		allGoodIntegers;
		fraction[]	coLevels;
		
		if (isCancelled())
			return;
		
		do
		{
			if(scanFirst)
			{
				int rootLength = calculateRootLength(levels, dynkinLabels);
				/** Only continue if the root length is not bigger than 2. */
				if(rootLength <= 2)
				{
					/** First check if all root components are integers and non-negative. */
					coLevels  = calculateCoLevels(levels, dynkinLabels);
					allGoodIntegers = true;
					for (int i = 0; i < coLevels.length; i++)
					{
						if(!coLevels[i].isInt() || coLevels[i].asDouble() * levelSign < 0)
						{
							allGoodIntegers = false;
							break;
						}
					}
					/** If we found a valid representation, add it. */
					if(allGoodIntegers)
					{
						addRepresentation(levels, dynkinLabels, coLevels, rootLength);
					}
				}
				else
				{
					/** The root length is bigger than 2, so abort this line. */
					return;
				}
			}
			if(beginIndex + 1 < dynkinLabels.length)
				LoopDynkinLabels(levels, dynkinLabels.clone(), beginIndex + 1, false);
			dynkinLabels[beginIndex]++;
			scanFirst = true;
		} while( !isCancelled() );
	}
	
	
	
	/********************************
	 * Adding & processing reps
	 ********************************/
	
	
	private void addRepresentation(int[] levels, int[] dynkinLabels, fraction[] coLevels, int rootLength)
	{
		int[] newCoLevels = new int[coLevels.length];
		for (int i = 0; i < coLevels.length; i++)
			newCoLevels[i] = coLevels[i].asInt();
		
		/** Add the representation. */
		CRepresentation rep = new CRepresentation(
				dynkinLabels,
				levels,
				newCoLevels,
				rootLength
				);
		reps.add(rep);
	}
	
	private void processRepresentations()
	{
		int		numIndices;
		long	outerSubMult;
		long	outerMult;
		CRepresentation repI;
		CRepresentation repJ;
		
		Collections.sort(reps);
		
		if(calcRootMult)
		{
			int[] heighestWeight;
			int[] weight;
			
			for (int i = 0; i < reps.size(); i++)
			{
				repI = reps.get(i);
				
				/** Get and set the root multiplicities */
				CRoot root = Globals.group.rs.getRoot(repI.rootVector);
				if(root != null)
					repI.setRootMult(root.mult);
				else
					continue;
				
				/**
				 * Calculate the outer multiplicities
				 */
				
				if(!calcRepMult)
					continue;
				
				/** First the outer multiplicities of the regular subalgebra representation */
				outerSubMult = repI.getRootMult();
				for (int j = 0; j < i; j++)
				{
					repJ = reps.get(j);
					if(!Globals.sameArrays(repI.disLevels,repJ.disLevels))
						continue;
					if(repJ.length <= repI.length)
						continue;
					heighestWeight	= repJ.subDynkinLabels;
					weight			= repI.subDynkinLabels;
					outerSubMult -= repJ.getOuterSubMult() * Globals.subGroup.weightMultiplicity(heighestWeight, weight);
				}
				repI.setOuterSubMult(outerSubMult);
				
				/** And now the outer multiplicity of the disconnected subalgebra representation. */
				outerMult = repI.getOuterSubMult();
				for (int j = 0; j < i; j++)
				{
					repJ = reps.get(j);
					/**
					 * This representation can only be a weight of the disconnected representation
					 * if the dynkin labels of the regular subalgebra are the same.
					 */
					if(!Globals.sameArrays(repI.subDynkinLabels,repJ.subDynkinLabels))
						continue;
					if(repJ.length <= repI.length)
						continue;
					heighestWeight	= repJ.disDynkinLabels;
					weight			= repI.disDynkinLabels;
					outerMult -= repJ.getOuterMult() * Globals.disGroup.weightMultiplicity(heighestWeight, weight);
				}
				repI.setOuterMult(outerMult);
			}
		}
		
		/** Publish the representations to the output table. */
		for(CRepresentation rep : reps)
		{
			/** Don't add this representation if is has zero outer multplicity and we don't display zero mults. */
			if(calcRootMult && calcRepMult && !showZeroMult && rep.getOuterMult() == 0)
				continue;
			
			/** Calculate the number of indices of the subalgebra representation. */
			numIndices	= 0;
			for (int i = 0; i < rep.subDynkinLabels.length; i++)
			{
				int j = rep.subDynkinLabels.length - i - 1;
				numIndices += rep.subDynkinLabels[j] * (i+1);
			}
			
			/** Add the data to the table */
			Object[] rowData = new Object[12];
			rowData[0] = Globals.intArrayToString(rep.levels);
			rowData[1] = (flipDynkinLabels) ? Globals.intArrayToString(Globals.flipIntArray(rep.subDynkinLabels)) : Globals.intArrayToString(rep.subDynkinLabels);
			rowData[2] = (flipDynkinLabels) ? Globals.intArrayToString(Globals.flipIntArray(rep.disDynkinLabels)) : Globals.intArrayToString(rep.disDynkinLabels);
			rowData[3] = Globals.intArrayToString(rep.rootVector);
			rowData[4] = rep.length;
			rowData[5] = (long) Globals.subGroup.dimOfRep(rep.subDynkinLabels);
			rowData[6] = (long) Globals.disGroup.dimOfRep(rep.disDynkinLabels);
			rowData[7] = rep.getRootMult();
			rowData[8] = rep.getOuterSubMult();
			rowData[9] = rep.getOuterMult();
			rowData[10] = rep.height;
			rowData[11] = numIndices;
			publish(rowData);
		}
	}
	
	
	
	/********************************
	 * Methods for representation 
	 * calculation
	 ********************************/
	
	/** Returns the actual root length */
	public int calculateRootLength(int[] levels, int[] dynkinLabels)
	{
		int[] levelComponents = calculateLevelComponents(levels);
		fraction rootLength = new fraction(0);
		
		for(int i=0; i < Globals.coGroup.rank; i++)
		{
			for(int j=0; j < Globals.coGroup.rank; j++)
			{
				rootLength.add( Globals.coGroup.cartanMatrixInv[i][j].times(
						(dynkinLabels[i] * dynkinLabels[j]) - (levelComponents[i] * levelComponents[j]) ) );
			}
		}
		for(int i=0; i < levels.length; i++)
		{
			for(int j=0; j < levels.length; j++)
			{
				rootLength.add( Globals.group.cartanMatrix[Globals.dd.translateLevel(i)][Globals.dd.translateLevel(j)] * levels[i] * levels[j] );
			}
		}
		
		return rootLength.asInt();
	}
	
	/** Returns the levels of the co-algebra. */
	public fraction[] calculateCoLevels(int[] levels, int[] dynkinLabels)
	{
		fraction[] coLevels		= new fraction[Globals.coGroup.rank];
		int[] levelComponents	= calculateLevelComponents(levels);
		
		for(int i=0; i < coLevels.length; i++)
		{
			coLevels[i] = new fraction(0);
			for(int j=0; j < Globals.coGroup.rank; j++)
			{
				coLevels[i].add(Globals.coGroup.cartanMatrixInv[i][j].times(dynkinLabels[j] - levelComponents[j]));
			}
		}
		
		return coLevels;
	}
	
	/** Calculates the contraction of the levels with the Cartan matrix. */
	public int[] calculateLevelComponents(int[] levels)
	{
		int[] levelComponents = new int[Globals.coGroup.rank];
		
		for(int i=0; i < levelComponents.length; i++)
		{
			levelComponents[i] = 0;
			for(int j=0; j < levels.length; j++)
			{
				levelComponents[i] += Globals.group.cartanMatrix[Globals.dd.translateCo(i)][Globals.dd.translateLevel(j)] * levels[j];
			}
		}
		return levelComponents;
	}
	
}
