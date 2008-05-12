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
	private DefaultTableModel tableModelWeights;
	private Algebra algebra;
	private AlgebraComposite algebras;
	private HighestWeightRep HWrep;
	
	/** Creates new form AlgebraInfo */
	public AlgebraInfo()
	{
		initComponents();
		tableModelRoots = (DefaultTableModel) rootTable.getModel();
		tableModelWeights = (DefaultTableModel) repTable.getModel();
		algebra = null;
		HWrep = null;
	}
	
	/** Sets the composite of algebras we want to view info of. */
	public void setAlgebraComposite(AlgebraComposite algebras)
	{
		this.algebras	= algebras;
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
		
		symCartanMatrix.setText(Helper.matrixToString(algebra.symA));
		cartanMatrixInverse.setText(Helper.matrixToString(algebra.invA));
		qFormMatrix.setText(Helper.matrixToString(algebra.G));
		rootSpaceMetric.setText(Helper.matrixToString(algebra.B));
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
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        symCartanMatrix = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        qFormMatrix = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        rootSpaceMetric = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        cartanMatrixInverse = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        rootTable = new edu.simplie.ui.reusable.UIPrintableColorTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        numPosRoots = new javax.swing.JLabel();
        constructedHeight = new javax.swing.JLabel();
        fillRootTable = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lDynkinLabels = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lDimRep = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lHeight = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        repTable = new edu.simplie.ui.reusable.UIPrintableColorTable();
        jPanel12 = new javax.swing.JPanel();
        tfDynkinLabels = new javax.swing.JTextField();
        repOKbutton = new javax.swing.JButton();
        cbDominant = new javax.swing.JCheckBox();
        repFillButton = new javax.swing.JButton();
        algebrasBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();

        tabbedPane.setMinimumSize(new java.awt.Dimension(0, 0));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Symmetrized Cartan matrix", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        symCartanMatrix.setColumns(20);
        symCartanMatrix.setEditable(false);
        symCartanMatrix.setFont(new java.awt.Font("Monospaced", 0, 11));
        symCartanMatrix.setRows(5);
        jScrollPane3.setViewportView(symCartanMatrix);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quadratic form matrix", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        qFormMatrix.setColumns(20);
        qFormMatrix.setEditable(false);
        qFormMatrix.setFont(new java.awt.Font("Monospaced", 0, 11));
        qFormMatrix.setRows(5);
        jScrollPane4.setViewportView(qFormMatrix);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Root space metric", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        rootSpaceMetric.setColumns(20);
        rootSpaceMetric.setEditable(false);
        rootSpaceMetric.setFont(new java.awt.Font("Monospaced", 0, 11));
        rootSpaceMetric.setRows(5);
        jScrollPane6.setViewportView(rootSpaceMetric);

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inverse of Cartan matrix", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        cartanMatrixInverse.setColumns(20);
        cartanMatrixInverse.setEditable(false);
        cartanMatrixInverse.setFont(new java.awt.Font("Monospaced", 0, 11));
        cartanMatrixInverse.setRows(5);
        jScrollPane5.setViewportView(cartanMatrixInverse);

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(6, 6, 6)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabbedPane.addTab("Matrices", jPanel1);

        rootTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Vector", "Norm", "Multiplicity", "CoMultiplicity", "Height"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Long.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
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
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Roots", jPanel9);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Representation info", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel6.setText("Dynkin labels:"); // NOI18N

        lDynkinLabels.setText("     "); // NOI18N

        jLabel5.setText("Dimension of rep:"); // NOI18N

        lDimRep.setText("    "); // NOI18N

        jLabel7.setText("Highest weight height:"); // NOI18N

        lHeight.setText("     "); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel7)
                    .add(jLabel5)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lDynkinLabels, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .add(lDimRep, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .add(lHeight, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel11Layout.createSequentialGroup()
                .add(jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lDynkinLabels)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(lDimRep))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(lHeight))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        repTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Labels", "Multiplicity", "Dimension", "Depth"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(repTable);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Enter Dynkin labels", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(AlgebraInfo.class);
        tfDynkinLabels.setToolTipText(resourceMap.getString("algebraInfo.enterLabelsTooltip")); // NOI18N

        repOKbutton.setText("OK"); // NOI18N
        repOKbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repOKbuttonActionPerformed(evt);
            }
        });

        cbDominant.setText("Only dominant weights"); // NOI18N
        cbDominant.setMargin(new java.awt.Insets(0, 0, 0, 0));

        repFillButton.setText("Fill weight table"); // NOI18N
        repFillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repFillButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(tfDynkinLabels, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .add(cbDominant, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(repOKbutton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(repFillButton))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel12Layout.createSequentialGroup()
                .add(jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(tfDynkinLabels, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(repOKbutton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cbDominant)
                    .add(repFillButton))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(jPanel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel12, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Representations", jPanel10);

        algebrasBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Full algebra", "Regular subalgebra", "Internal algebra" }));
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
                    .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(algebrasBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
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
                .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	
private void repFillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repFillButtonActionPerformed
	if(HWrep == null || algebra == null)
		return;
	
	// Fully construct the weight system.
	HWrep.construct(0);
	
	// Clear and fill the table.
	tableModelWeights.setRowCount(0);
	
	for (int i = 0; i < HWrep.size(); i++)
	{
		Collection<Weight> weights = HWrep.get(i);
		Iterator iterator	= weights.iterator();
		while (iterator.hasNext())
		{
			Weight weight = (Weight) iterator.next();
			if(cbDominant.isSelected() && !weight.isDominant)
				continue;
			Object[] rowData = new Object[4];
			rowData[0] = Helper.intArrayToString(weight.dynkinLabels);
			rowData[1] = weight.getMult();
			rowData[2] = (weight.isDominant) ? algebra.dimOfRep(weight.dynkinLabels) : 0;
			rowData[3] = weight.getDepth();
			tableModelWeights.addRow(rowData);
		}
	}
}//GEN-LAST:event_repFillButtonActionPerformed
		
	private void repOKbuttonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_repOKbuttonActionPerformed
	{//GEN-HEADEREND:event_repOKbuttonActionPerformed
		int[] labels = Helper.stringToIntArray(tfDynkinLabels.getText());
		if(algebra != null && labels.length == algebra.rank)
		{
			HWrep = new HighestWeightRep(algebra,labels);
			lDynkinLabels.setText(Helper.intArrayToString(labels));
			Long dim = new Long(HWrep.dim);
			lDimRep.setText(dim.toString());
			lHeight.setText(HWrep.highestHeight.toString());
		}
		
}//GEN-LAST:event_repOKbuttonActionPerformed
	
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
				Object[] rowData = new Object[5];
				rowData[0] = Helper.intArrayToString(root.vector);
				rowData[1] = root.norm;
				rowData[2] = root.mult;
				rowData[3] = root.coMult;
				rowData[4] = root.height();
				tableModelRoots.addRow(rowData);
			}
		}
	}//GEN-LAST:event_fillRootTableActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox algebrasBox;
    private javax.swing.JTextArea cartanMatrixInverse;
    private javax.swing.JCheckBox cbDominant;
    private javax.swing.JLabel constructedHeight;
    private javax.swing.JButton fillRootTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lDimRep;
    private javax.swing.JLabel lDynkinLabels;
    private javax.swing.JLabel lHeight;
    private javax.swing.JLabel numPosRoots;
    private javax.swing.JTextArea qFormMatrix;
    private javax.swing.JButton repFillButton;
    private javax.swing.JButton repOKbutton;
    private edu.simplie.ui.reusable.UIPrintableColorTable repTable;
    private javax.swing.JTextArea rootSpaceMetric;
    private edu.simplie.ui.reusable.UIPrintableColorTable rootTable;
    private javax.swing.JTextArea symCartanMatrix;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTextField tfDynkinLabels;
    // End of variables declaration//GEN-END:variables
	
	}
