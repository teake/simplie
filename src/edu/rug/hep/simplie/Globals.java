/*
 * Globals.java
 *
 * Created on 27 maart 2007, 17:17
 *
 */

package edu.rug.hep.simplie;

import edu.rug.hep.simplie.dynkindiagram.CDynkinDiagram;
import edu.rug.hep.simplie.group.CGroup;
import Jama.Matrix;
import java.util.ArrayList;

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
	/** Global CGroup object for the disconnected disabled subgroup */
	public static CGroup disGroup;
	/** Global CGroup object for the non-level subgroup (which is equal to the direct product of subGroup x disGroup) */
	public static CGroup coGroup;
	/** Global CDynkinDiagram object */
	public static CDynkinDiagram dd;
	/** Boolean to indicate whether or not we are scanning right now. */
	public static boolean scanning;
	
	/**********************************
	 * Singleton stuff
	 **********************************/
	
	private static Globals _instance = new Globals();
	
	/** Private constructor */
	private Globals()
	{
		scanning = false;
		dd = new CDynkinDiagram();
	}
	
	/** Singleton handler. */
	public static Globals getInstance()
	{
		return _instance;
	}
	
	
	/**********************************
	 * Helper functions defined below
	 **********************************/
	
	/**
	 * Returns a string representing the type of decomposition of the full group into
	 * the regular subgroup and the disconnected subgroup.
	 *
	 * @return	String of the type "regular subgroup x disconnected subgroup representations in fullgroup".
	 * @see		#getDynkinDiagramType
	 */
	public static String getDecompositionType()
	{
		String output;
		
		output = subGroup.type;
		if(disGroup.rank != 0)
			output += " x " + disGroup.type;
		output += " representations in " + group.type;
		
		return output;
	}
	
	/**
	 * Returns a string representing the regular subgroup and the disconnected subgroup of the full group.
	 *
	 * @return	String of the type "fullgroup as regular subgroup x disconnected subgroup".
	 * @see		#getDecompositionType
	 */
	public static String getDynkinDiagramType()
	{
		String output;
		
		output = group.type;
		if(subGroup.rank != group.rank)
			output += " as " + subGroup.type;
		if(disGroup.rank != 0)
			output += " x " + disGroup.type;
		
		return output;
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
	
}
