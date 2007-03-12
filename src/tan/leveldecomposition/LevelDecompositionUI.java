/*
 * LevelDecompositionUI.java
 *
 * Created on 8 maart 2007, 12:28
 */

package tan.leveldecomposition;

import javax.swing.JTextField;
import tan.leveldecomposition.dynkindiagram.CDynkinDiagram;
import tan.leveldecomposition.helper.*;

/**
 *
 * @author  Teake Nutma
 */
public class LevelDecompositionUI extends javax.swing.JFrame
{
    CDynkinDiagram dynkinDiagram;
    CHelper helper;
    
    /** Creates new form LevelDecompositionUI */
    public LevelDecompositionUI()
    {
	initComponents();
	dynkinDiagram	= new CDynkinDiagram();
	helper		= new CHelper();
	Update();
    }
    
    public void Update()
    {
	taDynkinDiagram.setText(dynkinDiagram.GetDiagram());
	taCartanMatrix.setText(helper.MatrixToString(dynkinDiagram.GetCartanMatrix(), true));
	taCartanSubMatrix.setText(helper.MatrixToString(dynkinDiagram.GetCartanSubMatrix(), true));

	Integer lastLabel = new Integer(dynkinDiagram.GetLastLabel());
	tfAddNodeConnectionTo.setText(lastLabel.toString());
	tfRemoveNodeLabel.setText(lastLabel.toString());
	
	Integer nextFreeLabel = new Integer(dynkinDiagram.GetNextFreeLabel());
	tfAddNodeLabel.setText(nextFreeLabel.toString());
	
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        PanelChangesNode = new javax.swing.JPanel();
        lAddNodeLabel = new javax.swing.JLabel();
        lAddNodeConnectionTo = new javax.swing.JLabel();
        bAddNode = new javax.swing.JButton();
        bRemoveNode = new javax.swing.JButton();
        bToggleNode = new javax.swing.JButton();
        tfAddNodeConnectionTo = new javax.swing.JTextField();
        tfAddNodeLabel = new javax.swing.JTextField();
        tfRemoveNodeLabel = new javax.swing.JTextField();
        tfToggleNodeLabel = new javax.swing.JTextField();
        lRemoveNodeLabel = new javax.swing.JLabel();
        lToggleNodeLabel = new javax.swing.JLabel();
        PanelAddConnection = new javax.swing.JPanel();
        lAddConnectionFromLabel = new javax.swing.JLabel();
        lAddConnectionToLabel = new javax.swing.JLabel();
        tfAddConnectionFromLabel = new javax.swing.JTextField();
        tfAddConnectionToLabel = new javax.swing.JTextField();
        bAddConnection = new javax.swing.JButton();
        tfRemoveConnectionFromLabel = new javax.swing.JTextField();
        lRemoveConnectionFromLabel = new javax.swing.JLabel();
        lRemoveConnectionToLabel = new javax.swing.JLabel();
        tfRemoveConnectionToLabel = new javax.swing.JTextField();
        bRemoveConnection = new javax.swing.JButton();
        PanelDynkinDiagram = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDynkinDiagram = new javax.swing.JTextArea();
        PanelCartanMatrix = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taCartanMatrix = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        taCartanSubMatrix = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        PanelChangesNode.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Change nodes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        lAddNodeLabel.setText("Add node:");

        lAddNodeConnectionTo.setText("Connection to:");

        bAddNode.setText("Add");
        bAddNode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bAddNodeActionPerformed(evt);
            }
        });

        bRemoveNode.setText("Remove");
        bRemoveNode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bRemoveNodeActionPerformed(evt);
            }
        });

        bToggleNode.setText("Toggle");
        bToggleNode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bToggleNodeActionPerformed(evt);
            }
        });

        lRemoveNodeLabel.setText("Remove node:");

        lToggleNodeLabel.setText("Toggle node:");

        javax.swing.GroupLayout PanelChangesNodeLayout = new javax.swing.GroupLayout(PanelChangesNode);
        PanelChangesNode.setLayout(PanelChangesNodeLayout);
        PanelChangesNodeLayout.setHorizontalGroup(
            PanelChangesNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelChangesNodeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelChangesNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelChangesNodeLayout.createSequentialGroup()
                        .addGroup(PanelChangesNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lRemoveNodeLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lAddNodeConnectionTo, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lAddNodeLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lToggleNodeLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelChangesNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfToggleNodeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(tfAddNodeConnectionTo, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(tfAddNodeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(tfRemoveNodeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)))
                    .addComponent(bAddNode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(bRemoveNode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(bToggleNode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                .addContainerGap())
        );
        PanelChangesNodeLayout.setVerticalGroup(
            PanelChangesNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelChangesNodeLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(PanelChangesNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lAddNodeLabel)
                    .addComponent(tfAddNodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(PanelChangesNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lAddNodeConnectionTo)
                    .addComponent(tfAddNodeConnectionTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bAddNode)
                .addGap(27, 27, 27)
                .addGroup(PanelChangesNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lRemoveNodeLabel)
                    .addComponent(tfRemoveNodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bRemoveNode)
                .addGap(27, 27, 27)
                .addGroup(PanelChangesNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lToggleNodeLabel)
                    .addComponent(tfToggleNodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bToggleNode)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelAddConnection.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Change connections", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        lAddConnectionFromLabel.setText("Add connection from:");

        lAddConnectionToLabel.setText("To:");

        bAddConnection.setText("Add");
        bAddConnection.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bAddConnectionActionPerformed(evt);
            }
        });

        lRemoveConnectionFromLabel.setText("Remove connection from:");

        lRemoveConnectionToLabel.setText("To:");

        bRemoveConnection.setText("Remove");
        bRemoveConnection.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bRemoveConnectionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelAddConnectionLayout = new javax.swing.GroupLayout(PanelAddConnection);
        PanelAddConnection.setLayout(PanelAddConnectionLayout);
        PanelAddConnectionLayout.setHorizontalGroup(
            PanelAddConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelAddConnectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelAddConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bRemoveConnection, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(bAddConnection, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PanelAddConnectionLayout.createSequentialGroup()
                        .addGroup(PanelAddConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lRemoveConnectionFromLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lRemoveConnectionToLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lAddConnectionToLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lAddConnectionFromLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelAddConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfRemoveConnectionFromLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                            .addComponent(tfRemoveConnectionToLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                            .addComponent(tfAddConnectionToLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                            .addComponent(tfAddConnectionFromLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))))
                .addContainerGap())
        );
        PanelAddConnectionLayout.setVerticalGroup(
            PanelAddConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAddConnectionLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(PanelAddConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lAddConnectionFromLabel)
                    .addComponent(tfAddConnectionFromLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelAddConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lAddConnectionToLabel)
                    .addComponent(tfAddConnectionToLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bAddConnection)
                .addGap(27, 27, 27)
                .addGroup(PanelAddConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lRemoveConnectionFromLabel)
                    .addComponent(tfRemoveConnectionFromLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelAddConnectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lRemoveConnectionToLabel)
                    .addComponent(tfRemoveConnectionToLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bRemoveConnection)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelDynkinDiagram.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dynkin Diagram", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        taDynkinDiagram.setColumns(20);
        taDynkinDiagram.setEditable(false);
        taDynkinDiagram.setFont(new java.awt.Font("Courier New", 0, 13));
        taDynkinDiagram.setRows(5);
        jScrollPane1.setViewportView(taDynkinDiagram);

        javax.swing.GroupLayout PanelDynkinDiagramLayout = new javax.swing.GroupLayout(PanelDynkinDiagram);
        PanelDynkinDiagram.setLayout(PanelDynkinDiagramLayout);
        PanelDynkinDiagramLayout.setHorizontalGroup(
            PanelDynkinDiagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDynkinDiagramLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelDynkinDiagramLayout.setVerticalGroup(
            PanelDynkinDiagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDynkinDiagramLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelCartanMatrix.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cartan Matrix", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        taCartanMatrix.setColumns(20);
        taCartanMatrix.setEditable(false);
        taCartanMatrix.setFont(new java.awt.Font("Courier New", 0, 13));
        taCartanMatrix.setRows(5);
        jScrollPane2.setViewportView(taCartanMatrix);

        taCartanSubMatrix.setColumns(20);
        taCartanSubMatrix.setEditable(false);
        taCartanSubMatrix.setFont(new java.awt.Font("Courier New", 0, 13));
        taCartanSubMatrix.setRows(5);
        jScrollPane3.setViewportView(taCartanSubMatrix);

        javax.swing.GroupLayout PanelCartanMatrixLayout = new javax.swing.GroupLayout(PanelCartanMatrix);
        PanelCartanMatrix.setLayout(PanelCartanMatrixLayout);
        PanelCartanMatrixLayout.setHorizontalGroup(
            PanelCartanMatrixLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCartanMatrixLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        PanelCartanMatrixLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane2, jScrollPane3});

        PanelCartanMatrixLayout.setVerticalGroup(
            PanelCartanMatrixLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCartanMatrixLayout.createSequentialGroup()
                .addGroup(PanelCartanMatrixLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelAddConnection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelChangesNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(PanelCartanMatrix, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelDynkinDiagram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelDynkinDiagram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelChangesNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelCartanMatrix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanelAddConnection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bToggleNodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bToggleNodeActionPerformed
    {//GEN-HEADEREND:event_bToggleNodeActionPerformed
	dynkinDiagram.ToggleNode(Integer.parseInt(tfToggleNodeLabel.getText()));
	Update();
    }//GEN-LAST:event_bToggleNodeActionPerformed

    private void bRemoveNodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bRemoveNodeActionPerformed
    {//GEN-HEADEREND:event_bRemoveNodeActionPerformed
	dynkinDiagram.RemoveNode(Integer.parseInt(tfRemoveNodeLabel.getText()));
	Update();
    }//GEN-LAST:event_bRemoveNodeActionPerformed

    private void bRemoveConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bRemoveConnectionActionPerformed
    {//GEN-HEADEREND:event_bRemoveConnectionActionPerformed
	dynkinDiagram.ModifyConnection(Integer.parseInt(tfRemoveConnectionFromLabel.getText()),Integer.parseInt(tfRemoveConnectionToLabel.getText()),false);
	Update();
    }//GEN-LAST:event_bRemoveConnectionActionPerformed

    private void bAddConnectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bAddConnectionActionPerformed
    {//GEN-HEADEREND:event_bAddConnectionActionPerformed
	dynkinDiagram.ModifyConnection(Integer.parseInt(tfAddConnectionFromLabel.getText()),Integer.parseInt(tfAddConnectionToLabel.getText()),true);
	Update();
    }//GEN-LAST:event_bAddConnectionActionPerformed
    
    private void bAddNodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bAddNodeActionPerformed
    {//GEN-HEADEREND:event_bAddNodeActionPerformed
	dynkinDiagram.AddNode(Integer.parseInt(tfAddNodeLabel.getText()),Integer.parseInt(tfAddNodeConnectionTo.getText()));
	Update();
    }//GEN-LAST:event_bAddNodeActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
	java.awt.EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		new LevelDecompositionUI().setVisible(true);
	    }
	});
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelAddConnection;
    private javax.swing.JPanel PanelCartanMatrix;
    private javax.swing.JPanel PanelChangesNode;
    private javax.swing.JPanel PanelDynkinDiagram;
    private javax.swing.JButton bAddConnection;
    private javax.swing.JButton bAddNode;
    private javax.swing.JButton bRemoveConnection;
    private javax.swing.JButton bRemoveNode;
    private javax.swing.JButton bToggleNode;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lAddConnectionFromLabel;
    private javax.swing.JLabel lAddConnectionToLabel;
    private javax.swing.JLabel lAddNodeConnectionTo;
    private javax.swing.JLabel lAddNodeLabel;
    private javax.swing.JLabel lRemoveConnectionFromLabel;
    private javax.swing.JLabel lRemoveConnectionToLabel;
    private javax.swing.JLabel lRemoveNodeLabel;
    private javax.swing.JLabel lToggleNodeLabel;
    private javax.swing.JTextArea taCartanMatrix;
    private javax.swing.JTextArea taCartanSubMatrix;
    private javax.swing.JTextArea taDynkinDiagram;
    private javax.swing.JTextField tfAddConnectionFromLabel;
    private javax.swing.JTextField tfAddConnectionToLabel;
    private javax.swing.JTextField tfAddNodeConnectionTo;
    private javax.swing.JTextField tfAddNodeLabel;
    private javax.swing.JTextField tfRemoveConnectionFromLabel;
    private javax.swing.JTextField tfRemoveConnectionToLabel;
    private javax.swing.JTextField tfRemoveNodeLabel;
    private javax.swing.JTextField tfToggleNodeLabel;
    // End of variables declaration//GEN-END:variables
    
}
