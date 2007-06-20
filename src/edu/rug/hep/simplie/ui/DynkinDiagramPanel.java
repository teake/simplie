/*
 * DynkinDiagramPanel.java
 *
 * Created on 20 maart 2007, 17:40
 */

package edu.rug.hep.simplie.ui;

import edu.rug.hep.simplie.*;
import edu.rug.hep.simplie.dynkindiagram.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Cursor;

/**
 *
 * @author  Teake Nutma
 */
public class DynkinDiagramPanel extends javax.swing.JPanel implements DiagramListener
{
	private CDynkinDiagram dd;
	
	private int spacing;
	private int radius;
	private int offset;
	
	private boolean		modifyingConnection;
	private boolean		addingConnection;
	private int			connectionLaced;
	private CDynkinNode connectionFrom;
	
	private int contextX;
	private int contextY;
	
	private int x;
	private int y;
	
	/** Creates new form DynkinDiagramPanel */
	public DynkinDiagramPanel()
	{
		spacing = 50;
		radius	= 8;
		offset	= 50;
		
		modifyingConnection	= false;
		connectionFrom		= null;
		
		contextX = contextY = 0;
		x = y = -1;
		initComponents();
	}
	
	public void setDynkinDiagram(CDynkinDiagram dd)
	{
		this.dd = dd;
		this.dd.addListener(this);
	}
	
	public void diagramChanged()
	{
		diagram.repaint();
	}
	
