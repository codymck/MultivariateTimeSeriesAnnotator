package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;

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
    
    
    public static AnnotateChartPanel buildCharts(ChartTypes c, ChartStruct cS) {
        switch (c) {
            case LineChart -> {
                LineChart l = new LineChart();
                return (AnnotateChartPanel) l.createChartPanel(cS);
            }
            case StepChart -> {
                StepChart s = new StepChart();
                return (AnnotateChartPanel) s.createChartPanel(cS);
            }
            case ScatterPlot -> {
                ScatterPlot sP = new ScatterPlot();
                return (AnnotateChartPanel) sP.createChartPanel(cS);
            }
            default -> throw new IllegalArgumentException("Invalid Chart");
        }
    }
    
    public static AnnotateChartPanel addSeries(ChartTypes c, ChartStruct cS) {
        AnnotateChartPanel cp;
        switch (c) {
            case LineChart -> {
                LineChart l = new LineChart();

                cp = l.addSeries(cS);
            }
            case StepChart -> {
                StepChart s = new StepChart();
                cp = s.addSeries(cS);
            }
            case ScatterPlot -> {
                ScatterPlot sP = new ScatterPlot();
                cp = sP.addSeries(cS);

            }
            default -> throw new IllegalArgumentException("Invalid Chart");
        }
        return cp;
    }
}
