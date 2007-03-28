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
    public String   dimension;
    public String   type;
    public boolean  finite;
    
    /** Private properties */
    private int		    constructedHeight;
    private int		    numPosRoots;
    private Vector<Vector>  rootTable;
    
    
    /**
     * Public methods
     */
    
    /**
     * Creates a new instance of CGroup.
     *
     * @param cartanMatrix    The Cartan matrix from which to construct the group.
     */
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
	finite = (det > 0) ? true : false;
	
	/** Try to determine the group type */
	// TODO: make this algorithm find more types
	Matrix compare = Helper.regularMatrix(rank);
	if(Helper.sameMatrices(compare,cartanMatrix))
	    type = "A" + rank;
	else
	{
	    compare.set(0,3,-1);
	    compare.set(3,0,-1);
	    compare.set(0,1,0);
	    compare.set(1,0,0);
	    if(Helper.sameMatrices(compare,cartanMatrix))
		type = "E" + rank;
	}
	
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
	    dim		= 2 * numPosRoots + rank;
	    dimension	= Helper.intToString(dim);
	}
	else
	{
	    dimension	= "Infinite";
	}
    }
    
    
    /**
     * Get a root by its root vector.
     *
     * @param vector	 The root vector of the root we should get.
     * @return		 A pointer to the root if found, and null if not found.
     */
    public CRoot getRoot(int[] vector)
    {
	CRoot		rootToGet;
	Vector<CRoot>	roots;
	
	rootToGet = new CRoot(vector);
	/** If we haven't constructed the root system this far, do so now. */
	if(rootToGet.height > constructedHeight)
	    constructRootSystem(rootToGet.height);
	/** Try to fetch the root. */
	try
	{
	    roots = rootTable.get(rootToGet.height);
	    for(CRoot root : roots)
	    {
		if(root.equals(rootToGet))
		    return root;
	    }
	}
	catch (Exception e)
	{
	}
	return null;
    }
    
    /**
     * Private methods
     */
    
    /**
     * Construct the root system up to the given height.
     *
     * @param maxHeight	 The height up to and including which we should construct the root system.
     *			 Construct the whole root system if maxHeight == 0.
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
    
    
    /**
     * Adds a root to the root table and increments numPosRoots.
     *
     * @param	root	 The root to add.
     * @return		 True if succesfull, false if it already was present.
     */
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
		System.out.println(Helper.intArrayToString(root.vector));
	    }
	}
    }
}
