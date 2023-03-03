package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import edu.csusm.capstone.timeseriesannotator.View.CommentMenu;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.text.TextUtils;
import org.jfree.chart.ui.TextAnchor;

public class CommentAnnotation extends AbstractAnnotation {

    private double[] coordinates = {0.0, 0.0};
    private boolean selected;
    private Color color;
    private XYPlot plot;
    private AnnotateChartPanel chartPanel;

    private XYTextAnnotation commentToDelete;

    public CommentAnnotation(XYPlot p, Color c, double[] point, AnnotateChartPanel a) {
        this.plot = p;
        this.color = c;
        this.coordinates[0] = point[0];
        this.coordinates[1] = point[1];
        this.chartPanel = a;

        CommentMenu cMenu = new CommentMenu(new javax.swing.JFrame(), true);
        cMenu.setVisible(true);
        if (cMenu.isSubmitted() == false) {
            return;
        }

        XYTextAnnotation at = new XYTextAnnotation(cMenu.getComment(), coordinates[0], coordinates[1]);
        at.setFont(new Font(AppFrame.getFontName(), AppFrame.getFontStyle(), AppFrame.getFontSize()));
        at.setPaint(AppFrame.getAbsoluteColor());
        at.setTextAnchor(TextAnchor.TOP_RIGHT);
        plot.addAnnotation(at);
    }

    @Override
    public boolean isSelected() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean clickedOn(double mouseX, double mouseY) {
        List<XYTextAnnotation> annotations = new ArrayList<>(plot.getAnnotations());
        List<XYTextAnnotation> textAnnotations = new ArrayList<>();
        for (XYAnnotation annotation : annotations) {
            if (annotation instanceof XYTextAnnotation xYTextAnnotation) {
                textAnnotations.add(xYTextAnnotation);
            }
        }
        for (XYTextAnnotation annotation : textAnnotations) {
            Point2D.Double click = new Point2D.Double(mouseX, mouseY);
            System.out.println(click.getX() + " " + click.getY());

            double x1 = annotation.getX();
            double y1 = annotation.getY();
            System.out.println(x1 + " " + y1);

            Graphics2D g2 = (Graphics2D) chartPanel.getGraphics();
            Font font = annotation.getFont();
            FontMetrics fm = g2.getFontMetrics(font);

            Rectangle2D bounds = TextUtils.getTextBounds(annotation.getText(), g2, fm);

            XYShapeAnnotation rect = new XYShapeAnnotation(bounds, new BasicStroke(1.0f), Color.RED, Color.RED);

            // Add the rectangle annotation to the plot
            plot.addAnnotation(rect);

            if (bounds.contains(click)) {
                commentToDelete = annotation;
                return true;
            }
        }
        annotations.clear();
        textAnnotations.clear();
        return false;
    }

    @Override
    public void delete() {
        plot.removeAnnotation(commentToDelete);
    }

    @Override
    public void move(double xOffset, double yOffset, boolean set) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
