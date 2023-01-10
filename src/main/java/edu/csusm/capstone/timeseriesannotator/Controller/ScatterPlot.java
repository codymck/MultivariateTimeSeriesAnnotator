package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Model.XYLineChartDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Cody McKinney
 */
public class ScatterPlot implements ChartsIF {
    
    XYLineChartDataset xyChart;
    Chart chartStruct = Chart.getInstance();
    
    ScatterPlot() {
        
    }

    @Override
    public ChartPanel createChartPanel() {
        
        dataSetter();
        
        String chartTitle = "Data";
        String xAxisLabel = "X";
        String yAxisLabel = "Y";

        XYDataset data = xyChart.getDataset();
        
        JFreeChart chart = ChartFactory.createScatterPlot(chartTitle,
                xAxisLabel, yAxisLabel, data);

        ChartPanel cP = new ChartPanel(chart);
        cP.setMouseZoomable(true);
        cP.setDomainZoomable(true);
        cP.setRangeZoomable(false);
        cP.setMouseWheelEnabled(true);
        
        XYDotRenderer r = new XYDotRenderer();
        r.setSeriesPaint(0, java.awt.Color.blue);
        r.setDotWidth(5);
        r.setDotHeight(5);
        
        
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
