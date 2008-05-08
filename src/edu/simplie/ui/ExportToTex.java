/*
 * ExportToTex.java
 *
 * Created on 19 april 2007, 16:47
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

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import edu.simplie.ui.reusable.UIPrintableColorTable;


/**
 * User interface for exporting various things (Dynkin diagram,
 * output of level decomposition) to LaTeX.
 * 
 * @author  Teake Nutma
 * @version $Revision$, $Date$
 */
public class ExportToTex extends javax.swing.JPanel
{
	private AlgebraComposite algebras;
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
	
	public void setup(JDialog parent, UIPrintableColorTable repTable, AlgebraComposite algebras)
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

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
        tfExtraColumns = new edu.simplie.ui.reusable.UINumTextfield();
        cbDDlabels = new javax.swing.JCheckBox();
        cbDDfigure = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        columnList = new javax.swing.JList();
        export = new javax.swing.JButton();

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
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

        cbDDfigure.setSelected(true);
        cbDDfigure.setText("Include figure environment");
        cbDDfigure.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbDDfigure.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cbDD)
                            .add(cbReps)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(17, 17, 17)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(cbDDfigure)
                                    .add(cbDDlabels)))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(rbClipboard)
                                    .add(rbFile)
                                    .add(rbSout)))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(17, 17, 17)
                                .add(cbCaption))))
                    .add(tfExtraColumns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(cbDD)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbDDlabels)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbDDfigure)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(cbReps)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbCaption)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tfExtraColumns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(rbClipboard))
                .add(7, 7, 7)
                .add(rbFile)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbSout)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select table columns", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        jScrollPane1.setViewportView(columnList);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addContainerGap())
        );

        export.setText("Export");
        export.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exportActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(export)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(export, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(cancel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
			output += algebras.dd.toTeX(cbCaption.isSelected(), cbDDlabels.isSelected(), cbDDfigure.isSelected());
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
			
			if ( returnVal == JFileChooser.APPROVE_OPTION )
			{
				String fileURL = chooser.getSelectedFile().getAbsolutePath();
				if(!texFilter.accept(chooser.getSelectedFile()))
					fileURL += ".tex";
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
    private javax.swing.JCheckBox cbDDfigure;
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
    private edu.simplie.ui.reusable.UINumTextfield tfExtraColumns;
    // End of variables declaration//GEN-END:variables
	
}