	public void drawDiagram(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Draw the preview.
		if(x != -1)
		{
			g2.setColor(Color.GRAY);
			g2.drawOval(
					spacing * x + offset - radius,
					spacing * y + offset - radius,
					2*radius, 2*radius);
			g2.setColor(Color.BLACK);
		}
		
		// Draw the diagram.
		if(dd != null)
			dd.drawDiagram(g2,offset,spacing,radius);
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
	
	private void setupMenu(boolean onNode)
	{
		menuAddConnection.setVisible(onNode);
		menuRemoveConnection.setVisible(onNode);
		menuSeparator.setVisible(onNode);
		menuAddNode.setVisible(!onNode);
		menuRemoveNode.setVisible(onNode);
		menuToggleNode.setVisible(onNode);
	}
	
	private void startModifyConnection(CDynkinNode node)
	{
		if(node == null)
		{
			status.setText("Invalid start point specified.");
			return;
		}
		status.setText("Modifying connection ...");
		modifyingConnection	= true;
		connectionFrom		= node;
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	private void stopModifyConnection()
	{
		modifyingConnection = false;
		connectionFrom		= null;
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        contextMenu = new javax.swing.JPopupMenu();
        menuAddConnection = new javax.swing.JMenu();
        menuAddSingleConnection = new javax.swing.JMenuItem();
        menuAddDoubleConnection = new javax.swing.JMenuItem();
        menuAddTripleConnection = new javax.swing.JMenuItem();
        menuRemoveConnection = new javax.swing.JMenuItem();
        menuSeparator = new javax.swing.JSeparator();
        menuAddNode = new javax.swing.JMenuItem();
        menuToggleNode = new javax.swing.JMenuItem();
        menuRemoveNode = new javax.swing.JMenuItem();
        diagram = new javax.swing.JPanel()
        {
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                drawDiagram(g);
            }
        };
        status = new javax.swing.JLabel();

        menuAddConnection.setText("Add connection");

        menuAddSingleConnection.setText("Single");
        menuAddSingleConnection.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuAddSingleConnectionActionPerformed(evt);
            }
        });
        menuAddConnection.add(menuAddSingleConnection);

        menuAddDoubleConnection.setText("Double");
        menuAddDoubleConnection.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuAddDoubleConnectionActionPerformed(evt);
            }
        });
        menuAddConnection.add(menuAddDoubleConnection);

        menuAddTripleConnection.setText("Triple");
        menuAddTripleConnection.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuAddTripleConnectionActionPerformed(evt);
            }
        });
        menuAddConnection.add(menuAddTripleConnection);

        contextMenu.add(menuAddConnection);

        menuRemoveConnection.setText("Remove connection");
        menuRemoveConnection.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuRemoveConnectionActionPerformed(evt);
            }
        });
        contextMenu.add(menuRemoveConnection);
        contextMenu.add(menuSeparator);

        menuAddNode.setText("Add node");
        menuAddNode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuAddNodeActionPerformed(evt);
            }
        });
        contextMenu.add(menuAddNode);

        menuToggleNode.setText("Toggle node");
        menuToggleNode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuToggleNodeActionPerformed(evt);
            }
        });
        contextMenu.add(menuToggleNode);

        menuRemoveNode.setText("Remove node");
        menuRemoveNode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuRemoveNodeActionPerformed(evt);
            }
        });
        contextMenu.add(menuRemoveNode);

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dynkin diagram", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));

        diagram.setBackground(new java.awt.Color(255, 255, 255));
        diagram.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        diagram.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                diagramMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                diagramMouseReleased(evt);
            }
        });
        diagram.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseMoved(java.awt.event.MouseEvent evt)
            {
                diagramMouseMoved(evt);
            }
        });

        status.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        status.setText("(Right) click to add a node to the diagram.");

        javax.swing.GroupLayout diagramLayout = new javax.swing.GroupLayout(diagram);
        diagram.setLayout(diagramLayout);
        diagramLayout.setHorizontalGroup(
            diagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(status, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
        );
        diagramLayout.setVerticalGroup(
            diagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(diagramLayout.createSequentialGroup()
                .addContainerGap(140, Short.MAX_VALUE)
                .addComponent(status))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(diagram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(diagram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
	
private void diagramMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diagramMouseExited
	if(!contextMenu.isVisible())
	{
		x = y = -1;
		diagram.repaint();
	}
}//GEN-LAST:event_diagramMouseExited

private void diagramMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diagramMouseMoved
	x = Math.round(((float) evt.getX() - offset) / spacing);
	y = Math.round(((float) evt.getY() - offset) / spacing);
	diagram.repaint();
}//GEN-LAST:event_diagramMouseMoved

private void diagramMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diagramMouseReleased
	// Don't do anything if the diagram is locked.
	if(dd.isLocked())
		return;
	
	CDynkinNode node = dd.getNodeByCoor(x,y);
	
	if(evt.getButton() == evt.BUTTON3)
	{
		setupMenu((node!=null));
		contextX = x;
		contextY = y;
		contextMenu.show(diagram,evt.getX(),evt.getY());
		return;
	}
	
	// Left click and not modifying connection: add a node.
	if(evt.getButton() == evt.BUTTON1 && !modifyingConnection)
	{
		int connectionToLast = (evt.isShiftDown()) ? 1 : 0;
		status.setText(dd.addNode(x, y, connectionToLast));
		return;
	}
	
	// Middle mouse or alt+left: toggle a node.
	if(evt.getButton() == evt.BUTTON2 || (evt.getButton() == evt.BUTTON1 && evt.isAltDown() ) )
	{
		stopModifyConnection();
		status.setText(dd.toggleNode(node));
	}
	
	if(modifyingConnection)
	{
		status.setText(dd.modifyConnection(connectionFrom, node, connectionLaced, addingConnection));
		stopModifyConnection();
	}
}//GEN-LAST:event_diagramMouseReleased

	private void menuAddTripleConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuAddTripleConnectionActionPerformed
	{//GEN-HEADEREND:event_menuAddTripleConnectionActionPerformed
		addingConnection = true;
		connectionLaced = 3;
		startModifyConnection(dd.getNodeByCoor(contextX, contextY));
	}//GEN-LAST:event_menuAddTripleConnectionActionPerformed
	
	private void menuAddDoubleConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuAddDoubleConnectionActionPerformed
	{//GEN-HEADEREND:event_menuAddDoubleConnectionActionPerformed
		addingConnection = true;
		connectionLaced = 2;
		startModifyConnection(dd.getNodeByCoor(contextX, contextY));
	}//GEN-LAST:event_menuAddDoubleConnectionActionPerformed
	
	private void menuToggleNodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuToggleNodeActionPerformed
	{//GEN-HEADEREND:event_menuToggleNodeActionPerformed
		CDynkinNode node = dd.getNodeByCoor(contextX, contextY);
		dd.toggleNode(node);
	}//GEN-LAST:event_menuToggleNodeActionPerformed
	
	private void menuRemoveConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuRemoveConnectionActionPerformed
	{//GEN-HEADEREND:event_menuRemoveConnectionActionPerformed
		addingConnection = false;
		startModifyConnection(dd.getNodeByCoor(contextX, contextY));
	}//GEN-LAST:event_menuRemoveConnectionActionPerformed
	
	private void menuAddSingleConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuAddSingleConnectionActionPerformed
	{//GEN-HEADEREND:event_menuAddSingleConnectionActionPerformed
		addingConnection = true;
		connectionLaced = 1;
		startModifyConnection(dd.getNodeByCoor(contextX, contextY));
	}//GEN-LAST:event_menuAddSingleConnectionActionPerformed
	
	private void menuRemoveNodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuRemoveNodeActionPerformed
	{//GEN-HEADEREND:event_menuRemoveNodeActionPerformed
		status.setText(dd.removeNode(dd.getNodeByCoor(contextX, contextY)));
	}//GEN-LAST:event_menuRemoveNodeActionPerformed
	
	private void menuAddNodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuAddNodeActionPerformed
	{//GEN-HEADEREND:event_menuAddNodeActionPerformed
		status.setText(dd.addNode(contextX, contextY, 0));
	}//GEN-LAST:event_menuAddNodeActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu contextMenu;
    private javax.swing.JPanel diagram;
    private javax.swing.JMenu menuAddConnection;
    private javax.swing.JMenuItem menuAddDoubleConnection;
    private javax.swing.JMenuItem menuAddNode;
    private javax.swing.JMenuItem menuAddSingleConnection;
    private javax.swing.JMenuItem menuAddTripleConnection;
    private javax.swing.JMenuItem menuRemoveConnection;
    private javax.swing.JMenuItem menuRemoveNode;
    private javax.swing.JSeparator menuSeparator;
    private javax.swing.JMenuItem menuToggleNode;
    private javax.swing.JLabel status;
    // End of variables declaration//GEN-END:variables
	
}
