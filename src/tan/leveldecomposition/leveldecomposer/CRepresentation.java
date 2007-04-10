/*
 * CRepresentation.java
 *
 * Created on 10 april 2007, 10:49
 *
 */

package tan.leveldecomposition.leveldecomposer;

import tan.leveldecomposition.group.*;

/**
 *
 * @author Teake Nutma
 */
public class CRepresentation implements Comparable<CRepresentation>
{
	public final CRoot root;
	public final int[] dynkinLabels;
	public final int[] levels;
	public final int[] rootComponents;
	public final int length;
	
	
	/** Creates a new instance of CRepresentation */
	public CRepresentation(CRoot root, int[] dynkinLabels, int[] levels, int[] rootComponents, int length)
	{
		this.root			= root;
		this.dynkinLabels	= dynkinLabels;
		this.levels			= levels;
		this.rootComponents = rootComponents;
		this.length			= length;
	}
	
	/** Sort representations by means of their associated roots. */
	public int compareTo(CRepresentation rep)
	{
		return root.compareTo(rep.root);
	}
}
