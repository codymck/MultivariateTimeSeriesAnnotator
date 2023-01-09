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
public class CSVaddAction implements ActionListener {
    JDialog dialog;
    int yaxis;
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

        System.out.println("CSVAction: Selected Axis --- Y-Axis: " + yaxis);
        if (yaxis == -1 ) {
            badIndex();
        } else {
            dialog.dispose();
        }
    }

    public void badIndex() {
        JFrame bFrame = new JFrame();
        JOptionPane.showMessageDialog(bFrame, "Select valid indexes", "Error", HEIGHT);
    }

    public int getYAxis() {
        return yaxis;
    }
    
}
