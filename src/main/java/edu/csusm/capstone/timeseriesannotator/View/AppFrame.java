package edu.csusm.capstone.timeseriesannotator.View;

import com.formdev.flatlaf.FlatLightLaf;
import edu.csusm.capstone.timeseriesannotator.Controller.*;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author Cody McKinney
 */
public class AppFrame extends javax.swing.JFrame {

    ChartDisplay chartDisplay;
    ArrayList<ChartDisplay> charts;

    MultiSplitPane split = new MultiSplitPane();

    /**
     * Creates new form Frame
     */
    public AppFrame() {
        this.charts = new ArrayList<>(6);
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }
        initComponents();
        initialChart();

        // jPanel1.add(split);

        this.setLocationRelativeTo(null);
    }

    // Creates an empty chart on instantiation of program
    private void initialChart() {
        chartDisplay = new ChartDisplay(this);
        charts.add(chartDisplay);
        split.addComponent(chartDisplay);
        jPanel1.removeAll();
        jPanel1.add(split);
        validate();
        repaint();
    }

    // Adds additional charts to the panel
    public void addChart(ArrayList<ChartDisplay> c) {
        this.charts = c;
        // jPanel1.removeAll();
        chartDisplay = charts.get(charts.size() - 1);
    }

    // Removes the ChartDisplay from the panel
    public void removeChart(ChartDisplay c) {
        split.removeComponent(c);
        charts.remove(c);
        // jPanel1.remove(c);
        validate();
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        importChooser = new javax.swing.JFileChooser();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton6 = new javax.swing.JToggleButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenuItem = new javax.swing.JMenu();
        importDataMenuItem = new javax.swing.JMenuItem();
        editMenuItem = new javax.swing.JMenu();
        optionsMenuItem = new javax.swing.JMenu();
        AddChartMenuItem = new javax.swing.JMenuItem();

        importChooser.setCurrentDirectory(new java.io.File("./dataFiles"));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Multivariate Time Series Annotator");
        setBackground(new java.awt.Color(255, 255, 255));
        setBounds(new java.awt.Rectangle(0, 0, 1000, 800));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 800));
        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel2.setMaximumSize(new java.awt.Dimension(55, 55));
        jPanel2.setMinimumSize(new java.awt.Dimension(55, 55));

        jToggleButton2.setIcon(new javax.swing.ImageIcon("src/main/resources/icons/zoomin.png"));
        jToggleButton2.setToolTipText("");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton1);
        jToggleButton1.setIcon(new javax.swing.ImageIcon("src/main/resources/icons/zoomout.png"));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton3);
        jToggleButton3.setIcon(new javax.swing.ImageIcon("src/main/resources/icons/4dirs.png"));
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton4);
        jToggleButton4.setIcon(new javax.swing.ImageIcon("src/main/resources/icons/shapes.jpeg"));
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton5);
        jToggleButton5.setIcon(new javax.swing.ImageIcon("src/main/resources/icons/select.png"));
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton6);
        jToggleButton6.setIcon(new javax.swing.ImageIcon("src/main/resources/icons/annotate.png"));
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 711, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jToggleButton2, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jToggleButton3, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jToggleButton4, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jToggleButton5, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jToggleButton6, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE));

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        menuBar.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        fileMenuItem.setText("File");

        importDataMenuItem.setText("Import Data");
        importDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importDataMenuItemActionPerformed(evt);
            }
        });
        fileMenuItem.add(importDataMenuItem);

        menuBar.add(fileMenuItem);

        editMenuItem.setText("Edit");
        menuBar.add(editMenuItem);

        optionsMenuItem.setText("Options");

        AddChartMenuItem.setText("Add Chart");
        AddChartMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddChartMenuItemActionPerformed(evt);
            }
        });
        optionsMenuItem.add(AddChartMenuItem);

        menuBar.add(optionsMenuItem);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AddChartMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_RemoveChartMenuItemActionPerformed
        ActionListener addChart = new AddChartAction(split, charts, this);
        addChart.actionPerformed(evt);
    }// GEN-LAST:event_RemoveChartMenuItemActionPerformed

    private void importDataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_importDataMenuItemActionPerformed
        ActionListener importAction = new ImportDataAction(importChooser, chartDisplay);
        importAction.actionPerformed(evt);
    }// GEN-LAST:event_importDataMenuItemActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jToggleButton2ActionPerformed
        AbstractButton aB = (AbstractButton) evt.getSource();
        boolean selected = aB.getModel().isSelected();
        System.out.println("Action - selected = " + selected);

        if (selected) {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).emptyChart.setZoomTriggerDistance(ChartPanel.DEFAULT_ZOOM_TRIGGER_DISTANCE);
                charts.get(i).emptyChart.setFillZoomRectangle(true);
                charts.get(i).emptyChart.setZoomOutlinePaint(new Color(14, 139, 98));
            }
        } else if (!selected) {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).emptyChart.setZoomTriggerDistance(Integer.MAX_VALUE);
                charts.get(i).emptyChart.setFillZoomRectangle(false);
                charts.get(i).emptyChart.setZoomOutlinePaint(new Color(0f, 0f, 0f, 0f));
            }
        }
    }// GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jToggleButton4ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jToggleButton5ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jToggleButton6ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jToggleButton6ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AddChartMenuItem;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenu editMenuItem;
    private javax.swing.JMenu fileMenuItem;
    private javax.swing.JFileChooser importChooser;
    private javax.swing.JMenuItem importDataMenuItem;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu optionsMenuItem;
    // End of variables declaration//GEN-END:variables

    public javax.swing.JMenuItem getImportButton() {
        return importDataMenuItem;
    }

}
