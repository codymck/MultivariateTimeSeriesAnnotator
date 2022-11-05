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
    }
    
    public void formatCSV(String xIndex, String yIndex) {
        System.out.println(xIndex + " " + yIndex);
        
        int x = Integer.valueOf(xIndex);
        int y = Integer.valueOf(yIndex);
        
        String[] keys = ((CSVReader)dReader).getHeaders();
        
        Float[] tempX = ((CSVReader)dReader).getColumn(keys[x]);
        Float[] tempY = ((CSVReader)dReader).getColumn(keys[y]);
        
        xData = ArrayUtils.toPrimitive(tempX);
        yData = ArrayUtils.toPrimitive(tempY);
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
