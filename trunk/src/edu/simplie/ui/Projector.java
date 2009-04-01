/*
 * RootSpaceDrawer.java
 *
 * Created on 9 April 2007, 12:08
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

import edu.simplie.AlgebraComposite;
import edu.simplie.dynkindiagram.DiagramListener;
import edu.simplie.projections.RootSystemProjector2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.jdesktop.application.Action;


/*
 * Projector.java
 *
 * Created on 31-mrt-2009, 11:31:31
 *
 * @author  Teake Nutma
 * @version $Rev$, $Date$
 */
public class Projector extends javax.swing.JPanel implements DiagramListener
{

	private AlgebraComposite		algebras;
	private RootSystemProjector2D	projector2D;

    /** Creates new form Projector */
    public Projector()
	{
        initComponents();
		projector2D = new RootSystemProjector2D();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgMode = new javax.swing.ButtonGroup();
        drawButton = new javax.swing.JButton();
        canvas = new javax.swing.JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                draw(g);
            }
        };
        spinnerMaxHeight = new edu.simplie.ui.reusable.UISpinner();
        tfMaxHeight = new javax.swing.JLabel();
        rbCoxeter = new javax.swing.JRadioButton();
        rbHasse = new javax.swing.JRadioButton();
        clearButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getActionMap(Projector.class, this);
        drawButton.setAction(actionMap.get("project")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(Projector.class);
        drawButton.setText(resourceMap.getString("projector.draw")); // NOI18N
        drawButton.setName("drawButton"); // NOI18N

        canvas.setBackground(new java.awt.Color(255, 255, 255));
        canvas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        canvas.setName("canvas"); // NOI18N

        org.jdesktop.layout.GroupLayout canvasLayout = new org.jdesktop.layout.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 539, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 384, Short.MAX_VALUE)
        );

        spinnerMaxHeight.setMinValue(0);
        spinnerMaxHeight.setName("spinnerMaxHeight"); // NOI18N
        spinnerMaxHeight.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerMaxHeightStateChanged(evt);
            }
        });

        tfMaxHeight.setText(resourceMap.getString("projector.maxHeight")); // NOI18N
        tfMaxHeight.setName("tfMaxHeight"); // NOI18N

        bgMode.add(rbCoxeter);
        rbCoxeter.setSelected(true);
        rbCoxeter.setText(resourceMap.getString("projector.rbCoxeter")); // NOI18N
        rbCoxeter.setName("rbCoxeter"); // NOI18N

        bgMode.add(rbHasse);
        rbHasse.setText(resourceMap.getString("projector.rbHasse")); // NOI18N
        rbHasse.setName("rbHasse"); // NOI18N

        clearButton.setAction(actionMap.get("clear")); // NOI18N
        clearButton.setText(resourceMap.getString("projector.clear")); // NOI18N
        clearButton.setName("clearButton"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(drawButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(clearButton)
                        .add(18, 18, 18)
                        .add(tfMaxHeight)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(spinnerMaxHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(rbCoxeter)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rbHasse))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, canvas, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(spinnerMaxHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rbCoxeter)
                    .add(rbHasse)
                    .add(drawButton)
                    .add(clearButton)
                    .add(tfMaxHeight))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(canvas, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void spinnerMaxHeightStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_spinnerMaxHeightStateChanged
	{//GEN-HEADEREND:event_spinnerMaxHeightStateChanged
		project();
	}//GEN-LAST:event_spinnerMaxHeightStateChanged



	public void setAlgebrasComposite(AlgebraComposite algebras)
	{
		this.algebras = algebras;
		projector2D.setAlgebrasComposite(algebras);
		algebras.dd.addListener(this);
	}

	@Action
	public void project()
	{
		// Project
		int mode = ( rbCoxeter.isSelected() ) ? RootSystemProjector2D.COXETER_MODE : RootSystemProjector2D.HASSE_MODE;
		projector2D.setMode(mode);
		projector2D.project(spinnerMaxHeight.getValue());
		// And draw
		canvas.repaint();
	}

	public void draw(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		projector2D.draw(g2,canvas.getBounds());
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgMode;
    private javax.swing.JPanel canvas;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton drawButton;
    private javax.swing.JRadioButton rbCoxeter;
    private javax.swing.JRadioButton rbHasse;
    private edu.simplie.ui.reusable.UISpinner spinnerMaxHeight;
    private javax.swing.JLabel tfMaxHeight;
    // End of variables declaration//GEN-END:variables

	public void diagramChanged()
	{
		if(!algebras.subAlgebra.finite && rbCoxeter.isSelected())
		{
			rbHasse.setSelected(true);
		}
		rbCoxeter.setEnabled(algebras.subAlgebra.finite);
	}

	@Action
	public void clear()
	{
		projector2D.clear();
		canvas.repaint();
	}
}