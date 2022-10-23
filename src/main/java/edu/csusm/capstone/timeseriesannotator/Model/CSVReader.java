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

/**
 *
 * @author cmcki
 */
public class CSVReader implements DataReader {

    ArrayList<String[]> dataRows;

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
                if (i != 0) {
                    String[] values = line.split(",");
                    dataRows.add(values);
                } else {
                    String[] columnNames = line.split(",");
                    dataRows.add(columnNames);
                }
                i++;
                //if (i == 4) { break;}
            }

            // printing each array within ArrayList dataRows
            for (String[] str : dataRows) {
                System.out.println(Arrays.toString(str));
            }

            // HOW TO PRINT ONE LINE OF THE dataRows ArrayList of String arrays
            // System.out.println(Arrays.toString(dataRows.get(4321))); 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
