package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author Ben Theurich
 */
public class ResizeHandle {

    private double[] coords;
    private double[] size = {0.0, 0.0};
    private double[] offset = {0.0, 0.0};
    private XYPlot plot;
    private AnnotateChartPanel chartPanel;

    private Ellipse2D.Double ell;
    private XYShapeAnnotation handle = null;

    public ResizeHandle(XYPlot p, double[] point, AnnotateChartPanel cp) {
        plot = p;
        chartPanel = cp;
        coords = point;
        recalculate();
        draw();
    }

    public void recalculate() {
        ValueAxis domainAxis = plot.getDomainAxis();
        double plotDomain = domainAxis.getUpperBound() - domainAxis.getLowerBound();
        ValueAxis rangeAxis = plot.getRangeAxis();
        double plotRange = rangeAxis.getUpperBound() - rangeAxis.getLowerBound();

        Rectangle2D.Double screenDataArea = (Rectangle2D.Double) chartPanel.getScreenDataArea();
        double screenWidthPx = screenDataArea.getMaxX() - screenDataArea.getMinX();
        double screenHeightPx = screenDataArea.getMaxY() - screenDataArea.getMinY();

        double xSize = plotDomain * 10 / screenWidthPx;
        double ySize = plotRange * 10 / screenHeightPx;

        size[0] = plotDomain * 10 / screenWidthPx;
        size[1] = plotRange * 10 / screenHeightPx;
        offset[0] = size[0] / 2.0;
        offset[1] = size[1] / 2.0;
    }

    public void draw() {
        if (handle != null) {
            plot.removeAnnotation(handle);
        }
        ell = new Ellipse2D.Double(coords[0] - offset[0], coords[1] - offset[1], size[0], size[1]);
        handle = new XYShapeAnnotation(ell, new BasicStroke(0), new Color(0, 0, 0), new Color(0, 0, 0));
        plot.addAnnotation(handle);
    }

    public void remove() {
        if (handle != null) {
            plot.removeAnnotation(handle);
        }
    }

    public void changeCoords(double[] c) {
        coords = c;
        recalculate();
    }

    public boolean contains(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        return ell.contains(p);
    }
}
