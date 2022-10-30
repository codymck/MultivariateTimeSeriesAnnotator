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
public class HDF5Action implements ActionListener {
    JDialog dialog;
    String xaxis;
    String yaxis;
    
    public HDF5Action(JDialog w, javax.swing.JTextField Xaxispath, javax.swing.JTextField Yaxispath) {
        this.dialog = w;
        xaxis = Xaxispath.getText();
        yaxis = Yaxispath.getText();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Y-Axis: " + yaxis + " X-Axis: " + xaxis);
        if(yaxis.isBlank() || xaxis.isEmpty()) {
            badPath();
        }
        
        dialog.dispose();
    }
    
    public void badPath(){
        JFrame bFrame = new JFrame();
        JOptionPane.showMessageDialog(bFrame, "Enter Valid Path", "Error", HEIGHT);
    }
    
}
