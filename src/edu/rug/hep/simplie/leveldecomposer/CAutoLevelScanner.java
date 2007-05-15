/*
 * CAutoLevelScanner.java
 *
 * Created on 20 maart 2007, 14:16
 *
 */

package edu.rug.hep.simplie.leveldecomposer;

import edu.rug.hep.simplie.Globals;
import edu.rug.hep.simplie.group.CRoot;
import edu.rug.hep.simplie.math.fraction;

import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A SwingWorker for scanning regular subalgebra representations within a group.
 * It currently reads its group information from the Globals singleton.
 *
 * @see		Globals
 * @author	Teake Nutma
 */
public class CAutoLevelScanner extends SwingWorker<Void,Object[]>
{
	private final boolean calcRootMult;
	private final boolean calcRepMult;
	private final boolean showZeroMult;
	private final boolean showExotic;
	private final int minLevel;
	private final int maxLevel;
	private final int signConvention;
	private final boolean posSignConvention;
	private final DefaultTableModel tableModel;
	
	private int levelSign;
	
	private int[] levelOneIndices;
	
	/** Comparator to sort all possible levels */
	private Comparator levelComparator;
	
	/**
	 * Creates a new instance of CAutoLevelScanner.
	 *
	 * @param	posSignConvention	If true, we will be scanning for highest weight representations (p_i = + A_ij m^j).
	 *								If false, we will be scanning for negative lowest weight representations (p_i = - A_ij m^j).
	 * @param	calcRootMult		If true, the multiplicity of the roots will be calculate. This will take longer.
	 * @param	calcRepMult			If true, the multiplicity of the subalgebra representations will be calculated.
	 *								This again will take longer.
	 * @param	showZeroMult		In case this and the above two parameters are true,
	 *								representations with zero multiplicity will not be shown.
	 * @param	showExotic			If true, show exotic fields with more indices than the space-time dimension.
	 * @param	tableModel			The DefaultTableModel in which the results of the scan should be put.
	 * @param	minLevel			The minimum value of the levels.
	 * @param	maxLevel			The maximum value of the levels.
	 */
	public CAutoLevelScanner(
			boolean posSignConvention,
			boolean calcRootMult,
			boolean calcRepMult,
			boolean showZeroMult,
			boolean showExotic,
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
		this.showExotic		= showExotic;
		this.signConvention = posSignConvention ? 1 : -1;
		this.posSignConvention = posSignConvention;
		
		this.levelSign = 0;
		
		this.levelOneIndices = new int[Globals.group.rank - Globals.coGroup.rank];
		for (int i = 0; i < levelOneIndices.length; i++)
		{
			levelOneIndices[i] = 0;
		}
		
		// This comparator sorts levels according to their squared sum.
		this.levelComparator = new Comparator<int[]>()
		{
			public int compare(int[] level1, int[] level2)
			{
				final int BEFORE = -1;
				final int EQUAL = 0;
				final int AFTER = 1;
				
				if(level1.length != level2.length)
					return EQUAL;
				
				int sum1 = 0;
				int sum2 = 0;
				for (int i = 0; i < level1.length; i++)
				{
					sum1 += level1[i] * level1[i];
					sum2 += level2[i] * level2[i];
				}
				if(sum1 > sum2) return AFTER;
				if(sum1 < sum2) return BEFORE;
				
				return EQUAL;
			}
		};
	}
	
