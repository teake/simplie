/*
 * CDynkinDiagram.java
 *
 * Created on 8 maart 2007, 14:38
 *
 */

package tan.leveldecomposition.dynkindiagram;

import tan.leveldecomposition.*;

import java.util.Vector;
import java.util.Iterator;
import java.util.Collections;
import java.io.*;
import Jama.Matrix;

/**
 * A class representing (as of yet only simply-laced) Dynkin diagrams.
 * Any node within this diagram can be disabled in order to form regular subalgebras
 * of the whole algebra, which in turn can be used for a level decomposition.
 *
 * @author Teake Nutma
 */
public class CDynkinDiagram
{
	/** Vector containing all nodes of this diagram. */
	public Vector<CDynkinNode> nodes;
	
	/**
	 * Creates a new instance of CDynkinDiagram
	 */
	public CDynkinDiagram()
	{
		nodes = new Vector<CDynkinNode>();
	}
	
	/** Clears the Dynkin diagram. That is, it deletes all nodes. */
	public void clear()
	{
		nodes.clear();
	}
	
	/** Returns the rank algebra associated to this diagram, i.e. the number of nodes. */
	public int rank()
	{
		return nodes.size();
	}
	
	/**
	 * Fetches a nodes by its coordinates.
	 * Returns null if the nodes is not found.
	 *
	 * @param	x	The x-coordinate of the node in the diagram.
	 * @param	y	The y-coordinate of the node in the diagram.
	 * @return		The node itself.
	 */
	public CDynkinNode getNodeByCoor(int x, int y)
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
	public int translateSub(int index)
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
	
	/** Translates a non-level index into an index of the full matrix */
	public int translateCo(int index)
	{
		int subIndex = 0;
		for(int i = 0; i < rank(); i++)
		{
			CDynkinNode node = nodes.get(i);
			if(!node.isLevel())
				subIndex++;
			if(subIndex == index + 1)
				return i;
		}
		return -1; // not found
	}
	
	/** Translates a disconnected-index into an index of the full matrix */
	public int translateDis(int index)
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
	
	/** Translates a level-index to an index of the full matrix */
	public int translateLevel(int index)
	{
		int subIndex = 0;
		for(int i = 0; i < rank(); i++)
		{
			CDynkinNode node = nodes.get(i);
			if(node.isLevel())
				subIndex++;
			if(subIndex == index + 1)
				return i;
		}
		return -1; // not found
	}
	
	/** Returns the Cartan matrix of the whole algebra. */
	public Matrix cartanMatrix()
	{
		Collections.sort(nodes);
		
		/** Creates a rank x rank matrix filled with zeros. */
		Matrix cartanMatrix = new Matrix(rank(),rank());
		
		/** Set the diagonals to two. */
		for(int i = 0; i < rank(); i++)
			cartanMatrix.set(i,i,2);
		
		/** Set the off-diagonal parts. */
		for (int i = 0; i < rank(); i++)
		{
			CDynkinNode nodeI = nodes.get(i);
			for (int j = i + 1; j < rank(); j++)
			{
				CDynkinNode nodeJ = nodes.get(j);
				if( nodeI.hasConnectionTo(nodeJ) || nodeJ.hasConnectionTo(nodeI) )
				{
					cartanMatrix.set(i,j,-1);
					cartanMatrix.set(j,i,-1);
				}
			}
		}
		
		return cartanMatrix;
	}
	
	/**
	 * Returns the Cartan matrix of one of the subalgebras.
	 *
	 * @param	type	The name of the subalgebra to get. Either "sub", "dis", or "co".
	 * @return			The cartan matrix of the regular ("sub"),
	 *					the disabled disconnect ("dis"), or the sub x dis ("co") subalgebra.
	 */
	public Matrix cartanSubMatrix(String type)
	{
		if( !( type == "sub" || type == "dis" || type == "co") )
			return null;
		
		int indexI;
		int indexJ;
		int subRank = 0;
		
		for (CDynkinNode node : nodes)
		{
			if((node.enabled && type == "sub")
			|| (node.isDisconnected() && type == "dis")
			|| (!node.isLevel() && type == "co") )
			{
				subRank++;
			}
		}
		Matrix cartanSubMatrix	= new Matrix(subRank,subRank);
		Matrix cartanMatrix	= cartanMatrix();
		
		/** Copy the Cartan matrix elements into the submatrix. */
		for(int i = 0; i < subRank; i++)
		{
			if(type == "sub")
				indexI = translateSub(i);
			else if(type == "co")
				indexI = translateCo(i);
			else
				indexI = translateDis(i);
			for(int j = 0; j < subRank; j++)
			{
				if(type == "sub")
					indexJ = translateSub(j);
				else if(type == "co")
					indexJ = translateCo(j);
				else
					indexJ = translateDis(j);
				cartanSubMatrix.set(i,j, cartanMatrix.get(indexI,indexJ));
			}
		}
		
		return cartanSubMatrix;
	}
	
