package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author Cody McKinney
 */
public class Controller {
    
    public static ArrayList<XYPlot> synced;
    static NumberAxis commonXAxis = new NumberAxis("X");
    static AnnotateChartPanel chartPanel;
    
    
    public Controller(AnnotateChartPanel cP){
        this.synced = new ArrayList<>(6);
        this.chartPanel = cP;
    }
    
    public static void sync(double x1, double x2){
        //System.out.println("x1: " + x1 + " x2: " + x2);
        commonXAxis.setRange(x1,x2);
        
        
        if (synced != null){
            for(XYPlot x : synced){
                if(!chartPanel.isFocusOwner() && synced.size() <= 1){
                    System.out.println(x.getPlotType() + " size: " +  synced.size());
                    break;
                }
                x.setDomainAxis(commonXAxis);
            }
        }
    }
    
    public void addSync(XYPlot p){
        this.synced.add(p);
        //Rectangle2D screenDataArea = chartPanel.getScreenDataArea();
        //commonXAxis.setRange(screenDataArea.getMinX(),screenDataArea.getMaxX());
    }
    
    public void removeSync(XYPlot p){
        synced.remove(p);
    }
    
}
