/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Controller;

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
    
    
    public static ChartPanel buildCharts(ChartTypes c) {
        switch (c) {
            case LineChart -> {
                LineChart l = new LineChart();
                return l.createChartPanel();
            }
            case StepChart -> {
                StepChart s = new StepChart();
                return s.createChartPanel();
            }
            case ScatterPlot -> {
                ScatterPlot sP = new ScatterPlot();
                return sP.createChartPanel();
            }
            default -> throw new IllegalArgumentException("Invalid Chart");
        }
    }
}
