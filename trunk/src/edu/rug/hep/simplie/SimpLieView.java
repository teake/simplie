/*
 * SimpLieView.java
 */

package edu.rug.hep.simplie;

import edu.rug.hep.simplie.*;
import org.jdesktop.application.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.*;

/**
 * The application's main frame.
 */
public class SimpLieView extends FrameView {

	private final CAlgebraComposite algebras;
	private JDialog exportDialog;
	private JDialog outputDialog;
	
	private final FileFilter ddFilter;
	private final FileFilter rsFilter;
	
	private final File workDir;
	private final File ddDir;
	private final File rsDir;
	
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
	
    public SimpLieView(SingleFrameApplication app) 
	{
        super(app);
		
        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
				
		algebras = new CAlgebraComposite();
		algebraSetup.setAlgebraComposite(algebras);
		algebraInfo.setAlgebraComposite(algebras);
		levelDecomposition.setAlgebraComposite(algebras);
		rootSpaceDrawer.setAlgebrasComposite(algebras);
		
		
		ddFilter = new FileNameExtensionFilter("Dynkin diagram (*.dd)", "dd");
		rsFilter = new FileNameExtensionFilter("Root system (*.rs)", "rs");
		
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
		reloadPresets();
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = SimpLieApp.getApplication().getMainFrame();
            aboutBox = new SimpLieAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SimpLieApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        MenuPresets = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        exportToTexItem = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        exportToTex = new edu.rug.hep.simplie.ui.ExportToTex();
        systemOutTextArea = new edu.rug.hep.simplie.ui.SystemOutTextArea();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        mainPane = new javax.swing.JTabbedPane();
        algebraSetup = new edu.rug.hep.simplie.ui.AlgebraSetup();
        algebraInfo = new edu.rug.hep.simplie.ui.AlgebraInfo();
        levelDecomposition = new edu.rug.hep.simplie.ui.LevelDecomposition();
        rootSpaceDrawer = new edu.rug.hep.simplie.ui.RootSpaceDrawer();

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.rug.hep.simplie.SimpLieApp.class).getContext().getResourceMap(SimpLieView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.rug.hep.simplie.SimpLieApp.class).getContext().getActionMap(SimpLieView.class, this);
        jMenuItem4.setAction(actionMap.get("loadDiagram")); // NOI18N
        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        fileMenu.add(jMenuItem4);

        jMenuItem5.setAction(actionMap.get("saveDiagram")); // NOI18N
        jMenuItem5.setName("jMenuItem5"); // NOI18N
        fileMenu.add(jMenuItem5);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        jMenuItem7.setAction(actionMap.get("loadRoots")); // NOI18N
        jMenuItem7.setName("jMenuItem7"); // NOI18N
        fileMenu.add(jMenuItem7);

        jMenuItem6.setAction(actionMap.get("saveRoots")); // NOI18N
        jMenuItem6.setName("jMenuItem6"); // NOI18N
        fileMenu.add(jMenuItem6);

