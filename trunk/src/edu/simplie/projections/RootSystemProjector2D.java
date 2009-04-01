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
import edu.simplie.AlgebraComposite;
import edu.simplie.Helper;
import edu.simplie.algebra.Root;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Teake Nutma
 * @version $Rev$, $Date$
 */
public class RootSystemProjector2D
{
	public static final int COXETER_MODE	= 1;
	public static final int HASSE_MODE		= 2;

	private AlgebraComposite algebras;

	double[] hasseBasis;
	double[] coxeterX;
	double[] coxeterY;
	double normCoxeterX;
	double normCoxeterY;
	double maxCoorX;
	double maxCoorY;
	double minCoorX;
	double minCoorY;
	double minCoxDist;
	double maxCoxDist;
	
	private HashSet<Connection2D> connections;
	private HashSet<Node2D> nodes;

	private int mode;
	private double offX;
	private double offY;
	private double scale;
	private double radius = 6.0d;

	public RootSystemProjector2D()
	{
		connections = new HashSet<Connection2D>();
		nodes = new HashSet<Node2D>();
	}

	public void setAlgebrasComposite(AlgebraComposite algebras)
	{
		this.algebras = algebras;
	}

	public void draw(Graphics2D g2, Rectangle bounds)
	{
		offX = bounds.getWidth() / 2;
		offY = bounds.getHeight() / 2;

		double scaleX = 0.9 * bounds.getWidth() / ( maxCoorX - minCoorX );
		double scaleY = 0.9 * bounds.getHeight() / ( maxCoorY - minCoorY );

		scale = Math.min(scaleX,scaleY);

		// Draw the connections
		g2.setStroke(new BasicStroke(0.5f));
		for(Iterator it = connections.iterator(); it.hasNext();)
		{
			Connection2D conn = (Connection2D) it.next();
			double[] pos1 = transformCoor(conn.x1, conn.y1);
			double[] pos2 = transformCoor(conn.x2, conn.y2);
			float relDist = (float) ((conn.maxDist - maxCoxDist) / ( minCoxDist - maxCoxDist));
			float[] color = Helper.colorSpectrum(2*relDist/3);
			g2.setColor(new Color(color[0],color[1],color[2], 0.7f));
			g2.draw((new Line2D.Double(pos1[0], pos1[1], pos2[0], pos2[1])));
		}

		// Draw the nodes
		g2.setStroke(new BasicStroke(1.0f));
		g2.setColor(Color.BLACK);
		for(Iterator it = nodes.iterator(); it.hasNext();)
		{
			Node2D node		= (Node2D) it.next();
			double[] pos	= transformCoor(node.x, node.y);
			g2.draw(new Ellipse2D.Double(pos[0]-radius/2,pos[1]-radius/2,radius,radius));
		}
	}

	public void setMode(int mode)
	{
		this.mode = mode;
	}

	private double[] transformCoor(double x, double y)
	{
		double[] newCoor = new double[2];
		newCoor[0] =  scale * ( x - (maxCoorX + minCoorX)/2 ) + offX;
		newCoor[1] = -scale * ( y - (maxCoorY + minCoorY)/2 ) + offY;
		return newCoor;
	}

