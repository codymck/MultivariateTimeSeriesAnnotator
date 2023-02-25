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

    private Point2D sPoint;
    private Point2D sPoint2;
    private Point2D ePoint;

    private double x;
    private double y;

    private double width;
    private double height;

    private XYShapeAnnotation triangleAnnotation;

    private int triClick = 0;
    private XYLineAnnotation fLine;

    public TriangleAnnotation(XYPlot p, Color c) {
        this.plot = p;
        this.color = c;
    }

    public void createShape(Point2D p, double[] point) {
        switch (triClick) {
            case 0 -> {
                this.sPoint = p;
                fLine = new XYLineAnnotation(
                        point[0],
                        point[1],
                        point[0],
                        point[1],
                        new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
                plot.addAnnotation(fLine);
                triClick++;
            }
            case 1 -> {
                this.sPoint2 = p;
                plot.removeAnnotation(fLine);
                drawTriangle = new Path2D.Double();
                drawTriangle.moveTo(sPoint.getX(), sPoint.getY());
                drawTriangle.lineTo(sPoint2.getX(), sPoint2.getY());
                drawTriangle.lineTo(sPoint2.getX(), sPoint2.getY());
                drawTriangle.closePath();
                triClick++;
            }
            case 2 -> {
                this.ePoint = p;
                drawTriangle = new Path2D.Double();
                drawTriangle.moveTo(sPoint.getX(), sPoint.getY());
                drawTriangle.lineTo(sPoint2.getX(), sPoint2.getY());
                drawTriangle.lineTo(ePoint.getX(), ePoint.getY());
                drawTriangle.closePath();
                XYShapeAnnotation triangleA = new XYShapeAnnotation(drawTriangle,
                        new BasicStroke(2), new Color(0, 0, 0, 0), color);
                drawTriangle = null;
                plot.addAnnotation(triangleA);
                triClick = 0;
            }
        }
    }

    public Path2D.Double drawTriangle(Point2D p, Rectangle2D screenDataArea) {
        if (triClick == 1) {
            sPoint2 = p;
            plot.removeAnnotation(fLine);
            fLine = new XYLineAnnotation(
                    sPoint.getX(),
                    sPoint.getY(),
                    sPoint2.getX(),
                    sPoint2.getY(),
                    new BasicStroke(2.0f), color);
            plot.addAnnotation(fLine);
        } else if (triClick == 2) {
            x = p.getX();
            y = p.getY();
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
            return drawTriangle;
        }
        return null;
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void delete(Point2D point) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    void move(double xOffset, double yOffset) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
