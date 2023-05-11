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
    int xaxis;
    String y;
    boolean selected = false;
    private javax.swing.JList<String> YAxisList;
    ChartStruct chartStruct;

    private static CSVaddAction instance;

    public static CSVaddAction getInstance() {
        if (instance == null) {
            System.err.println("CSVAction has not been initialized");
        }
        return instance;
    }

    public CSVaddAction(JDialog w, javax.swing.JList<String> YList, ChartStruct c) {
        this.dialog = w;
        this.YAxisList = YList;
        this.chartStruct = c;
        CSVaddAction.instance = this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        yaxis = YAxisList.getSelectedIndex();
        y = YAxisList.getSelectedValue();
        if (yaxis == -1 || yaxis == chartStruct.getXaxis()) {
            ErrorDialog.badIndex();
            selected = false;
        } else {
            dialog.dispose();
            selected = true;
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setXAxis(int x) {
        xaxis = x;
    }

    public int getYAxis() {
        return yaxis;
    }

}
