/*
 * CRoot.java
 *
 * Created on 26 maart 2007, 17:35
 *
 */

package tan.leveldecomposition.group;

/**
 *
 * @author Teake Nutma
 */
public class CRoot
{
    public  int[]	vector;
    private Integer	height;
    
    /**
     * Creates a new instance of CRoot.
     *
     * @param	rootVector   Integer array representing the root vector
     *			     from which we should construct the root.
     */
    public CRoot(int[] rootVector)
    {
	vector = rootVector;
    }
    
    /**
     * Creates a new instance of CRoot with a trivial vector.
     *
     * @param	rank	 The size of the zero-valued vector.
     */
    public CRoot(int rank)
    {
	vector = new int[rank];
	for (int i = 0; i < rank; i++)
	{
	    vector[i] = 0;
	}
	height = 0;
    }
    
    
    
    public int height()
    {
	if(height == null)
	{
	    height = 0;
	    for (int i = 0; i < vector.length; i++)
	    {
		height += vector[i];
	    }
	}
	return height;
    }
    
    public boolean equals(Object obj)
    {
	if(this == obj)
	    return true;
	
	if((obj == null) || (obj.getClass() != this.getClass()))
	    return false;
	
	CRoot compareRoot = (CRoot) obj;
	
	if(vector.length != compareRoot.vector.length)
	    return false;
	
	for (int i = 0; i < vector.length; i++)
	{
	    if(vector[i] != compareRoot.vector[i])
		return false;
	}
	
	return true;
    }
    
}
