/*
 * RepInfo.java
 *
 * Created on 3-jun-2009, 13:44:48
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
package edu.simplie.ui.reps;

import javax.swing.table.DefaultTableModel;
import edu.simplie.dynkindiagram.*;
import edu.simplie.algebra.*;
import edu.simplie.*;
import java.util.Collection;
import java.util.Iterator;
import org.jdesktop.application.Action;

/**
 *
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class RepInfo extends javax.swing.JPanel implements DiagramListener
{
	private DefaultTableModel tableModelWeights;
	private AlgebraComposite algebras;
	private HighestWeightRep HWrep;

    /** Creates new form RepInfo */
    public RepInfo()
	{
        initComponents();
		tableModelWeights = (DefaultTableModel) weightTable.getModel();
		HWrep = null;
    }

	public void setAlgebraComposite(AlgebraComposite algebras)
	{
		this.algebras = algebras;
		algebras.dd.addListener(this);
	}

	public void diagramChanged()
	{
		repSpinner.setAlgebra(algebras.algebra);
	}

	@Action
	public void showWeights()
	{
		HWrep = repSpinner.getRepresentation();
		if(HWrep == null)
			return;

		// Fully construct the weight system.
		HWrep.construct(0);

		// Clear and fill the table.
		tableModelWeights.setRowCount(0);

		for (int i = 0; i < HWrep.size(); i++)
		{
			Collection<Weight> weights = HWrep.get(i);
			Iterator iterator	= weights.iterator();
			while (iterator.hasNext())
			{
				Weight weight = (Weight) iterator.next();
				Object[] rowData = new Object[3];
				rowData[0] = Helper.intArrayToString(weight.dynkinLabels);
				rowData[1] = weight.getMult();
				rowData[2] = weight.getDepth();
				tableModelWeights.addRow(rowData);
			}
		}
	}


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        weightTable = new edu.simplie.ui.reusable.UIPrintableColorTable();
        bShowWeights = new javax.swing.JButton();
        repSpinner = new edu.simplie.ui.reps.RepSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        weightTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Dynkin labels", "Multiplicity", "Depth"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Long.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        weightTable.setName("weightTable"); // NOI18N
        jScrollPane1.setViewportView(weightTable);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getActionMap(RepInfo.class, this);
        bShowWeights.setAction(actionMap.get("showWeights")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(RepInfo.class);
        bShowWeights.setText(resourceMap.getString("reps.showweights")); // NOI18N
        bShowWeights.setName("bShowWeights"); // NOI18N

        repSpinner.setName("repSpinner"); // NOI18N

        jLabel1.setText(resourceMap.getString("reps.highestweight")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("generic.dim")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jButton1.setAction(actionMap.get("reset")); // NOI18N
        jButton1.setText(resourceMap.getString("generic.reset")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 131, Short.MAX_VALUE)
                                .add(jLabel2))
                            .add(repSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(bShowWeights, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(bShowWeights)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButton1)
                    .add(repSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	@Action
	public void reset()
	{
		tableModelWeights.setRowCount(0);
		repSpinner.reset();
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bShowWeights;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private edu.simplie.ui.reps.RepSpinner repSpinner;
    private edu.simplie.ui.reusable.UIPrintableColorTable weightTable;
    // End of variables declaration//GEN-END:variables


}
