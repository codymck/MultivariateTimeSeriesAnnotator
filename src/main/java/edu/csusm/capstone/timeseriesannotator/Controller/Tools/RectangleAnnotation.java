package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;

public class RectangleAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;
    private String type;
    private AnnotateChartPanel chartPanel;
    
    private Rectangle2D.Double storeRect = null;
    
    private ResizeHandle[] handles;

    private double[][] coordinates = { { 0.0, 0.0 }, { 0.0, 0.0 }, { 0.0, 0.0 }, { 0.0, 0.0 } };

    private double x, y, width, height;

    private XYShapeAnnotation rectAnnotation = null;
    private double[] minMax = { 0.0, 0.0, 0.0, 0.0 }; // minX, minY, maxX, maxY
    
    public RectangleAnnotation(XYPlot p, Color c, double[] point, String t, double[] m, AnnotateChartPanel cP) {
        this.handles = new ResizeHandle[]{null, null, null, null};
        this.plot = p;
        this.color = c;
        coordinates[0][0] = point[0];
        coordinates[0][1] = point[1];
        this.type = t;
        this.minMax = m;
        this.chartPanel = cP;
    }
    
    public RectangleAnnotation(XYPlot p, int[] c, double[][] coords, String[] t) {
        this.handles = new ResizeHandle[]{null, null, null, null};
        this.plot = p;
        String tempType = t[0];
        this.type = tempType.substring(1, tempType.length() - 1);
        this.color = new Color(c[0], c[1], c[2], c[3]);
        x = coords[0][0];
        y = coords[0][1];
        width = coords[0][2];
        height = coords[0][3];
        storeRect = new Rectangle2D.Double(x, y, width, height);
        rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(0),
                new Color(0, 0, 0, 0), color);
        plot.addAnnotation(rectAnnotation);
    }
    
    public void drawRect(double[] point) {
        if (rectAnnotation != null){
            plot.removeAnnotation(rectAnnotation);
            chartPanel.removeAbstractAnnotation(this);
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
        chartPanel.addAbstractAnnotation(this);
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        return storeRect.contains(p);
    }
    
    @Override
    public void select(){
        if(!selected){
            plot.removeAnnotation(rectAnnotation);
            rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(2),
                new Color(0, 0, 0), color);
            plot.addAnnotation(rectAnnotation);
            selected = true;
            updateCoords();
            for(int i = 0; i < 4; i++){
                handles[i] = new ResizeHandle(plot, coordinates[i], chartPanel);
            }
        }
    }
    
    @Override
    public void deselect(){
        if(selected){
            plot.removeAnnotation(rectAnnotation);
            for(int i = 0; i < 4; i++){
                handles[i].remove();
            }
            rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(0),
                new Color(0, 0, 0, 0), color);
            plot.addAnnotation(rectAnnotation);
            selected = false;
        }
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
        updateCoords();
        
        for(int i = 0; i < 4; i++){
            handles[i].changeCoords(coordinates[i]);
            handles[i].draw();
        }
        rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(2),
            new Color(0, 0, 0), color);
        plot.addAnnotation(rectAnnotation);
    }
    
    private void updateCoords(){
        coordinates[0][0] = storeRect.getX();
        coordinates[0][1] = storeRect.getY() + storeRect.getHeight();

        coordinates[1][0] = storeRect.getX() + storeRect.getWidth();
        coordinates[1][1] = storeRect.getY() + storeRect.getHeight();

        coordinates[2][0] = storeRect.getX() + storeRect.getWidth();
        coordinates[2][1] = storeRect.getY();

        coordinates[3][0] = storeRect.getX();
        coordinates[3][1] = storeRect.getY();
    }
    
    public void resizeHandles(){
        for(int i = 0; i < 4; i++){
            handles[i].recalculate();
            handles[i].draw();
        }
    }
    

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public String getType(){
        return "rectangle";
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
        return "[\"" + type + "\"]";
    }
}