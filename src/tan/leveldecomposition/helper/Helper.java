/*
 * CHelper.java
 *
 * Created on 12 maart 2007, 10:03
 *
 */

package tan.leveldecomposition.helper;

import Jama.Matrix;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Singleton class
 *
 * @author Teake Nutma
 */
public class Helper
{
    private static Helper _instance = null;
    
    /** Private constructor */
    private Helper()
    {
    }
    
    private static synchronized void createInstance()
    {
	if(_instance == null)
	    _instance = new Helper();
    }
    
    public static Helper getInstance()
    {
	if(_instance == null) createInstance();
	return _instance;
    }

    public static String IntToString(int x)
    {
	Integer value = new Integer(x);
	return value.toString();
    }
    
    
    public static String IntArrayToString(int[] array)
    {
	String output = new String();
	for (int i = 0; i < array.length; i++)
	{
	    output += IntToString(array[i]) + " ";
	}
	return output;
    }
    
    public static String MatrixToString(Matrix matrix, int decimalPlates)
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
    
    public Helper get_instance()
    {
	return _instance;
    }
}
