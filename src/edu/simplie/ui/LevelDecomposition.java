/*
 * LevelDecomposition.java
 *
 * Created on 13 maart 2007, 16:43
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
import edu.simplie.algebra.*;
import edu.simplie.leveldecomposer.AutoLevelScanner;

import java.util.*;
import javax.swing.table.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import org.jdesktop.swingworker.SwingWorker;
import edu.simplie.ui.reusable.UIPrintableColorTable;


/**
 * Windows for executing a level decomposition and vieuing its results.
 * 
 * @author  Teake Nutma
 * @version $Revision$, $Date$
 */
public class LevelDecomposition extends javax.swing.JPanel
{
	private DefaultTableModel	tableModelReps;
	private DefaultTableModel	tableModelWeights;
	private AutoLevelScanner	autoScanner;
	private AlgebraComposite	algebras;
	
	private long startTime;
	
	/** Creates new form LevelDecomposition */
	public LevelDecomposition()
	{
		initComponents();
		tableModelReps		= (javax.swing.table.DefaultTableModel) representationsTable.getModel();
		tableModelWeights	= (javax.swing.table.DefaultTableModel) weightsTable.getModel();
	}
	
	public void setAlgebraComposite(AlgebraComposite algebras)
	{
		this.algebras = algebras;
		this.algebras.setSignPos(rbSignPos.isSelected());
	}
	
	public UIPrintableColorTable getRepTable()
	{
		return representationsTable;
	}
	
