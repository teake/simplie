/*
 * DynkinDiagramPanel.java
 *
 * Created on 20 maart 2007, 17:40
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

import edu.simplie.*;
import edu.simplie.dynkindiagram.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import net.sf.epsgraphics.EpsGraphics;
import org.jdesktop.application.Action;


/**
 * Displays a Dynkin diagram and allows its editing via direct user-interaction.
 * 
 * @author  Teake Nutma
 * @version $Revision$, $Date$
 */
public class DynkinDiagramPanel extends javax.swing.JPanel implements DiagramListener, KeyListener
{
	// Constants indicating the status.
	public static final int STATUS_IDLE = 0;
	public static final int STATUS_PREVIEW = 1;
	public static final int STATUS_ADDCON = 2;
	public static final int STATUS_REMCON = 3;
	public static final int STATUS_ADDCOMP = 4;
	public static final int STATUS_REMCOMP = 5;
	
	// Variables storing the actual status.
	private int status;
	private int prev_status;
	
	private DynkinDiagram dd;
	
	// Some variables controlling the way the diagram looks.
	private int spacing;
	private int radius;
	private int offset;
	
	private int			connectionType;
	private DynkinNode modificationFrom;
	
	// Keeps track of the location the context menu.
	private Point contextXY;
	// Keeps track of the location of the mouse pointer.
	private Point xy;
	
	private boolean shiftDown;
	private boolean contextVisible;
	private AlgebraComposite algebras;
	
	/** Creates new form DynkinDiagramPanel */
	public DynkinDiagramPanel()
	{
		status = STATUS_IDLE;
		prev_status = STATUS_IDLE;
		
		spacing = 50;
		radius	= 8;
		offset	= 50;
		
		modificationFrom = null;
		
		contextXY = new Point(0,0);
		xy = new Point(-1,-1);
		
		shiftDown = false;
		contextVisible = false;
		initComponents();
		diagram.addKeyListener(this);
	}

	public void setAlgebraComposite(AlgebraComposite algebras)
	{
		this.algebras = algebras;
		this.dd = algebras.dd;
		algebras.dd.addListener(this);
	}

	
	public void diagramChanged()
	{
		diagram.repaint();
		setTitle("Dynkin diagram of " + algebras.getDynkinDiagramType());
	}

	@Action
	public void clear()
	{
		dd.clear();
	}

	@Action
	public void changeOrder()
	{
		int order = ( rbBottomTop.isSelected() ) ? DynkinDiagram.SORT_BOTTOMTOP : DynkinDiagram.SORT_TOPBOTTOM;
		dd.setSortOrder(order);
	}

	@Action
	public void toEPS()
	{
		JFileChooser chooser = new JFileChooser("");
		chooser.setSelectedFile(new File("DynkinDiagram.eps"));
		chooser.setDialogTitle("to EPS");
		if ( chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION )
		{
			FileOutputStream outputStream = null;
			EpsGraphics eps = null;
			Point[] bounds = dd.getDiagramBounds();
			Point min = trans(bounds[0]);
			Point max = trans(bounds[1]);
			try
			{
				outputStream = new FileOutputStream(chooser.getSelectedFile().getAbsolutePath());
				eps = new EpsGraphics("Projection", outputStream, 
						min.x-offset,min.y-offset, max.x+offset,max.y+offset,
						net.sf.epsgraphics.ColorMode.COLOR_RGB);
				drawDiagram(eps);
			}
			catch(Exception ex){}
			finally
			{
				try
				{
					eps.flush();
					eps.close();
				}
				catch(Exception ex){}
			}
		}
	}

