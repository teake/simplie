/*
 * CAutoLevelScanner.java
 *
 * Created on 20 maart 2007, 14:16
 *
 */

package edu.rug.hep.simplie.leveldecomposer;

import edu.rug.hep.simplie.*;
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
public class CAutoLevelScanner extends SwingWorker<Void,Object[]> implements Comparator<int[]>
{
	private final CAlgebraComposite algebras;
	private final boolean calcRootMult;
	private final boolean calcRepMult;
	private final boolean showZeroMultRoot;
	private final boolean showZeroMultRep;
	private final boolean showExotic;
	private final int minLevel;
	private final int maxLevel;
	private final DefaultTableModel tableModel;
	
	private int levelSign;
	
	private int[] levelOneIndices;
	
	/**
	 * Creates a new instance of CAutoLevelScanner.
	 *
	 * @param	posSignConvention	If true, we will be scanning for highest weight representations (p_i = + A_ij m^j).
	 *								If false, we will be scanning for negative lowest weight representations (p_i = - A_ij m^j).
	 * @param	calcRootMult		If true, the multiplicity of the roots will be calculate. This will take longer.
	 * @param	calcRepMult			If true, the multiplicity of the subalgebra representations will be calculated.
	 *								This again will take longer.
	 * @param	showZeroMultRoot	Indicates wethere or not to show reps that correspond to non-existing roots.
	 * @param	showZeroMultRep		In case this and the above two parameters are true,
	 *								representations with zero outer multiplicity will not be shown.
	 * @param	showExotic			If true, show exotic fields with more indices than the space-time dimension.
	 * @param	tableModel			The DefaultTableModel in which the results of the scan should be put.
	 * @param	minLevel			The minimum value of the levels.
	 * @param	maxLevel			The maximum value of the levels.
	 */
	public CAutoLevelScanner(
			CAlgebraComposite algebras,
			boolean calcRootMult,
			boolean calcRepMult,
			boolean showZeroMultRoot,
			boolean showZeroMultRep,
			boolean showExotic,
			DefaultTableModel tableModel,
			int minLevel,
			int maxLevel)
	{
		this.algebras		= algebras;
		this.tableModel		= tableModel;
		this.minLevel		= minLevel;
		this.maxLevel		= maxLevel;
		this.calcRootMult	= calcRootMult;
		this.calcRepMult	= calcRepMult;
		this.showZeroMultRoot	= showZeroMultRoot;
		this.showZeroMultRep	= showZeroMultRep;
		this.showExotic		= showExotic;
		
		this.levelSign = 0;
		
		this.levelOneIndices = new int[algebras.group.rank - algebras.coGroup.rank];
		for (int i = 0; i < levelOneIndices.length; i++)
		{
			levelOneIndices[i] = 0;
		}
		
	}
	
	/** This comparator sorts levels according to their squared sum. */
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
		if(algebras.group.rank == algebras.coGroup.rank)
			return null;
		
		// How many possibilities of level combinations are there?
		levelRank	= algebras.group.rank - algebras.coGroup.rank;
		base		= maxLevel + 1 - minLevel;
		num			= (int) Math.pow(base, levelRank);
		try
		{
			// Sort the levels.
			levels = new ArrayList<int[]>();
			for (int i = 0; i < num; i++)
			{
				levels.add(Helper.numberToVector(i,base,levelRank,minLevel));
			}
			Collections.sort(levels,this);
			
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
		if(!showExotic && minNumIndices > (algebras.subGroup.rank + 1) )
		{
			System.out.print("Skipping levels " + Helper.intArrayToString(levels));
			System.out.println(", number of indices too large: " + minNumIndices);
			return;
		}
		
		System.out.println("Scanning levels " + Helper.intArrayToString(levels));
		
		// Set up the Dynkin labels
		int[] dynkinLabels = new int[algebras.coGroup.rank];
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
				coLevels = algebras.calculateCoLevels(levels, dynkinLabels);
				int rootLength = algebras.calculateRootLength(levels, coLevels);
				// Only continue if the root length is not bigger than the maximum root length.
				if(rootLength <= algebras.group.rs.maxNorm)
				{
					// First check if all root components are integers and non-negative.
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
						// Add the representation.
						CRepresentation rep = new CRepresentation(
								algebras,
								dynkinLabels,
								levels,
								rootLength
								);
						repContainer.add(rep);
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
				if(algebras.isSignPos())
					k = repContainer.size() - i - 1;
				else
					k = i;
				repI = repContainer.get(k);
				
				// Get and set the root multiplicities.
				CRoot root = algebras.group.rs.getRoot(repI.rootVector.clone());
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
					if(algebras.isSignPos())
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
			// Don't add this representation if its root has zero multiplicity and we don't show those
			if(calcRootMult && !showZeroMultRoot && rep.getRootMult() == 0)
				continue;
			
			// Don't add this representation if it has zero outer multplicity and we don't display zero mults.
			if(calcRootMult && calcRepMult && !showZeroMultRep && rep.getOuterMult() == 0)
				continue;
			
			// Add the data to the table.
			Object[] rowData = new Object[12];
			rowData[0] = Helper.intArrayToString(rep.levels);
			rowData[1] = Helper.intArrayToString(rep.subDynkinLabels);
			rowData[2] = Helper.intArrayToString(rep.intDynkinLabels);
			rowData[3] = Helper.intArrayToString(rep.rootVector);
			rowData[4] = rep.length;
			rowData[5] = (long) algebras.subGroup.dimOfRep(rep.subDynkinLabels);
			rowData[6] = (long) algebras.intGroup.dimOfRep(rep.intDynkinLabels);
			rowData[7] = rep.getRootMult();
			rowData[8] = rep.getOuterMult();
			rowData[9] = rep.height;
			rowData[10] = algebras.isSignPos() ? rep.height : rep.height + 2 * rep.weightHeight;
			rowData[11] = rep.numIndices;
			publish(rowData);
		}
	}
}
