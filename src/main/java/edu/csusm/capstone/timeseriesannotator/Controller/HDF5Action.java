package edu.csusm.capstone.timeseriesannotator.Controller;

import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5SimpleReader;
import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import edu.csusm.capstone.timeseriesannotator.View.HDFdataSelectMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.image.ImageObserver.HEIGHT;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
        } else if (flag == 0) {
            try ( IHDF5SimpleReader reader = HDF5Factory.openForReading(chartStruct.getFileName())) {
                flag = 1;
            } catch (Exception ex) {
                System.err.println(ex);
                ErrorDialog.wrongData();
                //HDF5Action.deleteInstance();
                //HDFdataSelectMenu select = new HDFdataSelectMenu(new javax.swing.JFrame(), true);
                //select.setVisible(true);
                //HDF5Action hAction = HDF5Action.getInstance();
                //this.setPaths(hAction.getXPath(), hAction.getYPath());
                // return;
            }

        } 
        if(flag == 1){
            dialog.dispose();
        }
    }

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