	public void keyPressed(KeyEvent e)
	{
		if(e.isShiftDown())
		{
			shiftDown = true;
			diagram.repaint();
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		if(!e.isShiftDown())
		{
			shiftDown = false;
			diagram.repaint();
		}
	}
		
	public void keyTyped(KeyEvent e){}

	private Point invTrans(Point p)
	{
		int x = Math.round(((float) p.x - offset) / spacing);
		int y = Math.round(((float) p.y - offset) / spacing);
		return new Point(x,y);
	}
	private Point trans(Point p)
	{
		int x = spacing * p.x + offset;
		int y = spacing * p.y + offset;
		return new Point(x,y);
	}
	private Point trans(DynkinNode node)
	{
		return trans(new Point(node.x,node.y));
	}

	public void drawDiagram(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		Point mouseP = trans(xy);

		// Don't draw a preview if the context menu is visible.
		if(!contextMenu.isVisible())
		{
			switch(status)
			{
				case STATUS_PREVIEW:
					DynkinNode node = dd.getLastAddedNode();
					if(shiftDown && node != null)
					{
						Helper.drawConnection(g2,Color.LIGHT_GRAY,DynkinConnection.TYPE_SINGLE,trans(node),mouseP,radius);
					}
					Helper.drawFilledCircle(g2, Color.WHITE, Color.GRAY, mouseP, radius);
					break;
				case STATUS_ADDCOMP:
				case STATUS_ADDCON:
					Point modP = trans(modificationFrom);
					if(status == STATUS_ADDCOMP)
						Helper.drawCompactCon(g2, Color.GRAY,modP,mouseP);
					else
						Helper.drawConnection(g2, Color.LIGHT_GRAY, connectionType, modP, mouseP, radius);
					break;
				default:
					break;			
			}
		}

		// Draw the diagram, if it exists.
		if(dd == null)
			return;

		// Draw the connections first.
		g2.setColor(Color.BLACK);
		for (DynkinConnection conn : dd.connections)
		{
			Helper.drawConnection(g2, Color.BLACK, conn.type, trans(conn.fromNode), trans(conn.toNode), radius);
		}
		// Secondly the compact pair indicators.
		for(CompactPair pair : dd.compactPairs)
		{
			Helper.drawCompactCon(g2, Color.BLACK, trans(pair.node1), trans(pair.node2));
		}
		// Now draw the nodes.
		for (int i = 0; i < dd.rank(); i++)
		{
			DynkinNode node = dd.nodes.get(i);
			Point nodeP = trans(node);
			Color color;

			if(node.isEnabled() || node.isCompact())
				color = Color.WHITE;
			else if(node.isDisconnected())
				color = Color.ORANGE;
			else
				color = Color.GRAY;

			Helper.drawFilledCircle(g2,color,Color.BLACK,nodeP,radius);

			if(node.isCompact())
			{
				if(node.isEnabled())
					color = Color.WHITE;
				else if(node.isDisconnected())
					color = Color.ORANGE;
				else
					color = Color.GRAY;

				Helper.drawFilledCircle(g2,color,Color.BLACK,nodeP,radius/2);
			}
			if(rbNodeOrder.isSelected())
			{
				g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
				g2.drawString(Helper.intToString(i+1), nodeP.x + radius, nodeP.y + radius + 10);
			}
			else
			{
				int cox		= algebras.algebra.coxeterLabels[i];
				int dualCox = algebras.algebra.dualCoxeterLabels[i];
				g2.setFont(new Font("Monospaced", Font.ITALIC, 12));
				g2.drawString(Helper.intToString(dualCox), nodeP.x + radius, nodeP.y + radius + 10);
				if(cox != dualCox)
				{
					g2.setFont(new Font("Monospaced",Font.ITALIC|Font.BOLD, 12));
					g2.drawString(Helper.intToString(cox), nodeP.x + radius, nodeP.y + radius + 22);
				}
			}

		}
	}
	
	/**
	 * Sets the title in the border
	 *
	 * @param	text	The title of the dynkin diagram.
	 */
	public void setTitle(String text)
	{
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, text, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
	}

	private void setStatus(int new_status)
	{
		status		= new_status;
		prev_status = new_status;
		diagram.repaint();
	}
	
	private void setupMenu(DynkinNode node)
	{
		boolean onNode = (node!=null);
	
		menuAddNode.setVisible(!onNode);
		
		menuAddConnection.setVisible(onNode);
		menuRemoveConnection.setVisible(onNode);
		menuSeparator1.setVisible(onNode);
		menuRemoveNode.setVisible(onNode);
		/*
		menuToggleCompact.setVisible(onNode);
		menuAddCompactPair.setVisible(onNode);
		menuRemoveCompactPair.setVisible(onNode);
		menuSeparator2.setVisible(onNode);
		 */
		menuToggleCompact.setVisible(false);
		menuAddCompactPair.setVisible(false);
		menuRemoveCompactPair.setVisible(false);
		menuSeparator2.setVisible(false);
		
		menuState.setVisible(onNode);
		if(onNode)
		{
			switch (node.getState())
			{
				case DynkinNode.STATE_ENABLED:
					menuStateEnabled.setSelected(true);
					break;
				case DynkinNode.STATE_DISABLED:
					menuStateDisabled.setSelected(true);
					break;
				case DynkinNode.STATE_ALWAYS_LEVEL:
					menuStateLevel.setSelected(true);
					break;
			}
		}
	}
	
	private void startModify(int type, int action)
	{
		DynkinNode node = dd.getNodeByCoor(contextXY);
		if(node == null)
		{
			tf_status.setText("Invalid start point specified.");
			return;
		}
		connectionType = type;
		setStatus(action);
		tf_status.setText("Modifying ...");
		modificationFrom = node;
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contextVisible = false;
	}
	
	private void stopModify()
	{
		setStatus(STATUS_PREVIEW);
		modificationFrom = null;
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contextMenu = new javax.swing.JPopupMenu();
        menuAddConnection = new javax.swing.JMenu();
        menuAddSingleConnection = new javax.swing.JMenuItem();
        menuAddDoubleConnection = new javax.swing.JMenuItem();
        menuAddTripleConnection = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        menuAddQuadrupleConnection = new javax.swing.JMenuItem();
        menuAddSpecialDoubleConnection = new javax.swing.JMenuItem();
        menuRemoveConnection = new javax.swing.JMenuItem();
        menuSeparator1 = new javax.swing.JSeparator();
        menuToggleCompact = new javax.swing.JMenuItem();
        menuAddCompactPair = new javax.swing.JMenuItem();
        menuRemoveCompactPair = new javax.swing.JMenuItem();
        menuSeparator2 = new javax.swing.JSeparator();
        menuState = new javax.swing.JMenu();
        menuStateEnabled = new javax.swing.JRadioButtonMenuItem();
        menuStateDisabled = new javax.swing.JRadioButtonMenuItem();
        menuStateLevel = new javax.swing.JRadioButtonMenuItem();
        menuAddNode = new javax.swing.JMenuItem();
        menuRemoveNode = new javax.swing.JMenuItem();
        stateButtonGroup = new javax.swing.ButtonGroup();
        nodeLabelGroup = new javax.swing.ButtonGroup();
        orderGroup = new javax.swing.ButtonGroup();
        diagram = new javax.swing.JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawDiagram(g);
            }
        };
        tf_status = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        rbCoxeterLabels = new javax.swing.JRadioButton();
        rbNodeOrder = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        bToEPS = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        rbBottomTop = new javax.swing.JRadioButton();
        rbTopBottom = new javax.swing.JRadioButton();

