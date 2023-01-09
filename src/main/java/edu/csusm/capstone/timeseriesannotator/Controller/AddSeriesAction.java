/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Model.CSVReader;
import edu.csusm.capstone.timeseriesannotator.Model.DataFormatter;
import edu.csusm.capstone.timeseriesannotator.Model.DataReader;
import edu.csusm.capstone.timeseriesannotator.Model.HDFReader;
import edu.csusm.capstone.timeseriesannotator.Model.XYLineChartDataset;
import edu.csusm.capstone.timeseriesannotator.View.CSVaddSeries;
import edu.csusm.capstone.timeseriesannotator.View.CSVdataSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.ChartDisplay;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author josef
 */
public class AddSeriesAction implements ActionListener {
    
    ChartPanel cP;
    Chart chartStruct = Chart.getInstance();
    ChartDisplay dis;
    XYDataset series;
    
    public AddSeriesAction(Chart c, ChartDisplay d){
        this.chartStruct = c;
        this.dis = d;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        DataReader dReader;
        
        if("csv".equals(chartStruct.getFileType())) {
            System.out.println("ImportDataAction: CSV File Imported");
            dReader = new CSVReader();
            dReader.buildDataList(chartStruct.getFileName());
               
            CSVReader c = (CSVReader)dReader;
            String[] headers = c.getHeaders();
               
//               CSVdataSelectMenu select = new CSVdataSelectMenu(new javax.swing.JFrame(), true);
//               select.setModel(headers);
//               select.setVisible(true);
               
            CSVaddSeries series = new CSVaddSeries(new javax.swing.JFrame(), true);
            series.setModel(headers);
            series.setVisible(true);
               
            CSVaddAction cAction = CSVaddAction.getInstance();
               
            DataFormatter df = new DataFormatter(dReader);
            df.formatCSV(chartStruct.getXaxis(), cAction.getYAxis());
        }
        else if ("hdf5".equals(chartStruct.getFileType()) || "h5".equals(chartStruct.getFileType())) {
            System.out.println("ImportDataAction: HDF5 File Imported");
      
            HDFdataSelectMenu select = new HDFdataSelectMenu(new javax.swing.JFrame(), true);
            select.setVisible(true);
               
            dReader = new HDFReader();
            dReader.buildDataList(chartStruct.getFileName());
               
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

        cP = ChartBuilder.buildCharts(chartStruct.getChartType());
        dis.setChart(cP);
        
    }
    
    
    
}
