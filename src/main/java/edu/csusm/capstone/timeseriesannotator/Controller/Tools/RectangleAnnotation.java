package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import java.util.Enumeration;
import java.util.Hashtable;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;

public class RectangleAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;

    private Rectangle2D.Double rect = null;
    private Hashtable<Rectangle2D, XYShapeAnnotation> rectangleDict = new Hashtable<Rectangle2D, XYShapeAnnotation>();

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
        rect = new Rectangle2D.Double(sPoint.getX(), sPoint.getY(), 0, 0);

        return rect;
    }

    public Rectangle2D.Double drawArea(Point2D p, Rectangle2D screenDataArea) {
        if (rect != null) {
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
            rect.setRect(x, y, width, height);

            return rect;
        }
        return null;
    }

    public XYShapeAnnotation placeShape(double[][] coordinates) {
        x = Math.min(coordinates[0][0], coordinates[1][0]);
        y = Math.min(coordinates[0][1], coordinates[1][1]);
        width = Math.abs(coordinates[1][0] - coordinates[0][0]);
        height = Math.abs(coordinates[1][1] - coordinates[0][1]);
        rect.setFrame(x, y, width, height);
        rectAnnotation = new XYShapeAnnotation(rect, new BasicStroke(2),
                new Color(0, 0, 0, 0), color);
        return rectAnnotation;

    }

    public XYShapeAnnotation getShapeAnnotation() {
        return rectAnnotation;
    }

    public Rectangle2D.Double getShape() {
        return rect;
    }

    public void updateHashtable(Hashtable<Rectangle2D, XYShapeAnnotation> r) {
        this.rectangleDict = r;
        rect = null;
    }

    private Rectangle2D removeRect(double[] point) {
        Enumeration<Rectangle2D> e = rectangleDict.keys();

        Point2D p = new Point2D.Double(point[0], point[1]);

        while (e.hasMoreElements()) {
            Rectangle2D rec = e.nextElement();

            if (rec.contains(p)) {
                return rec;
            }
        }
        return null;
    }

    @Override
    boolean clickedOn(double mouseX, double mouseY) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clickedOn'");
    }

    @Override
    boolean clickedOn(Point2D point) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clickedOn'");
    }

    @Override
    public void delete(double[] point) {
        Rectangle2D rec = removeRect(point);
        if (rec != null) {
            XYShapeAnnotation sh = rectangleDict.get(rec);
            rectangleDict.remove(rec, sh);
            plot.removeAnnotation(sh);
        }
    }

    @Override
    void removeFromPlot(XYPlot plot) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFromPlot'");
    }

    @Override
    void move(double xOffset, double yOffset) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

}