/*
 * SimpLieView.java
 */

package edu.simplie;

import org.jdesktop.application.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
/**
 * The application's main frame.
 */
public class SimpLieView extends FrameView {

	private final AlgebraComposite algebras;
	
    private JDialog aboutBox;
	private JDialog exportDialog;
	private JDialog outputDialog;
	private JDialog algebraInfoDialog;
	private JDialog repDialog;
	private JDialog levelDecompDialog;
	private JDialog visDialog;
	
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
		
		this.getFrame().setIconImage(resourceMap.getImageIcon("icon16").getImage());
		
		algebras = new AlgebraComposite();
		mainPane.setAlgebraComposite(algebras);
		algebraInfo.setAlgebraComposite(algebras);
		repContainer.setAlgebraComposite(algebras);
		levelDecomposition.setAlgebraComposite(algebras);
		projector.setAlgebrasComposite(algebras);
		algebras.dd.clear();		
		
	
		// Check what the working dir is.
		String userDir	= java.lang.System.getProperty("user.home");
		File appData	= new File(userDir,"Application Data");
		if(appData.exists())
		{
			workDir = new File(appData, ".simplie");
		}
		else
		{
			workDir = new File(userDir, ".simplie");
		}
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        loadDiagramItem = new javax.swing.JMenuItem();
        saveDiagramItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        loadRootsItem = new javax.swing.JMenuItem();
        saveRootsItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        presetMenu = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JSeparator();
        clearItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        exportToTexItem = new javax.swing.JMenuItem();
        exporRootsItem = new javax.swing.JMenuItem();
        outputItem = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();
        algebraItem = new javax.swing.JMenuItem();
        repItem = new javax.swing.JMenuItem();
        levelDecompItem = new javax.swing.JMenuItem();
        visItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        exportToTex = new edu.simplie.ui.ExportToTex();
        systemOutTextArea = new edu.simplie.ui.SystemOutTextArea();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        repContainer = new edu.simplie.ui.reps.RepContainer();
        projector = new edu.simplie.ui.Projector();
        algebraInfo = new edu.simplie.ui.AlgebraInfo();
        levelDecomposition = new edu.simplie.ui.LevelDecomposition();
        mainPane = new edu.simplie.ui.DynkinDiagramPanel();

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setMnemonic('f');
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getResourceMap(SimpLieView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.simplie.SimpLieApp.class).getContext().getActionMap(SimpLieView.class, this);
        loadDiagramItem.setAction(actionMap.get("loadDiagram")); // NOI18N
        loadDiagramItem.setName("loadDiagramItem"); // NOI18N
        fileMenu.add(loadDiagramItem);

