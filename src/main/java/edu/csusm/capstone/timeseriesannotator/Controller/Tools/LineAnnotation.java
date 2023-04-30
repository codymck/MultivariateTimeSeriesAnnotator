package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author Ben Theurich
 * @author Cody McKinney
 */
public class LineAnnotation extends AbstractAnnotation {
    private Line2D.Double storeLine = null;
    private Rectangle2D.Double intersectRect;

    private double[][] coordinates = {{0.0, 0.0}, {0.0, 0.0}};
    private double[] startPoint = {0.0, 0.0};

    private XYShapeAnnotation lineAnnotation = null;

    public LineAnnotation(XYPlot p, Color c, double[] point, String t, AnnotateChartPanel cP) {
        this.plot = p;
        this.color = c;
        this.type = t;
        this.chartPanel = cP;
        coordinates[0] = point;
        startPoint[0] = point[0];
        startPoint[1] = point[1];
    }

    public LineAnnotation(XYPlot p, int[] c, double[][] coords, String[] t) {
        this.plot = p;
        String tempType = t[0];
        this.type = tempType.substring(1, tempType.length() - 1);
        this.color = new Color(c[0], c[1], c[2], c[3]);
        this.coordinates = coords;
        storeLine = new Line2D.Double(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
        lineAnnotation = new XYShapeAnnotation(storeLine, new BasicStroke(2), color);
        plot.addAnnotation(lineAnnotation);
    }

    public void drawLine(double[] point) {
        double dx, dy, angle, length;
        if (lineAnnotation != null) {
            plot.removeAnnotation(lineAnnotation);
            chartPanel.removeAbstractAnnotation(this);
        }
        switch (type) {
            case "segment" ->
                coordinates[1] = point;
            case "ray" -> {
                dx = startPoint[0] - point[0]; // change in x
                dy = startPoint[1] - point[1]; // change in y
                angle = Math.atan2(dy, dx); // angle of line
                length = Math.max(chartPanel.minMax[2] - chartPanel.minMax[0], chartPanel.minMax[3] - chartPanel.minMax[1]) * 10; // length of line
                coordinates[1][0] = startPoint[0] - length * Math.cos(angle);
                coordinates[1][1] = startPoint[1] - length * Math.sin(angle);
            }
            case "diagonal" -> {
                dx = startPoint[0] - point[0]; // change in x
                dy = startPoint[1] - point[1]; // change in y
                angle = Math.atan2(dy, dx); // angle of line
                length = Math.max(chartPanel.minMax[2] - chartPanel.minMax[0], chartPanel.minMax[3] - chartPanel.minMax[1]) * 10; // length of line
                coordinates[1][0] = startPoint[0] - length * Math.cos(angle);
                coordinates[1][1] = startPoint[1] - length * Math.sin(angle);

                coordinates[0][0] = point[0] + length * Math.cos(angle);
                coordinates[0][1] = point[1] + length * Math.sin(angle);
            }
            default -> {
            }
        }
        storeLine = new Line2D.Double(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
        lineAnnotation = new XYShapeAnnotation(storeLine, new BasicStroke(2), color);
        plot.addAnnotation(lineAnnotation);
        chartPanel.addAbstractAnnotation(this);
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        ValueAxis domainAxis = plot.getDomainAxis();
        double plotDomain = domainAxis.getUpperBound() - domainAxis.getLowerBound();
        ValueAxis rangeAxis = plot.getRangeAxis();
        double plotRange = rangeAxis.getUpperBound() - rangeAxis.getLowerBound();
        
        Rectangle2D.Double screenDataArea = (Rectangle2D.Double) chartPanel.getScreenDataArea();
        double screenWidthPx = screenDataArea.getMaxX() - screenDataArea.getMinX();
        double screenHeightPx = screenDataArea.getMaxY() - screenDataArea.getMinY();

        double xSize = plotDomain*10 / screenWidthPx;
        double ySize = plotRange*10 / screenHeightPx;

        double xOffset = xSize / 2.0;
        double yOffset = ySize / 2.0;
        intersectRect = new Rectangle2D.Double(mouseX - xOffset, mouseY - yOffset, xSize, ySize);
        //XYShapeAnnotation hitbox = new XYShapeAnnotation(intersectRect, new BasicStroke(0), color, color);
        //plot.addAnnotation(hitbox);
        boolean r = storeLine.intersects(intersectRect);
        return r;
    }
    
    @Override
    public void select(){
        if(!selected){
            plot.removeAnnotation(lineAnnotation);
            lineAnnotation = new XYShapeAnnotation(storeLine, dashed, color);
            plot.addAnnotation(lineAnnotation);
            selected = true;
        }
    }
    
    @Override
    public void deselect(){
        if(selected){
            plot.removeAnnotation(lineAnnotation);
            lineAnnotation = new XYShapeAnnotation(storeLine, new BasicStroke(2), color);
            plot.addAnnotation(lineAnnotation);
            selected = false;
        }
    }

    @Override
    public void delete() {
        if (lineAnnotation != null) {
            plot.removeAnnotation(lineAnnotation);
        }
    }

    @Override
    public void move(double xOffset, double yOffset, boolean set) {
        plot.removeAnnotation(lineAnnotation);
        if(!set){
            storeLine.setLine(coordinates[0][0] - xOffset, coordinates[0][1] - yOffset, coordinates[1][0] - xOffset, coordinates[1][1] - yOffset);

        }else{
            coordinates[0][0] -= xOffset;
            coordinates[0][1] -= yOffset;
            coordinates[1][0] -= xOffset;
            coordinates[1][1] -= yOffset;
        }
        lineAnnotation = new XYShapeAnnotation(storeLine, dashed, color);
        plot.addAnnotation(lineAnnotation);
    }
    
    @Override
    public String getType() {
        return "line";
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
        return "[\"" + type + "\"]";
    }
}
