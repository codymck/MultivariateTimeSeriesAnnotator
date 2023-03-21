package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

public class LineAnnotation extends AbstractAnnotation {

    public boolean selected;
    public Color color;
    public XYPlot plot;
    private String type;
    private AnnotateChartPanel chartPanel;

    private Line2D.Double storeLine = null;
    private Rectangle2D.Double intersectRect;

    private double[] minMax = {0.0, 0.0, 0.0, 0.0}; // minX, minY, maxX, maxY

    private double[][] coordinates = {{0.0, 0.0}, {0.0, 0.0}};
    private double[] startPoint = {0.0, 0.0};

    private XYShapeAnnotation lineAnnotation = null;

    public LineAnnotation(XYPlot p, Color c, double[] point, String t, double[] m, AnnotateChartPanel cP) {
        this.plot = p;
        this.color = c;
        this.type = t;
        this.chartPanel = cP;
        coordinates[0] = point;
        startPoint[0] = point[0];
        startPoint[1] = point[1];
        this.minMax = m;
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
                length = Math.max(minMax[2] - minMax[0], minMax[3] - minMax[1]) * 3; // length of line
                coordinates[1][0] = startPoint[0] - length * Math.cos(angle);
                coordinates[1][1] = startPoint[1] - length * Math.sin(angle);
            }
            case "diagonal" -> {
                dx = startPoint[0] - point[0]; // change in x
                dy = startPoint[1] - point[1]; // change in y
                angle = Math.atan2(dy, dx); // angle of line
                length = Math.max(minMax[2] - minMax[0], minMax[3] - minMax[1]) * 3; // length of line
                coordinates[0][0] = startPoint[0] - length * Math.cos(angle);
                coordinates[0][1] = startPoint[1] - length * Math.sin(angle);

                coordinates[1][0] = point[0] + length * Math.cos(angle);
                coordinates[1][1] = point[1] + length * Math.sin(angle);
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
        double domainMin = domainAxis.getLowerBound();
        double domainMax = domainAxis.getUpperBound();
        ValueAxis rangeAxis = plot.getRangeAxis();
        double rangeMin = rangeAxis.getLowerBound();
        double rangeMax = rangeAxis.getUpperBound();
        double xSize = (domainMax - domainMin) / 50.0;
        double ySize = (rangeMax - rangeMin) / 50.0;
        double xOffset = xSize / 2.0;
        double yOffset = ySize / 2.0;
        intersectRect = new Rectangle2D.Double(mouseX - xOffset, mouseY - yOffset, xSize, ySize);
        //XYShapeAnnotation hitbox = new XYShapeAnnotation(intersectRect, new BasicStroke(0), color, color);
        //plot.addAnnotation(hitbox);
        boolean r = storeLine.intersects(intersectRect);
        return r;
    }

    @Override
    public void delete() {
        if (lineAnnotation != null) {
            plot.removeAnnotation(lineAnnotation);
        }
    }

    @Override
    public void move(double newX, double newY, boolean set) {

    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public String getType() {
        return "line";
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
