/*
 * AlgebraSetup.java
 *
 * Created on 13 maart 2007, 15:05
 */

package edu.rug.hep.simplie.ui;

import edu.rug.hep.simplie.*;
import edu.rug.hep.simplie.dynkindiagram.*;

import Jama.Matrix.*;

/**
 *
 * @author  Teake Nutma
 */
public class AlgebraSetup extends javax.swing.JPanel implements DiagramListener
{
	private CAlgebraComposite algebras;
	
	/** Creates new form LevelDecompositionUI */
	public AlgebraSetup()
	{
		initComponents();
	}
	
	public void setAlgebraComposite(CAlgebraComposite algebras)
	{
		this.algebras = algebras;
		dynkinDiagramPanel.setDynkinDiagram(algebras.dd);
		algebras.dd.addListener(this);
	}
	
	public void diagramChanged()
	{
		algebraInfo.update(algebras.group);
		subAlgebraInfo.update(algebras.subGroup);
		disAlgebraInfo.update(algebras.intGroup);
		coAlgebraInfo.update(algebras.coGroup);
	
		dynkinDiagramPanel.setTitle("Dynkin Diagram of " + algebras.getDynkinDiagramType());
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        algebraInfo = new edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo();
        subAlgebraInfo = new edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo();
        disAlgebraInfo = new edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo();
        coAlgebraInfo = new edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo();
        dynkinDiagramPanel = new edu.rug.hep.simplie.ui.DynkinDiagramPanel();

        algebraInfo.setTitle("Full algebra");

        subAlgebraInfo.setTitle("Regular subalgebra");

        disAlgebraInfo.setTitle("Internal algebra");

        coAlgebraInfo.setTitle("Co-algebra (regular x internal)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dynkinDiagramPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(disAlgebraInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(algebraInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subAlgebraInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(coAlgebraInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dynkinDiagramPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(algebraInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subAlgebraInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coAlgebraInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(disAlgebraInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo algebraInfo;
    private edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo coAlgebraInfo;
    private edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo disAlgebraInfo;
    private edu.rug.hep.simplie.ui.DynkinDiagramPanel dynkinDiagramPanel;
    private edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo subAlgebraInfo;
    // End of variables declaration//GEN-END:variables
}
