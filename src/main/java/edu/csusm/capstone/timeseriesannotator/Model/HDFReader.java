package edu.csusm.capstone.timeseriesannotator.Model;

import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5Reader;
import ch.systemsx.cisd.hdf5.IHDF5SimpleReader;
import edu.csusm.capstone.timeseriesannotator.Controller.Chart;
import edu.csusm.capstone.timeseriesannotator.Controller.HDF5Action;
import edu.csusm.capstone.timeseriesannotator.Controller.HDF5addAction;
import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import edu.csusm.capstone.timeseriesannotator.View.HDF5addSeries;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import java.util.List;

/**
 *
 * @author Cody McKinney
 */
public class HDFReader implements DataReader {

    float[] myXdata;
    float[] myYdata;
    String file;
    public static String xP;
    public static String yP;
    Chart chartStruct = Chart.getInstance();

    @Override
    public void buildDataList(String fileName) {
        this.file = fileName;
    }

    public void setPaths(String x, String y, int flag) {
        xP = x;
        yP = y;
        IHDF5Reader reader2 = HDF5Factory.openForReading(file);
        try ( IHDF5SimpleReader reader = HDF5Factory.openForReading(file)) {
            List<String> headers = reader2.getGroupMembers("00000/");
            System.out.println("Headers: " + headers);
            myXdata = reader.readFloatArray(x);
            myYdata = reader.readFloatArray(y);
        } catch (Exception e) {
            System.err.println(e);
            ErrorDialog.wrongData();
            
            if (flag == 0) {
                HDF5Action.deleteInstance();
                HDFdataSelectMenu select = new HDFdataSelectMenu(new javax.swing.JFrame(), true);
                select.setVisible(true);
                HDF5Action hAction = HDF5Action.getInstance();
                this.setPaths(hAction.getXPath(), hAction.getYPath(), 0);
            } else {
                HDF5addAction.deleteInstance();
                HDF5addSeries select = new HDF5addSeries(new javax.swing.JFrame(), true);
                select.setVisible(true);
                HDF5addAction hAction = HDF5addAction.getInstance();
                this.setPaths(chartStruct.getXpath(), hAction.getYPath(), 1);
            }
            
            return;
        }
        // OUTPUT FOR DATA 
//        System.out.print("X :   ");
//        for (float d : myXdata) {
//            System.out.print(d + " , ");
//        }
//        System.out.println();
//        System.out.print("Y :   ");
//        for (float r : myYdata) {
//            System.out.print(r + " , ");
//        }


    }

    public float[] getXData() {
        return myXdata;
    }

    public float[] getYData() {
        return myYdata;
    }
}