	public void project(int maxHeight)
	{
		// Reset stuff
		connections.clear();
		nodes.clear();

		maxCoorX = maxCoorY = maxCoxDist = -Double.MAX_VALUE;
		minCoorX = minCoorY = minCoxDist = +Double.MAX_VALUE;

		// Don't do anything if the algebra is empty.
		if(algebras.algebra == null || algebras.algebra.rank == 0)
			return;

		// First construct the root system up to the wanted height.
		algebras.algebra.rs.construct(maxHeight);
		if(maxHeight == 0)
			maxHeight = algebras.algebra.rs.size();

		// Compute the Coxeter element.
		Matrix coxeterElement = new Matrix(algebras.algebra.rank,algebras.algebra.rank);
		for(int i = 0; i < algebras.subAlgebra.rank; i++)
		{
			Matrix reflection	= new Matrix(algebras.algebra.simpWeylRelfMatrix(algebras.dd.translateSub(i)));
			coxeterElement		= ( i == 0 ) ? reflection : coxeterElement.times(reflection);
		}

		// Get the projection vectors.
		if(mode == HASSE_MODE)
		{
			// Determine the basis vectors for the Hasse diagram projection.
			hasseBasis = new double[algebras.algebra.rank];
			for (int i = 0; i < algebras.algebra.rank; i++)
			{
				hasseBasis[i] = (double) i * 2 / (algebras.algebra.rank - 1) - 1;
			}
		}
		if(mode == COXETER_MODE)
		{
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

		// Loop over every root.
		for(int i = 1; i < maxHeight + 1 && i < algebras.algebra.rs.size(); i++)
		{
			Collection<Root> roots = algebras.algebra.rs.get(i);
			for(Iterator it = roots.iterator(); it.hasNext();)
			{
				Root	root	= (Root) it.next();
				double[] pos	= calcPos(root.vector, mode);

				checkMinMax(pos[0],pos[1]);

				// Project the Weyl reflections.
				if(mode == COXETER_MODE)
				{
					// Loop over every other root.
					for(int j = i; j < algebras.algebra.rs.size(); j++)
					{
						Collection<Root> otherRoots = algebras.algebra.rs.get(j);
						for(Iterator itr = otherRoots.iterator(); itr.hasNext();)
						{
							Root otherRoot = (Root) itr.next();
							if(otherRoot.equals(root))
								continue;
							int sum		= root.norm + otherRoot.norm;
							int product = 2 * algebras.algebra.innerProduct(root, otherRoot);
							// The distance for thisRoot & otherRoot
							if(sum - product == 2)
							{
								double[] pos2 = calcPos(otherRoot.vector, mode);
								connections.add(new Connection2D(pos[0], pos[1], pos2[0], pos2[1], 0, 0, 0));
								connections.add(new Connection2D(-pos[0], -pos[1], -pos2[0], -pos2[1], 0, 0, 0));
								continue;
							}
							// The distance for thisRoot & - otherRoot
							if(sum + product == 2)
							{
								double[] pos2 = calcPos(otherRoot.vector, mode);
								connections.add(new Connection2D(pos[0], pos[1], -pos2[0], -pos2[1], 0, 0, 0));
								connections.add(new Connection2D(-pos[0], -pos[1], pos2[0], pos2[1], 0, 0, 0));
							}
						}
					}
				}

				if(mode == HASSE_MODE)
				{
					int[] dynkinLabels = algebras.algebra.rootToWeight(root.vector);
					for(int k = 0; k < root.vector.length; k++)
					{
						// Only draw reflections downward.
						if(dynkinLabels[k] <= 0 || i == 1)
							continue;
						int[] reflVector = root.vector.clone();
						reflVector[k] -= dynkinLabels[k];
						// And add the connection.
						double[] pos2 = calcPos(reflVector, mode);
						connections.add(new Connection2D(pos[0], pos[1], pos2[0], pos2[1], 0, 0, 0));
					}
				}

				// Add the root
				nodes.add(new Node2D(pos[0],pos[1], 0, 0, 0));
				// Also add the negative root for Coxeter projections
				if(mode == COXETER_MODE)
				{
					nodes.add(new Node2D(-pos[0],-pos[1], 0, 0, 0));
					checkMinMax(-pos[0],-pos[1]);
				}
			}
		}


	}

	private void checkMinMax(double x, double y)
	{
		maxCoorX = Math.max(maxCoorX,x);
		maxCoorY = Math.max(maxCoorY,y);
		minCoorX = Math.min(minCoorX,x);
		minCoorY = Math.min(minCoorY,y);

		double square = x*x + y*y;
		maxCoxDist = Math.max(maxCoxDist,square);
		minCoxDist = Math.min(minCoxDist,square);
	}

	private double[] calcPos(int[] rootVector, int mode)
	{
		double[] pos = {0.0f, 0.0f};
		if(mode == HASSE_MODE)
		{
			for (int i = 0; i < rootVector.length; i++)
			{
				pos[0] += rootVector[i] * hasseBasis[i];
				pos[1] += rootVector[i];
			}
		}
		if(mode == COXETER_MODE)
		{
			pos[0] = 2 * innerProduct(rootVector, coxeterX) / normCoxeterX;
			pos[1] = 2 * innerProduct(rootVector, coxeterY) / normCoxeterY;
		}

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
