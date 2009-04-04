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

import edu.simplie.algebra.Root;

/**
 *
 * @author Teake Nutma
 * @version $Rev$, $Date$
 */
public class HasseProjector extends EmptyProjector
{
	private double[] hasseBasis;

	@Override
	public void preProject()
	{
		// Determine the basis vectors for the Hasse diagram projection.
		hasseBasis = new double[algebras.algebra.rank];
		if(algebras.algebra.rank == 1)
		{
			hasseBasis[0] = 0.0;
			return;
		}
		for (int i = 0; i < algebras.algebra.rank; i++)
		{
			hasseBasis[i] = (double) i * 2 / (algebras.algebra.rank - 1) - 1;
		}
	}

	@Override
	public void projectRoot(Root root)
	{
		// Don't draw imaginary roots for Coxeter projections
		if(root.norm <= 0)
			return;

		double[] pos = calcPos(root.vector);
		maxCoorX = Math.max(maxCoorX,pos[0]);
		maxCoorY = Math.max(maxCoorY,pos[1]);
		minCoorX = Math.min(minCoorX,pos[0]);
		minCoorY = Math.min(minCoorY,pos[1]);

		// Add the root
		if(drawNodes)
			nodes.add(new Node2D(pos));

		if(drawConnections)
		{
			int[] dynkinLabels = algebras.algebra.rootToWeight(root.vector);
			for(int k = 0; k < root.vector.length; k++)
			{
				// Only draw reflections downward.
				if(dynkinLabels[k] <= 0 || root.height() == 1)
					continue;
				int[] reflVector = root.vector.clone();
				reflVector[k] -= dynkinLabels[k];
				// And add the connection.
				addConnection(k,new Connection2D(pos, calcPos(reflVector)));
			}
		}
	}

	private double[] calcPos(int[] rootVector)
	{
		double[] pos = {0.0f, 0.0f};
		for (int i = 0; i < rootVector.length; i++)
		{
			pos[0] += rootVector[i] * hasseBasis[i];
			pos[1] += rootVector[i];
		}
		return pos;
	}
}
