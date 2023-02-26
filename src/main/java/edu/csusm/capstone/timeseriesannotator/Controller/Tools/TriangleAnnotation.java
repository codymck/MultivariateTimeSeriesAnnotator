package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;

import java.awt.geom.Point2D;
import java.awt.Color;
import org.jfree.chart.plot.XYPlot;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import java.awt.BasicStroke;
import org.jfree.chart.annotations.XYLineAnnotation;

public class TriangleAnnotation extends AbstractAnnotation {

    public boolean selected;
    public Color color;
    public XYPlot plot;

    private Path2D.Double drawTriangle = null;
    private Path2D.Double storeTriangle = null;
    
    private double[][] coordinates = { { 0.0, 0.0 }, { 0.0, 0.0 }, { 0.0, 0.0 } };
    
    private Point2D sPoint;
    private Point2D sPoint2;
    private Point2D ePoint;

    private double x;
    private double y;

    private double width;
    private double height;

    private XYShapeAnnotation triangleAnnotation;

    private int triClick = 0;
    private XYLineAnnotation line;

    public TriangleAnnotation(XYPlot p, Color c) {
        this.plot = p;
        this.color = c;
    }

    public Path2D.Double createShape(double[] point, Point2D pointObj) {
        switch (triClick) {
            case 0 -> {
                sPoint = pointObj;
                coordinates[0][0] = point[0];
                coordinates[0][1] = point[1];
                line = new XYLineAnnotation(
                        point[0],
                        point[1],
                        point[0],
                        point[1],
                        new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
                plot.addAnnotation(line);
                drawTriangle = new Path2D.Double();
                triClick++;
            }
            case 1 -> {
                plot.removeAnnotation(line);
                sPoint2 = pointObj;
                coordinates[1][0] = point[0];
                coordinates[1][1] = point[1];
                drawTriangle = new Path2D.Double();
                drawTriangle.moveTo(sPoint.getX(), sPoint.getY());
                drawTriangle.lineTo(sPoint2.getX(), sPoint2.getY());
                drawTriangle.lineTo(sPoint2.getX(), sPoint2.getY());
                drawTriangle.closePath();
                triClick++;
            }
        }
        return drawTriangle;
    }

    public Path2D.Double drawTriangle(double[] point, Point2D pointObj, Rectangle2D screenDataArea) {
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
            x = pointObj.getX();
            y = pointObj.getY();
            // make sure it doesn't overflow the bounds of the chart
            if (x < screenDataArea.getMinX()) {
                x = screenDataArea.getMinX();
            }
            if (y < screenDataArea.getMinY()) {
                y = screenDataArea.getMinY();
            }
            if (x > screenDataArea.getMaxX()) {
                x = screenDataArea.getMaxX();
            }
            if (y > screenDataArea.getMaxY()) {
                y = screenDataArea.getMaxY();
            }
            drawTriangle = new Path2D.Double();
            drawTriangle.moveTo(sPoint.getX(), sPoint.getY());
            drawTriangle.lineTo(sPoint2.getX(), sPoint2.getY());
            drawTriangle.lineTo(x, y);
            drawTriangle.closePath();
        }
        return drawTriangle;
    }
    public void placeShape(double[] point) {
        coordinates[2][0] = point[0];
        coordinates[2][1] = point[1];
        storeTriangle = new Path2D.Double();
        storeTriangle.moveTo(coordinates[0][0], coordinates[0][1]);
        storeTriangle.lineTo(coordinates[1][0], coordinates[1][1]);
        storeTriangle.lineTo(coordinates[2][0], coordinates[2][1]);
        storeTriangle.closePath();
        triangleAnnotation = new XYShapeAnnotation(storeTriangle,
                new BasicStroke(2), new Color(0, 0, 0, 0), color);
        plot.addAnnotation(triangleAnnotation);
        drawTriangle = null;
        triClick = 0;
    }
    
    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        return storeTriangle.contains(p);
    }

    @Override
    void move(double xOffset, double yOffset) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete() {
        plot.removeAnnotation(triangleAnnotation);
    }

}
