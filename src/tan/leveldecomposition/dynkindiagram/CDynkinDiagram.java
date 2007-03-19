/*
 * CDynkinDiagram.java
 *
 * Created on 8 maart 2007, 14:38
 *
 */

package tan.leveldecomposition.dynkindiagram;

import java.util.*;
import Jama.Matrix;

/**
 *
 * @author Teake Nutma
 */
public class CDynkinDiagram
{
    private Vector<CDynkinNode>		nodes;
    private Vector<CDynkinConnection>	connections;
    
    /** Creates a new instance of CDynkinDiagram */
    public CDynkinDiagram()
    {
	nodes	    = new Vector<CDynkinNode>();
	connections = new Vector<CDynkinConnection>();
    }

    /** Clears the Dynkin diagram */
    public void Clear()
    {
	nodes.clear();
	connections.clear();
    }
    
    /** Returns an array of booleans.
	True if the corresponding node is enabled, false if disabled. */
    public boolean[] GetEnabledNodes()
    {
	boolean[] enabledNodes = new boolean[GetRank()];
	for (int i = 0; i < GetRank(); i++)
	{
	    if(GetNodeByLabel(i+1).enabled)
		enabledNodes[i] = true;
	    else
		enabledNodes[i] = false;
	}
	return enabledNodes;
    }
    