        saveDiagramItem.setAction(actionMap.get("saveDiagram")); // NOI18N
        saveDiagramItem.setName("saveDiagramItem"); // NOI18N
        fileMenu.add(saveDiagramItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        loadRootsItem.setAction(actionMap.get("loadRoots")); // NOI18N
        loadRootsItem.setName("loadRootsItem"); // NOI18N
        fileMenu.add(loadRootsItem);

        saveRootsItem.setAction(actionMap.get("saveRoots")); // NOI18N
        saveRootsItem.setName("saveRootsItem"); // NOI18N
        fileMenu.add(saveRootsItem);

        jSeparator3.setName("jSeparator3"); // NOI18N
        fileMenu.add(jSeparator3);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        presetMenu.setMnemonic('p');
        presetMenu.setText(resourceMap.getString("presetMenu.text")); // NOI18N
        presetMenu.setName("presetMenu"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N
        presetMenu.add(jSeparator1);

        clearItem.setAction(actionMap.get("clearDiagram")); // NOI18N
        clearItem.setText(resourceMap.getString("clearItem.text")); // NOI18N
        clearItem.setName("clearItem"); // NOI18N
        presetMenu.add(clearItem);

        menuBar.add(presetMenu);

        toolsMenu.setMnemonic('t');
        toolsMenu.setText(resourceMap.getString("toolsMenu.text")); // NOI18N
        toolsMenu.setName("toolsMenu"); // NOI18N

        exportToTexItem.setAction(actionMap.get("showExportTexDialog")); // NOI18N
        exportToTexItem.setName("exportToTexItem"); // NOI18N
        toolsMenu.add(exportToTexItem);

        exporRootsItem.setAction(actionMap.get("exportRoots")); // NOI18N
        exporRootsItem.setName("exporRootsItem"); // NOI18N
        toolsMenu.add(exporRootsItem);

        outputItem.setAction(actionMap.get("showOutput")); // NOI18N
        outputItem.setText(resourceMap.getString("outputItem.text")); // NOI18N
        outputItem.setName("outputItem"); // NOI18N
        toolsMenu.add(outputItem);

        menuBar.add(toolsMenu);

        windowMenu.setMnemonic('w');
        windowMenu.setText(resourceMap.getString("windowMenu.text")); // NOI18N
        windowMenu.setName("windowMenu"); // NOI18N

        algebraItem.setAction(actionMap.get("showAlgebraInfo")); // NOI18N
        algebraItem.setText(resourceMap.getString("algebraInfo")); // NOI18N
        algebraItem.setName("algebraItem"); // NOI18N
        windowMenu.add(algebraItem);

        repItem.setAction(actionMap.get("showReps")); // NOI18N
        repItem.setText(resourceMap.getString("reps")); // NOI18N
        repItem.setName("repItem"); // NOI18N
        windowMenu.add(repItem);

        levelDecompItem.setAction(actionMap.get("showLevelDecomp")); // NOI18N
        levelDecompItem.setText(resourceMap.getString("levelDecomp")); // NOI18N
        levelDecompItem.setName("levelDecompItem"); // NOI18N
        windowMenu.add(levelDecompItem);

        visItem.setAction(actionMap.get("showVis")); // NOI18N
        visItem.setText(resourceMap.getString("projector")); // NOI18N
        visItem.setName("visItem"); // NOI18N
        windowMenu.add(visItem);

        menuBar.add(windowMenu);

        helpMenu.setMnemonic('h');
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
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 681, Short.MAX_VALUE)
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

        repContainer.setName("repContainer"); // NOI18N

        projector.setName("projector"); // NOI18N

        algebraInfo.setName("algebraInfo"); // NOI18N

        levelDecomposition.setName("levelDecomposition"); // NOI18N

        mainPane.setName("mainPane"); // NOI18N

        setComponent(mainPane);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

	@Action
	public void clearDiagram()
	{
		algebras.dd.clear();
	}

	@Action
	public void reloadPresets()
	{
		// Clear everything except the reset button
		while(presetMenu.getItemCount() > 2)
		{
			presetMenu.remove(0);
		}
		
		// Add the diagram presets to the UI.
		int pos = 0;
		for(File file : ddDir.listFiles())
		{
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
			presetMenu.add(ddPreset, pos++);
		}
	}

	@Action
	public void loadDiagram()
	{
		JFileChooser chooser;
	
		chooser = new JFileChooser(ddDir);
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
		chooser.setSelectedFile(new File(algebras.getDynkinDiagramType() + ".dd"));
		chooser.setDialogTitle("Save Dynkin diagram");
		
		if ( chooser.showSaveDialog(this.getComponent()) == JFileChooser.APPROVE_OPTION )
		{
		/* To create a URL for a file on the local file-system, we simply
		 * pre-pend the "file" protocol to the absolute path of the file.
		 */
			String fileURL = chooser.getSelectedFile().getAbsolutePath();
			algebras.dd.saveTo(fileURL);
		}
		// Reload the presets.
		reloadPresets();
	}

	@Action
	public void saveRoots()
	{
		JFileChooser chooser = new JFileChooser(rsDir);
		chooser.setSelectedFile(new File(algebras.algebra.type + "_height_" + algebras.algebra.rs.constructedHeight() + ".rs"));
		chooser.setDialogTitle("Save root system");
		
		if ( chooser.showSaveDialog(this.getComponent()) == JFileChooser.APPROVE_OPTION )
		{
		/* To create a URL for a file on the local file-system, we simply
		 * pre-pend the "file" protocol to the absolute path of the file.
		 */
			String fileURL = chooser.getSelectedFile().getAbsolutePath();
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

	/**
	 * Actions for showing JDialogs
	 * TODO: cleanup duplicate code.
	 */

    @Action
    public void showAboutBox()
	{
        if (aboutBox == null)
		{
            JFrame mainFrame = SimpLieApp.getApplication().getMainFrame();
            aboutBox = new SimpLieAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SimpLieApp.getApplication().show(aboutBox);
    }
	
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
			exportDialog.setResizable(false);
        }
        SimpLieApp.getApplication().show(exportDialog);
	}

	@Action
	public void showOutput()
	{
		if (outputDialog == null)
		{
            JFrame mainFrame = SimpLieApp.getApplication().getMainFrame();
            outputDialog = new JDialog(mainFrame);
			outputDialog.add(systemOutTextArea);
            outputDialog.setLocationRelativeTo(mainFrame);
			outputDialog.setTitle("SimpLie Output");
        }
        SimpLieApp.getApplication().show(outputDialog);
	}

	@Action
	public void showAlgebraInfo()
	{
		if(algebraInfoDialog == null)
		{
			JFrame mainFrame = SimpLieApp.getApplication().getMainFrame();
			algebraInfoDialog = new JDialog(mainFrame);
			algebraInfoDialog.add(algebraInfo);
            algebraInfoDialog.setLocationRelativeTo(mainFrame);
			algebraInfoDialog.setTitle(getResourceMap().getString("algebraInfo"));
        }
		SimpLieApp.getApplication().show(algebraInfoDialog);
	}

	@Action
	public void showReps()
	{
		if(repDialog == null)
		{
			JFrame mainFrame = SimpLieApp.getApplication().getMainFrame();
			repDialog = new JDialog(mainFrame);
			repDialog.add(repContainer);
            repDialog.setLocationRelativeTo(mainFrame);
			repDialog.setTitle(getResourceMap().getString("reps"));
        }
		SimpLieApp.getApplication().show(repDialog);
	}

	@Action
	public void showLevelDecomp()
	{
		if(levelDecompDialog == null)
		{
			JFrame mainFrame = SimpLieApp.getApplication().getMainFrame();
			levelDecompDialog = new JDialog(mainFrame);
			levelDecompDialog.add(levelDecomposition);
            levelDecompDialog.setLocationRelativeTo(mainFrame);
			levelDecompDialog.setTitle(getResourceMap().getString("levelDecomp"));
        }
		SimpLieApp.getApplication().show(levelDecompDialog);
	}

	@Action
	public void showVis()
	{
		if(visDialog == null)
		{
			JFrame mainFrame = SimpLieApp.getApplication().getMainFrame();
			visDialog = new JDialog(mainFrame);
			visDialog.add(projector);
            visDialog.setLocationRelativeTo(mainFrame);
			visDialog.setTitle(getResourceMap().getString("projector"));
        }
		SimpLieApp.getApplication().show(visDialog);
	}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private edu.simplie.ui.AlgebraInfo algebraInfo;
    private javax.swing.JMenuItem algebraItem;
    private javax.swing.JMenuItem clearItem;
    private javax.swing.JMenuItem exporRootsItem;
    private edu.simplie.ui.ExportToTex exportToTex;
    private javax.swing.JMenuItem exportToTexItem;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JMenuItem levelDecompItem;
    private edu.simplie.ui.LevelDecomposition levelDecomposition;
    private javax.swing.JMenuItem loadDiagramItem;
    private javax.swing.JMenuItem loadRootsItem;
    private edu.simplie.ui.DynkinDiagramPanel mainPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem outputItem;
    private javax.swing.JMenu presetMenu;
    private javax.swing.JProgressBar progressBar;
    private edu.simplie.ui.Projector projector;
    private edu.simplie.ui.reps.RepContainer repContainer;
    private javax.swing.JMenuItem repItem;
    private javax.swing.JMenuItem saveDiagramItem;
    private javax.swing.JMenuItem saveRootsItem;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private edu.simplie.ui.SystemOutTextArea systemOutTextArea;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JMenuItem visItem;
    private javax.swing.JMenu windowMenu;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
}
