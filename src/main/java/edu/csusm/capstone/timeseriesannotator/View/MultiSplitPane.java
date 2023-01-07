/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.View;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JSplitPane;

/**
 *
 * @author Cody McKinney
 */
public class MultiSplitPane extends JSplitPane {
    
    private ArrayList<JSplitPane> panelList = new ArrayList<JSplitPane>();
    private int numberOfPanels = 1;
    private final int division = 6;
    
    /**
     * Builds MultiSplitPane
     */
    public MultiSplitPane() {
        super();
        this.setLeftComponent(null);
        this.setRightComponent(null);
        this.setBorder(null);
        panelList.add(this);
        setAllBorders(division);
    }
    
    /**
     * @param c
     */
    public void addComponent(Component c) {
        JSplitPane container = new JSplitPane();
        container.setRightComponent(null);
        container.setLeftComponent(c);
        container.setDividerSize(division);
        container.setBorder(null);
        
        panelList.get(numberOfPanels - 1).setRightComponent(container);
        panelList.add(container);
        
        numberOfPanels++;
        this.fixWeights();
    }
    
    /**
     * 
     * @param orientation 
     *          JSplitPane.HORIZONTAL_SPLIT - sets orientation of components to horizontal alignment
     *          JSPlitPane.VERTICAL_SPLIT - sets orientation of components to vertical alignment
     */
    public void setAlignment(int orientation) {
        for (int i = 0; i < numberOfPanels; i++) {
            panelList.get(i).setOrientation(orientation);
        }
    }

    /**
     * 
     * @param size - resizes the borders of all components
     */
    private void setAllBorders(int size) {
        this.setDividerSize(size);
        
        for(int i = 0; i < numberOfPanels; i++) {
            panelList.get(i).setDividerSize(size);
        }
    }

    private void fixWeights() {
        panelList.get(0).setResizeWeight(1.0);
        
        for (int i = 1; i < numberOfPanels; i++) {
            double resize = (double) 1 / (double) (i + 1);
            panelList.get(numberOfPanels - i - 1).setResizeWeight(resize);
        }
        
        panelList.get(numberOfPanels - 1).setResizeWeight(0.0);
    }
    
    /**
     * 
     * @param c - component to be removed
     */
    public void removeComponent(Component c) {
        for (int i = 0; i < numberOfPanels; i++) {
            if (numberOfPanels == 2) {
                if (c == panelList.get(i).getLeftComponent()) {
                    panelList.remove(i);
                    numberOfPanels--;
                    panelList.get(0).setRightComponent(null);
                }
            } else if (numberOfPanels > 2 && i != (numberOfPanels - 1)) {
                if (c == panelList.get(i).getLeftComponent()) {
                    panelList.get(i - 1).setRightComponent(panelList.get(i + 1));
                    panelList.remove(i);
                    numberOfPanels--;
                }
            } else if (i == (numberOfPanels - 1)) {
                if (c == panelList.get(i).getLeftComponent()) {
                    panelList.get(i - 1).setRightComponent(null);
                    panelList.remove(i);
                    numberOfPanels--;
                }
            }
            this.fixWeights();        
        }
    }
    
    /**
     * 
     * @param index - index to remove the items
     */
    public void removeComponent(int index) {
        index = index + 1;
        for (int i = 1; i < numberOfPanels; i++) {
            // treats when there are just 2 elements
            if (numberOfPanels == 2) {
                if (index == i) {
                    panelList.remove(i);
                    numberOfPanels--;
                    panelList.get(0).setRightComponent(null);
                }

            } else if (numberOfPanels > 2 && i != (numberOfPanels - 1)) {
                if (index == i) {
                    panelList.get(i - 1).setRightComponent(panelList.get(i + 1));
                    panelList.remove(i);
                    numberOfPanels--;
                }

            } else if (i == (numberOfPanels - 1)) {
                if (index == i) {
                    panelList.get(i - 1).setRightComponent(null);
                    panelList.remove(i);
                    numberOfPanels--;
                }
            }
            this.fixWeights();
        }

    }

    public int getNumberOfComponents() {
        return numberOfPanels;
    }
    
}
