package edu.csusm.capstone.timeseriesannotator.Model;

import edu.csusm.capstone.timeseriesannotator.Controller.ChartStruct;
import edu.csusm.capstone.timeseriesannotator.View.CSVdataSelectMenu;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author Cody McKinney
 * @author Josef Arevalo
 */
public class DataFormatter {

    DataReader dReader;
    public static DataFormatter dF;
    //sets the epoch of program to be at 2015
    static final long EPOCH = new java.util.Date(2015 - 1900, Calendar.JANUARY, 1).getTime(); // 2015/1/1 in GMT

    //holds value to plot onto axis
    float[] xData;
    float[] yData;

    //holds value to plot datetimestamp on x-axis
    Date[] xDateData;

    ChartStruct chartStruct;

    public DataFormatter(DataReader dR, ChartStruct cS) {
        if (dR instanceof CSVReader) {
            dReader = (CSVReader) dR;
        } else if (dR instanceof HDFReader) {
            dReader = (HDFReader) dR;
        }
        dF = this;
        chartStruct = cS;
    }

    //Singleton instance of this class
    public static DataFormatter getInstance() {
        return dF;
    }

    /*
    Gets index of the x and y headers from user input then uses it to format
    the data into proper float values. If use selects the button to indicate
    that the x-axis is timestampdata then x-values are converted into Date values.
     */
    public void formatCSV(int xIndex, int yIndex) {
        String[] keys = ((CSVReader) dReader).getHeaders();

        Float[] tempX = ((CSVReader) dReader).getColumn(keys[xIndex]);
        Float[] tempY = ((CSVReader) dReader).getColumn(keys[yIndex]);

        xData = ArrayUtils.toPrimitive(tempX);
        yData = ArrayUtils.toPrimitive(tempY);

        if (CSVdataSelectMenu.CSV != null) {
            if (CSVdataSelectMenu.CSV.getTimeStamp()) {
                xDateData = new Date[tempX.length];
                for (int x = 0; x < tempX.length; x++) {
                    int t = (int) xData[x];
                    xDateData[x] = new java.util.Date(t * 1000 + EPOCH);
                }
                chartStruct.setTimeStamp(true);
            }
        } else if (chartStruct.getTimeStamp()) {
            xDateData = new Date[tempX.length];
            for (int x = 0; x < tempX.length; x++) {
                int t = (int) xData[x];
                xDateData[x] = new java.util.Date(t * 1000 + EPOCH);
            }
        }
    }

    /*
    Gets string path of the x and y from user input then uses it to format
    the data into float values based on file data type. If use selects the button to 
    indicate that the x-axis is timestampdata and the values are of integer value
    then x-values are converted into Date values.
     */
    public void formatHDF5(String xPath, String yPath) {
        switch (((HDFReader) dReader).getXType()) {
            case "INTEGER" -> {

                int[] t = ((HDFReader) dReader).getIntXData();

                if (HDFdataSelectMenu.HDF != null) {
                    if (HDFdataSelectMenu.HDF.getTimeStamp()) {
                        xDateData = new Date[t.length];
                        for (int x = 0; x < t.length; x++) {
                            xDateData[x] = new java.util.Date(t[x] * 1000 + EPOCH);
                        }
                        chartStruct.setTimeStamp(true);
                    } else if (chartStruct.getTimeStamp()) {
                        xDateData = new Date[t.length];
                        for (int x = 0; x < t.length; x++) {
                            xDateData[x] = new java.util.Date(t[x] * 1000 + EPOCH);
                        }
                    } else {
                        xData = new float[t.length];
                        for (int x = 0; x < t.length; x++) {
                            xData[x] = (float) t[x];
                        }
                        chartStruct.setTimeStamp(false);
                    }
                }

                break;
            }
            case "FLOAT" -> {
                xData = ((HDFReader) dReader).getXData();
                break;
            }
            case "DOUBLE" -> {
                double[] t = ((HDFReader) dReader).getDoubleXData();
                for (int x = 0; x < t.length; x++) {
                    xData[x] = (float) t[x];
                }
                break;
            }
        }
        switch (((HDFReader) dReader).getYType()) {
            case "INTEGER" -> {

                int[] t2 = ((HDFReader) dReader).getIntYData();
                yData = new float[t2.length];

                for (int x = 0; x < t2.length; x++) {
                    yData[x] = (float) t2[x];
                }

                break;
            }
            case "FLOAT" -> {
                yData = ((HDFReader) dReader).getYData();
                break;
            }
            case "DOUBLE" -> {
                double[] t2 = ((HDFReader) dReader).getDoubleYData();
                for (int x = 0; x < t2.length; x++) {
                    yData[x] = (float) t2[x];
                }
                break;
            }
        }
    }

    //returns float array of X-axis
    public float[] getXDataset() {
        return xData;
    }

    //returns float array of Y-axis
    public float[] getYDataset() {
        return yData;
    }

    //returns Date array of X-Axis
    public Date[] getXDateDataset() {
        return xDateData;
    }
}
