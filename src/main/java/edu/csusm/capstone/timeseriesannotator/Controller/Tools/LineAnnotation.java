package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import java.awt.BasicStroke;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;

public class LineAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;
    
    private Line2D.Double storeLine = null;
    private Rectangle2D.Double bounds;
    
    private double[][] coordinates = { { 0.0, 0.0 }, { 0.0, 0.0 } };

    private XYShapeAnnotation lineAnnotation = null;
        
    public LineAnnotation(XYPlot p, Color c, double[] point) {
        this.plot = p;
        this.color = c;
        coordinates[0][0] = point[0];
        coordinates[0][1] = point[1];
    }
    
    public void drawLine(double[] point) {
        if (lineAnnotation != null){
            plot.removeAnnotation(lineAnnotation);
        }
        coordinates[1][0] = point[0];
        coordinates[1][1] = point[1];
        
        storeLine = new Line2D.Double(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
        lineAnnotation = new XYShapeAnnotation(storeLine, new BasicStroke(2), color);
        plot.addAnnotation(lineAnnotation);
    }
    
    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        bounds = (Rectangle2D.Double) storeLine.getBounds2D();
        boolean r = bounds.contains(p);
        return r;
    }

    @Override
    public void delete() {
        plot.removeAnnotation(lineAnnotation);                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void move(double newX, double newY, boolean set) {

    }

    @Override
    public boolean isSelected() {
        return selected;
    }
}