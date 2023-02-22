package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Model.XYLineChartDataset;
import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.Color;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Cody McKinney
 */
public class LineChart implements ChartsIF {
    
    XYLineChartDataset xyChart;
    Chart chartStruct = Chart.getInstance();
    XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer();
    
    LineChart() {
        
    }

    @Override
    public AnnotateChartPanel createChartPanel() {
        
        dataSetter();
        
        NumberAxis xAxis = new NumberAxis("Type");
        NumberAxis yAxis = new NumberAxis("Value");   
        String chartTitle = "Data";
        

        XYDataset data = xyChart.getDataset();
        
        XYPlot plot = new XYPlot(data, xAxis, yAxis, lineRenderer);
        chartStruct.setPlot(plot);
        
        plot.setDataset(0, data);
        plot.setBackgroundPaint(new java.awt.Color(204, 204, 204));
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        
        lineRenderer.setDefaultShapesVisible(false);
        
        JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        AnnotateChartPanel cP = new AnnotateChartPanel(chart);
        cP.setMouseZoomable(true);
        cP.setDomainZoomable(true);
        cP.setRangeZoomable(true);
        cP.setMouseWheelEnabled(true);
        
        plot.setRangePannable(true);
        plot.setDomainPannable(true);
        return cP;
    }
    
    @Override
    public AnnotateChartPanel addSeries(){
        
        dataSetter();
        
        XYPlot plotter = chartStruct.getPlot();
        XYDataset data = xyChart.getDataset2();
        
        plotter.setDataset(1, data);
        plotter.setRenderer(1,lineRenderer);
        plotter.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        
        JFreeChart chart = new JFreeChart("Test", JFreeChart.DEFAULT_TITLE_FONT, plotter, true);
        AnnotateChartPanel cP = new AnnotateChartPanel(chart);
        cP.setMouseZoomable(true);
        cP.setDomainZoomable(true);
        cP.setRangeZoomable(true);
        cP.setMouseWheelEnabled(true);
        
        plotter.setRangePannable(true);
        plotter.setDomainPannable(true);
        return cP;
    }
    
    public void dataSetter(){
        xyChart = chartStruct.getSeries();
        if(chartStruct.getFlag() == 1){
            xyChart.createDataset();
            chartStruct.setFlag(2);
        }
        else if (chartStruct.getFlag() == 2){
            xyChart.addDataset();
        }
    }
    
}
