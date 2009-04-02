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

import Jama.Matrix;
import edu.simplie.Helper;
import edu.simplie.algebra.Root;
import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Teake Nutma
 * @version $Rev$, $Date$
 */
public class CoxeterProjector extends EmptyProjector
{
	private double[] coxeterX;
	private double[] coxeterY;
	private double normCoxeterX;
	private double normCoxeterY;
	private	double minCoxDist;
	private double maxCoxDist;

	@Override
	public void clear()
	{
		super.clear();
		maxCoxDist = -Double.MAX_VALUE;
		minCoxDist = +Double.MAX_VALUE;
	}

	@Override
	public void preProject()
	{
		// Compute the Coxeter element.
		Matrix coxeterElement = new Matrix(algebras.algebra.rank,algebras.algebra.rank);
		for(int i = 0; i < algebras.subAlgebra.rank; i++)
		{
			Matrix reflection	= new Matrix(algebras.algebra.simpWeylRelfMatrix(algebras.dd.translateSub(i)));
			coxeterElement		= ( i == 0 ) ? reflection : coxeterElement.times(reflection);
		}

		// Determine the basis vectors for the Coxeter plane projection.
		// The real and imaginary parts of the wanted eigenvalue.
		double angle		= 2 * Math.PI / algebras.subAlgebra.coxeterNumber;
		double ReEigenval	= Math.cos(angle);
		double ImEigenval	= Math.sin(angle);
		double[][] complex	= Helper.complexEigenvector(coxeterElement, ReEigenval, ImEigenval);

		coxeterX = complex[0];
		coxeterY = complex[1];

		normCoxeterX = innerProduct(coxeterX, coxeterX);
		normCoxeterY = innerProduct(coxeterY, coxeterY);
		if(normCoxeterX == 0.0f) normCoxeterX = 1.0f;
		if(normCoxeterY == 0.0f) normCoxeterY = 1.0f;
	}

	@Override
	public void projectRoot(Root root)
	{
		// Don't draw imaginary roots for Coxeter projections
		if(root.norm <= 0)
			return;

		double[] pos = calcPos(root.vector);

		// Project the Weyl reflections.
		// Loop over every other root.
		for(int j = root.height(); j < algebras.algebra.rs.size(); j++)
		{
			Collection<Root> otherRoots = algebras.algebra.rs.get(j);
			for(Iterator itr = otherRoots.iterator(); itr.hasNext();)
			{
				Root otherRoot = (Root) itr.next();
				if(otherRoot.norm <= 0 || otherRoot.equals(root))
					continue;
				int sum		= root.norm + otherRoot.norm;
				int product = 2 * algebras.algebra.innerProduct(root, otherRoot);
				// The distance for thisRoot & otherRoot
				if(sum - product == 2)
				{
					double[] pos2 = calcPos(otherRoot.vector);
					addConnection(pos[0], pos[1], pos2[0], pos2[1]);
					addConnection(-pos[0], -pos[1], -pos2[0], -pos2[1]);
					continue;
				}
				// The distance for thisRoot & - otherRoot
				if(sum + product == 2)
				{
					double[] pos2 = calcPos(otherRoot.vector);
					addConnection(pos[0], pos[1], -pos2[0], -pos2[1]);
					addConnection(-pos[0], -pos[1], pos2[0], pos2[1]);
				}
			}
		}

		// Add the root
		checkMinMax(pos[0],pos[1]);
		checkMinMax(-pos[0],-pos[1]);
		nodes.add(new Node2D(pos[0],pos[1], 0, 0, 0));
		nodes.add(new Node2D(-pos[0],-pos[1], 0, 0, 0));
	}

	@Override
	public Color connectionColor(Connection2D connection)
	{
		float relDist = (float) ((connection.maxDist - maxCoxDist) / ( minCoxDist - maxCoxDist));
		float[] color = Helper.colorSpectrum(2*relDist/3);
		return (new Color(color[0],color[1],color[2], 0.7f));
	}

	private boolean addConnection(double x1, double y1, double x2, double y2)
	{
		Connection2D conn = new Connection2D(x1, y1, x2, y2, 0, 0, 0);
		if(!connections.add(conn))
		{
			return false;
		}
		else
		{	
			maxCoxDist = Math.max(maxCoxDist,conn.maxDist);
			minCoxDist = Math.min(minCoxDist,conn.maxDist);
			checkMinMax(x2, y2);
			return true;
		}
	}

	public double[] calcPos(int[] rootVector)
	{
		double[] pos = new double[2];
		pos[0] = 2 * innerProduct(rootVector, coxeterX) / normCoxeterX;
		pos[1] = 2 * innerProduct(rootVector, coxeterY) / normCoxeterY;
		return pos;
	}

	private double innerProduct(double[] v1, double[] v2)
	{
		double result = 0.0d;
		for (int i = 0; i < v1.length; i++)
		{
			for (int j = 0; j < v1.length; j++)
			{
				result += ((double) (algebras.algebra.B[i][j])) * v1[i] * v2[j];
			}
		}
		return result;
	}

	private double innerProduct(int[] v1, double[] v2)
	{
		double[] f1 = new double[v1.length];
		for(int i = 0; i < v1.length; i++)
		{
			f1[i] = (double) v1[i];
		}
		return innerProduct(f1,v2);
	}
}
