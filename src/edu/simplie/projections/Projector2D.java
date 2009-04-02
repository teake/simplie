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
import java.awt.Rectangle;

/**
 *
 * @author Teake Nutma
 * @version $Rev$, $Date$
 */
public interface Projector2D
{
	void setAlgebrasComposite(AlgebraComposite algebras);

	void clear();

	void draw(Graphics2D g2, Rectangle bounds);

	void project(int maxHeight);

}
