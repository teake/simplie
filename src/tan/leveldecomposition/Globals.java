/*
 * Globals.java
 *
 * Created on 27 maart 2007, 17:17
 *
 */

package tan.leveldecomposition;

import tan.leveldecomposition.group.*;
import Jama.Matrix;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Singleton holding all 'global' variables
 *
 * @author Teake Nutma
 */
public class Globals
{
 
    /**********************************
     * Global variables
     **********************************/
    
    /** Global CGroup object for the full group */
    public static CGroup group;
    /** Global CGroup object for the regular subgroup */
    public static CGroup subGroup;
    
    
    /**********************************
     * Singleton stuff
     **********************************/
    
    private static Globals _instance = null;
    
    /** Private constructor */
    private Globals()
    {
    }
    
    private static synchronized void createInstance()
    {
	if(_instance == null)
	    _instance = new Globals();
    }
    
    /** Singleton containing 'global' variables. */
    public static Globals getInstance()
    {
	if(_instance == null) createInstance();
	return _instance;
    }
    
    
    /**********************************
     * Helper functions defined below
     **********************************/
    
    
    public static int stringToInt(String string)
    {
	int value;
	try
	{
	    value = Integer.parseInt(string);
	}
	catch (Exception e)
	{
	    value = 0;
	}
	return value;
    }
    
    public static String intToString(int x)
    {
	Integer value = new Integer(x);
	return value.toString();
    }
    
    
    public static String intArrayToString(int[] array)
    {
	String output = new String();
	for (int i = 0; i < array.length; i++)
	{
	    output += intToString(array[i]) + " ";
	}
	return output;
    }
    
    /**
     * Returns the A_n cartan matrix of the given rank.
     *
     * @param	 rank	 The rank of the A_n matrix to be returned.
     * @return		 The A_n cartan matrix of the given rank.
     */
    public static Matrix regularMatrix(int rank)
    {
	Matrix matrix = new Matrix(rank, rank);
	for (int i = 0; i < rank; i++)
	{
	    for (int j = 0; j < rank; j++)
	    {
		if(i == j)
		    matrix.set(i,j,2);
		if(j == i-1 || j == i+1)
		    matrix.set(i,j,-1);
	    }
	}
	return matrix;
    }
    
    /**
     * Compares two Matrices.
     * Appareantly Matrix.equals() isn't properly implemented ...
     *
     * @param	matrix1	 The first matrix we compare against the second.
     * @param	matrix2	 The second matrix we compare against the first.
     * @return		 True if the matrices are the same, false otherwise.
     */
    public static boolean sameMatrices(Matrix matrix1, Matrix matrix2)
    {
	if(matrix1.getColumnDimension() != matrix2.getColumnDimension())
	    return false;
	if(matrix1.getRowDimension() != matrix2.getRowDimension())
	    return false;
	for (int i = 0; i < matrix1.getRowDimension(); i++)
	{
	    for (int j = 0; j < matrix1.getColumnDimension(); j++)
	    {
		if(matrix1.get(i,j) != matrix2.get(i,j))
		    return false;
	    }
	}
	return true;
    }
    
    /**
     * Takes a matrix and returns it as a string,
     * with all entries formatted to have the given decimal plates.
     *
     * @param	matrix		 The matrix to be formatted to a string.
     * @param	decimalPlates	 The number of decimals each entry will have.
     * @return			 A string representing the matrix.
     */
    public static String matrixToString(Matrix matrix, int decimalPlates)
    {
	/** Set up the decimal format for double -> string parsing */
	String dfPattern = new String("0");
	for (int i = 0; i < decimalPlates; i++)
	{
	    if(i==0) dfPattern += ".";
	    dfPattern += "0";
	}
	DecimalFormat df = new DecimalFormat( dfPattern );
	
	double biggestEntry = 0;
	String stringMatrix = new String("");
	
	if(matrix.getRowDimension() > 0 && matrix.getColumnDimension() > 0)
	{
	    /** Determine the biggest entry first for the whitespacing */
	    for(int i = 0; i < matrix.getRowDimension(); i++)
	    {
		for(int j = 0; j < matrix.getColumnDimension(); j++)
		{
		    if(Math.abs(matrix.get(i,j)) > biggestEntry)
			biggestEntry = Math.abs(matrix.get(i,j));
		}
	    }
	    int biggestSize = (int) Math.floor(Math.log10(biggestEntry));
	    
	    for(int i = 0; i < matrix.getRowDimension(); i++)
	    {
		for(int j = 0; j < matrix.getColumnDimension(); j++)
		{
		    /** Round it properly */
		    BigDecimal bd   = new BigDecimal(matrix.get(i,j));
		    bd		    = bd.setScale(decimalPlates,BigDecimal.ROUND_HALF_UP);
		    double entry    = bd.doubleValue();
		    
		    /** Append the correct amount of whitespace */
		    if(entry >= 0)
			stringMatrix += " "; // for the spacing of the minus signs
		    int entrySize = Math.max( (int) Math.floor(Math.log10(Math.abs(entry))), 0 );
		    for(int k=0; k<biggestSize - entrySize; k++)
		    {
			stringMatrix += " ";
		    }
		    
		    /** Format it correctly */
		    stringMatrix += df.format(entry);
		    stringMatrix += " ";
		}
		stringMatrix += "\n";
	    }
	}
	return stringMatrix;
    }
    
}
