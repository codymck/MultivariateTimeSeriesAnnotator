package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

public abstract class AbstractAnnotation {
    
    public abstract boolean isSelected();
    
    public abstract boolean clickedOn(double mouseX, double mouseY);

    public abstract void delete();

    public abstract void move(double xOffset, double yOffset, boolean set);
    
    public abstract String getType();
    
    public abstract String getRGBA();
    
    public abstract String getCoords();
    
    public abstract String getData();
    
    public String[] export(){
        String[] row = {this.getType(), this.getRGBA(), this.getCoords(), this.getData()};
        return row;
    }
}