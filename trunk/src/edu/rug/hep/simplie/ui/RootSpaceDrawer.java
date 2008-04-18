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

import com.sun.opengl.util.GLUT;
import edu.rug.hep.simplie.CAlgebraComposite;
import edu.rug.hep.simplie.Helper;
import edu.rug.hep.simplie.algebra.CRoot;
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
	
	// Define some colors for the roots.
	private float reflCol[]		= { 0.6f, 0.6f, 0.6f };
	private float maxColorIm[]	= { 1.0f, 0.84f, 0.0f };
	private float minColorIm[]	= { 0.8f, 0.2f, 0.0f };
	private float maxColorReal[]= { 0.13f, 0.7f, 0.66f };
	private float minColorReal[]= { 0.0f, 0.8f, 0.2f };
	
	
	// Variables for rotations, translation and zoom.
	private float viewRotX = 0.0f, viewRotY = 0.0f;
	private float viewTransX = 0.0f, viewTransY = 0.0f;
	private float zoom = 1.0f;
	private int prevMouseX, prevMouseY;
	private boolean rightMouseDown = false;
	
	// Indices for the GL display lists.
	private int realReflsObj;
	private int imReflsObj;
	private int realRootsObj;
	private int imRootsObj;
	private int sphereObj;
	private int[] listContainer;
	
	private CAlgebraComposite algebras;
	
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

        container = new javax.swing.JPanel();
        canvas = new javax.media.opengl.GLJPanel();
        jPanel1 = new javax.swing.JPanel();
        cbRealRoots = new javax.swing.JCheckBox();
        cbImRoots = new javax.swing.JCheckBox();
        cbReflections = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        bReset = new javax.swing.JButton();
        bDrawRoots = new javax.swing.JButton();

        container.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Root space", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        canvas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.jdesktop.layout.GroupLayout canvasLayout = new org.jdesktop.layout.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 414, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 123, Short.MAX_VALUE)
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

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

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cbRealRoots)
                    .add(cbImRoots)
                    .add(cbReflections))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(cbRealRoots)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbImRoots)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbReflections)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Actions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        bReset.setText("Reset position");
        bReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bResetActionPerformed(evt);
            }
        });

        bDrawRoots.setText("Draw root space");
        bDrawRoots.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bDrawRootsActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(bReset, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .add(bDrawRoots))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(bDrawRoots)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(bReset)
                .addContainerGap(12, Short.MAX_VALUE))
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
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
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
		canvas.repaint();
		String text = "Root space of " + algebras.algebra.type;
		if(!algebras.algebra.finite)
			text += " up to height " + (algebras.algebra.rs.size() - 1);
		container.setBorder(javax.swing.BorderFactory.createTitledBorder(null, text, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
}//GEN-LAST:event_bDrawRootsActionPerformed

	private void bResetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bResetActionPerformed
	{//GEN-HEADEREND:event_bResetActionPerformed
		viewRotX = viewRotY = viewTransX = viewTransY = 0.0f;
		zoom = 1.0f;
		canvas.repaint();
	}//GEN-LAST:event_bResetActionPerformed

	private void cbRealRootsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cbRealRootsActionPerformed
	{//GEN-HEADEREND:event_cbRealRootsActionPerformed
		canvas.repaint();
	}//GEN-LAST:event_cbRealRootsActionPerformed

	private void cbImRootsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cbImRootsActionPerformed
	{//GEN-HEADEREND:event_cbImRootsActionPerformed
		canvas.repaint();
	}//GEN-LAST:event_cbImRootsActionPerformed

	private void cbReflectionsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cbReflectionsActionPerformed
	{//GEN-HEADEREND:event_cbReflectionsActionPerformed
		canvas.repaint();
}//GEN-LAST:event_cbReflectionsActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bDrawRoots;
    private javax.swing.JButton bReset;
    private javax.media.opengl.GLJPanel canvas;
    private javax.swing.JCheckBox cbImRoots;
    private javax.swing.JCheckBox cbRealRoots;
    private javax.swing.JCheckBox cbReflections;
    private javax.swing.JPanel container;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

	public void init(GLAutoDrawable drawable)
	{
		this.gl			= drawable.getGL();
		this.glDrawable	= drawable;
		this.context	= drawable.getContext();
		
		sphereObj = gl.glGenLists(1);
		realRootsObj	= gl.glGenLists(1);
		imRootsObj		= gl.glGenLists(1);
		realReflsObj	= gl.glGenLists(1);
		imReflsObj		= gl.glGenLists(1);
		
		listContainer = new int[4];
		listContainer[0] = realRootsObj;
		listContainer[1] = imRootsObj;
		listContainer[2] = realReflsObj;
		listContainer[3] = imReflsObj;
		
		glut = new GLUT();
		
		gl.glNewList(sphereObj, GL.GL_COMPILE);
		glut.glutSolidSphere(0.12d, 8, 8);
		gl.glEndList();
		
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_NORMALIZE);
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
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
		
		// Draw the real roots.
		if(cbRealRoots.isSelected())
			gl.glCallList(realRootsObj);

		// Draw the imaginary roots.
		if(cbImRoots.isSelected())
			gl.glCallList(imRootsObj);
		
		// Draw the weyl reflections.
		if(cbReflections.isSelected())
		{
			gl.glDisable(GL.GL_LIGHTING);
			gl.glColor3f(reflCol[0], reflCol[1], reflCol[2]);
			if(cbRealRoots.isSelected())
				gl.glCallList(realReflsObj);
			if(cbImRoots.isSelected())
				gl.glCallList(imReflsObj);
			gl.glEnable(GL.GL_LIGHTING);
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
		
		if(rightMouseDown)
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
		rightMouseDown = false;
	}

	public void mousePressed(MouseEvent e)
	{
		rightMouseDown = (e.getButton() == MouseEvent.BUTTON3);
	}
	
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	
	private void updateRoots()
	{
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
		
		// Stuff for color mixing.
		float normMinReal	= algebras.algebra.rs.minNormReal();
		float normDiffReal	= algebras.algebra.rs.maxNorm() - normMinReal;
		float normMinIm		= algebras.algebra.rs.minNorm();
		float[] col;
		
		// Construct the roots list.
		for(int j = 0; j < listContainer.length; j++)
		{
			int index = listContainer[j];
			gl.glNewList(index, GL.GL_COMPILE);
			for(int i = 1; i < algebras.algebra.rs.size(); i++)
			{
				Collection<CRoot> roots = algebras.algebra.rs.get(i);
				for(Iterator it = roots.iterator(); it.hasNext();)
				{
					CRoot root	= (CRoot) it.next();
					float[] pos	= calcPos(root.vector, posX, posZ);
					
					if(index == realReflsObj && root.norm > 0)
						addReflections(pos, root.vector, posX, posZ);
					if(index == imReflsObj && root.norm <= 0)
						addReflections(pos, root.vector, posX, posZ);
					if(index == realRootsObj && root.norm > 0)
					{
						if(normDiffReal == 0)
							col = maxColorReal;
						else
							col = Helper.mixColors(maxColorReal, minColorReal, ((float) root.norm - normMinReal) / normDiffReal);
						addRoot(pos, col);
					}
					if(index == imRootsObj && root.norm <= 0)
					{
						if(normMinIm == 0)
							col = maxColorIm;
						else
							col = Helper.mixColors(maxColorIm, minColorIm,(normMinIm - (float) root.norm) / normMinIm);
						addRoot(pos, col);
					}
				}
			}
			gl.glEndList();
		}
	}
	
	private float[] calcPos(int[] rootVector, float[] posX, float[] posZ)
	{
		float pos[] = { 0.0f, 0.0f, 0.0f };
		for (int i = 0; i < rootVector.length; i++)
		{
			pos[0] += rootVector[i] * posX[i];
			pos[2] += rootVector[i] * posZ[i];
			pos[1] += rootVector[i];
		}
		return pos;
	}
	
	private void addRoot(float[] pos, float[] col)
	{
		// Draw a positive root.
		drawRoot(col,pos[0],pos[1],pos[2]);
		// Draw a negative root.
		drawRoot(col,-pos[0],-pos[1],-pos[2]);
	}
	
	private void addReflections(float[] pos, int[] vector, float[] posX, float[] posZ)
	{
		int[] dynkinLabels = algebras.algebra.rootToWeight(vector);
		for(int i = 0; i < vector.length; i++)
		{
			int[] reflVector = vector.clone();
			reflVector[i] -= dynkinLabels[i];
			drawReflection(pos, calcPos(reflVector, posX, posZ));
		}
	}
	
	private void drawRoot(float[] color, float x, float y, float z)
	{
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);
		gl.glCallList(sphereObj);
		gl.glPopMatrix();
	}
	
	private void drawReflection(float[] pos1, float[] pos2)
	{
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
