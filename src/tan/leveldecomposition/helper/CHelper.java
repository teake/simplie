/*
 * CHelper.java
 *
 * Created on 12 maart 2007, 10:03
 *
 */

package tan.leveldecomposition.helper;

import Jama.Matrix;

/**
 *
 * @author Teake Nutma
 */
public class CHelper
{
    
    /** Creates a new instance of CHelper */
    public CHelper()
    {
    }
    
    public String IntArrayToString(int[] array)
    {
	String output = new String();
	for (int i = 0; i < array.length; i++)
	{
	    Integer value = new Integer(array[i]);
	    output += value.toString() + " ";
	}
	return output;
    }
    
    public String MatrixToString(Matrix matrix, int decimalPlates)
    {
	int decimalFactor   = Math.round((float) Math.pow(10,decimalPlates));
	int biggestEntry    = 0;
	String stringMatrix = new String("");
	
	if(matrix.getRowDimension() > 0 && matrix.getColumnDimension() > 0)
	{
	    // get the biggest entry first;
	    for(int i = 0; i < matrix.getRowDimension(); i++)
	    {
		for(int j = 0; j < matrix.getColumnDimension(); j++)
		{
		    if(Math.abs(matrix.get(i,j)) > biggestEntry)
			biggestEntry = (int) Math.abs(matrix.get(i,j));
		}
	    }
	    int biggestSize = (int) Math.floor(Math.log10(biggestEntry));
	    
	    for(int i = 0; i < matrix.getRowDimension(); i++)
	    {
		for(int j = 0; j < matrix.getColumnDimension(); j++)
		{
		    if(matrix.get(i,j) >= 0)
		    {
			stringMatrix += " "; // for the spacing of the minus signs
		    }
		    double entry = matrix.get(i,j);
		    int entrySize = (int) Math.floor(Math.log10(entry));
		    if(entrySize == -1) entrySize = 0;
		    for(int k=0; k<biggestSize - entrySize; k++)
		    {
			stringMatrix += " ";
		    }
		    
		    if(decimalPlates == 0)
		    {
			stringMatrix += Math.round(entry);
		    }
		    else
		    {
			stringMatrix += (int) Math.floor(entry) + ".";
			int decimals = (Math.round((float) entry * decimalFactor) % decimalFactor);
			if(decimals == 0)
			{
			    for(int k=0;k<decimalPlates;k++)
			    {
				stringMatrix += "0";
			    }
			}
			else
			{
			    stringMatrix += decimals;
			}
		    }
		    stringMatrix += " ";
		}
		stringMatrix += "\n";
	    }
	}
	return stringMatrix;
    }
}
