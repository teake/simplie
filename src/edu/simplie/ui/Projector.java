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
import edu.simplie.projections.CoxeterProjector;
import edu.simplie.projections.HasseProjector;
import edu.simplie.projections.Projector2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import javax.swing.JFileChooser;
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

	private AlgebraComposite	algebras;
	private Projector2D			projector2D;
	private Projector2D			coxeterProjector;
	private Projector2D			hasseProjector;

    /** Creates new form Projector */
    public Projector()
	{
        initComponents();
		coxeterProjector	= new CoxeterProjector();
		hasseProjector		= new HasseProjector();
		projector2D			= coxeterProjector;
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
        canvas = new javax.swing.JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                draw(g);
            }
        };
        optionPanel = new javax.swing.JPanel();
        drawButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        epsButton = new javax.swing.JButton();
        tfMaxHeight = new javax.swing.JLabel();
        spinnerMaxHeight = new edu.simplie.ui.reusable.UISpinner();
        tfProjectionMode = new javax.swing.JLabel();
        rbCoxeter = new javax.swing.JRadioButton();
        rbHasse = new javax.swing.JRadioButton();
        tfDraw = new javax.swing.JLabel();
        cbNodes = new javax.swing.JCheckBox();
        cbConnections = new javax.swing.JCheckBox();
        spinnerLevels = new edu.simplie.ui.reusable.UIMultiSpinner();
        cbLevels = new javax.swing.JCheckBox();

        setName("Form"); // NOI18N

        canvas.setBackground(new java.awt.Color(255, 255, 255));
        canvas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        canvas.setName("canvas"); // NOI18N

        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 339, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 422, Short.MAX_VALUE)
        );

        optionPanel.setName("optionPanel"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getActionMap(Projector.class, this);
        drawButton.setAction(actionMap.get("project")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(Projector.class);
        drawButton.setText(resourceMap.getString("projector.draw")); // NOI18N
        drawButton.setName("drawButton"); // NOI18N

        clearButton.setAction(actionMap.get("clear")); // NOI18N
        clearButton.setText(resourceMap.getString("projector.clear")); // NOI18N
        clearButton.setName("clearButton"); // NOI18N

        epsButton.setAction(actionMap.get("toEPS")); // NOI18N
        epsButton.setText(resourceMap.getString("projector.toEps")); // NOI18N
        epsButton.setName("epsButton"); // NOI18N

        tfMaxHeight.setText(resourceMap.getString("projector.maxHeight")); // NOI18N
        tfMaxHeight.setName("tfMaxHeight"); // NOI18N

        spinnerMaxHeight.setMinValue(0);
        spinnerMaxHeight.setName("spinnerMaxHeight"); // NOI18N

        tfProjectionMode.setText(resourceMap.getString("projector.tfProjectionMode")); // NOI18N
        tfProjectionMode.setName("tfProjectionMode"); // NOI18N

        bgMode.add(rbCoxeter);
        rbCoxeter.setSelected(true);
        rbCoxeter.setText(resourceMap.getString("projector.rbCoxeter")); // NOI18N
        rbCoxeter.setName("rbCoxeter"); // NOI18N

        bgMode.add(rbHasse);
        rbHasse.setText(resourceMap.getString("projector.rbHasse")); // NOI18N
        rbHasse.setName("rbHasse"); // NOI18N

        tfDraw.setText(resourceMap.getString("projector.tfDraw")); // NOI18N
        tfDraw.setName("tfDraw"); // NOI18N

        cbNodes.setSelected(true);
        cbNodes.setText(resourceMap.getString("projector.cbNodes")); // NOI18N
        cbNodes.setName("cbNodes"); // NOI18N

        cbConnections.setSelected(true);
        cbConnections.setText(resourceMap.getString("projector.cbConnections")); // NOI18N
        cbConnections.setName("cbConnections"); // NOI18N

        javax.swing.GroupLayout optionPanelLayout = new javax.swing.GroupLayout(optionPanel);
        optionPanel.setLayout(optionPanelLayout);
        optionPanelLayout.setHorizontalGroup(
            optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tfMaxHeight)
            .addComponent(spinnerMaxHeight, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
            .addComponent(epsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
            .addComponent(clearButton, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
            .addComponent(drawButton, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
            .addGroup(optionPanelLayout.createSequentialGroup()
                .addGroup(optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfProjectionMode)
                    .addComponent(cbNodes)
                    .addComponent(cbConnections)
                    .addComponent(rbHasse)
                    .addComponent(tfDraw)
                    .addComponent(rbCoxeter, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                .addContainerGap())
        );
        optionPanelLayout.setVerticalGroup(
            optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionPanelLayout.createSequentialGroup()
                .addComponent(drawButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(epsButton)
                .addGap(18, 18, 18)
                .addComponent(tfMaxHeight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spinnerMaxHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tfProjectionMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbCoxeter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbHasse)
                .addGap(18, 18, 18)
                .addComponent(tfDraw)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbNodes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbConnections)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        spinnerLevels.setMinValue(0);
        spinnerLevels.setName("spinnerLevels"); // NOI18N

        cbLevels.setText(resourceMap.getString("projector.limitLevels")); // NOI18N
        cbLevels.setName("cbLevels"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbLevels)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spinnerLevels, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(optionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(optionPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(canvas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spinnerLevels, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbLevels))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	public void setAlgebrasComposite(AlgebraComposite algebras)
	{
		algebras.dd.addListener(this);
		this.algebras = algebras;
		coxeterProjector.setAlgebrasComposite(algebras);
		hasseProjector.setAlgebrasComposite(algebras);
	}

	@Action
	public void project()
	{
		// Project
		setProjectorOptions();
		projector2D.project();
		// And draw
		canvas.repaint();
	}

	public void draw(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		projector2D.draw(g2,canvas.getBounds().getWidth(),canvas.getBounds().getHeight());
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgMode;
    private javax.swing.JPanel canvas;
    private javax.swing.JCheckBox cbConnections;
    private javax.swing.JCheckBox cbLevels;
    private javax.swing.JCheckBox cbNodes;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton drawButton;
    private javax.swing.JButton epsButton;
    private javax.swing.JPanel optionPanel;
    private javax.swing.JRadioButton rbCoxeter;
    private javax.swing.JRadioButton rbHasse;
    private edu.simplie.ui.reusable.UIMultiSpinner spinnerLevels;
    private edu.simplie.ui.reusable.UISpinner spinnerMaxHeight;
    private javax.swing.JLabel tfDraw;
    private javax.swing.JLabel tfMaxHeight;
    private javax.swing.JLabel tfProjectionMode;
    // End of variables declaration//GEN-END:variables

	public void diagramChanged()
	{
		if(!algebras.subAlgebra.finite && rbCoxeter.isSelected())
		{
			rbHasse.setSelected(true);
		}
		rbCoxeter.setEnabled(algebras.subAlgebra.finite);
		spinnerLevels.setNumSpinners(algebras.algebra.rank - algebras.coAlgebra.rank);
	}

	@Action
	public void clear()
	{
		projector2D.clear();
		canvas.repaint();
	}

	@Action
	public void toEPS()
	{
		JFileChooser chooser = new JFileChooser("");
		chooser.setSelectedFile(new File("Projection of " + algebras.getDynkinDiagramType() + ".eps"));
		chooser.setDialogTitle("to EPS");
		if ( chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION )
		{
			setProjectorOptions();
			projector2D.project();
			projector2D.toEpsFile(chooser.getSelectedFile().getAbsolutePath());
		}
	}

	private void setProjectorOptions()
	{
		projector2D = ( rbCoxeter.isSelected() ) ? coxeterProjector : hasseProjector;
		projector2D.setMaxHeight(spinnerMaxHeight.getValue());
		projector2D.setLevels(spinnerLevels.getValues());
		projector2D.setLimitLevels(cbLevels.isSelected());
		projector2D.setDrawNodes(cbNodes.isSelected());
		projector2D.setDrawConnections(cbConnections.isSelected());
	}
}
