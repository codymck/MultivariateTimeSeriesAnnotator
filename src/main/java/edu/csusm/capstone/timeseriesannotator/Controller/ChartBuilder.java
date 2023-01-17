package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author Cody McKinney
 */
public class ChartBuilder {
    
    public enum ChartTypes {
        LineChart, 
        ScatterPlot, 
        StepChart;
    }
    
    
    public static AnnotateChartPanel buildCharts(ChartTypes c) {
        switch (c) {
            case LineChart -> {
                LineChart l = new LineChart();
                return (AnnotateChartPanel) l.createChartPanel();
            }
            case StepChart -> {
                StepChart s = new StepChart();
                return (AnnotateChartPanel) s.createChartPanel();
            }
            case ScatterPlot -> {
                ScatterPlot sP = new ScatterPlot();
                return (AnnotateChartPanel) sP.createChartPanel();
            }
            default -> throw new IllegalArgumentException("Invalid Chart");
        }
    }
}
