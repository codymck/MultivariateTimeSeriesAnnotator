package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.BasicStroke;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;

public class EllipseAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;

    private Ellipse2D.Double drawEllipse = null;
    private Ellipse2D.Double storeEllipse = null;

    private double[][] coordinates = { { 0.0, 0.0 }, { 0.0, 0.0 } };
    
    private Point2D sPoint;
    private Point2D ePoint;

    private double x;
    private double y;

    private double width;
    private double height;

    private XYShapeAnnotation ellipseAnnotation;

    public EllipseAnnotation(XYPlot p, Color c) {
        this.plot = p;
        this.color = c;
    }

    public Ellipse2D.Double createShape(double[] point, Point2D pointObj) {
        this.sPoint = pointObj;
        coordinates[0][0] = point[0];
        coordinates[0][1] = point[1];
        drawEllipse = new Ellipse2D.Double(sPoint.getX(), sPoint.getY(), 0, 0);
        return drawEllipse;
    }

    public Ellipse2D.Double drawEllipse(Point2D p, Rectangle2D screenDataArea) {
        if (drawEllipse != null) {
            this.ePoint = p;
            x = Math.min(sPoint.getX(), ePoint.getX());
            y = Math.min(sPoint.getY(), ePoint.getY());
            width = Math.abs(sPoint.getX() - ePoint.getX());
            height = Math.abs(sPoint.getY() - ePoint.getY());

            // make sure it doesn't overflow the bounds of the chart
            if (x < screenDataArea.getMinX()) {
                width -= screenDataArea.getMinX() - x;
                x = screenDataArea.getMinX();
            }
            if (y < screenDataArea.getMinY()) {
                height -= screenDataArea.getMinY() - y;
                y = screenDataArea.getMinY();
            }
            if (x + width > screenDataArea.getMaxX()) {
                width = (screenDataArea.getMaxX() - x);
            }
            if (y + height > screenDataArea.getMaxY()) {
                height = (screenDataArea.getMaxY() - y);
            }
            drawEllipse.setFrame(x, y, width, height);

            return drawEllipse;
        }
        return null;
    }

    public void placeShape(double[] point) {
        coordinates[1][0] = point[0];
        coordinates[1][1] = point[1];
        x = Math.min(coordinates[0][0], coordinates[1][0]);
        y = Math.min(coordinates[0][1], coordinates[1][1]);
        width = Math.abs(coordinates[1][0] - coordinates[0][0]);
        height = Math.abs(coordinates[1][1] - coordinates[0][1]);
        storeEllipse = new Ellipse2D.Double(x, y, width, height);
        ellipseAnnotation = new XYShapeAnnotation(storeEllipse, new BasicStroke(2),
                new Color(0, 0, 0, 0), color);
        drawEllipse = null;
        plot.addAnnotation(ellipseAnnotation);
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        return storeEllipse.contains(p);
    }

    @Override
    void move(double xOffset, double yOffset) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

    @Override
    public void delete() {
        plot.removeAnnotation(ellipseAnnotation);
    }
}