/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.View;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleEdge;

/**
 *
 * @author Ben Theurich
 */
public class AnnotateChartPanel extends ChartPanel implements MouseListener{
    private JFreeChart chart = null;
    private boolean selecting = true;
    
    private double[][] coordinates = {{0.0, 0.0}, {0.0, 0.0}};
    
    public AnnotateChartPanel(JFreeChart chart) {
        super(chart);
        this.chart = chart;
    }
    
    public void mousePressed(MouseEvent e){
        if (selecting){
            System.out.println("mouse pressed");
            setMouseZoomable(false,true);
            super.mousePressed(e);
            getPointInChart(e, 0);
            setMouseZoomable(true,true);
        }else{
            super.mousePressed(e);
        }
    }
    public void mouseReleased(MouseEvent e){
        if (selecting){
            System.out.println("mouse released");
            setMouseZoomable(false,true);
            super.mouseReleased(e);
            getPointInChart(e, 1);
            setMouseZoomable(true,false);
            XYPlot plot = (XYPlot) chart.getPlot();
            Color color = new Color(0, 0, 255, 60);
                        
            XYBoxAnnotation a = new XYBoxAnnotation(
                    coordinates[0][0], 
                    coordinates[1][1], 
                    coordinates[1][0], 
                    coordinates[0][1],
                    null, 
                    null, 
                    color
            );
            plot.addAnnotation(a);
        }else{
            super.mouseReleased(e);
        }
    }
    
    public void getPointInChart(MouseEvent e, int state){
        
        Insets insets = getInsets();
        int mouseX = (int) ((e.getX() - insets.left) / this.getScaleX());
        int mouseY = (int) ((e.getY() - insets.top) / this.getScaleY());
        System.out.println("x = " + mouseX + ", y = " + mouseY);

        Point2D p = this.translateScreenToJava2D(new Point(mouseX, mouseY));
        XYPlot plot = (XYPlot) chart.getPlot();
        ChartRenderingInfo info = this.getChartRenderingInfo();
        Rectangle2D dataArea = info.getPlotInfo().getDataArea();

        ValueAxis domainAxis = plot.getDomainAxis();
        RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
        ValueAxis rangeAxis = plot.getRangeAxis();
        RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();

        double chartX = domainAxis.java2DToValue(p.getX(), dataArea, domainAxisEdge);
        double chartY = rangeAxis.java2DToValue(p.getY(), dataArea, rangeAxisEdge);
        System.out.println("Chart: x = " + chartX + ", y = " + chartY);
        
        coordinates[state][0] = chartX;
        coordinates[state][1] = chartY;
        
    }   
}