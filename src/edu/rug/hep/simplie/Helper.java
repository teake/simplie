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

package edu.rug.hep.simplie;

import edu.rug.hep.simplie.math.*;

import Jama.Matrix;
import edu.rug.hep.simplie.dynkindiagram.CDynkinConnection;
import edu.rug.hep.simplie.ui.shapes.LinesWithArrow;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
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
	 * Returns the cartan matrix of the given rank and type.
	 *
	 * @param	 rank		The rank of the matrix to be returned.
	 * @param	 extended	The number of times the matrix is extended.
	 *						(1=affine extension,2=over extended, 3=very extended, etc).
	 * @param	 type		The type of the Cartan matrix, "A", "B", etc.
	 * @return				The cartan matrix of the given rank, or null if the type is illegal.
	 */
	public static int[][] cartanMatrix(int rank, int extended, String type)
	{
		int[][] matrix = new int[rank][rank];
		int simpleRank = rank - extended;
		
		if(simpleRank <= 0)
			return null;
		
		// First do the regular A-series matrix.
		for (int i = 0; i < rank; i++)
		{
			for (int j = 0; j < rank; j++)
			{
				if(i == j)
					matrix[i][j] = 2;
				else if(j == i-1 || j == i+1)
					matrix[i][j] = -1;
				else
					matrix[i][j] = 0;
			}
		}
		
		// And now the deviations from it.
		
		if(type.equals("A"))
		{
			if(extended > 0)
			{
				if(simpleRank == 1)
					matrix[0][1] = matrix[1][0] = -2;
				else
					matrix[0][simpleRank] = matrix[simpleRank][0] = -1;
			}
			return matrix;
		}
		
		if(type.equals("B") && simpleRank > 1)
		{
			matrix[1][0] = -2;
			if(extended > 0)
			{
				matrix[simpleRank][simpleRank-1] = matrix[simpleRank-1][simpleRank] = 0;
				matrix[simpleRank][simpleRank-2] = matrix[simpleRank-2][simpleRank] = -1;
			}
			return matrix;
		}
		
		if(type.equals("C") && simpleRank > 1)
		{
			matrix[0][1] = -2;
			if(extended > 0)
				matrix[simpleRank][simpleRank-1] = -2;
			return matrix;
		}
		
		if(type.equals("D") && simpleRank > 3)
		{
			matrix[0][1] = matrix[1][0] = 0;
			matrix[0][2] = matrix[2][0] = -1;
			if(extended > 0)
			{
				matrix[simpleRank][simpleRank-1] = matrix[simpleRank-1][simpleRank] = 0;
				matrix[simpleRank][simpleRank-2] = matrix[simpleRank-2][simpleRank] = -1;
			}
			return matrix;
		}
		
		if(type.equals("E") && simpleRank > 5 && simpleRank < 9)
		{
			matrix[0][3] = matrix[3][0] = -1;
			matrix[0][1] = matrix[1][0] = 0;
			if(extended > 0)
			{
				if(simpleRank == 6)
				{
					matrix[0][simpleRank] = matrix[simpleRank][0] = -1;
					matrix[simpleRank][simpleRank-1] = matrix[simpleRank-1][simpleRank] = 0;
				}
				if(simpleRank == 7)
				{
					matrix[1][simpleRank] = matrix[simpleRank][1] = -1;
					matrix[simpleRank][simpleRank-1] = matrix[simpleRank-1][simpleRank] = 0;
				}
				
			}
			return matrix;
		}
		
		if(type.equals("F") && simpleRank == 4)
		{
			matrix[1][2] = -2;
			return matrix;
		}
		
		if(type.equals("G") && simpleRank == 2)
		{
			matrix[1][0] = -3;
			return matrix;
		}
		
		return null;
	}
	
	/**
	 * Determines whether two Cartan matrix are equivalent.
	 * That is, after arbitrary permutations of rows and columns,
	 * the matrices should be identical.
	 * Instead of doing the actual permutations, a couple of permutation-invariant
	 * characteristics of both matrices are calculated and compared.
	 *
	 * @param	matrix1	The first Cartan matrix to compare against the second.
	 * @param	matrix2	The second Cartan matrix to compare against the first.
	 * @return			True is the Cartan matrices are equivalent, false otherwise.
	 */
	public static boolean sameCartanMatrices(int[][] matrix1, int[][] matrix2)
	{
		if(matrix1 == null || matrix2 == null)
			return false;
		if(matrix1.length != matrix2.length)
			return false;
		
		Matrix A = new Matrix(matrix1.length,matrix1.length);
		Matrix B = new Matrix(matrix1.length,matrix1.length);
		
		// Calculate the norms (the sum of all matrix entries)
		int normA = 0;
		int normB = 0;
		// Calculate the number of nodes which have a given number of connections
		int[] nodeConnA = new int[matrix1.length];
		int[] nodeConnB = new int[matrix1.length];
		for (int i = 0; i < nodeConnB.length; i++)
			nodeConnB[i] = nodeConnA[i] = 0;
		
		// Calculate the sum of each column and sort these later on
		int[] columnSums1 = new int[matrix1.length];
		int[] columnSums2 = new int[matrix1.length];

		for (int i = 0; i < matrix1.length; i++)
		{
			int connA = 0;
			int connB = 0;
			
			columnSums1[i] = 0;
			columnSums2[i] = 0;
			
			for (int j = 0; j < matrix1.length; j++)
			{
				A.set(i,j,matrix1[i][j]);
				B.set(i,j,matrix2[i][j]);
				normA += matrix1[i][j];
				normB += matrix2[i][j];
				if(i != j)
				{
					if(matrix1[i][j] != 0)
						connA++;
					if(matrix2[i][j] != 0)
						connB++;
					columnSums1[i] += matrix1[i][j];
					columnSums2[i] += matrix2[i][j];
				}
			}
			nodeConnA[connA]++;
			nodeConnB[connB]++;
			
		}
		
		Arrays.sort(columnSums1);
		Arrays.sort(columnSums2);
		
		if(!Arrays.equals(columnSums1, columnSums2))
			return false;
		
		if(normA != normB)
			return false;
		
		for (int i = 0; i < nodeConnB.length; i++)
		{
			if(nodeConnB[i] != nodeConnA[i])
				return false;			
		}

		
		if(Math.round(A.det()) != Math.round(B.det()))
			return false;
		
		if(A.rank() != B.rank())
			return false;
		
		if(Math.round(100 * A.normF()) != Math.round(100 * B.normF()))
			return false;
		
		return true;
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
			case CDynkinConnection.TYPE_SINGLE:
				line = new Line2D.Double(begin,end);
				break;
			case CDynkinConnection.TYPE_DOUBLE:
				line = new LinesWithArrow(begin,end,2,2*radius,true);
				break;
			case CDynkinConnection.TYPE_TRIPLE:
				line = new LinesWithArrow(begin,end,3,2*radius,true);
				break;
			case CDynkinConnection.TYPE_QUADRUPLE:
				line = new LinesWithArrow(begin,end,4,2*radius,true);
				break;
			case CDynkinConnection.TYPE_SPECIAL_DOUBLE:
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
		perc = perc % 1;
		float[] newCol = {0.0f, 0.0f, 0.0f};
		if(perc < 1/3.0f)
		{
			perc = perc * 3;
			newCol[0] = (float) Math.sqrt(1 - perc);
			newCol[1] = (float) Math.sqrt(perc);
		}
		else if (perc < 2/3.0f)
		{
			perc = (perc - 1/3.0f) * 3;
			newCol[1] = (float) Math.sqrt(1 - perc);
			newCol[2] = (float) Math.sqrt(perc);
		}
		else
		{
			perc = (perc - 2/3.0f) * 3;
			newCol[2] = (float) Math.sqrt(1 - perc);
			newCol[0] = (float) Math.sqrt(perc);
		}
		return newCol;
	}
}
