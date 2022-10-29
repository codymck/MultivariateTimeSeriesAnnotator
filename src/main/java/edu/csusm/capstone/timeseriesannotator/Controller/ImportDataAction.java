/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Model.CSVReader;
import edu.csusm.capstone.timeseriesannotator.Model.DataReader;
import edu.csusm.capstone.timeseriesannotator.Model.HDFReader;
import edu.csusm.capstone.timeseriesannotator.View.CSVdataSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.HDF5dataSelectMenu;
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
    
    /**
     * 
     * @param file - name of the file we are parsing
     * @return - returns a string of the file type
     */
    public String findFileType(String file) {
        String fileType = "";
        char c;
        
        // loop through file name from the end
        for (int i = file.length() - 1; i >= 0; i--) {
            c = file.charAt(i);
            // when we reach a '.' it is the end of file type
            if (c == '.') {
                break;
            }
            // append character to rever
            fileType = c + fileType;
        }
        
        return fileType;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DataReader dReader;
        importChooser.showOpenDialog(null);
        File importFile = importChooser.getSelectedFile();
        
        if (importFile != null) {
           String fileName = importFile.getAbsolutePath();
           System.out.println(fileName);
           
           String fileType = findFileType(fileName);
           
           if ("csv".equals(fileType)) {
               System.out.println("ImportDataAction: CSV File Imported");
               dReader = new CSVReader();
               dReader.buildDataList(fileName);
               String[] headers = dReader.getHeaders();
               CSVdataSelectMenu select = new CSVdataSelectMenu(new javax.swing.JFrame(), true);
               select.setModel(headers);
               select.setVisible(true);
               dReader.setPaths(select.getXPath(), select.getYPath());
           }
           else if ("hdf5".equals(fileType) || "h5".equals(fileType)) {
               System.out.println("ImportDataAction: HDF5 File Imported");
               dReader = new HDFReader();
               dReader.buildDataList(fileName);
               HDF5dataSelectMenu select = new HDF5dataSelectMenu(new javax.swing.JFrame(), true);
               select.setVisible(true);
               dReader.setPaths(select.getXPath(), select.getYPath());
           }
           else {
               System.out.println("ImportDataAction: Unsupported File Type");
               // TODO build popup window with error message for unsupported file type
           }
        }
    }
    
}
