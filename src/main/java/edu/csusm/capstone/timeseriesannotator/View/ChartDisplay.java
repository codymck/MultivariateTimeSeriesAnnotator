package edu.csusm.capstone.timeseriesannotator.View;

import edu.csusm.capstone.timeseriesannotator.Controller.AddSeriesAction;
import edu.csusm.capstone.timeseriesannotator.Controller.ChartStruct;
import edu.csusm.capstone.timeseriesannotator.Controller.Controller;
import edu.csusm.capstone.timeseriesannotator.Controller.ImportDataAction;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.entity.AxisEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.TitleEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author josef
 */
public class ChartDisplay extends javax.swing.JPanel implements ActionListener {

    AnnotateChartPanel aChartPanel;
    AppFrame frame;
    ChartStruct chartStruct;
    Controller control;
    XYPlot plot;
    JFreeChart chart;

    /**
     * Creates new form ChartPanel
     */
    public ChartDisplay(AppFrame f) {
        this.frame = f;
        initComponents();
        startChart();
        
    }

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

        XYPlot plot = chart.getXYPlot();
        plot.setRangePannable(true);
        plot.setDomainPannable(true);
        plot.setBackgroundPaint(new java.awt.Color(255, 255, 255));
        plot.setOutlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineStroke(new BasicStroke(3));
        plot.setDomainGridlinePaint(new java.awt.Color(0, 0, 0, 70));
        plot.setRangeGridlinePaint(new java.awt.Color(0, 0, 0, 70));


