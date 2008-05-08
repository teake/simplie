/*
 * DiagramListener.java
 *
 * Created on 18 mei 2007, 16:11
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

/**
 * Interface for listeners to changes of a Dynkin diagram.
 * 
 * @author Teake Nutma
 */
public interface DiagramListener
{
	/**
	 * The function called when the diagram has changed 
	 * (for example, when a nodes has been disabled or deleted).
	 */
	void diagramChanged();
}
