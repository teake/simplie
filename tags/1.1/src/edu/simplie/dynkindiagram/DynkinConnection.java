/*
 * DynkinConnection.java
 *
 * Created on 9 maart 2007, 11:19
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

import java.io.Serializable;

/**
 * A class for storing connections between Dynkin nodes.
 *
 * @see		DynkinNode
 * @author	Teake Nutma
 * @version $Revision$, $Date$
 */
public class DynkinConnection implements Serializable
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
	public final DynkinNode fromNode;
	/** The Dynkin node to which this connection points. */
	public final DynkinNode toNode;
	/** The number of lines */
	public final int type;
	
	/** 
	 * Creates a new instance of DynkinConnection
	 *
	 * @param	fromNode	The node from which this connection points.
	 * @param	toNode		The node to which this connection points.
	 */
	public DynkinConnection(DynkinNode fromNode, DynkinNode toNode,int type)
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
		DynkinConnection compareConnection = (DynkinConnection) obj;
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
