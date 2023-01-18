package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Model.XYLineChartDataset;
import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Cody McKinney
 */
public class StepChart implements ChartsIF {
    
    XYLineChartDataset xyChart;
    Chart chartStruct = Chart.getInstance();
    XYStepRenderer stepRenderer = new XYStepRenderer();
    
    StepChart() {
        
    }

    @Override
    public AnnotateChartPanel createChartPanel() {
               
        dataSetter();
        
        NumberAxis xAxis = new NumberAxis("Type");
        NumberAxis yAxis = new NumberAxis("Value");   
        String chartTitle = "Data";
        
        stepRenderer.setSeriesPaint(0, new java.awt.Color(0, 100, 0));
        stepRenderer.setSeriesShapesVisible(0,  false);

        XYDataset data = xyChart.getDataset();
        
        XYPlot plot = new XYPlot(data, xAxis, yAxis, stepRenderer);
        chartStruct.setPlot(plot);
        
        plot.setDataset(0, data);
        
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
        plotter.setRenderer(1,stepRenderer);
        plotter.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        
        JFreeChart chart = new JFreeChart("Test", JFreeChart.DEFAULT_TITLE_FONT, plotter, true);
        AnnotateChartPanel cP = new AnnotateChartPanel(chart);
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
