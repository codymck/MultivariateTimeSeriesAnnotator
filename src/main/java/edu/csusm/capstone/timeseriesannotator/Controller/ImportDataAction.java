/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Model.CSVReader;
import edu.csusm.capstone.timeseriesannotator.Model.DataReader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Cody McKinney
 */
public class ImportDataAction implements ActionListener {
    
    JFileChooser importChooser;
    
    public ImportDataAction(JFileChooser importChooser) {
        this.importChooser = importChooser;
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DataReader dReader;
        importChooser.showOpenDialog(null);
        File importFile = importChooser.getSelectedFile();
        
        if (importFile != null) {
           String fileName = importFile.getAbsolutePath();
           System.out.println(fileName);
           
           String[] fileType = fileName.split("[.]");
           
           if ("csv".equals(fileType[1])) {
               System.out.println("CSV File Imported");
               dReader = new CSVReader();
               dReader.buildDataList(fileName);
           }
           else {
               System.out.println("Unsupported File Type");
               // TODO build popup window with error message for unsupported file type
           }
        }
    }
    
}
