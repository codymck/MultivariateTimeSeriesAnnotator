package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Controller.ChartBuilder.ChartTypes;
import edu.csusm.capstone.timeseriesannotator.Model.XYLineChartDataset;
import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.util.ArrayList;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author josef
 */
public class ChartStruct {
    private String fileName;
    private String fileType;
    private ChartTypes chartType;
    private int Xaxis; 
    private String Xpath;
    private XYLineChartDataset series;
    private int flag;
    XYPlot plot;
    private ValueAxis dAxis;
    private ValueAxis rAxis;
    private ArrayList<String> Labels; //0:Title, 1: X, 2: Y, 3+: Yn
    private AnnotateChartPanel aP;
    
    //public static ChartStruct Ct;
    
    
    public ChartStruct(String f, String t, ChartTypes c, XYLineChartDataset d){
        fileName = f;
        fileType = t;
        chartType = c;
        series = d;
        flag = 1;
        //Ct = this;
    }
    
//    public static ChartStruct getInstance() {
//        return Ct;
//    }
    
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
    
    public XYPlot getPlot(){
        return plot;
    }
    public void setPlot(XYPlot p){
        plot = p;
    }
    
    public ValueAxis getDomainAxis(){
        return dAxis;
    }
    public void setDomainAxis(ValueAxis a){
        dAxis = a;
    }
    
    public ValueAxis getRangeAxis(){
        return rAxis;
    }
    public void setRangeAxis(ValueAxis r){
        rAxis = r;
    }
    
    public void setLabels(ArrayList<String> l){
        Labels = l;
    }
    public ArrayList<String> getLabels(){
        return Labels;
    }
    
    public void setAnnotateChartPanel(AnnotateChartPanel a) {
        aP = a;
    }
    
    public AnnotateChartPanel getAnnotateChartPanel() {
        return aP;
    }
}
