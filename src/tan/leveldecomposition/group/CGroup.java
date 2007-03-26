/*
 * CGroup.java
 *
 * Created on 26 maart 2007, 17:17
 *
 */

package tan.leveldecomposition.group;

import Jama.Matrix;

/**
 *
 * @author Teake Nutma
 */
public class CGroup
{
    /** Public properties */
    public int[][]  cartanMatrix;
    public int	    rank;
    public int	    det;
    public int	    numRoots;
    public int	    dim;
    public boolean  finite;
    public String   type; //TODO: implement ("E_n", "A_n", etc)
    
    
    /** Private properties */
    private int	    heightConstructed;

    
    /** 
     * Public methods 
     */
    
    /** Creates a new instance of CGroup */
    public boolean CGroup(Matrix cartanMatrix)
    {
	/** Do some preliminary checks */
	if(cartanMatrix.getColumnDimension() != cartanMatrix.getRowDimension()
	|| cartanMatrix.getColumnDimension() == 0)
	    return false;
	
	/** Set the rank, the cartan matrix, the determinant, and the finite property */
	this.rank = cartanMatrix.getColumnDimension();
	for(int i=0; i<rank; i++)
	{
	    for(int j=0; j<rank; j++)
	    {
		this.cartanMatrix[i][j] = (int) Math.round(cartanMatrix.get(i,j));
	    }
	}
	this.det    = (int) Math.round(cartanMatrix.det());
	this.finite = (this.det > 0) ? true : false;

	/** If the group is finite, we can construct the root system to all heights. */
	if(finite)
	    constructRootSystem(0);
	
	return true;
    }

    
    /** 
     * Private methods
     */
    
    /** Construct the root system up to the given height. */
    private void constructRootSystem(int height)
    {
	// do something
    }
}
