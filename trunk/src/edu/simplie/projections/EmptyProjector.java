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

import edu.simplie.AlgebraComposite;
import edu.simplie.Helper;
import edu.simplie.algebra.Root;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.epsgraphics.EpsGraphics;

/**
 *
 * @author Teake Nutma
 * @version $Rev$, $Date$
 */
public class EmptyProjector implements Projector2D
{
	public SortedMap<Number,Set<Connection2D>> conns;
	public Set<Node2D> nodes;
	public AlgebraComposite algebras;

	public double maxCoorX;
	public double maxCoorY;
	public double minCoorX;
	public double minCoorY;

	private double offX;
	private double offY;
	private double scale;
	private double radius = 6.0d;

	public EmptyProjector()
	{
		conns = new TreeMap<Number,Set<Connection2D>>();
		nodes = new HashSet<Node2D>();
	}

	public void setAlgebrasComposite(AlgebraComposite algebras)
	{
		this.algebras = algebras;
	}

	public void clear()
	{
		conns.clear();
		nodes.clear();
		maxCoorX = maxCoorY = -Double.MAX_VALUE;
		minCoorX = minCoorY = +Double.MAX_VALUE;
	}

	public void draw(Graphics2D g2, double width, double height)
	{
		if(nodes.isEmpty() || conns.isEmpty())
			return;
		
		offX = width / 2;
		offY = height / 2;

		double scaleX = 0.9 * width / ( maxCoorX - minCoorX );
		double scaleY = 0.9 * height / ( maxCoorY - minCoorY );

		scale = Math.min(scaleX,scaleY);
		radius = scale / 16;

		// Draw the connections.
		g2.setStroke(new BasicStroke(0.5f));
		double maxKey = conns.lastKey().doubleValue();
		double minKey = conns.firstKey().doubleValue();
		for(Map.Entry<Number,Set<Connection2D>> entry : conns.entrySet())
		{
			// Determine the color
			float frac = (float) ((entry.getKey().doubleValue() - maxKey) / ( minKey - maxKey));
			float[] color = Helper.colorSpectrum(2*frac/3);
			g2.setColor(new Color(color[0],color[1],color[2], 0.7f));
			// Iterate over the connections
			for(Iterator it = entry.getValue().iterator(); it.hasNext();)
			{
				Connection2D conn = (Connection2D) it.next();
				double[] pos1 = transformCoor(conn.x1, conn.y1);
				double[] pos2 = transformCoor(conn.x2, conn.y2);
				
				g2.draw((new Line2D.Double(pos1[0], pos1[1], pos2[0], pos2[1])));
			}
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

	private double[] transformCoor(double x, double y)
	{
		double[] newCoor = new double[2];
		newCoor[0] =  scale * ( x - (maxCoorX + minCoorX)/2 ) + offX;
		newCoor[1] = -scale * ( y - (maxCoorY + minCoorY)/2 ) + offY;
		return newCoor;
	}

	public void project(int maxHeight)
	{
		// Don't do anything if ...
		if(algebras.algebra == null 
				|| algebras.algebra.rank == 0
				|| algebras.subAlgebra.rank == 0
				|| !algebras.subAlgebra.finite)
			return;
		
		// Reset stuff
		clear();

		// First construct the root system up to the wanted height.
		algebras.algebra.rs.construct(maxHeight);
		if(maxHeight == 0)
			maxHeight = algebras.algebra.rs.size();

		// Do perhaps some other stuff
		preProject();

		// Loop over every root.
		for(int i = 1; i < maxHeight + 1 && i < algebras.algebra.rs.size(); i++)
		{
			Collection<Root> roots = algebras.algebra.rs.get(i);
			for(Iterator it = roots.iterator(); it.hasNext();)
			{
				projectRoot((Root) it.next());
			}
		}

		// Do again some other stuff
		postProject();
	}

	public boolean addConnection(Number key, Connection2D conn)
	{
		Set<Connection2D> connections = conns.get(key);
		if(connections == null)
		{
			connections = new HashSet<Connection2D>();
			conns.put(key, connections);
		}
		return connections.add(conn);
	}

	public void toEpsFile(String filename)
	{
		FileOutputStream outputStream = null;
		EpsGraphics eps = null;
		try
		{
			outputStream = new FileOutputStream(filename);
			eps = new EpsGraphics("Projection", outputStream, 0, 0, 500, 500, net.sf.epsgraphics.ColorMode.COLOR_RGB);
			draw(eps, 500, 500);
		}
		catch(Exception ex)
		{
			Logger.getLogger(EmptyProjector.class.getName()).log(Level.SEVERE, null, ex);
		}
		finally
		{
			try
			{
				eps.flush();
				eps.close();
			}
			catch(IOException ex)
			{
				Logger.getLogger(EmptyProjector.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void preProject(){}

	public void postProject(){}

	public void projectRoot(Root root){}
}