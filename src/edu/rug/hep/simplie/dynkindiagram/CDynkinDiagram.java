/*
 * CDynkinDiagram.java
 *
 * Created on 8 maart 2007, 14:38
 *
 */

package edu.rug.hep.simplie.dynkindiagram;

import edu.rug.hep.simplie.Helper;
import edu.rug.hep.simplie.ui.shapes.*;

import java.util.Vector;
import java.util.Iterator;
import java.util.Collections;
import java.io.*;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.Color;
import java.awt.Font;
import Jama.Matrix;

/**
 * A class representing (as of yet only simply-laced) Dynkin diagrams.
 * Any node within this diagram can be disabled in order to form regular subalgebras
 * of the whole algebra, which in turn can be used for a level decomposition.
 *
 * @author Teake Nutma
 */
public class CDynkinDiagram
{
	/** Vector containing all nodes of this diagram. */
	private Vector<CDynkinNode> nodes;
	/** Vector containing all connections of this diagram */
	private Vector<CDynkinConnection> connections;
	/** Font for drawing the diagram. */
	private Font font;
	/** The node that was added last. */
	private CDynkinNode lastAddedNode;
	/** The internal list of listeners */
	private Vector<DiagramListener> listeners;
	/** Internal boolean to keep if the diagram is locked or not */
	private boolean locked;
	
	/**
	 * Creates a new instance of CDynkinDiagram
	 */
	public CDynkinDiagram()
	{
		locked		= false;
		nodes		= new Vector<CDynkinNode>();
		connections	= new Vector<CDynkinConnection>();
		font		= new Font("Monospaced", Font.PLAIN, 12);
		lastAddedNode	= null;
		listeners		= new Vector<DiagramListener>();
	}
	
	public void addListener(DiagramListener listener)
	{
		listeners.add(listener);
	}
	
	public void setLocked(boolean locked)
	{
		this.locked = locked;
	}
	
	public boolean getLocked()
	{
		return this.locked;
	}
	
	/** Clears the Dynkin diagram. That is, it deletes all nodes. */
	public void clear()
	{
		if(locked)
			return;
		
		nodes.clear();
		connections.clear();
		lastAddedNode = null;
		update();
	}
	
	/** Returns the rank algebra associated to this diagram, i.e. the number of nodes. */
	public int rank()
	{
		return nodes.size();
	}
	
	/**
	 * Fetches a nodes by its coordinates.
	 * Returns null if the nodes is not found.
	 *
	 * @param	x	The x-coordinate of the node in the diagram.
	 * @param	y	The y-coordinate of the node in the diagram.
	 * @return		The node itself.
	 */
	public CDynkinNode getNodeByCoor(int x, int y)
	{
		for (CDynkinNode node : nodes)
		{
			if(node.x == x && node.y == y)
			{
				return node;
			}
		}
		return null;
	}
	
	/** Translates an index of the submatrix into an index of the full matrix */
	public int translateSub(int index)
	{
		int subIndex = 0;
		for(int i = 0; i < rank(); i++)
		{
			CDynkinNode node = nodes.get(i);
			if(node.isEnabled())
				subIndex++;
			if(subIndex == index + 1)
				return i;
		}
		return -1; // not found
	}
	
	/** Translates a non-level index into an index of the full matrix */
	public int translateCo(int index)
	{
		int subIndex = 0;
		for(int i = 0; i < rank(); i++)
		{
			CDynkinNode node = nodes.get(i);
			if(!node.isLevel())
				subIndex++;
			if(subIndex == index + 1)
				return i;
		}
		return -1; // not found
	}
	
	/** Translates a disconnected-index into an index of the full matrix */
	public int translateDis(int index)
	{
		int subIndex = 0;
		for(int i = 0; i < rank(); i++)
		{
			CDynkinNode node = nodes.get(i);
			if(node.isDisconnected())
				subIndex++;
			if(subIndex == index + 1)
				return i;
		}
		return -1; // not found
	}
	
