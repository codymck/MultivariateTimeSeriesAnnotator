/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.View;

import edu.csusm.capstone.timeseriesannotator.Controller.*;
import java.awt.event.ActionListener;

/**
 *
 * @author Cody McKinney
 */
public class AppFrame extends javax.swing.JFrame {

    /**
     * Creates new form Frame
     */
    public AppFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        importChooser = new javax.swing.JFileChooser();
        frameDisplayPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        fileMenuItem = new javax.swing.JMenu();
        importDataMenuItem = new javax.swing.JMenuItem();
        editMenuItem = new javax.swing.JMenu();
        toolsMenuItem = new javax.swing.JMenu();

        importChooser.setCurrentDirectory(new java.io.File("dataFiles"));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Multivariate Time Series Annotator");
        setBackground(new java.awt.Color(255, 255, 255));

        frameDisplayPanel.setBackground(new java.awt.Color(153, 153, 153));
        frameDisplayPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(0, 0, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Apple Chancery", 0, 48)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(0, 255, 204));
        jTextArea1.setRows(5);
        jTextArea1.setText("Fun Fun Fun");
        jTextArea1.setToolTipText("");
        jTextArea1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextArea1.setSelectionColor(new java.awt.Color(51, 51, 51));
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout frameDisplayPanelLayout = new javax.swing.GroupLayout(frameDisplayPanel);
        frameDisplayPanel.setLayout(frameDisplayPanelLayout);
        frameDisplayPanelLayout.setHorizontalGroup(
            frameDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1196, Short.MAX_VALUE)
        );
        frameDisplayPanelLayout.setVerticalGroup(
            frameDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
        );

        menuBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuBar.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        fileMenuItem.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fileMenuItem.setText("File");

        importDataMenuItem.setText("Import Data");
        importDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importDataMenuItemActionPerformed(evt);
            }
        });
        fileMenuItem.add(importDataMenuItem);

        menuBar.add(fileMenuItem);

        editMenuItem.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editMenuItem.setText("Edit");
        menuBar.add(editMenuItem);

        toolsMenuItem.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        toolsMenuItem.setText("Tools");
        menuBar.add(toolsMenuItem);

        setJMenuBar(menuBar);
        menuBar.getAccessibleContext().setAccessibleName("Welcome");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(frameDisplayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(frameDisplayPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        frameDisplayPanel.getAccessibleContext().setAccessibleName("");
        frameDisplayPanel.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void importDataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importDataMenuItemActionPerformed
        ActionListener importAction = new ImportDataAction(importChooser);
        importAction.actionPerformed(evt);
    }//GEN-LAST:event_importDataMenuItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu editMenuItem;
    private javax.swing.JMenu fileMenuItem;
    private javax.swing.JPanel frameDisplayPanel;
    private javax.swing.JFileChooser importChooser;
    private javax.swing.JMenuItem importDataMenuItem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu toolsMenuItem;
    // End of variables declaration//GEN-END:variables
}
