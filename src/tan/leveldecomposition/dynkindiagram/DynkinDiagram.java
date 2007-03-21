/*
 * DynkinDiagram.java
 *
 * Created on 8 maart 2007, 14:38
 *
 */

package tan.leveldecomposition.dynkindiagram;

import java.util.*;
import Jama.Matrix;
import java.io.*;

/**
 * Singleton class
 *
 * @author Teake Nutma
 */
public class DynkinDiagram
{
    private static DynkinDiagram _instance = new DynkinDiagram();
    public static Vector<CDynkinNode>		nodes;
    public static Vector<CDynkinConnection>	connections;
    
    /**
     * Creates a new instance of DynkinDiagram
     */
    private DynkinDiagram()
    {
	nodes	    = new Vector<CDynkinNode>();
	connections = new Vector<CDynkinConnection>();
    }
    
    public static DynkinDiagram getInstance()
    {
	return _instance;
    }
    
    /** Clears the Dynkin diagram */
    public static void Clear()
    {
	nodes.clear();
	connections.clear();
    }
    /** Returns the rank of the whole algebra */
    public static int GetRank()
    {
	if(nodes == null)
	    System.out.println("blablabla");
	return nodes.size();
    }
    
    /** Returns the rank of the subalgebra. */
    public static int GetSubRank()
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
    
    /**
     * Returns an array of booleans.
     * True if the corresponding node is enabled, false if disabled.
     */
    public static boolean[] GetEnabledNodes()
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
    
    /**
     * Returns the internal id of a node, given its label number.
     * Returns -1 if there's no node found.
     */
    private static int GetNodeIdByLabel(int label)
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
    
    /**
     * Returns the external label of a node, given its internal id.
     * Returns -1 if there's no node found.
     */
    private static int GetNodeLabelById(int id)
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
    
    /**
     * Fetches a node by its internal id.
     * Returns null if the node is not found.
     */
    public static CDynkinNode GetNodeById(int id)
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
    private static CDynkinNode GetNodeByLabel(int label)
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
    
    public static CDynkinNode GetNodeByCoor(int x, int y)
    {
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    if(node.x == x && node.y == y)
	    {
		return node;
	    }
	}
	return null;
    }
    
    /** Returns the Cartan matrix of the whole algebra. */
    public static Matrix GetCartanMatrix()
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
    public static Matrix GetCartanSubMatrix()
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
    
    /** Returns the last label that was added. */
    public static int GetLastLabel()
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
    
    public static int GetNextFreeLabel()
    {
	int nextFreeLabel = GetLastLabel() + 1;
	return nextFreeLabel;
    }
    
    public static boolean AddNode(int x, int y)
    {
	int newId = -1;
	
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	{
	    CDynkinNode node = (CDynkinNode) e.nextElement();
	    if (newId < node.id)
	    {
		newId = node.id;
	    }
	}
	newId++;
	
	CDynkinNode newNode = new CDynkinNode( newId, GetNextFreeLabel(), x, y);
	nodes.add(newNode);
	return true;
    }
    
    public static void RemoveNode(CDynkinNode node)
    {
	/** check if the node has connections and remove them */
	for (Enumeration e = connections.elements(); e.hasMoreElements();)
	{
	    CDynkinConnection connection = (CDynkinConnection) e.nextElement();
	    if(connection.idNode1 == node.id || connection.idNode2 == node.id)
	    {
		connections.remove(connection);
	    }
	}
	
	nodes.remove(node);
	
    }
    
    public static void ModifyConnection(int fromLabel, int toLabel, boolean add) // if add == false then remove
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
    private static void Refactor()
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
    
    /**
     * Saves the dynkindiagram to file.
     * Returns true upon succes, false on failure.
     */
    public static boolean SaveTo(String filename)
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
    public static boolean LoadFrom(String filename)
    {
	filename.trim();
	FileInputStream fis	= null;
	ObjectInputStream in	= null;
	try
	{
	    fis = new FileInputStream(filename);
	    in = new ObjectInputStream(fis);
	    nodes	= (Vector<CDynkinNode>) in.readObject();
	    connections = (Vector<CDynkinConnection>) in.readObject();
	    in.close();
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
    
}