	/** Translates a level-index to an index of the full matrix */
	public int translateLevel(int index)
	{
		int subIndex = 0;
		for(int i = 0; i < rank(); i++)
		{
			CDynkinNode node = nodes.get(i);
			if(node.isLevel())
				subIndex++;
			if(subIndex == index + 1)
				return i;
		}
		return -1; // not found
	}
	
	/** Returns the Cartan matrix of the whole algebra. */
	public Matrix cartanMatrix()
	{
		// Creates a rank x rank matrix filled with zeros.
		Matrix cartanMatrix = new Matrix(rank(),rank());
		
		// Set the diagonals to two.
		for(int i = 0; i < rank(); i++)
			cartanMatrix.set(i,i,2);
		
		// Set the off-diagonal parts.
		for (CDynkinConnection connection : connections)
		{
			int i		= connection.fromNode.getLabel() - 1;
			int j		= connection.toNode.getLabel() - 1;
			cartanMatrix.set(i,j,-1*connection.laced);
			cartanMatrix.set(j,i,-1);
		}
		
		return cartanMatrix;
	}
	
	/**
	 * Returns the Cartan matrix of one of the subalgebras.
	 *
	 * @param	type	The name of the subalgebra to get. Either "sub", "int", or "co".
	 * @return			The cartan matrix of the regular ("sub"),
	 *					the internal ("int"), or the sub x dis ("co") subalgebra.
	 */
	public Matrix cartanSubMatrix(String type)
	{
		if( !( type == "sub" || type == "int" || type == "co") )
			return null;
		
		int indexI;
		int indexJ;
		int subRank = 0;
		
		for (CDynkinNode node : nodes)
		{
			if((node.isEnabled() && type == "sub")
					|| (node.isDisconnected() && type == "int")
					|| (!node.isLevel() && type == "co") )
			{
				subRank++;
			}
		}
		Matrix cartanSubMatrix	= new Matrix(subRank,subRank);
		Matrix cartanMatrix	= cartanMatrix();
		
		// Copy the Cartan matrix elements into the submatrix.
		for(int i = 0; i < subRank; i++)
		{
			if(type == "sub")
				indexI = translateSub(i);
			else if(type == "co")
				indexI = translateCo(i);
			else
				indexI = translateDis(i);
			for(int j = 0; j < subRank; j++)
			{
				if(type == "sub")
					indexJ = translateSub(j);
				else if(type == "co")
					indexJ = translateCo(j);
				else
					indexJ = translateDis(j);
				cartanSubMatrix.set(i,j, cartanMatrix.get(indexI,indexJ));
			}
		}
		
		return cartanSubMatrix;
	}
	
	/**
	 * Adds a node to the diagram on the specified coordinates.
	 *
	 * @param	x					The x-coordinate of the node in the diagram.
	 * @param	y					The y-coordinate of the node in the diagram.
	 * @param	connectionToLast	Integer indicating whether or not a connection should be made
	 *								from this node to the last one added. If it equals zero, then
	 *								a connection won't be made. If it is bigger than zero it represents
	 *								the lacing of the connection.
	 * @return						A string representing the action taken.
	 */
	public String addNode(int x, int y, int connectionToLast)
	{
		if(locked)
			return "Diagram is locked.";
		
		CDynkinNode newNode = new CDynkinNode(x, y);
		
		if(nodes.contains(newNode))
		{
			return "Cannot add node that is already present.";
		}
		else
		{
			if(connectionToLast > 0 && lastAddedNode != null)
			{
				modifyConnection(lastAddedNode, newNode, connectionToLast, true);
			}
			nodes.add(newNode);
			lastAddedNode = newNode;
			update();
			return "Node added.";
		}
	}
	
	public String toggleNode(CDynkinNode node)
	{
		if(locked)
			return "Diagram is locked.";
		
		if(node != null && nodes.contains(node))
		{
			node.toggle();
			update();
			return "Node toggled.";
		}
		return "No node to toggle.";
	}
	
