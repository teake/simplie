/*
 * Globals.java
 *
 * Created on 27 maart 2007, 17:17
 *
 */

package tan.leveldecomposition;

import tan.leveldecomposition.group.*;

/**
 * Singleton holding all 'global' variables
 *
 * @author Teake Nutma
 */
public class Globals
{
    /** Global variable for the full group */
    public static CGroup group;
    /** Global variable for the regular subgroup */
    public static CGroup subGroup;
    
    private static Globals _instance = null;
    
    /** Private constructor */
    private Globals()
    {
    }
    
    private static synchronized void createInstance()
    {
	if(_instance == null)
	    _instance = new Globals();
    }
    
    public static Globals getInstance()
    {
	if(_instance == null) createInstance();
	return _instance;
    } 

    
}
