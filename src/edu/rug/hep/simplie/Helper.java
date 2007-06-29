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
		
		if(type == "A")
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
		
		
		if(type == "B" && simpleRank > 1)
		{
			matrix[1][0] = -2;
			if(extended > 0)
			{
				matrix[simpleRank][simpleRank-1] = matrix[simpleRank-1][simpleRank] = 0;
				matrix[simpleRank][simpleRank-2] = matrix[simpleRank-2][simpleRank] = -1;
			}
			return matrix;
		}
		
		if(type == "C" && simpleRank > 2)
		{
			matrix[0][1] = -2;
			if(extended > 0)
				matrix[simpleRank][simpleRank-1] = -2;
			return matrix;
		}
		
		if(type == "D" && simpleRank > 3)
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
		
		if(type == "E" && simpleRank > 5 && simpleRank < 9)
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
		
		if(type == "F" && simpleRank == 4)
		{
			matrix[1][2] = -2;
			return matrix;
		}
		
		if(type == "G" && simpleRank == 2)
		{
			matrix[0][1] = -3;
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
			nodeConnB[i] = nodeConnA[i] =0;

		for (int i = 0; i < matrix1.length; i++)
		{
			int connA = 0;
			int connB = 0;
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
				}
			}
			nodeConnA[connA]++;
			nodeConnB[connB]++;;
		}
		
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
