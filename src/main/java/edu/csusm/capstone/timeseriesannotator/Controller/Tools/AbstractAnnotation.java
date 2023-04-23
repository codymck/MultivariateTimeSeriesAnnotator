package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import org.jfree.chart.plot.XYPlot;

public abstract class AbstractAnnotation {
    
    public boolean isSelected(){
        return selected;
    };
    
    public abstract boolean clickedOn(double mouseX, double mouseY);

    public abstract void delete();

    public abstract void move(double xOffset, double yOffset, boolean set);
    
    public abstract void select();
    
    public abstract void deselect();
    
    public abstract String getType();
    
    public String getRGBA(){
        String R = String.valueOf(color.getRed());
        String G = String.valueOf(color.getGreen());
        String B = String.valueOf(color.getBlue());
        String A = String.valueOf(color.getAlpha());

        return "[" + R + ", " + G + ", " + B + ", " + A + "]";
    };
    
    public abstract String getCoords();
    
    public abstract String getData();
    
    public String[] export(){
        String[] row = {this.getType(), this.getRGBA(), this.getCoords(), this.getData()};
        return row;
    }
    
    protected Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, 
            BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
    
    protected ResizeHandle[] handles;
    protected boolean selected;
    protected Color color;
    protected XYPlot plot;
    protected AnnotateChartPanel chartPanel;
    protected String type;

}