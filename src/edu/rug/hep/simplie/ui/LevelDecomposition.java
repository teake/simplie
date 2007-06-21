/*
 * LevelDecomposition.java
 *
 * Created on 13 maart 2007, 16:43
 */

package edu.rug.hep.simplie.ui;

import edu.rug.hep.simplie.*;
import edu.rug.hep.simplie.group.*;
import edu.rug.hep.simplie.leveldecomposer.CAutoLevelScanner;

import java.util.*;
import javax.swing.JTable;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.SwingWorker;
import java.text.MessageFormat;
import edu.rug.hep.simplie.ui.reusable.UIPrintableColorTable;


/**
 *
 * @author  Teake Nutma
 */
public class LevelDecomposition extends javax.swing.JPanel
{
	private DefaultTableModel	tableModelReps;
	private DefaultTableModel	tableModelWeights;
	private CAutoLevelScanner	autoScanner;
	private CAlgebraComposite	algebras;
	
	private long startTime;
	
	/** Creates new form LevelDecomposition */
	public LevelDecomposition()
	{
		initComponents();
		tableModelReps		= (javax.swing.table.DefaultTableModel) representationsTable.getModel();
		tableModelWeights	= (javax.swing.table.DefaultTableModel) weightsTable.getModel();
		// Listen for selection changes in the rep table.
		representationsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent evt)
			{
				if(evt.getValueIsAdjusting())
					return;
				if(representationsTable.getSelectedRowCount() != 1)
					return;
				int index = representationsTable.getSelectedRow();
				int[] vector = Helper.stringToIntArray((String) tableModelReps.getValueAt(index,3));
				setSelectedRep(vector);
			}
		});
	}
	
	public void setAlgebraComposite(CAlgebraComposite algebras)
	{
		this.algebras = algebras;
		this.algebras.setSignPos(rbSignPos.isSelected());
	}
	
	public void printTable()
	{
		try
		{
			MessageFormat footer = new MessageFormat("Page {0}");
			MessageFormat header = new MessageFormat(algebras.getDecompositionType());
			representationsTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
		}
		catch (PrinterException e)
		{
			e.printStackTrace();
		}
	}
	
	public UIPrintableColorTable getRepTable()
	{
		return representationsTable;
	}
	
	private void setSelectedRep(int[] rootVector)
	{
		int[] coDynkinLabels = algebras.coDynkinLabels(rootVector);
		int[] levels = algebras.levels(rootVector);
 		CHighestWeightRep hwRep = new CHighestWeightRep(algebras.coGroup,coDynkinLabels);
		
		// Fully construct the weight system.
		hwRep.construct(0);
		
		// Clear and fill the table.
		tableModelWeights.setRowCount(0);
		
		for (int i = 0; i < hwRep.size(); i++)
		{
			Collection<CWeight> weights = hwRep.get(i);
			Iterator iterator	= weights.iterator();
			while (iterator.hasNext())
			{
				CWeight weight = (CWeight) iterator.next();
				if(!weight.isDominant && !cbAllWeights.isSelected())
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
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        signButtonGroup = new javax.swing.ButtonGroup();
        AutoScanPanel = new javax.swing.JPanel();
        bAutoScan = new javax.swing.JButton();
        autoScanProgressBar = new javax.swing.JProgressBar();
        autoScanMaxLevel = new edu.rug.hep.simplie.ui.reusable.UILevelTextfield();
        autoScanMinLevel = new edu.rug.hep.simplie.ui.reusable.UILevelTextfield();
        settingsPanel = new javax.swing.JPanel();
        cbRootMult = new javax.swing.JCheckBox();
        cbZeroMultRep = new javax.swing.JCheckBox();
        cbRepMult = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        rbSignPos = new javax.swing.JRadioButton();
        rbSignNeg = new javax.swing.JRadioButton();
        cbExotic = new javax.swing.JCheckBox();
        cbZeroMultRoot = new javax.swing.JCheckBox();
        cbAllWeights = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        representationsTable = new edu.rug.hep.simplie.ui.reusable.UIPrintableColorTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        weightsTable = new edu.rug.hep.simplie.ui.reusable.UIPrintableColorTable();

        AutoScanPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Scan levels", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        bAutoScan.setText("Scan");
        bAutoScan.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bAutoScanActionPerformed(evt);
            }
        });

        autoScanProgressBar.setString("Idle");
        autoScanProgressBar.setStringPainted(true);

        autoScanMaxLevel.setText("Maximum level:");

        autoScanMinLevel.setText("Minimum level:");

        javax.swing.GroupLayout AutoScanPanelLayout = new javax.swing.GroupLayout(AutoScanPanel);
        AutoScanPanel.setLayout(AutoScanPanelLayout);
        AutoScanPanelLayout.setHorizontalGroup(
            AutoScanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AutoScanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AutoScanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AutoScanPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(autoScanProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAutoScan, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(autoScanMinLevel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addComponent(autoScanMaxLevel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                .addContainerGap())
        );
        AutoScanPanelLayout.setVerticalGroup(
            AutoScanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AutoScanPanelLayout.createSequentialGroup()
                .addComponent(autoScanMaxLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autoScanMinLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AutoScanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bAutoScan)
                    .addComponent(autoScanProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        cbRootMult.setSelected(true);
        cbRootMult.setText("Calc root mults");
        cbRootMult.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbRootMult.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbZeroMultRep.setSelected(true);
        cbZeroMultRep.setText("Show 0 mu reps");
        cbZeroMultRep.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbZeroMultRep.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbRepMult.setSelected(true);
        cbRepMult.setText("Calc rep mults");
        cbRepMult.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbRepMult.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel1.setText("Sign convention:");

        signButtonGroup.add(rbSignPos);
        rbSignPos.setText("p = + A m");
        rbSignPos.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbSignPos.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rbSignPos.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                rbSignPosStateChanged(evt);
            }
        });

        signButtonGroup.add(rbSignNeg);
        rbSignNeg.setSelected(true);
        rbSignNeg.setText("p = - A m");
        rbSignNeg.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbSignNeg.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbExotic.setSelected(true);
        cbExotic.setText("Show > D indices reps");
        cbExotic.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbExotic.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbZeroMultRoot.setText("Show zero mult roots");
        cbZeroMultRoot.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbZeroMultRoot.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbAllWeights.setText("All weights");
        cbAllWeights.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbAllWeights.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbSignPos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbSignNeg))
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbZeroMultRoot)
                            .addComponent(cbZeroMultRep)
                            .addComponent(cbExotic))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(settingsPanelLayout.createSequentialGroup()
                                .addComponent(cbAllWeights)
                                .addGap(20, 20, 20))
                            .addComponent(cbRootMult)
                            .addComponent(cbRepMult))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        settingsPanelLayout.setVerticalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(rbSignPos)
                    .addComponent(rbSignNeg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addComponent(cbZeroMultRoot)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbZeroMultRep)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbExotic))
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addComponent(cbAllWeights)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbRootMult)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbRepMult)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Subalgebra representations", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        representationsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "l", "p_r", "p_i", "vector", "a^2", "d_r", "d_i", "mult", "mu", "h", "h'", "ind"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        representationsTable.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(representationsTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Weights of selected representation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        weightsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "vector", "p_r", "p_i", "mult"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(weightsTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(AutoScanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(settingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {AutoScanPanel, settingsPanel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AutoScanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	
private void rbSignPosStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbSignPosStateChanged
	algebras.setSignPos(rbSignPos.isSelected());
}//GEN-LAST:event_rbSignPosStateChanged
	
    private void bAutoScanActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bAutoScanActionPerformed
    {//GEN-HEADEREND:event_bAutoScanActionPerformed
		if(!(autoScanner == null || autoScanner.isDone()))
		{
			// The scanner is busy, and the button is a cancel button. So cancel the task.
			autoScanner.cancel(true);
			algebras.group.cancelEverything();
		}
		else
		{
			// Clear the tables.
			tableModelReps.setRowCount(0);
			tableModelWeights.setRowCount(0);
			
			// Prepare the UI:
			//  - Change the "scan" button into a "cancel" button.
			//  - Start the progressbar animation.
			bAutoScan.setText("Cancel");
			autoScanProgressBar.setIndeterminate(true);
			autoScanProgressBar.setString("Scanning");
			algebras.setLocked(true);
			
			// Set up the scan.
			autoScanner = new CAutoLevelScanner(
					algebras,
					cbRootMult.isSelected(),
					cbRepMult.isSelected(),
					cbZeroMultRoot.isSelected(),
					cbZeroMultRep.isSelected(),
					cbExotic.isSelected(),
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
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AutoScanPanel;
    private edu.rug.hep.simplie.ui.reusable.UILevelTextfield autoScanMaxLevel;
    private edu.rug.hep.simplie.ui.reusable.UILevelTextfield autoScanMinLevel;
    private javax.swing.JProgressBar autoScanProgressBar;
    private javax.swing.JButton bAutoScan;
    private javax.swing.JCheckBox cbAllWeights;
    private javax.swing.JCheckBox cbExotic;
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
    private edu.rug.hep.simplie.ui.reusable.UIPrintableColorTable representationsTable;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.ButtonGroup signButtonGroup;
    private edu.rug.hep.simplie.ui.reusable.UIPrintableColorTable weightsTable;
    // End of variables declaration//GEN-END:variables
	
}
