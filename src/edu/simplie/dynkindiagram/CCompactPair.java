/*
 * CCompactPair.java
 *
 * Created on 9-nov-2007, 15:02:40
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
 *
 * @author Teake Nutma
 */
public class CCompactPair implements Serializable
{
	/** First node in the compact pair. */
	public final CDynkinNode node1;
	/** Second node in the compact pair. */
	public final CDynkinNode node2;
	
	public CCompactPair(CDynkinNode node1, CDynkinNode node2)
	{
		this.node1 = node1;
		this.node2 = node2;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		CCompactPair comparePair = (CCompactPair) obj;
		if(comparePair.node1.equals(node1) 
				|| comparePair.node2.equals(node2) 
				|| comparePair.node2.equals(node1))
			return true;
		else
			return false;
	}

	/** Represents this compact pair as a string */
	@Override
	public String toString()
	{
		return "Compact pair consisting of node " + node1.getLabel() + " and node " + node2.getLabel() + ".";
	}
}
