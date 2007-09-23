/*
 * CDynkinConnection.java
 *
 * Created on 9 maart 2007, 11:19
 *
 */

package edu.rug.hep.simplie.dynkindiagram;

import java.io.Serializable;

/**
 * A class for storing connections between Dynkin nodes.
 *
 * @see		CDynkinNode
 * @author	Teake Nutma
 */
public class CDynkinConnection implements Serializable
{
	/** The constants for the type of connections */
	
	/** No connection, shouldn't be used in principle */
	public static final int TYPE_NULL		= 0;
	/** Simply laced connection */
	public static final int TYPE_SINGLE		= 1;
	/** Doubly laced connection */
	public static final int TYPE_DOUBLE		= 2;
	/** Triply laced connection */
	public static final int TYPE_TRIPLE		= 3;
	/** Quadruply laced connection */
	public static final int TYPE_QUADRUPLE	= 4;
	/** A double unoriented connection (used in A_1+). */
	public static final int TYPE_SPECIAL_DOUBLE = -2;
	
	/** The Dynkin node from which this connection points. */
	public final CDynkinNode fromNode;
	/** The Dynkin node to which this connection points. */
	public final CDynkinNode toNode;
	/** The number of lines */
	public final int type;
	
	/** 
	 * Creates a new instance of CDynkinConnection
	 *
	 * @param	fromNode	The node from which this connection points.
	 * @param	toNode		The node to which this connection points.
	 */
	public CDynkinConnection(CDynkinNode fromNode, CDynkinNode toNode,int type)
	{
		this.fromNode	= fromNode;
		this.toNode		= toNode;
		this.type		= type;
	}
	
	/** 
	 * Checks if this connection is equal to another connection.
	 * The are the same both the fromNode and the toNode are the same.
	 *
	 * @param	obj		The object to be compared, which has to be a connection.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		CDynkinConnection compareConnection = (CDynkinConnection) obj;
		if((compareConnection.fromNode.equals(fromNode) && compareConnection.toNode.equals(toNode))
		|| (compareConnection.toNode.equals(fromNode) && compareConnection.fromNode.equals(toNode)) )
			return true;
		else
			return false;
	}
	
	/** Represents to connection as a string */
	@Override
	public String toString()
	{
		return "Connection from " + fromNode.getLabel() + " to " + toNode.getLabel() + ", " + type + " time(s) laced.";
	}
}
