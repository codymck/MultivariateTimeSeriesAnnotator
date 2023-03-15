package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import com.opencsv.CSVWriter;
import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import edu.csusm.capstone.timeseriesannotator.View.CommentMenu;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.text.TextUtils;
import org.jfree.chart.ui.TextAnchor;

public class CommentAnnotation extends AbstractAnnotation {

    private double[] coordinates = {0.0, 0.0};
    private boolean selected;
    private Color color;
    private XYPlot plot;
    private Font font;
    private AnnotateChartPanel chartPanel;
    private XYTextAnnotation commentAnnotation = null;
    private String text;

    public CommentAnnotation(XYPlot p, Color c, double[] point, AnnotateChartPanel a, Font f) {
        this.plot = p;
        this.color = c;
        this.coordinates[0] = point[0];
        this.coordinates[1] = point[1];
        this.chartPanel = a;
        this.font = f;

        CommentMenu cMenu = new CommentMenu(new javax.swing.JFrame(), true);
        cMenu.setVisible(true);
        if (!cMenu.isSubmitted()) {
            return;
        }
        text = cMenu.getComment();
        commentAnnotation = new XYTextAnnotation(text, coordinates[0], coordinates[1]);
        commentAnnotation.setFont(f);
        commentAnnotation.setPaint(AppFrame.getAbsoluteColor());
        commentAnnotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
        plot.addAnnotation(commentAnnotation);
    }

    @Override
    public boolean isSelected() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {

        Point2D.Double click = new Point2D.Double(mouseX, mouseY);

        double x1 = commentAnnotation.getX();
        double y1 = commentAnnotation.getY();
        
        Rectangle2D.Double screenDataArea = (Rectangle2D.Double) chartPanel.getScreenDataArea();
        double screenWidthPx = screenDataArea.getMaxX() - screenDataArea.getMinX();
        double screenHeightPx = screenDataArea.getMaxY() - screenDataArea.getMinY();

        Graphics2D g2 = (Graphics2D) chartPanel.getGraphics();
        FontMetrics fm = g2.getFontMetrics(font);
        Rectangle2D.Double bounds = (Rectangle2D.Double) TextUtils.getTextBounds(commentAnnotation.getText(), g2, fm);

        double commentWidthPx = bounds.getMaxX() - bounds.getMinX();
        double commentHeightPx = bounds.getMaxY() - bounds.getMinY();
        
        
        ValueAxis domainAxis = plot.getDomainAxis();
        double domainMin = domainAxis.getLowerBound();
        double domainMax = domainAxis.getUpperBound();
        ValueAxis rangeAxis = plot.getRangeAxis();
        double rangeMin = rangeAxis.getLowerBound();
        double rangeMax = rangeAxis.getUpperBound();
        
        double plotWidth = domainMax - domainMin;
        double plotHeight = rangeMax - rangeMin;
        
        double hitboxWidth = (commentWidthPx * plotWidth) / screenWidthPx ;
        double hitboxHeight = (commentHeightPx * plotHeight) / screenHeightPx;
        
        Rectangle2D.Double hitbox = new Rectangle2D.Double(x1, y1, hitboxWidth, hitboxHeight);
        
        // Add the rectangle annotation to the plot
        //XYShapeAnnotation rect = new XYShapeAnnotation(hitbox, new BasicStroke(2.0f), Color.RED, new Color(0,0,0,0));
        //plot.addAnnotation(rect);

        if (hitbox.contains(click)) {
            return true;
        }
        return false;
    }

    @Override
    public void delete() {
        plot.removeAnnotation(commentAnnotation);
    }

    @Override
    public void move(double xOffset, double yOffset, boolean set) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void export(CSVWriter writer) {
        String[] annotation_type = {"comment"};
        String[] rgba = getRGBAList();
        String[] coords = getCoordsList();
        String[] data = getDataList();
        
        String[] row = {annotation_type[0], rgba[0], coords[0], data[0]};
        
        writer.writeNext(row);
    }
    
    @Override
    public String[] getRGBAList() {
        int R = color.getRed();
        int G = color.getGreen();
        int B = color.getBlue();
        int A = color.getAlpha();
        
        String[] rgba = {'[' + String.valueOf(R) + '/' + String.valueOf(G) + '/' + String.valueOf(B) + '/' + String.valueOf(A) + ']'};
        
        return rgba;
    }
    
    @Override
    public String[] getDataList() {
        String[] data = {'[' + text + '/' + font.getFamily() + '/' + Integer.toString(font.getStyle()) + '/' + Integer.toString(font.getSize()) + ']'};
        return data;
    }

    @Override
    public String[] getCoordsList() {
        String[] coords = {'[' + String.valueOf(coordinates[0]) + '/' + String.valueOf(coordinates[1]) + ']'};
        return coords;
    }

    
}
