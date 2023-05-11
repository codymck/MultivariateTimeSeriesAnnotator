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
public class HDF5Action implements ActionListener {

    JDialog dialog;
    String xaxis;
    String yaxis;
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
        if (yaxis.isBlank() || xaxis.isBlank()) {
            badPath();
        } else {
            dialog.dispose();
        }
    }

    public void badPath() {
        JFrame bFrame = new JFrame();
        bFrame.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(bFrame, "Enter Valid Path", "Error", HEIGHT);

    }

    public String getXPath() {
        return xaxis;
    }

    public String getYPath() {
        return yaxis;
    }
}
