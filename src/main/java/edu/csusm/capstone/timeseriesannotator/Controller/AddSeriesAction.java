package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Controller.ChartBuilder.ChartTypes;
import edu.csusm.capstone.timeseriesannotator.Model.CSVReader;
import edu.csusm.capstone.timeseriesannotator.Model.DataFormatter;
import edu.csusm.capstone.timeseriesannotator.Model.DataReader;
import edu.csusm.capstone.timeseriesannotator.Model.HDFReader;
import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import edu.csusm.capstone.timeseriesannotator.View.CSVaddSeries;
import edu.csusm.capstone.timeseriesannotator.View.CSVdataSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.ChartDisplay;
import edu.csusm.capstone.timeseriesannotator.View.ChartSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import edu.csusm.capstone.timeseriesannotator.View.HDF5addSeries;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author josef
 */
public class AddSeriesAction implements ActionListener {
    
    AnnotateChartPanel cP;
    Chart chartStruct = Chart.getInstance();
    ChartDisplay dis;
    XYDataset series;
    ChartTypes t = ChartTypes.LineChart;
    
    public AddSeriesAction(Chart c, ChartDisplay d){
        this.chartStruct = c;
        this.dis = d;
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        
        DataReader dReader;
        
        ChartSelectMenu Cselect = new ChartSelectMenu(new javax.swing.JFrame(), true);
        Cselect.setVisible(true);
        ChartAction tAction = ChartAction.getInstance();
        int chartType = tAction.getType();
        
        
        switch (chartType) {
            case 1:
                t = ChartBuilder.ChartTypes.LineChart;
                break;
            case 2:
                t = ChartBuilder.ChartTypes.ScatterPlot;
                break;
            case 3:
                t = ChartBuilder.ChartTypes.StepChart;
                break;
            default:
        }
        
//        chartStruct.setChartType(t);
//        
        if("csv".equals(chartStruct.getFileType())) {
            System.out.println("ImportDataAction: CSV File Imported");
            dReader = new CSVReader();
            dReader.buildDataList(chartStruct.getFileName());
               
            CSVReader c = (CSVReader)dReader;
            String[] headers = c.getHeaders();
               
            CSVaddSeries series = new CSVaddSeries(new javax.swing.JFrame(), true);
            series.setModel(headers);
            series.setVisible(true);
               
            CSVaddAction cAction = CSVaddAction.getInstance();
               
            DataFormatter df = new DataFormatter(dReader);
            df.formatCSV(chartStruct.getXaxis(), cAction.getYAxis());
        }
        else if ("hdf5".equals(chartStruct.getFileType()) || "h5".equals(chartStruct.getFileType())) {
            System.out.println("ImportDataAction: HDF5 File Imported");
      
            HDF5addSeries select = new HDF5addSeries(new javax.swing.JFrame(), true);
            select.setVisible(true);
               
            dReader = new HDFReader();
            dReader.buildDataList(chartStruct.getFileName());
               
            HDF5addAction hAction = HDF5addAction.getInstance();
            HDFReader h = (HDFReader)dReader;
            h.setPaths(chartStruct.getXpath(), hAction.getYPath(), 1);
            
            DataFormatter df = new DataFormatter(dReader);
            df.formatHDF5(HDFReader.xP, HDFReader.yP);
        }
        else {
            ErrorDialog.UnsupportedFile();
        }

        cP = ChartBuilder.addSeries(t);
        dis.setChart(cP);
        
    }
      
}
