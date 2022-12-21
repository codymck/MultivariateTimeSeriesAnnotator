package edu.csusm.capstone.timeseriesannotator.Model;

import org.jfree.data.general.SeriesException;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYLineChartDataset implements ChartData {

    public XYSeriesCollection dataset;
    DataFormatter dFormat;
    
    public XYLineChartDataset() {
        System.out.println("Chart being created");
    }

    @Override
    public void createDataset() {
        dFormat = DataFormatter.getInstance();
        
        this.dataset = new XYSeriesCollection();//need to reference for new series

        XYSeries series1 = new XYSeries("test");

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
    
    @Override
    public void addDataset() {
        dFormat = DataFormatter.getInstance();
        
        //dataset = new XYSeriesCollection();//need to reference for new series

        XYSeries series2 = new XYSeries("test");

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

        this.dataset.addSeries(series2);

    }
    
    public XYSeriesCollection getDataset() {
        return dataset;
    }

}
