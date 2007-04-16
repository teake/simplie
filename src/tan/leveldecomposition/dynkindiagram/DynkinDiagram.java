/*
 * DynkinDiagram.java
 *
 * Created on 8 maart 2007, 14:38
 *
 */

package tan.leveldecomposition.dynkindiagram;

import java.util.Vector;
import java.util.Iterator;
import java.util.Collections;
import java.io.*;
import Jama.Matrix;

/**
 * Singleton class
 *
 * @author Teake Nutma
 */
public class DynkinDiagram
{
	private static DynkinDiagram _instance = new DynkinDiagram();
	public static Vector<CDynkinNode>		nodes;
	
	/**
	 * Creates a new instance of DynkinDiagram
	 */
	private DynkinDiagram()
	{
		nodes = new Vector<CDynkinNode>();
	}
	
	public static DynkinDiagram getInstance()
	{
		return _instance;
	}
	
	/** Clears the Dynkin diagram */
	public static void Clear()
	{
		nodes.clear();
	}
	
	public static int rank()
	{
		return nodes.size();
	}
	
	/**
	 * Fetches a node by its external label.
	 * Returns null if the node is not found.
	 */
	private static CDynkinNode getNodeByLabel(int label)
	{
		for (CDynkinNode node : nodes)
		{
			if(node.label == label)
			{
				return node;
			}
		}
		return null;
	}
	
	public static CDynkinNode getNodeByCoor(int x, int y)
	{
		for (CDynkinNode node : nodes)
		{
			if(node.x == x && node.y == y)
			{
				return node;
			}
		}
		return null;
	}
	
	/** Translates an index of the submatrix into an index of the full matrix */
	public static int translateSubIndex(int index)
	{
		int subIndex = 0;
		for(int i = 0; i < rank(); i++)
		{
			CDynkinNode node = nodes.get(i);
			if(node.enabled)
				subIndex++;
			if(subIndex == index + 1)
				return i;
		}
		return -1; // not found
	}
	
	/** Translates a co-index into an index of the full matrix */
	public static int translateCoIndex(int index)
	{
		int subIndex = 0;
		for(int i = 0; i < rank(); i++)
		{
			CDynkinNode node = nodes.get(i);
			if(!node.enabled)
				subIndex++;
			if(subIndex == index + 1)
				return i;
		}
		return -1; // not found
	}
	
	/** Translates a disabled-index into an index of the full matrix */
	public static int translateDisIndex(int index)
	{
		int subIndex = 0;
		for(int i = 0; i < rank(); i++)
		{
			CDynkinNode node = nodes.get(i);
			if(node.isDisconnected())
				subIndex++;
			if(subIndex == index + 1)
				return i;
		}
		return -1; // not found
	}
	
	/** Returns the Cartan matrix of the whole algebra. */
	public static Matrix cartanMatrix()
	{
		refactor();
		
		/** Creates a rank x rank matrix filled with zeros. */
		Matrix cartanMatrix = new Matrix(rank(),rank());
		
		/** Set the diagonals to two. */
		for(int i = 0; i < rank(); i++)
			cartanMatrix.set(i,i,2);
		
		/** Set the off-diagonal parts. */
		for (int i = 0; i < rank(); i++)
		{
			for (int j = i + 1; j < rank(); j++)
			{
				if( getNodeByLabel(i+1).hasConnectionTo(j+1) || getNodeByLabel(j+1).hasConnectionTo(i+1) )
				{
					cartanMatrix.set(i,j,-1);
					cartanMatrix.set(j,i,-1);
				}
			}
		}
		
		return cartanMatrix;
	}
	
