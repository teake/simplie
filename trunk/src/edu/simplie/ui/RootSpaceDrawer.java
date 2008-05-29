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

import com.sun.opengl.util.GLUT;
import edu.simplie.AlgebraComposite;
import edu.simplie.Helper;
import edu.simplie.algebra.Root;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collection;
import java.util.Iterator;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;

/**
 *
 * @author  Teake Nutma
 * @version $Revision$, $Date$
 */
public class RootSpaceDrawer extends javax.swing.JPanel implements 
		GLEventListener, 
		MouseMotionListener, 
		MouseWheelListener, 
		MouseListener
{
	private GLAutoDrawable glDrawable;
	private GL gl;
	private GLUT glut;
	private GLContext context;
	
	// Variables for rotations, translation and zoom.
	private float viewRotX = 0.0f, viewRotY = 0.0f;
	private float viewTransX = 0.0f, viewTransY = 0.0f;
	private float zoom = 1.0f;
	private int prevMouseX, prevMouseY;
	private boolean rotate = false;
	private float[] offset = {0.0f, 0.0f, 0.0f};
	
	// Indices for the GL display lists.
	private int realReflsObj;
	private int imReflsObj;
	private int realRootsObj;
	private int imRootsObj;
	private int sphereObj;
	private int simpReflsObj;
	private int realLabelObj;
	private int imLabelObj;
	private int[] listContainer;
	
	private AlgebraComposite algebras;
	
	/** Creates new form RootSpaceDrawer */
	public RootSpaceDrawer()
	{
		initComponents();
		
		canvas.setVisible(true);
		canvas.addGLEventListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);
		canvas.addMouseListener(this);
	}
	
	public void setAlgebrasComposite(AlgebraComposite algebras)
	{
		this.algebras = algebras;
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgColorCoding = new javax.swing.ButtonGroup();
        container = new javax.swing.JPanel();
        canvas = new javax.media.opengl.GLJPanel();
        jpSettings = new javax.swing.JPanel();
        cbRealRoots = new javax.swing.JCheckBox();
        cbImRoots = new javax.swing.JCheckBox();
        cbReflections = new javax.swing.JCheckBox();
        rbColorLevels = new javax.swing.JRadioButton();
        rbColorNorms = new javax.swing.JRadioButton();
        tfColor = new javax.swing.JLabel();
        cbNegative = new javax.swing.JCheckBox();
        cbLabels = new javax.swing.JCheckBox();
        maxHeightField = new edu.simplie.ui.reusable.UISpinner();
        tfMaxHeight = new javax.swing.JLabel();
        jpActions = new javax.swing.JPanel();
        bReset = new javax.swing.JButton();
        bUpdate = new javax.swing.JButton();

        container.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hasse diagram of the root space", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        canvas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.jdesktop.layout.GroupLayout canvasLayout = new org.jdesktop.layout.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 533, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout containerLayout = new org.jdesktop.layout.GroupLayout(container);
        container.setLayout(containerLayout);
        containerLayout.setHorizontalGroup(
            containerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(containerLayout.createSequentialGroup()
                .addContainerGap()
                .add(canvas, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        containerLayout.setVerticalGroup(
            containerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(containerLayout.createSequentialGroup()
                .add(canvas, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpSettings.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        cbRealRoots.setSelected(true);
        cbRealRoots.setText("Real roots");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(RootSpaceDrawer.class);
        cbRealRoots.setToolTipText(resourceMap.getString("drawer.realTooltip")); // NOI18N
        cbRealRoots.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repaint(evt);
            }
        });

        cbImRoots.setSelected(true);
        cbImRoots.setText("Imaginary roots");
        cbImRoots.setToolTipText(resourceMap.getString("drawer.imaginaryTooltip")); // NOI18N
        cbImRoots.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repaint(evt);
            }
        });

        cbReflections.setSelected(true);
        cbReflections.setText("Weyl reflections");
        cbReflections.setToolTipText(resourceMap.getString("drawer.reflectionsTooltip")); // NOI18N
        cbReflections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repaint(evt);
            }
        });

        bgColorCoding.add(rbColorLevels);
        rbColorLevels.setText("Level decomposition");
        rbColorLevels.setToolTipText(resourceMap.getString("drawer.levelTooltip")); // NOI18N
        rbColorLevels.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateAndRepaint(evt);
            }
        });

        bgColorCoding.add(rbColorNorms);
        rbColorNorms.setSelected(true);
        rbColorNorms.setText("Root norms");
        rbColorNorms.setToolTipText(resourceMap.getString("drawer.normTooltip")); // NOI18N
        rbColorNorms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateAndRepaint(evt);
            }
        });

        tfColor.setText("Color coding:");

        cbNegative.setText("Negative roots");
        cbNegative.setToolTipText(resourceMap.getString("drawer.negativeTooltip")); // NOI18N
        cbNegative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repaint(evt);
            }
        });

        cbLabels.setSelected(true);
        cbLabels.setText("Root labels");
        cbLabels.setToolTipText(resourceMap.getString("drawer.labelsTooltip")); // NOI18N
        cbLabels.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repaint(evt);
            }
        });

        maxHeightField.setToolTipText(resourceMap.getString("drawer.maxHeightTooltip")); // NOI18N
        maxHeightField.setMinValue(0);

        tfMaxHeight.setText("Max height:");
        tfMaxHeight.setToolTipText(resourceMap.getString("drawer.maxHeightTooltip")); // NOI18N

        org.jdesktop.layout.GroupLayout jpSettingsLayout = new org.jdesktop.layout.GroupLayout(jpSettings);
        jpSettings.setLayout(jpSettingsLayout);
        jpSettingsLayout.setHorizontalGroup(
            jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jpSettingsLayout.createSequentialGroup()
                        .add(jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cbRealRoots)
                            .add(cbImRoots))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cbLabels)
                            .add(cbReflections))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(tfColor)
                            .add(tfMaxHeight))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(rbColorLevels)
                            .add(rbColorNorms)
                            .add(maxHeightField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(cbNegative)))
        );
        jpSettingsLayout.setVerticalGroup(
            jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpSettingsLayout.createSequentialGroup()
                .add(jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jpSettingsLayout.createSequentialGroup()
                        .add(jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cbRealRoots)
                            .add(cbLabels)
                            .add(tfMaxHeight)
                            .add(maxHeightField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cbImRoots)
                            .add(cbReflections)))
                    .add(jpSettingsLayout.createSequentialGroup()
                        .add(23, 23, 23)
                        .add(jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(tfColor)
                            .add(rbColorNorms))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cbNegative)
                    .add(rbColorLevels))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpActions.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Actions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        bReset.setText("Reset"); // NOI18N
        bReset.setToolTipText(resourceMap.getString("drawer.resetTooltip")); // NOI18N
        bReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bResetActionPerformed(evt);
            }
        });

        bUpdate.setText("Draw");
        bUpdate.setToolTipText(resourceMap.getString("drawer.drawTooltip")); // NOI18N
        bUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateAndRepaint(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jpActionsLayout = new org.jdesktop.layout.GroupLayout(jpActions);
        jpActions.setLayout(jpActionsLayout);
        jpActionsLayout.setHorizontalGroup(
            jpActionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpActionsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jpActionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, bUpdate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, bReset, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpActionsLayout.setVerticalGroup(
            jpActionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpActionsLayout.createSequentialGroup()
                .addContainerGap()
                .add(bUpdate)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(bReset)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, container, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jpActions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jpSettings, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jpActions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jpSettings, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(container, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void bResetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bResetActionPerformed
	{//GEN-HEADEREND:event_bResetActionPerformed
		viewRotX = viewRotY = viewTransX = viewTransY = 0.0f;
		zoom = 1.0f;
		repaint(evt);
	}//GEN-LAST:event_bResetActionPerformed

private void repaint(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repaint
		canvas.repaint();
		boolean drawExtras = cbRealRoots.isSelected() || cbImRoots.isSelected();
		cbLabels.setEnabled(drawExtras);
		cbReflections.setEnabled(drawExtras);
		cbNegative.setEnabled(drawExtras);
		tfMaxHeight.setEnabled(drawExtras);
		maxHeightField.setEnabled(drawExtras);
		tfColor.setEnabled(drawExtras);
		rbColorLevels.setEnabled(drawExtras);
		rbColorNorms.setEnabled(drawExtras);		
}//GEN-LAST:event_repaint

private void updateAndRepaint(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateAndRepaint
		updateRoots();
		repaint(evt);
		String text = "Hasse diagram of the root space of " + algebras.algebra.type;
		container.setBorder(javax.swing.BorderFactory.createTitledBorder(null, text, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
}//GEN-LAST:event_updateAndRepaint
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bReset;
    private javax.swing.JButton bUpdate;
    private javax.swing.ButtonGroup bgColorCoding;
    private javax.media.opengl.GLJPanel canvas;
    private javax.swing.JCheckBox cbImRoots;
    private javax.swing.JCheckBox cbLabels;
    private javax.swing.JCheckBox cbNegative;
    private javax.swing.JCheckBox cbRealRoots;
    private javax.swing.JCheckBox cbReflections;
    private javax.swing.JPanel container;
    private javax.swing.JPanel jpActions;
    private javax.swing.JPanel jpSettings;
    private edu.simplie.ui.reusable.UISpinner maxHeightField;
    private javax.swing.JRadioButton rbColorLevels;
    private javax.swing.JRadioButton rbColorNorms;
    private javax.swing.JLabel tfColor;
    private javax.swing.JLabel tfMaxHeight;
    // End of variables declaration//GEN-END:variables

	public void init(GLAutoDrawable drawable)
	{
		this.gl			= drawable.getGL();
		this.glDrawable	= drawable;
		this.context	= drawable.getContext();
		
		sphereObj		= gl.glGenLists(1);
		realRootsObj	= gl.glGenLists(1);
		imRootsObj		= gl.glGenLists(1);
		realReflsObj	= gl.glGenLists(1);
		imReflsObj		= gl.glGenLists(1);
		simpReflsObj	= gl.glGenLists(1);
		realLabelObj	= gl.glGenLists(1);
		imLabelObj		= gl.glGenLists(1);
		
		listContainer = new int[6];
		listContainer[0] = realRootsObj;
		listContainer[1] = imRootsObj;
		listContainer[2] = realReflsObj;
		listContainer[3] = imReflsObj;
		listContainer[4] = realLabelObj;
		listContainer[5] = imLabelObj;
		
		glut = new GLUT();
		
		gl.glNewList(sphereObj, GL.GL_COMPILE);
		glut.glutSolidSphere(0.12d, 8, 8);
		gl.glEndList();
		
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_NORMALIZE);
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void display(GLAutoDrawable drawable)
	{
		// Reset stuff.
		if ((drawable instanceof GLJPanel) &&
				!((GLJPanel) drawable).isOpaque() &&
				((GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent()) {
			gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		} 
		else
		{
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		}
		gl.glPushMatrix();
		
		// Rotate and zoom the coordinate system.
		gl.glScalef(zoom, zoom, zoom);
		gl.glTranslatef(viewTransX, viewTransY, 0.0f);
		gl.glRotatef(viewRotX,1.0f,0.0f,0.0f);
		gl.glRotatef(viewRotY,0.0f,1.0f,0.0f);
		
		if(!cbNegative.isSelected())
			gl.glTranslatef(offset[0],offset[1],offset[2]);	
		
		for(int i = cbNegative.isSelected() ? 0 : 1; i < 2; i++)
		{		
			// Draw the real roots.
			if(cbRealRoots.isSelected())
				gl.glCallList(realRootsObj);

			// Draw the imaginary roots.
			if(cbImRoots.isSelected())
				gl.glCallList(imRootsObj);

			// Draw the weyl reflections.
			if(cbReflections.isSelected())
			{
				gl.glColor3f(0.6f, 0.6f, 0.6f);
				if(cbRealRoots.isSelected())
					gl.glCallList(realReflsObj);
				if(cbImRoots.isSelected())
					gl.glCallList(imReflsObj);
				if(i == 0 && cbRealRoots.isSelected())
					gl.glCallList(simpReflsObj);
			}
			
			// Draw the root labels.
			if(cbLabels.isSelected())
			{
				if(i == 1 && cbNegative.isSelected())
					gl.glColor3f(0.5f,0.0f,0.0f);
				else
					gl.glColor3f(0.0f,0.5f,0.0f);
				if(cbRealRoots.isSelected())
					gl.glCallList(realLabelObj);
				if(cbImRoots.isSelected())
					gl.glCallList(imLabelObj);
			}
			gl.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
		}	
		gl.glPopMatrix();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		float h = (float)height / (float)width;

		gl.glMatrixMode(GL.GL_PROJECTION);

		gl.glLoadIdentity();
		gl.glFrustum(-1.0f, 1.0f, -h, h, 4.0f, 100.0f);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -40.0f);
		
		canvas.repaint();
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
	{
	}

	public void mouseDragged(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();

		Dimension size = e.getComponent().getSize();
		float diffX = ( (float)(x-prevMouseX)/(float)size.width );
		float diffY = ( (float)(prevMouseY-y)/(float)size.height );
				
		if(rotate)
		{	
			viewRotX += 240.0f * diffY;
			viewRotY += 240.0f * diffX;
		}
		else
		{
			viewTransX += 8.0f * diffX / zoom;
			viewTransY += 8.0f * diffY / zoom;
		}
		
		prevMouseX = x;
		prevMouseY = y;
		
		canvas.repaint();
	}

	public void mouseMoved(MouseEvent e)
	{
		prevMouseX = e.getX();
		prevMouseY = e.getY();
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) 
	{
		zoom *= 1.0f - (float) e.getWheelRotation() / 12;
		canvas.repaint();
	}
	
	public void mouseReleased(MouseEvent e)
	{
		rotate = false;
	}

	public void mousePressed(MouseEvent e)
	{
		rotate = (e.getButton() == MouseEvent.BUTTON3) || e.isAltDown();
	}
	
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	
	private void updateRoots()
	{
		// First construct the root system up to the wanted height.
		int maxHeight = Math.abs(maxHeightField.getValue());
		algebras.algebra.rs.construct(maxHeight);
		if(maxHeight == 0)
			maxHeight = algebras.algebra.rs.size();
		
		// Make sure we're in the right GL context.
		if(context == null)
			return;
		context.makeCurrent();
		
		// Reset the lists.
		for(int j = 0; j < listContainer.length; j++)
		{
			gl.glNewList(listContainer[j], GL.GL_COMPILE);
			gl.glEndList();
		}
		
		float[] maxCoor = {0.0f,0.0f,0.0f};
		float[] minCoor = {Float.MAX_VALUE, Float.MAX_VALUE,Float.MAX_VALUE};
		
		// Don't do anything if the algebra is empty.
		if(algebras.algebra == null || algebras.algebra.rank == 0)
			return;
		
		// Determine some variables for positional calculation.
		float[] posX = new float[algebras.algebra.rank];
		float[] posZ = new float[algebras.algebra.rank];
		int[] bounds = algebras.dd.getDiagramBounds();
		float coeffX = (bounds[1] == bounds[0]) ? 0 : 2 / (float) (bounds[1] - bounds[0]);
		float coeffZ = (bounds[2] == bounds[3]) ? 0 : 2 / (float) (bounds[3] - bounds[2]);
		for (int i = 0; i < algebras.algebra.rank; i++)
		{
			posX[i] = (bounds[1] == bounds[0]) ? 0 : (float) (algebras.dd.getNodeByIndex(i).x - bounds[0]) * coeffX - 1;
			posZ[i] = (bounds[2] == bounds[3]) ? 0 : (float) (algebras.dd.getNodeByIndex(i).y - bounds[2]) * coeffZ - 1;
		}
		
		// Stuff for color coding.
		float[] color;
		int colorIndex;
		float colA, colB, colDiff;
		if(rbColorNorms.isSelected())
		{
			// Stuff for root norm color coding.
			colDiff	= algebras.algebra.rs.maxNorm() - algebras.algebra.rs.minNorm();
			colA	= (colDiff == 0) ? 0 : (-1 / colDiff) * (1 - 1/colDiff);
			colB	= (colDiff == 0) ? 0 : -1 * colA * algebras.algebra.rs.maxNorm();
		}
		else
		{
			// Stuff for level decomposition color coding.
			colDiff	= algebras.algebra.rank - algebras.coAlgebra.rank;
			colA	= (colDiff == 0) ? 0 : 1 / (float) Math.pow(2, (int) colDiff);
			colB	= 0;
		}
		colB += 2.0f /3.0f;
		
		// Draw the simple weyl reflections down
		gl.glNewList(simpReflsObj, GL.GL_COMPILE);
		for (int i = 0; i < posX.length; i++)
		{
			gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(posX[i], 0.5f, posZ[i]);
			gl.glVertex3f(posX[i],-0.5f,-posZ[i]);
			gl.glEnd();
		}
		gl.glEndList();
			
		// Construct the roots list.
		for(int j = 0; j < listContainer.length; j++)
		{
			int index = listContainer[j];
			gl.glNewList(index, GL.GL_COMPILE);
			// Loop over every root for every GL list.
			for(int i = 1; i < maxHeight + 1 && i < algebras.algebra.rs.size(); i++)
			{
				Collection<Root> roots = algebras.algebra.rs.get(i);
				for(Iterator it = roots.iterator(); it.hasNext();)
				{
					Root	root	= (Root) it.next();
					float[] pos		= calcPos(root.vector, posX, posZ);
					boolean real	= (root.norm > 0);
					
					for (int k = 0; k < 3; k++)
					{
						maxCoor[k] = Math.max(maxCoor[k],pos[k]);
						minCoor[k] = Math.min(minCoor[k],pos[k]);
					}
					
					if(index == realLabelObj || index == imLabelObj)
					{
						if( (index == imLabelObj && real) || (index == realLabelObj && !real) )
							continue;					
						gl.glRasterPos3f(pos[0] + 0.15f,pos[1] + 0.15f,pos[2] + 0.15f);
						glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, Helper.intArrayToString(root.vector));
					}
					
					// Draw the Weyl reflections.
					if(index == realReflsObj || index == imReflsObj)
					{
						if( (index == imReflsObj && real) || (index == realReflsObj && !real) )
							continue;
						
						int[] dynkinLabels = algebras.algebra.rootToWeight(root.vector);
						for(int k = 0; k < root.vector.length; k++)
						{
							// Only draw reflections upward.
							if(dynkinLabels[k] >= 0)
								continue;
							int[] reflVector = root.vector.clone();
							reflVector[k] -= dynkinLabels[k];
							float[] newPos = calcPos(reflVector, posX, posZ);
							// And draw the line.
							gl.glBegin(GL.GL_LINES);
							gl.glVertex3f(pos[0],pos[1],pos[2]);
							gl.glVertex3f(newPos[0],newPos[1],newPos[2]);
							gl.glEnd();
						}
					}
					
					// Draw the roots.
					if(index == realRootsObj || index == imRootsObj)
					{
						if( (index == imRootsObj && real) || (index == realRootsObj && !real) )
							continue;
						
						// First determine the color.
						if(rbColorNorms.isSelected())
						{
							colorIndex = root.norm;
						}
						else
						{
							int[] levels = algebras.levels(root.vector);
							colorIndex = 0;
							for (int k = 0; k < levels.length; k++)
							{
								colorIndex += (levels[k] % 2) * Math.pow(2,k);
							}

						}
						color = Helper.colorSpectrum((float) colorIndex * colA + colB);
						// Draw the root.
						gl.glColor3f(color[0], color[1], color[2]);
						gl.glPushMatrix();
						gl.glTranslatef(pos[0],pos[1],pos[2]);
						gl.glCallList(sphereObj);
						gl.glPopMatrix();
					}
				}
			}
			gl.glEndList();
		}
		
		for (int i = 0; i < 3; i++)
		{
			offset[i] = -1 * (maxCoor[i]+minCoor[i]) / 2;
		}

	}
	
	private float[] calcPos(int[] rootVector, float[] posX, float[] posZ)
	{
		float pos[] = { 0.0f, -0.5f, 0.0f };
		for (int i = 0; i < rootVector.length; i++)
		{
			pos[0] += rootVector[i] * posX[i];
			pos[2] += rootVector[i] * posZ[i];
			pos[1] += rootVector[i];
		}
		return pos;
	}
}
