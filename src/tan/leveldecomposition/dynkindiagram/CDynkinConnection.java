/*
 * CDynkinConnection.java
 *
 * Created on 9 maart 2007, 11:19
 *
 */

package tan.leveldecomposition.dynkindiagram;

import java.io.Serializable;

/**
 *
 * @author Teake Nutma
 */
public class CDynkinConnection implements Serializable
{
	public final CDynkinNode fromNode;
	public final CDynkinNode toNode;
	
	/** possibly extend this for non-simpy laced cases */
	// int laced;
	// int pointingTo;
	
	/** Creates a new instance of CDynkinConnection */
	public CDynkinConnection(CDynkinNode fromNode, CDynkinNode toNode)
	{
		this.fromNode	= fromNode;
		this.toNode		= toNode;
	}
	
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		CDynkinConnection compareConnection = (CDynkinConnection) obj;
		if(compareConnection.fromNode.equals(fromNode) && compareConnection.toNode.equals(toNode))
			return true;
		else
			return false;
	}
}
