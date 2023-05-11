package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.Controller.ChartBuilder.ChartTypes;
import edu.csusm.capstone.timeseriesannotator.Model.CSVReader;
import edu.csusm.capstone.timeseriesannotator.Model.DataFormatter;
import edu.csusm.capstone.timeseriesannotator.Model.DataReader;
import edu.csusm.capstone.timeseriesannotator.Model.HDFReader;
import edu.csusm.capstone.timeseriesannotator.Model.XYLineChartDataset;
import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import edu.csusm.capstone.timeseriesannotator.View.CSVdataSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.ChartDisplay;
import edu.csusm.capstone.timeseriesannotator.View.ChartSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author Cody McKinney
 */
public class ImportDataAction implements ActionListener {

    AnnotateChartPanel cP;

    JFileChooser importChooser;
    ChartDisplay dis;
    ChartStruct chartStruct;// = ChartStruct.getInstance();

    public ImportDataAction(JFileChooser iC, ChartDisplay f) {
        this.importChooser = iC;
        this.dis = f;
    }

    /**
     *
     * @param file - name of the file we are parsing
     * @return - returns a string of the file typeX
     */
    public String findFileType(String file) {
        String fileType = "";
        char c;

        // loop through file name from the end
        for (int i = file.length() - 1; i >= 0; i--) {
            c = file.charAt(i);
            // when we reach a '.' it is the end of file typeX
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
        JFrame frame = AppFrame.frame;
        int x = frame.getX() + (frame.getWidth() - importChooser.getWidth()) / 2;
        int y = frame.getY() + (frame.getHeight() - importChooser.getHeight()) / 2;
        importChooser.setLocation(x, y);
        File importFile = null;
        DataReader dReader;
        if (importChooser.showOpenDialog(frame) != JFileChooser.CANCEL_OPTION) {
            importFile = importChooser.getSelectedFile();
        }
        if (importFile != null) {

            String fileName = importFile.getAbsolutePath();

            String fileType = findFileType(fileName);

            ChartSelectMenu Cselect = new ChartSelectMenu(new javax.swing.JFrame(), true);
            Cselect.setVisible(true);
            ChartAction tAction = ChartAction.getInstance();
            if (!Cselect.isSelected()) {
                return;
            }
            int chartType = tAction.getType();
            ChartTypes t = ChartTypes.LineChart;

            switch (chartType) {
                case 1:
                    t = ChartTypes.LineChart;
                    break;
                case 2:
                    t = ChartTypes.ScatterPlot;
                    break;
                case 3:
                    t = ChartTypes.StepChart;
                    break;
                default:
            }

            XYLineChartDataset xyChart = new XYLineChartDataset();

            chartStruct = new ChartStruct(fileName, fileType, t, xyChart);

            //Select menu features
            if ("csv".equals(fileType)) {
                dReader = new CSVReader();
                dReader.buildDataList(fileName);

                CSVReader c = (CSVReader) dReader;
                String[] headers = c.getHeaders();

                CSVdataSelectMenu select = new CSVdataSelectMenu(new javax.swing.JFrame(), true);
                select.setModel(headers);
                select.setVisible(true);

                CSVAction cAction = CSVAction.getInstance();
                if (!cAction.isSelected()) {
                    return;
                }

                chartStruct.setXaxis(cAction.getXAxis());
                ArrayList<String> labels = new ArrayList<>();
                labels.add(cAction.getY() + " vs " + cAction.getX());
                labels.add(cAction.getX());
                labels.add(cAction.getY());
                chartStruct.setLabels(labels);

                DataFormatter df = new DataFormatter(dReader, chartStruct);
                df.formatCSV(cAction.getXAxis(), cAction.getYAxis());
            } else if ("hdf5".equals(fileType) || "h5".equals(fileType)) {
                dReader = new HDFReader();
                dReader.buildDataList(fileName);//sets file name
                HDFReader h = (HDFReader) dReader;

                long startTime = System.currentTimeMillis();
                List<String> headers = h.buildPath("/");//get initial list of headers
                long endTime = System.currentTimeMillis();

                HDFdataSelectMenu select = new HDFdataSelectMenu(new javax.swing.JFrame(), true);
                select.setModel(headers, h);
                select.setVisible(true);

                HDF5Action hAction = HDF5Action.getInstance();
                if (!select.isSelected()) {
                    return;
                }

                h.setPaths(hAction.getXPath(), hAction.getYPath(), 0);
                chartStruct.setXpath(hAction.getXPath());
                String[] tmpX = hAction.getXPath().split("/");
                String[] tmpY = hAction.getYPath().split("/");

                ArrayList<String> labels = new ArrayList<>();
                labels.add(tmpY[tmpY.length - 1] + " vs " + tmpX[tmpX.length - 1]);
                labels.add(tmpX[tmpX.length - 1]);
                labels.add(tmpY[tmpY.length - 1]);
                chartStruct.setLabels(labels);

                DataFormatter df = new DataFormatter(dReader, chartStruct);
                df.formatHDF5(HDFReader.xP, HDFReader.yP);
            } else {
                ErrorDialog.UnsupportedFile();
            }

            cP = ChartBuilder.buildCharts(chartStruct.getChartType(), chartStruct);

            dis.setChartData(chartStruct);

            dis.setChart(cP);
        }
    }
}
