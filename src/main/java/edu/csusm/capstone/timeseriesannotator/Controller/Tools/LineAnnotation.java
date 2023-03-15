package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import com.opencsv.CSVWriter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

public class LineAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;
    private String type;
    private Line2D.Double storeLine = null;
    private Rectangle2D.Double intersectRect;
    
    private double[] minMax = { 0.0, 0.0, 0.0, 0.0 }; // minX, minY, maxX, maxY
    
    private double[][] coordinates = { { 0.0, 0.0 }, { 0.0, 0.0 } };
    private double[] startPoint = { 0.0, 0.0 };
    
    private XYShapeAnnotation lineAnnotation = null;
        
    public LineAnnotation(XYPlot p, Color c, double[] point, String t, double[] m) {
        this.plot = p;
        this.color = c;
        this.type = t;
        coordinates[0] = point;
        startPoint[0] = point[0];
        startPoint[1] = point[1];
        this.minMax = m;
    }
    
    public void drawLine(double[] point) {
        double dx, dy, angle, length;
        if (lineAnnotation != null){
            plot.removeAnnotation(lineAnnotation);
        }
        switch (type) {
            case "segment" -> coordinates[1] = point;
            case "ray" -> {
                dx = startPoint[0] - point[0]; // change in x
                dy = startPoint[1] - point[1]; // change in y
                angle = Math.atan2(dy, dx); // angle of line
                length = Math.max(minMax[2] - minMax[0], minMax[3] - minMax[1]) * 3; // length of line
                coordinates[1][0] = startPoint[0] - length * Math.cos(angle);
                coordinates[1][1] = startPoint[1] - length * Math.sin(angle);
            }
            case "diagonal" -> {
                dx = startPoint[0] - point[0]; // change in x
                dy = startPoint[1] - point[1]; // change in y
                angle = Math.atan2(dy, dx); // angle of line
                length = Math.max(minMax[2] - minMax[0], minMax[3] - minMax[1]) * 3; // length of line
                coordinates[0][0] = startPoint[0] - length * Math.cos(angle);
                coordinates[0][1] = startPoint[1] - length * Math.sin(angle);
                
                coordinates[1][0] = point[0] + length * Math.cos(angle);
                coordinates[1][1] = point[1] + length * Math.sin(angle);
            }
            default -> {
            }
        }
        storeLine = new Line2D.Double(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
        lineAnnotation = new XYShapeAnnotation(storeLine, new BasicStroke(2), color);
        plot.addAnnotation(lineAnnotation);
    }
    
    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        ValueAxis domainAxis = plot.getDomainAxis();
        double domainMin = domainAxis.getLowerBound();
        double domainMax = domainAxis.getUpperBound();
        ValueAxis rangeAxis = plot.getRangeAxis();
        double rangeMin = rangeAxis.getLowerBound();
        double rangeMax = rangeAxis.getUpperBound();
        double xSize = (domainMax - domainMin) / 50.0;
        double ySize = (rangeMax - rangeMin) / 50.0;
        double xOffset = xSize / 2.0;
        double yOffset = ySize / 2.0;
        intersectRect = new Rectangle2D.Double(mouseX-xOffset, mouseY-yOffset, xSize, ySize);
        //XYShapeAnnotation hitbox = new XYShapeAnnotation(intersectRect, new BasicStroke(0), color, color);
        //plot.addAnnotation(hitbox);
        boolean r = storeLine.intersects(intersectRect);
        return r;
    }

    @Override
    public void delete() {
        plot.removeAnnotation(lineAnnotation);
    }
    
    @Override
    public void move(double newX, double newY, boolean set) {

    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void export(CSVWriter writer) {
        String[] annotation_type = {"lines"};
        String[] rgba = getRGBAList();
        String[] coords = getLineCoords();
        String[] data = getDataList();
        
        String[] row = {annotation_type[0], rgba[0], coords[0], data[0]};
        
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
        String[] data = new String[1];
        if (type.equals("diagonal")) data[0] = "diagonal";
        else if (type.equals("ray")) data[0] = "ray";
        else if (type.equals("segment")) data[0] = "segment";
        
        return data;
    }

    @Override
    public String[] getCoordsList() {
        return null;
    }
    
    public String[] getLineCoords() {
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