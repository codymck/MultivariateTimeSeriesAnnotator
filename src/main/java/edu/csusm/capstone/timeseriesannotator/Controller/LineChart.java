package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Model.XYLineChartDataset;
import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.BasicStroke;
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
    ChartStruct chartStruct;// = ChartStruct.getInstance();
    XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer();
    
    LineChart() {
        
    }

    @Override
    public AnnotateChartPanel createChartPanel(ChartStruct c){
        
        chartStruct = c;
        dataSetter();
        
        NumberAxis xAxis = new NumberAxis(chartStruct.getLabels().get(1));
        NumberAxis yAxis = new NumberAxis(chartStruct.getLabels().get(2));   
        String chartTitle = chartStruct.getLabels().get(0);
        

        XYDataset data = xyChart.getDataset();
        
        XYPlot plot = new XYPlot(data, xAxis, yAxis, lineRenderer);
        chartStruct.setPlot(plot);
        
        xAxis.setAutoRangeIncludesZero(false);
        xAxis.setAutoRange(true);
        
        plot.setDataset(0, data);
        plot.setBackgroundPaint(new java.awt.Color(255, 255, 255));
        plot.setDomainGridlinePaint(new java.awt.Color(0, 0, 0, 70));
        plot.setRangeGridlinePaint(new java.awt.Color(0, 0, 0, 70));
        plot.setOutlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineStroke(new BasicStroke(3));
        
        lineRenderer.setDefaultShapesVisible(false);
        
        JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        AnnotateChartPanel cP = new AnnotateChartPanel(chart);
        chartStruct.setAnnotateChartPanel(cP);
        cP.setMouseZoomable(true);
        cP.setDomainZoomable(true);
        cP.setRangeZoomable(true);
        cP.setMouseWheelEnabled(true);
        
        plot.setRangePannable(true);
        plot.setDomainPannable(true);
        return cP;
    }
    
    @Override
    public AnnotateChartPanel addSeries(ChartStruct c){
        
        chartStruct = c;
        dataSetter();
        
        XYPlot plotter = chartStruct.getPlot();
        XYDataset data = xyChart.getDataset2();
        
        plotter.setDataset(chartStruct.getFlag()-1, data);
        plotter.setRenderer(chartStruct.getFlag()-1,lineRenderer);
        plotter.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        
        JFreeChart chart = new JFreeChart(chartStruct.getLabels().get(0), JFreeChart.DEFAULT_TITLE_FONT, plotter, true);
        AnnotateChartPanel cP = chartStruct.getAnnotateChartPanel();
        cP.setChart(chart);
//        cP.setBackground(Color.WHITE);
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
            xyChart.createDataset(chartStruct.getLabels().get(2));
            chartStruct.setFlag(2);
        }
        else if (chartStruct.getFlag() >= 2){
            String name = chartStruct.getLabels().get(chartStruct.getFlag()+1);
            xyChart.addDataset(name);
            chartStruct.setFlag(chartStruct.getFlag()+1);
        }
    }
    
}
