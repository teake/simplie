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
import java.awt.Graphics2D;

/**
 * Interface for 2-dimensional root system projectors.
 * 
 * @author Teake Nutma
 * @version $Rev$, $Date$
 */
public interface Projector2D
{
	/**
	 * Sets the algebra composite whose root system is to be projected.
	 *
	 * @param algebras	The algebra composite that should get projected.
	 */
	void setAlgebrasComposite(AlgebraComposite algebras);

	/**
	 * Clears the projection.
	 */
	void clear();

	/**
	 * Draws the projected root system onto a Graphics2D object.
	 *
	 * @param g2		The object onto which to draw.
	 * @param width		The maximum width of the resulting image.
	 * @param height	The maximum height of the resutling image.
	 */
	void draw(Graphics2D g2, double width, double height);

	/**
	 * Writes the projected image to an EPS file
	 *
	 * @param filename	The file to which to write the image.
	 */
	void toEpsFile(String filename);

	/**
	 * Do the projection calculation.
	 */
	void project();

	/**
	 * Should the roots get drawn?
	 *
	 * @param drawNodes		The projected roots will be drawn if true.
	 */
	void setDrawNodes(boolean drawNodes);

	/**
	 * Should the connections between roots be drawn?
	 *
	 * @param drawConnections	The connections between projected root will be drawn if true.
	 */
	void setDrawConnections(boolean drawConnections);

	/**
	 * Sets the maximum height of the roots to which to project.
	 * @param maxHeight		The maximum height of roots.
	 */
	void setMaxHeight(int maxHeight);
}
