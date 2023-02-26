package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.awt.Color;

import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import java.awt.BasicStroke;

import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleAnchor;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;

public class HVLineAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;
    public String type;

    ValueMarker drawMarker;
    ValueMarker traceMarker = null;
    ValueMarker storeMarker;
    ValueMarker deleteMarker;

    public HVLineAnnotation(XYPlot p, Color c, String t) {
        this.plot = p;
        this.color = c;
        this.type = t;
    }

    public void createLine(double[] point) {
        if (type == "horizontal") {
            drawMarker = new ValueMarker(point[1]);
            drawMarker.setLabelAnchor(RectangleAnchor.CENTER);
            drawMarker.setPaint(AppFrame.getAbsoluteColor());
            drawMarker.setStroke(new BasicStroke(2.0f));
            storeMarker = drawMarker;
            drawMarker = null;
            plot.addRangeMarker(storeMarker);
        } else if (type == "vertical") {
            drawMarker = new ValueMarker(point[0]);
            drawMarker.setLabelAnchor(RectangleAnchor.CENTER);
            drawMarker.setPaint(AppFrame.getAbsoluteColor());
            drawMarker.setStroke(new BasicStroke(2.0f));
            storeMarker = drawMarker;
            drawMarker = null;
            plot.addDomainMarker(storeMarker);
        }

    }

    public void drawTrace(double[] point) {
        if (type == "horizontal") {
            if (traceMarker != null) {
                plot.removeRangeMarker(traceMarker);
            }
            traceMarker = new ValueMarker(point[1]);
            traceMarker.setLabelAnchor(RectangleAnchor.CENTER);
            traceMarker.setPaint(AppFrame.getAbsoluteColor());
            traceMarker.setStroke(new BasicStroke(2.0f));
            plot.addRangeMarker(traceMarker);
        } else if (type == "vertical") {
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
        if (type == "horizontal") {
            if (traceMarker != null) {
                plot.removeRangeMarker(traceMarker);
            }
        } else if (type == "vertical") {
            if (traceMarker != null) {
                plot.removeDomainMarker(traceMarker);
            }
        }

    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        if (type == "horizontal" && mouseY >= storeMarker.getValue() - 3 && mouseY <= storeMarker.getValue() + 3) {
            return true;
        }
        if (type == "vertical" && mouseX >= storeMarker.getValue() - 3 && mouseX <= storeMarker.getValue() + 3) {
            return true;
        }

        return false;
    }

    @Override
    void move(double xOffset, double yOffset) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

    @Override
    public void delete() {
        if (type == "horizontal") {
            plot.removeRangeMarker(storeMarker);
        } else if (type == "vertical") {
            plot.removeDomainMarker(storeMarker);
        }
    }

}