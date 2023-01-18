package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author Cody McKinney
 */
public interface ChartsIF {
    public ChartPanel createChartPanel();
    
    public AnnotateChartPanel addSeries();
}
