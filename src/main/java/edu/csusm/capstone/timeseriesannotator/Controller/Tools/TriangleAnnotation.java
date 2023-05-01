package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.geom.Point2D;
import java.awt.Color;
import org.jfree.chart.plot.XYPlot;
import java.awt.geom.Path2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import java.awt.BasicStroke;
import java.awt.geom.PathIterator;
import org.jfree.chart.annotations.XYLineAnnotation;

/**
 *
 * @author Ben Theurich
 * @author Cody McKinney
 */
public class TriangleAnnotation extends AbstractAnnotation {

    private Path2D.Double storeTriangle = null;

    private double[][] coordinates = {{0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}};
    
    private double[][] handleCoordinates = {{0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}};
    private boolean dragHandle = false;
    private int handleNumber = 0;

    
    private XYShapeAnnotation triangleAnnotation = null;
    private XYLineAnnotation line = null;

    private int triClick = 0;

    public TriangleAnnotation(XYPlot p, Color c, AnnotateChartPanel cP) {
        this.handles = new ResizeHandle[]{null, null, null};
        this.plot = p;
        this.color = new Color(c.getRed(), c.getGreen(), c.getBlue(), fillAlpha);
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
                dashed, color);
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
                    new BasicStroke(2.0f), color);
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
        boolean clicked = storeTriangle.contains(p);
        if(selected){
            for(int i = 0; i < 3; i++){
                if(handles[i].contains(mouseX, mouseY)){
                    dragHandle = true;
                    handleNumber = i;
                    return true;
                }
            }
        }
        return clicked;
    }
    
    @Override
    public void select(){
        if(!selected){
            plot.removeAnnotation(triangleAnnotation);
            triangleAnnotation = new XYShapeAnnotation(storeTriangle, dashed,
                    new Color(0, 0, 0), color);
            plot.addAnnotation(triangleAnnotation);
            selected = true;
            updateHandleCoords();
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
            double[][] tempCoords = {{0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}};
            for(int i = 0; i < 3; i++){
                if(!dragHandle){
                    tempCoords[i][0] = coordinates[i][0] - xOffset;
                    tempCoords[i][1] = coordinates[i][1] - yOffset;
                }else{
                    tempCoords[i][0] = coordinates[i][0];
                    tempCoords[i][1] = coordinates[i][1];
                    if(i == handleNumber){
                        tempCoords[i][0] = coordinates[i][0] - xOffset;
                        tempCoords[i][1] = coordinates[i][1] - yOffset;
                    }
                }
            }
            redrawTriangle(tempCoords);
        } else {
            if(!dragHandle){
                for (int i = 0; i < 3; i++) {
                    coordinates[i][0] -= xOffset;
                    coordinates[i][1] -= yOffset;
                }
            }else{
                coordinates[handleNumber][0] -= xOffset;
                coordinates[handleNumber][1] -= yOffset;
            }
            redrawTriangle(coordinates);
            dragHandle = false;
        }
        updateHandleCoords();
        
        for(int i = 0; i < 3; i++){
            handles[i].changeCoords(handleCoordinates[i]);
            handles[i].draw();
        }
        triangleAnnotation = new XYShapeAnnotation(storeTriangle, dashed,
                new Color(0, 0, 0), color);
        plot.addAnnotation(triangleAnnotation);
    }
    
    private void redrawTriangle(double[][] c){
        storeTriangle = new Path2D.Double();
        storeTriangle.moveTo(c[0][0], c[0][1]);
        storeTriangle.lineTo(c[1][0], c[1][1]);
        storeTriangle.lineTo(c[2][0], c[2][1]);
        storeTriangle.closePath();
    }
    
    private void updateHandleCoords(){
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
                i++;
            }
            iterator.next();
        }
    }
    
    @Override
    public void changeColor(Color c){
        color = new Color(c.getRed(), c.getGreen(), c.getBlue(), fillAlpha);
        plot.removeAnnotation(triangleAnnotation);
        triangleAnnotation = new XYShapeAnnotation(storeTriangle, dashed,
                    new Color(0, 0, 0), color);
        plot.addAnnotation(triangleAnnotation);
    }

    @Override
    public String getType() {
        return "triangle";
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
