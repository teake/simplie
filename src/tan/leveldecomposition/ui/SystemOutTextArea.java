/*
 * SystemOutTextArea.java
 *
 * Created on 19 april 2007, 11:12
 */

package tan.leveldecomposition.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author  P221000
 */
public class SystemOutTextArea extends javax.swing.JPanel
{
	
	/**
	 * Creates new form SystemOutTextArea
	 */
	public SystemOutTextArea()
	{
		initComponents();
		
		PrintStream out = new PrintStream(new OutputStream()
		{
			public void write( int b ) throws IOException
			{
				// append the data as characters to the JTextArea control
				systemOutput.append( String.valueOf( ( char )b ) );
				systemOutput.setCaretPosition(systemOutput.getDocument().getLength());
			}
			
		});
		System.setOut( out );
		System.setErr( out );
		
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        jScrollPane1 = new javax.swing.JScrollPane();
        systemOutput = new javax.swing.JTextArea();

        systemOutput.setColumns(20);
        systemOutput.setFont(new java.awt.Font("Monospaced", 0, 11));
        systemOutput.setRows(5);
        jScrollPane1.setViewportView(systemOutput);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea systemOutput;
    // End of variables declaration//GEN-END:variables
	
}