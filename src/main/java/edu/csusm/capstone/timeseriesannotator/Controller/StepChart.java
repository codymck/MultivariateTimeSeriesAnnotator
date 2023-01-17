package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Model.XYLineChartDataset;
import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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
    
    StepChart() {
        
    }

    @Override
    public AnnotateChartPanel createChartPanel() {
        
        dataSetter();
        
        String chartTitle = "Data";
        String xAxisLabel = "X";
        String yAxisLabel = "Y";

        XYDataset data = xyChart.getDataset();

        JFreeChart chart = ChartFactory.createXYStepChart(chartTitle,
                xAxisLabel, yAxisLabel, data);

        AnnotateChartPanel cP = new AnnotateChartPanel(chart);
        cP.setMouseZoomable(true);
        cP.setDomainZoomable(true);
        cP.setRangeZoomable(true);
        cP.setMouseWheelEnabled(true);
        
        XYStepRenderer r = new XYStepRenderer();
        r.setSeriesPaint(0, new java.awt.Color(0, 100, 0));
        r.setSeriesShapesVisible(0,  false);
        
        
        XYPlot plot = (XYPlot) chart.getXYPlot();
        plot.setDataset(0, data);
        plot.setRenderer(0, r);
        plot.setRangePannable(true);
        plot.setDomainPannable(true);
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
