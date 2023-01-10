package edu.csusm.capstone.timeseriesannotator.Controller;

import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5SimpleReader;
import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import hdf.hdf5lib.H5;
import hdf.hdf5lib.HDF5Constants;
import hdf.hdf5lib.structs.H5G_info_t;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.image.ImageObserver.HEIGHT;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

/**
 *
 * @author josef
 */
public class HDF5Action implements ActionListener {

    JDialog dialog;
    String xaxis;
    String yaxis;
    Chart chartStruct = Chart.getInstance();
    int flag = 0;

    private static HDF5Action instance;

    public static HDF5Action getInstance() {
        if (instance == null) {
            System.err.println("HDF5Action has not been initialized");
        }
        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }

    public HDF5Action(JDialog w, javax.swing.JTextField Xaxispath, javax.swing.JTextField Yaxispath) {
        this.dialog = w;
        xaxis = Xaxispath.getText();
        yaxis = Yaxispath.getText();
        instance = this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("HDF5Action: Selected Axis --- X-Axis: " + xaxis + "   Y-Axis: " + yaxis);
        if (yaxis.isBlank() || xaxis.isBlank()) {
            badPath();
        }else{
            dialog.dispose();
        }
        
        
        
//        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
//        File file = new File(chartStruct.getFileName());
//        H5File h5file = null;
//        try {
//            h5file = (H5File) fileFormat.createFile(file.getPath(), FileFormat.FILE_CREATE_OPEN);
//        } catch (Exception ex) {
//            Logger.getLogger(HDF5Action.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        try {
//            System.out.print("\n\nfuckerr\n\n");
//            h5file.close();
//        } catch (HDF5Exception ex) {
//            Logger.getLogger(HDF5Action.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        
//        long file_id = -1;
//        int group_id = -1;
//
//
//        // Open file using the default properties.
//        try {
//            file_id = H5.H5Fopen(chartStruct.getFileName(), HDF5Constants.H5F_ACC_RDWR, HDF5Constants.H5P_DEFAULT);
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        
//        // Open the group, obtaining a new handle.
//        try {
//            if (file_id >= 0)
//                group_id = (int) H5.H5Gopen(file_id, "/", HDF5Constants.H5P_DEFAULT);
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            printGroup(group_id, "/", "");
//        } catch (Exception ex) {
//            Logger.getLogger(HDF5Action.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        // Close the group.
//        try {
//            if (group_id >= 0)
//                H5.H5Gclose(group_id);
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        // Close the file.
//        try {
//            if (file_id >= 0)
//                H5.H5Fclose(file_id);
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        } else if (flag == 0) {
//            
//            try ( IHDF5SimpleReader reader = HDF5Factory.openForReading(chartStruct.getFileName())) {
//                DataSetInformation ds = reader.getDataSetInformation(chartStruct.getFileName())
//                flag = 1;
//            } catch (Exception ex) {
//                System.err.println(ex);
//                ErrorDialog.wrongData();
//                //HDF5Action.deleteInstance();
//                //HDFdataSelectMenu select = new HDFdataSelectMenu(new javax.swing.JFrame(), true);
//                //select.setVisible(true);
//                //HDF5Action hAction = HDF5Action.getInstance();
//                //this.setPaths(hAction.getXPath(), hAction.getYPath());
//                // return;
//            }
//
//        } 
//        if(flag == 1){
//            dialog.dispose();
//        }
    }
    
    
//    private static void printGroup(int g_id, String gname, String indent) throws Exception {
//        if (g_id < 0) return;
//
//        H5G_info_t members = H5.H5Gget_info(g_id);
//        String objNames[] = new String[(int) members.nlinks];
//        int objTypes[] = new int[(int) members.nlinks];
//        int lnkTypes[] = new int[(int) members.nlinks];
//        long objRefs[] = new long[(int) members.nlinks];
//        int names_found = 0;
//        try {
//            names_found = H5.H5Gget_obj_info_all(g_id, null, objNames,
//                    objTypes, lnkTypes, objRefs, HDF5Constants.H5_INDEX_NAME);
//        }
//        catch (Throwable err) {
//            err.printStackTrace();
//        }
//
//        indent += "    ";
//        for (int i = 0; i < names_found; i++) {
//            System.out.println(indent + objNames[i]);
//            int group_id = -1;
//            if (objTypes[i]==HDF5Constants.H5O_TYPE_GROUP) {
//                // Open the group, obtaining a new handle.
//                try {
//                    if (g_id >= 0)
//                        group_id = (int) H5.H5Gopen(g_id, objNames[i], HDF5Constants.H5P_DEFAULT);
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (group_id >= 0)
//                    printGroup(group_id, objNames[i], indent);
//                
//                // Close the group. 
//                try {
//                    if (group_id >= 0)
//                        H5.H5Gclose(group_id);
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    
    

    public void badPath() {
        JFrame bFrame = new JFrame();
        JOptionPane.showMessageDialog(bFrame, "Enter Valid Path", "Error", HEIGHT);
    }

    public String getXPath() {
        return xaxis;
    }

    public String getYPath() {
        return yaxis;
    }

}
