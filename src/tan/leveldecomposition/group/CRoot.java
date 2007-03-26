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
    public int[]    components;
    public int	    height;
    public int	    mult;
    public int	    norm;
    
    /** Creates a new instance of CRoot */
    public CRoot(int[] components)
    {
	this.components = components;
	this.height	= 0;
	for (int i = 0; i < components.length; i++)
	{
	    height += components[i];
	}
    }
    
}
