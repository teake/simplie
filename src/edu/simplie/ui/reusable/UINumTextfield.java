/*
 * UINumTextfield.java
 *
 * Created on 14 maart 2007, 9:33
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

package edu.simplie.ui.reusable;

import edu.simplie.Helper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author  Teake Nutma
 * @version $Revision$, $Date$
 */
public class UINumTextfield extends javax.swing.JPanel
{
	
	/**
	 * Creates new form UINumTextfield
	 */
	public UINumTextfield()
	{
		initComponents();
		
		tfLevel.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
			}
			public void keyReleased(KeyEvent e)
			{
			}
			public void keyTyped(KeyEvent e)
			{
				int k=e.getKeyChar();
				if((k>47 && k<58)|| k==8 || k==KeyEvent.VK_MINUS)
				{
				}
				else
				{
					e.setKeyChar((char)KeyEvent.VK_CLEAR);
				}
			}
		});
	}
	
	public void setText(String text)
	{
		textField.setText(text);
	}
	public int getValue()
	{
		return Helper.stringToInt(tfLevel.getText());
	}
	
	public void setValue(int value)
	{
		tfLevel.setText(Helper.intToString(value));
	}
	
	

	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        textField = new javax.swing.JLabel();
        tfLevel = new javax.swing.JTextField();
        buttonMinus = new javax.swing.JButton();
        buttonPlus = new javax.swing.JButton();

        textField.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        textField.setText("<text>:");

        tfLevel.setText("0");
        tfLevel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                tfLevelActionPerformed(evt);
            }
        });

        buttonMinus.setText("-");
        buttonMinus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                buttonMinusActionPerformed(evt);
            }
        });

        buttonPlus.setText("+");
        buttonPlus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                buttonPlusActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(textField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tfLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonPlus)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonMinus))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(tfLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(buttonMinus)
                    .add(buttonPlus)
                    .add(textField))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
		
	private void tfLevelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_tfLevelActionPerformed
	{//GEN-HEADEREND:event_tfLevelActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_tfLevelActionPerformed

	private void buttonMinusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_buttonMinusActionPerformed
	{//GEN-HEADEREND:event_buttonMinusActionPerformed
		setValue(getValue()-1);
}//GEN-LAST:event_buttonMinusActionPerformed

	private void buttonPlusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_buttonPlusActionPerformed
	{//GEN-HEADEREND:event_buttonPlusActionPerformed
		setValue(getValue()+1);
}//GEN-LAST:event_buttonPlusActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonMinus;
    private javax.swing.JButton buttonPlus;
    private javax.swing.JLabel textField;
    private javax.swing.JTextField tfLevel;
    // End of variables declaration//GEN-END:variables
	
}
