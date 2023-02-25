package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import java.awt.geom.Point2D;
import java.awt.Color;
import org.jfree.chart.plot.XYPlot;

public class LineAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;

    LineAnnotation(XYPlot p, Color c) {
        this.plot = p;
        this.color = c;
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
    void delete(double[] point) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
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