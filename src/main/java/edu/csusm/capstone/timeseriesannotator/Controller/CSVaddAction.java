package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/**
 *
 * @author josef
 */
public class CSVaddAction implements ActionListener {
    JDialog dialog;
    int yaxis;
    String y;
    private javax.swing.JList<String> YAxisList;

    private static CSVaddAction instance;

    public static CSVaddAction getInstance() {
        if (instance == null) {
            System.err.println("CSVAction has not been initialized");
        }
        return instance;
    }

    public CSVaddAction(JDialog w, javax.swing.JList<String> YList) {
        this.dialog = w;
        this.YAxisList = YList;
        CSVaddAction.instance = this;
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        yaxis = YAxisList.getSelectedIndex();
        y = YAxisList.getSelectedValue();
//        System.out.println("CSVAction: Selected Axis --- Y-Axis: " + yaxis);
        if (yaxis == -1 ) {
            ErrorDialog.badIndex();
        } else {
            dialog.dispose();
        }
    }

    public int getYAxis() {
        return yaxis;
    }
    
}
