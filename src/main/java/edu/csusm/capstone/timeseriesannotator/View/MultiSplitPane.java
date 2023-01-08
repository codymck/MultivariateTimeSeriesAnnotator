package edu.csusm.capstone.timeseriesannotator.View;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JPanel;

import javax.swing.JSplitPane;

/**
 *
 * @author Cody McKinney
 * @author Ben Theurich
 */
public class MultiSplitPane extends JPanel {

    private ArrayList<ChartDisplay> chartList = new ArrayList<ChartDisplay>();
    private final int division = 6;

    /**
     * Builds MultiSplitPane
     */
    public MultiSplitPane() {
        super();
        this.setBorder(null);
        this.setLayout(new java.awt.BorderLayout());
    }

    /**
     * @param c - component to add
     */
    public void addComponent(ChartDisplay c) {
        chartList.add(c);
        formatSplitPane();
    }
    
    /**
     * @param orientation - orientation of the split pane
     * @param pane1 - what goes in the top/left pane
     * @param pane2 - what goes in the right/bottom pane
     * @param resizeWeight - how to divide the weight between the split panes
     */
    private JSplitPane createSplitPane(int orientation, Component pane1, Component pane2, double resizeWeight){
        JSplitPane container = new JSplitPane();
        container.setOrientation(orientation);
        container.setTopComponent(pane1);
        container.setBottomComponent(pane2);
        container.setDividerSize(division);
        container.setBorder(null);
        container.setResizeWeight(resizeWeight);
        
        return container;
    }

    /**
     * Formats the panes as intended whenever one is added or deleted
     */
    private void formatSplitPane() {
        this.removeAll();
        switch (chartList.size()) {
            case 1 ->
                this.add(chartList.get(0));
            case 2 -> {
                JSplitPane containerMain = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        chartList.get(0),
                        chartList.get(1),
                        0.5);
                this.add(containerMain);
            }
            case 3 -> {
                // bottom 2 charts
                JSplitPane containerNested = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        chartList.get(1),
                        chartList.get(2),
                        0.5);

                // top chart and nested charts below
                JSplitPane containerMain = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        chartList.get(0),
                        containerNested,
                        0.3);

                this.add(containerMain);
            }
            case 4 -> {
                // 2 charts on the right
                JSplitPane containerRight = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        chartList.get(1),
                        chartList.get(3),
                        0.5);
                
                // 2 charts on the left
                JSplitPane containerLeft = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        chartList.get(0),
                        chartList.get(2),
                        0.5);

                // split pane to hold both left and right
                JSplitPane containerMain = createSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        containerLeft,
                        containerRight,
                        0.5);

                this.add(containerMain);
            }
            case 5 -> {
                // 2 charts on the right
                JSplitPane containerRight = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        chartList.get(1),
                        chartList.get(3),
                        0.5);
                
                // 2 charts on the left
                JSplitPane containerLeft = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        chartList.get(0),
                        chartList.get(2),
                        0.5);

                // split pane to hold both left and right
                JSplitPane containerTop = createSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        containerLeft,
                        containerRight,
                        0.5);

                // split pane to hold both top 4 and bottom chart
                JSplitPane containerMain = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        containerTop,
                        chartList.get(4),
                        0.7);

                this.add(containerMain);
            }
            case 6 -> {
                
                // 2 charts nested on the right
                JSplitPane containerRightNest = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        chartList.get(3),
                        chartList.get(5),
                        0.5);
                
                // chart and nested on the right
                JSplitPane containerRight = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        chartList.get(1),
                        containerRightNest,
                        0.3);
                
                
                // 2 charts nested on the left
                JSplitPane containerLeftNest = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        chartList.get(2),
                        chartList.get(4),
                        0.5);
                
                // chart and nested on the left
                JSplitPane containerLeft = createSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        chartList.get(0),
                        containerLeftNest,
                        0.3);

                // chart and nested on the left
                JSplitPane containerMain = createSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        containerLeft,
                        containerRight,
                        0.5);
                
                this.add(containerMain);
            }
            default -> {
            }
        }
    }

    /**
     * @param c - component to be removed
     */
    public void removeComponent(ChartDisplay c) {
        for (int i = 0; i < chartList.size(); i++) {
            if(chartList.get(i) == c){
                chartList.remove(i);
            }
        }
        formatSplitPane();
    }
}
