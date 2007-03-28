/*
 * DynkinDiagramPanel.java
 *
 * Created on 20 maart 2007, 17:40
 */

package tan.leveldecomposition.ui;

import tan.leveldecomposition.dynkindiagram.*;
import tan.leveldecomposition.*;

import java.util.Vector;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;

/**
 *
 * @author  P221000
 */
public class DynkinDiagramPanel extends javax.swing.JPanel
{
    int spacing;
    int radius;
    int offset;
    Font font;
    
    boolean addingConnection;
    int	    connectionTo;
    
    AlgebraSetup algebraSetup;
    
    /** Creates new form DynkinDiagramPanel */
    public DynkinDiagramPanel()
    {
	initComponents();
	
	spacing = 40;
	radius	= 10;
	offset	= 25;
	font	= new Font("Monospaced", Font.PLAIN, 12);
	
	addingConnection    = false;
	connectionTo	    = -1;
	
	this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    public void Initialize(AlgebraSetup algebraSetup)
    {
	this.algebraSetup = algebraSetup;
    }
    
    public void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	
	for (CDynkinConnection connection : DynkinDiagram.connections)
	{
	    paintConnection(connection, g2);
	}
	for (CDynkinNode node : DynkinDiagram.nodes)
	{
	    paintNode(node, g2);
	}
    }
    
    private void paintNode(CDynkinNode node, Graphics2D g2)
    {
	if(node.enabled)
	    g2.setColor(Color.WHITE);
	else
	    g2.setColor(Color.GRAY);
	g2.fillOval(cTrans(node.x), cTrans(node.y), radius, radius);
	
	g2.setColor(Color.BLACK);
	g2.drawOval(cTrans(node.x), cTrans(node.y), radius, radius);
	
	g2.setFont(font);
	g2.drawString(Globals.intToString(node.label), cTrans(node.x) + radius/2, cTrans(node.y) + radius + 15);
    }
    
    private void paintConnection(CDynkinConnection connection, Graphics2D g2)
    {
	CDynkinNode node1 = DynkinDiagram.GetNodeById(connection.idNode1);
	CDynkinNode node2 = DynkinDiagram.GetNodeById(connection.idNode2);
	if(node1 != null && node2 != null)
	{
	    g2.drawLine(cTrans(node1.x) + radius/2,cTrans(node1.y) + radius/2, cTrans(node2.x) + radius/2,cTrans(node2.y) + radius/2);
	}
    }
    
    private int cTrans(int coordinate)
    {
	return spacing*coordinate + offset;
    }
    private int cTransInv(int coordinate)
    {
	return Math.round((coordinate - offset) / spacing);
    }
    private void startAddConnection(int label)
    {
	addingConnection = true;
	connectionTo = label;
	this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }
    private void stopAddConnection()
    {
	addingConnection = false;
	connectionTo = -1;
	this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        setBackground(new java.awt.Color(255, 255, 255));
        addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                formMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
            
    private void formMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseReleased
    {//GEN-HEADEREND:event_formMouseReleased
	int x = cTransInv(evt.getX());
	int y = cTransInv(evt.getY());
	CDynkinNode node = DynkinDiagram.GetNodeByCoor(x,y);
	
	if(evt.getButton() == evt.BUTTON1)
	{
	    if(!evt.isControlDown())
	    {
		if(node == null)
		{
		    int nextLabel = DynkinDiagram.GetNextFreeLabel();
		    int lastLabel = DynkinDiagram.GetLastLabel();
		    DynkinDiagram.AddNode(x,y);
		    if(evt.isShiftDown())
			DynkinDiagram.ModifyConnection(nextLabel,lastLabel,true);
		}
	    }
	    else
	    {
		if(node != null) DynkinDiagram.RemoveNode(node);
	    }
	}
	
	if(node == null)
	{
	    stopAddConnection();
	    algebraSetup.Update();
	    return;
	}
	
	if(evt.getButton() == evt.BUTTON2)
	{
	    stopAddConnection();
	    node.enabled = !node.enabled;
	}
	if(evt.getButton() == evt.BUTTON3)
	{
	    if(!addingConnection)
	    {
		startAddConnection(node.label);
	    }
	    else
	    {
		if(evt.isControlDown())
		    DynkinDiagram.ModifyConnection(node.label, connectionTo, false);
		else
		    DynkinDiagram.ModifyConnection(node.label, connectionTo, true);
		stopAddConnection();
	    }
	}
	algebraSetup.Update();
    }//GEN-LAST:event_formMouseReleased
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
