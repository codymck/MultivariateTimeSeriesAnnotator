package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.SeriesChangeListener;

/**
 *
 * @author Cody McKinney
 */
public class Controller {
    
    static AtomicReferenceArray<ArrayList<JFreeChart>> atomicList = new AtomicReferenceArray<>(new ArrayList[] { new ArrayList<JFreeChart>(6) });
    public static ArrayList<JFreeChart> synced;
    public static Map<XYPlot, ValueAxis> syncedPlots = new HashMap<>();
    static ValueAxis commonXAxis = new NumberAxis("X");
    //static NumberAxis commonYAxis = new NumberAxis("Y");
    static AnnotateChartPanel chartPanel;
    static Chart chartStruct;
    static int counter = 0;
    static ValueAxis FirstAxis;
    static ValueAxis TempAxis = new NumberAxis("T");
    
    
    public Controller(AnnotateChartPanel cP){
        this.synced = new ArrayList<>(6);
        this.chartPanel = cP;
    }
    
    
    //IGNORE FOR NOW
//    public static void sync(double x1, double x2, double y1, double y2){
//        //System.out.println("x1: " + x1 + " x2: " + x2);
//        commonXAxis.setRange(x1,x2);
//        //commonYAxis.setRange(y1,y2);
//        synced = atomicList.get(0);
//        System.out.println("Plot: " + synced.get(0) + "Size: " +  counter);
//        if (synced.contains(chartStruct.getPlot()) ){
//            for(XYPlot x : synced){
//                x.setDomainAxis(commonXAxis);
                //x.setRangeAxis(commonYAxis);
//                if(!(chartPanel.isFocusOwner()) && synced.size() <= 1){
//                    System.out.println(x.getPlotType() + " size: " +  synced.size());
//                }
//                else{
//                    x.setDomainAxis(commonXAxis);
//                    x.setRangeAxis(commonYAxis);
//                }
//            }
//        }
//        else if (synced != null && chartStruct != null){
//            for(XYPlot x : synced){
//                x.setDomainAxis(chartStruct.getDomainAxis());
//                x.setRangeAxis(chartStruct.getRangeAxis());
//            }
////            synced.get(0).setDomainAxis(chartStruct.getDomainAxis());
////            synced.get(0).setRangeAxis(chartStruct.getRangeAxis());
//        }
//    }
    
    
        public static void syncX(double x1, double x2, XYPlot p){
        commonXAxis.setRange(x1,x2);
        //synced = atomicList.get(0);
        
        System.out.println("Plot: " + syncedPlots.keySet().contains(p) + "Size: " +  counter);
        if (syncedPlots.keySet().contains(p)){
            for (Map.Entry<XYPlot,ValueAxis> e : syncedPlots.entrySet()){
                 System.out.println("Changing Sync");
                 e.getKey().setDomainAxis(commonXAxis);
//                 TempAxis.setRange(x1,x2);
//                 e.setValue(TempAxis);
             }
//            for(XYPlot x : syncedPlots.keySet()){
//                System.out.println("Changing Sync");
//                x.setDomainAxis(commonXAxis);
//            }
        }
        else{
             for (Map.Entry<XYPlot,ValueAxis> e : syncedPlots.entrySet()){
                 System.out.println("Undoing Sync");
                 e.getKey().setDomainAxis(e.getValue());
             }
//            for(XYPlot x : syncedPlots.keySet()){
//                System.out.println("Undoing Sync");
//                x.setDomainAxis(chartStruct.getDomainAxis());
//            }
        }
    }
        
    public static void syncX2(JFreeChart chart){
        //System.out.println("Syncing? " + chart);
        synced = atomicList.get(0);
        for(JFreeChart x : synced){
            if(x != chart){
                System.out.println("In loop " + x);
                //if(x.getXYPlot().getSeriesCount() == 1)
                x.getXYPlot().setDomainAxis(chart.getXYPlot().getDomainAxis());
                //x.fireChartChanged();
            }
        }
    }
    
//    public void addSync(XYPlot p, Chart cS){
//        ArrayList<XYPlot> list = atomicList.get(0);
//        list.add(p);
//        atomicList.set(0, list);
//        this.chartStruct = cS;
//        
//        ValueAxis xAxis = p.getDomainAxis();
//        syncedPlots.put(p, xAxis);
//        
//        if(counter == 0){
//            FirstAxis = xAxis;
//        }
//        else if(counter == 1){
//            double x3 = FirstAxis.getRange().getLowerBound();
//            double x4 = FirstAxis.getRange().getUpperBound();
//            commonXAxis.setRange(x3,x4);
//            p.setDomainAxis(commonXAxis);
//        }
//        else{
//            double x1 = xAxis.getRange().getLowerBound();
//            double x2 = xAxis.getRange().getUpperBound();
//            commonXAxis.setRange(x1,x2);
//            p.setDomainAxis(commonXAxis);
//        }
//        counter++;
//    }
//    
//    public void removeSync(XYPlot p){
//        //synced.remove(p);
//        syncedPlots.remove(p);
//        counter--;
//        if(counter < 1){
//            for(XYPlot x : syncedPlots.keySet()){
//                x.setDomainAxis(FirstAxis);
//                //x.setRangeAxis(chartStruct.getRangeAxis());
//            }
//        }
//    }
    
    public void removeSync2(JFreeChart c){
        synced = atomicList.get(0);
        synced.remove(c);
        atomicList.set(0, synced);
    }
    
    public void addSync2(JFreeChart c){
        c.getXYPlot().setDomainAxis(commonXAxis);
        ArrayList<JFreeChart> list = atomicList.get(0);
        list.add(c);
        atomicList.set(0, list);
        //synced.add(c);
    }
    
}
