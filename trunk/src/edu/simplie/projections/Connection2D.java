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
public class Connection2D
{
	public final double x1;
	public final double y1;
	public final double x2;
	public final double y2;
	public final double maxDist;


	/**
	 * Creates a new 2-dimensional node.
	 *
	 * @param x1	The x-coordinate.
	 * @param y1	The y-coordinate.
	 * @param x2	The x-coordinate.
	 * @param y2	The y-coordinate.
	 */
	public Connection2D(double x1, double y1, double x2, double y2)
	{
		double pow = Math.pow(10, 12);
		this.x1 = Math.round( pow * x1 ) / pow;
		this.y1 = Math.round( pow * y1 ) / pow;
		this.x2 = Math.round( pow * x2 ) / pow;
		this.y2 = Math.round( pow * y2 ) / pow;

		double dist1 = x1*x1 + y1*y1;
		double dist2 = x2*x2 + y2*y2;
		this.maxDist = Math.max(dist1, dist2);
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
		final Connection2D other = (Connection2D) obj;
		if(this.x1 == other.x1 && this.y1 == other.y1 && this.x2 == other.x2 && this.y2 == other.y2)
		{
			return true;
		}
		if(this.x1 == other.x2 && this.y1 == other.y2 && this.x2 == other.x1 && this.y2 == other.y1)
		{
			return true;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 29 * hash + (int) (Double.doubleToLongBits(this.x1 + this.x2) ^ (Double.doubleToLongBits(this.x1 + this.x2) >>> 32));
		hash = 29 * hash + (int) (Double.doubleToLongBits(this.y1 + this.y2) ^ (Double.doubleToLongBits(this.y1 + this.y2) >>> 32));
		return hash;
	}



}
