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
	private long outerMult;
	private long rootMult;
	
	
	/** Creates a new instance of CRepresentation */
	public CRepresentation(int[] rootVector, int[] dynkinLabels, int[] levels, int[] rootComponents, int length)
	{
		this.rootVector		= rootVector;
		this.dynkinLabels	= dynkinLabels;
		this.levels			= levels;
		this.rootComponents = rootComponents;
		this.length			= length;
		this.rootMult		= 0;
		this.outerMult		= 0;
		
		int tHeight = 0;
		for (int i = 0; i < rootVector.length; i++)
		{
			tHeight += rootVector[i];
		}
		this.height = tHeight;
	}
	
	public void setOuterMult(long outerMult)
	{
		this.outerMult = outerMult;
	}
	
	public long getOuterMult()
	{
		return outerMult;
	}
	
	public void setRootMult(long rootMult)
	{
		this.rootMult = rootMult;
	}
	
	public long getRootMult()
	{
		return rootMult;
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
