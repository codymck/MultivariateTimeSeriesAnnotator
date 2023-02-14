package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author Cody McKinney
 */
public class Controller {
    
    static AtomicReferenceArray<ArrayList<XYPlot>> atomicList = new AtomicReferenceArray<>(new ArrayList[] { new ArrayList<XYPlot>() });
    public static ArrayList<XYPlot> synced;
    static NumberAxis commonXAxis = new NumberAxis("X");
    //static NumberAxis commonYAxis = new NumberAxis("Y");
    static AnnotateChartPanel chartPanel;
    Chart chartStruct;
    static int counter = 0;
    static ValueAxis FirstAxis;
    
    
    public Controller(AnnotateChartPanel cP){
        this.synced = new ArrayList<>(6);
        this.chartPanel = cP;
    }
    
    public static void sync(double x1, double x2, double y1, double y2){
        //System.out.println("x1: " + x1 + " x2: " + x2);
        commonXAxis.setRange(x1,x2);
        //commonYAxis.setRange(y1,y2);
        synced = atomicList.get(0);
        //System.out.println("Plot: " + synced.get(0) + "Size: " +  synced.size());
        if (counter > 1){
            for(XYPlot x : synced){
                x.setDomainAxis(commonXAxis);
                //x.setRangeAxis(commonYAxis);
//                if(!(chartPanel.isFocusOwner()) && synced.size() <= 1){
//                    System.out.println(x.getPlotType() + " size: " +  synced.size());
//                }
//                else{
//                    x.setDomainAxis(commonXAxis);
//                    x.setRangeAxis(commonYAxis);
//                }
            }
        }
//        else if (synced != null && chartStruct != null){
//            for(XYPlot x : synced){
//                x.setDomainAxis(chartStruct.getDomainAxis());
//                x.setRangeAxis(chartStruct.getRangeAxis());
//            }
////            synced.get(0).setDomainAxis(chartStruct.getDomainAxis());
////            synced.get(0).setRangeAxis(chartStruct.getRangeAxis());
//        }
    }
    
    public void addSync(XYPlot p, Chart cS){
        ArrayList<XYPlot> list = atomicList.get(0);
        list.add(p);
        atomicList.set(0, list);
        this.chartStruct = cS;
        ValueAxis xAxis = p.getDomainAxis();
        if(counter == 0){
            FirstAxis = xAxis;
        }
        double x1 = xAxis.getRange().getLowerBound();
        double x2 = xAxis.getRange().getUpperBound();
        commonXAxis.setRange(x1,x2);
        p.setDomainAxis(commonXAxis);
        counter++;
    }
    
    public void removeSync(XYPlot p){
        synced.remove(p);
        counter--;
        if(counter < 1){
            for(XYPlot x : synced){
                x.setDomainAxis(FirstAxis);
                //x.setRangeAxis(chartStruct.getRangeAxis());
            }
        }
    }
    
}
