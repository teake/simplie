/*
 * CDynkinDiagram.java
 *
 * Created on 8 maart 2007, 14:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
    Vector<CDynkinNode>		nodes;
    Vector<CDynkinConnection>	connections;
    
    /** Creates a new instance of CDynkinDiagram */
    public CDynkinDiagram()
    {
	nodes	    = new Vector<CDynkinNode>();
	connections = new Vector<CDynkinConnection>();
    }
    
    // returns the internal id of a node, given its label number.
    // returns -1 if there's no node found.
    public int GetNodeIdByLabel(int label)
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
    
    public int GetNodeLabelById(int id)
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
    
    public CDynkinNode GetNodeById(int id)
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
    
    public CDynkinNode GetNodeByLabel(int label)
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
    
    public int GetRank()
    {
	return nodes.size();
    }
    
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
    
    public Matrix GetCartanSubMatrix()
    {
	int subRank = GetSubRank();
	int rank    = GetRank();
	int offsetI = 0;
	int offsetJ = 0;
	
	Matrix cartanSubMatrix	= new Matrix(subRank,subRank);
	Matrix cartanMatrix	= GetCartanMatrix();
	
	// copy the Cartan matrix elements into the submatrix
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
    
    public String GetDiagram()
    {
	// ONLY WORKS FOR THE A SERIES CURRENTLY!!! FIX IT FOR MORE GENERAL CONNECTIONS!!!
	
	Refactor();
	
	String diagramNodes	= new String();
	String diagramLabels	= new String();
	
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    if(node.enabled)
	    {
		diagramNodes += "o";
	    }
	    else
	    {
		diagramNodes += "x";
	    }
	    
	    if(node.connections > 0)
		diagramNodes += " - ";
	    else
		diagramNodes += "   ";
	    
	    Integer label = new Integer(node.label);
	    diagramLabels   += label.toString() + "   ";
	}
	
	return diagramNodes + "\n" + diagramLabels;
    }
    
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
	
	// check if the node has connections and remove them
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
	    {
		connections.remove(newConnection);
		GetNodeById(fromId).connections--;
		GetNodeById(toId).connections--;
	    }
	    return;
	}
	
	if(add)
	{
	    connections.add(newConnection);
	    GetNodeById(fromId).connections++;
	    GetNodeById(toId).connections++;
	}
    }
    
    
    private void Refactor()
    {
	// sort the nodes according to their label
	Collections.sort(nodes);
	//Collections.sort(connections, CompareConnections());
	
	// reset all the labels
	int label = 1;
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    node.label = label++;
	}
	
    }
    
    /*
    private int CompareConnections(CDynkinConnection connection1, CDynkinConnection connection2)
    {
	final int BEFORE = -1;
	final int EQUAL = 0;
	final int AFTER = 1;
     
	if(getNodeLabelById(connection1.idNode1) > getNodeLabelById(connection2.idNode1)) return AFTER;
	if(getNodeLabelById(connection1.idNode1) < getNodeLabelById(connection2.idNode1)) return BEFORE;
     
	return EQUAL;
    }
     */
    
}
