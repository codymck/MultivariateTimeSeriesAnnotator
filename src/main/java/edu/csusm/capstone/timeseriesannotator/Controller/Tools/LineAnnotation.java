package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.abs;
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
    private double[][] handleCoordinates = {{0.0, 0.0}, {0.0, 0.0}};
    private boolean dragHandle = false;
    private int handleNumber = 0;
    private double[] startPoint = {0.0, 0.0};

    private XYShapeAnnotation lineAnnotation = null;

    public LineAnnotation(XYPlot p, Color c, double[] point, String t, AnnotateChartPanel cP) {
        this.handles = new ResizeHandle[]{null, null};
        this.plot = p;
        this.color = c;
        this.type = t;
        this.chartPanel = cP;
        coordinates[0] = point;
        startPoint[0] = point[0];
        startPoint[1] = point[1];
    }

    public LineAnnotation(XYPlot p, int[] c, double[][] coords, String[] t, AnnotateChartPanel cP) {
        this.chartPanel = cP;
        this.handles = new ResizeHandle[]{null, null};
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

        double xSize = plotDomain * 10 / screenWidthPx;
        double ySize = plotRange * 10 / screenHeightPx;

        double xOffset = xSize / 2.0;
        double yOffset = ySize / 2.0;
        intersectRect = new Rectangle2D.Double(mouseX - xOffset, mouseY - yOffset, xSize, ySize);
        //XYShapeAnnotation hitbox = new XYShapeAnnotation(intersectRect, new BasicStroke(0), color, color);
        //plot.addAnnotation(hitbox);

        Point2D p = new Point2D.Double(mouseX, mouseY);
        boolean clicked = storeLine.intersects(intersectRect);
        if (selected) {
            for (int i = 0; i < 2; i++) {
                if (handles[i].contains(mouseX, mouseY)) {
                    dragHandle = true;
                    handleNumber = i;
                    return true;
                }
            }
        }
        return clicked;
    }

    @Override
    public void select() {
        if (!selected) {
            plot.removeAnnotation(lineAnnotation);
            lineAnnotation = new XYShapeAnnotation(storeLine, dashed, color);
            plot.addAnnotation(lineAnnotation);
            selected = true;
            updateHandleCoords();
            for (int i = 0; i < 2; i++) {
                handles[i] = new ResizeHandle(plot, handleCoordinates[i], chartPanel);
            }
        }
    }

    @Override
    public void deselect() {
        if (selected) {
            plot.removeAnnotation(lineAnnotation);
            for (int i = 0; i < 2; i++) {
                handles[i].remove();
            }
            lineAnnotation = new XYShapeAnnotation(storeLine, new BasicStroke(2), color);
            plot.addAnnotation(lineAnnotation);
            selected = false;
        }
    }

    @Override
    public void delete() {
        if (lineAnnotation != null) {
            plot.removeAnnotation(lineAnnotation);
            if (selected) {
                for (int i = 0; i < 2; i++) {
                    handles[i].remove();
                }
            }
        }
    }

    @Override
    public void move(double xOffset, double yOffset, boolean set) {
        plot.removeAnnotation(lineAnnotation);
        if (!set) {
            if (!dragHandle) {
                storeLine.setLine(coordinates[0][0] - xOffset, coordinates[0][1] - yOffset, coordinates[1][0] - xOffset, coordinates[1][1] - yOffset);
                lineAnnotation = new XYShapeAnnotation(storeLine, dashed, color);
                plot.addAnnotation(lineAnnotation);
                updateHandleCoords();
                redrawHandles();
            } else {
                if (type.equals("segment")) {
                    if (handleNumber == 0) {
                        storeLine.setLine(coordinates[0][0] - xOffset, coordinates[0][1] - yOffset, coordinates[1][0], coordinates[1][1]);
                    }
                    if (handleNumber == 1) {
                        storeLine.setLine(coordinates[0][0], coordinates[0][1], coordinates[1][0] - xOffset, coordinates[1][1] - yOffset);
                    }
                    lineAnnotation = new XYShapeAnnotation(storeLine, dashed, color);
                    plot.addAnnotation(lineAnnotation);
                    updateHandleCoords();
                    redrawHandles();
                } else {
                    int oppHandle = abs(handleNumber - 1);
                    double[] newPoint = {handleCoordinates[handleNumber][0] - xOffset, handleCoordinates[handleNumber][1] - yOffset};
                    rotateLine(handleCoordinates[oppHandle], newPoint);
                    lineAnnotation = new XYShapeAnnotation(storeLine, dashed, color);
                    plot.addAnnotation(lineAnnotation);
                    handles[handleNumber].changeCoords(newPoint);
                    for (int i = 0; i < 2; i++) {
                        handles[i].draw();
                    }
                }
            }
        } else {
            if (!dragHandle) {
                coordinates[0][0] -= xOffset;
                coordinates[0][1] -= yOffset;
                coordinates[1][0] -= xOffset;
                coordinates[1][1] -= yOffset;
            } else {
                if (type.equals("segment")) {
                    coordinates[handleNumber][0] -= xOffset;
                    coordinates[handleNumber][1] -= yOffset;
                } else {
                    coordinates[0][0] = storeLine.getX1();
                    coordinates[0][1] = storeLine.getY1();
                    coordinates[1][0] = storeLine.getX2();
                    coordinates[1][1] = storeLine.getY2();
                }
            }
            lineAnnotation = new XYShapeAnnotation(storeLine, dashed, color);
            plot.addAnnotation(lineAnnotation);
            updateHandleCoords();
            redrawHandles();
            dragHandle = false;
        }
    }

    private void updateHandleCoords() {
        if (type.equals("segment")) {
            handleCoordinates[0][0] = storeLine.getX1();
            handleCoordinates[0][1] = storeLine.getY1();
            handleCoordinates[1][0] = storeLine.getX2();
            handleCoordinates[1][1] = storeLine.getY2();
        } else {
            // Get the x and y axis ranges of the chart
            ValueAxis domainAxis = plot.getDomainAxis();
            ValueAxis rangeAxis = plot.getRangeAxis();
            double xmin = domainAxis.getLowerBound();
            double xmax = domainAxis.getUpperBound();
            double ymin = rangeAxis.getLowerBound();
            double ymax = rangeAxis.getUpperBound();

            // Calculate the slope of the line
            double deltaX = storeLine.getX2() - storeLine.getX1();
            double deltaY = storeLine.getY2() - storeLine.getY1();
            double slope = deltaY / deltaX;

            // Calculate the y-intercept of the line using either endpoint of the line
            double yIntercept = storeLine.getY1() - slope * storeLine.getX1();

            // Calculate the x-coordinates of the two points where the line intersects the left and right edges of the screen
            double xLeft = (ymin - yIntercept) / slope;
            double xRight = (ymax - yIntercept) / slope;

            // Calculate the y-coordinates of the two points where the line intersects the top and bottom edges of the screen
            double yTop = ymin;
            double yBottom = ymax;

            // Check if the line is vertical
            if (Double.isInfinite(slope)) {
                xLeft = storeLine.getX1();
                xRight = storeLine.getX1();

                // Check if the line is above the screen or below the screen
                if (storeLine.getY1() < ymin) {
                    yTop = ymin;
                    yBottom = ymin;
                } else if (storeLine.getY1() > ymax) {
                    yTop = ymax;
                    yBottom = ymax;
                } else {
                    // Line is within the screen, so we don't need to adjust the y coordinates
                    yTop = storeLine.getY1();
                    yBottom = storeLine.getY2();
                }
            } else {
                // Check if the line is outside the left or right bounds of the screen
                if (xLeft < xmin) {
                    // Adjust the x-coordinate and y-coordinate of the left intersection point
                    xLeft = xmin;
                    yTop = slope * xmin + yIntercept;
                } else if (xLeft > xmax) {
                    // Adjust the x-coordinate and y-coordinate of the left intersection point
                    xLeft = xmax;
                    yTop = slope * xmax + yIntercept;
                }
                if (xRight < xmin) {
                    // Adjust the x-coordinate and y-coordinate of the right intersection point
                    xRight = xmin;
                    yBottom = slope * xmin + yIntercept;
                } else if (xRight > xmax) {
                    // Adjust the x-coordinate and y-coordinate of the right intersection point
                    xRight = xmax;
                    yBottom = slope * xmax + yIntercept;
                }
            }

            // Calculate the x and y coordinates of the two points where the circles will be drawn
            if (type.equals("diagonal")) {
                handleCoordinates[0][0] = xLeft + 0.25 * (xRight - xLeft);
                handleCoordinates[0][1] = yTop + 0.25 * (yBottom - yTop);
                handleCoordinates[1][0] = xLeft + 0.75 * (xRight - xLeft);
                handleCoordinates[1][1] = yTop + 0.75 * (yBottom - yTop);
            } else if (type.equals("ray")) {
                handleCoordinates[0][0] = storeLine.getX1();
                handleCoordinates[0][1] = storeLine.getY1();
                if (storeLine.getY1() > storeLine.getY2()) {
                    handleCoordinates[1][0] = xLeft + 0.5 * (storeLine.getX1() - xLeft);
                    handleCoordinates[1][1] = yTop + 0.5 * (storeLine.getY1() - yTop);
                } else {
                    handleCoordinates[1][0] = xRight + 0.5 * (storeLine.getX1() - xRight);
                    handleCoordinates[1][1] = yBottom + 0.5 * (storeLine.getY1() - yBottom);
                }
            }
        }
    }

    public void rotateLine(double[] axis, double[] move) {
        double dx, dy, angle, length;
        dx = axis[0] - move[0]; // change in x
        dy = axis[1] - move[1]; // change in y
        angle = Math.atan2(dy, dx); // angle of line
        length = Math.max(chartPanel.minMax[2] - chartPanel.minMax[0], chartPanel.minMax[3] - chartPanel.minMax[1]) * 10; // length of line
        if (type.equals("ray")) {
            if (handleNumber == 0) {
                coordinates[0][0] = move[0];
                coordinates[0][1] = move[1];
                coordinates[1][0] = axis[0] + length * Math.cos(angle);
                coordinates[1][1] = axis[1] + length * Math.sin(angle);
            } else {
                coordinates[1][0] = move[0] - length * Math.cos(angle);
                coordinates[1][1] = move[1] - length * Math.sin(angle);
            }
        } else if (type.equals("diagonal")) {
            coordinates[1][0] = axis[0] + length * Math.cos(angle);
            coordinates[1][1] = axis[1] + length * Math.sin(angle);
            coordinates[0][0] = move[0] - length * Math.cos(angle);
            coordinates[0][1] = move[1] - length * Math.sin(angle);
        }

        storeLine.setLine(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
    }

    public void redrawHandles() {
        for (int i = 0; i < 2; i++) {
            handles[i].changeCoords(handleCoordinates[i]);
            handles[i].draw();
        }
    }

    @Override
    public void changeColor(Color c) {
        color = c;
        plot.removeAnnotation(lineAnnotation);
        lineAnnotation = new XYShapeAnnotation(storeLine, dashed, color);
        plot.addAnnotation(lineAnnotation);
        redrawHandles();
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
