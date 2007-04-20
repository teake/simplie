/*
 * LevelDecomposition.java
 *
 * Created on 13 maart 2007, 16:43
 */

package tan.leveldecomposition.ui;

import tan.leveldecomposition.dynkindiagram.*;
import tan.leveldecomposition.leveldecomposer.*;
import tan.leveldecomposition.*;

import java.util.*;
import javax.swing.JTable;
import javax.swing.table.*;
import java.awt.print.PrinterException;
import java.awt.Cursor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.SwingWorker;
import java.text.MessageFormat;
import tan.leveldecomposition.ui.reusable.UIPrintableColorTable;


/**
 *
 * @author  Teake Nutma
 */
public class LevelDecomposition extends javax.swing.JPanel
{
	DefaultTableModel	tableModel;
	CAutoLevelScanner	autoScanner;
	
	public UIPrintableColorTable repTable;
	
	/** Creates new form LevelDecomposition */
	public LevelDecomposition()
	{
		initComponents();
		
		autoScanMinLevel.SetLabel("Minimum level:");
		autoScanMaxLevel.SetLabel("Maximum level:");
		
		tableModel = (DefaultTableModel) representationsTable.getModel();
		representationsTable.setAutoCreateRowSorter(true);
		representationsTable.setModel(tableModel);
		
		repTable = representationsTable;
		
		SetSignConvention();
	}
	
	public void printTable()
	{
		try
		{
			MessageFormat footer = new MessageFormat("Page {0}");
			MessageFormat header = new MessageFormat(Globals.getDecompositionType());
			representationsTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
		}
		catch (PrinterException e)
		{
			e.printStackTrace();
		}
	}
	
