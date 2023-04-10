package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import edu.csusm.capstone.timeseriesannotator.View.AppFrame;

import java.awt.geom.Point2D;
import java.awt.Color;
import org.jfree.chart.plot.XYPlot;
import java.awt.geom.Path2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import java.awt.BasicStroke;
import java.awt.geom.PathIterator;
import org.jfree.chart.annotations.XYLineAnnotation;

public class TriangleAnnotation extends AbstractAnnotation {

    public boolean selected;
    public Color color;
    public XYPlot plot;
    private AnnotateChartPanel chartPanel;

    private Path2D.Double storeTriangle = null;

    private double[][] coordinates = {{0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}};
    
    private ResizeHandle[] handles;
    private double[][] handleCoordinates = {{0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}};

    
    private XYShapeAnnotation triangleAnnotation = null;
    private XYLineAnnotation line = null;

    private int triClick = 0;

    public TriangleAnnotation(XYPlot p, Color c, AnnotateChartPanel cP) {
        this.handles = new ResizeHandle[]{null, null, null};
        this.plot = p;
        this.color = c;
        this.chartPanel = cP;
    }

    public TriangleAnnotation(XYPlot p, int[] c, double[][] coords) {
        this.handles = new ResizeHandle[]{null, null, null};
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
        for (int i = triClick; i < 3; i++) {
            coordinates[i][0] = point[0];
            coordinates[i][1] = point[1];
        }
        if (triClick == 0) {
            line = new XYLineAnnotation(
                    point[0],
                    point[1],
                    point[0],
                    point[1],
                    new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
            plot.addAnnotation(line);
            triClick++;
        } else {
            if (triangleAnnotation != null) {
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
            if (triClick > 2) {
                chartPanel.addAbstractAnnotation(this);
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
            if (line != null) {
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
        return storeTriangle.contains(p);
    }
    
    @Override
    public void select(){
        if(!selected){
            plot.removeAnnotation(triangleAnnotation);
            triangleAnnotation = new XYShapeAnnotation(storeTriangle, new BasicStroke(2),
                    new Color(0, 0, 0), color);
            plot.addAnnotation(triangleAnnotation);
            selected = true;
            updateCoords();
            for(int i = 0; i < 3; i++){
                handles[i] = new ResizeHandle(plot, coordinates[i], chartPanel);
            }
        }
    }
    
    @Override
    public void deselect(){
        if(selected){
            plot.removeAnnotation(triangleAnnotation);
            for(int i = 0; i < 3; i++){
                handles[i].remove();
            }
            triangleAnnotation = new XYShapeAnnotation(storeTriangle, new BasicStroke(0),
                    new Color(0, 0, 0, 0), color);
            plot.addAnnotation(triangleAnnotation);
            selected = false;
        }
    }

    @Override
    public void delete() {
//        System.out.println("TriClick: " + triClick);
        if (line != null) {
            plot.removeAnnotation(line);
            line = null;
        }
        if(triangleAnnotation != null){
            plot.removeAnnotation(triangleAnnotation);
        }
        if(selected){
            for(int i = 0; i < 3; i++){
                handles[i].remove();
            }
        }
        triClick = 0;
    }

    @Override
    public void move(double xOffset, double yOffset, boolean set) {
        plot.removeAnnotation(triangleAnnotation);
        if (!set) {
            storeTriangle = new Path2D.Double();
            storeTriangle.moveTo(coordinates[0][0] - xOffset, coordinates[0][1] - yOffset);
            storeTriangle.lineTo(coordinates[1][0] - xOffset, coordinates[1][1] - yOffset);
            storeTriangle.lineTo(coordinates[2][0] - xOffset, coordinates[2][1] - yOffset);
            storeTriangle.closePath();
        } else {
            for (int i = 0; i < 3; i++) {
                coordinates[i][0] -= xOffset;
                coordinates[i][1] -= yOffset;
            }
            storeTriangle = new Path2D.Double();
            storeTriangle.moveTo(coordinates[0][0], coordinates[0][1]);
            storeTriangle.lineTo(coordinates[1][0], coordinates[1][1]);
            storeTriangle.lineTo(coordinates[2][0], coordinates[2][1]);
            storeTriangle.closePath();
        }
        updateCoords();
        
        for(int i = 0; i < 3; i++){
            handles[i].changeCoords(handleCoordinates[i]);
            handles[i].draw();
        }
        triangleAnnotation = new XYShapeAnnotation(storeTriangle, new BasicStroke(2),
                new Color(0, 0, 0), color);
        plot.addAnnotation(triangleAnnotation);
    }
    
    private void updateCoords(){
        PathIterator iterator = storeTriangle.getPathIterator(null);
        int i = 0;
        double[] coords = new double[2];
        while (!iterator.isDone()) {
            int type = iterator.currentSegment(coords);
            if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO) {
                double x = coords[0];
                double y = coords[1];
                handleCoordinates[i][0] = x;
                handleCoordinates[i][1] = y;
                System.out.println("x: " + x + " - y : " + y);
                i++;
            }
            iterator.next();
        }
    }
    
    public void resizeHandles(){
        for(int i = 0; i < 3; i++){
            handles[i].recalculate();
            handles[i].draw();
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public String getType() {
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
                if (i < coordinate.length - 1) {
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
