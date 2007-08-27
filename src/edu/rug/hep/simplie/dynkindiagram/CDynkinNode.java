/*
 * CDynkinNode.java
 *
 * Created on 8 maart 2007, 14:39
 *
 */

package edu.rug.hep.simplie.dynkindiagram;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * A class for storing nodes within a Dynkin diagram.
 *
 * @see		CDynkinDiagram
 * @author	Teake Nutma
 */
public class CDynkinNode implements Serializable, Comparable<CDynkinNode>
{
	/** The x-coordinate of the node in the dynkin diagram */
	public final int x;
	/** The y-coordinate of the node in the dynkin diagram */
	public final int y;
	
	/** The external label */
	private int label;
	/** Is the node enabled or not? */
	private boolean	enabled;
	/** The internal list of nodes to which this node has connections. */
	private ArrayList<CDynkinNode> connectionsTo;
	
	/**
	 * Creates a new instance of CDynkinNode.
	 * When instantiated, it is enabled.
	 *
	 * @param	x		The x-coordinate of the node in the dynkin diagram.
	 * @param	y		The y-coordinate of the node in the dynkin diagram.
	 */
	public CDynkinNode(int x, int y)
	{
		this.enabled	= true;
		this.x			= x;
		this.y			= y;
		this.label		= 1;
		
		this.connectionsTo = new ArrayList<CDynkinNode>();
	}
	
	public int getLabel()
	{
		return label;
	}

	public void setLabel(int label)
	{
		this.label = label;
	}
	
	/**
	 * Boolean indicating whether or not the node is enabled ( = not disabled or disconnected).
	 */
	public boolean isEnabled()
	{
		return enabled;
	}
	
	/** 
	 * Toggles a node from enabled <-> disabled.
	 */
	public void toggle()
	{
		enabled = !enabled;
	}
	
	/**
	 * The node is "disconnected" if it is part of the disconnected subalgebra,
	 * i.e. the subalgebra that is disabled and has no connections to enabled nodes.
	 *
	 * @return	True is the node is disconnected, false otherwise.
	 */
	public boolean isDisconnected()
	{
		if(enabled)
			return false;
		for(CDynkinNode toNode : connectionsTo)
		{
			if(toNode.enabled)
				return false;
		}
		return true;
	}
	
	/**
	 * The node is a "level node" if it is not enabled and not disconnected.
	 *
	 * @return	True is the node is a level node, false otherwise.
	 */
	public boolean isLevel()
	{
		if(!enabled && !isDisconnected())
			return true;
		else
			return false;
	}
	
	/**
	 * Adds a connection from this node to "toNode"
	 *
	 * @param	toNode	The node to which we should lay a connection
	 * @return			True if succesfull, false if the connection already existed.
	 */
	public boolean addConnection(CDynkinNode toNode)
	{
		if(connectionsTo.contains(toNode))
			return false;
		else
			connectionsTo.add(toNode);
		
		return true;
	}
	
	/**
	 * Removes a connection from this node to "toNode"
	 *
	 * @param	toNode	The node to which we should delete the connection.
	 * @return			True if succesfull, false if there was no connection.
	 */
	public boolean removeConnection(CDynkinNode toNode)
	{
		return connectionsTo.remove(toNode);
	}
	
	/**
	 * Checks if this node gas a connection to another specific node.
	 *
	 * @param toNode	The node to which this node should have connection.
	 * @return			True if the connection exists, false otherwise.
	 */
	public boolean hasConnectionTo(CDynkinNode toNode)
	{
		return connectionsTo.contains(toNode);
	}
	
	/**
	 * Compares nodes according to their position in the Dynkin diagram.
	 * The nodes are sorted according to their position in the Dynkin diagram.
	 *
	 * @param	compareNode		The node to compare this one to.
	 * @return					1 if this node comes after the other,
	 *							0 if we cannot sort it,
	 *							-1 if this nodes comes before the other.
	 */
	public int compareTo(CDynkinNode compareNode)
	{
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		
		if(this.y > compareNode.y) return AFTER;
		if(this.y < compareNode.y) return BEFORE;
		
		if(this.x > compareNode.x) return AFTER;
		if(this.x < compareNode.x) return BEFORE;
		
		return EQUAL;
	}
	
	/**
	 * Checks if nodes are equal.
	 * @return		True if the nodes are equal, and false otherwise.
	 */
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		CDynkinNode compareNode = (CDynkinNode) obj;
		if(compareNode.x == x && compareNode.y == y)
			return true;
		else
			return false;
	}
	
}
