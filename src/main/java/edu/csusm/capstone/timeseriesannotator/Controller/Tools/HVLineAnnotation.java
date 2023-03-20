package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import java.awt.Color;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import java.awt.BasicStroke;
import org.jfree.chart.ui.RectangleAnchor;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.ValueAxis;

public class HVLineAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;
    public String type;
    private double coordinate;

    private ValueMarker drawMarker;
    private ValueMarker traceMarker = null;
    private Line2D.Double storeLine = null;
    private Rectangle2D.Double intersectRect;
    
    private double[] minMax = { 0.0, 0.0, 0.0, 0.0 }; // minX, minY, maxX, maxY
    
    public HVLineAnnotation(XYPlot p, Color c, String t, double[] m) {
        this.plot = p;
        this.color = c;
        this.type = t;
        this.minMax = m;
    }
    public HVLineAnnotation(XYPlot p, int[] c, String[] t, double[] m, double[] point) {
        this.plot = p;
        this.color = new Color(c[0], c[1], c[2], c[3]);
        String tempType = t[0];
        this.type = tempType.substring(1, tempType.length() - 1);
        this.minMax = m;
        this.createLine(point);
    }

    public void createLine(double[] point) {
        if (type.equals("horizontal")) {
            coordinate = point[1];
            drawMarker = new ValueMarker(point[1]);
            drawMarker.setLabelAnchor(RectangleAnchor.CENTER);
            drawMarker.setPaint(color);
            drawMarker.setStroke(new BasicStroke(2.0f));
            plot.addRangeMarker(drawMarker);
            double lengthX = minMax[2] - minMax[0];
            storeLine = new Line2D.Double(minMax[0] + lengthX*3, point[1], minMax[2] - lengthX*3, point[1]);
        } else if (type.equals("vertical")) {
            coordinate = point[0];
            drawMarker = new ValueMarker(point[0]);
            drawMarker.setLabelAnchor(RectangleAnchor.CENTER);
            drawMarker.setPaint(color);
            drawMarker.setStroke(new BasicStroke(2.0f));
            plot.addDomainMarker(drawMarker);
            double lengthY = minMax[3] - minMax[1];
            storeLine = new Line2D.Double(point[0], minMax[1] - lengthY*3, point[0], minMax[3] + lengthY*3);
        }
    }

    public void drawTrace(double[] point) {
        if (type.equals("horizontal")) {
            if (traceMarker != null) {
                plot.removeRangeMarker(traceMarker);
            }
            traceMarker = new ValueMarker(point[1]);
            traceMarker.setLabelAnchor(RectangleAnchor.CENTER);
            traceMarker.setPaint(AppFrame.getAbsoluteColor());
            traceMarker.setStroke(new BasicStroke(2.0f));
            plot.addRangeMarker(traceMarker);
        } else if (type.equals("vertical")) {
            if (traceMarker != null) {
                plot.removeDomainMarker(traceMarker);
            }
            traceMarker = new ValueMarker(point[0]);
            traceMarker.setLabelAnchor(RectangleAnchor.CENTER);
            traceMarker.setPaint(AppFrame.getAbsoluteColor());
            traceMarker.setStroke(new BasicStroke(2.0f));
            plot.addDomainMarker(traceMarker);
        }
    }

    public void removeTrace() {
        if (type.equals("horizontal")) {
            if (traceMarker != null) {
                plot.removeRangeMarker(traceMarker);
            }
        } else if (type.equals("vertical")) {
            if (traceMarker != null) {
                plot.removeDomainMarker(traceMarker);
            }
        }
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
        intersectRect = new Rectangle2D.Double(mouseX-xOffset, mouseY-yOffset, xSize, ySize);
        //XYShapeAnnotation hitbox = new XYShapeAnnotation(intersectRect, new BasicStroke(0), color, color);
        //plot.addAnnotation(hitbox);
        boolean r = storeLine.intersects(intersectRect);
        return r;
    }

    @Override
    public void delete() {
        if (type.equals("horizontal")) {
            plot.removeRangeMarker(drawMarker);
        } else if (type.equals("vertical")) {
            plot.removeDomainMarker(drawMarker);
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
    public String getType(){
        return "hvline";
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
        String COORD = String.valueOf(coordinate);

        if(type.equals("horizontal")){
            return "[[ 0, " + COORD + "]]";
        }else{
            return "[[" + COORD + ", 0]]";
        }
        
    }
    
    @Override
    public String getData() {
        return "[\"" + type + "\"]";
    }
}