	@Override
	public Void doInBackground()
	{
		ArrayList<CRepresentation> repContainer;
		ArrayList<CRepresentation> levelOneReps;
		
		ArrayList<int[]> levels;
		
		int levelRank;
		int base;
		int num;
		
		int[] levelOne;
		int[] tempLevelOneIndices;
		
		// Some preliminary checks.
		if(minLevel > maxLevel)
			return null;
		if(Globals.group.rank == Globals.coGroup.rank)
			return null;
		
		// How many possibilities of level combinations are there?
		levelRank	= Globals.group.rank - Globals.coGroup.rank;
		base		= maxLevel + 1 - minLevel;
		num			= (int) Math.pow(base, levelRank);
		try
		{
			// Sort the levels.
			levels = new ArrayList<int[]>();
			for (int i = 0; i < num; i++)
			{
				levels.add(Globals.numberToVector(i,base,levelRank,minLevel));
			}
			Collections.sort(levels,levelComparator);
			
			// Calculate the level one reps
			if(!showExotic)
			{
				tempLevelOneIndices = new int[levelRank];
				for (int i = 0; i < levelRank; i++)
				{
					tempLevelOneIndices[i]	= Integer.MAX_VALUE;
					levelOneReps			= new ArrayList<CRepresentation>();
					levelOne				= new int[levelRank];
					for (int j = 0; j < levelRank; j++)
					{
						levelOne[j] = (i == j) ? 1 : 0;
					}
					Scan(levelOne, levelOneReps);
					for(CRepresentation levelOneRep : levelOneReps)
					{
						tempLevelOneIndices[i] = Math.min(levelOneRep.numIndices, tempLevelOneIndices[i]);
					}
				}
				levelOneIndices = tempLevelOneIndices;
			}
			
			// Perform the scan.
			for (int i = 0; i < levels.size(); i++)
			{
				repContainer = new ArrayList<CRepresentation>();
				Scan((int[]) levels.get(i), repContainer);
				processRepresentations(repContainer);
			}
		}
		catch (Exception e)
		{
			// Because we are in a seperate thread, all exceptions have to be manually printed to sout.
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
	private void Scan(int[] levels, ArrayList<CRepresentation> repContainer)
	{
		// Are the levels all positive or all negative?
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
			// This cannot be, thanks to the triangular decomposition.
			return;
		if(positive)
			levelSign = 1;
		if(negative)
			levelSign = -1;
		
		// Calculate the minimum number of indices at this level
		int minNumIndices = 0;
		for (int i = 0; i < levels.length; i++)
		{
			minNumIndices += levels[i] * levelOneIndices[i];
		}
		
		// Don't scan this level if the number of indices exceeds the number of dimensions
		// and we don't want to scan for exotic fields.
		if(!showExotic && minNumIndices > (Globals.subGroup.rank + 1) )
		{
			System.out.print("Skipping levels " + Globals.intArrayToString(levels));
			System.out.println(", number of indices too large: " + minNumIndices);
			return;
		}
		
		System.out.println("Scanning levels " + Globals.intArrayToString(levels));
		
		// Set up the Dynkin labels
		int[] dynkinLabels = new int[Globals.coGroup.rank];
		for (int i = 0; i < dynkinLabels.length; i++)
		{
			dynkinLabels[i] = 0;
		}
		
		LoopDynkinLabels(levels, dynkinLabels, 0, true, repContainer);
	}
	
	/** Loops through every possible dynkin label. */
	private void LoopDynkinLabels(int[] levels, int[] dynkinLabels, int beginIndex, boolean scanFirst, ArrayList<CRepresentation> repContainer)
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
				// Only continue if the root length is not bigger than 2.
				if(rootLength <= 2)
				{
					// First check if all root components are integers and non-negative.
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
					// If we found a valid representation, add it.
					if(allGoodIntegers)
					{
						addRepresentation(levels, dynkinLabels, coLevels, rootLength, repContainer);
					}
				}
				else
				{
					// The root length is bigger than 2, so abort this line.
					return;
				}
			}
			if(beginIndex + 1 < dynkinLabels.length)
				LoopDynkinLabels(levels, dynkinLabels.clone(), beginIndex + 1, false, repContainer);
			dynkinLabels[beginIndex]++;
			scanFirst = true;
		} while( !isCancelled() );
	}
	
	
	
	/********************************
	 * Adding & processing reps
	 ********************************/
	
	/** Adds a new valid representation to "reps" */
	private void addRepresentation(int[] levels, int[] dynkinLabels, fraction[] coLevels, int rootLength, ArrayList<CRepresentation> repContainer)
	{
		int[] newCoLevels = new int[coLevels.length];
		for (int i = 0; i < coLevels.length; i++)
			newCoLevels[i] = coLevels[i].asInt();
		
		// Add the representation.
		CRepresentation rep = new CRepresentation(
				dynkinLabels,
				levels,
				newCoLevels,
				rootLength,
				posSignConvention
				);
		repContainer.add(rep);
	}
	
	/** Processes all the representations contained in "reps" */
	private void processRepresentations(ArrayList<CRepresentation> repContainer)
	{
		long outerMult;
		CRepresentation repI;
		CRepresentation repJ;
		
		Collections.sort(repContainer);
		
		if(calcRootMult)
		{
			int k;
			int l;
			
			for (int i = 0; i < repContainer.size(); i++)
			{
				// Reverse the order if the sign is positive.
				if(posSignConvention)
					k = repContainer.size() - i - 1;
				else
					k = i;
				repI = repContainer.get(k);
				
				// Get and set the root multiplicities.
				CRoot root = Globals.group.rs.getRoot(repI.rootVector.clone());
				if(root != null)
					repI.setRootMult(root.mult);
				else
					continue;
				
				//
				// Calculate the outer multiplicities
				//
				
				if(!calcRepMult)
					continue;
				
				// First the outer multiplicities of the regular subalgebra representation.
				outerMult = repI.getRootMult();
				for (int j = 0; j < i; j++)
				{
					if(posSignConvention)
						l = repContainer.size() - j - 1;
					else
						l = j;
					repJ = repContainer.get(l);
					
					if(repJ.length <= repI.length)
						continue;
					
					outerMult -= repJ.getOuterMult() * repJ.getWeightMult(repI.dynkinLabels);
				}
				repI.setOuterMult(outerMult);
			}
		}
		
		// Publish the representations to the output table.
		for(CRepresentation rep : repContainer)
		{
			// Don't add this representation if is has zero outer multplicity and we don't display zero mults.
			if(calcRootMult && calcRepMult && !showZeroMult && rep.getOuterMult() == 0)
				continue;
			
			// Add the data to the table.
			Object[] rowData = new Object[11];
			rowData[0] = Globals.intArrayToString(rep.levels);
			rowData[1] = Globals.intArrayToString(rep.subDynkinLabels);
			rowData[2] = Globals.intArrayToString(rep.disDynkinLabels);
			rowData[3] = Globals.intArrayToString(rep.rootVector);
			rowData[4] = rep.length;
			rowData[5] = (long) Globals.subGroup.dimOfRep(rep.subDynkinLabels);
			rowData[6] = (long) Globals.disGroup.dimOfRep(rep.disDynkinLabels);
			rowData[7] = rep.getRootMult();
			rowData[8] = rep.getOuterMult();
			rowData[9] = rep.height;
			rowData[10] = rep.numIndices;
			publish(rowData);
		}
	}
	
	
	
	/********************************
	 * Methods for representation
	 * calculation
	 ********************************/
	
	/** Returns the actual root length */
	private int calculateRootLength(int[] levels, int[] dynkinLabels)
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
	private fraction[] calculateCoLevels(int[] levels, int[] dynkinLabels)
	{
		fraction[] coLevels		= new fraction[Globals.coGroup.rank];
		int[] levelComponents	= calculateLevelComponents(levels);
		
		for(int i=0; i < coLevels.length; i++)
		{
			coLevels[i] = new fraction(0);
			for(int j=0; j < Globals.coGroup.rank; j++)
			{
				coLevels[i].add(Globals.coGroup.cartanMatrixInv[i][j].times(signConvention * dynkinLabels[j] - levelComponents[j]));
			}
		}
		
		return coLevels;
	}
	
	/** Calculates the contraction of the levels with the Cartan matrix. */
	private int[] calculateLevelComponents(int[] levels)
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
