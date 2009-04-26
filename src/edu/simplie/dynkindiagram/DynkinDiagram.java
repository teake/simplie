/*
 * DynkinDiagram.java
 *
 * Created on 8 maart 2007, 14:38
 *
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

package edu.simplie.dynkindiagram;

import java.util.Vector;
import java.util.Iterator;
import java.util.Collections;
import java.io.*;


/**
 * A class representing (as of yet only simply-laced) Dynkin diagrams.
 * Any node within this diagram can be disabled in order to form regular subalgebras
 * of the whole algebra, which in turn can be used for a level decomposition.
 *
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class DynkinDiagram
{
	/** Vector containing all nodes of this diagram. */
	public Vector<DynkinNode> nodes;
	/** Vector containing all connections of this diagram */
	public Vector<DynkinConnection> connections;
	/** Vector containing all the compact pairs of this diagram */
	public Vector<CompactPair> compactPairs;
	/** The node that was added last. */
	private DynkinNode lastAddedNode;
	/** The internal list of listeners */
	private Vector<DiagramListener> listeners;
	/** Internal boolean to keep if the diagram is locked or not */
	private boolean locked;
	
	/**
	 * Creates a new instance of DynkinDiagram
	 */
	public DynkinDiagram()
	{
		locked		= false;
		nodes		= new Vector<DynkinNode>();
		connections	= new Vector<DynkinConnection>();
		compactPairs= new Vector<CompactPair>();
		lastAddedNode	= null;
		listeners	= new Vector<DiagramListener>();
	}
	
	/**
	 * Adds a listener to this diagram.
	 * 
	 * @param   listener	The listener to add.
	 */
	public void addListener(DiagramListener listener)
	{
		listeners.add(listener);
	}
	
	
	/**
	 * Locks the diagram.
	 * 
	 * @param   locked  If true, the diagram will get locked.
	 *		    If false, the diagram will get unlocked.
	 */
	public void setLocked(boolean locked)
	{
		this.locked = locked;
	}
	
	/**
	 * If the diagram is locked it cannot be changed
	 * (no nodes or connections can be added or removed).
	 * 
	 * @return  True is the diagram is locked, false otherwise.
	 */
	public boolean isLocked()
	{
		return this.locked;
	}
				
	public DynkinNode getLastAddedNode()
	{
		return lastAddedNode;
	}
	
	/** Clears the Dynkin diagram. That is, it deletes all nodes. */
	public void clear()
	{
		if(locked)
			return;
		
		nodes.clear();
		connections.clear();
		lastAddedNode = null;
		update();
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
	public DynkinNode getNodeByCoor(int x, int y)
	{
		for (DynkinNode node : nodes)
		{
			if(node.x == x && node.y == y)
			{
				return node;
			}
		}
		return null;
	}
		
	/** returns [minX, maxX, minY, maxY] */
	public int[] getDiagramBounds()
	{
		int[] bounds = new int[4];
		bounds[0] = bounds[2] = Integer.MAX_VALUE;
		bounds[1] = bounds[3] = Integer.MIN_VALUE;
		for (int i = 0; i < rank(); i++)
		{
			int x = nodes.get(i).x;
			int y = nodes.get(i).y;
			bounds[0] = Math.min(bounds[0], x);
			bounds[1] = Math.max(bounds[1], x);
			bounds[2] = Math.min(bounds[2], y);
			bounds[3] = Math.max(bounds[3], y);
		}

		return bounds;
	}
	
	/** Translates an index of the submatrix into an index of the full matrix */
	public int translateSub(int index)
	{
		int subIndex = 0;
		for(int i = 0; i < rank(); i++)
		{
			DynkinNode node = nodes.get(i);
			if(node.isEnabled())
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
			DynkinNode node = nodes.get(i);
			if(!node.isLevel())
				subIndex++;
			if(subIndex == index + 1)
				return i;
		}
		return -1; // not found
	}
	
	/** Translates a internal-index into an index of the full matrix */
	public int translateInt(int index)
	{
		int subIndex = 0;
		for(int i = 0; i < rank(); i++)
		{
			DynkinNode node = nodes.get(i);
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
			DynkinNode node = nodes.get(i);
			if(node.isLevel())
				subIndex++;
			if(subIndex == index + 1)
				return i;
		}
		return -1; // not found
	}
	
	/** Returns the Cartan matrix of the whole algebra. */
	public int[][] cartanMatrix()
	{
		// Creates a rank x rank matrix filled with zeros.
		int[][] cartanMatrix = new int[rank()][rank()];
		for (int i = 0; i < rank(); i++)
		{
			for(int j = 0; j < rank(); j++)
			{
				cartanMatrix[i][j] = (i == j) ? 2 : 0;
			}
		}
		
		// Set the off-diagonal parts.
		for (DynkinConnection connection : connections)
		{
			int i		= connection.fromNode.getLabel() - 1;
			int j		= connection.toNode.getLabel() - 1;
			switch(connection.type)
			{
			case DynkinConnection.TYPE_SINGLE:
				cartanMatrix[i][j] = -1;
				cartanMatrix[j][i] = -1;
				break;
			case DynkinConnection.TYPE_DOUBLE:
				cartanMatrix[i][j] = -2;
				cartanMatrix[j][i] = -1;
				break;
			case DynkinConnection.TYPE_TRIPLE:
				cartanMatrix[i][j] = -3;
				cartanMatrix[j][i] = -1;
				break;
			case DynkinConnection.TYPE_QUADRUPLE:
				cartanMatrix[i][j] = -4;
				cartanMatrix[j][i] = -1;
				break;
			case DynkinConnection.TYPE_SPECIAL_DOUBLE:
				cartanMatrix[i][j] = -2;
				cartanMatrix[j][i] = -2;
				break;
			default:
				break;
			}
		}
		
		return cartanMatrix;
	}
	
	/**
	 * Returns the Cartan matrix of one of the subalgebras.
	 *
	 * @param	type	The name of the subalgebra to get. Either "sub", "int", or "co".
	 * @return			The cartan matrix of the regular ("sub"),
	 *					the internal ("int"), or the sub x dis ("co") subalgebra.
	 */
	public int[][] cartanSubMatrix(String type)
	{
		if( !( type.equals("sub") || type.equals("int") || type.equals("co")) )
			return null;
		
		int indexI;
		int indexJ;
		int subRank = 0;
		
		for (DynkinNode node : nodes)
		{
			if((node.isEnabled() && type.equals("sub"))
					|| (node.isDisconnected() && type.equals("int"))
					|| (!node.isLevel() && type.equals("co")) )
			{
				subRank++;
			}
		}
		int[][] cartanSubMatrix	= new int[subRank][subRank];
		int[][] cartanMatrix	= cartanMatrix();
		
		// Copy the Cartan matrix elements into the submatrix.
		for(int i = 0; i < subRank; i++)
		{
			if(type.equals("sub"))
				indexI = translateSub(i);
			else if(type.equals("co"))
				indexI = translateCo(i);
			else
				indexI = translateInt(i);
			for(int j = 0; j < subRank; j++)
			{
				if(type.equals("sub"))
					indexJ = translateSub(j);
				else if(type.equals("co"))
					indexJ = translateCo(j);
				else
					indexJ = translateInt(j);
				cartanSubMatrix[i][j] = cartanMatrix[indexI][indexJ];
			}
		}
		
		return cartanSubMatrix;
	}
	
	/**
	 * Adds a node to the diagram on the specified coordinates.
	 *
	 * @param	x					The x-coordinate of the node in the diagram.
	 * @param	y					The y-coordinate of the node in the diagram.
	 * @param	connectionToLast	Integer indicating whether or not a connection should be made
	 *								from this node to the last one added. If it equals zero, then
	 *								a connection won't be made. If it is bigger than zero it represents
	 *								the lacing of the connection.
	 * @return						A string representing the action taken.
	 */
	public String addNode(int x, int y, int connectionToLast)
	{
		if(locked)
			return "Diagram is locked.";
		
		DynkinNode newNode = new DynkinNode(x, y);
		
		if(nodes.contains(newNode))
		{
			return "Cannot add node that is already present.";
		}
		else
		{
			nodes.add(newNode);
			if(connectionToLast > 0 && lastAddedNode != null)
			{
				modifyConnection(lastAddedNode, newNode, connectionToLast, true);
			}
			lastAddedNode = newNode;
			update();
			return "Node added.";
		}
	}
	 
	public String toggleCompactNode(DynkinNode node)
	{
		if(locked)
			return "Diagram is locked.";
		
		if(node != null && nodes.contains(node))
		{
			if(node.toggleCompact())
			{
				update();
				return "Node toggled.";
			}
			else
				return "Node not toggled.";
		}
		return "No node to toggle.";
	}
	
	
	/**
	 * Toggles a certain node and returns a 
	 * string containing info on the action performed.
	 * If the node was enabled, it will be disabled, and vice-versa.
	 * 
	 * @param   node    The node to be toggled.
	 * @return	    String containing info on the action taken.
	 */
	public String toggleNode(DynkinNode node)
	{
		if(locked)
			return "Diagram is locked.";
		
		if(node != null && nodes.contains(node))
		{
			node.toggle();
			update();
			return "Node toggled.";
		}
		return "No node to toggle.";
	}
	
	public String setNodeState(DynkinNode node, int state)
	{
		if(locked)
			return "Diagram is locked.";
		
		if(node != null && nodes.contains(node))
		{
			node.setState(state);
			update();
			return "Node state set.";
		}
		return "No node state set.";
	}
	
	/** Removes a node from the diagram. */
	public String removeNode(DynkinNode nodeToRemove)
	{
		Iterator it;
		
		if(locked)
			return "Diagram is locked.";
		
		int prevRank = rank();
		// Remove the node.
		if(lastAddedNode != null && lastAddedNode.equals(nodeToRemove))
		{
			lastAddedNode = null;
		}
		nodes.remove(nodeToRemove);
		for(DynkinNode node : nodes)
		{
			node.removeConnection(nodeToRemove);
		}
		// Remove any connections to it.
		it = connections.iterator();
		while(it.hasNext())
		{
			DynkinConnection connection = (DynkinConnection) it.next();
			if(connection.fromNode.equals(nodeToRemove)
					|| connection.toNode.equals(nodeToRemove) )
			{
				it.remove();
			}
		}
		// Remove the compact pair it was part of (if any).
		it = compactPairs.iterator();
		while(it.hasNext())
		{
			CompactPair pair = (CompactPair) it.next();
			if(pair.node1.equals(nodeToRemove))
			{
				pair.node2.setCompactPartner(null);
				it.remove();
			}
			if(pair.node2.equals(nodeToRemove))
			{
				pair.node1.setCompactPartner(null);
				it.remove();
			}

		}
		if(prevRank > rank())
		{
			update();
			return "Node removed.";
		}
		else
			return "No node to remove.";
	}
	
	/**
	 * Adds or removes a connection.
	 *
	 * @param fromNode		The node from which the connection points.
	 * @param toNode		The node to which the connection points.
	 * @param add			True: add the connection. False: remove the connection.
	 */
	public String modifyConnection(DynkinNode fromNode, DynkinNode toNode, int laced, boolean add)
	{
		if(locked)
			return "Diagram is locked.";
		
		String action = (add) ? "added" : "removed";
		// Do nothing if either one of the nodes is not found, or if both are the same.
		if( fromNode == null || toNode == null || fromNode.equals(toNode) )
			return "No connection " + action + ", begin and / or end point incorrect.";
		
		DynkinConnection connection = new DynkinConnection(fromNode, toNode, laced);
		
		if(add)
		{
			if(connections.contains(connection))
				return "Connection already exists.";
			connections.add(connection);
			fromNode.addConnection(toNode);
			toNode.addConnection(fromNode);
		}
		else
		{
			connections.remove(connection);
			fromNode.removeConnection(toNode);
			toNode.removeConnection(fromNode);
		}
		
		update();
		return "Connection " + action + ".";
	}

	public String modifyCompactPair(DynkinNode n1, DynkinNode n2, boolean add)
	{
		if(locked)
			return "Diagram is locked.";
		
		String action = (add) ? "added" : "removed";
		// Do nothing if either one of the nodes is not found, or if both are the same.
		if( n1 == null || n2 == null || n1.equals(n2) )
			return "No compact pair " + action + ", begin and / or end point incorrect.";
		
		CompactPair pair = new CompactPair(n1, n2);
		
		if(add)
		{
			if(compactPairs.contains(pair))
				return "No compact pair added.";
			compactPairs.add(pair);
			n1.setCompactPartner(n2);
			n2.setCompactPartner(n1);
		}
		else
		{
			compactPairs.remove(pair);
			n1.setCompactPartner(null);
			n2.setCompactPartner(null);
		}
		
		update();
		return "Compact pair " + action + ".";
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
			out.writeObject(connections);
			out.writeObject(compactPairs);
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
	@SuppressWarnings("unchecked")
	public boolean loadFrom(String filename)
	{
		if(locked)
			return false;
		
		filename.trim();
		FileInputStream fis		= null;
		ObjectInputStream in	= null;
		try
		{
			fis		= new FileInputStream(filename);
			in		= new ObjectInputStream(fis);
			nodes	= (Vector<DynkinNode>) in.readObject();
			connections = (Vector<DynkinConnection>) in.readObject();
			compactPairs = (Vector<CompactPair>) in.readObject();
			lastAddedNode = null;
			in.close();
			update();
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
		
	private void update()
	{
		Collections.sort(nodes);
		for(int i = 0; i < rank(); i++)
		{
			nodes.get(i).setLabel(i+1);
		}
		for(DiagramListener listener : listeners)
		{
			listener.diagramChanged();
		}
	}
}
