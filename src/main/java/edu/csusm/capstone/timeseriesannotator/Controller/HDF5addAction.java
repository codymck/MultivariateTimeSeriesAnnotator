package edu.csusm.capstone.timeseriesannotator.Controller;

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
public class HDF5addAction implements ActionListener {
    JDialog dialog;
    String yaxis;

    private static HDF5addAction instance;

    public static HDF5addAction getInstance() {
        if (instance == null) {
            System.err.println("CSVAction has not been initialized");
        }
        return instance;
    }
    
    public static void deleteInstance() {
        instance = null;
    }

    public HDF5addAction(JDialog w, javax.swing.JTextField Yaxispath) {
        this.dialog = w;
        yaxis = Yaxispath.getText();
        HDF5addAction.instance = this;
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
//        System.out.println("HDF5Action: Selected Axis --- Y-Axis: " + yaxis);
        if(yaxis.isBlank()) {
            badPath();
        }else{
            dialog.dispose();
        }
    }

    public void badPath(){
        JFrame bFrame = new JFrame();
        JOptionPane.showMessageDialog(bFrame, "Enter Valid Path", "Error", HEIGHT);
    }
   

    public String getYPath() {
        return yaxis;
    }
    
}