	private void setSelectedRep(boolean onlyDominant)
	{
		if(representationsTable.getSelectedRowCount() != 1)
			return;
		int index = representationsTable.getSelectedRow();
		int[] rootVector = Helper.stringToIntArray((String) tableModelReps.getValueAt(
				index,
				3));
		int[] coDynkinLabels = algebras.coDynkinLabels(rootVector);
		int[] levels = algebras.levels(rootVector);
		HighestWeightRep hwRep = new HighestWeightRep(algebras.coAlgebra,coDynkinLabels);
		
		// Fully construct the weight system.
		hwRep.construct(0);
		
		// Clear and fill the table.
		tableModelWeights.setRowCount(0);
		
		for (int i = 0; i < hwRep.size(); i++)
		{
			Collection<Weight> weights = hwRep.get(i);
			Iterator iterator	= weights.iterator();
			while (iterator.hasNext())
			{
				Weight weight = (Weight) iterator.next();
				if(!weight.isDominant && onlyDominant)
					continue;
				int[] root = algebras.rootVector(levels, weight.dynkinLabels);
				Object[] rowData = new Object[4];
				rowData[0] = Helper.intArrayToString(root);
				rowData[1] = Helper.intArrayToString(algebras.subDynkinLabels(root));
				rowData[2] = Helper.intArrayToString(algebras.intDynkinLabels(root));
				rowData[3] = weight.getMult();
				tableModelWeights.addRow(rowData);
			}
		}
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        signButtonGroup = new javax.swing.ButtonGroup();
        AutoScanPanel = new javax.swing.JPanel();
        bAutoScan = new javax.swing.JButton();
        autoScanProgressBar = new javax.swing.JProgressBar();
        autoScanMaxLevel = new edu.simplie.ui.reusable.UINumTextfield();
        autoScanMinLevel = new edu.simplie.ui.reusable.UINumTextfield();
        settingsPanel = new javax.swing.JPanel();
        cbRootMult = new javax.swing.JCheckBox();
        cbZeroMultRep = new javax.swing.JCheckBox();
        cbRepMult = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        rbSignPos = new javax.swing.JRadioButton();
        rbSignNeg = new javax.swing.JRadioButton();
        cbZeroMultRoot = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        representationsTable = new edu.simplie.ui.reusable.UIPrintableColorTable();
        jPanel2 = new javax.swing.JPanel();
        bDominantWeights = new javax.swing.JButton();
        bAllWeights = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        weightsTable = new edu.simplie.ui.reusable.UIPrintableColorTable();

        AutoScanPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Scan levels", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        bAutoScan.setText("Scan");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(LevelDecomposition.class);
        bAutoScan.setToolTipText(resourceMap.getString("levelDecomp.scanTooltip")); // NOI18N
        bAutoScan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAutoScanActionPerformed(evt);
            }
        });

        autoScanProgressBar.setString("Idle");
        autoScanProgressBar.setStringPainted(true);

        autoScanMaxLevel.setToolTipText(resourceMap.getString("levelDecomp.maxLevelTooltip")); // NOI18N
        autoScanMaxLevel.setText("Max:"); // NOI18N

        autoScanMinLevel.setToolTipText(resourceMap.getString("levelDecomp.minLevelTooltip")); // NOI18N
        autoScanMinLevel.setText("Min:"); // NOI18N

        org.jdesktop.layout.GroupLayout AutoScanPanelLayout = new org.jdesktop.layout.GroupLayout(AutoScanPanel);
        AutoScanPanel.setLayout(AutoScanPanelLayout);
        AutoScanPanelLayout.setHorizontalGroup(
            AutoScanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(AutoScanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(AutoScanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(bAutoScan, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .add(autoScanProgressBar, 0, 0, Short.MAX_VALUE))
                .add(12, 12, 12)
                .add(AutoScanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(autoScanMaxLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(autoScanMinLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        AutoScanPanelLayout.setVerticalGroup(
            AutoScanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(AutoScanPanelLayout.createSequentialGroup()
                .add(AutoScanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(AutoScanPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(autoScanProgressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(bAutoScan))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, AutoScanPanelLayout.createSequentialGroup()
                        .add(autoScanMaxLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(autoScanMinLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        cbRootMult.setSelected(true);
        cbRootMult.setText("Calc root mults");
        cbRootMult.setToolTipText(resourceMap.getString("levelDecomp.calMultTooltip")); // NOI18N
        cbRootMult.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbZeroMultRep.setText("Show zero mu reps");
        cbZeroMultRep.setToolTipText(resourceMap.getString("levelDecomp.zeroMuTooltip")); // NOI18N
        cbZeroMultRep.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbRepMult.setSelected(true);
        cbRepMult.setText("Calc rep mults");
        cbRepMult.setToolTipText(resourceMap.getString("levelDecomp.calcMuTooltip")); // NOI18N
        cbRepMult.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel1.setText("Sign convention:");

        signButtonGroup.add(rbSignPos);
        rbSignPos.setText("p = + A m");
        rbSignPos.setToolTipText(resourceMap.getString("levelDecomp.signPosTooltip")); // NOI18N
        rbSignPos.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rbSignPos.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbSignPosStateChanged(evt);
            }
        });

        signButtonGroup.add(rbSignNeg);
        rbSignNeg.setSelected(true);
        rbSignNeg.setText("p = - A m");
        rbSignNeg.setToolTipText(resourceMap.getString("levelDecomp.signNegTooltip")); // NOI18N
        rbSignNeg.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbZeroMultRoot.setText("Show zero mult roots");
        cbZeroMultRoot.setToolTipText(resourceMap.getString("levelDecomp.zeroMultTooltip")); // NOI18N
        cbZeroMultRoot.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout settingsPanelLayout = new org.jdesktop.layout.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(settingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(settingsPanelLayout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rbSignPos)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(rbSignNeg))
                    .add(settingsPanelLayout.createSequentialGroup()
                        .add(settingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cbZeroMultRoot)
                            .add(cbZeroMultRep))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(settingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cbRepMult)
                            .add(cbRootMult))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        settingsPanelLayout.setVerticalGroup(
            settingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(settingsPanelLayout.createSequentialGroup()
                .add(settingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(rbSignPos)
                    .add(rbSignNeg))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(settingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(settingsPanelLayout.createSequentialGroup()
                        .add(cbZeroMultRoot)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cbZeroMultRep))
                    .add(settingsPanelLayout.createSequentialGroup()
                        .add(cbRootMult)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cbRepMult)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Subalgebra representations", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        representationsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "l", "p_r", "p_i", "vector", "a^2", "d_r", "d_i", "mult", "mu", "h"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        representationsTable.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(representationsTable);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Weights of selected representation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        bDominantWeights.setText("Dominant weights");
        bDominantWeights.setToolTipText(resourceMap.getString("levelDecomp.dominantTooltip")); // NOI18N
        bDominantWeights.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDominantWeightsActionPerformed(evt);
            }
        });

        bAllWeights.setText("All weights");
        bAllWeights.setToolTipText(resourceMap.getString("levelDecomp.allweightsTooltip")); // NOI18N
        bAllWeights.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAllWeightsActionPerformed(evt);
            }
        });

        weightsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "vector", "p_r", "p_i", "mult"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class
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
        jScrollPane2.setViewportView(weightsTable);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(bDominantWeights)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(bAllWeights)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(bAllWeights)
                    .add(bDominantWeights))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(settingsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(AutoScanPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {AutoScanPanel, settingsPanel}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(settingsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(AutoScanPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	
private void rbSignPosStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbSignPosStateChanged
	algebras.setSignPos(rbSignPos.isSelected());
}//GEN-LAST:event_rbSignPosStateChanged

    private void bAutoScanActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bAutoScanActionPerformed
    {//GEN-HEADEREND:event_bAutoScanActionPerformed
		if(algebras.coAlgebra == null)
			return;
		
		if(!(autoScanner == null || autoScanner.isDone()))
		{
			// The scanner is busy, and the button is a cancel button. So cancel the task.
			autoScanner.cancel(true);
			algebras.algebra.cancelEverything();
		}
		else
		{
			// Clear the tables.
			tableModelReps.setRowCount(0);
			tableModelWeights.setRowCount(0);
			
			// Prepare the UI:
			//  - Change the "scan" button into a "cancel" button.
			//  - Start the progressbar animation.
			//	- Set the rep table title.
			bAutoScan.setText("Cancel");
			autoScanProgressBar.setIndeterminate(true);
			autoScanProgressBar.setString("Scanning");
			algebras.setLocked(true);
			rbSignNeg.setEnabled(false);
			rbSignPos.setEnabled(false);
			jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null,algebras.getDecompositionType(false), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
			representationsTable.setTitleTeX(algebras.getDecompositionType(true));
			
			// Set up the scan.
			autoScanner = new AutoLevelScanner(
					algebras,
					cbRootMult.isSelected(),
					cbRepMult.isSelected(),
					cbZeroMultRoot.isSelected(),
					cbZeroMultRep.isSelected(),
					tableModelReps,
					autoScanMinLevel.getValue(),
					autoScanMaxLevel.getValue()
					
					);
			autoScanner.addPropertyChangeListener(new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent evt)
				{
					if (autoScanner.getState().equals(SwingWorker.StateValue.DONE) && algebras.dd.isLocked())
					{
						// Invoked when the scan is done (or canceled).
						bAutoScan.setText("Scan");
						autoScanProgressBar.setIndeterminate(false);
						autoScanProgressBar.setString("Idle");
						algebras.setLocked(false);
						rbSignNeg.setEnabled(true);
						rbSignPos.setEnabled(true);
						System.out.print("Finished level decomposition. Milliseconds: ");
						System.out.println(System.currentTimeMillis() - startTime);
					}
				}
			});
			
			// Start the scan.
			startTime = System.currentTimeMillis();
			System.out.println("Starting level decomposition");
			autoScanner.execute();
		}
    }//GEN-LAST:event_bAutoScanActionPerformed

	private void bDominantWeightsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bDominantWeightsActionPerformed
	{//GEN-HEADEREND:event_bDominantWeightsActionPerformed
		setSelectedRep(true);
}//GEN-LAST:event_bDominantWeightsActionPerformed

	private void bAllWeightsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bAllWeightsActionPerformed
	{//GEN-HEADEREND:event_bAllWeightsActionPerformed
		setSelectedRep(false);
}//GEN-LAST:event_bAllWeightsActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AutoScanPanel;
    private edu.simplie.ui.reusable.UINumTextfield autoScanMaxLevel;
    private edu.simplie.ui.reusable.UINumTextfield autoScanMinLevel;
    private javax.swing.JProgressBar autoScanProgressBar;
    private javax.swing.JButton bAllWeights;
    private javax.swing.JButton bAutoScan;
    private javax.swing.JButton bDominantWeights;
    private javax.swing.JCheckBox cbRepMult;
    private javax.swing.JCheckBox cbRootMult;
    private javax.swing.JCheckBox cbZeroMultRep;
    private javax.swing.JCheckBox cbZeroMultRoot;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rbSignNeg;
    private javax.swing.JRadioButton rbSignPos;
    private edu.simplie.ui.reusable.UIPrintableColorTable representationsTable;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.ButtonGroup signButtonGroup;
    private edu.simplie.ui.reusable.UIPrintableColorTable weightsTable;
    // End of variables declaration//GEN-END:variables
	
}