        menuAddConnection.setText("Add connection");

        menuAddSingleConnection.setText("Single");
        menuAddSingleConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddSingleConnectionActionPerformed(evt);
            }
        });
        menuAddConnection.add(menuAddSingleConnection);

        menuAddDoubleConnection.setText("Double");
        menuAddDoubleConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddDoubleConnectionActionPerformed(evt);
            }
        });
        menuAddConnection.add(menuAddDoubleConnection);

        menuAddTripleConnection.setText("Triple");
        menuAddTripleConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddTripleConnectionActionPerformed(evt);
            }
        });
        menuAddConnection.add(menuAddTripleConnection);
        menuAddConnection.add(jSeparator1);

        menuAddQuadrupleConnection.setText("Quadruple");
        menuAddQuadrupleConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddQuadrupleConnectionActionPerformed(evt);
            }
        });
        menuAddConnection.add(menuAddQuadrupleConnection);

        menuAddSpecialDoubleConnection.setText("Special double");
        menuAddSpecialDoubleConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddSpecialDoubleConnectionActionPerformed(evt);
            }
        });
        menuAddConnection.add(menuAddSpecialDoubleConnection);

        contextMenu.add(menuAddConnection);

        menuRemoveConnection.setText("Remove connection");
        menuRemoveConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveConnectionActionPerformed(evt);
            }
        });
        contextMenu.add(menuRemoveConnection);
        contextMenu.add(menuSeparator1);

        menuToggleCompact.setText("Compact toggle");
        menuToggleCompact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuToggleCompactActionPerformed(evt);
            }
        });
        contextMenu.add(menuToggleCompact);

        menuAddCompactPair.setText("Create compact pair");
        menuAddCompactPair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddCompactPairActionPerformed(evt);
            }
        });
        contextMenu.add(menuAddCompactPair);

        menuRemoveCompactPair.setText("Remove compact pair");
        menuRemoveCompactPair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveCompactPairActionPerformed(evt);
            }
        });
        contextMenu.add(menuRemoveCompactPair);
        contextMenu.add(menuSeparator2);

        menuState.setText("State");

        stateButtonGroup.add(menuStateEnabled);
        menuStateEnabled.setSelected(true);
        menuStateEnabled.setText("Enabled");
        menuStateEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStateEnabledActionPerformed(evt);
            }
        });
        menuState.add(menuStateEnabled);

        stateButtonGroup.add(menuStateDisabled);
        menuStateDisabled.setText("Disabled");
        menuStateDisabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStateDisabledActionPerformed(evt);
            }
        });
        menuState.add(menuStateDisabled);

        stateButtonGroup.add(menuStateLevel);
        menuStateLevel.setText("Always level");
        menuStateLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStateLevelActionPerformed(evt);
            }
        });
        menuState.add(menuStateLevel);

        contextMenu.add(menuState);

        menuAddNode.setText("Add node");
        menuAddNode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddNodeActionPerformed(evt);
            }
        });
        contextMenu.add(menuAddNode);

        menuRemoveNode.setText("Remove node");
        menuRemoveNode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveNodeActionPerformed(evt);
            }
        });
        contextMenu.add(menuRemoveNode);

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dynkin diagram", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        diagram.setBackground(new java.awt.Color(255, 255, 255));
        diagram.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        diagram.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                diagramMouseMoved(evt);
            }
        });
        diagram.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                diagramMouseReleased(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                diagramMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                diagramMouseEntered(evt);
            }
        });

        tf_status.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tf_status.setText("(Right) click to add a node to the diagram.");

        org.jdesktop.layout.GroupLayout diagramLayout = new org.jdesktop.layout.GroupLayout(diagram);
        diagram.setLayout(diagramLayout);
        diagramLayout.setHorizontalGroup(
            diagramLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, tf_status, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
        );
        diagramLayout.setVerticalGroup(
            diagramLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(diagramLayout.createSequentialGroup()
                .addContainerGap(298, Short.MAX_VALUE)
                .add(tf_status))
        );

        nodeLabelGroup.add(rbCoxeterLabels);
        rbCoxeterLabels.setText("Coxeter labels");

        nodeLabelGroup.add(rbNodeOrder);
        rbNodeOrder.setSelected(true);
        rbNodeOrder.setText("Node order");
        rbNodeOrder.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbNodeOrderStateChanged(evt);
            }
        });

        jLabel1.setText("Node labels:");

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getActionMap(DynkinDiagramPanel.class, this);
        bToEPS.setAction(actionMap.get("toEPS")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(DynkinDiagramPanel.class);
        bToEPS.setText(resourceMap.getString("projector.toEps")); // NOI18N

        jButton1.setAction(actionMap.get("clear")); // NOI18N
        jButton1.setText(resourceMap.getString("projector.clear")); // NOI18N

        jLabel2.setText("Node order:");

        rbBottomTop.setAction(actionMap.get("changeOrder")); // NOI18N
        orderGroup.add(rbBottomTop);
        rbBottomTop.setSelected(true);
        rbBottomTop.setText("Bottom to top");

        rbTopBottom.setAction(actionMap.get("changeOrder")); // NOI18N
        orderGroup.add(rbTopBottom);
        rbTopBottom.setText("Top to bottom");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(bToEPS, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(rbNodeOrder)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(rbCoxeterLabels)
                    .add(jLabel2)
                    .add(rbBottomTop)
                    .add(rbTopBottom))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jButton1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(bToEPS)
                .add(18, 18, 18)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(rbNodeOrder)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbCoxeterLabels)
                .add(18, 18, 18)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(rbBottomTop)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbTopBottom)
                .addContainerGap(94, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(diagram, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, diagram, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	
	private void menuAddSpecialDoubleConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuAddSpecialDoubleConnectionActionPerformed
	{//GEN-HEADEREND:event_menuAddSpecialDoubleConnectionActionPerformed
		startModify(DynkinConnection.TYPE_SPECIAL_DOUBLE, STATUS_ADDCON);
}//GEN-LAST:event_menuAddSpecialDoubleConnectionActionPerformed

	private void menuAddQuadrupleConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuAddQuadrupleConnectionActionPerformed
	{//GEN-HEADEREND:event_menuAddQuadrupleConnectionActionPerformed
		startModify(DynkinConnection.TYPE_QUADRUPLE, STATUS_ADDCON);
}//GEN-LAST:event_menuAddQuadrupleConnectionActionPerformed
	
private void diagramMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diagramMouseExited
	prev_status = status;
	status		= STATUS_IDLE;
	diagram.repaint();
}//GEN-LAST:event_diagramMouseExited

private void diagramMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diagramMouseMoved
	Point xy_new = invTrans(new Point(evt.getX(), evt.getY()));
	if(xy!=xy_new && xy_new.x >= 0 && xy_new.y >= 0)
	{
		xy = xy_new;
		diagram.repaint();
	}
}//GEN-LAST:event_diagramMouseMoved

