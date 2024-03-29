package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Controller.ChartBuilder.ChartTypes;
import edu.csusm.capstone.timeseriesannotator.Model.CSVReader;
import edu.csusm.capstone.timeseriesannotator.Model.DataFormatter;
import edu.csusm.capstone.timeseriesannotator.Model.DataReader;
import edu.csusm.capstone.timeseriesannotator.Model.HDFReader;
import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import edu.csusm.capstone.timeseriesannotator.View.CSVaddSeries;
import edu.csusm.capstone.timeseriesannotator.View.ChartDisplay;
import edu.csusm.capstone.timeseriesannotator.View.ChartSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import edu.csusm.capstone.timeseriesannotator.View.HDF5addSeries;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author josef
 */
public class AddSeriesAction implements ActionListener {

    AnnotateChartPanel cP;
    ChartStruct chartStruct;
    ChartDisplay dis;
    XYDataset series;
    ChartTypes t = ChartTypes.LineChart;

    public AddSeriesAction(ChartStruct c, ChartDisplay d) {
        this.chartStruct = c;
        this.dis = d;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        DataReader dReader;

        ChartSelectMenu Cselect = new ChartSelectMenu(new javax.swing.JFrame(), true);
        Cselect.setVisible(true);
        ChartAction tAction = ChartAction.getInstance();
        if (!Cselect.isSelected()) {
            return;
        }
        int chartType = tAction.getType();
        ArrayList<String> labels = chartStruct.getLabels();

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

        if ("csv".equals(chartStruct.getFileType())) {
            dReader = new CSVReader();
            dReader.buildDataList(chartStruct.getFileName());

            CSVReader c = (CSVReader) dReader;
            String[] headers = c.getHeaders();

            CSVaddSeries series = new CSVaddSeries(new javax.swing.JFrame(), true, chartStruct);
            series.setModel(headers);
            series.setVisible(true);

            CSVaddAction cAction = CSVaddAction.getInstance();
            if (!cAction.isSelected()) {
                return;
            }
            labels.add(cAction.y);
            chartStruct.setLabels(labels);

            DataFormatter df = new DataFormatter(dReader, chartStruct);
            df.formatCSV(chartStruct.getXaxis(), cAction.getYAxis());
        } else if ("hdf5".equals(chartStruct.getFileType()) || "h5".equals(chartStruct.getFileType())) {
            dReader = new HDFReader();
            dReader.buildDataList(chartStruct.getFileName());//sets file name
            HDFReader h = (HDFReader) dReader;
            List<String> headers = h.buildPath("/");//get initial list of headers

            HDF5addSeries select = new HDF5addSeries(new javax.swing.JFrame(), true);
            select.setModel(headers, h);
            select.setVisible(true);

            HDF5addAction hAction = HDF5addAction.getInstance();

            if (!select.isSelected()) {
                return;
            }

            h.setPaths(chartStruct.getXpath(), hAction.getYPath(), 1);
            String[] tmpY = hAction.getYPath().split("/");
            labels.add(tmpY[tmpY.length - 1]);

            chartStruct.setLabels(labels);

            DataFormatter df = new DataFormatter(dReader, chartStruct);
            df.formatHDF5(HDFReader.xP, HDFReader.yP);
        } else {
            ErrorDialog.UnsupportedFile();
        }

        cP = ChartBuilder.addSeries(t, chartStruct);
        dis.setChart(cP);
    }
}
