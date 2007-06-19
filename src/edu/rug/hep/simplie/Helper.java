/*
 * Helper.java
 *
 * Created on 19-jun-2007, 9:19:50
 */

package edu.rug.hep.simplie;

import edu.rug.hep.simplie.math.*;

import Jama.Matrix;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.math.BigDecimal;

/**
 *
 * @author Teake Nutma
 */
public class Helper
{
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
	 * Returns the A_n cartan matrix of the given rank.
	 *
	 * @param	 rank	The rank of the A_n matrix to be returned.
	 * @return			The A_n cartan matrix of the given rank.
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
	 * The matrices are the same if all their values are the same.
	 *
	 * @param	matrix1	The first matrix we compare against the second.
	 * @param	matrix2	The second matrix we compare against the first.
	 * @return			True if the matrices are the same, false otherwise.
	 */
	public static boolean sameMatrices(Matrix matrix1, Matrix matrix2)
	{
		if(matrix1 == null || matrix2 == null)
			return false;
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
	 * Compares a matrix of integers against a Matrix.
	 * They are the same if the values of the Matrix cast to integers are the same
	 * as those of the integer matrix.
	 *
	 * @param	matrix1	The first matrix we compare against the second.
	 * @param	matrix2	The second matrix we compare against the first.
	 * @return			True if the matrices are the same, false otherwise.
	 */
	public static boolean sameMatrices(Matrix matrix1, int[][] matrix2)
	{
		if(matrix1 == null || matrix2 == null)
			return false;
		if(matrix1.getColumnDimension() != matrix2.length || matrix1.getColumnDimension() != matrix2.length)
			return false;
		for (int i = 0; i < matrix2.length; i++)
		{
			for (int j = 0; j < matrix2.length; j++)
			{
				if(matrix1.get(i,j) != (double) matrix2[i][j])
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Compares two integer arrays.
	 * The arrays are the same if all their are equal.
	 *
	 * @param	array1	The first array to compare against the second.
	 * @param	array2	The second array to compare against the first.
	 * @return			True if the arrays are the same, false otherwise.
	 */
	public static boolean sameArrays(int[] array1, int[] array2)
	{
		if(array1 == null || array2 == null)
			return false;
		if(array1.length != array2.length)
			return false;
		for (int i = 0; i < array1.length; i++)
		{
			if(array1[i] != array2[i])
				return false;
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
	
	public static String matrixToString(int[][] matrix, int decimalPlates)
	{
		Matrix newMatrix = new Matrix(matrix.length, matrix.length);
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix.length; j++)
			{
				newMatrix.set(i,j,matrix[i][j]);
			}
		}
		return matrixToString(newMatrix,decimalPlates);
	}
	
	
	public static String matrixToString(fraction[][] matrix, int decimalPlates)
	{
		Matrix newMatrix = new Matrix(matrix.length, matrix.length);
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix.length; j++)
			{
				if(matrix[i][j] == null)
					return "";
				newMatrix.set(i,j,matrix[i][j].asDouble());
			}
		}
		return matrixToString(newMatrix,decimalPlates);
	}
	
	/**
	 * Takes a matrix and returns it as a string,
	 * with all entries formatted to have the given decimal plates.
	 *
	 * @param	matrix			 The matrix to be formatted to a string.
	 * @param	decimalPlates	 The number of decimals each entry will have.
	 * @return					 A string representing the matrix.
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
					bd				= bd.setScale(decimalPlates,BigDecimal.ROUND_HALF_UP);
					double entry	= bd.doubleValue();
					
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
