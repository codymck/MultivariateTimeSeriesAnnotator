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
public class HDF5AddAction implements ActionListener {

    JDialog dialog;
    String yaxis;

    private static HDF5AddAction instance;

    public static HDF5AddAction getInstance() {
        if (instance == null) {
            System.err.println("CSVAction has not been initialized");
        }
        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }

    public HDF5AddAction(JDialog w, javax.swing.JTextField Yaxispath) {
        this.dialog = w;
        yaxis = Yaxispath.getText();
        HDF5AddAction.instance = this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (yaxis.isBlank()) {
            badPath();
        } else {
            dialog.dispose();
        }
    }

    public void badPath() {
        JFrame bFrame = new JFrame();
        JOptionPane.showMessageDialog(bFrame, "Enter Valid Path", "Error", HEIGHT);
    }

    public String getYPath() {
        return yaxis;
    }

}
