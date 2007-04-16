/*
 * CDynkinNode.java
 *
 * Created on 8 maart 2007, 14:39
 *
 */

package tan.leveldecomposition.dynkindiagram;

import java.util.ArrayList;
import java.io.Serializable;

/**
 *
 * @author Teake Nutma
 */
public class CDynkinNode implements Serializable, Comparable<CDynkinNode>
{
	/** The internal index of the node */
	public final int id;
	/** The x-coordinate of the node in the dynkin diagram */
	public final int x;
	/** The y-coordinate of the node in the dynkin diagram */
	public final int y;
	/** The label of the node in the dynkin diagram */
	public int		label;
	/** Is the node enabled or not? */
	public boolean	enabled;
	
	/** The internal list of connections the node has. */
	private ArrayList<CDynkinConnection> connections;
	
	/** Creates a new instance of CDynkinNode */
	public CDynkinNode(int id, int label, int x, int y)
	{
		this.id			= id;
		this.label		= label;
		this.enabled	= true;
		this.x			= x;
		this.y			= y;
		
		this.connections = new ArrayList<CDynkinConnection>();
	}
	
	/** 
	 * The node is "disconnected" if it is part of the disconnected subalgebra,
	 * i.e. the subalgebra that is disabled and has no connections to enabled nodes.
	 */
	public boolean isDisconnected()
	{
		if(enabled)
			return false;
		for(CDynkinConnection conn : connections)
		{
			if(conn.toNode.enabled)
				return false;
		}
		return true;
	}
	
	/** 
	 * Adds a connection from this node to "toNode"
	 *
	 * @param	toNode	The node to which we should lay a connection
	 * @return			True if succesfull, false if the connection already existed.
	 */
	public boolean addConnection(CDynkinNode toNode)
	{
		CDynkinConnection newConn = new CDynkinConnection(this,toNode);
		if(connections.contains(newConn))
			return false;
		else
			connections.add(newConn);
		
		return true;
	}
	
	/** 
	 * Removes a connections from this node to "toNode"
	 *
	 * @param	toNode	The node to which we should delete the connection.
	 * @return			True if succesfull, false if there was no connection.		
	 */
	public boolean removeConnection(CDynkinNode toNode)
	{
		CDynkinConnection oldConn = new CDynkinConnection(this,toNode);
		return connections.remove(oldConn);
	}
	
	/** The number of connections this node has. */
	public int numConnections()
	{
		return connections.size();
	}
	
	/** Returns the index'th connection of this node. */
	public CDynkinConnection getConnection(int index)
	{
		//TODO: perhaps return a clone.
		return connections.get(index);
	}
	
	/** Has this node a connection to the node with label "toLabel"? */
	public boolean hasConnectionTo(int toLabel)
	{
		for(CDynkinConnection connection : connections)
		{
			if(connection.toNode.label == toLabel)
				return true;
		}
		return false;
	}
	
	/** Compares nodes according to their position in the Dynkin diagram */
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
	
	/** Checks if connections are equal. */
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		CDynkinNode compareNode = (CDynkinNode) obj;
		if(compareNode.id == id && compareNode.x == x && compareNode.y == y)
			return true;
		else
			return false;
	}
	
}
