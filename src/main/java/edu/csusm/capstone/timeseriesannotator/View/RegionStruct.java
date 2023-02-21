package edu.csusm.capstone.timeseriesannotator.View;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.plot.IntervalMarker;

/**
 *
 * @author Ben Theurich
 */
public class RegionStruct {
    private double x0, y0;
    private double x1, y1;
    private XYBoxAnnotation rect;
    private IntervalMarker region;
    private String type;
    
    RegionStruct(double upperLeftX, double upperLeftY, double lowerRightX, 
        double lowerRightY, IntervalMarker region, String type){
        x0 = upperLeftX;
        y0 = upperLeftY;

        x1 = lowerRightX;
        y1 = lowerRightY;
        this.region = region;
        this.type = type;
    }
    
    RegionStruct(double upperLeftX, double upperLeftY, double lowerRightX, 
        double lowerRightY, XYBoxAnnotation rect, String type){
        x0 = upperLeftX;
        y0 = upperLeftY;

        x1 = lowerRightX;
        y1 = lowerRightY;
        this.rect = rect;
        this.type = type;
    }
    
    public boolean isClickedOn(double mouseX, double mouseY){
        
        if(mouseX >= x0 && mouseX <= x1){
            if(type.equals("region")){
                return true;
            }else if(mouseY >= y0 && mouseY <= y1){
                return true;
            }
        }
        return false;
    }
    public XYBoxAnnotation getRect(){
        return rect;
    }
    public IntervalMarker getRegion(){
        return region;
    }
}
