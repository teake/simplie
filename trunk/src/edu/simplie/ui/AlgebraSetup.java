/*
 * AlgebraSetup.java
 *
 * Created on 13 maart 2007, 15:05
 *
 * This file is part of SimpLie.
 * 
 * SimpLie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SimpLie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SimpLie.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package edu.simplie.ui;

import edu.simplie.*;
import edu.simplie.dynkindiagram.*;

/**
 * Panel that displays a Dynkin diagram and information on the 4 derived algebras.
 * 
 * @author  Teake Nutma
 * @version $Revision$, $Date$
 */
public class AlgebraSetup extends javax.swing.JPanel implements DiagramListener
{
	private AlgebraComposite algebras;
	
	/** Creates new form LevelDecompositionUI */
	public AlgebraSetup()
	{
		initComponents();
	}
	
	public void setAlgebraComposite(AlgebraComposite algebras)
	{
		this.algebras = algebras;
		dynkinDiagramPanel.setDynkinDiagram(algebras.dd);
		algebras.dd.addListener(this);
	}
	
	public void diagramChanged()
	{
		algebraInfo.update(algebras.algebra);
		subAlgebraInfo.update(algebras.subAlgebra);
		disAlgebraInfo.update(algebras.intAlgebra);
		cartanMatrixTextArea.setText(Helper.matrixToString(algebras.algebra.A));
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        algebraInfo = new edu.simplie.ui.reusable.UIAlgebraInfo();
        subAlgebraInfo = new edu.simplie.ui.reusable.UIAlgebraInfo();
        disAlgebraInfo = new edu.simplie.ui.reusable.UIAlgebraInfo();
        dynkinDiagramPanel = new edu.simplie.ui.DynkinDiagramPanel();
        cartanMatrixPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cartanMatrixTextArea = new javax.swing.JTextArea();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(AlgebraSetup.class);
        algebraInfo.setToolTipText(resourceMap.getString("algebraSetup.fullTooltip")); // NOI18N
        algebraInfo.setTitle("Full algebra");

        subAlgebraInfo.setToolTipText(resourceMap.getString("algebraSetup.subTooltip")); // NOI18N
        subAlgebraInfo.setTitle("Regular subalgebra");

        disAlgebraInfo.setToolTipText(resourceMap.getString("algebraSetup.internalTooltip")); // NOI18N
        disAlgebraInfo.setTitle("Internal algebra");

        dynkinDiagramPanel.setMinimumSize(new java.awt.Dimension(0, 0));

        cartanMatrixPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cartan Matrix", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        cartanMatrixPanel.setToolTipText(resourceMap.getString("algebraSetup.cartanMatrixTooltip")); // NOI18N
        cartanMatrixPanel.setMinimumSize(new java.awt.Dimension(0, 0));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cartanMatrixTextArea.setColumns(20);
        cartanMatrixTextArea.setEditable(false);
        cartanMatrixTextArea.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        cartanMatrixTextArea.setRows(5);
        jScrollPane1.setViewportView(cartanMatrixTextArea);

        org.jdesktop.layout.GroupLayout cartanMatrixPanelLayout = new org.jdesktop.layout.GroupLayout(cartanMatrixPanel);
        cartanMatrixPanel.setLayout(cartanMatrixPanelLayout);
        cartanMatrixPanelLayout.setHorizontalGroup(
            cartanMatrixPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cartanMatrixPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );
        cartanMatrixPanelLayout.setVerticalGroup(
            cartanMatrixPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cartanMatrixPanelLayout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, dynkinDiagramPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(cartanMatrixPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(subAlgebraInfo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(disAlgebraInfo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(algebraInfo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 287, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {algebraInfo, disAlgebraInfo, subAlgebraInfo}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(dynkinDiagramPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(layout.createSequentialGroup()
                        .add(algebraInfo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(subAlgebraInfo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(disAlgebraInfo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(cartanMatrixPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private edu.simplie.ui.reusable.UIAlgebraInfo algebraInfo;
    private javax.swing.JPanel cartanMatrixPanel;
    private javax.swing.JTextArea cartanMatrixTextArea;
    private edu.simplie.ui.reusable.UIAlgebraInfo disAlgebraInfo;
    private edu.simplie.ui.DynkinDiagramPanel dynkinDiagramPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private edu.simplie.ui.reusable.UIAlgebraInfo subAlgebraInfo;
    // End of variables declaration//GEN-END:variables
}
