/*
 * CGroup.java
 *
 * Created on 26 maart 2007, 17:17
 *
 */

package tan.leveldecomposition.group;

import tan.leveldecomposition.helper.*;
import java.util.Vector;
import Jama.Matrix;
import java.text.DecimalFormat;

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
    private int		    constructedHeight;
    private int		    numPosRoots;
    private Vector<Vector>  rootTable;
    
    
    /**
     * Public methods
     */
    
    /** Creates a new instance of CGroup */
    public CGroup(Matrix cartanMatrix)
    {
	/** Do some preliminary checks */
	if(cartanMatrix.getColumnDimension() != cartanMatrix.getRowDimension()
	|| cartanMatrix.getColumnDimension() == 0)
	    return;
	
	/** Set the rank, the cartan matrix, the determinant, and the finite property */
	rank = cartanMatrix.getColumnDimension();
	this.cartanMatrix = new int[rank][rank];
	for(int i=0; i<rank; i++)
	{
	    for(int j=0; j<rank; j++)
	    {
		this.cartanMatrix[i][j] = (int) Math.round(cartanMatrix.get(i,j));
	    }
	}
	det    = (int) Math.round(cartanMatrix.det());
	finite = (this.det > 0) ? true : false;
	
	/** Try to determine the group type */
	Matrix diff = Helper.RegularMatrix(rank).minus(cartanMatrix);
	boolean zero = true;
	for (int i = 0; i < rank; i++)
	{
	    for (int j = 0; j < rank; j++)
	    {
		if(diff.get(i,j) != 0)
		    zero = false;
	    }
	}
	if(zero)
	    type = "A" + rank;
	
	
	/** Add an empty Vector at index 0 of the root table */
	rootTable = new Vector<Vector>();
	rootTable.add(0,new Vector<CRoot>());
	/** Add the simple roots to the root table */
	for (int i = 0; i < rank; i++)
	{
	    int[] rootVector = new int[rank];
	    for (int j = 0; j < rank; j++)
	    {
		rootVector[j] = ( i == j) ? 1 : 0;
	    }
	    addRoot(new CRoot(rootVector));
	}
	constructedHeight = 1;
	
	/** If the group is finite, we can construct the root system to all heights. */
	if(finite)
	{
	    constructRootSystem(0);
	    dim = 2 * numPosRoots + rank;
	}
     }
    
    
    /**
     * Private methods
     */
    
    /**
     * Construct the root system up to the given height.
     * If maxHeight == 0, construct the whole root system.
     */
    private void constructRootSystem(int maxHeight)
    {
	Vector<CRoot>	simpleRoots = rootTable.get(1);
	Vector<CRoot>	previousRoots;
	boolean		addedSomething;
	
	while(constructedHeight < maxHeight || maxHeight == 0)
	{
	    addedSomething  = false;
	    previousRoots   = rootTable.get(constructedHeight);
	    
	    /** Try to add the simple roots to all the previous roots */
	    for(CRoot root : previousRoots)
	    {
		for(CRoot simpleRoot : simpleRoots)
		{
		    if(innerProduct(root,simpleRoot) < 0)
		    {
			/** root + simpleRoot is a root, so add it to the root table */
			if(addRoot(sumRoots(root,simpleRoot)))
			    addedSomething = true;
		    }
		}
	    }
	    
	    if(addedSomething)
	    {
		/** Add the next height of roots to the root table */
		constructedHeight++;
	    }
	    else
	    {
		/** We did nothing, and thus reached the highest root */
		break;
	    }
	}
    }
    
    
    private int innerProduct(CRoot root1, CRoot root2)
    {
	int result = 0;
	for (int i = 0; i < rank; i++)
	{
	    for (int j = 0; j < rank; j++)
	    {
		result += cartanMatrix[i][j] * root1.vector[i] * root2.vector[j];
	    }
	}
	return result;
    }
    
    private boolean addRoot(CRoot root)
    {
	Vector<CRoot> roots;
	try
	{
	    roots = rootTable.get(root.height);
	    if(roots.contains(root))
		return false;
	    roots.add(root);
	}
	catch (Exception e)
	{
	    roots = new Vector<CRoot>();
	    roots.add(root);
	    rootTable.add(root.height,roots);
	}
	numPosRoots++;
	return true;
    }
    
    
    private CRoot sumRoots(CRoot root1, CRoot root2)
    {
	int[] rootVector = new int[rank];
	for (int i = 0; i < rank; i++)
	{
	    rootVector[i] = root1.vector[i] + root2.vector[i];
	}
	return new CRoot(rootVector);
    }
    
    private void printRootTable()
    {
	for(Vector<CRoot> roots : rootTable)
	{
	    for(CRoot root : roots)
	    {
		System.out.println(Helper.IntArrayToString(root.vector));
	    }
	}
    }
}