	/** Removes a node from the diagram. */
	public String removeNode(CDynkinNode nodeToRemove)
	{
		if(locked)
			return "Diagram is locked.";
		
		int prevRank = rank();
		if(lastAddedNode != null && lastAddedNode.equals(nodeToRemove))
		{
			lastAddedNode = null;
		}
		nodes.remove(nodeToRemove);
		for(CDynkinNode node : nodes)
		{
			node.removeConnection(nodeToRemove);
		}
		Iterator it = connections.iterator();
		while(it.hasNext())
		{
			CDynkinConnection connection = (CDynkinConnection) it.next();
			if(connection.fromNode.equals(nodeToRemove)
					|| connection.toNode.equals(nodeToRemove) )
			{
				it.remove();
			}
		}
		if(prevRank > rank())
		{
			update();
			return "Node removed.";
		}
		else
			return "No node to remove.";
	}
	
	/**
	 * Adds or removes a connection.
	 *
	 * @param fromNode		The node from which the connection points.
	 * @param toNode		The node to which the connection points.
	 * @param add			True: add the connection. False: remove the connection.
	 */
	public String modifyConnection(CDynkinNode fromNode, CDynkinNode toNode, int laced, boolean add)
	{
		if(locked)
			return "Diagram is locked.";
		
		String action = (add) ? "added" : "removed";
		// Do nothing if either one of the nodes is not found, or if both are the same.
		if( fromNode == null || toNode == null || fromNode.equals(toNode) )
			return "No connection " + action + ", begin and / or end point incorrect.";
		
		CDynkinConnection connection = new CDynkinConnection(fromNode, toNode, laced);
		
		if(add && !connections.contains(connection))
		{
			connections.add(connection);
			fromNode.addConnection(toNode);
			toNode.addConnection(fromNode);
		}
		else
		{
			connections.remove(connection);
			fromNode.removeConnection(toNode);
			toNode.removeConnection(fromNode);
		}
		
		update();
		return "Connection " + action + ".";
	}
	
