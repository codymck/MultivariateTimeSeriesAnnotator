/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import edu.csusm.capstone.timeseriesannotator.View.ChartDisplay;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author josef
 */
public class AddChartAction implements ActionListener {
    
    ChartDisplay chartDisplay;
    ArrayList<ChartDisplay> charts;
    AppFrame frame;
    
    public AddChartAction(AppFrame f, ArrayList<ChartDisplay> c){
        this.frame = f;
        this.charts = c;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (charts.size() < 6) {
            chartDisplay = new ChartDisplay(frame);
            charts.add(chartDisplay);
            frame.addChart(charts);
        }
        else{
            //add too many charts error message
        }
    }
    
    
    
}
