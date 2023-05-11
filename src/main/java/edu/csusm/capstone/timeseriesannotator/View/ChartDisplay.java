package edu.csusm.capstone.timeseriesannotator.View;

import edu.csusm.capstone.timeseriesannotator.Controller.AddSeriesAction;
import edu.csusm.capstone.timeseriesannotator.Controller.ChartStruct;
import edu.csusm.capstone.timeseriesannotator.Controller.Controller;
import edu.csusm.capstone.timeseriesannotator.Controller.ImportDataAction;
import edu.csusm.capstone.timeseriesannotator.Model.ToolState;
import static edu.csusm.capstone.timeseriesannotator.View.ChartSelectMenu.color;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.AxisEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Josef Arevalo
 */
public class ChartDisplay extends javax.swing.JPanel implements ActionListener {

    AnnotateChartPanel aChartPanel;
    AppFrame frame;
    ChartStruct chartStruct;
    Controller control;
    XYPlot plot;
    JFreeChart chart;
    static int flag = 0;

    /**
     * Creates new form ChartPanel
     *
     * @param f
     */
    public ChartDisplay(AppFrame f) {
        this.frame = f;
        initComponents();
        startChart();

    }

    /**
     * Creates an empty chart as soon as a new chartDisplay is added to the app
     * frame
     */
    private void startChart() {
        XYSeriesCollection emptyData = new XYSeriesCollection();

        String chartTitle = "Data";
        String xAxisLabel = "X";
        String yAxisLabel = "Y";

        chart = ChartFactory.createXYLineChart(chartTitle,
                xAxisLabel, yAxisLabel, emptyData);

        aChartPanel = new AnnotateChartPanel(chart);
        aChartPanel.setZoomOutlinePaint(Color.BLACK);
        aChartPanel.setMouseWheelEnabled(true);
        aChartPanel.setBackground(Color.BLUE);

        JPopupMenu popupMenu = aChartPanel.getPopupMenu();
        popupMenu.setEnabled(false);

        plot = chart.getXYPlot();
        plot.setRangePannable(true);
        plot.setDomainPannable(true);
        plot.setBackgroundPaint(new java.awt.Color(255, 255, 255));
        plot.setOutlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineStroke(new BasicStroke(3));
        plot.setDomainGridlinePaint(new java.awt.Color(0, 0, 0, 70));
        plot.setRangeGridlinePaint(new java.awt.Color(0, 0, 0, 70));

        jPanel2.add(aChartPanel);
    }

    /**
     * @return XYPlot of current ChartDisplay
     */
    public XYPlot returnPlot() {
        chart = aChartPanel.getChart();
        return chart.getXYPlot();
    }

