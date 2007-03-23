/*
 * LevelDecomposition.java
 *
 * Created on 13 maart 2007, 16:43
 */

package tan.leveldecomposition.ui;

import tan.leveldecomposition.dynkindiagram.*;
import tan.leveldecomposition.leveldecomposer.*;
import tan.leveldecomposition.helper.*;

import java.util.*;
import javax.swing.table.*;
import java.awt.Cursor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.SwingWorker;


/**
 *
 * @author  Teake Nutma
 */
public class LevelDecomposition extends javax.swing.JPanel
{
    DefaultTableModel	tableModel;
    CLevelScanner	levelScanner;
    CAutoLevelScanner	autoScanner;
    
    boolean isBusy;
    
    /** Creates new form LevelDecomposition */
    public LevelDecomposition()
    {
	initComponents();
	
	autoScanMinLevel.SetLabel("Minimum level:");
	autoScanMaxLevel.SetLabel("Maximum level:");
	
	tableModel = (DefaultTableModel) representationsTable.getModel();
	representationsTable.setAutoCreateRowSorter(true);
	representationsTable.setModel(tableModel);
	
	levelScanner= new CLevelScanner();
	
	SetSignConvention();
	
	isBusy = false;
    }
    
    private void SetSignConvention()
    {
	if(signButtonPos.isSelected())
	    LevelHelper.SetSignConvention(1);
	else
	    LevelHelper.SetSignConvention(-1);
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
        representationsTable = new javax.swing.JTable();
        AutoScanPanel = new javax.swing.JPanel();
        bAutoScan = new javax.swing.JButton();
        cbLocked = new javax.swing.JCheckBox();
        autoScanMaxLevel = new tan.leveldecomposition.ui.reusable.UILevelTextfield();
        autoScanMinLevel = new tan.leveldecomposition.ui.reusable.UILevelTextfield();
        autoScanProgressBar = new javax.swing.JProgressBar();
        settingsPanel = new javax.swing.JPanel();
        tfSign = new javax.swing.JLabel();
        signButtonPos = new javax.swing.JRadioButton();
        signButtonNeg = new javax.swing.JRadioButton();

        RepresentationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Subalgebra Representations", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        representationsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "l", "p", "p deleted", "m", "root length"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
            };

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addContainerGap())
        );
        RepresentationPanelLayout.setVerticalGroup(
            RepresentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RepresentationPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
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

        cbLocked.setText("Lock maximum - minimum");
        cbLocked.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbLocked.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cbLocked.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                cbLockedStateChanged(evt);
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
                    .addComponent(cbLocked)
                    .addComponent(autoScanMaxLevel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(autoScanMinLevel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AutoScanPanelLayout.createSequentialGroup()
                        .addComponent(autoScanProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAutoScan, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        AutoScanPanelLayout.setVerticalGroup(
            AutoScanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AutoScanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbLocked)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autoScanMaxLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
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
        signButtonNeg.setSelected(true);
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

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tfSign)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(signButtonNeg)
                    .addComponent(signButtonPos))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(RepresentationPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
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
	if(isBusy)
	{
	    autoScanner.cancel(true);
	}
	else
	{
	    /** Clear the table. */
	    tableModel.setRowCount(0);
	    
	    /* Prepare the UI */
	    bAutoScan.setText("Cancel");
	    autoScanProgressBar.setIndeterminate(true);
	    autoScanProgressBar.setString("Scanning");
	    isBusy = true;
	    
	    /** Set up the scan */
	    autoScanner	= new CAutoLevelScanner(tableModel, levelScanner, autoScanMinLevel.GetValue(),autoScanMaxLevel.GetValue());
	    levelScanner.Initialize(autoScanner);
	    autoScanner.addPropertyChangeListener(new PropertyChangeListener()
	    {
		public void propertyChange(PropertyChangeEvent evt)
		{
		    if (autoScanner.getState().equals(SwingWorker.StateValue.DONE))
		    {
			bAutoScan.setText("Scan");
			isBusy = false;
			autoScanProgressBar.setIndeterminate(false);
			autoScanProgressBar.setString("Idle");
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
    private javax.swing.JCheckBox cbLocked;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable representationsTable;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.ButtonGroup signButtonGroup;
    private javax.swing.JRadioButton signButtonNeg;
    private javax.swing.JRadioButton signButtonPos;
    private javax.swing.JLabel tfSign;
    // End of variables declaration//GEN-END:variables
    
}
