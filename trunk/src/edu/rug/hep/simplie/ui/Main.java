/*
 * Main.java
 *
 * Created on 8 maart 2007, 12:28 *
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

import edu.rug.hep.simplie.*;
import edu.rug.hep.simplie.dynkindiagram.*;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;
import java.io.File;
import java.util.ArrayList;
import java.awt.Image;
import javax.swing.JPopupMenu;

/**
 * The runnable of SimpLie, also displays all of its sub-windows.
 * 
 * @author  Teake Nutma
 */
public class Main extends javax.swing.JFrame
{
	// TODO: make this work for JDK5
	//private final FileFilter ddFilter;
	//private final FileFilter rsFilter;
	
	private final File workDir;
	private final File ddDir;
	private final File rsDir;
	private final CAlgebraComposite algebras;
	private final String build;
	
	/** This class is used in the diagram preset UI */
	class ddListener implements java.awt.event.ActionListener
	{
		String url;
		public ddListener(String url)
		{
			this.url = url;
		}
		public void actionPerformed(java.awt.event.ActionEvent evt)
		{
			algebras.dd.loadFrom(this.url);
		}
	}
	
	/** Creates new form LevelDecompositionUI */
	public Main()
	{
		// This is replaced by SVN everytime we commit
		String revision = "$Rev$";
		build = revision.replace("Rev:","").replace("$","");
		
		// Try to set the Look and Feel to the system native look and feel.
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
		
		// Set the custom SimpLie icons.
		ArrayList iconList = new ArrayList<Image>();
		iconList.add(java.awt.Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("icon32.png")));
		iconList.add(java.awt.Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("icon16.png")));
		// TODO: make this work for JDK5
		//this.setIconImages(iconList);
		
		// Allow the menubars to draw on top of the GLCanvas
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		initComponents();
		
		// Create the algebras and initialize the other components.
		algebras = new CAlgebraComposite();
		algebraSetup.setAlgebraComposite(algebras);
		algebraInfo.setAlgebraComposite(algebras);
		levelDecomposition.setAlgebraComposite(algebras);
		rootSpaceDrawer.setAlgebrasComposite(algebras);
		
		this.setLocation(20,20);
		
		systemOutputDialog.setLocation(100,100);
		systemOutputDialog.setMinimumSize(new java.awt.Dimension(0,0));
		exportDialog.setLocation(300,250);
		exportToTex.setup(exportDialog,levelDecomposition.getRepTable(),algebras);
		
		// TODO: make this work for JDK5
		//ddFilter = new FileNameExtensionFilter("Dynkin diagram (*.dd)", "dd");
		//rsFilter = new FileNameExtensionFilter("Root system (*.rs)", "rs");
		
		// Check what the working dir is.
		String userDir	= java.lang.System.getProperty("user.home");
		File appData	= new File(userDir,"Application Data");
		if(appData.exists())
		{
			workDir = new File(appData, ".simplie");
		}
		else
			workDir = new File(userDir, ".simplie");
		ddDir = new File(workDir, "diagrams");
		rsDir = new File(workDir, "roots");
		
		// If it doesn't exist, create it.
		if(!ddDir.exists())
			ddDir.mkdirs();
		if(!rsDir.exists())
			rsDir.mkdirs();
		
		// Fill the preset menu.
		resetPresets();
	}
	
	private void resetPresets()
	{
		// Clear everything except the reset button
		while(MenuPresets.getItemCount() > 3)
		{
			MenuPresets.remove(0);
		}
		
		// Add the diagram presets to the UI.
		int pos = 0;
		for(File file : ddDir.listFiles())
		{
			// TODO: make this work for JDK5
			//if(!ddFilter.accept(file))
				//continue;
			
			// Add the menu item.
			javax.swing.JMenuItem ddPreset = new javax.swing.JMenuItem();
			String text = file.getName();
			ddPreset.setText(text.substring(0,text.lastIndexOf(".dd")));
			ddPreset.addActionListener(new ddListener(file.toString()));
			if(pos < 12)
			{
				String shiftKey = "F" + (pos + 1);
				ddPreset.setAccelerator(javax.swing.KeyStroke.getKeyStroke("shift " + shiftKey));
			}
			MenuPresets.add(ddPreset, pos++);
		}
	}
	
	
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popup = new javax.swing.JFrame();
        optionPane = new javax.swing.JOptionPane();
        exportDialog = new javax.swing.JDialog();
        exportToTex = new edu.rug.hep.simplie.ui.ExportToTex();
        systemOutputDialog = new javax.swing.JDialog();
        systemOutTextArea = new edu.rug.hep.simplie.ui.SystemOutTextArea();
        TabbedPane = new javax.swing.JTabbedPane();
        algebraSetup = new edu.rug.hep.simplie.ui.AlgebraSetup();
        rootSpaceDrawer = new edu.rug.hep.simplie.ui.RootSpaceDrawer();
        algebraInfo = new edu.rug.hep.simplie.ui.AlgebraInfo();
        levelDecomposition = new edu.rug.hep.simplie.ui.LevelDecomposition();
        MenuBar = new javax.swing.JMenuBar();
        MenuFile = new javax.swing.JMenu();
        MenuItemLoadDD = new javax.swing.JMenuItem();
        MenuItemSaveDD = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        MenuItemLoadRoots = new javax.swing.JMenuItem();
        MenuItemSaveRoots = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        MenuItemExit = new javax.swing.JMenuItem();
        MenuPresets = new javax.swing.JMenu();
        jSeparator2 = new javax.swing.JSeparator();
        MenuItemClear = new javax.swing.JMenuItem();
        MenuItemResetPresets = new javax.swing.JMenuItem();
        MenuTools = new javax.swing.JMenu();
        MenuExportToTex = new javax.swing.JMenuItem();
        MenuExportRootSystem = new javax.swing.JMenuItem();
        MenuShowOutput = new javax.swing.JMenuItem();
        MenuHelp = new javax.swing.JMenu();
        MenuItemHelp = new javax.swing.JMenuItem();
        MenuItemAbout = new javax.swing.JMenuItem();

        popup.setTitle("About");
        popup.setMinimumSize(new java.awt.Dimension(220, 180));
        popup.setResizable(false);

        org.jdesktop.layout.GroupLayout popupLayout = new org.jdesktop.layout.GroupLayout(popup.getContentPane());
        popup.getContentPane().setLayout(popupLayout);
        popupLayout.setHorizontalGroup(
            popupLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(optionPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        popupLayout.setVerticalGroup(
            popupLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(optionPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        exportDialog.setTitle("Export to TeX");
        exportDialog.setMinimumSize(new java.awt.Dimension(430, 316));
        exportDialog.setResizable(false);

        org.jdesktop.layout.GroupLayout exportDialogLayout = new org.jdesktop.layout.GroupLayout(exportDialog.getContentPane());
        exportDialog.getContentPane().setLayout(exportDialogLayout);
        exportDialogLayout.setHorizontalGroup(
            exportDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(exportDialogLayout.createSequentialGroup()
                .add(exportToTex, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        exportDialogLayout.setVerticalGroup(
            exportDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(exportToTex, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
        );

        systemOutputDialog.setTitle("System output");
        systemOutputDialog.setMinimumSize(new java.awt.Dimension(360, 360));

        org.jdesktop.layout.GroupLayout systemOutputDialogLayout = new org.jdesktop.layout.GroupLayout(systemOutputDialog.getContentPane());
        systemOutputDialog.getContentPane().setLayout(systemOutputDialogLayout);
        systemOutputDialogLayout.setHorizontalGroup(
            systemOutputDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(systemOutTextArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
        );
        systemOutputDialogLayout.setVerticalGroup(
            systemOutputDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(systemOutTextArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SimpLie");

        TabbedPane.addTab("Algebra setup", algebraSetup);
        TabbedPane.addTab("Root space", rootSpaceDrawer);
        TabbedPane.addTab("Algebra info", algebraInfo);
        TabbedPane.addTab("Level decomposition", levelDecomposition);

        MenuFile.setMnemonic('f');
        MenuFile.setLabel("File");

        MenuItemLoadDD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemLoadDD.setMnemonic('o');
        MenuItemLoadDD.setLabel("Load Dynkin diagram");
        MenuItemLoadDD.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuItemLoadDDActionPerformed(evt);
            }
        });
        MenuFile.add(MenuItemLoadDD);

        MenuItemSaveDD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemSaveDD.setMnemonic('s');
        MenuItemSaveDD.setLabel("Save Dynkin diagram");
        MenuItemSaveDD.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuItemSaveDDActionPerformed(evt);
            }
        });
        MenuFile.add(MenuItemSaveDD);
        MenuFile.add(jSeparator4);

        MenuItemLoadRoots.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemLoadRoots.setMnemonic('l');
        MenuItemLoadRoots.setText("Load root system");
        MenuItemLoadRoots.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuItemLoadRootsActionPerformed(evt);
            }
        });
        MenuFile.add(MenuItemLoadRoots);

        MenuItemSaveRoots.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemSaveRoots.setMnemonic('r');
        MenuItemSaveRoots.setText("Save root system");
        MenuItemSaveRoots.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuItemSaveRootsActionPerformed(evt);
            }
        });
        MenuFile.add(MenuItemSaveRoots);
        MenuFile.add(jSeparator1);

        MenuItemExit.setMnemonic('x');
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

        MenuPresets.setMnemonic('p');
        MenuPresets.setText("Presets");
        MenuPresets.add(jSeparator2);

        MenuItemClear.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.SHIFT_MASK));
        MenuItemClear.setMnemonic('c');
        MenuItemClear.setText("Clear diagram");
        MenuItemClear.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuItemClearActionPerformed(evt);
            }
        });
        MenuPresets.add(MenuItemClear);

        MenuItemResetPresets.setMnemonic('r');
        MenuItemResetPresets.setText("Reset presets");
        MenuItemResetPresets.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuItemResetPresetsActionPerformed(evt);
            }
        });
        MenuPresets.add(MenuItemResetPresets);

        MenuBar.add(MenuPresets);

        MenuTools.setMnemonic('t');
        MenuTools.setText("Tools");

        MenuExportToTex.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        MenuExportToTex.setMnemonic('e');
        MenuExportToTex.setText("Export to TeX");
        MenuExportToTex.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuExportToTexActionPerformed(evt);
            }
        });
        MenuTools.add(MenuExportToTex);

        MenuExportRootSystem.setText("Export root system");
        MenuExportRootSystem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuExportRootSystemActionPerformed(evt);
            }
        });
        MenuTools.add(MenuExportRootSystem);

        MenuShowOutput.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MenuShowOutput.setMnemonic('s');
        MenuShowOutput.setText("Show system output");
        MenuShowOutput.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuShowOutputActionPerformed(evt);
            }
        });
        MenuTools.add(MenuShowOutput);

        MenuBar.add(MenuTools);

        MenuHelp.setMnemonic('h');
        MenuHelp.setText("Help");

        MenuItemHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        MenuItemHelp.setMnemonic('h');
        MenuItemHelp.setText("Help");
        MenuItemHelp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuItemHelpActionPerformed(evt);
            }
        });
        MenuHelp.add(MenuItemHelp);

        MenuItemAbout.setMnemonic('a');
        MenuItemAbout.setText("About");
        MenuItemAbout.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MenuItemAboutActionPerformed(evt);
            }
        });
        MenuHelp.add(MenuItemAbout);

        MenuBar.add(MenuHelp);

        setJMenuBar(MenuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(TabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 763, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(TabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        );

        TabbedPane.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
	
	private void MenuItemResetPresetsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuItemResetPresetsActionPerformed
	{//GEN-HEADEREND:event_MenuItemResetPresetsActionPerformed
		resetPresets();
}//GEN-LAST:event_MenuItemResetPresetsActionPerformed
	
	private void MenuExportRootSystemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuExportRootSystemActionPerformed
	{//GEN-HEADEREND:event_MenuExportRootSystemActionPerformed
		JFileChooser chooser = new JFileChooser("");
		chooser.setSelectedFile(new File(algebras.algebra.type + "_height_" + algebras.algebra.rs.constructedHeight() + ".txt"));
		chooser.setDialogTitle("Export root system");
		int returnVal = chooser.showSaveDialog(this);
		
		if ( returnVal == JFileChooser.APPROVE_OPTION )
		{
		/* To create a URL for a file on the local file-system, we simply
		 * pre-pend the "file" protocol to the absolute path of the file.
		 */
			String fileURL = chooser.getSelectedFile().getAbsolutePath();
			algebras.algebra.rs.writeTxtFile(fileURL);
		}
	}//GEN-LAST:event_MenuExportRootSystemActionPerformed
	
	private void MenuShowOutputActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuShowOutputActionPerformed
	{//GEN-HEADEREND:event_MenuShowOutputActionPerformed
		systemOutputDialog.setVisible(true);
	}//GEN-LAST:event_MenuShowOutputActionPerformed
	
	private void MenuExportToTexActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuExportToTexActionPerformed
	{//GEN-HEADEREND:event_MenuExportToTexActionPerformed
		exportDialog.setVisible(true);
	}//GEN-LAST:event_MenuExportToTexActionPerformed
		
	private void MenuItemSaveRootsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuItemSaveRootsActionPerformed
	{//GEN-HEADEREND:event_MenuItemSaveRootsActionPerformed
		JFileChooser chooser = new JFileChooser(rsDir);
		// TODO: make this work for JDK5
		//chooser.addChoosableFileFilter(rsFilter);
		chooser.setSelectedFile(new File(algebras.algebra.type + "_height_" + algebras.algebra.rs.constructedHeight()));
		chooser.setDialogTitle("Save root system");
		int returnVal = chooser.showSaveDialog(this);
		
		if ( returnVal == JFileChooser.APPROVE_OPTION )
		{
		/* To create a URL for a file on the local file-system, we simply
		 * pre-pend the "file" protocol to the absolute path of the file.
		 */
			String fileURL = chooser.getSelectedFile().getAbsolutePath();
			// TODO: make this work for JDK5
			//if(!rsFilter.accept(chooser.getSelectedFile()))
				//fileURL += ".rs";
			algebras.algebra.rs.saveTo(fileURL);
		}
	}//GEN-LAST:event_MenuItemSaveRootsActionPerformed
	
	private void MenuItemLoadRootsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuItemLoadRootsActionPerformed
	{//GEN-HEADEREND:event_MenuItemLoadRootsActionPerformed
		String fileURL;
		JFileChooser chooser;
		int returnVal;
		
		chooser = new JFileChooser(rsDir);
		// TODO: make this work for JDK5
		//chooser.addChoosableFileFilter(rsFilter);
		chooser.setDialogTitle("Open root system");
		returnVal = chooser.showOpenDialog(this);
		
		if ( returnVal == JFileChooser.APPROVE_OPTION )
		{
		/* To create a URL for a file on the local file-system, we simply
		 * pre-pend the "file" protocol to the absolute path of the file.
		 */
			fileURL = chooser.getSelectedFile().getAbsolutePath();
			if(!algebras.algebra.rs.loadFrom(fileURL))
			{
				JOptionPane.showMessageDialog(
						popup,
						"The root system does not belong to this Dynkin diagram.",
						"Failed to load root system",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}//GEN-LAST:event_MenuItemLoadRootsActionPerformed
	
    private void MenuItemHelpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuItemHelpActionPerformed
    {//GEN-HEADEREND:event_MenuItemHelpActionPerformed
		JOptionPane.showMessageDialog(
				popup,
				"Dynkin diagram interaction: \n \n" +
				"Left mouse-click: \n   Add a node. \n" +
				"Middle mouse-click: \n   Toggle a node. \n" +
				"Right mouse-click: \n   Bring up context menu.",
				"Help",
				JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_MenuItemHelpActionPerformed

	private void MenuItemSaveDDActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuItemSaveDDActionPerformed
	{//GEN-HEADEREND:event_MenuItemSaveDDActionPerformed
		JFileChooser chooser = new JFileChooser(ddDir);
		// TODO: make this work for JDK5
		//chooser.addChoosableFileFilter(ddFilter);
		chooser.setSelectedFile(new File(algebras.getDynkinDiagramType(false) + ".dd"));
		chooser.setDialogTitle("Save Dynkin diagram");
		int returnVal = chooser.showSaveDialog(this);
		
		if ( returnVal == JFileChooser.APPROVE_OPTION )
		{
		/* To create a URL for a file on the local file-system, we simply
		 * pre-pend the "file" protocol to the absolute path of the file.
		 */
			String fileURL = chooser.getSelectedFile().getAbsolutePath();
			// TODO: make this work for JDK5
			//if(!ddFilter.accept(chooser.getSelectedFile()))
				//fileURL += ".dd";
			algebras.dd.saveTo(fileURL);
		}
	}//GEN-LAST:event_MenuItemSaveDDActionPerformed
	
	private void MenuItemLoadDDActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuItemLoadDDActionPerformed
	{//GEN-HEADEREND:event_MenuItemLoadDDActionPerformed
		String fileURL;
		JFileChooser chooser;
		int returnVal;
		
		chooser = new JFileChooser(ddDir);
		// TODO: make this work for JDK5
		//chooser.addChoosableFileFilter(ddFilter);
		chooser.setDialogTitle("Open Dynkin diagram");
		returnVal = chooser.showOpenDialog(this);
		
		if ( returnVal == JFileChooser.APPROVE_OPTION )
		{
		/* To create a URL for a file on the local file-system, we simply
		 * pre-pend the "file" protocol to the absolute path of the file.
		 */
			fileURL = chooser.getSelectedFile().getAbsolutePath();
			algebras.dd.loadFrom(fileURL);
		}
	}//GEN-LAST:event_MenuItemLoadDDActionPerformed
	
    private void MenuItemClearActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuItemClearActionPerformed
    {//GEN-HEADEREND:event_MenuItemClearActionPerformed
		algebras.dd.clear();
    }//GEN-LAST:event_MenuItemClearActionPerformed
	
    private void MenuItemAboutActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuItemAboutActionPerformed
    {//GEN-HEADEREND:event_MenuItemAboutActionPerformed
		JOptionPane.showMessageDialog(
				popup,
				"SimpLie, a simple program for Lie algebras.\nVersion 1.0\n \nWritten by Teake Nutma \nt.a.nutma@rug.nl",
				"About",
				JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_MenuItemAboutActionPerformed
	
    private void MenuItemExitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MenuItemExitActionPerformed
    {//GEN-HEADEREND:event_MenuItemExitActionPerformed
		System.exit(0);
    }//GEN-LAST:event_MenuItemExitActionPerformed
	
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
    private javax.swing.JMenuItem MenuExportRootSystem;
    private javax.swing.JMenuItem MenuExportToTex;
    private javax.swing.JMenu MenuFile;
    private javax.swing.JMenu MenuHelp;
    private javax.swing.JMenuItem MenuItemAbout;
    private javax.swing.JMenuItem MenuItemClear;
    private javax.swing.JMenuItem MenuItemExit;
    private javax.swing.JMenuItem MenuItemHelp;
    private javax.swing.JMenuItem MenuItemLoadDD;
    private javax.swing.JMenuItem MenuItemLoadRoots;
    private javax.swing.JMenuItem MenuItemResetPresets;
    private javax.swing.JMenuItem MenuItemSaveDD;
    private javax.swing.JMenuItem MenuItemSaveRoots;
    private javax.swing.JMenu MenuPresets;
    private javax.swing.JMenuItem MenuShowOutput;
    private javax.swing.JMenu MenuTools;
    private javax.swing.JTabbedPane TabbedPane;
    private edu.rug.hep.simplie.ui.AlgebraInfo algebraInfo;
    private edu.rug.hep.simplie.ui.AlgebraSetup algebraSetup;
    private javax.swing.JDialog exportDialog;
    private edu.rug.hep.simplie.ui.ExportToTex exportToTex;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private edu.rug.hep.simplie.ui.LevelDecomposition levelDecomposition;
    private javax.swing.JOptionPane optionPane;
    private javax.swing.JFrame popup;
    private edu.rug.hep.simplie.ui.RootSpaceDrawer rootSpaceDrawer;
    private edu.rug.hep.simplie.ui.SystemOutTextArea systemOutTextArea;
    private javax.swing.JDialog systemOutputDialog;
    // End of variables declaration//GEN-END:variables
	
}