private void diagramMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diagramMouseReleased
	// Don't do anything if the diagram is locked.
	if(dd.isLocked())
		return;
	
	DynkinNode node = dd.getNodeByCoor(xy);
	
	if(evt.getButton() == MouseEvent.BUTTON3 || evt.isControlDown())
	{
		setupMenu(node);
		contextXY = xy;
		contextMenu.show(diagram,evt.getX(),evt.getY());
		contextVisible = true;
		return;
	}
	
	if(contextVisible)
	{
		contextVisible = false;
		diagram.repaint();
		return;
	}
	
	// Left click and not modifying connection: add a node.
	if(evt.getButton() == MouseEvent.BUTTON1 && status == STATUS_PREVIEW && !evt.isAltDown())
	{
		int connectionToLast = (evt.isShiftDown()) ? 1 : 0;
		tf_status.setText(dd.addNode(xy, connectionToLast));
		return;
	}
	
	// Middle mouse or alt+left: toggle a node.
	if(evt.getButton() == MouseEvent.BUTTON2 || (evt.getButton() == MouseEvent.BUTTON1 && evt.isAltDown() ) )
	{
		stopModify();
		tf_status.setText(dd.toggleNode(node));
	}
	
	if(status == STATUS_ADDCON || status == STATUS_REMCON)
	{
		boolean add = (status == STATUS_ADDCON) ? true : false;
		tf_status.setText(dd.modifyConnection(modificationFrom, node, connectionType, add));
		stopModify();
	}
	
	if(status == STATUS_ADDCOMP || status == STATUS_REMCOMP)
	{
		boolean add = (status == STATUS_ADDCOMP) ? true : false;
		tf_status.setText(dd.modifyCompactPair(modificationFrom, node, add));
		stopModify();
	}
}//GEN-LAST:event_diagramMouseReleased

	private void menuAddTripleConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuAddTripleConnectionActionPerformed
	{//GEN-HEADEREND:event_menuAddTripleConnectionActionPerformed
		startModify(DynkinConnection.TYPE_TRIPLE, STATUS_ADDCON);
	}//GEN-LAST:event_menuAddTripleConnectionActionPerformed
	
	private void menuAddDoubleConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuAddDoubleConnectionActionPerformed
	{//GEN-HEADEREND:event_menuAddDoubleConnectionActionPerformed
		startModify(DynkinConnection.TYPE_DOUBLE, STATUS_ADDCON);
	}//GEN-LAST:event_menuAddDoubleConnectionActionPerformed
		
	private void menuRemoveConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuRemoveConnectionActionPerformed
	{//GEN-HEADEREND:event_menuRemoveConnectionActionPerformed
		startModify(DynkinConnection.TYPE_NULL, STATUS_REMCON);
	}//GEN-LAST:event_menuRemoveConnectionActionPerformed
	
	private void menuAddSingleConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuAddSingleConnectionActionPerformed
	{//GEN-HEADEREND:event_menuAddSingleConnectionActionPerformed
		startModify(DynkinConnection.TYPE_SINGLE, STATUS_ADDCON);
	}//GEN-LAST:event_menuAddSingleConnectionActionPerformed
	
	private void menuRemoveNodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuRemoveNodeActionPerformed
	{//GEN-HEADEREND:event_menuRemoveNodeActionPerformed
		tf_status.setText(dd.removeNode(dd.getNodeByCoor(contextXY)));
		contextVisible = false;
	}//GEN-LAST:event_menuRemoveNodeActionPerformed
	
	private void menuAddNodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuAddNodeActionPerformed
	{//GEN-HEADEREND:event_menuAddNodeActionPerformed
		tf_status.setText(dd.addNode(contextXY, 0));
		contextVisible = false;
	}//GEN-LAST:event_menuAddNodeActionPerformed

	private void menuToggleCompactActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuToggleCompactActionPerformed
	{//GEN-HEADEREND:event_menuToggleCompactActionPerformed
		tf_status.setText(dd.toggleCompactNode(dd.getNodeByCoor(contextXY)));
		contextVisible = false;
}//GEN-LAST:event_menuToggleCompactActionPerformed

	private void diagramMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_diagramMouseEntered
	{//GEN-HEADEREND:event_diagramMouseEntered
		// Set the focus to the diagram panel for key events.
		diagram.requestFocusInWindow();
		
		if(prev_status == STATUS_IDLE)
			status = STATUS_PREVIEW;
		else
			status = prev_status;
		
		shiftDown = evt.isShiftDown();
	}//GEN-LAST:event_diagramMouseEntered

	private void menuAddCompactPairActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuAddCompactPairActionPerformed
	{//GEN-HEADEREND:event_menuAddCompactPairActionPerformed
		startModify(DynkinConnection.TYPE_NULL, STATUS_ADDCOMP);
}//GEN-LAST:event_menuAddCompactPairActionPerformed

	private void menuRemoveCompactPairActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuRemoveCompactPairActionPerformed
	{//GEN-HEADEREND:event_menuRemoveCompactPairActionPerformed
		startModify(DynkinConnection.TYPE_NULL, STATUS_REMCOMP);
	}//GEN-LAST:event_menuRemoveCompactPairActionPerformed

	private void menuStateEnabledActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuStateEnabledActionPerformed
	{//GEN-HEADEREND:event_menuStateEnabledActionPerformed
		tf_status.setText(dd.setNodeState(dd.getNodeByCoor(contextXY),DynkinNode.STATE_ENABLED));
		contextVisible = false;
	}//GEN-LAST:event_menuStateEnabledActionPerformed

	private void menuStateDisabledActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuStateDisabledActionPerformed
	{//GEN-HEADEREND:event_menuStateDisabledActionPerformed
		tf_status.setText(dd.setNodeState(dd.getNodeByCoor(contextXY),DynkinNode.STATE_DISABLED));
		contextVisible = false;
	}//GEN-LAST:event_menuStateDisabledActionPerformed

	private void menuStateLevelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuStateLevelActionPerformed
	{//GEN-HEADEREND:event_menuStateLevelActionPerformed
		tf_status.setText(dd.setNodeState(dd.getNodeByCoor(contextXY),DynkinNode.STATE_ALWAYS_LEVEL));
		contextVisible = false;
	}//GEN-LAST:event_menuStateLevelActionPerformed

	private void rbNodeOrderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_rbNodeOrderStateChanged
	{//GEN-HEADEREND:event_rbNodeOrderStateChanged
		diagram.repaint();
	}//GEN-LAST:event_rbNodeOrderStateChanged
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bToEPS;
    private javax.swing.JPopupMenu contextMenu;
    private javax.swing.JPanel diagram;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuItem menuAddCompactPair;
    private javax.swing.JMenu menuAddConnection;
    private javax.swing.JMenuItem menuAddDoubleConnection;
    private javax.swing.JMenuItem menuAddNode;
    private javax.swing.JMenuItem menuAddQuadrupleConnection;
    private javax.swing.JMenuItem menuAddSingleConnection;
    private javax.swing.JMenuItem menuAddSpecialDoubleConnection;
    private javax.swing.JMenuItem menuAddTripleConnection;
    private javax.swing.JMenuItem menuRemoveCompactPair;
    private javax.swing.JMenuItem menuRemoveConnection;
    private javax.swing.JMenuItem menuRemoveNode;
    private javax.swing.JSeparator menuSeparator1;
    private javax.swing.JSeparator menuSeparator2;
    private javax.swing.JMenu menuState;
    private javax.swing.JRadioButtonMenuItem menuStateDisabled;
    private javax.swing.JRadioButtonMenuItem menuStateEnabled;
    private javax.swing.JRadioButtonMenuItem menuStateLevel;
    private javax.swing.JMenuItem menuToggleCompact;
    private javax.swing.ButtonGroup nodeLabelGroup;
    private javax.swing.ButtonGroup orderGroup;
    private javax.swing.JRadioButton rbBottomTop;
    private javax.swing.JRadioButton rbCoxeterLabels;
    private javax.swing.JRadioButton rbNodeOrder;
    private javax.swing.JRadioButton rbTopBottom;
    private javax.swing.ButtonGroup stateButtonGroup;
    private javax.swing.JLabel tf_status;
    // End of variables declaration//GEN-END:variables
	
}
