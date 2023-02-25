package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;

public class RectangleAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;

    private Rectangle2D.Double drawRect = null;
    private Rectangle2D.Double storeRect = null;

    private Point2D sPoint;
    private Point2D ePoint;

    private double x;
    private double y;

    private double width;
    private double height;

    private XYShapeAnnotation rectAnnotation;

    public RectangleAnnotation(XYPlot p, Color c) {
        this.plot = p;
        this.color = c;
    }

    public RectangleAnnotation(XYPlot p, Color c, double x0, double y0, double width, double height, Stroke s,
            Paint outlineP, Paint fillP) {

    }

    public Rectangle2D.Double createShape(Point2D p) {
        this.sPoint = p;
        drawRect = new Rectangle2D.Double(sPoint.getX(), sPoint.getY(), 0, 0);
        return drawRect;
    }

    public Rectangle2D.Double drawRect(Point2D p, Rectangle2D screenDataArea) {
        if (drawRect != null) {
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
            drawRect.setRect(x, y, width, height);

            return drawRect;
        }
        return null;
    }

    public void placeShape(double[][] coordinates) {
        x = Math.min(coordinates[0][0], coordinates[1][0]);
        y = Math.min(coordinates[0][1], coordinates[1][1]);
        width = Math.abs(coordinates[1][0] - coordinates[0][0]);
        height = Math.abs(coordinates[1][1] - coordinates[0][1]);
        storeRect = new Rectangle2D.Double(x, y, width, height);
        rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(2),
                new Color(0, 0, 0, 0), color);
        drawRect = null;
        plot.addAnnotation(rectAnnotation);
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        return storeRect.contains(p);
    }

    @Override
    public void delete() {
        plot.removeAnnotation(rectAnnotation);
    }

    @Override
    void move(double xOffset, double yOffset) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

}