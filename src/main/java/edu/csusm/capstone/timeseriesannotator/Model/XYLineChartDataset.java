package edu.csusm.capstone.timeseriesannotator.Model;

import edu.csusm.capstone.timeseriesannotator.View.CSVdataSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import java.util.Date;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYLineChartDataset implements ChartData {

    public AbstractIntervalXYDataset data;
    public TimeSeriesCollection d;
    public TimeSeriesCollection d2;
    public XYSeriesCollection dataset;
    public XYSeriesCollection dataset2;
    DataFormatter dFormat;

    public XYLineChartDataset() {
//        System.out.println("Chart being created");
    }

    @Override
    public void createDataset(String name) {
        dFormat = DataFormatter.getInstance();

        if (HDFdataSelectMenu.HDF == null) {
            //CSV process
            if (CSVdataSelectMenu.CSV.getTimeStamp() && dFormat.time) {
                timeDataset(name);
            } else {
                basicDataset(name);
            }

        } else if (CSVdataSelectMenu.CSV == null) {
            //HDF5 process
            if (HDFdataSelectMenu.HDF.getTimeStamp() && dFormat.time) {
                timeDataset(name);
            } else {
                basicDataset(name);
            }
        }
    }

    @Override
    public void addDataset(String name) {
        dFormat = DataFormatter.getInstance();

        if (dFormat.getXDataset() == null) {
            this.d2 = new TimeSeriesCollection();
            TimeSeries series2 = new TimeSeries(name);
            Date[] x = dFormat.getXDateDataset();
            float[] y = dFormat.getYDataset();
            System.out.println(x[0]);

            for (int i = 0; i < x.length; i++) {
                try {
                    series2.addOrUpdate(new Second(x[i]), y[i]);
                } catch (SeriesException e) {
                    System.err.println("Error adding to series");
                    e.printStackTrace();
                    break;
                }
            }
            this.d2.addSeries(series2);
        } else {
            this.dataset2 = new XYSeriesCollection();
            XYSeries series2 = new XYSeries(name);
            float[] x = dFormat.getXDataset();
            float[] y = dFormat.getYDataset();

            for (int i = 0; i < x.length; i++) {
                try {
                    series2.add(x[i], y[i]);
                } catch (SeriesException e) {
                    System.err.println("Error adding to series");
                    e.printStackTrace();
                    break;
                }
            }
            this.dataset2.addSeries(series2);
        }
    }

    private void basicDataset(String name) {
        this.dataset = new XYSeriesCollection();

        XYSeries series1 = new XYSeries(name);

        float[] x = dFormat.getXDataset();
        float[] y = dFormat.getYDataset();

        for (int i = 0; i < x.length; i++) {
            try {
                series1.add(x[i], y[i]);
            } catch (SeriesException e) {
                System.err.println("Error adding to series");
                e.printStackTrace();
                break;
            }
        }
        this.dataset.addSeries(series1);
    }

    private void timeDataset(String name) {
        this.d = new TimeSeriesCollection();
        TimeSeries series1 = new TimeSeries(name);

        Date[] x;
        try {
            x = dFormat.getXDateDataset();
            float[] y = dFormat.getYDataset();

            for (int i = 0; i < x.length; i++) {
                try {
                    series1.addOrUpdate(new Second(x[i]), y[i]);
                } catch (SeriesException e) {
                    System.err.println("Error adding to series");
                    e.printStackTrace();
                    break;
                }
            }   
        } catch (Exception e) {
            System.err.println(e);
            ErrorDialog.wrongData();
        }
        this.d.addSeries(series1);
    }

    public XYSeriesCollection getDataset() {
        return dataset;
    }

    public XYSeriesCollection getDataset2() {
        return dataset2;
    }

    public TimeSeriesCollection getDateDataset() {
        return d;
    }

    public TimeSeriesCollection getDateDataset2() {
        return d2;
    }

}
