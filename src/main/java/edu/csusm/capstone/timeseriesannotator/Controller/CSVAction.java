/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
public class CSVAction implements ActionListener {

    JDialog dialog;
    int xaxis;
    int yaxis;
    private javax.swing.JList<String> XAxisList;
    private javax.swing.JList<String> YAxisList;

    private static CSVAction instance;

    public static CSVAction getInstance() {
        if (instance == null) {
            System.err.println("CSVAction has not been initialized");
        }
        return instance;
    }

    public CSVAction(JDialog w, javax.swing.JList<String> XList, javax.swing.JList<String> YList) {
        this.dialog = w;
        this.XAxisList = XList;
        this.YAxisList = YList;
        CSVAction.instance = this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        xaxis = XAxisList.getSelectedIndex();
        yaxis = YAxisList.getSelectedIndex();

        System.out.println("CSVAction: Selected Axis --- X-Axis: " + xaxis + "   Y-Axis: " + yaxis);
        if (yaxis == -1 || xaxis == -1 || xaxis == yaxis) {
            badIndex();
        } else {
            dialog.dispose();
        }
    }

    public void badIndex() {
        JFrame bFrame = new JFrame();
        JOptionPane.showMessageDialog(bFrame, "Select valid indexes", "Error", HEIGHT);
    }

    public int getXAxis() {
        return xaxis;
    }

    public int getYAxis() {
        return yaxis;
    }

}
