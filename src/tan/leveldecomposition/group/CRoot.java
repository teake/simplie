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
    public int[]    vector;
    public int	    height;
    public int	    mult;
    public int	    norm;
    
    /** Creates a new instance of CRoot */
    public CRoot(int[] rootVector)
    {
	vector = rootVector;
	height = 0;
	for (int i = 0; i < vector.length; i++)
	{
	    height += vector[i];
	}
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
