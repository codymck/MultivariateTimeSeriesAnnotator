package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;

import java.awt.geom.Point2D;
import java.awt.Color;
import org.jfree.chart.plot.XYPlot;
import java.awt.geom.Path2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import java.awt.BasicStroke;
import org.jfree.chart.annotations.XYLineAnnotation;

public class TriangleAnnotation extends AbstractAnnotation {

    public boolean selected;
    public Color color;
    public XYPlot plot;

    private Path2D.Double storeTriangle = null;

    private double[][] coordinates = { { 0.0, 0.0 }, { 0.0, 0.0 }, { 0.0, 0.0 } };

    private XYShapeAnnotation triangleAnnotation = null;
    private XYLineAnnotation line = null;
    
    private int triClick = 0;

    public TriangleAnnotation(XYPlot p, Color c) {
        this.plot = p;
        this.color = c;
    }
    
    public TriangleAnnotation(XYPlot p, int[] c, double[][] coords) {
        this.plot = p;
        this.color = new Color(c[0], c[1], c[2], c[3]);
        this.coordinates = coords;
        storeTriangle = new Path2D.Double();
        storeTriangle.moveTo(coordinates[0][0], coordinates[0][1]);
        storeTriangle.lineTo(coordinates[1][0], coordinates[1][1]);
        storeTriangle.lineTo(coordinates[2][0], coordinates[2][1]);
        storeTriangle.closePath();
        triangleAnnotation = new XYShapeAnnotation(storeTriangle,
                new BasicStroke(0), new Color(0, 0, 0, 0), color);
        plot.addAnnotation(triangleAnnotation);
    }

    public void createShape(double[] point) {
        for(int i = triClick; i < 3; i++){
            coordinates[i][0] = point[0];
            coordinates[i][1] = point[1];
        }
        if(triClick == 0){
            line = new XYLineAnnotation(
                    point[0],
                    point[1],
                    point[0],
                    point[1],
                    new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
            plot.addAnnotation(line);
            triClick++;
        }else{
            if(triangleAnnotation != null){
                plot.removeAnnotation(triangleAnnotation);
            }
            storeTriangle = new Path2D.Double();
            storeTriangle.moveTo(coordinates[0][0], coordinates[0][1]);
            storeTriangle.lineTo(coordinates[1][0], coordinates[1][1]);
            storeTriangle.lineTo(coordinates[2][0], coordinates[2][1]);
            storeTriangle.closePath();
            triangleAnnotation = new XYShapeAnnotation(storeTriangle,
                    new BasicStroke(0), new Color(0, 0, 0, 0), color);
            plot.addAnnotation(triangleAnnotation);
            triClick++;
            if(triClick > 2){
                triClick = 0;
            }
        }
    }

    public void drawTriangle(double[] point) {
        if (triClick == 1) {
            plot.removeAnnotation(line);
            line = new XYLineAnnotation(
                    coordinates[0][0],
                    coordinates[0][1],
                    point[0],
                    point[1],
                    new BasicStroke(2.0f), color);
            plot.addAnnotation(line);
        } else if (triClick == 2) {
            if(line != null){
                plot.removeAnnotation(line);
                line = null;
            }
            plot.removeAnnotation(triangleAnnotation);
            storeTriangle = new Path2D.Double();
            storeTriangle.moveTo(coordinates[0][0], coordinates[0][1]);
            storeTriangle.lineTo(coordinates[1][0], coordinates[1][1]);
            storeTriangle.lineTo(point[0], point[1]);
            storeTriangle.closePath();
            triangleAnnotation = new XYShapeAnnotation(storeTriangle,
                    new BasicStroke(0), new Color(0, 0, 0, 0), color);
            plot.addAnnotation(triangleAnnotation);
        }
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        boolean r = storeTriangle.contains(p);
        
        if(r && !selected){
            plot.removeAnnotation(triangleAnnotation);
            triangleAnnotation = new XYShapeAnnotation(storeTriangle, new BasicStroke(2),
                new Color(0, 0, 0), color);
            plot.addAnnotation(triangleAnnotation);
            selected = true;
        }else if(r && selected){
            plot.removeAnnotation(triangleAnnotation);
            triangleAnnotation = new XYShapeAnnotation(storeTriangle, new BasicStroke(0),
                new Color(0, 0, 0, 0), color);
            plot.addAnnotation(triangleAnnotation);
            selected = false;
        }
        return r;
    }

    @Override
    public void delete() {
        plot.removeAnnotation(triangleAnnotation);
    }
    
    @Override
    public void move(double xOffset, double yOffset, boolean set) {
        plot.removeAnnotation(triangleAnnotation);
        if(!set){
            storeTriangle = new Path2D.Double();
            storeTriangle.moveTo(coordinates[0][0]-xOffset, coordinates[0][1]-yOffset);
            storeTriangle.lineTo(coordinates[1][0]-xOffset, coordinates[1][1]-yOffset);
            storeTriangle.lineTo(coordinates[2][0]-xOffset, coordinates[2][1]-yOffset);
            storeTriangle.closePath();
        }else{
            for(int i = 0; i < 3; i++){
                coordinates[i][0]-=xOffset;
                coordinates[i][1]-=yOffset;
            }
            storeTriangle = new Path2D.Double();
            storeTriangle.moveTo(coordinates[0][0], coordinates[0][1]);
            storeTriangle.lineTo(coordinates[1][0], coordinates[1][1]);
            storeTriangle.lineTo(coordinates[2][0], coordinates[2][1]);
            storeTriangle.closePath();
        }
        triangleAnnotation = new XYShapeAnnotation(storeTriangle, new BasicStroke(2),
            new Color(0, 0, 0), color);
        plot.addAnnotation(triangleAnnotation);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }
    
    @Override
    public String getType(){
        return "triangle";
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
        String set = "[";
        for (double[] coordinate : coordinates) {
            set += "[";
            for (int i = 0; i < coordinate.length; i++) {
                set += (String.valueOf(coordinate[i]));
                if (i < coordinate.length - 1){
                    set += ", ";
                }
            }
            set += "]";
            set += ", ";
        }
        set = set.substring(0, set.length() - 2);// remove the extra comma and space from the end of the string
        set += "]";
        return set;
    }
    
    @Override
    public String getData() {
        return "[\" \"]";
    }
    
}
