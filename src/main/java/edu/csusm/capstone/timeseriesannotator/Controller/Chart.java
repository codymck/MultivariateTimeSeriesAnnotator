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
    private String Xpath;
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
    
    public void setXaxis(int x){
        Xaxis = x;
    }
    
    public int getXaxis(){
        return Xaxis;
    }
    
    public void setXpath(String x){
        Xpath = x;
    }
    
    public String getXpath(){
        return Xpath;
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
    
    public void setChartType(ChartTypes cT){
        chartType = cT;
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
