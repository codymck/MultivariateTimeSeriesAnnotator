package edu.csusm.capstone.timeseriesannotator.Model;

import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5Reader;
import ch.systemsx.cisd.hdf5.IHDF5SimpleReader;
import edu.csusm.capstone.timeseriesannotator.Controller.ChartStruct;
import edu.csusm.capstone.timeseriesannotator.Controller.HDF5Action;
import edu.csusm.capstone.timeseriesannotator.Controller.HDF5addAction;
import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import edu.csusm.capstone.timeseriesannotator.View.HDF5addSeries;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Cody McKinney
 */
public class HDFReader implements DataReader {

    private ArrayList<String> dataPaths;
    //String currentPath = "/";
    
    float[] myXdata;
    float[] myYdata;
    
    double[] doublemyXdata;
    double[] doublemyYdata;
    
    Date[] datemyXdata;
    Date[] datemyYdata;
    
    String file;
    String type;
    public static String xP;
    public static String yP;
    ChartStruct chartStruct;// = ChartStruct.getInstance();

    @Override
    public void buildDataList(String fileName) {
        this.file = fileName;
    }

    public void setPaths(String x, String y, int flag) {
        xP = x;
        yP = y;
        //IHDF5Reader reader2 = HDF5Factory.openForReading(file);
        try (IHDF5SimpleReader reader = HDF5Factory.openForReading(file)) {
            //List<String> headers = reader2.getGroupMembers("/user1");
            //System.out.println("Headers: " + headers);
            type = reader.getDataSetInformation(x).getTypeInformation().toString().toUpperCase();
                        
            if(type.contains("FLOAT")){
                myXdata = reader.readFloatArray(x);
                myYdata = reader.readFloatArray(y);
                type = "FLOAT";
            }
            if(type.contains("DOUBLE")){
                doublemyXdata = reader.readDoubleArray(x);
                doublemyYdata = reader.readDoubleArray(y);
                type = "DOUBLE";
            }
            if(type.contains("DATE")){
                datemyXdata = reader.readDateArray(x);
                datemyYdata = reader.readDateArray(y);
                type = "DATE";
            }
            
            
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

    public List<String> buildPath(String currentPath) {

        //dataPaths = new ArrayList<String>();
        //List<String> values;

        try (IHDF5Reader reader2 = HDF5Factory.openForReading(file)) {
            //values =
            if(currentPath.endsWith("/")){
               return reader2.getGroupMembers(currentPath);
            }else{
               return reader2.getGroupMembers(currentPath + "/");
            }
            
        } catch (Exception e) {
            System.err.println(e);
            //ErrorDialog.wrongData();
        }
        return null;

    }
    
    public String getType(){
        return type;
    }

    
    public float[] getXData() {
        return myXdata;
    }

    public float[] getYData() {
        return myYdata;
    }
    
    public double[] getDoubleXData() {
        return doublemyXdata;
    }

    public double[] getDoubleYData() {
        return doublemyYdata;
    }
    
    public Date[] getDateXData() {
        return datemyXdata;
    }

    public Date[] getDateYData() {
        return datemyYdata;
    }
}
