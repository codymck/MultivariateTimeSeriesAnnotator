package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.BasicStroke;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;

public class EllipseAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;
    private AnnotateChartPanel chartPanel;

    private Ellipse2D.Double storeEllipse = null;

    private double[][] coordinates = { { 0.0, 0.0 }, { 0.0, 0.0 } };
    
    private double x, y, width, height;

    private XYShapeAnnotation ellipseAnnotation = null;

    public EllipseAnnotation(XYPlot p, Color c, double[] point, AnnotateChartPanel cP) {
        this.plot = p;
        this.color = c;
        this.chartPanel = cP;
        coordinates[0][0] = point[0];
        coordinates[0][1] = point[1];
    }
    
    public EllipseAnnotation(XYPlot p, int[] c, double[][] coords) {
        this.plot = p;
        this.color = new Color(c[0], c[1], c[2], c[3]);
        x = coords[0][0];
        y = coords[0][1];
        width = coords[0][2];
        height = coords[0][3];
        storeEllipse = new Ellipse2D.Double(x, y, width, height);
        ellipseAnnotation = new XYShapeAnnotation(storeEllipse, new BasicStroke(0),
                new Color(0, 0, 0, 0), color);
        plot.addAnnotation(ellipseAnnotation);
    }

    public void drawEllipse(double[] point) {
        if (ellipseAnnotation != null){
            plot.removeAnnotation(ellipseAnnotation);
            chartPanel.removeAbstractAnnotation(this);
        }
        coordinates[1][0] = point[0];
        coordinates[1][1] = point[1];
        x = Math.min(coordinates[0][0], coordinates[1][0]);
        y = Math.min(coordinates[0][1], coordinates[1][1]);
        width = Math.abs(coordinates[1][0] - coordinates[0][0]);
        height = Math.abs(coordinates[1][1] - coordinates[0][1]);
        storeEllipse = new Ellipse2D.Double(x, y, width, height);
        ellipseAnnotation = new XYShapeAnnotation(storeEllipse, new BasicStroke(0),
                new Color(0, 0, 0, 0), color);
        plot.addAnnotation(ellipseAnnotation);
        chartPanel.addAbstractAnnotation(this);
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        boolean r = storeEllipse.contains(p);
        
        if(r && !selected){
            plot.removeAnnotation(ellipseAnnotation);
            ellipseAnnotation = new XYShapeAnnotation(storeEllipse, new BasicStroke(2),
                new Color(0, 0, 0), color);
            plot.addAnnotation(ellipseAnnotation);
            selected = true;
        }else if(r && selected){
            plot.removeAnnotation(ellipseAnnotation);
            ellipseAnnotation = new XYShapeAnnotation(storeEllipse, new BasicStroke(0),
                new Color(0, 0, 0, 0), color);
            plot.addAnnotation(ellipseAnnotation);
            selected = false;
        }
        return r;
    }

    @Override
    public void delete() {
        plot.removeAnnotation(ellipseAnnotation);
    }
    
    @Override
    public void move(double xOffset, double yOffset, boolean set) {
        plot.removeAnnotation(ellipseAnnotation);
        if(!set){
            storeEllipse.setFrame(x-xOffset, y-yOffset, width, height);
        }else{
            x -= xOffset;
            y -= yOffset;
            storeEllipse.setFrame(x, y, width, height);
        }
        ellipseAnnotation = new XYShapeAnnotation(storeEllipse, new BasicStroke(2),
            new Color(0, 0, 0), color);
        plot.addAnnotation(ellipseAnnotation);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }
    
    @Override
    public String getType(){
        return "ellipse";
    }
    
    @Override
    public String getRGBA() {
        String R = String.valueOf(color.getRed());
        String G = String.valueOf(color.getGreen());
        String B = String.valueOf(color.getBlue());
        String A = String.valueOf(color.getAlpha());

        return "[" + R + ", " + G + ", " + B + ", " + A + "]";
    }
    
    @Override
    public String getCoords() {
        String X = String.valueOf(x);
        String Y = String.valueOf(y);
        String WIDTH = String.valueOf(width);
        String HEIGHT = String.valueOf(height);

        return "[[" + X + ", " + Y + ", " + WIDTH + ", " + HEIGHT + "]]";
    }
    
    @Override
    public String getData() {
        return "[\" \"]";
    }
}