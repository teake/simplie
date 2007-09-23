/*
 * DiagramListener.java
 *
 * Created on 18 mei 2007, 16:11
 *
 */

package edu.rug.hep.simplie.dynkindiagram;

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
