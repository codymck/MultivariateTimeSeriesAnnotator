package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author Ben Theurich
 */
public class ResizeHandle {
    private double[] coords;
    private double[] size = {0.0, 0.0};
    private double[] offset = {0.0, 0.0};
    private XYPlot plot;
    
    private Ellipse2D.Double ell;
    private XYShapeAnnotation handle = null;
    
    
    public ResizeHandle(XYPlot p, double[] point){
        plot = p;
        coords = point;
        recalculate();
        draw();
    }
    
    public void recalculate(){
        ValueAxis domainAxis = plot.getDomainAxis();
        double domainMin = domainAxis.getLowerBound();
        double domainMax = domainAxis.getUpperBound();
        ValueAxis rangeAxis = plot.getRangeAxis();
        double rangeMin = rangeAxis.getLowerBound();
        double rangeMax = rangeAxis.getUpperBound();
        size[0] = (domainMax - domainMin) / 50.0;
        size[1] = (rangeMax - rangeMin) / 50.0;
        offset[0] = size[0] / 2.0;
        offset[1] = size[1] / 2.0;
        
    }
    
    public void draw(){
        if(handle != null){
            plot.removeAnnotation(handle);
        }
        ell = new Ellipse2D.Double(coords[0] - offset[0], coords[1] - offset[1], size[0], size[1]);
        handle = new XYShapeAnnotation(ell, new BasicStroke(0), new Color(0,0,0), new Color(0,0,0));
        plot.addAnnotation(handle);
    }
    
    public void remove(){
        if(handle != null){
            plot.removeAnnotation(handle);
        }
    }
    
    public void changeCoords(double[] point){
        coords = point;
        recalculate();
    }
    
    public boolean contains(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        return ell.contains(p);
    }
}
