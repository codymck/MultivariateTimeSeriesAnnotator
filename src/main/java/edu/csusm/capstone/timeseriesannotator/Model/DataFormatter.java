/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Model;

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
            System.out.println("CSV data to be formatted");
            
            dReader = (CSVReader)dR;
        }
        else if (dR instanceof HDFReader) {
            System.out.println("HDF5 data to be formatted");
            
            dReader = (HDFReader)dR;
        }
        
        dF = this;
    }
    
    public static DataFormatter getInstance() {
        return dF;
    }
    
    public void formatCSV(int xIndex, int yIndex) {
        System.out.println(xIndex + " " + yIndex);
        
        String[] keys = ((CSVReader)dReader).getHeaders();
        
        Float[] tempX = ((CSVReader)dReader).getColumn(keys[xIndex]);
        Float[] tempY = ((CSVReader)dReader).getColumn(keys[yIndex]);
        
        xData = ArrayUtils.toPrimitive(tempX);
        yData = ArrayUtils.toPrimitive(tempY);
        
//        for(float f : xData){
//            System.out.print(f + " ");
//        }
//        System.out.println();
//        for(float f : yData){
//            System.out.print(f + " ");
//        }
        
        System.out.println("DataFormatted");
    }
    
    public void formatHDF5(String xPath, String yPath) {
        System.out.println(xPath+ " " + yPath);
        
        xData = ((HDFReader)dReader).getXData();
        yData = ((HDFReader)dReader).getYData();
    }
    
    public float[] getXDataset() {
        return xData;
    }
    
    public float[] getYDataset() {
        return yData;
    }
}
