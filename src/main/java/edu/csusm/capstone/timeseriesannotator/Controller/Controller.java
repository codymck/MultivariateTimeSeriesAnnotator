package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.Range;

/**
 *
 * @author Cody McKinney
 * @author Josef Arevalo
 */
public class Controller {

    static AtomicReferenceArray<Map<JFreeChart, AnnotateChartPanel>> atomicMap = new AtomicReferenceArray<>(new HashMap[]{new HashMap<JFreeChart, AnnotateChartPanel>(6)});
    static volatile Map<JFreeChart, AnnotateChartPanel> syncMap;
    static ValueAxis commonXAxis = new NumberAxis("Synced");
    static ValueAxis commonDateXAxis = new DateAxis("Synced");

    public Controller() {
        this.syncMap = new HashMap<>(6);
    }

    /*
    updates all the synced plots to changes that were made on a single chart in sync
     */
    public static void syncX(JFreeChart chart) {
        syncMap = atomicMap.get(0);
        syncMap.forEach((x, cS) -> {
            if (x != chart) {
                x.getXYPlot().setDomainAxis(chart.getXYPlot().getDomainAxis());
                cS.restoreAutoRangeBounds();
            }
        });
    }

    /*
    Removes the selected chart from the list of synced charts
     */
    public void removeSync(JFreeChart c) {
        syncMap = atomicMap.get(0);
        syncMap.remove(c);
        atomicMap.set(0, syncMap);

        if (!syncMap.isEmpty()) {
            Range r = syncMap.keySet().iterator().next().getXYPlot().getDomainAxis().getRange();
            commonXAxis.setRange(r);
        }
    }

    /*
    Adds the selected chart to the list of synced charts
     */
    public void addSync(JFreeChart c, AnnotateChartPanel cS, ChartStruct chartStruct) {
        if (chartStruct.getTimeStamp()) {
            c.getXYPlot().setDomainAxis(commonDateXAxis);
        } else {
            c.getXYPlot().setDomainAxis(commonXAxis);
        }
        syncMap.put(c, cS);
        atomicMap.set(0, syncMap);
    }
}
