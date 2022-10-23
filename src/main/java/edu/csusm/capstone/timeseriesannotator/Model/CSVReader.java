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
    
    public CSVReader() {
        
    }
    
    public void buildDataList(String fileName) {
            var dataRows = new ArrayList<float[]>();
            String[] columnNames;
            
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i != 0) {
                    String[] values = line.split(",");
                    System.out.println(Arrays.toString(values));
                } else {
                columnNames = line.split(",");
                System.out.println(Arrays.toString(columnNames));
                }
                i++;
                if (i == 4) { break;}
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
