package edu.csusm.capstone.timeseriesannotator.Model;

import edu.csusm.capstone.timeseriesannotator.View.CSVdataSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author Cody McKinney
 */
public class DataFormatter {
    
    DataReader dReader;
    public static DataFormatter dF;
    
    float[] xData;
    float[] yData;
    
    public DataFormatter(DataReader dR) {
        if (dR instanceof CSVReader) {
//            System.out.println("CSV data to be formatted");
            
            dReader = (CSVReader)dR;
        }
        else if (dR instanceof HDFReader) {
//            System.out.println("HDF5 data to be formatted");
            
            dReader = (HDFReader)dR;
        }
        
        dF = this;
    }
    
    public static DataFormatter getInstance() {
        return dF;
    }
    
    public void formatCSV(int xIndex, int yIndex) {
//        System.out.println(xIndex + " " + yIndex);
        
        String[] keys = ((CSVReader)dReader).getHeaders();
        
        Float[] tempX = ((CSVReader)dReader).getColumn(keys[xIndex]);
        Float[] tempY = ((CSVReader)dReader).getColumn(keys[yIndex]);
        
        xData = ArrayUtils.toPrimitive(tempX);
        yData = ArrayUtils.toPrimitive(tempY);
        
        if(CSVdataSelectMenu.CSV.getTimeStamp()){
            System.out.println("Cody is sexy");
        }
        
//        for(float f : xData){
//            System.out.print(f + " ");
//        }
//        System.out.println();
//        for(float f : yData){
//            System.out.print(f + " ");
//        }
        
//        System.out.println("DataFormatted");
    }
    
    public void formatHDF5(String xPath, String yPath) {
//        System.out.println(xPath+ " " + yPath);
        
        switch(((HDFReader)dReader).getType()){
            case "INTEGER" -> {
                int[] t = ((HDFReader)dReader).getIntXData();
                int[] t2 = ((HDFReader)dReader).getIntYData();
                
                if(HDFdataSelectMenu.HDF.getTimeStamp()){
                    for(int x = 0; x < t.length; x++ ){
                        Date d = new java.util.Date(x);
                        xData[x] = d.getHours() + d.getMinutes() / 10000;
                    }
                    for(int x = 0; x < t2.length; x++ ){
                        Date d = new java.util.Date(x);
                        yData[x] = d.getHours() + d.getMinutes() / 10000;
                    }
                }
                else{
                    for(int x = 0; x < t.length; x++ ){
                        xData[x] = (float)t[x];
                    }
                    for(int x = 0; x < t2.length; x++ ){
                        yData[x] = (float)t2[x];
                    }
                }
            }
            case "FLOAT" -> {
                xData = ((HDFReader)dReader).getXData();
                yData = ((HDFReader)dReader).getYData();
            }
            case "DOUBLE" -> {
                double[] t = ((HDFReader)dReader).getDoubleXData();
                for(int x = 0; x < t.length; x++ ){
                    xData[x] = (float)t[x];
                }
                double[] t2 = ((HDFReader)dReader).getDoubleYData();
                for(int x = 0; x < t2.length; x++ ){
                    yData[x] = (float)t2[x];
                }
            }
            case "DATE" -> {
                //have to make the dataset into a TimeSeriesCollection
                //https://stackoverflow.com/questions/12837986/how-to-display-date-in-a-x-axis-of-line-graph-using-jfreechart
            }
        }

        
    }
    
    public float[] getXDataset() {
        return xData;
    }
    
    public float[] getYDataset() {
        return yData;
    }
}
