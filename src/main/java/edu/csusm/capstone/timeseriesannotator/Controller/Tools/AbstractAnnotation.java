package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import java.awt.geom.Point2D;
import org.jfree.chart.plot.XYPlot;

public abstract class AbstractAnnotation {

    abstract boolean clickedOn(double mouseX, double mouseY);

    abstract boolean clickedOn(Point2D point);

    abstract void delete(double[] point);

    abstract void removeFromPlot(XYPlot plot);

    abstract void move(double xOffset, double yOffset);
}