    /**
     * Sets a new chart for the current ChartDisplay
     *
     * @param p
     */
    public void setChart(AnnotateChartPanel p) {
        if (aChartPanel.getParent() == jPanel2) {
            jPanel2.remove(aChartPanel);
        }

        aChartPanel = p;
        ChartDisplay cD = this;

        JPopupMenu popupMenu = aChartPanel.getPopupMenu();
        popupMenu.setEnabled(false);

        aChartPanel.setZoomOutlinePaint(Color.BLACK);

        aChartPanel.addMouseMotionListener(p);

        aChartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                //Zooms screen to size of data
                if (event.getTrigger().getClickCount() == 2) {
                    if (aChartPanel.getToolState() != ToolState.SELECT) {
                        aChartPanel.restoreAutoBounds();
                        aChartPanel.setDomainZoomable(true);
                        aChartPanel.setRangeZoomable(true);
                    }
                }
                //checks if user is clicking on the axis or title labels
                if (event.getEntity().toString().contains("AxisEntity") || event.getEntity().toString().contains("TitleEntity")) {
                    LabelsMenu chartTitle = new LabelsMenu(new javax.swing.JFrame(), true);
                    chartTitle.setVisible(true);

                    if (!chartTitle.isSubmitted()) {//user exits label menu
                        return;
                    }

                    //Axis label has been selected and updates based on user input
                    if (event.getEntity().toString().contains("AxisEntity")) {
                        try {
                            AxisEntity l = (AxisEntity) event.getEntity().clone();
                            l.getAxis().setLabel(chartTitle.getComment());
                        }
                        catch (CloneNotSupportedException ex) {
                            Logger.getLogger(ChartDisplay.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {//Title label has been selected and updates based on user input
                        aChartPanel.getChart().setTitle(chartTitle.getComment());
                        chartStruct.getLabels().set(0, chartTitle.getComment());
                    }
                }

                //checks if the user selected an item in the legend
                if (event.getEntity().toString().contains("LegendItemEntity")) {
                    ChartEntity e = event.getEntity();
                    LegendItemEntity entity = (LegendItemEntity) e;
                    Comparable seriesKey = entity.getSeriesKey();
                    XYDataset dataset;
                    XYItemRenderer renderer;
                    boolean rotate = false;
                    ArrayList<String> labels = chartStruct.getLabels();

                    //loops through all datasets on plot
                    for (int i = 0; i < chartStruct.getFlag() - 1; i++) {
                        dataset = plot.getDataset(i);
                        renderer = plot.getRenderer(i);

                        if (dataset.getSeriesKey(0).equals(seriesKey)) {
                            //user left clicks Legend
                            if (event.getTrigger().getClickCount() == 1 && event.getTrigger().getButton() == 1) {
                                color = JColorChooser.showDialog(aChartPanel, "Select a color", new Color(0, 100, 255, 60));
                                renderer.setSeriesPaint(0, color);
                            }
                            //user right clicks Legend
                            if (event.getTrigger().getButton() == 3) {
                                //open menu to confirm deletion
                                int input = JOptionPane.showConfirmDialog(AppFrame.frame, "Are you sure you want to delete this series", "Delete Series", JOptionPane.YES_NO_OPTION);
                                if (input != 0) {
                                    break;
                                }

                                //case for single dataset on plot
                                if (chartStruct.getFlag() == 2) {
                                    frame.removeChart(cD);
                                    frame.initialChart();
                                    break;
                                }

                                plot.setDataset(i, null);
                                labels.remove(i);
                                chartStruct.setLabels(labels);
                                chartStruct.setFlag(chartStruct.getFlag() - 1);
                                rotate = true;
                            }
                        }
                        //moves every dataset after removed one forward
                        if (rotate && i < chartStruct.getFlag() - 1) {
                            plot.setRenderer(i, plot.getRenderer(i + 1));
                            plot.setDataset(i, plot.getDataset(i + 1));
                            if (i == chartStruct.getFlag() - 2)//sets very last dataset to null
                            {
                                plot.setDataset(i + 1, null);
                            }
                        }
                    }
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {

            }
        });

        aChartPanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                //check if the user is zooming out and enforces a zoom limit
                if (!event.isShiftDown()) {
                    int k = event.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK;
                    if (event.getPreciseWheelRotation() > 0 || event.isShiftDown() || k == MouseEvent.CTRL_DOWN_MASK) {
                        if (plot.getDomainAxis().getUpperBound() > aChartPanel.minMax[2] * 3 || plot.getDomainAxis().getLowerBound() < -aChartPanel.minMax[2] * 3
                                || plot.getRangeAxis().getUpperBound() > aChartPanel.minMax[3] * 3 || plot.getRangeAxis().getLowerBound() < -aChartPanel.minMax[3] * 3) {
                            aChartPanel.setDomainZoomable(false);
                            aChartPanel.setRangeZoomable(false);
                        }
                    } else {
                        aChartPanel.setDomainZoomable(true);
                        aChartPanel.setRangeZoomable(true);
                    }
                }
            }
        });