	/**
	 * Adds a node to the diagram on the specified coordinates.
	 *
	 * @param	x					The x-coordinate of the node in the diagram.
	 * @param	y					The y-coordinate of the node in the diagram.
	 * @param	connectionToLast	Boolean to indicate whether or not a connection should be made
	 *								from this node to the last one added.
	 * @return						The node that was added, null if no node was added.
	 */
	public CDynkinNode addNode(int x, int y, boolean connectionToLast)
	{
		CDynkinNode prevNode;
		CDynkinNode newNode = new CDynkinNode(x, y);
		
		if(nodes.contains(newNode))
		{
			return null;
		}
		else
		{
			if(connectionToLast && rank() > 0)
			{
				prevNode = nodes.get(rank() - 1);
				modifyConnection(prevNode, newNode, true);
			}
			nodes.add(newNode);
			return newNode;
		}
	}
	
	/** Removes a node from the diagram. */
	public void removeNode(CDynkinNode nodeToRemove)
	{
		nodes.remove(nodeToRemove);
		for(CDynkinNode node : nodes)
		{
			node.removeConnection(nodeToRemove);
		}
		
	}
	
	/**
	 * Adds or removes a connection.
	 *
	 * @param fromNode		The node from which the connection points.
	 * @param toNode		The node to which the connection points.
	 * @param add			True: add the connection. False: remove the connection.
	 */
	public void modifyConnection(CDynkinNode fromNode, CDynkinNode toNode, boolean add)
	{
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
	
	/**
	 * Saves the dynkindiagram to file.
	 * Returns true upon succes, false on failure.
	 */
	public boolean saveTo(String filename)
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
	public boolean loadFrom(String filename)
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
	
	/** Returns a string of LaTeX representing the diagram visually. */
	public String toTeX(boolean includeCaption)
	{
		if(rank() == 0)
		{
			return "";
		}
		
		/** First determine the min and max values of x and y */
		int xMin = Integer.MAX_VALUE;
		int yMin = Integer.MAX_VALUE;
		int xMax = 0;
		int yMax = 0;
		
		for(CDynkinNode node : nodes)
		{
			xMin = Math.min(node.x,xMin);
			yMin = Math.min(node.y,yMin);
			xMax = Math.max(node.x,xMax);
			yMax = Math.max(node.y,yMax);
		}
		
		String output = new String();
		
		/** The header */
		output += "\\begin{figure}\n";
		output += "\\begin{center}\n";
		output += "\\begin{pspicture}(" + xMin + "," + yMin + ")(" + xMax + "," + yMax + ")\n";
		
		/** The nodes and connections */
		for(int i = 0; i < rank(); i++)
		{
			CDynkinNode	node	= nodes.get(i);
			int			labelI	= i + 1;
			
			output += "\\cnode";
			if(node.isDisconnected())
				output += "[fillstyle=solid,fillcolor=lightgray]";
			if(node.isLevel())
				output += "[fillstyle=solid,fillcolor=black]";
			output += "(" + node.x + "," + (yMax - node.y) + "){0.15}{N" + labelI + "} \n";
			output += "\\nput{-60}{N" + labelI + "}{" + labelI + "}\n";
			for (int j = 0; j < node.numConnections(); j++)
			{
				int labelJ = nodes.indexOf(node.getConnection(j).toNode);
				if(labelI != labelJ)
					output += "\\ncline{-}{N" + labelI + "}{N" + labelJ + "}\n";
			}
		}
		
		
		/** The footer */
		output += "\\end{pspicture}\n";
		output += "\\end{center}\n";
		if(includeCaption)
			output += "\\caption{Dynkin diagram of " + Globals.getDynkinDiagramType() + "}\n";
		output += "\\end{figure}\n";
		
		return output;
	}
	
}
