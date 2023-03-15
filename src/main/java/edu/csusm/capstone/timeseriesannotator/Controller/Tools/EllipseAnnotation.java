package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import com.opencsv.CSVWriter;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;

public class EllipseAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;

    private Ellipse2D.Double storeEllipse = null;

    private double[][] coordinates = { { 0.0, 0.0 }, { 0.0, 0.0 } };
    
    private double x, y, width, height;

    private XYShapeAnnotation ellipseAnnotation = null;

    public EllipseAnnotation(XYPlot p, Color c, double[] point) {
        this.plot = p;
        this.color = c;
        coordinates[0][0] = point[0];
        coordinates[0][1] = point[1];
    }

    public void drawEllipse(double[] point) {
        if (ellipseAnnotation != null){
            plot.removeAnnotation(ellipseAnnotation);
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
    public void export(CSVWriter writer) {
        String[] annotation_type = {"ellipse"};
        String[] rgba = getRGBAList();
        String[] coords = getCoordsList();
        
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
        String[] coord = {String.valueOf(x), String.valueOf(y), String.valueOf(width), String.valueOf(height)};
        
        return coord;
    }
}