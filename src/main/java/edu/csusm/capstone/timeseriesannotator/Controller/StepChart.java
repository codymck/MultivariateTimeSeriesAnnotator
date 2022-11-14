/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Model.XYLineChartDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Cody McKinney
 */
public class StepChart implements ChartsIF {
    
    StepChart() {
        
    }

    @Override
    public ChartPanel createChartPanel() {
        XYLineChartDataset xyChart = new XYLineChartDataset();
        xyChart.createDataset();
        
        String chartTitle = "Data";
        String xAxisLabel = "X";
        String yAxisLabel = "Y";

        XYDataset data = xyChart.getDataset();

        JFreeChart chart = ChartFactory.createXYStepChart(chartTitle,
                xAxisLabel, yAxisLabel, data);

        ChartPanel cP = new ChartPanel(chart);
        cP.setMouseZoomable(true);
        cP.setDomainZoomable(true);
        cP.setRangeZoomable(false);
        cP.setMouseWheelEnabled(true);
        
        XYPlot plot = chart.getXYPlot();
        plot.setRangePannable(true);
        plot.setDomainPannable(true);
        return cP;
    }
    
}
