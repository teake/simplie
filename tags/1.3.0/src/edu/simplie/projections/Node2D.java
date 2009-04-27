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
 * A class for storing projected roots.
 *
 * @author Teake Nutma
 * @version $Rev$, $Date$
 */
public class Node2D
{
	public final double x;
	public final double y;

	public final double roundX;
	public final double roundY;

	/**
	 * Creates a new 2-dimensional node.
	 *
	 * @param x	The x-coordinate.
	 * @param y	The y-coordinate.
	 */
	public Node2D(double x, double y)
	{
		double pow = Math.pow(10, 10);

		this.x = x;
		this.y = y;
		this.roundX = Math.round( pow * x ) / pow;
		this.roundY = Math.round( pow * y ) / pow;
	}

	/**
	 * Convenience constructor.
	 * @param vector	First entry is the x component, the second the y component.
	 */
	public Node2D(double[] vector)
	{
		this(vector[0],vector[1]);
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
		if(this.roundX != other.roundX)
		{
			return false;
		}
		if(this.roundY != other.roundY)
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 41 * hash + (int) (Double.doubleToLongBits(this.roundX) ^ (Double.doubleToLongBits(this.roundX) >>> 32));
		hash = 41 * hash + (int) (Double.doubleToLongBits(this.roundY) ^ (Double.doubleToLongBits(this.roundY) >>> 32));
		return hash;
	}
}
