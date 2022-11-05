/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Model;

import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5SimpleReader;

/**
 *
 * @author Cody McKinney
 */
public class HDFReader implements DataReader {

    float[] myXdata;
    float[] myYdata;
    String file;

    @Override
    public void buildDataList(String fileName) {
        this.file = fileName;
    }

    @Override
    public void setPaths(String x, String y) {
        try ( IHDF5SimpleReader reader = HDF5Factory.openForReading(file)) {
            myXdata = reader.readFloatArray(x);
            myYdata = reader.readFloatArray(y);
        } catch (Exception e) {
            System.err.println(e);
        }
        // OUTPUT FOR DATA 
//        System.out.print("X :   ");
//        for (float d : myXdata) {
//            System.out.print(d + " , ");
//        }
//        System.out.println();
//        System.out.print("Y :   ");
//        for (float r : myYdata) {
//            System.out.print(r + " , ");
//        }
        
        DataFormatter df = new DataFormatter(this);
    }
    
    public float[] getXData() {
        return myXdata;
    }
    
    public float[] getYData() {
        return myYdata;
    }
}
