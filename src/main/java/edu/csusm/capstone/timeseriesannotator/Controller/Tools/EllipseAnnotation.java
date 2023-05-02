package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.BasicStroke;
import static java.lang.Math.abs;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author Ben Theurich
 * @author Cody McKinney
 */
public class EllipseAnnotation extends AbstractAnnotation {

    private Ellipse2D.Double storeEllipse = null;
    
    private double[][] coordinates = { { 0.0, 0.0 }, { 0.0, 0.0 }, { 0.0, 0.0 }, { 0.0, 0.0 } };
    private boolean dragHandle = false;
    private int handleNumber = 0;
    private double x, y, width, height;

    private XYShapeAnnotation ellipseAnnotation = null;

    public EllipseAnnotation(XYPlot p, Color c, double[] point, AnnotateChartPanel cP) {
        this.handles = new ResizeHandle[]{null, null, null, null};
        this.plot = p;
        this.color = new Color(c.getRed(), c.getGreen(), c.getBlue(), fillAlpha);
        this.chartPanel = cP;
        coordinates[0][0] = point[0];
        coordinates[0][1] = point[1];
    }
    
    public EllipseAnnotation(XYPlot p, int[] c, double[][] coords, AnnotateChartPanel cP) {
        this.chartPanel = cP;
        this.handles = new ResizeHandle[]{null, null, null, null};
        this.plot = p;
        this.color = new Color(c[0], c[1], c[2], c[3]);
        x = coords[0][0];
        y = coords[0][1];
        width = coords[0][2];
        height = coords[0][3];
        storeEllipse = new Ellipse2D.Double(x, y, width, height);
        ellipseAnnotation = new XYShapeAnnotation(storeEllipse, dashed,
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
        ellipseAnnotation = new XYShapeAnnotation(storeEllipse, dashed,
                new Color(0, 0, 0, 0), color);
        plot.addAnnotation(ellipseAnnotation);
        chartPanel.addAbstractAnnotation(this);
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        boolean clicked = storeEllipse.contains(p);
        clicked = storeEllipse.contains(p);
        if(selected){
            for(int i = 0; i < 4; i++){
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
            plot.removeAnnotation(ellipseAnnotation);
            ellipseAnnotation = new XYShapeAnnotation(storeEllipse, dashed,
                new Color(0, 0, 0), color);
            plot.addAnnotation(ellipseAnnotation);
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
            plot.removeAnnotation(ellipseAnnotation);
            for(int i = 0; i < 4; i++){
                handles[i].remove();
            }
            ellipseAnnotation = new XYShapeAnnotation(storeEllipse, new BasicStroke(0),
                new Color(0, 0, 0, 0), color);
            plot.addAnnotation(ellipseAnnotation);
            selected = false;
        }
    }
    
    @Override
    public void delete() {
        plot.removeAnnotation(ellipseAnnotation);
        if(selected){
            for(int i = 0; i < 4; i++){
                handles[i].remove();
            }
        }
    }
    
    @Override
    public void move(double xOffset, double yOffset, boolean set) {
        plot.removeAnnotation(ellipseAnnotation);
        double tempWidth, tempHeight;
        if(!set){
            if(!dragHandle){
                storeEllipse.setFrame(x-xOffset, y-yOffset, width, height);
            }else{
                switch (handleNumber) {
                    case 0 -> {
                        tempWidth = width+xOffset;
                        if(tempWidth > 0){
                            storeEllipse.setFrame(x-xOffset, y, width+xOffset, height);
                        }else{
                            storeEllipse.setFrame(x-xOffset+tempWidth, y, abs(tempWidth), height);
                        }
                    }
                    case 2 -> {
                        tempWidth = width-xOffset;
                        if(tempWidth > 0){
                            storeEllipse.setFrame(x, y, width-xOffset, height);
                        }else{
                            storeEllipse.setFrame(x-xOffset+width, y, abs(tempWidth), height);
                        }
                    }
                    case 1 -> {
                        tempHeight = height-yOffset;
                        if(tempHeight > 0){
                            storeEllipse.setFrame(x, y, width, height-yOffset);
                        }else{
                            storeEllipse.setFrame(x, y-yOffset+height, width, abs(tempHeight));
                        }
                    }
                    case 3 -> {
                        tempHeight = height+yOffset;
                        if(tempHeight > 0){
                            storeEllipse.setFrame(x, y-yOffset, width, height+yOffset);
                        }else{
                            storeEllipse.setFrame(x, y-yOffset+tempHeight, width, abs(tempHeight));
                        }
                    }
                    default -> {
                    }
                }
            }
        }else{
            if(!dragHandle){
                x -= xOffset;
                y -= yOffset;
            }else{
                switch (handleNumber) {
                    case 0 -> {
                        tempWidth = width+xOffset;
                        if(tempWidth > 0){
                            x -= xOffset;
                            width += xOffset;
                        }else{
                            x = x-xOffset+tempWidth;
                            width = abs(tempWidth);
                        }
                    }
                    case 2 -> {
                        tempWidth = width-xOffset;
                        if(tempWidth > 0){
                            width -= xOffset;
                        }else{
                            x = x-xOffset+width;
                            width = abs(tempWidth);
                        }
                    }
                    case 1 -> {
                        tempHeight = height-yOffset;
                        if(tempHeight > 0){
                            height -= yOffset;
                        }else{
                            y = y-yOffset+height;
                            height = abs(tempHeight);
                        }
                    }
                    case 3 -> {
                        tempHeight = height+yOffset;
                        if(tempHeight > 0){
                            y -= yOffset;
                            height += yOffset;
                        }else{
                            y = y-yOffset+tempHeight;
                            height = abs(tempHeight);
                        }
                    }
                    default -> {
                    }
                }
            }
            storeEllipse.setFrame(x, y, width, height);
            dragHandle = false;
        }
        updateCoords();
        
        for(int i = 0; i < 4; i++){
            handles[i].changeCoords(coordinates[i]);
            handles[i].draw();
        }
        ellipseAnnotation = new XYShapeAnnotation(storeEllipse, dashed,
            new Color(0, 0, 0), color);
        plot.addAnnotation(ellipseAnnotation);
    }
    
    private void updateCoords(){
        coordinates[0][0] = storeEllipse.getX();
        coordinates[0][1] = storeEllipse.getY() + storeEllipse.getHeight()/2;

        coordinates[1][0] = storeEllipse.getX() + storeEllipse.getWidth()/2;
        coordinates[1][1] = storeEllipse.getY() + storeEllipse.getHeight();

        coordinates[2][0] = storeEllipse.getX() + storeEllipse.getWidth();
        coordinates[2][1] = storeEllipse.getY() + storeEllipse.getHeight()/2;

        coordinates[3][0] = storeEllipse.getX() + storeEllipse.getWidth()/2;
        coordinates[3][1] = storeEllipse.getY();
    }
    
    @Override
    public void changeColor(Color c){
        color = new Color(c.getRed(), c.getGreen(), c.getBlue(), fillAlpha);
        plot.removeAnnotation(ellipseAnnotation);
        ellipseAnnotation = new XYShapeAnnotation(storeEllipse, dashed,
                    new Color(0, 0, 0), color);
        plot.addAnnotation(ellipseAnnotation);
    }
    
    @Override
    public String getType(){
        return "ellipse";
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