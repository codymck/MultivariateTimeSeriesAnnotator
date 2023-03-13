package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import com.opencsv.CSVWriter;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;

public class RectangleAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;
    private String type;
    private Rectangle2D.Double storeRect = null;

    private double[][] coordinates = { { 0.0, 0.0 }, { 0.0, 0.0 } };

    private double x, y, width, height;

    private XYShapeAnnotation rectAnnotation = null;
    private double[] minMax = { 0.0, 0.0, 0.0, 0.0 }; // minX, minY, maxX, maxY
    
    public RectangleAnnotation(XYPlot p, Color c, double[] point, String t, double[] m) {
        this.plot = p;
        this.color = c;
        coordinates[0][0] = point[0];
        coordinates[0][1] = point[1];
        this.type = t;
        this.minMax = m;
    }
    
    public void drawRect(double[] point) {
        if (rectAnnotation != null){
            plot.removeAnnotation(rectAnnotation);
        }
        
        if(type.equals("rectangle")){
            coordinates[1][0] = point[0];
            coordinates[1][1] = point[1];
            x = Math.min(coordinates[0][0], coordinates[1][0]);
            y = Math.min(coordinates[0][1], coordinates[1][1]);
            width = Math.abs(coordinates[1][0] - coordinates[0][0]);
            height = Math.abs(coordinates[1][1] - coordinates[0][1]);
        }else if(type.equals("region")){
            double rectHeight = 10 * (minMax[3] - minMax[1]);
            coordinates[1][0] = point[0];
            x = Math.min(coordinates[0][0], coordinates[1][0]);
            y = minMax[3] - (rectHeight/2);
            width = Math.abs(coordinates[1][0] - coordinates[0][0]);
            height = rectHeight;
        }
        storeRect = new Rectangle2D.Double(x, y, width, height);
        rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(0),
                new Color(0, 0, 0, 0), color);
        plot.addAnnotation(rectAnnotation);
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        boolean r = storeRect.contains(p);
        
        if(r && !selected){
            plot.removeAnnotation(rectAnnotation);
            rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(2),
                new Color(0, 0, 0), color);
            plot.addAnnotation(rectAnnotation);
            selected = true;
        }else if(r && selected){
            plot.removeAnnotation(rectAnnotation);
            rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(0),
                new Color(0, 0, 0, 0), color);
            plot.addAnnotation(rectAnnotation);
            selected = false;
        }
        
        return r;
    }

    @Override
    public void delete() {
        plot.removeAnnotation(rectAnnotation);
    }

    @Override
    public void move(double xOffset, double yOffset, boolean set) {
        plot.removeAnnotation(rectAnnotation);
        if(!set){
            storeRect.setFrame(x-xOffset, y-yOffset, width, height);
        }else{
            x -= xOffset;
            y -= yOffset;
            storeRect.setFrame(x, y, width, height);
        }
        rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(2),
            new Color(0, 0, 0), color);
        plot.addAnnotation(rectAnnotation);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void export(CSVWriter writer) {
        String annotation_type = "";
        if (type.equals("rectangle")) { annotation_type = "rectangle"; }
        else if (type.equals("region")) { annotation_type = "region"; }
        
        List<Integer> rgba = getRGBAList();
        List<Double> coords = getCoordsList();
        
        System.out.println("Annotation Type: " + annotation_type);
        System.out.println("Coordinates: " + coords.toString());
        System.out.println("RGBA Values: " + rgba.toString());
        System.out.println("");
        
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
        return null;
    }

    @Override
    public List<Double> getCoordsList() {
        List<Double> coords = new ArrayList<>();
        if (type.equals("rectangle")) { 
            coords.add(x);
            coords.add(y);
            coords.add(width);
            coords.add(height);
        }
        else if (type.equals("region")) {
            coords.add(x);
            coords.add(width);
        }
        
        return coords;
    }

}