        jPanel2.add(aChartPanel);
    }

    public XYPlot returnPlot() {
        chart = aChartPanel.getChart();
        return chart.getXYPlot();
    }

    public void setChart(AnnotateChartPanel p) {
        if (aChartPanel.getParent() == jPanel2) {
            jPanel2.remove(aChartPanel);
        }
        
        
        aChartPanel = p;
        
        JPopupMenu popupMenu = aChartPanel.getPopupMenu();
        popupMenu.setEnabled(false);
        
        aChartPanel.setZoomOutlinePaint(Color.BLACK);

        aChartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                if (event.getTrigger().getClickCount() == 2) {
                    aChartPanel.restoreAutoBounds();
                    aChartPanel.setDomainZoomable(true);
                    aChartPanel.setRangeZoomable(true);
                }
                if(event.getEntity().toString().contains("AxisEntity") || event.getEntity().toString().contains("TitleEntity")){
                    LabelsMenu chartTitle = new LabelsMenu(new javax.swing.JFrame(), true);
                    chartTitle.setVisible(true);
                    
                    if(!chartTitle.isSubmitted())return;
                    
                    if(event.getEntity().toString().contains("AxisEntity")){
                        try {
                            AxisEntity l = (AxisEntity)event.getEntity().clone();
                            l.getAxis().setLabel(chartTitle.getComment());
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(ChartDisplay.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else{
                        aChartPanel.getChart().setTitle(chartTitle.getComment());
                        chartStruct.getLabels().set(0, chartTitle.getComment());
                    }
                }                
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                
            }
        });
        
        aChartPanel.addMouseWheelListener(new MouseWheelListener(){
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                if(!event.isShiftDown()){
                    int k = event.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK;
                    //System.out.println("Type: " + event.getScrollType() + " Precise: " + event.getPreciseWheelRotation());
                    if (event.getPreciseWheelRotation() > 0 || event.isShiftDown() || k == MouseEvent.CTRL_DOWN_MASK) {
                        //System.out.println("max x: " + aChartPanel.maxX*3 + " min x: " + aChartPanel.minX*3 + " max y: " + aChartPanel.maxY*3 + " min y: " + aChartPanel.minY*3);
                        if (plot.getDomainAxis().getUpperBound() > aChartPanel.minMax[2]*3 || plot.getDomainAxis().getLowerBound() < -aChartPanel.minMax[2]*3 ||
                            plot.getRangeAxis().getUpperBound() > aChartPanel.minMax[3]*3 || plot.getRangeAxis().getLowerBound() < -aChartPanel.minMax[3]*3){
                            aChartPanel.setDomainZoomable(false);
                            aChartPanel.setRangeZoomable(false);
                        }
                    }
                    else{
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
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
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
        jPanel3 = new javax.swing.JPanel();
        removeChartButton = new javax.swing.JButton();
        SyncButton = new javax.swing.JToggleButton();

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
        ExportAnnotationsButton.setName("ExportAnnotations");
        ImportAnnotationsButton.addActionListener(this);
        jPanel4.add(ImportAnnotationsButton);

        restoreAutoBoundsButton.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        restoreAutoBoundsButton.setText("Refocus");
        restoreAutoBoundsButton.setToolTipText("Refocus");
        restoreAutoBoundsButton.setName("Refocus");
        restoreAutoBoundsButton.addActionListener(this);
        jPanel4.add(restoreAutoBoundsButton);

        removeChartButton.setBackground(new java.awt.Color(204, 0, 0));
        removeChartButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        removeChartButton.setForeground(new java.awt.Color(255, 255, 255));
        removeChartButton.setText("X");
        removeChartButton.setName("removeChartButton");
        removeChartButton.setToolTipText("Close Chart");
        removeChartButton.setMaximumSize(new java.awt.Dimension(25, 25));
        removeChartButton.setMinimumSize(new java.awt.Dimension(25, 25));
        removeChartButton.addActionListener(this);

        SyncButton.setBackground(new java.awt.Color(255, 255, 204));
        SyncButton.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        SyncButton.setText("Sync");
        SyncButton.addActionListener(this);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(94, Short.MAX_VALUE)
                .addComponent(SyncButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeChartButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeChartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SyncButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
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
        else if (evt.getSource() == removeChartButton) {
            ChartDisplay.this.removeChartButtonActionPerformed(evt);
        }
        else if (evt.getSource() == SyncButton) {
            ChartDisplay.this.SyncButtonActionPerformed(evt);
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
        if(SyncButton.isSelected()){
            SyncButton.doClick();
            toggle = true;
        }
        
        if (chartStruct != null) {
            ActionListener addAction = new AddSeriesAction(this.chartStruct, this);
            addAction.actionPerformed(evt);
        } else{
            ActionListener importAction = new ImportDataAction(frame.getImportChooser(), this);
            importAction.actionPerformed(evt);
        }
        
        if(toggle == true){
            SyncButton.doClick();
        }
    }//GEN-LAST:event_AddSeriesButtonActionPerformed

    private void ExportAnnotationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportAnnotationsButtonActionPerformed
        try {
            aChartPanel.exportAnnotations();
        } catch (IOException ex) {
            Logger.getLogger(ChartDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ExportAnnotationsButtonActionPerformed

    private void ImportAnnotationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportAnnotationsButtonActionPerformed
        try {
            aChartPanel.importAnnotations();
        } catch (IOException ex) {
            Logger.getLogger(ChartDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ImportAnnotationsButtonActionPerformed

    private void restoreAutoBoundsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restoreAutoBoundsButtonActionPerformed
        aChartPanel.restoreAutoBounds();
    }//GEN-LAST:event_restoreAutoBoundsButtonActionPerformed

    private void SyncButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SyncButtonActionPerformed
        if(SyncButton.isSelected() && plot != null ){
            aChartPanel.setSync(true);
            aChartPanel.setMouseZoomable(false);
            aChartPanel.setRangeZoomable(false);
            control.addSync(aChartPanel.getChart(), aChartPanel);
        }
        else if(SyncButton.isSelected()){
            //Throw error "Can't Sync a empty chart!"
            SyncButton.setSelected(false);
        }
        else if(!SyncButton.isSelected()){
            control.removeSync(aChartPanel.getChart());
            aChartPanel.setSync(false);
            plot.setDomainAxis(chartStruct.getDomainAxis());
            aChartPanel.restoreAutoBounds();
        }
    }//GEN-LAST:event_SyncButtonActionPerformed

    public boolean synced(){
        return true;
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
