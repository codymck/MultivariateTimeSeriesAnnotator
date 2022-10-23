/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Model;


/**
 *
 * @author cmcki
 */
public interface DataReader {

    /**
     *
     * @param fileName - name of the file that has been selected
     */
    public void buildDataList(String fileName);
}
