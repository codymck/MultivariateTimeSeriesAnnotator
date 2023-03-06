package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

public abstract class AbstractAnnotation {
    
    public abstract boolean isSelected();
    
    public abstract boolean clickedOn(double mouseX, double mouseY);

    public abstract void delete();

    public abstract void move(double xOffset, double yOffset, boolean set);
}