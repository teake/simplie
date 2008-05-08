/*
 * UIAlgebraInfo.java
 *
 * Created on 22 maart 2007, 10:01
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
import edu.simplie.algebra.Algebra;

/**
 *
 * @author  Teake Nutma
 * @version $Revision$, $Date$
 */
public class UIAlgebraInfo extends javax.swing.JPanel
{
	
	/**
	 * Creates new form UIAlgebraInfo
	 */
	public UIAlgebraInfo()
	{
		initComponents();
	}
	
	public void setTitle(String title)
	{
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, title, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
	}
		
	public void update(Algebra algebra)
	{
		algebraInfoType.setText(algebra.typeHTML);
		algebraInfoRank.setText(Helper.intToString(algebra.rank));
		algebraInfoDim.setText(algebra.dimension);
		algebraInfoCMDet.setText(Helper.intToString(algebra.det));
		algebraInfoCMrank.setText(Helper.intToString(algebra.rankA));
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel4 = new javax.swing.JLabel();
        algebraInfoType = new javax.swing.JLabel();
        algebraInfoRank = new javax.swing.JLabel();
        algebraInfoDim = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        algebraInfoCMDet = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        algebraInfoCMrank = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Algebra information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        jLabel4.setText("Type:");

        algebraInfoType.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        algebraInfoType.setText("   ");

        algebraInfoRank.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        algebraInfoRank.setText("    ");

        algebraInfoDim.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        algebraInfoDim.setText("    ");

        jLabel2.setText("Dimension:");

        jLabel1.setText("Rank:");

        jLabel3.setText("CM det:");

        algebraInfoCMDet.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        algebraInfoCMDet.setText("   ");

        jLabel5.setText("CM rank:");

        algebraInfoCMrank.setText("   ");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jLabel2)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(algebraInfoRank, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(algebraInfoDim, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)))
                        .add(25, 25, 25)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel3)
                            .add(jLabel5))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(algebraInfoCMrank, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(algebraInfoCMDet, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)))
                    .add(algebraInfoType, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(algebraInfoType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel2))
                    .add(layout.createSequentialGroup()
                        .add(algebraInfoRank)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(algebraInfoDim))
                    .add(layout.createSequentialGroup()
                        .add(algebraInfoCMDet)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(algebraInfoCMrank))
                    .add(layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel5)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel algebraInfoCMDet;
    private javax.swing.JLabel algebraInfoCMrank;
    private javax.swing.JLabel algebraInfoDim;
    private javax.swing.JLabel algebraInfoRank;
    private javax.swing.JLabel algebraInfoType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    // End of variables declaration//GEN-END:variables
	
}
