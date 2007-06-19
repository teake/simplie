/*
 * CAlgebraComposite.java
 *
 * Created on 18-jun-2007, 14:47:23
 *
 */

package edu.rug.hep.simplie;

import edu.rug.hep.simplie.dynkindiagram.*;
import edu.rug.hep.simplie.group.*;

/**
 * @author Teake Nutma
 */
public class CAlgebraComposite implements DiagramListener
{
	/** CGroup object for the full group */
	public CGroup group;
	/** CGroup object for the regular subgroup */
	public CGroup subGroup;
	/** CGroup object for the internal subgroup */
	public CGroup intGroup;
	/** CGroup object for the non-level subgroup (which is equal to the direct product of subGroup x intGroup) */
	public CGroup coGroup;
	/** CDynkinDiagram object */
	public CDynkinDiagram dd;
	
	public CAlgebraComposite()
	{
		dd = new CDynkinDiagram();
		dd.addListener(this);
	}
	
	public void diagramChanged()
	{
		if(group == null || !Helper.sameMatrices(dd.cartanMatrix(), group.A))
			group = new CGroup(dd.cartanMatrix());
		
		subGroup	= new CGroup(dd.cartanSubMatrix("sub"));
		intGroup	= new CGroup(dd.cartanSubMatrix("int"));
		coGroup		= new CGroup(dd.cartanSubMatrix("co"));
	}
	
	/**
	 * Returns a string representing the type of decomposition of the full group into
	 * the regular subgroup and the disconnected subgroup.
	 *
	 * @return	String of the type "regular subgroup x disconnected subgroup representations in fullgroup".
	 * @see		#getDynkinDiagramType
	 */
	public String getDecompositionType()
	{
		String output;
		
		output = subGroup.type;
		if(intGroup.rank != 0)
			output += " x " + intGroup.type;
		output += " representations in " + group.type;
		
		return output;
	}
	
	/**
	 * Returns a string representing the regular subgroup and the disconnected subgroup of the full group.
	 *
	 * @return	String of the type "fullgroup as regular subgroup x disconnected subgroup".
	 * @see		#getDecompositionType
	 */
	public String getDynkinDiagramType()
	{
		String output;
		
		output = group.type;
		if(subGroup.rank != group.rank)
			output += " as " + subGroup.type;
		if(intGroup.rank != 0)
			output += " x " + intGroup.type;
		
		return output;
	}
	
}
