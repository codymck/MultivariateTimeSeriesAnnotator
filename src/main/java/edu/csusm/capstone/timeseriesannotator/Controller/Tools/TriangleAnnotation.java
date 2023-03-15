package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import com.opencsv.CSVWriter;
import edu.csusm.capstone.timeseriesannotator.View.AppFrame;

import java.awt.geom.Point2D;
import java.awt.Color;
import org.jfree.chart.plot.XYPlot;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.util.List;
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
    public void export(CSVWriter writer) {
        String[] annotation_type = {"triangle"};
        String[] rgba = getRGBAList();
        String[] coords = getTriCoords();
        
        String[] row = {annotation_type[0], rgba[0], coords[0]};
        
        writer.writeNext(row);
    }

    @Override
    public String[] getRGBAList() {
        int R = color.getRed();
        int G = color.getGreen();
        int B = color.getBlue();
        int A = color.getAlpha();
        
        String[] rgba = {'[' + String.valueOf(R) + '/' + String.valueOf(G) + '/' + String.valueOf(B) + '/' + String.valueOf(A) + ']'};
        
        return rgba;
    }

    @Override
    public String[] getDataList() {
        return null;
    }

    @Override
    public String[] getCoordsList() {
        return null;
    }
    
    public String[] getTriCoords() {
        String[] coord = new String[1]; 
        
        String set = "";
        for (double[] coordinate : coordinates) {
            set += '[';
            for (int i = 0; i < coordinate.length; i++) {
                set+= (String.valueOf(coordinate[i]));
                if (i < coordinate.length - 1)
                {
                    set+= '/';
                }
            }
            set += ']';
        }
        coord[0] = set;
        return coord;
    }
}
