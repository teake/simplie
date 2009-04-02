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

import edu.simplie.dynkindiagram.DynkinConnection;
import edu.simplie.ui.shapes.LinesWithArrow;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;

/**
 * This class contains miscellaneous helper functions that
 * come in handy from time to time.
 * 
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class Helper
{
	private static final float dash[] = {5.0f,2.0f};
	private static final BasicStroke dashedStroke = new BasicStroke(0.75f,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 
				10.0f, dash, 0.0f);
	private static final BasicStroke normalStroke = new BasicStroke(1.0f);;
	
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
	
	public static String matrixToTex(int[][] matrix)
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
	
	// TODO: remove duplicate code
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

	public static void drawFilledCircle(Graphics2D g, Color c1, Color c2, int x, int y, int radius)
	{
		g.setColor(c1);
		g.fillOval(x - radius, y - radius, 2*radius, 2*radius);
		g.setColor(c2);
		g.drawOval(x - radius, y - radius, 2*radius, 2*radius);	
	}
	
	public static void drawCompactCon(Graphics2D g, Color c, int x1, int y1, int x2, int y2)
	{
		g.setColor(c);
		g.setStroke(dashedStroke);
		int controlx = (x1 + x2 + y1 - y2) / 2;
		int controly = (y1 + y2 + x2 - x1) / 2;
		g.draw(new QuadCurve2D.Float(x1, y1, controlx, controly, x2, y2));
		g.setStroke(normalStroke);
	}
	
	public static void drawConnection(Graphics2D g, Color c, int type, Point begin, Point end, int radius)
	{
		g.setColor(c);
		Shape line;
		switch(type)
		{
			case DynkinConnection.TYPE_SINGLE:
				line = new Line2D.Double(begin,end);
				break;
			case DynkinConnection.TYPE_DOUBLE:
				line = new LinesWithArrow(begin,end,2,2*radius,true);
				break;
			case DynkinConnection.TYPE_TRIPLE:
				line = new LinesWithArrow(begin,end,3,2*radius,true);
				break;
			case DynkinConnection.TYPE_QUADRUPLE:
				line = new LinesWithArrow(begin,end,4,2*radius,true);
				break;
			case DynkinConnection.TYPE_SPECIAL_DOUBLE:
				line = new LinesWithArrow(begin,end,2,2*radius,false);
				break;
			default:
				line = null;
				break;
		}
		if(line != null)
			g.draw(line);	
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
	public static double[][] complexEigenvector(Matrix matrix, double eigenvalRe, double eigenvalIm)
	{
		int size = matrix.getRowDimension();
		double[][] complexEigenvector = new double[2][size];
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < size; j++)
			{
				complexEigenvector[i][j] = 0.0d;
			}
		}

		// JAMA doesn't do imaginary eigenvectors ... sigh.
		Matrix A = matrix.minus(Matrix.identity(size,size).times(eigenvalRe));
		Matrix B = new Matrix(2*size, 2*size, 0.0);
		B.setMatrix(0,size-1,size,(2*size)-1,A);
		B.setMatrix(size, (2*size)-1, 0, size-1, A.times(-1.0));

		EigenvalueDecomposition eig = new EigenvalueDecomposition(B);
		Matrix eigenvalues	= eig.getD();
		Matrix eigenvectors	= eig.getV();

		for(int i = 0; i < 2*size; i++)
		{
			if((eigenvalues.get(i, i) < eigenvalIm + 0.0001) && (eigenvalues.get(i, i) > eigenvalIm - 0.0001))
			{
				// Found the complex part of the right eigenvalue.
				// Set the eigenvectors.
				for(int j = 0; j < size; j++)
				{
					complexEigenvector[0][j] = eigenvectors.get(j, i);
					complexEigenvector[1][j] = eigenvectors.get(j+size, i);
				}
				break;
			}
		}

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
}
