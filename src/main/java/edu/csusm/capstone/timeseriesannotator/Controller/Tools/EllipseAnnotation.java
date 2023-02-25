package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import java.awt.geom.Point2D;
import java.awt.Color;
import org.jfree.chart.plot.XYPlot;

public class EllipseAnnotation extends AbstractAnnotation {
    public boolean selected;
    public Color color;
    public XYPlot plot;

    EllipseAnnotation(XYPlot p, Color c) {
        this.plot = p;
        this.color = c;
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clickedOn'");
    }

    void delete(Point2D point) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
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