        jPanel2.add(aChartPanel);
        validate();
        repaint();
        setVisible(true);
    }

    public void setChartData(ChartStruct c) {
        this.chartStruct = c;
        this.plot = chartStruct.getPlot();
        chartStruct.setDomainAxis(this.plot.getDomainAxis());
        chartStruct.setRangeAxis(this.plot.getRangeAxis());
        control = new Controller();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        AddSeriesButton = new javax.swing.JButton();
        ExportAnnotationsButton = new javax.swing.JButton();
        ImportAnnotationsButton = new javax.swing.JButton();
        restoreAutoBoundsButton = new javax.swing.JButton();
        SyncButton = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        removeChartButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 938, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMinimumSize(new java.awt.Dimension(200, 23));
        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        AddSeriesButton.setBackground(new java.awt.Color(102, 196, 105));
        AddSeriesButton.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        AddSeriesButton.setText("Add Series");
        AddSeriesButton.setName("AddSeries");
        AddSeriesButton.setToolTipText("Add Series");
        AddSeriesButton.addActionListener(this);
        jPanel4.add(AddSeriesButton);

        ExportAnnotationsButton.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        ExportAnnotationsButton.setText("Export Annotations");
        ExportAnnotationsButton.setToolTipText("Export Annotations");
        ExportAnnotationsButton.setName("ExportAnnotations");
        ExportAnnotationsButton.addActionListener(this);
        jPanel4.add(ExportAnnotationsButton);

        ImportAnnotationsButton.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        ImportAnnotationsButton.setText("Import Annotations");
        ImportAnnotationsButton.setToolTipText("Import Annotations");
        ImportAnnotationsButton.setName("ImportAnnotations");
        ImportAnnotationsButton.addActionListener(this);
        jPanel4.add(ImportAnnotationsButton);

        restoreAutoBoundsButton.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        restoreAutoBoundsButton.setText("Refocus");
        restoreAutoBoundsButton.setToolTipText("Refocus");
        restoreAutoBoundsButton.setName("Refocus");
        restoreAutoBoundsButton.addActionListener(this);
        jPanel4.add(restoreAutoBoundsButton);

        SyncButton.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        SyncButton.setText("Sync");
        SyncButton.setToolTipText("Sync Charts");
        SyncButton.setName("SyncChart"); // NOI18N
        SyncButton.setName("SyncChart");
        SyncButton.addActionListener(this);
        jPanel4.add(SyncButton);

        removeChartButton.setBackground(new java.awt.Color(204, 0, 0));
        removeChartButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        removeChartButton.setForeground(new java.awt.Color(255, 255, 255));
        removeChartButton.setText("X");
        removeChartButton.setName("removeChartButton");
        removeChartButton.setToolTipText("Close Chart");
        removeChartButton.setMaximumSize(new java.awt.Dimension(25, 25));
        removeChartButton.setMinimumSize(new java.awt.Dimension(25, 25));
        removeChartButton.addActionListener(this);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(125, Short.MAX_VALUE)
                .addComponent(removeChartButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(removeChartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.add(jPanel3);

        add(jPanel4, java.awt.BorderLayout.PAGE_START);
    }

    // Code for dispatching events from components to event handlers.

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == AddSeriesButton) {
            ChartDisplay.this.AddSeriesButtonActionPerformed(evt);
        }
        else if (evt.getSource() == ExportAnnotationsButton) {
            ChartDisplay.this.ExportAnnotationsButtonActionPerformed(evt);
        }
        else if (evt.getSource() == ImportAnnotationsButton) {
            ChartDisplay.this.ImportAnnotationsButtonActionPerformed(evt);
        }
        else if (evt.getSource() == restoreAutoBoundsButton) {
            ChartDisplay.this.restoreAutoBoundsButtonActionPerformed(evt);
        }
        else if (evt.getSource() == SyncButton) {
            ChartDisplay.this.SyncButtonActionPerformed(evt);
        }
        else if (evt.getSource() == removeChartButton) {
            ChartDisplay.this.removeChartButtonActionPerformed(evt);
        }
    }// </editor-fold>//GEN-END:initComponents

    private void removeChartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeChartButtonActionPerformed
        if (frame.charts.size() > 1) {
            frame.removeChart(this);
        } else {
            frame.removeChart(this);
            frame.initialChart();
        }
    }//GEN-LAST:event_removeChartButtonActionPerformed

    private void AddSeriesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddSeriesButtonActionPerformed
        boolean toggle = false;
        if (SyncButton.isSelected()) {
            SyncButton.doClick();
            toggle = true;
        }

        if (chartStruct != null) {
            ActionListener addAction = new AddSeriesAction(this.chartStruct, this);
            addAction.actionPerformed(evt);
        } else {
            ActionListener importAction = new ImportDataAction(frame.getImportChooser(), this);
            importAction.actionPerformed(evt);
        }

        if (toggle == true) {
            SyncButton.doClick();
        }
    }//GEN-LAST:event_AddSeriesButtonActionPerformed

    private void ExportAnnotationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportAnnotationsButtonActionPerformed
        try {
            aChartPanel.exportAnnotations();
        }
        catch (IOException ex) {
            Logger.getLogger(ChartDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ExportAnnotationsButtonActionPerformed

    private void ImportAnnotationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportAnnotationsButtonActionPerformed
        try {
            aChartPanel.importAnnotations();
        }
        catch (IOException ex) {
            Logger.getLogger(ChartDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ImportAnnotationsButtonActionPerformed

    private void restoreAutoBoundsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restoreAutoBoundsButtonActionPerformed
        aChartPanel.restoreAutoBounds();
    }//GEN-LAST:event_restoreAutoBoundsButtonActionPerformed

    private void SyncButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SyncButtonActionPerformed
        if (SyncButton.isSelected() && plot != null) {
            aChartPanel.setSync(true);
            aChartPanel.setMouseZoomable(false);
            aChartPanel.setRangeZoomable(false);
            control.addSync(aChartPanel.getChart(), aChartPanel, chartStruct);
        } else if (SyncButton.isSelected()) {
            //Throw error "Can't Sync a empty chart!"
            SyncButton.setSelected(false);
        } else if (!SyncButton.isSelected()) {
            control.removeSync(aChartPanel.getChart());
            aChartPanel.setSync(false);
            plot.setDomainAxis(chartStruct.getDomainAxis());
            aChartPanel.restoreAutoBounds();
        }
    }//GEN-LAST:event_SyncButtonActionPerformed

    public boolean synced() {
        return true;
    }

    public AnnotateChartPanel getAChartPanel() {
        return aChartPanel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddSeriesButton;
    private javax.swing.JButton ExportAnnotationsButton;
    private javax.swing.JButton ImportAnnotationsButton;
    private javax.swing.JToggleButton SyncButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton removeChartButton;
    private javax.swing.JButton restoreAutoBoundsButton;
    // End of variables declaration//GEN-END:variables
}
