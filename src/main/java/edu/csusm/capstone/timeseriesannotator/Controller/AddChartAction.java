/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import edu.csusm.capstone.timeseriesannotator.View.ChartDisplay;
import edu.csusm.capstone.timeseriesannotator.View.MultiSplitPane;
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
    MultiSplitPane split;
    
    public AddChartAction(MultiSplitPane s, ArrayList<ChartDisplay> c, AppFrame f){
        this.split = s;
        this.charts = c;
        this.frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (charts.size() < 6) {
            chartDisplay = new ChartDisplay(frame);
            charts.add(chartDisplay);
            split.addComponent(chartDisplay);
            frame.addChart(charts);
        }
        else{
            //add too many charts error message
        }
    }
    
    
    
}
