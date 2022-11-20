/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Model.CSVReader;
import edu.csusm.capstone.timeseriesannotator.Model.DataFormatter;
import edu.csusm.capstone.timeseriesannotator.Model.DataReader;
import edu.csusm.capstone.timeseriesannotator.Model.HDFReader;
import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import edu.csusm.capstone.timeseriesannotator.View.CSVdataSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.ChartSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author Cody McKinney
 */
public class ImportDataAction implements ActionListener {
    
    ChartPanel cP;
    
    JFileChooser importChooser;
    AppFrame fun;
    
    public ImportDataAction(JFileChooser importChooser, AppFrame f) {
        this.importChooser = importChooser;
        this.fun = f;
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
            // append character to fileType
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
           
           
           ChartSelectMenu Cselect = new ChartSelectMenu(new javax.swing.JFrame(), true);
           Cselect.setVisible(true);
           ChartAction tAction = ChartAction.getInstance();
           int chartType = tAction.getType();
           //Select menu features
           
           if("csv".equals(fileType)) {
               System.out.println("ImportDataAction: CSV File Imported");
               dReader = new CSVReader();
               dReader.buildDataList(fileName);
               
               CSVReader c = (CSVReader)dReader;
               String[] headers = c.getHeaders();
               
               CSVdataSelectMenu select = new CSVdataSelectMenu(new javax.swing.JFrame(), true);
               select.setModel(headers);
               select.setVisible(true);
               
               CSVAction cAction = CSVAction.getInstance();
               
               DataFormatter df = new DataFormatter(dReader);
               df.formatCSV(cAction.getXAxis(), cAction.getYAxis());
           }
           else if ("hdf5".equals(fileType) || "h5".equals(fileType)) {
               System.out.println("ImportDataAction: HDF5 File Imported");
        
               HDFdataSelectMenu select = new HDFdataSelectMenu(new javax.swing.JFrame(), true);
               select.setVisible(true);
               
               dReader = new HDFReader();
               dReader.buildDataList(fileName);
               
               HDF5Action hAction = HDF5Action.getInstance();
               HDFReader h = (HDFReader)dReader;
               h.setPaths(hAction.getXPath(), hAction.getYPath());
               
               DataFormatter df = new DataFormatter(dReader);
               df.formatHDF5(HDFReader.xP, HDFReader.yP);
           }
           else {
               System.out.println("ImportDataAction: Unsupported File Type");
               // TODO build popup window with error message for unsupported file type
           }
           switch(chartType) {
                case 1:
                    cP = ChartBuilder.buildCharts(ChartBuilder.ChartTypes.LineChart);
                    break;
                case 2:
                    cP = ChartBuilder.buildCharts(ChartBuilder.ChartTypes.ScatterPlot);
                    break;
                case 3:
                    cP = ChartBuilder.buildCharts(ChartBuilder.ChartTypes.StepChart);
                    break;
                default:
            }
           fun.setChart(cP);
        }
    }
    
}
