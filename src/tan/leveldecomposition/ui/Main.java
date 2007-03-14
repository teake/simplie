/*
 * LevelDecompositionUI.java
 *
 * Created on 8 maart 2007, 12:28
 */

package tan.leveldecomposition.ui;

import javax.swing.JTextField;
import javax.swing.UIManager;

import tan.leveldecomposition.*;
import tan.leveldecomposition.dynkindiagram.CDynkinDiagram;

/**
 *
 * @author  Teake Nutma
 */
public class Main extends javax.swing.JFrame
{
    CDynkinDiagram dynkinDiagram;
    
    /** Creates new form LevelDecompositionUI */
    public Main()
    {
	/** Try to set the Look and Feel to the system native look and feel */
	UIManager uiManager = new UIManager();
	try
	{
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}
	catch (Exception e)
	{
	}
	initComponents();
	
	dynkinDiagram = new CDynkinDiagram();
	
	algebraSetup.Initialize(dynkinDiagram);
	levelDecomposition.Initialize(dynkinDiagram);
    }
    

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        TabbedPane = new javax.swing.JTabbedPane();
        algebraSetup = new tan.leveldecomposition.ui.AlgebraSetup();
        levelDecomposition = new tan.leveldecomposition.ui.LevelDecomposition();
        MenuBar = new javax.swing.JMenuBar();
        MenuFile = new javax.swing.JMenu();
        MenuItemOpen = new javax.swing.JMenuItem();
        MenuItemSave = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        MenuItemExit = new javax.swing.JMenuItem();
        MenuHelp = new javax.swing.JMenu();
        MenuItemAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Level decomposition");
        TabbedPane.addTab("Algebra Setup", algebraSetup);

        TabbedPane.addTab("Level Decomposition", levelDecomposition);

        MenuFile.setLabel("File");
        MenuItemOpen.setText("Open algebra settings");
        MenuItemOpen.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuItemOpenActionPerformed(evt);
            }
        });

        MenuFile.add(MenuItemOpen);

        MenuItemSave.setText("Save algebra settings");
        MenuFile.add(MenuItemSave);

        MenuFile.add(jSeparator1);

        MenuItemExit.setText("Exit");
        MenuItemExit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuItemExitActionPerformed(evt);
            }
        });

        MenuFile.add(MenuItemExit);

        MenuBar.add(MenuFile);

        MenuHelp.setText("Help");
        MenuItemAbout.setText("About");
        MenuHelp.add(MenuItemAbout);

        MenuBar.add(MenuHelp);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MenuItemExitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuItemExitActionPerformed
    {//GEN-HEADEREND:event_MenuItemExitActionPerformed
	System.exit(0);
    }//GEN-LAST:event_MenuItemExitActionPerformed

    private void MenuItemOpenActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuItemOpenActionPerformed
    {//GEN-HEADEREND:event_MenuItemOpenActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_MenuItemOpenActionPerformed
                        
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
	java.awt.EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		new Main().setVisible(true);
	    }
	});
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenu MenuFile;
    private javax.swing.JMenu MenuHelp;
    private javax.swing.JMenuItem MenuItemAbout;
    private javax.swing.JMenuItem MenuItemExit;
    private javax.swing.JMenuItem MenuItemOpen;
    private javax.swing.JMenuItem MenuItemSave;
    private javax.swing.JTabbedPane TabbedPane;
    private tan.leveldecomposition.ui.AlgebraSetup algebraSetup;
    private javax.swing.JSeparator jSeparator1;
    private tan.leveldecomposition.ui.LevelDecomposition levelDecomposition;
    // End of variables declaration//GEN-END:variables
    
}
