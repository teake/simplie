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
import edu.simplie.math.fraction;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.jdesktop.application.Action;

// TODO: make this work for non-simply laced and affine algebras.

/**
 *
 * @author Teake Nutma
 * @version $Revision: 434 $, $Date: 2009-09-16 16:19:33 +0200 (wo, 16 sep 2009) $
 */
public class Branching extends javax.swing.JPanel implements DiagramListener
{
	private DefaultTableModel tableModelWeights;
	private AlgebraComposite algebras;
	private HighestWeightRep HWrep;

    /** Creates new form RepInfo */
    public Branching()
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
		boolean enable = (algebras.dd.getNumLevels() > 0);
		depthSpinner.setEnabled(enable);
		bBranch.setEnabled(enable);
	}

	@Action
	public void showWeights()
	{
		HighestWeightRep subRep;
		HighestWeightRep intRep;
		HighestWeightRep coRep;
		// Map of subalgebra reps, with the levels converted to a string as key.
		Map<String,Collection<HighestWeightRep>> coReps = new HashMap<String,Collection<HighestWeightRep>>();

		HWrep = repSpinner.getRepresentation();
		if(HWrep == null)
			return;

		// Clear and fill the table.
		tableModelWeights.setRowCount(0);

		// Construct the weight system up the given depth.
		int maxDepth = depthSpinner.getValue();
		HWrep.construct(maxDepth);
		maxDepth = (maxDepth == 0) ? HWrep.size() : Math.min(HWrep.size(),maxDepth+1);

		for (int i = 0; i < maxDepth; i++)
		{
			Collection<Weight> weights = HWrep.get(i);
			Iterator iterator	= weights.iterator();

			weightLoop:
			while (iterator.hasNext())
			{
				Weight weight = (Weight) iterator.next();
				int[] labels = weight.dynkinLabels;
				// Split the int and sub parts.
				// Check for dominant parts at the same time.
				int[] intLabels = new int[algebras.intAlgebra.rank];
				int[] subLabels = new int[algebras.subAlgebra.rank];
				int[] coLabels = new int[algebras.coAlgebra.rank];
				for (int j = 0; j < algebras.intAlgebra.rank; j++)
				{
					intLabels[j] = labels[algebras.dd.translateInt(j)];
					if(intLabels[j] < 0)
						continue weightLoop;
				}
				for (int j = 0; j < algebras.subAlgebra.rank; j++)
				{
					subLabels[j] = labels[algebras.dd.translateSub(j)];
					if(subLabels[j] < 0)
						continue weightLoop;
				}
				for (int j = 0; j < algebras.coAlgebra.rank; j++)
				{
					coLabels[j] = labels[algebras.dd.translateCo(j)];
				}
				// If we got this far we have a valid subrep branching.
				subRep = new HighestWeightRep(algebras.subAlgebra, subLabels);
				intRep = new HighestWeightRep(algebras.intAlgebra, intLabels);
				coRep = new HighestWeightRep(algebras.coAlgebra, coLabels);

				// Calculate the levels for this weight
				fraction[] levels = algebras.levels(algebras.algebra.weightToRoot(labels));
				
				// Fetch representations previously processed at this level.
				Collection<HighestWeightRep> oldReps = coReps.get(Helper.arrayToString(levels));
				if(oldReps == null)
				{
					oldReps = new HashSet<HighestWeightRep>();
					coReps.put(Helper.arrayToString(levels), oldReps);
				}

				// Check the outer multiplicity
				long outerMult = weight.getMult();
				Iterator repIt = oldReps.iterator();
				while(repIt.hasNext())
				{
					HighestWeightRep oldCoRep = (HighestWeightRep) repIt.next();
					outerMult = outerMult - ( oldCoRep.getWeightMult(coLabels) * oldCoRep.getOuterMult() );
				}
				// Don't add this rep if its outer multiplicity is zero.
				if(outerMult == 0)
					continue;

				coRep.setOuterMult(outerMult);
				oldReps.add(coRep);

				Object[] rowData = new Object[7];
				rowData[0] = Helper.arrayToString(levels);
				rowData[1] = i;
				rowData[2] = Helper.arrayToString(intLabels);
				rowData[3] = Helper.arrayToString(subLabels);
				rowData[4] = intRep.dim;
				rowData[5] = subRep.dim;
				rowData[6] = outerMult;
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
        bBranch = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        repSpinner = new edu.simplie.ui.reps.RepSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        tfDepth = new javax.swing.JLabel();
        depthSpinner = new edu.simplie.ui.reusable.UISpinner();

        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        weightTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Levels", "Depth", "Dynkin labels int", "Dynkin label sub", "Dimension int", "Dimension sub", "Multiplicity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getActionMap(Branching.class, this);
        bBranch.setAction(actionMap.get("showWeights")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(Branching.class);
        bBranch.setText(resourceMap.getString("reps.branch")); // NOI18N
        bBranch.setName("bBranch"); // NOI18N

        jButton1.setAction(actionMap.get("reset")); // NOI18N
        jButton1.setText(resourceMap.getString("generic.reset")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        repSpinner.setName("repSpinner"); // NOI18N

        jLabel1.setText(resourceMap.getString("reps.highestweight")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("generic.dim")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 194, Short.MAX_VALUE)
                        .add(jLabel2))
                    .add(repSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(repSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        tfDepth.setText(resourceMap.getString("reps.maxdepth")); // NOI18N
        tfDepth.setName("tfDepth"); // NOI18N

        depthSpinner.setMinValue(0);
        depthSpinner.setName("depthSpinner"); // NOI18N
        depthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                depthSpinnerStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tfDepth)
                    .add(depthSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(tfDepth)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(depthSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(bBranch, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(bBranch)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton1))
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {jPanel1, jPanel2}, org.jdesktop.layout.GroupLayout.VERTICAL);

    }// </editor-fold>//GEN-END:initComponents

	private void depthSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_depthSpinnerStateChanged
	{//GEN-HEADEREND:event_depthSpinnerStateChanged
		showWeights();
	}//GEN-LAST:event_depthSpinnerStateChanged

	@Action
	public void reset()
	{
		depthSpinner.setValue(0);
		tableModelWeights.setRowCount(0);
		repSpinner.reset();
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBranch;
    private edu.simplie.ui.reusable.UISpinner depthSpinner;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private edu.simplie.ui.reps.RepSpinner repSpinner;
    private javax.swing.JLabel tfDepth;
    private edu.simplie.ui.reusable.UIPrintableColorTable weightTable;
    // End of variables declaration//GEN-END:variables


}
