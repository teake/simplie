/*
 * SystemOutTextArea.java
 *
 * Created on 19 april 2007, 11:12
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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Output panel for low-level SimpLie messages, which are not of direct concern
 * of to the user.
 * 
 * @author  Teake Nutma
 * @version $Revision$, $Date$
 */
public class SystemOutTextArea extends javax.swing.JPanel
{
	PrintStream textOut;
	PrintStream sysOut;
	
	/**
	 * Creates new form SystemOutTextArea
	 */
	public SystemOutTextArea()
	{
		initComponents();
		
		sysOut	= System.out;
		textOut = new PrintStream(new OutputStream()
		{
			public void write( int b ) throws IOException
			{
				// append the data as characters to the JTextArea control
				systemOutput.append( String.valueOf( ( char )b ) );
				systemOutput.setCaretPosition(systemOutput.getDocument().getLength());
			}
			
		});
		cbProgramStateChanged(null);
		cbErrorsStateChanged(null);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        systemOutput = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        cbErrors = new javax.swing.JCheckBox();
        cbProgram = new javax.swing.JCheckBox();

        systemOutput.setColumns(20);
        systemOutput.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        systemOutput.setRows(5);
        jScrollPane1.setViewportView(systemOutput);

        jButton1.setText("Clear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cbErrors.setText("Show Java errors");
        cbErrors.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cbErrors.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cbErrorsStateChanged(evt);
            }
        });

        cbProgram.setSelected(true);
        cbProgram.setText("Show program output");
        cbProgram.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cbProgram.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cbProgramStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jButton1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 86, Short.MAX_VALUE)
                        .add(cbProgram)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(cbErrors)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton1)
                    .add(cbErrors)
                    .add(cbProgram))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void cbProgramStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_cbProgramStateChanged
	{//GEN-HEADEREND:event_cbProgramStateChanged
		if(cbProgram.isSelected())
			System.setOut(textOut);
		else
			System.setOut(sysOut);
	}//GEN-LAST:event_cbProgramStateChanged

	private void cbErrorsStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_cbErrorsStateChanged
	{//GEN-HEADEREND:event_cbErrorsStateChanged
		if(cbErrors.isSelected())
			System.setErr(textOut);
		else
			System.setErr(sysOut);
	}//GEN-LAST:event_cbErrorsStateChanged

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
	{//GEN-HEADEREND:event_jButton1ActionPerformed
		systemOutput.setText(null);
	}//GEN-LAST:event_jButton1ActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbErrors;
    private javax.swing.JCheckBox cbProgram;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea systemOutput;
    // End of variables declaration//GEN-END:variables
	
}