	/**
	 * Returns the Cartan matrix of one of the subalgebras
	 *
	 * @param	type	The name of the subalgebra to get. Either "regular", "deleted", or "disconnected".
	 * @return			The cartan matrix of the regular or deleted subalgebra.
	 */
	public static Matrix cartanSubMatrix(String type)
	{
		if( !( type == "regular" || type == "deleted" || type == "disconnected") )
			return null;
		
		int indexI;
		int indexJ;
		int subRank = 0;
		
		for (CDynkinNode node : nodes)
		{
			if((node.enabled && type == "regular")
			|| (!node.enabled && type == "deleted")
			|| (node.isDisconnected() && type == "disconnected"))
			{
				subRank++;
			}
		}
		Matrix cartanSubMatrix	= new Matrix(subRank,subRank);
		Matrix cartanMatrix	= cartanMatrix();
		
		/** Copy the Cartan matrix elements into the submatrix. */
		for(int i = 0; i < subRank; i++)
		{
			if(type == "regular")
				indexI = translateSubIndex(i);
			else if(type == "deleted")
				indexI = translateCoIndex(i);
			else
				indexI = translateDisIndex(i);
			for(int j = 0; j < subRank; j++)
			{
				if(type == "regular")
					indexJ = translateSubIndex(j);
				else if(type == "deleted")
					indexJ = translateCoIndex(j);
				else
					indexJ = translateDisIndex(j);
				cartanSubMatrix.set(i,j, cartanMatrix.get(indexI,indexJ));
			}
		}
		
		return cartanSubMatrix;
	}
	
	/** Returns the last label that was added. */
	public static int lastLabel()
	{
		int lastLabel = 0;
		for (CDynkinNode node : nodes)
		{
			if(node.label > lastLabel)
			{
				lastLabel = node.label;
			}
		}
		
		return lastLabel;
	}
	
	public static int nextFreeLabel()
	{
		return (lastLabel() + 1);
	}
	
	public static boolean addNode(int x, int y)
	{
		int newId = -1;
		
		for (CDynkinNode node : nodes)
		{
			if (newId < node.id)
			{
				newId = node.id;
			}
		}
		newId++;
		
		CDynkinNode newNode = new CDynkinNode( newId, nextFreeLabel(), x, y);
		nodes.add(newNode);
		return true;
	}
	
	public static void removeNode(CDynkinNode nodeToRemove)
	{
		nodes.remove(nodeToRemove);
		for(CDynkinNode node : nodes)
		{
			node.removeConnection(nodeToRemove);
		}
		
	}
	
	public static void modifyConnection(int fromLabel, int toLabel, boolean add) // if add == false then remove
	{
		CDynkinNode fromNode	= getNodeByLabel(fromLabel);
		CDynkinNode toNode		= getNodeByLabel(toLabel);
		
		/* Do nothing if either one of the nodes is not found, or if both are the same */
		if( fromNode == null || toNode == null || fromNode.equals(toNode) )
			return;
		
		if(add)
		{
			fromNode.addConnection(toNode);
			toNode.addConnection(fromNode);
		}
		else
		{
			fromNode.removeConnection(toNode);
			toNode.removeConnection(fromNode);
		}
	}
	
	/** Reshapes the internal structure. */
	private static void refactor()
	{
		/** sort the nodes according to their label */
		Collections.sort(nodes);
		
		/** Reset all the labels */
		int label = 1;
		for (CDynkinNode node : nodes)
		{
			node.label = label++;
		}
	}
	
	/**
	 * Saves the dynkindiagram to file.
	 * Returns true upon succes, false on failure.
	 */
	public static boolean saveTo(String filename)
	{
		filename.trim();
		FileOutputStream fos	= null;
		ObjectOutputStream out	= null;
		try
		{
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(nodes);
			out.close();
		}
		catch(IOException ex)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Loads the dynkindiagram from a file.
	 * Returns true on succes, false on failure.
	 */
	public static boolean loadFrom(String filename)
	{
		filename.trim();
		FileInputStream fis		= null;
		ObjectInputStream in	= null;
		try
		{
			fis		= new FileInputStream(filename);
			in		= new ObjectInputStream(fis);
			nodes	= (Vector<CDynkinNode>) in.readObject();
			in.close();
		}
		catch(IOException ex)
		{
			return false;
		}
		catch(ClassNotFoundException ex)
		{
			return false;
		}
		return true;
	}
	
}
