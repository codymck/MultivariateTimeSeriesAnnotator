/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Model;

import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5SimpleReader;
import java.lang.reflect.Array;

/**
 *
 * @author Cody McKinney
 */
public class HDFReader implements DataReader {
    float[] mydata;
    String xaxis;
    String yaxis;
        
    @Override
    public void buildDataList(String fileName) {
        try (IHDF5SimpleReader reader = HDF5Factory.openForReading(fileName)) {
            mydata = reader.readFloatArray("00000/000/t");
        }
        for (float d : mydata) {
            System.out.println(d);
        }
    }
    
    @Override
    public String[] getHeaders(){
        String[] temp = {"temp"};
        return temp;
    }
    
    @Override
    public void setPaths(String x, String y){
        xaxis = x;
        yaxis = y;
    }

}