	private void SetSignConvention()
	{
		if(signButtonPos.isSelected())
			LevelHelper.setSignConvention(1);
		else
			LevelHelper.setSignConvention(-1);
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
        RepresentationPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        representationsTable = new tan.leveldecomposition.ui.reusable.UIPrintableColorTable();
        AutoScanPanel = new javax.swing.JPanel();
        bAutoScan = new javax.swing.JButton();
        autoScanMaxLevel = new tan.leveldecomposition.ui.reusable.UILevelTextfield();
        autoScanMinLevel = new tan.leveldecomposition.ui.reusable.UILevelTextfield();
        autoScanProgressBar = new javax.swing.JProgressBar();
        settingsPanel = new javax.swing.JPanel();
        tfSign = new javax.swing.JLabel();
        signButtonPos = new javax.swing.JRadioButton();
        signButtonNeg = new javax.swing.JRadioButton();
        cbMultiplicities = new javax.swing.JCheckBox();
        cbLocked = new javax.swing.JCheckBox();
        cbZeroMult = new javax.swing.JCheckBox();
        cbFlipDynkinLabels = new javax.swing.JCheckBox();

        RepresentationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Subalgebra Representations", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        representationsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "l", "p_s", "p_d", "m", "a^2", "d_s", "d_d", "mult", "mu", "nu", "h", "ind"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Integer.class
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
        representationsTable.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(representationsTable);

        javax.swing.GroupLayout RepresentationPanelLayout = new javax.swing.GroupLayout(RepresentationPanel);
        RepresentationPanel.setLayout(RepresentationPanelLayout);
        RepresentationPanelLayout.setHorizontalGroup(
            RepresentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RepresentationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addContainerGap())
        );
        RepresentationPanelLayout.setVerticalGroup(
            RepresentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RepresentationPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addContainerGap())
        );

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

        javax.swing.GroupLayout AutoScanPanelLayout = new javax.swing.GroupLayout(AutoScanPanel);
        AutoScanPanel.setLayout(AutoScanPanelLayout);
        AutoScanPanelLayout.setHorizontalGroup(
            AutoScanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AutoScanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AutoScanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autoScanMaxLevel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(autoScanMinLevel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(AutoScanPanelLayout.createSequentialGroup()
                        .addComponent(autoScanProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAutoScan, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        AutoScanPanelLayout.setVerticalGroup(
            AutoScanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AutoScanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(autoScanMaxLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autoScanMinLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AutoScanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autoScanProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(bAutoScan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        settingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        tfSign.setText("Sign convention:");

        signButtonGroup.add(signButtonPos);
        signButtonPos.setSelected(true);
        signButtonPos.setText("p_i = + A_ij m^j");
        signButtonPos.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        signButtonPos.setMargin(new java.awt.Insets(0, 0, 0, 0));
        signButtonPos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                signButtonPosActionPerformed(evt);
            }
        });

        signButtonGroup.add(signButtonNeg);
        signButtonNeg.setText("p_i = - A_ij m^j");
        signButtonNeg.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        signButtonNeg.setMargin(new java.awt.Insets(0, 0, 0, 0));
        signButtonNeg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                signButtonNegActionPerformed(evt);
            }
        });

        cbMultiplicities.setSelected(true);
        cbMultiplicities.setText("Calculate root & rep multiplicities");
        cbMultiplicities.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbMultiplicities.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbLocked.setText("Lock maximum - minimum level");
        cbLocked.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbLocked.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cbLocked.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                cbLockedStateChanged(evt);
            }
        });

        cbZeroMult.setText("Show reps with zero multiplicity");
        cbZeroMult.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbZeroMult.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbFlipDynkinLabels.setSelected(true);
        cbFlipDynkinLabels.setText("Flip Dynkin labels");
        cbFlipDynkinLabels.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbFlipDynkinLabels.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addComponent(tfSign)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(signButtonNeg)
                            .addComponent(signButtonPos)))
                    .addComponent(cbMultiplicities)
                    .addComponent(cbZeroMult)
                    .addComponent(cbFlipDynkinLabels)
                    .addComponent(cbLocked))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        settingsPanelLayout.setVerticalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfSign)
                    .addComponent(signButtonPos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(signButtonNeg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbMultiplicities)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbZeroMult)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbFlipDynkinLabels)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbLocked)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AutoScanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RepresentationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {AutoScanPanel, settingsPanel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RepresentationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AutoScanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	
    private void cbLockedStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_cbLockedStateChanged
    {//GEN-HEADEREND:event_cbLockedStateChanged
		if(cbLocked.isSelected())
		{
			autoScanMaxLevel.LinkTo(autoScanMinLevel);
			autoScanMinLevel.LinkTo(autoScanMaxLevel);
		}
		else
		{
			autoScanMaxLevel.LinkTo(null);
			autoScanMinLevel.LinkTo(null);
		}
    }//GEN-LAST:event_cbLockedStateChanged
	
    private void signButtonNegActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_signButtonNegActionPerformed
    {//GEN-HEADEREND:event_signButtonNegActionPerformed
		SetSignConvention();
    }//GEN-LAST:event_signButtonNegActionPerformed
	
    private void signButtonPosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_signButtonPosActionPerformed
    {//GEN-HEADEREND:event_signButtonPosActionPerformed
		SetSignConvention();
    }//GEN-LAST:event_signButtonPosActionPerformed
	
    private void bAutoScanActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bAutoScanActionPerformed
    {//GEN-HEADEREND:event_bAutoScanActionPerformed
		if(!(autoScanner == null || autoScanner.isDone()))
		{
			/** The scanner is busy, and the button is a cancel button. So cancel the task. */
			autoScanner.cancel(true);
			Globals.group.cancelEverything();
		}
		else
		{
			/** Clear the table. */
			tableModel.setRowCount(0);
			
			/**
			 * Prepare the UI:
			 *  - Change the "scan" button into a "cancel" button.
			 *  - Start the progressbar animation.
			 */
			RepresentationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Globals.getDecompositionType(), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
			bAutoScan.setText("Cancel");
			autoScanProgressBar.setIndeterminate(true);
			autoScanProgressBar.setString("Scanning");
			Globals.scanning = true;
			
			/** Set up the scan */
			autoScanner	= new CAutoLevelScanner(
					cbMultiplicities.isSelected(),
					cbZeroMult.isSelected(), 
					cbFlipDynkinLabels.isSelected(), 
					tableModel, 
					autoScanMinLevel.GetValue(),
					autoScanMaxLevel.GetValue()
					);
			autoScanner.addPropertyChangeListener(new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent evt)
				{
					if (autoScanner.getState().equals(SwingWorker.StateValue.DONE) && Globals.scanning)
					{
						/** Invoked when the scan is done (or canceled). */
						bAutoScan.setText("Scan");
						autoScanProgressBar.setIndeterminate(false);
						autoScanProgressBar.setString("Idle");
						Globals.scanning = false;
					}
				}
			});
			
			/* Start the scan */
			autoScanner.execute();
		}
    }//GEN-LAST:event_bAutoScanActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AutoScanPanel;
    private javax.swing.JPanel RepresentationPanel;
    private tan.leveldecomposition.ui.reusable.UILevelTextfield autoScanMaxLevel;
    private tan.leveldecomposition.ui.reusable.UILevelTextfield autoScanMinLevel;
    private javax.swing.JProgressBar autoScanProgressBar;
    private javax.swing.JButton bAutoScan;
    private javax.swing.JCheckBox cbFlipDynkinLabels;
    private javax.swing.JCheckBox cbLocked;
    private javax.swing.JCheckBox cbMultiplicities;
    private javax.swing.JCheckBox cbZeroMult;
    private javax.swing.JScrollPane jScrollPane1;
    private tan.leveldecomposition.ui.reusable.UIPrintableColorTable representationsTable;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.ButtonGroup signButtonGroup;
    private javax.swing.JRadioButton signButtonNeg;
    private javax.swing.JRadioButton signButtonPos;
    private javax.swing.JLabel tfSign;
    // End of variables declaration//GEN-END:variables
	
}