	/**
	 * Saves the dynkindiagram to file.
	 * Returns true upon succes, false on failure.
	 */
	public boolean saveTo(String filename)
	{
		filename.trim();
		FileOutputStream fos	= null;
		ObjectOutputStream out	= null;
		try
		{
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(nodes);
			out.writeObject(connections);
			out.close();
		}
		catch(IOException ex)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Loads the dynkindiagram from a file.
	 * Returns true on succes, false on failure.
	 */
	public boolean loadFrom(String filename)
	{
		if(locked)
			return false;
		
		filename.trim();
		FileInputStream fis		= null;
		ObjectInputStream in	= null;
		try
		{
			fis		= new FileInputStream(filename);
			in		= new ObjectInputStream(fis);
			nodes	= (Vector<CDynkinNode>) in.readObject();
			connections = (Vector<CDynkinConnection>) in.readObject();
			in.close();
			update();
		}
		catch(IOException ex)
		{
			return false;
		}
		catch(ClassNotFoundException ex)
		{
			return false;
		}
		return true;
	}
	
	/** Returns a string of LaTeX representing the diagram visually. */
	public String toTeX(boolean includeCaption, boolean includeLabels)
	{
		if(rank() == 0)
		{
			return "";
		}
		
		// Append a hashcode of this specific diagram to all the labels in the psfigure.
		// This prevents multiple garbled psfigures on one page.
		String hashCode =
				(Helper.matrixToString(cartanMatrix(), 0) + Helper.matrixToString(cartanSubMatrix("sub"), 0).hashCode());
		
		// First determine the min and max values of x and y
		int xMin = Integer.MAX_VALUE;
		int yMin = Integer.MAX_VALUE;
		int xMax = 0;
		int yMax = 0;
		
		for(CDynkinNode node : nodes)
		{
			xMin = Math.min(node.x,xMin);
			yMin = Math.min(node.y,yMin);
			xMax = Math.max(node.x,xMax);
			yMax = Math.max(node.y,yMax);
		}
		
		String output = new String();
		
		// The header
		output += "\\begin{figure}[h]\n";
		output += "\\psset{unit=1.5cm}\n";
		output += "\\begin{center}\n";
		output += "\\begin{pspicture}(" + xMin + "," + yMin + ")(" + xMax + "," + yMax + ")\n";
		
		// The nodes.
		for(CDynkinNode node : nodes)
		{
			output += "\\cnode";
			if(node.isDisconnected())
				output += "[fillstyle=solid,fillcolor=lightgray]";
			if(node.isLevel())
				output += "[fillstyle=solid,fillcolor=black]";
			output += "(" + node.x + "," + (yMax - node.y) + "){0.17}{N" + node.getLabel() + hashCode + "} \n";
			if(includeLabels)
				output += "\\nput[labelsep=0.2]{-40}{N" + node.getLabel() + hashCode + "}{" + node.getLabel() + "}\n";
		}
		
		// The connections.
		for(CDynkinConnection connection : connections)
		{
			String toFrom= "{N" + connection.fromNode.getLabel() + hashCode + "}"
					+ "{N" + connection.toNode.getLabel() + hashCode + "}\n";
			output += "\\ncline";
			if(connection.laced > 1)
				output += "[doubleline=true,doublesep=0.2,arrowsize=0.6,arrowlength=0.25,arrowinset=0.6]{->}";
			else
				output += "{-}";
			output += toFrom;
			if(connection.laced == 3)
				output += "\\ncline{-}" + toFrom;
		}
		
		
		// The footer
		output += "\\end{pspicture}\n";
		output += "\\end{center}\n";
		if(includeCaption)
			output += "\\caption{Dynkin diagram of ... }\n";
		output += "\\end{figure}\n";
		
		return output;
	}
	
	/**
	 * Draw the diagram onto a graphics component.
	 *
	 * @param	g2		The graphics component onto which the diagram should be drawn.
	 * @param	offset	The amount of spacing between the edges of the component and the diagram.
	 * @param	spacing	The amount of spacing between each node.
	 * @param	radius	The radius of each node.
	 */
	public void drawDiagram(Graphics2D g2, int offset, int spacing, int radius)
	{
		for (CDynkinConnection connection : connections)
		{
			CDynkinNode node1 = connection.fromNode;
			CDynkinNode node2 = connection.toNode;
			Shape line;
			Point begin	= new Point(spacing * node1.x + offset, spacing * node1.y + offset);
			Point end	= new Point(spacing * node2.x + offset, spacing * node2.y + offset);
			switch(connection.laced)
			{
			case 3:
				line = new LinesWithArrow(begin,end,3,2*radius);
				break;
			case 2:
				line = new LinesWithArrow(begin,end,2,2*radius);
				break;
			default:
				line = new Line2D.Double(begin,end);
			}
			g2.draw(line);
		}
		for (CDynkinNode node : nodes)
		{
			if(node.isEnabled())
				g2.setColor(Color.WHITE);
			else if(node.isDisconnected())
				g2.setColor(Color.ORANGE);
			else
				g2.setColor(Color.GRAY);
			g2.fillOval(
					spacing * node.x + offset - radius,
					spacing * node.y + offset - radius,
					2*radius, 2*radius);
			
			g2.setColor(Color.BLACK);
			g2.drawOval(
					spacing * node.x + offset - radius,
					spacing * node.y + offset - radius,
					2*radius, 2*radius);
			
			g2.setFont(font);
			g2.drawString(Helper.intToString(node.getLabel()),
					spacing * node.x + offset + radius,
					spacing * node.y + offset + radius + 10);
		}
	}
	
	private void update()
	{
		Collections.sort(nodes);
		for(int i = 0; i < rank(); i++)
		{
			nodes.get(i).setLabel(i+1);
		}
		for(DiagramListener listener : listeners)
		{
			listener.diagramChanged();
		}
	}
}
