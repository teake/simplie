/*
 * AlgebraSetup.java
 *
 * Created on 13 maart 2007, 15:05
 */

package edu.rug.hep.simplie.ui;

import edu.rug.hep.simplie.Globals;
import edu.rug.hep.simplie.group.CGroup;

import Jama.Matrix.*;

/**
 *
 * @author  Teake Nutma
 */
public class AlgebraSetup extends javax.swing.JPanel
{
	/** Creates new form LevelDecompositionUI */
	public AlgebraSetup()
	{
		initComponents();
		dynkinDiagramPanel.Initialize(this);
		Update();
		algebraInfo.SetTitle("Full algebra");
		subAlgebraInfo.SetTitle("Regular subalgebra");
		disAlgebraInfo.SetTitle("Disconnected subalgebra");
		coAlgebraInfo.SetTitle("Co-subalgebra (regular x disconnected)");
	}
	
	public void Update()
	{
		if(Globals.group == null || !Globals.sameMatrices(Globals.dd.cartanMatrix(), Globals.group.cartanMatrix))
			Globals.group	= new CGroup(Globals.dd.cartanMatrix());
		
		Globals.subGroup	= new CGroup(Globals.dd.cartanSubMatrix("sub"));
		Globals.disGroup	= new CGroup(Globals.dd.cartanSubMatrix("dis"));
		Globals.coGroup		= new CGroup(Globals.dd.cartanSubMatrix("co"));
		
		algebraInfo.Update(Globals.group);
		subAlgebraInfo.Update(Globals.subGroup);
		disAlgebraInfo.Update(Globals.disGroup);
		coAlgebraInfo.Update(Globals.coGroup);
	
		dynkinDiagramPanel.repaint();
		PanelDynkinDiagram.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dynkin Diagram of " + Globals.getDynkinDiagramType(), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        PanelDynkinDiagram = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dynkinDiagramPanel = new edu.rug.hep.simplie.ui.DynkinDiagramPanel();
        algebraInfo = new edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo();
        subAlgebraInfo = new edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo();
        disAlgebraInfo = new edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo();
        coAlgebraInfo = new edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo();

        PanelDynkinDiagram.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dynkin Diagram", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        jScrollPane1.setViewportView(dynkinDiagramPanel);

        javax.swing.GroupLayout PanelDynkinDiagramLayout = new javax.swing.GroupLayout(PanelDynkinDiagram);
        PanelDynkinDiagram.setLayout(PanelDynkinDiagramLayout);
        PanelDynkinDiagramLayout.setHorizontalGroup(
            PanelDynkinDiagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDynkinDiagramLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelDynkinDiagramLayout.setVerticalGroup(
            PanelDynkinDiagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDynkinDiagramLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelDynkinDiagram, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(disAlgebraInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(algebraInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subAlgebraInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(coAlgebraInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelDynkinDiagram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
    private javax.swing.JPanel PanelDynkinDiagram;
    private edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo algebraInfo;
    private edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo coAlgebraInfo;
    private edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo disAlgebraInfo;
    private edu.rug.hep.simplie.ui.DynkinDiagramPanel dynkinDiagramPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private edu.rug.hep.simplie.ui.reusable.UIAlgebraInfo subAlgebraInfo;
    // End of variables declaration//GEN-END:variables
}