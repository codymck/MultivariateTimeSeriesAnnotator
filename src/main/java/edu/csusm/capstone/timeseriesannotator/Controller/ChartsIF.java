package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;


/**
 *
 * @author Cody McKinney
 */
public interface ChartsIF {
    public AnnotateChartPanel createChartPanel(ChartStruct c);
    
    public AnnotateChartPanel addSeries(ChartStruct c);
}
