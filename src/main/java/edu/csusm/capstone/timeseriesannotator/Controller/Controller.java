package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.ChartChangeListener;

/**
 *
 * @author Cody McKinney
 */
public class Controller {
    
    static AtomicReferenceArray<ArrayList<JFreeChart>> atomicList = new AtomicReferenceArray<>(new ArrayList[] { new ArrayList<JFreeChart>(6) });
    static AtomicReferenceArray<Map<JFreeChart,AnnotateChartPanel>> atomicMap = new AtomicReferenceArray<>(new HashMap[] {new HashMap<JFreeChart,AnnotateChartPanel>(6) });
    static volatile  Map<JFreeChart, AnnotateChartPanel> syncMap; 
    public static ArrayList<JFreeChart> synced;
    static ValueAxis commonXAxis = new NumberAxis("X");
    
    public Controller(){
        this.synced = new ArrayList<>(6);
        this.syncMap = new HashMap<>(6);
    }
        
    public static void syncX(JFreeChart chart, ChartChangeListener L){
        syncMap = atomicMap.get(0);
        //syncMap.remove(chart);
        
       synchronized(chart){
           chart.removeChangeListener(L);
            try{
                syncMap.forEach((x, cS) -> {
                    if(x != chart){
                        System.out.println("in loop " + cS.getCursor());
                        x.getXYPlot().setDomainAxis(chart.getXYPlot().getDomainAxis());
                        cS.restoreAutoRangeBounds();
                    }
                });
            }finally{
                chart.addChangeListener(L);
                //chart.fireChartChanged();
            }
        }
    }
    
    public void restoreY(JFreeChart chart){
        syncMap = atomicMap.get(0);
        syncMap.remove(chart);
        syncMap.forEach((x, cS) -> {
            cS.restoreAutoRangeBounds();
        });
    }
    
    public void removeSync(JFreeChart c){
        syncMap = atomicMap.get(0); 
        syncMap.remove(c);
        atomicMap.set(0, syncMap);
        
        synced = atomicList.get(0);
        synced.remove(c);
        atomicList.set(0, synced);
    }
    
    public void addSync(JFreeChart c, AnnotateChartPanel cS){
        c.getXYPlot().setDomainAxis(commonXAxis);
        ArrayList<JFreeChart> list = atomicList.get(0);
        
        syncMap.put(c,cS);
        atomicMap.set(0, syncMap);
        
        list.add(c);
        atomicList.set(0, list);
    }
    
}
