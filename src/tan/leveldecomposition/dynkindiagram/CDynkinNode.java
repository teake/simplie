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
    int     label;
    boolean enabled;
    
    /** Creates a new instance of CDynkinNode */
    public CDynkinNode(int id, int label)
    {
	this.id		    = id;
	this.label	    = label;
	this.enabled	    = true;
    }
    
    // compares labels
    public int compareTo(CDynkinNode compareNode)
    {
	final int BEFORE = -1;
	final int EQUAL = 0;
	final int AFTER = 1;
	
	if(this.label > compareNode.label) return AFTER;
	if(this.label < compareNode.label) return BEFORE;
	
	return EQUAL;
    }
    
}
