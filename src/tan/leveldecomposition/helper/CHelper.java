/*
 * CHelper.java
 *
 * Created on 12 maart 2007, 10:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
    
    public String MatrixToString(Matrix matrix, boolean asInteger)
    {
	String stringMatrix = new String("");
	if(matrix.getRowDimension() > 0 && matrix.getColumnDimension() > 0)
	{
	    for(int i = 0; i < matrix.getRowDimension(); i++)
	    {
		for(int j = 0; j < matrix.getColumnDimension(); j++)
		{
		    if(matrix.get(i,j) >= 0)
		    {
			stringMatrix += " "; // for the spacing of the minus signs
		    }
		    if(asInteger)
		    {
			stringMatrix += (int) matrix.get(i,j);
		    }
		    else
		    {
			stringMatrix += matrix.get(i,j);
		    }
		    stringMatrix += " ";
		}
		stringMatrix += "\n";
	    }
	}
	return stringMatrix;
    }
    
}
