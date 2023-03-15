package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import com.opencsv.CSVWriter;
import java.util.List;

public abstract class AbstractAnnotation {
    
    public abstract boolean isSelected();
    
    public abstract boolean clickedOn(double mouseX, double mouseY);

    public abstract void delete();

    public abstract void move(double xOffset, double yOffset, boolean set);
    
    public abstract void export(CSVWriter writer);
    
    public abstract String[] getRGBAList();
    
    public abstract String[] getCoordsList();
    
    public abstract String[] getDataList();
}