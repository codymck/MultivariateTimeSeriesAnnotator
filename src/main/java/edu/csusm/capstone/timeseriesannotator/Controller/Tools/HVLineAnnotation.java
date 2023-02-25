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

    ValueMarker drawMarker;
    ValueMarker storeMarker;

    public HVLineAnnotation(XYPlot p, Color c) {
        this.plot = p;
        this.color = c;
    }

    public void createHorizontalLine(double[] point) {
        drawMarker = new ValueMarker(point[1]);
        drawMarker.setLabelAnchor(RectangleAnchor.CENTER);
        drawMarker.setPaint(AppFrame.getAbsoluteColor());
        drawMarker.setStroke(new BasicStroke(2.0f));
        storeMarker = drawMarker;
        drawMarker = null;
        plot.addRangeMarker(storeMarker);
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        Collection<ValueMarker> markers = plot.getRangeMarkers(Layer.FOREGROUND);
        List<ValueMarker> markerList = new ArrayList<>(markers);
        for (ValueMarker marker : markerList) {
            double y = mouseY;
            double x = mouseX;
            if (y >= marker.getValue() - 3 && y <= marker.getValue() + 3) {
                return true;
            }
            if (x >= marker.getValue() - 3 && x <= marker.getValue() + 3) {
                return true;
            }
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}