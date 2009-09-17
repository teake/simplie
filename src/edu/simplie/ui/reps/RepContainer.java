/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RepContainer.java
 *
 * Created on 3-jun-2009, 14:24:22
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

package edu.simplie.ui.reps;

import edu.simplie.*;

/**
 *
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class RepContainer extends javax.swing.JPanel
{

    /** Creates new form RepContainer */
    public RepContainer()
	{
        initComponents();
    }

	public void setAlgebraComposite(AlgebraComposite algebras)
	{
		repInfo.setAlgebraComposite(algebras);
		tensorProducts.setAlgebraComposite(algebras);
		branching.setAlgebraComposite(algebras);
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        repInfo = new edu.simplie.ui.reps.RepInfo();
        tensorProducts = new edu.simplie.ui.reps.TensorProducts();
        branching = new edu.simplie.ui.reps.Branching();

        setName("Form"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        repInfo.setName("repInfo"); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(RepContainer.class);
        jTabbedPane1.addTab(resourceMap.getString("reps.info"), repInfo); // NOI18N

        tensorProducts.setName("tensorProducts"); // NOI18N
        jTabbedPane1.addTab(resourceMap.getString("reps.products"), tensorProducts); // NOI18N

        branching.setName("branching"); // NOI18N
        jTabbedPane1.addTab(resourceMap.getString("reps.branching"), branching); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private edu.simplie.ui.reps.Branching branching;
    private javax.swing.JTabbedPane jTabbedPane1;
    private edu.simplie.ui.reps.RepInfo repInfo;
    private edu.simplie.ui.reps.TensorProducts tensorProducts;
    // End of variables declaration//GEN-END:variables

}
