/*
 * CDynkinConnection.java
 *
 * Created on 9 maart 2007, 11:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tan.leveldecomposition.dynkindiagram;

/**
 *
 * @author Teake Nutma
 */
public class CDynkinConnection
{
    // always have idNode1 < idNode2
    int idNode1;
    int idNode2;
    
    /** possibly extend this for non-simpy laced cases */
    // int laced;
    // int pointingTo;
    
    /** Creates a new instance of CDynkinConnection */
    public CDynkinConnection(int node1, int node2)
    {
	// always have idNode1 < idNode2
	if(node1 > node2)
	{
	    int tempId = node2;
	    node2 = node1;
	    node1 = tempId;
	}
	idNode1 = node1;
	idNode2 = node2;
    }
    
    public boolean equals(Object obj)
    {
	if(this == obj)
	    return true;
	
	if((obj == null) || (obj.getClass() != this.getClass()))
	    return false;
	CDynkinConnection compareConnection = (CDynkinConnection) obj;
	if(compareConnection.idNode1 == idNode1 && compareConnection.idNode2 == idNode2)
	    return true;
	else
	    return false;
    }
}
