/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author josef
 */
public class ChartAction implements ActionListener {

    JDialog dialog;
    int select;
    
    private static ChartAction instance;

    public static ChartAction getInstance() {
        if (instance == null) {
            System.err.println("ChartAction has not been initialized");
        }
        return instance;
    }
    
    public ChartAction(JDialog w ,int s) {
        this.dialog = w;
        this.select = s;
        ChartAction.instance = this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.dispose();
    }
    
    public int getType(){
        return select;
    }
    
}