    /** Returns the internal id of a node, given its label number.
	Returns -1 if there's no node found.  */
    private int GetNodeIdByLabel(int label)
    {
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    if(node.label == label)
	    {
		return node.id;
	    }
	}
	return -1;
    }
    
    /** Returns the external label of a node, given its internal id.
	Returns -1 if there's no node found.  */
    private int GetNodeLabelById(int id)
    {
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    if(node.id == id)
	    {
		return node.label;
	    }
	}
	return -1;
    }
    
    /** Returns a vector containing ids of nodes with a connection to this node. */
    private Vector<Integer> GetNodeConnections(int id)
    {
	Vector<Integer> nodeConnections = new Vector<Integer>();
	int connectionId;
	for (Enumeration e = connections.elements(); e.hasMoreElements();)
	{
	    connectionId = -1;
	    CDynkinConnection connection = (CDynkinConnection) e.nextElement();
	    
	    if(connection.idNode1 == id)
		connectionId = connection.idNode2;
	    if(connection.idNode2 == id)
		connectionId = connection.idNode1;
	    
	    if(connectionId != -1 && !nodeConnections.contains(new Integer(connectionId)))
		nodeConnections.add(new Integer(connectionId));
	}
	return nodeConnections;
    }
    
    /** 
     * Fetches a node by its internal id.
     * Returns null if the node is not found.
     */
    private CDynkinNode GetNodeById(int id)
    {
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    if(node.id == id)
	    {
		return node;
	    }
	}
	return null;
    }
    
    /** 
     * Fetches a node by its external label.
     * Returns null if the node is not found.
     */
    private CDynkinNode GetNodeByLabel(int label)
    {
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    if(node.label == label)
	    {
		return node;
	    }
	}
	return null;
    }
    
    /** Returns the rank of the whole algebra */
    public int GetRank()
    {
	return nodes.size();
    }
    
    /** Returns the rank of the subalgebra. */
    public int GetSubRank()
    {
	int subRank = 0;
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    if(node.enabled)
	    {
		subRank++;
	    }
	}
	return subRank;
    }
    
    /** Returns the Cartan matrix of the whole algebra. */
    public Matrix GetCartanMatrix()
    {
	Refactor();
	
	Matrix cartanMatrix = new Matrix(GetRank(),GetRank());
	for(int i = 0; i < GetRank(); i++)
	{
	    cartanMatrix.set(i,i,2);
	    for (Enumeration e = connections.elements(); e.hasMoreElements();)
	    {
		CDynkinConnection connection = (CDynkinConnection) e.nextElement();
		if(GetNodeLabelById(connection.idNode1) == i + 1)
		{
		    int index2 = GetNodeLabelById(connection.idNode2) - 1;
		    cartanMatrix.set(i,index2, -1);
		    cartanMatrix.set(index2,i, -1);
		}
	    }
	    
	}
	return cartanMatrix;
    }
    
    /** Returns the Cartan matrix of the subalgebra */
    public Matrix GetCartanSubMatrix()
    {
	int subRank = GetSubRank();
	int rank    = GetRank();
	int offsetI = 0;
	int offsetJ = 0;
	
	Matrix cartanSubMatrix	= new Matrix(subRank,subRank);
	Matrix cartanMatrix	= GetCartanMatrix();
	
	/** Copy the Cartan matrix elements into the submatrix. */
	for(int i = 0; i < rank; i++)
	{
	    if(GetNodeByLabel(i+1).enabled)
	    {
		for(int j = 0; j < rank; j++)
		{
		    if(GetNodeByLabel(j+1).enabled)
		    {
			cartanSubMatrix.set(offsetI, offsetJ, cartanMatrix.get(i,j));
			offsetJ++;
		    }
		}
		offsetJ = 0;
		offsetI++;
	    }
	}
	
	return cartanSubMatrix;
    }
    
    /** Returns a plain string visually representing the Dynkin diagram. */
    public String GetDiagram()
    {
	// ONLY WORKS FOR THE A SERIES CURRENTLY!!! FIX IT FOR MORE GENERAL CONNECTIONS!!!
	
	String diagram		= new String();
	String diagramNodes	= new String();
	String diagramLabels	= new String();
	
	if(GetRank() == 0)
	    return diagram;
	
	Refactor();
	
	Vector<Integer> drawnNodes  = new Vector<Integer>();
	int	nextNode	    = -1;
	boolean hasNewConnection    = false;
	
	for(int i = 1; i < GetRank() + 1; i++)
	{
	    if(!drawnNodes.contains(new Integer(i)))
	    {
		do
		{
		    int label		= hasNewConnection ? nextNode : i;
		    CDynkinNode node	= GetNodeByLabel(label);
		    hasNewConnection	= false;
		    
		    if(node.enabled)
			diagramNodes += "o";
		    else
			diagramNodes += "x";
		    diagramLabels += label;
		    
		    drawnNodes.add(new Integer(label));
		    for (Enumeration e = GetNodeConnections(node.id).elements(); e.hasMoreElements();)
		    {
			nextNode = GetNodeLabelById((Integer) e.nextElement());
			if(!drawnNodes.contains(nextNode))
			{
			    hasNewConnection = true;
			    diagramNodes += " - ";
			    break;
			}
		    }
		    if(!hasNewConnection)
			diagramNodes += "   ";
		    diagramLabels += "   ";
		    
		} while (hasNewConnection);
	    }
	}
	
	
	return diagramNodes + "\n" + diagramLabels;
    }
    
    /** Returns the last label that was added. */
    public int GetLastLabel()
    {
	int lastLabel = 0;
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    if(node.label > lastLabel)
	    {
		lastLabel = node.label;
	    }
	}
	
	return lastLabel;
    }
    
    public int GetNextFreeLabel()
    {
	int nextFreeLabel = GetLastLabel() + 1;
	return nextFreeLabel;
    }
    
    public void AddNode(int newLabel, int toLabel)
    {
	int newId = -1;
	
	// get the first free id and check if the label isn't already present
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    if (newId < node.id)
	    {
		newId = node.id;
	    }
	    if(node.label == newLabel)
	    {
		return; // we can't add a label that's already there
	    }
	}
	newId++;
	
	CDynkinNode newNode = new CDynkinNode(newId, newLabel);
	nodes.add(newNode);
	
	ModifyConnection(newLabel,toLabel,true);
    }
    
    public void RemoveNode(int label)
    {
	int id = GetNodeIdByLabel(label);
	if(id == -1)
	    return;
	
	/** check if the node has connections and remove them */
	for (Enumeration e = connections.elements(); e.hasMoreElements();)
	{
	    CDynkinConnection connection = (CDynkinConnection) e.nextElement();
	    if(connection.idNode1 == id || connection.idNode2 == id)
	    {
		connections.remove(connection);
	    }
	}
	
	nodes.remove(GetNodeById(id));
	
    }
    
    public void ToggleNode(int label)
    {
	CDynkinNode node = GetNodeByLabel(label);
	if(node != null)
	{
	    node.enabled = !node.enabled;
	}
    }
    
    public void ModifyConnection(int fromLabel, int toLabel, boolean add) // if add == false then remove
    {
	int fromId  = GetNodeIdByLabel(fromLabel);
	int toId    = GetNodeIdByLabel(toLabel);
	
	// do nothing if either one of the ids is not found, or if both are the same
	if(toId == -1 || fromId == -1 || toId == fromId)
	    return;
	
	CDynkinConnection newConnection = new CDynkinConnection(toId, fromId);
	if(connections.contains(newConnection))
	{
	    if(!add)
		connections.remove(newConnection);
	    return;
	}
	
	if(add)
	    connections.add(newConnection);
    }
    
    /** Reshapes the internal structure. */
    private void Refactor()
    {
	/** sort the nodes according to their label */
	Collections.sort(nodes);
	
	/** Reset all the labels */
	int label = 1;
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    node.label = label++;
	}
    }
    
}
