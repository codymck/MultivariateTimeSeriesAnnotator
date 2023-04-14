package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.Color;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import java.awt.BasicStroke;
import org.jfree.chart.ui.RectangleAnchor;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.ValueAxis;

public class HVLineAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;
    public String type;
    private double[] coordinate = {0.0, 0.0};

    private ValueMarker drawMarker;
    private ValueMarker traceMarker = null;
    private Line2D.Double storeLine = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
    private Rectangle2D.Double intersectRect;
    private AnnotateChartPanel chartPanel;
    
    private Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                  0, new float[]{9}, 0);
    
    private double[] minMax = { 0.0, 0.0, 0.0, 0.0 }; // minX, minY, maxX, maxY
    
    public HVLineAnnotation(XYPlot p, Color c, String t, double[] m, AnnotateChartPanel cP) {
        this.plot = p;
        this.color = c;
        this.type = t;
        this.minMax = m;
        this.chartPanel = cP;
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
            coordinate[1] = point[1];
            drawMarker = new ValueMarker(point[1]);
            drawMarker.setLabelAnchor(RectangleAnchor.CENTER);
            drawMarker.setPaint(color);
            drawMarker.setStroke(new BasicStroke(2.0f));
            plot.addRangeMarker(drawMarker);
        } else if (type.equals("vertical")) {
            coordinate[0] = point[0];
            drawMarker = new ValueMarker(point[0]);
            drawMarker.setLabelAnchor(RectangleAnchor.CENTER);
            drawMarker.setPaint(color);
            drawMarker.setStroke(new BasicStroke(2.0f));
            plot.addDomainMarker(drawMarker);
        }
        calculateLine();
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
            traceMarker.setStroke(new BasicStroke(2));
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
        double plotDomain = domainAxis.getUpperBound() - domainAxis.getLowerBound();
        ValueAxis rangeAxis = plot.getRangeAxis();
        double plotRange = rangeAxis.getUpperBound() - rangeAxis.getLowerBound();
       
        
        Rectangle2D.Double screenDataArea = (Rectangle2D.Double) chartPanel.getScreenDataArea();
        double screenWidthPx = screenDataArea.getMaxX() - screenDataArea.getMinX();
        double screenHeightPx = screenDataArea.getMaxY() - screenDataArea.getMinY();

        double xSize = plotDomain*10 / screenWidthPx;
        double ySize = plotRange*10 / screenHeightPx;

        double xOffset = xSize / 2.0;
        double yOffset = ySize / 2.0;
        intersectRect = new Rectangle2D.Double(mouseX-xOffset, mouseY-yOffset, xSize, ySize);
        //XYShapeAnnotation hitbox = new XYShapeAnnotation(intersectRect, new BasicStroke(0), color, color);
        //plot.addAnnotation(hitbox);
        boolean r = storeLine.intersects(intersectRect);
        return r;
    }
    
    @Override
    public void select(){
        if(!selected){
            if (type.equals("horizontal")) {
                plot.removeRangeMarker(drawMarker);
                drawMarker.setStroke(dashed);
                plot.addRangeMarker(drawMarker);
            } else if (type.equals("vertical")) {
                plot.removeDomainMarker(drawMarker);
                drawMarker.setStroke(dashed);
                plot.addDomainMarker(drawMarker);
            }
            selected = true;
        }
    }
    
    @Override
    public void deselect(){
        if(selected){
            drawMarker.setStroke(new BasicStroke(2));
            selected = false;
        }
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
    public void move(double xOffset, double yOffset, boolean set) {
        double[] tempLocation = {0.0, 0.0};
        if(!set){
            tempLocation[0] = coordinate[0] - xOffset;
            tempLocation[1] = coordinate[1] - yOffset;
        }else{
            tempLocation[0] = coordinate[0] - xOffset;
            tempLocation[1] = coordinate[1] - yOffset;
            coordinate[0] = tempLocation[0];
            coordinate[1] = tempLocation[1];
            calculateLine();
        }
        if (type.equals("horizontal")) {
            plot.removeRangeMarker(drawMarker);
            drawMarker.setValue(tempLocation[1]);
            plot.addRangeMarker(drawMarker);
        } else if (type.equals("vertical")) {
            plot.removeDomainMarker(drawMarker);
            drawMarker.setValue(tempLocation[0]);
            plot.addDomainMarker(drawMarker);
        }

    }
    
    private void calculateLine(){
        if (type.equals("horizontal")) {
            double lengthX = minMax[2] - minMax[0];
            storeLine = new Line2D.Double(minMax[0] + lengthX*3, coordinate[1], minMax[2] - lengthX*3, coordinate[1]);
        } else if (type.equals("vertical")) {
            double lengthY = minMax[3] - minMax[1];
            storeLine = new Line2D.Double(coordinate[0], minMax[1] - lengthY*3, coordinate[0], minMax[3] + lengthY*3);
        }
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