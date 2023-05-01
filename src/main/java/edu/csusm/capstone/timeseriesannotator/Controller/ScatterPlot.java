package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Model.XYLineChartDataset;
import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import edu.csusm.capstone.timeseriesannotator.View.CSVdataSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.ChartSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.EventListener;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Cody McKinney
 */
public class ScatterPlot implements ChartsIF {
    
    XYLineChartDataset xyChart;
    ChartStruct chartStruct;// = ChartStruct.getInstance();
    XYDotRenderer dotRenderer = new XYDotRenderer();
    ValueAxis xAxis;
    NumberAxis yAxis;
    XYDataset data;
    
    ScatterPlot() {
        
    }

    @Override
    public AnnotateChartPanel createChartPanel(ChartStruct c){
        
        chartStruct = c;
        dataSetter();
        
        if(HDFdataSelectMenu.HDF == null){
            //CSV process
            if(CSVdataSelectMenu.CSV.getTimeStamp()){
                DateAxis Axis = new DateAxis(chartStruct.getLabels().get(1));
                xAxis = (DateAxis)Axis;
                data = xyChart.getDateDataset();
            }else{
                NumberAxis Axis = new NumberAxis(chartStruct.getLabels().get(1));
                Axis.setAutoRangeIncludesZero(false);
                Axis.setAutoRange(true);
                xAxis = (NumberAxis)Axis;
                data = xyChart.getDataset();
            }
        }
        else if(CSVdataSelectMenu.CSV == null){
            //HDF process
            if(HDFdataSelectMenu.HDF.getTimeStamp()){
                DateAxis Axis = new DateAxis(chartStruct.getLabels().get(1));
                xAxis = (DateAxis)Axis;
                data = xyChart.getDateDataset();
            }else{
                NumberAxis Axis = new NumberAxis(chartStruct.getLabels().get(1));
                Axis.setAutoRangeIncludesZero(false);
                Axis.setAutoRange(true);
                xAxis = (NumberAxis)Axis;
                data = xyChart.getDataset();
            }
        }
        yAxis = new NumberAxis(chartStruct.getLabels().get(2));
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setAutoRange(true); 
        
        String chartTitle = chartStruct.getLabels().get(0);
        
        dotRenderer.setSeriesPaint(0, ChartSelectMenu.getColor());
        dotRenderer.setDotWidth(5);
        dotRenderer.setDotHeight(5);
        
        XYPlot plot = new XYPlot(data, xAxis, yAxis, dotRenderer);
        chartStruct.setPlot(plot);  
        
        plot.setDataset(0, data);
        plot.setBackgroundPaint(new java.awt.Color(255, 255, 255));
        plot.setDomainGridlinePaint(new java.awt.Color(0, 0, 0, 70));
        plot.setRangeGridlinePaint(new java.awt.Color(0, 0, 0, 70));
        plot.setOutlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineStroke(new BasicStroke(3));
        
        JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        AnnotateChartPanel cP = new AnnotateChartPanel(chart);
        chartStruct.setAnnotateChartPanel(cP);
        cP.setMouseZoomable(true);
        cP.setDomainZoomable(true);
        cP.setRangeZoomable(true);
        cP.setMouseWheelEnabled(true);
        cP.redrawNewMM();
        
        plot.setRangePannable(true);
        plot.setDomainPannable(true);
        return cP;
    }
    
    @Override
    public AnnotateChartPanel addSeries(ChartStruct c){
        
        chartStruct = c;
        dataSetter();
        
        XYPlot plotter = chartStruct.getPlot();
        XYDataset data2;
        if(xyChart.getDataset2() == null){
            data2 = xyChart.getDateDataset2();
        }else{
            data2 = xyChart.getDataset2();
        }
        
        dotRenderer.setSeriesPaint(0, ChartSelectMenu.getColor());//possibly update later for user selection
        dotRenderer.setDotWidth(5);
        dotRenderer.setDotHeight(5);
        
        plotter.setDataset(chartStruct.getFlag()-2, data2);
//        plotter.setBackgroundPaint(new java.awt.Color(204, 204, 204));
//        plotter.setDomainGridlinePaint(Color.WHITE);
//        plotter.setRangeGridlinePaint(Color.WHITE);
        
        plotter.setRenderer(chartStruct.getFlag()-2,dotRenderer);
        plotter.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        
        JFreeChart chart = new JFreeChart(chartStruct.getLabels().get(0), JFreeChart.DEFAULT_TITLE_FONT, plotter, true);
        AnnotateChartPanel cP = chartStruct.getAnnotateChartPanel();
        EventListener[] t = cP.getListeners(ChartMouseListener.class);
        for(EventListener x : t){
            cP.removeChartMouseListener((ChartMouseListener) x);
        } 
        
        cP.setChart(chart);
        cP.setMouseZoomable(true);
        cP.setDomainZoomable(true);
        cP.setRangeZoomable(true);
        cP.setMouseWheelEnabled(true);
        cP.redrawNewMM();
        cP.restoreAutoBounds();
        
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
