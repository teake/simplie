/*
 * Helper.java
 *
 * Created on 19-jun-2007, 9:19:50
 *
 * This file is part of SimpLie.
 * 
 * SimpLie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SimpLie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SimpLie.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package edu.simplie;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import edu.simplie.math.*;
import java.util.ArrayList;

/**
 * This class contains miscellaneous helper functions that
 * come in handy from time to time.
 * 
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class Helper
{	
	public Helper()
	{
	}
	
	/**
	 * Clones a two-dimensional integer array.
	 * 
	 * @param matrix	 The matrix to be cloned.
	 * @return			 A clone of the original matrix.
	 */
	public static int[][] cloneMatrix(int[][] matrix)
	{
		int[][] clone = matrix.clone();
		for(int i = 0; i < clone.length; i++)
		{
			clone[i] = matrix[i].clone();			
		}
		return clone;
	}

	/**
	 * Takes an matrix of integers and spits out a Jama Matrix object.
	 * @param array		The 2-dimensional array of integers to convert.
	 * @return			A Jama Matrix based on the input.
	 */
	public static Matrix intArrayToMatrix(int[][] array)
	{
		Matrix matrix = new Matrix(array.length, array.length);
		for (int i = 0; i < array.length; i++)
		{
			for(int j = 0; j < array.length; j++)
			{
				matrix.set(i,j,array[i][j]);
			}
		}
		return matrix;
	}
	
	/**
	 * Converts a string into an integer. If the string cannot be parsed into an integer,
	 * it returns 0.
	 *
	 * @param	string	The string to convert into an integer.
	 * @return			The integer value of the string.
	 * @see				#intToString
	 */
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
	
	/**
	 * Converts an integer into a string.
	 *
	 * @param	x		The integer to convert to a string.
	 * @return			A string representing the integer.
	 * @see				#stringToInt
	 */
	public static String intToString(int x)
	{
		Integer value = new Integer(x);
		return value.toString();
	}
	
	/**
	 * Converts an array of integers integer into a single string.
	 * The string will be delimited with spaces.
	 *
	 * @param	array	The array of integers to convert to a string.
	 * @return			A string representing the array of integers.
	 */
	public static String intArrayToString(int[] array)
	{
		String output = new String();
		for (int i = 0; i < array.length; i++)
		{
			output += intToString(array[i]);
			if(i != array.length - 1)
				output += " ";
		}
		return output;
	}
	
	/**
	 * Splits a string into and array of integers.
	 * The delimiter is a space (" ") and is fixed.
	 * For example, the string "1 2 3 4" returns the array [1,2,3,4].
	 * 
	 * @param   string  The string to split.
	 * @return	    An integer array.
	 */
	public static int[] stringToIntArray(String string)
	{
		ArrayList<Integer> values = new ArrayList<Integer>();
		String[] array = string.trim().split(" ");
		for(int i = 0; i < array.length; i++)
		{
			Integer value = stringToInt(array[i]);
			if(value.toString().equals(array[i]))
			{
				values.add(value);
			}
		}
		int[] output = new int[values.size()];
		for (int i = 0; i < values.size(); i++)
		{
			output[i] = values.get(i);
		}
		
		return output;
	}
	
	/**
	 * Compares two matrices of integers.
	 * The matrices are the same if all their values are the same.
	 *
	 * @param	matrix1	The first matrix we compare against the second.
	 * @param	matrix2	The second matrix we compare against the first.
	 * @return			True if the matrices are the same, false otherwise.
	 */
	public static boolean sameMatrices(int[][] matrix1, int[][] matrix2)
	{
		if(matrix1 == null || matrix2 == null)
			return false;
		if(matrix1.length != matrix2.length)
			return false;
		for (int i = 0; i < matrix1.length; i++)
		{
			for (int j = 0; j < matrix1.length; j++)
			{
				if(matrix1[i][j] != matrix2[i][j])
					return false;
			}
		}
		return true;
	}
	

	/**
	 * Convert a decimal number to a vector of the given size in the given basis,
	 * with given offset.
	 */
	public static int[] numberToVector(long number, int base, int size, int offset)
	{
		ArrayList<Integer> stack = new ArrayList<Integer>();
		while(number >= base)
		{
			stack.add((int) (number % base));
			number = number / base;
		}
		stack.add((int) number);
		
		int[] vector = new int[size];
		for (int i = 0; i <stack.size(); i++)
		{
			vector[i] = (int) stack.get(i) + offset;
		}
		for (int i = stack.size(); i < size; i++)
		{
			vector[i] = 0 + offset;
		}
		return vector;
	}

	/**
	 * Takes a fractional matrix and returns it as a nicely formatted string,
	 * with all entries as "numerator / denominator".
	 * 
	 * @param matrix	 The fractional matrix to be formatted to a string.
	 * @return			 A string representing the matrix.
	 */
	public static String matrixToString(fraction[][] matrix)
	{
		long biggestNum = 0;
		long biggestDen = 0;
		int numSize = 0;
		int denSize = 0;
		boolean allInt = true;
		String output = new String("");
		
		// First find the biggest numerator and denominator.
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix.length; j++)
			{
				if(matrix[i][j] == null)
					return "";
				if(!matrix[i][j].isInt())
					allInt = false;
				biggestNum = Math.max( biggestNum, Math.abs(matrix[i][j].numerator()) );
				biggestDen = Math.max( biggestDen, Math.abs(matrix[i][j].denominator()) );
			}
		}
		
		// Count the number of digits in the biggest numerator and denominator.
		biggestNum = (long) Math.floor(Math.log10(biggestNum));
		biggestDen = (long) Math.floor(Math.log10(biggestDen));
		
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix.length; j++)
			{
				if(matrix[i][j].numerator() == 0)
				{
					numSize = 0;
				}
				else
				{
					numSize = (int) Math.floor(Math.log10(Math.abs(matrix[i][j].numerator())));
				}

				if(!allInt && matrix[i][j].isInt())
				{
					denSize = -2;
				}
				else
				{
					denSize = (int) Math.floor(Math.log10(Math.abs(matrix[i][j].denominator())));
				}
				
				for(int k = 0; k < biggestNum - numSize; k++)
				{
					output += " ";
				}
				if(matrix[i][j].asDouble() >= 0)
				{
					output += " ";
				}
				output += matrix[i][j].toString();
				for(int k = 0; k < biggestDen - denSize; k++)
				{
					output += " ";
				}
				output += " ";
			}
			output += "\n";
		}
		return output;
	}
	
	
	/**
	 * Takes an integer matrix and returns it as a string,
	 * with all entries formatted to have the given decimal plates.
	 *
	 * @param	matrix	 The integer matrix to be formatted to a string.
	 * @return			 A string representing the matrix.
	 */
	public static String matrixToString(int[][] matrix)
	{
		int biggestEntry = 0;
		int entrySize = 0;
		String stringMatrix = new String("");
		
		/** Determine the biggest entry first for the whitespacing */
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix.length; j++)
			{
				biggestEntry = Math.max(Math.abs(matrix[i][j]), biggestEntry);
			}
		}
		int biggestSize = (int) Math.floor(Math.log10(biggestEntry));

		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix.length; j++)
			{
				/** Append the correct amount of whitespace */
				if(matrix[i][j] >= 0)
					stringMatrix += " "; // for the spacing of the minus signs
				if(matrix[i][j] == 0)
					entrySize = 0;
				else
					entrySize = (int) Math.floor(Math.log10(Math.abs(matrix[i][j])));
				for(int k=0; k<biggestSize - entrySize; k++)
				{
					stringMatrix += " ";
				}
				stringMatrix += matrix[i][j];
				stringMatrix += " ";
			}
			stringMatrix += "\n";
		}
		
		return stringMatrix;
	}

	/**
	 * Takes a rectangular matrix of integers and spits out a LaTeX formatted String.
	 *
	 * @param matrix	The 2-dimensional array of integers to convert.
	 * @return			A LaTeX formatted string based on the input.
	 */
	public static String matrixToTex(int[][] matrix)
	{
		String[][] m = new String[matrix.length][matrix.length];
		for(int i = 0; i < m.length; i++)
		{
			for(int j = 0; j < m.length; j++)
			{
				m[i][j] = intToString(matrix[i][j]);
			}
		}
		return matrixToTex(m);
	}
	
	/**
	 * Takes a rectangular matrix of objects and spits out a LaTeX formatted String.
	 *
	 * @param matrix	The 2-dimensional array of object to convert.
	 * @return			A LaTeX formatted string based on the input.
	 */
	public static String matrixToTex(Object[][] matrix)
	{
		String matrixString = "\\begin{equation}\n\\left(\\begin{array}{";
		for(int i = 0; i < matrix.length; i++)
		{
			matrixString += "r";
		}
		matrixString += "}\n";
		
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix.length; j++)
			{
				matrixString += matrix[i][j];
				if(j != matrix.length - 1)
					matrixString += "\t & ";
			}
			if(i != matrix.length - 1)
				matrixString += " \\\\ \n";
		}
	
		matrixString += "\n\\end{array}\\right)\n\\end{equation}\n";
		return matrixString;
	}
	
	/**
	 * Mixes two colors based on a percentage.
	 * 
	 * @param col1	 The first color to mix.
	 * @param col2	 The second color to mix.
	 * @param perc	 Percentage indicating how much of the first color should be used.
	 * @return		 The mixed color.
	 */
	public static float[] mixColors(float[] col1, float[] col2, float perc)
	{
		float[] newCol = new float[col1.length];
		for (int i = 0; i < col1.length; i++)
		{
			newCol[i] = perc * col1[i] + (1-perc) * col2[i];
		}
		return newCol;
	}
	
	/**
	 * Returns a color in the color spectrum R-G-B, based on percentage.
	 * 
	 * @param perc	 A float in the range 0 - 1.
	 * @return		 0: Red, 1/3: Green, 2/3: Blue, 1: Red again, and in between a mixture.
	 */
	public static float[] colorSpectrum(float perc)
	{
		perc = Math.abs(perc % 1);
		float[] newCol = {0.0f, 0.0f, 0.0f};
		if(3 * perc < 1.0f)
		{
			perc = 3 * perc;
			newCol[0] = (float) Math.sqrt(1 - perc);
			newCol[1] = (float) Math.sqrt(perc);
		}
		else if (3 * perc < 2.0f)
		{
			perc = 3 * perc - 1.0f;
			newCol[1] = (float) Math.sqrt(1 - perc);
			newCol[2] = (float) Math.sqrt(perc);
		}
		else
		{
			perc = 3 * perc - 2.0f;
			newCol[2] = (float) Math.sqrt(1 - perc);
			newCol[0] = (float) Math.sqrt(perc);
		}
		return newCol;
	}

	/**
	 * Determines the complex eigenvector of a given real matrix belonging to
	 * the given complex eigenvalue. The specified complex eigenvalue has to be
	 * an actual eigenvalue of the given matrix.
	 *
	 * @param matrix		 The matrix for which to compute the complex eigenvalue. Has to be real.
	 * @param eigenvalRe	 The real part of the complex eigenvalue.
	 * @param eigenvalIm	 The imaginary part of the complex eigenvalue.
	 * @return				 The complex eigenvector belong to the given arguments,
	 *						 with the first entry for the real part and the second for the imaginary.
	 *						 Both are zero vectors if no eigenvalue can be found.
	 */
	public static double[][] complexEigenvector(double[][] matrix, double eigenvalRe, double eigenvalIm)
	{
		int size = matrix.length;
		double[][] complexEigenvector = new double[2][size];

		Jampack.Zmat m = new Jampack.Zmat(matrix);
		try
		{
			Jampack.Eig eig		= new Jampack.Eig(m);
			for(int i = 0; i < size; i++)
			{
				double re = eig.D.get0(i).re;
				double im = eig.D.get0(i).im;
				if((im < eigenvalIm + 0.0001)
						&& (im > eigenvalIm - 0.0001)
						&& (re < eigenvalRe + 0.0001)
						&& (re > eigenvalRe - 0.0001))
				{
					for(int j = 0; j < size; j++)
					{
						complexEigenvector[0][j] = eig.X.get0(j,i).re;
						complexEigenvector[1][j] = eig.X.get0(j,i).im;
					}
					break;
				}
			}
		}
		catch(Exception ex){}

		return complexEigenvector;
	}


	/**
	 * Rotate a 2-dimension vector over an angle.
	 * @param vector	The vector to rotate.
	 * @param angle		The angle over which to rotate.
	 * @return			The rotated vector.
	 */
	public static double[] rotate(double[] vector, double angle)
	{
		double[] result = new double[2];

		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		result[0] = cos * vector[0] - sin * vector[1];
		result[1] = sin * vector[0] + cos * vector[1];

		return result;
	}

	/**
	 * Calculates the first null eigenvectors of a matrix with determinant 0.
	 * @param matrix	The matrix of which to calculate a null eigenvector.
	 * @return			A rounded integer vector normalized s.t. the lowest entry
	 *					is equal to 1.
	 */
	public static int[] nullEigenVector(Matrix matrix)
	{
		int rank			= matrix.getRowDimension();
		int[] nullVector	= new int[rank];

		EigenvalueDecomposition eig = matrix.eig();
		Matrix eigVals = eig.getD();
		Matrix eigVecs = eig.getV();
		double[] eigVec = new double[rank];
		for(int i = 0; i < rank; i++)
		{
			if(Math.abs(eigVals.get(i, i)) < 0.0000001)
			{
				for(int j = 0; j < rank; j++)
				{
					eigVec[j] = eigVecs.getArray()[j][i];
				}
				break;
			}
		}
		// Normalize the null eigenvector
		double min = Double.MAX_VALUE;
		for(int i = 0; i < rank; i++)
		{
			if(Math.abs(eigVec[i]) > 0.0000001 && Math.abs(eigVec[i]) < Math.abs(min))
				min = eigVec[i];
		}
		for(int i = 0; i < rank; i++)
		{
			nullVector[i] = (int) Math.round(( eigVec[i] / min ));
		}

		return nullVector;
	}
}
