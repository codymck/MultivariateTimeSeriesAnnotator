package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

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
        System.out.println(""+type);
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
    public void export() {
        String annotation_type = "Line";
    }

    @Override
    public List<Integer> getRGBAList() {
        int R = color.getRed();
        int G = color.getGreen();
        int B = color.getBlue();
        int A = color.getAlpha();
        List<Integer> rgba = new ArrayList<>();
        rgba.add(R);
        rgba.add(G);
        rgba.add(B);
        rgba.add(A);
        
        return rgba;
    }

    @Override
    public List<String> getDataList() {
        List<String> data = new ArrayList<>();
        if (type.equals("diagonal")) data.add("diagonal");
        else if (type.equals("ray")) data.add("ray");
        else if (type.equals("segment")) data.add("segment");
        
        return data;
    }

    @Override
    public List<Double> getCoordsList() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}