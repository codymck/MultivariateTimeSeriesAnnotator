package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/**
 *
 * @author josef
 */
public class CSVAddAction implements ActionListener {
    JDialog dialog;
    int yaxis;
    int xaxis;
    String y;
    boolean selected = false;
    private javax.swing.JList<String> YAxisList;
    ChartStruct chartStruct;

    private static CSVAddAction instance;

    public static CSVAddAction getInstance() {
        if (instance == null) {
            System.err.println("CSVAction has not been initialized");
        }
        return instance;
    }

    public CSVAddAction(JDialog w, javax.swing.JList<String> YList, ChartStruct c) {
        this.dialog = w;
        this.YAxisList = YList;
        this.chartStruct = c;
        CSVAddAction.instance = this;
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        yaxis = YAxisList.getSelectedIndex();
        y = YAxisList.getSelectedValue();
        System.out.println("CSVAction: Selected Axis --- Y-Axis: " + yaxis);
        if (yaxis == -1 || yaxis == chartStruct.getXaxis()) {
            ErrorDialog.badIndex();
            selected = false;
        } else {
            dialog.dispose();
            selected = true;
        }
    }
    
    public boolean isSelected(){
        return selected;
    }
    
    public void setXAxis(int x){
        xaxis = x;
    }

    public int getYAxis() {
        return yaxis;
    }
    
}
