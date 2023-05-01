package edu.csusm.capstone.timeseriesannotator.Model;

import edu.csusm.capstone.timeseriesannotator.View.CSVdataSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author Cody McKinney
 */
public class DataFormatter {
    
    DataReader dReader;
    public static DataFormatter dF;
    static final long EPOCH = new java.util.Date(2015 - 1900, Calendar.JANUARY, 1).getTime(); // 2015/1/1 in GMT

    
    float[] xData;
    float[] yData;
    
    Date[] xDateData;
    Date[] yDateData;
    
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
            System.out.println("Cody is sexy and hot");
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
        
        switch(((HDFReader)dReader).getXType()){
            case "INTEGER" -> {

                int[] t = ((HDFReader)dReader).getIntXData();
//                System.out.println(EPOCH);
                
                if(HDFdataSelectMenu.HDF.getTimeStamp()){
                    System.out.println("Formatting");
                    xDateData = new Date[t.length];
                    for(int x = 0; x < t.length; x++ ){
                        xDateData[x] = new java.util.Date(t[x] * 1000 + EPOCH);
//                        System.out.println(xDateData[x]);
                    }
                }
                else{
                    xData = new float[t.length];
                    for(int x = 0; x < t.length; x++ ){
                        xData[x] = (float)t[x];
                    }
                }
                break;
            }
            case "FLOAT" -> {
                xData = ((HDFReader)dReader).getXData();
                break;
            }
            case "DOUBLE" -> {
                double[] t = ((HDFReader)dReader).getDoubleXData();
                for(int x = 0; x < t.length; x++ ){
                    xData[x] = (float)t[x];
                }
                break;
            }
            case "DATE" -> {
                //have to make the dataset into a TimeSeriesCollection
                //https://stackoverflow.com/questions/12837986/how-to-display-date-in-a-x-axis-of-line-graph-using-jfreechart
            }
        }
        switch(((HDFReader)dReader).getYType()){
            case "INTEGER" -> {

                int[] t2 = ((HDFReader)dReader).getIntYData();
                yData = new float[t2.length];

                for(int x = 0; x < t2.length; x++){
                    yData[x] = (float)t2[x];
                }
                
                break;
            }
            case "FLOAT" -> {
                yData = ((HDFReader)dReader).getYData();
                break;
            }
            case "DOUBLE" -> {
                double[] t2 = ((HDFReader)dReader).getDoubleYData();
                for(int x = 0; x < t2.length; x++ ){
                    yData[x] = (float)t2[x];
                }
                break;
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
    
    public Date[] getXDateDataset() {
        return xDateData;
    }
    
    public Date[] getYDateDataset() {
        return yDateData;
    }
}
