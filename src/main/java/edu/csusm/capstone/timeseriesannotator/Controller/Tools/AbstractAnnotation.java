package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import java.awt.geom.Point2D;
import org.jfree.chart.plot.XYPlot;

public abstract class AbstractAnnotation {

    public abstract boolean clickedOn(double mouseX, double mouseY);

    public abstract void delete();

    abstract void move(double xOffset, double yOffset);
}