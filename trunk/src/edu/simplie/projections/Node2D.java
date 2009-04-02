/*
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

package edu.simplie.projections;

/**
 *
 * @author Teake Nutma
 * @version $Rev$, $Date$
 */
public class Node2D
{
	public final double x;
	public final double y;

	/**
	 * Creates a new 2-dimensional node.
	 *
	 * @param x	The x-coordinate.
	 * @param y	The y-coordinate.
	 */
	public Node2D(double x, double y)
	{
		double pow = Math.pow(10, 12);
		this.x = Math.round( pow * x ) / pow;
		this.y = Math.round( pow * y ) / pow;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		if(getClass() != obj.getClass())
		{
			return false;
		}
		final Node2D other = (Node2D) obj;
		if(this.x != other.x)
		{
			return false;
		}
		if(this.y != other.y)
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 41 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
		hash = 41 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
		return hash;
	}
}
