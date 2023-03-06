package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.Range;
/**
 *
 * @author Cody McKinney
 */
public class Controller {
    static AtomicReferenceArray<Map<JFreeChart,AnnotateChartPanel>> atomicMap = new AtomicReferenceArray<>(new HashMap[] {new HashMap<JFreeChart,AnnotateChartPanel>(6) });
    static volatile  Map<JFreeChart, AnnotateChartPanel> syncMap; 
    static ValueAxis commonXAxis = new NumberAxis("X"); //refactor to be proper label
    
    public Controller(){
        this.syncMap = new HashMap<>(6);
    }
        
    public static void syncX(JFreeChart chart){
        syncMap = atomicMap.get(0); 
        syncMap.forEach((x, cS) -> {
            if (x != chart) {
                //System.out.println("in loop " + x.getXYPlot().getDatasetCount());
                x.getXYPlot().setDomainAxis(chart.getXYPlot().getDomainAxis());
                cS.restoreAutoRangeBounds();
            }
        });
    }
    
    public void removeSync(JFreeChart c){
        syncMap = atomicMap.get(0); 
        syncMap.remove(c);
        atomicMap.set(0, syncMap);
        
        if (!syncMap.isEmpty()) {
            Range r = syncMap.keySet().iterator().next().getXYPlot().getDomainAxis().getRange();
            commonXAxis.setRange(r);
        }
    }
    
    public void addSync(JFreeChart c, AnnotateChartPanel cS){
        c.getXYPlot().setDomainAxis(commonXAxis);
        
        syncMap.put(c,cS);
        atomicMap.set(0, syncMap);
    }
    
}
