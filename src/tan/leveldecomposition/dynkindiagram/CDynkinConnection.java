/*
 * CDynkinConnection.java
 *
 * Created on 9 maart 2007, 11:19
 *
 */

package tan.leveldecomposition.dynkindiagram;

import java.io.Serializable;

/**
 * A class for storing connections between Dynkin nodes.
 *
 * @see		CDynkinNode
 * @author	Teake Nutma
 */
public class CDynkinConnection implements Serializable
{
	/** The Dynkin node from which this connection points. */
	public final CDynkinNode fromNode;
	/** The Dynkin node to which this connection points. */
	public final CDynkinNode toNode;
	
	/** 
	 * Creates a new instance of CDynkinConnection
	 *
	 * @param	fromNode	The node from which this connection points.
	 * @param	toNode		The node to which this connection points.
	 */
	public CDynkinConnection(CDynkinNode fromNode, CDynkinNode toNode)
	{
		this.fromNode	= fromNode;
		this.toNode		= toNode;
	}
	
	/** 
	 * Checks if this connection is equal another connection.
	 * The are the same both the fromNode and the toNode are the same.
	 *
	 * @param	obj		The object to be compared, which has to be a connection.
	 */
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		CDynkinConnection compareConnection = (CDynkinConnection) obj;
		if(compareConnection.fromNode.equals(fromNode) && compareConnection.toNode.equals(toNode))
			return true;
		else
			return false;
	}
}