        jSeparator3.setName("jSeparator3"); // NOI18N
        fileMenu.add(jSeparator3);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        MenuPresets.setText(resourceMap.getString("MenuPresets.text")); // NOI18N
        MenuPresets.setName("MenuPresets"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N
        MenuPresets.add(jSeparator1);

        jMenuItem2.setAction(actionMap.get("clearDiagram")); // NOI18N
        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        MenuPresets.add(jMenuItem2);

        jMenuItem3.setAction(actionMap.get("reloadPresets")); // NOI18N
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        MenuPresets.add(jMenuItem3);

        menuBar.add(MenuPresets);

        toolsMenu.setText(resourceMap.getString("toolsMenu.text")); // NOI18N
        toolsMenu.setName("toolsMenu"); // NOI18N

        exportToTexItem.setAction(actionMap.get("showExportTexDialog")); // NOI18N
        exportToTexItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportToTexItem.setText(resourceMap.getString("exportToTexItem.text")); // NOI18N
        exportToTexItem.setName("exportToTexItem"); // NOI18N
        toolsMenu.add(exportToTexItem);

        jMenuItem8.setAction(actionMap.get("exportRoots")); // NOI18N
        jMenuItem8.setName("jMenuItem8"); // NOI18N
        toolsMenu.add(jMenuItem8);

        jMenuItem1.setAction(actionMap.get("showOutput")); // NOI18N
        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        toolsMenu.add(jMenuItem1);

        menuBar.add(toolsMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        exportToTex.setName("exportToTex"); // NOI18N

        systemOutTextArea.setName("systemOutTextArea"); // NOI18N

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 505, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(statusMessageLabel)
                    .add(statusAnimationLabel)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3))
        );

        mainPane.setName("mainPane"); // NOI18N

        algebraSetup.setName("algebraSetup"); // NOI18N
        mainPane.addTab(resourceMap.getString("algebraSetup.TabConstraints.tabTitle"), algebraSetup); // NOI18N

        algebraInfo.setName("algebraInfo"); // NOI18N
        mainPane.addTab(resourceMap.getString("algebraInfo.TabConstraints.tabTitle"), algebraInfo); // NOI18N

        levelDecomposition.setName("levelDecomposition"); // NOI18N
        mainPane.addTab(resourceMap.getString("levelDecomposition.TabConstraints.tabTitle"), levelDecomposition); // NOI18N

        rootSpaceDrawer.setName("rootSpaceDrawer"); // NOI18N
        mainPane.addTab(resourceMap.getString("rootSpaceDrawer.TabConstraints.tabTitle"), rootSpaceDrawer); // NOI18N

        setComponent(mainPane);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

	@Action
	public void showExportTexDialog()
	{
		if (exportDialog == null)
		{
			JFrame mainFrame = SimpLieApp.getApplication().getMainFrame();
			exportDialog = new JDialog(mainFrame);
			exportDialog.add(exportToTex);
			exportToTex.setup(exportDialog,levelDecomposition.getRepTable(),algebras);
            exportDialog.setLocationRelativeTo(mainFrame);
			exportDialog.setTitle("Export to TeX");
        }
        SimpLieApp.getApplication().show(exportDialog);
	}

	@Action
	public void showOutput()
	{
		if (exportDialog == null)
		{
            JFrame mainFrame = SimpLieApp.getApplication().getMainFrame();
            outputDialog = new JDialog(mainFrame);
			outputDialog.add(systemOutTextArea);
            outputDialog.setLocationRelativeTo(mainFrame);
			outputDialog.setTitle("Program output");
        }
        SimpLieApp.getApplication().show(outputDialog);		
	}
	
	@Action
	public void clearDiagram()
	{
		algebras.dd.clear();
	}

	@Action
	public void reloadPresets()
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
			if(!ddFilter.accept(file))
				continue;
			
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

	@Action
	public void loadDiagram()
	{
		JFileChooser chooser;
	
		chooser = new JFileChooser(ddDir);
		chooser.addChoosableFileFilter(ddFilter);
		chooser.setDialogTitle("Load Dynkin diagram");
		
		if ( chooser.showOpenDialog(this.getComponent()) == JFileChooser.APPROVE_OPTION )
		{
			algebras.dd.loadFrom(chooser.getSelectedFile().getAbsolutePath());
		}
	}

	@Action
	public void saveDiagram()
	{
		JFileChooser chooser = new JFileChooser(ddDir);
		chooser.addChoosableFileFilter(ddFilter);
		chooser.setSelectedFile(new File(algebras.getDynkinDiagramType(false) + ".dd"));
		chooser.setDialogTitle("Save Dynkin diagram");
		
		if ( chooser.showSaveDialog(this.getComponent()) == JFileChooser.APPROVE_OPTION )
		{
		/* To create a URL for a file on the local file-system, we simply
		 * pre-pend the "file" protocol to the absolute path of the file.
		 */
			String fileURL = chooser.getSelectedFile().getAbsolutePath();
			if(!ddFilter.accept(chooser.getSelectedFile()))
				fileURL += ".dd";
			algebras.dd.saveTo(fileURL);
		}
	}

	@Action
	public void saveRoots()
	{
		JFileChooser chooser = new JFileChooser(rsDir);
		chooser.addChoosableFileFilter(rsFilter);
		chooser.setSelectedFile(new File(algebras.algebra.type + "_height_" + algebras.algebra.rs.constructedHeight()));
		chooser.setDialogTitle("Save root system");
		
		if ( chooser.showSaveDialog(this.getComponent()) == JFileChooser.APPROVE_OPTION )
		{
		/* To create a URL for a file on the local file-system, we simply
		 * pre-pend the "file" protocol to the absolute path of the file.
		 */
			String fileURL = chooser.getSelectedFile().getAbsolutePath();
			if(!rsFilter.accept(chooser.getSelectedFile()))
				fileURL += ".rs";
			algebras.algebra.rs.saveTo(fileURL);
		}
	}

	@Action
	public
	void loadRoots()
	{
		String fileURL;
		JFileChooser chooser;
		
		chooser = new JFileChooser(rsDir);
		chooser.addChoosableFileFilter(rsFilter);
		chooser.setDialogTitle("Load root system");
		
		if ( chooser.showOpenDialog(this.getComponent()) == JFileChooser.APPROVE_OPTION )
		{
		/* To create a URL for a file on the local file-system, we simply
		 * pre-pend the "file" protocol to the absolute path of the file.
		 */
			fileURL = chooser.getSelectedFile().getAbsolutePath();
			if(!algebras.algebra.rs.loadFrom(fileURL))
			{
				System.out.println("Failed to load root system.");
			}
		}
	}

	@Action
	public void exportRoots()
	{
		JFileChooser chooser = new JFileChooser("");
		chooser.setSelectedFile(new File(algebras.algebra.type + "_height_" + algebras.algebra.rs.constructedHeight() + ".txt"));
		chooser.setDialogTitle("Export root system");
		if ( chooser.showSaveDialog(this.getComponent()) == JFileChooser.APPROVE_OPTION )
		{
		/* To create a URL for a file on the local file-system, we simply
		 * pre-pend the "file" protocol to the absolute path of the file.
		 */
			String fileURL = chooser.getSelectedFile().getAbsolutePath();
			algebras.algebra.rs.writeTxtFile(fileURL);
		}
	}




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu MenuPresets;
    private edu.rug.hep.simplie.ui.AlgebraInfo algebraInfo;
    private edu.rug.hep.simplie.ui.AlgebraSetup algebraSetup;
    private edu.rug.hep.simplie.ui.ExportToTex exportToTex;
    private javax.swing.JMenuItem exportToTexItem;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private edu.rug.hep.simplie.ui.LevelDecomposition levelDecomposition;
    private javax.swing.JTabbedPane mainPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private edu.rug.hep.simplie.ui.RootSpaceDrawer rootSpaceDrawer;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private edu.rug.hep.simplie.ui.SystemOutTextArea systemOutTextArea;
    private javax.swing.JMenu toolsMenu;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
