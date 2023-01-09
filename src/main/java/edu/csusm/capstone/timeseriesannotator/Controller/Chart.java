/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Controller.ChartBuilder.ChartTypes;
import edu.csusm.capstone.timeseriesannotator.Model.XYLineChartDataset;

/**
 *
 * @author josef
 */
public class Chart {
    private String fileName;
    private String fileType;
    private ChartTypes chartType;
    private int Xaxis; 
    private XYLineChartDataset series;
    private int flag;
    
    public static Chart Ct;
    
    
    public Chart(String f, String t, ChartTypes c, XYLineChartDataset d){
        fileName = f;
        fileType = t;
        chartType = c;
        series = d;
        flag = 1;
        Ct = this;
    }
    
    public static Chart getInstance() {
        return Ct;
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
    
    public XYLineChartDataset getSeries(){
        return series;
    }
    
    public int getFlag(){
        return flag;
    }
    public void setFlag(int f){
        flag = f;
    }
    
}
