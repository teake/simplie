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

package edu.rug.hep.simplie.ui;

import edu.rug.hep.simplie.CAlgebraComposite;
import edu.rug.hep.simplie.algebra.CRoot;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
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
 */
public class RootSpaceDrawer extends javax.swing.JPanel implements GLEventListener, MouseMotionListener
{
	private GLAutoDrawable glDrawable;
	private GL gl;
	private GLContext context;
	
	// Define some colors for the roots.
	private float negCol[]	= { 0.8f, 0.2f, 0.0f, 1.0f };
	private float posCol[]	= { 0.0f, 0.8f, 0.2f, 1.0f };
	private float imPosCol[]= { 0.13f, 0.7f, 0.66f, 1.0f };
	private float imNegCol[]= { 1.0f, 0.84f, 0.0f, 1.0f };
	private float reflCol[]	= { 0.8f, 0.8f, 0.8f, 1.0f };
	
	private float view_rotx = 0.0f, view_roty = 0.0f;
	private float zoom		= 1.0f;
	private int prevMouseX, prevMouseY;
	private int rootsObj;
	
	private CAlgebraComposite algebras;
	
	/** Creates new form RootSpaceDrawer */
	public RootSpaceDrawer()
	{
		initComponents();
		
		canvas.setVisible(true);
		canvas.addGLEventListener(this);
		canvas.addMouseMotionListener(this);
	}
	
