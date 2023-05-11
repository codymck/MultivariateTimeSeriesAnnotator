package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import edu.csusm.capstone.timeseriesannotator.View.ChartDisplay;
import edu.csusm.capstone.timeseriesannotator.View.ErrorDialog;
import edu.csusm.capstone.timeseriesannotator.View.MultiSplitPane;
import java.util.ArrayList;

/**
 *
 * @author josef
 */
public class AddChartAction {

    ChartDisplay chartDisplay;
    ArrayList<ChartDisplay> charts;
    AppFrame frame;
    MultiSplitPane split;

    public AddChartAction(MultiSplitPane s, ArrayList<ChartDisplay> c, AppFrame f) {
        this.split = s;
        this.charts = c;
        this.frame = f;

        if (charts.size() < 6) {
            chartDisplay = new ChartDisplay(frame);
            charts.add(chartDisplay);
            split.addComponent(chartDisplay);
            frame.addChart(charts);
        } else {
            ErrorDialog.TooManyCharts();
        }
    }
}
