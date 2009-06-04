/*
 * AlgebraInfo.java
 *
 * Created on 18 mei 2007, 15:18
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

import edu.simplie.dynkindiagram.*;
import edu.simplie.algebra.*;
import edu.simplie.*;

import javax.swing.table.*;
import java.util.Collection;
import java.util.Iterator;


/**
 * Displays information on a composite of algebras.
 * The information contains the (symmetrized / inverse) Cartan matrix, 
 * the root space metric, the root system, and arbitrary highest
 * weight representations.
 * 
 * @see		AlgebraComposite
 * @author  Teake Nutma
 * @version $Revision$, $Date$
 */
public class AlgebraInfo extends javax.swing.JPanel implements DiagramListener
{
	private DefaultTableModel tableModelRoots;
	private Algebra algebra;
	private AlgebraComposite algebras;
	
	/** Creates new form AlgebraInfo */
	public AlgebraInfo()
	{
		initComponents();
		tableModelRoots = (DefaultTableModel) rootTable.getModel();
		algebra = null;
	}
	
	/** Sets the composite of algebras we want to view info of. */
	public void setAlgebraComposite(AlgebraComposite algebras)
	{
		this.algebras = algebras;
		algebras.dd.addListener(this);
	}
	
	public void diagramChanged()
	{
		switch(algebrasBox.getSelectedIndex())
		{
		case 0:
			algebra = algebras.algebra;
			break;
		case 1:
			algebra = algebras.subAlgebra;
			break;
		case 2:
			algebra = algebras.intAlgebra;
			break;
		default:
			algebra = null;
		}
		
		if(algebra == null)
			return;
		
		constructedHeight.setText(Helper.intToString(algebra.rs.constructedHeight()));
		numPosRoots.setText(Helper.intToString((int) algebra.rs.numPosRoots()));

		String matrix;
		switch(matrixBox.getSelectedIndex())
		{
			case 0:
				matrix = Helper.matrixToString(algebra.A);
				break;
			case 1:
				matrix = Helper.matrixToString(algebra.symA);
				break;
			case 2:
				matrix = Helper.matrixToString(algebra.B);
				break;
			case 3:
				matrix = Helper.matrixToString(algebra.invA);
				break;
			case 4:
				matrix = Helper.matrixToString(algebra.G);
				break;
			default:
				matrix = "";
		}
		tfMatrix.setText(matrix);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        matrixBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tfMatrix = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        rootTable = new edu.simplie.ui.reusable.UIPrintableColorTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        numPosRoots = new javax.swing.JLabel();
        constructedHeight = new javax.swing.JLabel();
        fillRootTable = new javax.swing.JButton();
        algebrasBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();

        tabbedPane.setMinimumSize(new java.awt.Dimension(0, 0));

        matrixBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cartan matrix", "Symmetrized Cartan matrix", "Root space metric", "Inverse of Cartan matrix", "Quadratic form matrix" }));
        matrixBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                matrixBoxActionPerformed(evt);
            }
        });

        jLabel4.setText("Select matrix:");

        tfMatrix.setColumns(20);
        tfMatrix.setRows(5);
        jScrollPane1.setViewportView(tfMatrix);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(matrixBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(matrixBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Matrices", jPanel1);

        rootTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Vector", "Dynkin labels", "Norm", "Multiplicity", "CoMultiplicity", "Height"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Long.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        rootTable.setColumnSelectionAllowed(true);
        jScrollPane2.setViewportView(rootTable);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Roots info", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel1.setText("Constructed height:"); // NOI18N

        jLabel2.setText("Number positive roots:"); // NOI18N

        numPosRoots.setText("    "); // NOI18N

        constructedHeight.setText("    "); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(constructedHeight)
                    .add(numPosRoots))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(constructedHeight))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(numPosRoots))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fillRootTable.setText("Fill root table"); // NOI18N
        fillRootTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillRootTableActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fillRootTable)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(fillRootTable))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Roots", jPanel9);

        algebrasBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Full algebra", "Regular subalgebra", "Internal algebra" }));
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(AlgebraInfo.class);
        algebrasBox.setToolTipText(resourceMap.getString("algebraInfo.selectTooltip")); // NOI18N
        algebrasBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                algebrasBoxActionPerformed(evt);
            }
        });

        jLabel3.setText("Select algebra:"); // NOI18N
        jLabel3.setToolTipText(resourceMap.getString("algebraInfo.selectTooltip")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(algebrasBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(algebrasBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
				
	private void algebrasBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_algebrasBoxActionPerformed
	{//GEN-HEADEREND:event_algebrasBoxActionPerformed
		diagramChanged();
}//GEN-LAST:event_algebrasBoxActionPerformed
	
	private void fillRootTableActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fillRootTableActionPerformed
	{//GEN-HEADEREND:event_fillRootTableActionPerformed
		// Set the focus to the root table.
		tabbedPane.setSelectedIndex(1);
		
		if(algebra == null)
			return;
		
		// Clear and fill the table.
		tableModelRoots.setRowCount(0);
		
		for (int i = 0; i < algebra.rs.size(); i++)
		{
			Collection roots	= algebra.rs.get(i);
			Iterator iterator	= roots.iterator();
			while (iterator.hasNext())
			{
				Root root = (Root) iterator.next();
				Object[] rowData = new Object[6];
				rowData[0] = Helper.intArrayToString(root.vector);
				rowData[1] = Helper.intArrayToString(algebra.rootToWeight(root.vector));
				rowData[2] = root.norm;
				rowData[3] = root.mult;
				rowData[4] = root.coMult;
				rowData[5] = root.height();
				tableModelRoots.addRow(rowData);
			}
		}
	}//GEN-LAST:event_fillRootTableActionPerformed

	private void matrixBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_matrixBoxActionPerformed
	{//GEN-HEADEREND:event_matrixBoxActionPerformed
		diagramChanged();
	}//GEN-LAST:event_matrixBoxActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox algebrasBox;
    private javax.swing.JLabel constructedHeight;
    private javax.swing.JButton fillRootTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox matrixBox;
    private javax.swing.JLabel numPosRoots;
    private edu.simplie.ui.reusable.UIPrintableColorTable rootTable;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTextArea tfMatrix;
    // End of variables declaration//GEN-END:variables
	
	}
