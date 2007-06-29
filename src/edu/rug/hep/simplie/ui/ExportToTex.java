/*
 * ExportToTex.java
 *
 * Created on 19 april 2007, 16:47
 */

package edu.rug.hep.simplie.ui;

import edu.rug.hep.simplie.*;
import java.awt.datatransfer.Clipboard;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import edu.rug.hep.simplie.ui.reusable.UIPrintableColorTable;


/**
 *
 * @author  Teake Nutma
 */
public class ExportToTex extends javax.swing.JPanel
{
	private CAlgebraComposite algebras;
	private FileFilter texFilter;
	private JDialog parent;
	private UIPrintableColorTable repTable;
	private Clipboard clipboard;
	
	/** Creates new form ExportToTex */
	public ExportToTex()
	{
		initComponents();
		texFilter = new FileNameExtensionFilter("LaTeX file (*.tex)", "tex");
		clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	public void setup(JDialog parent, UIPrintableColorTable repTable, CAlgebraComposite algebras)
	{
		this.algebras = algebras;
		this.parent = parent;
		this.repTable = repTable;
		
		Object[] listData = new Object[repTable.getColumnCount()];
		int[] selectedIndeces = new int[repTable.getColumnCount()];
		
		for (int i = 0; i < repTable.getColumnCount(); i++)
		{
			listData[i] = repTable.getColumnName(i);
			selectedIndeces[i] = i;
		}
		columnList.setListData(listData);
		columnList.setSelectedIndices(selectedIndeces);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        exportToGroup = new javax.swing.ButtonGroup();
        cancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        cbDD = new javax.swing.JCheckBox();
        cbCaption = new javax.swing.JCheckBox();
        cbReps = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        rbFile = new javax.swing.JRadioButton();
        rbSout = new javax.swing.JRadioButton();
        rbClipboard = new javax.swing.JRadioButton();
        tfExtraColumns = new edu.rug.hep.simplie.ui.reusable.UILevelTextfield();
        cbDDlabels = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        columnList = new javax.swing.JList();
        export = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(0, 0));

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        cbDD.setSelected(true);
        cbDD.setText("Export Dynkin diagram");
        cbDD.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbDD.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbCaption.setSelected(true);
        cbCaption.setText("Include captions");
        cbCaption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbCaption.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbReps.setSelected(true);
        cbReps.setText("Export representation table");
        cbReps.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbReps.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel1.setText("Export to:");

        exportToGroup.add(rbFile);
        rbFile.setText("File");
        rbFile.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbFile.setMargin(new java.awt.Insets(0, 0, 0, 0));

        exportToGroup.add(rbSout);
        rbSout.setText("System output");
        rbSout.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbSout.setMargin(new java.awt.Insets(0, 0, 0, 0));

        exportToGroup.add(rbClipboard);
        rbClipboard.setSelected(true);
        rbClipboard.setText("Clipboard");
        rbClipboard.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbClipboard.setMargin(new java.awt.Insets(0, 0, 0, 0));

        tfExtraColumns.setText("Extra columns");

        cbDDlabels.setSelected(true);
        cbDDlabels.setText("Include labels on Dynkin diagram");
        cbDDlabels.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbDDlabels.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbDD)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbClipboard)
                            .addComponent(rbFile)
                            .addComponent(rbSout)))
                    .addComponent(tfExtraColumns, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbDDlabels)
                    .addComponent(cbReps)
                    .addComponent(cbCaption))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbDD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbDDlabels)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbReps)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCaption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tfExtraColumns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(rbClipboard))
                .addGap(7, 7, 7)
                .addComponent(rbFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbSout)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select table columns", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        jScrollPane1.setViewportView(columnList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addContainerGap())
        );

        export.setText("Export");
        export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(export)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancel))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(export)
                            .addComponent(cancel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	
	private void cancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelActionPerformed
	{//GEN-HEADEREND:event_cancelActionPerformed
		parent.setVisible(false);
	}//GEN-LAST:event_cancelActionPerformed
	
	private void exportActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exportActionPerformed
	{//GEN-HEADEREND:event_exportActionPerformed
		parent.setVisible(false);
		
		String output = new String();
		
		if(cbDD.isSelected())
			output += algebras.dd.toTeX(cbCaption.isSelected(), cbDDlabels.isSelected());
		if(cbDD.isSelected() && cbReps.isSelected())
			output += "\n";
		if(cbReps.isSelected())
			output += repTable.toTeX(cbCaption.isSelected(),columnList.getSelectedIndices(),Math.max(0,tfExtraColumns.getValue()));
		
		if(rbFile.isSelected())
		{
			JFileChooser chooser = new JFileChooser("");
			chooser.setSelectedFile(new File(algebras.getDynkinDiagramType(false) + ".tex"));
			chooser.addChoosableFileFilter(texFilter);
			chooser.setDialogTitle("Export to TeX");
			int returnVal = chooser.showSaveDialog(this);
			
			if ( returnVal == chooser.APPROVE_OPTION )
			{
				String fileURL = chooser.getSelectedFile().getAbsolutePath();
				if(!texFilter.accept(chooser.getSelectedFile()))
					fileURL += ".rs";
				// write it out
				FileWriter outputStream = null;
				try
				{
					outputStream = new FileWriter(fileURL);
					outputStream.write(output);
					outputStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		if(rbClipboard.isSelected())
		{
			StringSelection outputSelection = new StringSelection(output);
			clipboard.setContents(outputSelection,outputSelection);
		}
		if(rbSout.isSelected())
		{
			System.out.println("%%%%%%%% BEGIN LATEX OUTPUT %%%%%%%%");
			System.out.print(output);
			System.out.println("%%%%%%%%  END LATEX OUTPUT  %%%%%%%%");
		}
	}//GEN-LAST:event_exportActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JCheckBox cbCaption;
    private javax.swing.JCheckBox cbDD;
    private javax.swing.JCheckBox cbDDlabels;
    private javax.swing.JCheckBox cbReps;
    private javax.swing.JList columnList;
    private javax.swing.JButton export;
    private javax.swing.ButtonGroup exportToGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rbClipboard;
    private javax.swing.JRadioButton rbFile;
    private javax.swing.JRadioButton rbSout;
    private edu.rug.hep.simplie.ui.reusable.UILevelTextfield tfExtraColumns;
    // End of variables declaration//GEN-END:variables
	
}
