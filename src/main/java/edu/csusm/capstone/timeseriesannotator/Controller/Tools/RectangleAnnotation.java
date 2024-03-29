package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author Ben Theurich
 * @author Cody McKinney
 */
public class RectangleAnnotation extends AbstractAnnotation {

    private Rectangle2D.Double storeRect = null;

    private double[][] coordinates = {{0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}};

    private double[][] handleCoordinates = {{0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}};
    private boolean dragHandle = false;
    private int handleNumber = 0;

    private double x, y, width, height;

    private XYShapeAnnotation rectAnnotation = null;

    public RectangleAnnotation(XYPlot p, Color c, double[] point, String t, AnnotateChartPanel cP) {
        this.plot = p;
        this.color = new Color(c.getRed(), c.getGreen(), c.getBlue(), fillAlpha);
        coordinates[0][0] = point[0];
        coordinates[0][1] = point[1];
        this.type = t;
        this.chartPanel = cP;
        if (type.equals("rectangle")) {
            this.handles = new ResizeHandle[]{null, null, null, null};
        } else if (type.equals("region")) {
            this.handles = new ResizeHandle[]{null, null};
        }
    }

    public RectangleAnnotation(XYPlot p, int[] c, double[][] coords, String[] t, AnnotateChartPanel cP) {
        this.chartPanel = cP;
        this.plot = p;
        String tempType = t[0];
        this.type = tempType.substring(1, tempType.length() - 1);
        if (type.equals("rectangle")) {
            this.handles = new ResizeHandle[]{null, null, null, null};
        } else if (type.equals("region")) {
            this.handles = new ResizeHandle[]{null, null};
        }
        this.color = new Color(c[0], c[1], c[2], c[3]);
        x = coords[0][0];
        y = coords[0][1];
        width = coords[0][2];
        height = coords[0][3];
        storeRect = new Rectangle2D.Double(x, y, width, height);
        rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(0),
                new Color(0, 0, 0, 0), color);
        plot.addAnnotation(rectAnnotation);
    }

    public void drawRect(double[] point) {
        if (rectAnnotation != null) {
            plot.removeAnnotation(rectAnnotation);
            chartPanel.removeAbstractAnnotation(this);
        }

        if (type.equals("rectangle")) {
            coordinates[1][0] = point[0];
            coordinates[1][1] = point[1];
            x = Math.min(coordinates[0][0], coordinates[1][0]);
            y = Math.min(coordinates[0][1], coordinates[1][1]);
            width = Math.abs(coordinates[1][0] - coordinates[0][0]);
            height = Math.abs(coordinates[1][1] - coordinates[0][1]);

        } else if (type.equals("region")) {
            double rectHeight = 8 * (chartPanel.minMax[3] - chartPanel.minMax[1]);
            coordinates[1][0] = point[0];
            x = Math.min(coordinates[0][0], coordinates[1][0]);
            y = chartPanel.minMax[3] - (rectHeight / 1.8);
            width = Math.abs(coordinates[1][0] - coordinates[0][0]);
            height = rectHeight;
        }
        storeRect = new Rectangle2D.Double(x, y, width, height);
        rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(0),
                new Color(0, 0, 0, 0), color);
        plot.addAnnotation(rectAnnotation);
        chartPanel.addAbstractAnnotation(this);
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        Point2D p = new Point2D.Double(mouseX, mouseY);
        boolean clicked = false;
        clicked = storeRect.contains(p);
        if (selected) {
            for (int i = 0; i < handles.length; i++) {
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
            plot.removeAnnotation(rectAnnotation);
            rectAnnotation = new XYShapeAnnotation(storeRect, dashed,
                    new Color(0, 0, 0), color);
            plot.addAnnotation(rectAnnotation);
            selected = true;
            updateHandleCoords();
            for (int i = 0; i < handles.length; i++) {
                handles[i] = new ResizeHandle(plot, handleCoordinates[i], chartPanel);
            }
        }
    }

    @Override
    public void deselect() {
        if (selected) {
            plot.removeAnnotation(rectAnnotation);
            for (int i = 0; i < handles.length; i++) {
                handles[i].remove();
            }
            rectAnnotation = new XYShapeAnnotation(storeRect, new BasicStroke(0),
                    new Color(0, 0, 0, 0), color);
            plot.addAnnotation(rectAnnotation);
            selected = false;
        }
    }

    @Override
    public void delete() {
        plot.removeAnnotation(rectAnnotation);
        storeRect = null;
        if (selected) {
            for (int i = 0; i < handles.length; i++) {
                handles[i].remove();
            }
        }
    }

    @Override
    public void move(double xOffset, double yOffset, boolean set) {
        plot.removeAnnotation(rectAnnotation);
        if (!set) {
            if (!dragHandle) {
                if (type.equals("rectangle")) {
                    storeRect.setFrame(x - xOffset, y - yOffset, width, height);
                } else if (type.equals("region")) {
                    storeRect.setFrame(x - xOffset, y, width, height);
                }
            } else {
                double[] draggedPoint = {coordinates[handleNumber][0] - xOffset, coordinates[handleNumber][1] - yOffset};
                int oppositePoint = (handleNumber + 2 + 4) % 4;
                if (type.equals("rectangle")) {
                    redrawRect(draggedPoint, coordinates[oppositePoint], false);
                } else if (type.equals("region")) {
                    redrawRegion(draggedPoint, coordinates[Math.abs(handleNumber - 1)], false);
                }
            }
        } else {
            if (!dragHandle) {
                if (type.equals("rectangle")) {
                    x -= xOffset;
                    y -= yOffset;
                } else if (type.equals("region")) {
                    x -= xOffset;
                }
                storeRect.setFrame(x, y, width, height);
            } else {
                double[] draggedPoint = {coordinates[handleNumber][0] - xOffset, coordinates[handleNumber][1] - yOffset};
                int oppositePoint = (handleNumber + 2 + 4) % 4;
                if (type.equals("rectangle")) {
                    redrawRect(draggedPoint, coordinates[oppositePoint], true);
                } else if (type.equals("region")) {
                    redrawRegion(draggedPoint, coordinates[Math.abs(handleNumber - 1)], true);
                }
            }
            dragHandle = false;
            updateCoords();
        }
        updateHandleCoords();
        rectAnnotation = new XYShapeAnnotation(storeRect, dashed,
                new Color(0, 0, 0), color);
        plot.addAnnotation(rectAnnotation);
        for (int i = 0; i < handles.length; i++) {
            handles[i].changeCoords(handleCoordinates[i]);
            handles[i].draw();
        }
    }

    public void redrawRect(double[] p1, double[] p2, boolean set) {
        double tempX = Math.min(p1[0], p2[0]);
        double tempY = Math.min(p1[1], p2[1]);
        double tempWidth = Math.abs(p2[0] - p1[0]);
        double tempHeight = Math.abs(p2[1] - p1[1]);
        storeRect.setFrame(tempX, tempY, tempWidth, tempHeight);
        if (set) {
            x = tempX;
            y = tempY;
            width = tempWidth;
            height = tempHeight;
        }
    }

    public void redrawRegion(double[] p1, double[] p2, boolean set) {
        double tempX = Math.min(p1[0], p2[0]);
        double tempWidth = Math.abs(p2[0] - p1[0]);
        storeRect.setFrame(tempX, y, tempWidth, height);
        if (set) {
            x = tempX;
            width = tempWidth;
        }
    }

    public void newBounds() {
        if (type.equals("region")) {
            plot.removeAnnotation(rectAnnotation);
            double rectHeight = 8 * (chartPanel.minMax[3] - chartPanel.minMax[1]);
            x = Math.min(coordinates[0][0], coordinates[1][0]);
            y = chartPanel.minMax[3] - (rectHeight / 1.8);
            width = Math.abs(coordinates[1][0] - coordinates[0][0]);
            height = rectHeight;

            storeRect.setFrame(x, y, width, height);
            rectAnnotation = new XYShapeAnnotation(storeRect, dashed,
                    new Color(0, 0, 0), color);
            plot.addAnnotation(rectAnnotation);
            if (selected) {
                deselect();
                select();
                scale();
            }
        }
    }

    private void updateCoords() {
        coordinates[0][0] = storeRect.getX();
        coordinates[0][1] = storeRect.getY() + storeRect.getHeight();

        coordinates[1][0] = storeRect.getX() + storeRect.getWidth();
        coordinates[1][1] = storeRect.getY() + storeRect.getHeight();

        coordinates[2][0] = storeRect.getX() + storeRect.getWidth();
        coordinates[2][1] = storeRect.getY();

        coordinates[3][0] = storeRect.getX();
        coordinates[3][1] = storeRect.getY();
    }

    private void updateHandleCoords() {
        if (type.equals("rectangle")) {
            handleCoordinates[0][0] = storeRect.getX();
            handleCoordinates[0][1] = storeRect.getY() + storeRect.getHeight();

            handleCoordinates[1][0] = storeRect.getX() + storeRect.getWidth();
            handleCoordinates[1][1] = storeRect.getY() + storeRect.getHeight();

            handleCoordinates[2][0] = storeRect.getX() + storeRect.getWidth();
            handleCoordinates[2][1] = storeRect.getY();

            handleCoordinates[3][0] = storeRect.getX();
            handleCoordinates[3][1] = storeRect.getY();
        } else if (type.equals("region")) {
            handleCoordinates[0][0] = storeRect.getX();
            handleCoordinates[0][1] = storeRect.getY() + storeRect.getHeight() / 2;

            handleCoordinates[1][0] = storeRect.getX() + storeRect.getWidth();
            handleCoordinates[1][1] = storeRect.getY() + storeRect.getHeight() / 2;
        }
    }

    @Override
    public void changeColor(Color c) {
        color = new Color(c.getRed(), c.getGreen(), c.getBlue(), fillAlpha);
        plot.removeAnnotation(rectAnnotation);
        rectAnnotation = new XYShapeAnnotation(storeRect, dashed,
                new Color(0, 0, 0), color);
        plot.addAnnotation(rectAnnotation);
    }

    @Override
    public String getType() {
        return "rectangle";
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
        return "[\"" + type + "\"]";
    }
}
