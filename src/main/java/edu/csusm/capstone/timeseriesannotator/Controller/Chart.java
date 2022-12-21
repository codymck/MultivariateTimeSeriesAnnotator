/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Controller.ChartBuilder.ChartTypes;

/**
 *
 * @author josef
 */
public class Chart {
    private String fileName;
    private String fileType;
    private ChartTypes chartType;
    private int Xaxis; 
    
    
    public Chart(String f, String t, ChartTypes c){
        fileName = f;
        fileType = t;
        chartType = c;
    }
    
//    public void setFileName(String f){
//        fileName = f;
//    }
//    
//    public void setFileType(String t){
//        fileType = t;
//    }
//        
//    public void setChartType(ChartTypes c){
//        chartType = c;
//    }
    
    public void setXaxis(int x){
        Xaxis = x;
    }
    
    public int getXaxis(){
        return Xaxis;
    }
    
    public String getFileName(){
        return fileName;
    }
    
    public String getFileType(){
        return fileType;
    }
        
    public ChartTypes getChartType(){
        return chartType;
    }
    
}
