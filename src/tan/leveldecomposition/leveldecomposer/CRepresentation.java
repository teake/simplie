/*
 * CRepresentation.java
 *
 * Created on 14 maart 2007, 14:46
 *
 */

package tan.leveldecomposition.leveldecomposer;

/**
 *
 * @author Teake Nutma
 */
public class CRepresentation
{
    int[] levels;
    int[] dynkinLabels;
    int[] rootComponents;
    int rootLength;
    
    /** Creates a new instance of CRepresentation */
    public CRepresentation(int[] l, int[] p, int[] m, int alpha)
    {
	levels		= l.clone();
	dynkinLabels	= p.clone();
	rootComponents	= m.clone();
	rootLength	= alpha;
    }

    public int[] GetLevels()
    {
	return levels.clone();
    }

    public int[] GetDynkinLabels()
    {
	return dynkinLabels.clone();
    }

    public int[] GetRootComponents()
    {
	return rootComponents.clone();
    }
    
    public int GetRootLength()
    {
	return rootLength;
    }
    
}
