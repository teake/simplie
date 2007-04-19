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
 * @author  Teake Nutma
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
		
		addingConnection	= false;
		connectionTo		= -1;
		
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
		
		for (CDynkinNode node : Globals.dd.nodes)
		{
			for (int i = 0; i < node.numConnections(); i++)
				paintConnection(node.getConnection(i), g2);
		}
		for (CDynkinNode node : Globals.dd.nodes)
			paintNode(node, g2);
	}
	
	private void paintNode(CDynkinNode node, Graphics2D g2)
	{
		if(node.enabled)
			g2.setColor(Color.WHITE);
		else if(node.isDisconnected())
			g2.setColor(Color.ORANGE);
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
		CDynkinNode node1 = connection.fromNode;
		CDynkinNode node2 = connection.toNode;
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
		/** Don't do anything while we are scanning. */
		if(Globals.scanning)
			return;
		
		int x = cTransInv(evt.getX());
		int y = cTransInv(evt.getY());
		CDynkinNode node = Globals.dd.getNodeByCoor(x,y);
		
		if(evt.getButton() == evt.BUTTON1 && !evt.isAltDown())
		{
			if(!evt.isControlDown())
			{
				if(node == null)
				{
					int nextLabel = Globals.dd.nextFreeLabel();
					int lastLabel = Globals.dd.lastLabel();
					Globals.dd.addNode(x,y);
					if(evt.isShiftDown())
						Globals.dd.modifyConnection(nextLabel,lastLabel,true);
				}
			}
			else
			{
				if(node != null) Globals.dd.removeNode(node);
			}
		}
		
		if(node == null)
		{
			stopAddConnection();
			algebraSetup.Update();
			return;
		}
		
		if(evt.getButton() == evt.BUTTON2 || (evt.getButton() == evt.BUTTON1 && evt.isAltDown() ) )
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
					Globals.dd.modifyConnection(node.label, connectionTo, false);
				else
					Globals.dd.modifyConnection(node.label, connectionTo, true);
				stopAddConnection();
			}
		}
		algebraSetup.Update();
    }//GEN-LAST:event_formMouseReleased
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
	
}
