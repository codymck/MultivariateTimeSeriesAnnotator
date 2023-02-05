package edu.csusm.capstone.timeseriesannotator.View;
import org.jfree.chart.annotations.XYBoxAnnotation;

/**
 *
 * @author Ben Theurich
 */
public class RegionStruct {
    private double x0, y0;
    private double x1, y1;
    private XYBoxAnnotation region;
    
    RegionStruct(double upperLeftX, double upperLeftY, double lowerRightX, 
        double lowerRightY, XYBoxAnnotation region){
        x0 = upperLeftX;
        y0 = upperLeftY;

        x1 = lowerRightX;
        y1 = lowerRightY;
        this.region = region;
    }
    
    public boolean isClickedOn(double mouseX, double mouseY){
        System.out.println("mouseX: " + mouseX + ", mouseY: " + mouseY);
        if(mouseX >= x0 && mouseX <= x1 && 
                mouseY >= y0 && mouseY <= y1){
            System.out.println("inside");
            return true;
        }
        return false;
    }
    public XYBoxAnnotation getRegion(){
        return region;
    }
}
