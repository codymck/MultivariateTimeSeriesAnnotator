package edu.csusm.capstone.timeseriesannotator.Model;

import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5SimpleReader;
import edu.csusm.capstone.timeseriesannotator.Controller.HDF5Action;
import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;

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

    @Override
    public void buildDataList(String fileName) {
        this.file = fileName;
    }

    public void setPaths(String x, String y) {
        xP = x;
        yP = y;
        try ( IHDF5SimpleReader reader = HDF5Factory.openForReading(file)) {
            myXdata = reader.readFloatArray(x);
            myYdata = reader.readFloatArray(y);
        } catch (Exception e) {
            System.err.println(e);
            ErrorDialog.wrongData();
            HDF5Action.deleteInstance();
            HDFdataSelectMenu select = new HDFdataSelectMenu(new javax.swing.JFrame(), true);
            select.setVisible(true);
            HDF5Action hAction = HDF5Action.getInstance();
            this.setPaths(hAction.getXPath(), hAction.getYPath());
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
