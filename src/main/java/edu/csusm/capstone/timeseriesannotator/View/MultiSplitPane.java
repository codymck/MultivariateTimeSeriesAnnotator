/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.View;

import java.util.ArrayList;
import javax.swing.JPanel;

import javax.swing.JSplitPane;

/**
 *
 * @author Cody McKinney
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
        validate();
        repaint();
    }

    private void formatSplitPane() {
        this.removeAll();
        switch (chartList.size()) {
            case 1 ->
                this.add(chartList.get(0));
            case 2 -> {
                JSplitPane container = new JSplitPane();
                container.setOrientation(JSplitPane.VERTICAL_SPLIT);
                container.setTopComponent(chartList.get(0));
                container.setBottomComponent(chartList.get(1));
                container.setDividerSize(division);
                container.setBorder(null);
                container.setResizeWeight(0.5);
                this.add(container);
            }
            case 3 -> {
                JSplitPane container2 = new JSplitPane();
                container2.setOrientation(JSplitPane.VERTICAL_SPLIT);
                container2.setTopComponent(chartList.get(1));
                container2.setBottomComponent(chartList.get(2));
                container2.setDividerSize(division);
                container2.setBorder(null);
                container2.setResizeWeight(0.5);
                JSplitPane container1 = new JSplitPane();
                container1.setOrientation(JSplitPane.VERTICAL_SPLIT);
                container1.setTopComponent(chartList.get(0));
                container1.setBottomComponent(container2);
                container1.setDividerSize(division);
                container1.setBorder(null);
                container1.setResizeWeight(0.3);
                this.add(container1);
            }
            case 4 -> {
                JSplitPane containerRight = new JSplitPane();
                containerRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
                containerRight.setTopComponent(chartList.get(1));
                containerRight.setBottomComponent(chartList.get(3));
                containerRight.setDividerSize(division);
                containerRight.setBorder(null);
                containerRight.setResizeWeight(0.5);
                JSplitPane containerLeft = new JSplitPane();
                containerLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
                containerLeft.setTopComponent(chartList.get(0));
                containerLeft.setBottomComponent(chartList.get(2));
                containerLeft.setDividerSize(division);
                containerLeft.setBorder(null);
                containerLeft.setResizeWeight(0.5);
                JSplitPane container1 = new JSplitPane();
                container1.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                container1.setLeftComponent(containerLeft);
                container1.setRightComponent(containerRight);
                container1.setDividerSize(division);
                container1.setBorder(null);
                container1.setResizeWeight(0.5);
                this.add(container1);
            }
            case 5 -> {
                JSplitPane containerRight = new JSplitPane();
                containerRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
                containerRight.setTopComponent(chartList.get(1));
                containerRight.setBottomComponent(chartList.get(3));
                containerRight.setDividerSize(division);
                containerRight.setBorder(null);
                containerRight.setResizeWeight(0.5);
                JSplitPane containerLeft = new JSplitPane();
                containerLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
                containerLeft.setTopComponent(chartList.get(0));
                containerLeft.setBottomComponent(chartList.get(2));
                containerLeft.setDividerSize(division);
                containerLeft.setBorder(null);
                containerLeft.setResizeWeight(0.5);
                JSplitPane container1 = new JSplitPane();
                container1.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                container1.setLeftComponent(containerLeft);
                container1.setRightComponent(containerRight);
                container1.setDividerSize(division);
                container1.setBorder(null);
                container1.setResizeWeight(0.5);
                this.add(container1);
                JSplitPane container0 = new JSplitPane();
                container0.setOrientation(JSplitPane.VERTICAL_SPLIT);
                container0.setTopComponent(container1);
                container0.setBottomComponent(chartList.get(4));
                container0.setDividerSize(division);
                container0.setBorder(null);
                container0.setResizeWeight(0.75);
                this.add(container0);
            }
            case 6 -> {
                JSplitPane containerRightNest = new JSplitPane();
                containerRightNest.setOrientation(JSplitPane.VERTICAL_SPLIT);
                containerRightNest.setTopComponent(chartList.get(3));
                containerRightNest.setBottomComponent(chartList.get(5));
                containerRightNest.setDividerSize(division);
                containerRightNest.setBorder(null);
                containerRightNest.setResizeWeight(0.5);
                JSplitPane containerRight = new JSplitPane();
                containerRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
                containerRight.setTopComponent(chartList.get(1));
                containerRight.setBottomComponent(containerRightNest);
                containerRight.setDividerSize(division);
                containerRight.setBorder(null);
                containerRight.setResizeWeight(0.3);
                JSplitPane containerLeftNest = new JSplitPane();
                containerLeftNest.setOrientation(JSplitPane.VERTICAL_SPLIT);
                containerLeftNest.setTopComponent(chartList.get(2));
                containerLeftNest.setBottomComponent(chartList.get(4));
                containerLeftNest.setDividerSize(division);
                containerLeftNest.setBorder(null);
                containerLeftNest.setResizeWeight(0.5);
                JSplitPane containerLeft = new JSplitPane();
                containerLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
                containerLeft.setTopComponent(chartList.get(0));
                containerLeft.setBottomComponent(containerLeftNest);
                containerLeft.setDividerSize(division);
                containerLeft.setBorder(null);
                containerLeft.setResizeWeight(0.3);
                JSplitPane container1 = new JSplitPane();
                container1.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                container1.setLeftComponent(containerLeft);
                container1.setRightComponent(containerRight);
                container1.setDividerSize(division);
                container1.setBorder(null);
                container1.setResizeWeight(0.5);
                this.add(container1);
            }
            default -> {
            }
        }
    }

    /**
     *
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
