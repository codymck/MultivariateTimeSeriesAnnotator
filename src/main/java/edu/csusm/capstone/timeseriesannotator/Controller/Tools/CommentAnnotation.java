package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import edu.csusm.capstone.timeseriesannotator.View.CommentMenu;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
        chartPanel.addAbstractAnnotation(this);
    }
    
    public CommentAnnotation(XYPlot p, int[] c, double[][] point, AnnotateChartPanel a, String[] t) {
        this.plot = p;
        this.color = new Color(c[0], c[1], c[2], 255);
        this.coordinates[0] = point[0][0];
        this.coordinates[1] = point[0][1];
        this.chartPanel = a;
        
        this.text = t[0].substring(1, t[0].length()-1);
        String name = t[1].substring(1, t[1].length()-1);
        String fStyle = t[2].substring(1, t[2].length()-1);
        String fSize = t[3].substring(1, t[3].length()-1);
        
        int fontStyle = Integer.parseInt(fStyle);
        int fontSize = Integer.parseInt(fSize);
        
        Font f = new Font(name, fontStyle, fontSize);
        this.font = f;
        commentAnnotation = new XYTextAnnotation(text, coordinates[0], coordinates[1]);
        commentAnnotation.setFont(font);
        commentAnnotation.setPaint(color);
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
    public String getType(){
        return "comment";
    }
    
    @Override
    public String getRGBA() {
        String R = String.valueOf(color.getRed());
        String G = String.valueOf(color.getGreen());
        String B = String.valueOf(color.getBlue());
        String A = String.valueOf(color.getAlpha());

        return "[" + R + ", " + G + ", " + B + ", " + A + "]";
    }

    
    @Override
    public String getData() {
        String FONT = font.getFamily();
        String FONTSTYLE = Integer.toString(font.getStyle());
        String FONTSIZE = Integer.toString(font.getSize());
        return "[\"" + text + "\", \"" + FONT + "\", \"" + FONTSTYLE + "\", \"" + FONTSIZE + "\"]";
    }

    @Override
    public String getCoords() {
        String X = String.valueOf(coordinates[0]);
        String Y = String.valueOf(coordinates[1]);
        return "[[" + X + ", " + Y + "]]";
    }
}
