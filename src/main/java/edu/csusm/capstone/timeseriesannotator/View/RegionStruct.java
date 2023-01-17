/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.View;
import org.jfree.chart.annotations.XYBoxAnnotation;

/**
 *
 * @author Ben Theurich
 */
public class RegionStruct {
    private double[][] coordinates;
    private XYBoxAnnotation region;
    
    RegionStruct(double[][] coordinates, XYBoxAnnotation region){
        this.coordinates = coordinates;
        this.region = region;
    }
    
    public boolean isClickedOn(double mouseX, double mouseY){
        System.out.println("mouseX: " + mouseX + ", mouseY: " + mouseY);
        if(mouseX >= coordinates[0][0] && mouseX <= coordinates[1][0] && 
                mouseY >= coordinates[1][1] && mouseY <= coordinates[0][1]){
            System.out.println("inside");
            return true;
        }
        return false;
    }
    
    public XYBoxAnnotation getRegion(){
        return region;
    }
}
