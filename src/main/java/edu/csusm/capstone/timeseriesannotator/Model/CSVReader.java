/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ben Theurich
 * @author Cody McKinney
 */
public class CSVReader implements DataReader {

    private ArrayList<String[]> dataRows;
    private String[] headers;
    private Map<String, Float[]> columns = new HashMap<>();

    /**
     * Class constructor.
     */
    public CSVReader() {

    }

    /**
     *
     * @param fileName - name of the file that has been imported
     */
    @Override
    public void buildDataList(String fileName) {
        dataRows = new ArrayList<String[]>();

        try ( BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i == 0) {
                    String[] columnNames = line.split(",", -1);

                    for (int j = 0; j < columnNames.length; j++) {
                        if (columnNames[j].isBlank()) {
                            columnNames[j] = "null";
                        }
                    }
                    headers = columnNames;
                    dataRows.add(columnNames);
                } else {
                    String[] values = line.split(",", -1);
                    for (int j = 0; j < values.length; j++) {//
                        if (values[j].isBlank()) {
                            values[j] = "0.0"; // Ask Ryan what value he would want here
                        }
                    }
                    dataRows.add(values);
                }
                i++;
            }

            // iterates through every "column"
            for (int j = 0; j < dataRows.get(0).length; j++) {
                Float[] tempArray = new Float[dataRows.size() - 1];
                // iterates through every "row"
                for (int d = 0; d < tempArray.length; d++) {
                    tempArray[d] = (Float.valueOf(dataRows.get(d + 1)[j]));
                }
                // add keypair to hashmap
                columns.put(dataRows.get(0)[j], tempArray);
            }

            // printing out each keypair in the hashmap
//            for (var name : columns.keySet()) {
//                String key = name;
//                String value = Arrays.toString(columns.get(name));
//                System.out.println(key + "" + value);
//            }
   
            // HOW TO PRINT ONE LINE OF THE dataRows ArrayList of String arrays
            // System.out.println(Arrays.toString(dataRows.get(4321))); 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param s - key in hashmap (chosen by user)
     * @return - return an array of float values corresponding to the key s
     */
    public Float[] getColumn(String s) {
        return columns.get(s);
    }

    /**
     *
     * @return - return the array of header strings
     */
    public String[] getHeaders() {
        return headers;
    }

    @Override
    public void setPaths(String xaxis, String yaxis) {
        int xaxisColumn = Integer.parseInt(xaxis);
        int yaxisColumn = Integer.parseInt(yaxis);
    }
}