	public void setAlgebrasComposite(CAlgebraComposite algebras)
	{
		this.algebras = algebras;
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        bDrawRoots = new javax.swing.JButton();
        bReset = new javax.swing.JButton();
        cbRealRoots = new javax.swing.JCheckBox();
        cbImRoots = new javax.swing.JCheckBox();
        cbReflections = new javax.swing.JCheckBox();
        container = new javax.swing.JPanel();
        canvas = new javax.media.opengl.GLJPanel();

        bDrawRoots.setText("Draw root space");
        bDrawRoots.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bDrawRootsActionPerformed(evt);
            }
        });

        bReset.setText("Reset position");
        bReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bResetActionPerformed(evt);
            }
        });

        cbRealRoots.setSelected(true);
        cbRealRoots.setText("Draw real roots");
        cbRealRoots.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cbRealRootsActionPerformed(evt);
            }
        });

        cbImRoots.setSelected(true);
        cbImRoots.setText("Draw imaginary roots");
        cbImRoots.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cbImRootsActionPerformed(evt);
            }
        });

        cbReflections.setSelected(true);
        cbReflections.setText("Draw Weyl reflections");
        cbReflections.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cbReflectionsActionPerformed(evt);
            }
        });

        container.setBorder(javax.swing.BorderFactory.createTitledBorder("Root space"));

        org.jdesktop.layout.GroupLayout canvasLayout = new org.jdesktop.layout.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 577, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 107, Short.MAX_VALUE)
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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(container, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(bDrawRoots)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(bReset)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cbRealRoots)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cbImRoots)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cbReflections)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(bReset)
                    .add(cbRealRoots)
                    .add(cbImRoots)
                    .add(cbReflections)
                    .add(bDrawRoots))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(container, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void bDrawRootsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bDrawRootsActionPerformed
	{//GEN-HEADEREND:event_bDrawRootsActionPerformed
		if(algebras.algebra == null)
			return;	
		updateRoots();
		String text = "Root space of " + algebras.algebra.type;
		if(!algebras.algebra.finite)
			text += " up to height " + (algebras.algebra.rs.size() - 1);
		container.setBorder(javax.swing.BorderFactory.createTitledBorder(null, text, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
}//GEN-LAST:event_bDrawRootsActionPerformed

	private void bResetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bResetActionPerformed
	{//GEN-HEADEREND:event_bResetActionPerformed
		view_rotx = view_roty = 0.0f;
		zoom = 1.0f;
		canvas.repaint();
	}//GEN-LAST:event_bResetActionPerformed

	private void cbRealRootsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cbRealRootsActionPerformed
	{//GEN-HEADEREND:event_cbRealRootsActionPerformed
		updateRoots();
	}//GEN-LAST:event_cbRealRootsActionPerformed

	private void cbImRootsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cbImRootsActionPerformed
	{//GEN-HEADEREND:event_cbImRootsActionPerformed
		updateRoots();
	}//GEN-LAST:event_cbImRootsActionPerformed

	private void cbReflectionsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cbReflectionsActionPerformed
	{//GEN-HEADEREND:event_cbReflectionsActionPerformed
		updateRoots();
}//GEN-LAST:event_cbReflectionsActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bDrawRoots;
    private javax.swing.JButton bReset;
    private javax.media.opengl.GLJPanel canvas;
    private javax.swing.JCheckBox cbImRoots;
    private javax.swing.JCheckBox cbRealRoots;
    private javax.swing.JCheckBox cbReflections;
    private javax.swing.JPanel container;
    // End of variables declaration//GEN-END:variables

	public void init(GLAutoDrawable drawable)
	{
		this.gl			= drawable.getGL();
		this.glDrawable	= drawable;
		this.context	= drawable.getContext();
		
		rootsObj = gl.glGenLists(1);
		
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_POINT_SMOOTH);
		gl.glEnable(GL.GL_NORMALIZE);
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		// TODO: implement the following
		/*
		if(gl.isExtensionAvailable("GL_EXT_point_parameters"))
		{
			float[] quadratic = { 0.25f, 0.0f, 1/60.0f };
			gl.glPointParameterfvEXT(GL.GL_DISTANCE_ATTENUATION_EXT, quadratic, 0);
			gl.glPointParameterfEXT(GL.GL_POINT_FADE_THRESHOLD_SIZE_EXT, 1.0f);
		}
		 */
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
		gl.glRotatef(view_rotx,1.0f,0.0f,0.0f);
		gl.glRotatef(view_roty,0.0f,1.0f,0.0f);
		gl.glScalef(zoom, zoom, zoom);
		gl.glPointSize(16.0f * zoom);
		
		// Draw the roots.
		gl.glCallList(rootsObj);
		
		gl.glPopMatrix();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		float h = (float)height / (float)width;

		gl.glMatrixMode(GL.GL_PROJECTION);

		gl.glLoadIdentity();
		gl.glFrustum(-1.0f, 1.0f, -h, h, 10.0f, 60.0f);
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
		
		// Shift down: zoom
		if(e.isShiftDown())
		{
			float diff = (float)(prevMouseY-y)/(float)size.height;
			zoom *= 1 + 2*diff;
		}
		else
		{
			float thetaY = 240.0f * ( (float)(x-prevMouseX)/(float)size.width);
			float thetaX = 240.0f * ( (float)(prevMouseY-y)/(float)size.height);
			view_rotx -= thetaX;
			view_roty -= thetaY;
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

	private void updateRoots()
	{
		if(context == null)
			return;
		context.makeCurrent();
		
		gl.glNewList(rootsObj, GL.GL_COMPILE);
		
		if(algebras.algebra != null && algebras.algebra.rank > 0)
		{
			int rank		= algebras.algebra.rank;
			int numPerCoor	= (int) Math.floor(rank / 3);
			int remainder	= rank % 3;
					
			// The rest of the roots.
			for(int i = 1; i < algebras.algebra.rs.size(); i++)
			{
				Collection<CRoot> roots = algebras.algebra.rs.get(i);
				for(Iterator it = roots.iterator(); it.hasNext();)
				{
					CRoot root	= (CRoot) it.next();
					
					if(root.norm > 0 && !cbRealRoots.isSelected())
						continue;
					if(root.norm <= 0 && !cbImRoots.isSelected())
						continue;
					
					float[] pos	= calcPos(root.vector, numPerCoor, remainder);
					
					if(cbReflections.isSelected())
					{
						int[] dynkinLabels = algebras.algebra.rootToWeight(root.vector);
						for(int j = 0; j < rank; j++)
						{
							int[] reflVector = root.vector.clone();
							reflVector[j] -= dynkinLabels[j];
							drawReflection(pos, calcPos(reflVector, numPerCoor, remainder));
						}
					}
					
					// Draw a positive root.
					drawRoot((root.norm > 0 ? posCol : imPosCol),pos[0],pos[1],pos[2]);
					// Draw a negative root.
					drawRoot((root.norm > 0 ? negCol : imNegCol),-pos[0],-pos[1],-pos[2]);
				}
			}
		}
		gl.glEndList();
		
		canvas.repaint();
	}
	
	private float[] calcPos(int[] rootVector, int numPerCoor, int remainder)
	{
		float pos[] = { 0.0f, 0.0f, 0.0f };
		for(int j = 0; j < 3; j++)
		{
			for(int k = 0; k < numPerCoor; k++)
			{
				pos[j] += rootVector[remainder + numPerCoor*j + k];
			}
			if(j < remainder)
			{
				pos[j] += rootVector[j];
			}
		}
		return pos;
	}
	
	private void drawRoot(float[] color, float x, float y, float z)
	{
		gl.glColor3f(color[0],color[1],color[2]);
		gl.glBegin(GL.GL_POINTS);
			gl.glVertex3f(x, y, z);
		gl.glEnd();				
	}
	
	private void drawReflection(float[] pos1, float[] pos2)
	{
		gl.glColor3f(reflCol[0],reflCol[1],reflCol[2]);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(pos1[0],pos1[1],pos1[2]);
			gl.glVertex3f(pos2[0],pos2[1],pos2[2]);
		gl.glEnd();
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(-pos1[0],-pos1[1],-pos1[2]);
			gl.glVertex3f(-pos2[0],-pos2[1],-pos2[2]);
		gl.glEnd();
	}
}