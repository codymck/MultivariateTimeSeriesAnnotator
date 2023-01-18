package edu.csusm.capstone.timeseriesannotator.View;
import org.jfree.chart.annotations.XYBoxAnnotation;

/**
 *
 * @author Ben Theurich
 */
public class RegionStruct {
    private double x1, y1;
    private double x2, y2;
    private XYBoxAnnotation region;
    
    RegionStruct(double[][] coordinates, XYBoxAnnotation region){
        x1 = coordinates[0][0];
        x2 = coordinates[1][0];
        
        y1 = coordinates[1][1];
        y2 = coordinates[0][1];
        this.region = region;
    }
    
    public boolean isClickedOn(double mouseX, double mouseY){
        System.out.println("mouseX: " + mouseX + ", mouseY: " + mouseY);
        if(mouseX >= x1 && mouseX <= x2 && 
                mouseY >= y1 && mouseY <= y2){
            System.out.println("inside");
            return true;
        }
        return false;
    }
    
    public XYBoxAnnotation getRegion(){
        return region;
    }
}
