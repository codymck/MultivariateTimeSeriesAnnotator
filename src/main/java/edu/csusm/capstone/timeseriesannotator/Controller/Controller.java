package edu.csusm.capstone.timeseriesannotator.Controller;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;

/**
 *
 * @author Cody McKinney
 */
public class Controller {
    
    static AtomicReferenceArray<ArrayList<JFreeChart>> atomicList = new AtomicReferenceArray<>(new ArrayList[] { new ArrayList<JFreeChart>(6) });
    public static ArrayList<JFreeChart> synced;
    static ValueAxis commonXAxis = new NumberAxis("X");
    
    public Controller(){
        this.synced = new ArrayList<>(6);
    }
        
    public static void syncX(JFreeChart chart){
        synced = atomicList.get(0);
        for(JFreeChart x : synced){
            if(x != chart){
                x.getXYPlot().setDomainAxis(chart.getXYPlot().getDomainAxis());
            }
        }
    }
    
    public void removeSync(JFreeChart c){
        synced = atomicList.get(0);
        synced.remove(c);
        atomicList.set(0, synced);
    }
    
    public void addSync(JFreeChart c){
        c.getXYPlot().setDomainAxis(commonXAxis);
        ArrayList<JFreeChart> list = atomicList.get(0);
        list.add(c);
        atomicList.set(0, list);
    }
    
}
