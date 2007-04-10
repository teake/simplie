/*
 * CRepresentation.java
 *
 * Created on 10 april 2007, 10:49
 *
 */

package tan.leveldecomposition.leveldecomposer;

/**
 *
 * @author Teake Nutma
 */
public class CRepresentation implements Comparable<CRepresentation>
{
	public final int[] rootVector;
	public final int[] dynkinLabels;
	public final int[] levels;
	public final int[] rootComponents;
	public final int length;
	public final int height;
	
	
	/** Creates a new instance of CRepresentation */
	public CRepresentation(int[] rootVector, int[] dynkinLabels, int[] levels, int[] rootComponents, int length)
	{
		this.rootVector		= rootVector;
		this.dynkinLabels	= dynkinLabels;
		this.levels			= levels;
		this.rootComponents = rootComponents;
		this.length			= length;

		int tHeight = 0;
		for (int i = 0; i < rootVector.length; i++)
		{
			tHeight += rootVector[i];
		}
		this.height = tHeight;		
	}
	
	/** Sort representations by means of their associated roots. */
	public int compareTo(CRepresentation rep)
	{
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		
		if(height > rep.height)
			return AFTER;
		if(height < rep.height)
			return BEFORE;
		
		return EQUAL;
	}
}
