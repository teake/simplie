/*
 * CDynkinNode.java
 *
 * Created on 8 maart 2007, 14:39
 *
 */

package tan.leveldecomposition.dynkindiagram;

import java.util.*;
import java.io.Serializable;

/**
 *
 * @author Teake Nutma
 */
public class CDynkinNode implements Serializable, Comparable<CDynkinNode>
{
    int	    id;
    public int     label;
    public boolean enabled;
    public int x;
    public int y;
    
    /** Creates a new instance of CDynkinNode */
    public CDynkinNode(int id, int label, int x, int y)
    {
	this.id		= id;
	this.label	= label;
	this.enabled	= true;
	this.x		= x;
	this.y		= y;
    }
    
    // compares labels
    public int compareTo(CDynkinNode compareNode)
    {
	final int BEFORE = -1;
	final int EQUAL = 0;
	final int AFTER = 1;
	
	if(this.y > compareNode.y) return AFTER;
	if(this.y < compareNode.y) return BEFORE;
	
	if(this.x > compareNode.x) return AFTER;
	if(this.x < compareNode.x) return BEFORE;
	
	return EQUAL;
    }
    
}
