package edu.csusm.capstone.timeseriesannotator.Controller.Tools;

import edu.csusm.capstone.timeseriesannotator.View.AnnotateChartPanel;
import edu.csusm.capstone.timeseriesannotator.View.CommentMenu;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.text.TextUtils;
import org.jfree.chart.ui.TextAnchor;

/**
 *
 * @author Ben Theurich
 * @author Cody McKinney
 */
public class CommentAnnotation extends AbstractAnnotation {

    private double[] coordinates = {0.0, 0.0};
    private Font font;
    private XYTextAnnotation commentAnnotation = null;
    private XYShapeAnnotation selectedRect = null;
    private Rectangle2D.Double hitbox = null;
    private String text;
    private boolean clickFlag = false;
    private long startTime = 0;
    private long endTime = 0;

    public CommentAnnotation(XYPlot p, Color c, double[] point, Font f, AnnotateChartPanel cP) {
        this.plot = p;
        this.color = c;
        this.coordinates[0] = point[0];
        this.coordinates[1] = point[1];
        this.chartPanel = cP;
        this.font = f;

        CommentMenu cMenu = new CommentMenu(new javax.swing.JFrame(), true, "Enter text here...");
        cMenu.setVisible(true);
        if (!cMenu.isSubmitted()) {
            return;
        }
        text = cMenu.getComment();
        commentAnnotation = new XYTextAnnotation(text, coordinates[0], coordinates[1]);
        commentAnnotation.setFont(f);
        commentAnnotation.setPaint(color);
        commentAnnotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
        plot.addAnnotation(commentAnnotation);
        chartPanel.addAbstractAnnotation(this);
    }

    public CommentAnnotation(XYPlot p, int[] c, double[][] point, String[] t, AnnotateChartPanel cP) {
        this.plot = p;
        this.color = new Color(c[0], c[1], c[2], 255);
        this.coordinates[0] = point[0][0];
        this.coordinates[1] = point[0][1];
        this.chartPanel = cP;

        this.text = t[0].substring(1, t[0].length() - 1);
        String name = t[1].substring(1, t[1].length() - 1);
        String fStyle = t[2].substring(1, t[2].length() - 1);
        String fSize = t[3].substring(1, t[3].length() - 1);

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
    public boolean clickedOn(double mouseX, double mouseY) {
        Point2D.Double click = new Point2D.Double(mouseX, mouseY);
        if (!selected) {
            this.getBounds(false);
        }
        boolean clicked = hitbox.contains(click);

        if (clicked) {
            if (!clickFlag) {
                startTime = System.nanoTime();
                clickFlag = true;
            } else {
                endTime = System.nanoTime();
                double difference = (endTime - startTime) / 1e6;
                if (difference < 500) {
                    changeText();
                }
                clickFlag = false;
            }
        }
        return clicked;
    }

    @Override
    public void select() {
        if (!selected) {
            plot.addAnnotation(selectedRect);
            selected = true;
        }
    }

    @Override
    public void deselect() {
        if (selected) {
            plot.removeAnnotation(selectedRect);
            selected = false;
        }
    }

    @Override
    public void scale() {
        getBounds(true);
    }

    private void getBounds(boolean draw) {
        if (draw) {
            plot.removeAnnotation(selectedRect);
        }

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

        double hitboxWidth = (commentWidthPx * plotWidth) / screenWidthPx;
        double hitboxHeight = (commentHeightPx * plotHeight) / screenHeightPx;

        if (hitbox == null) {
            hitbox = new Rectangle2D.Double(x1, y1, hitboxWidth, hitboxHeight);
        } else {
            hitbox.setFrame(x1, y1, hitboxWidth, hitboxHeight);
        }

        selectedRect = new XYShapeAnnotation(hitbox, dashed, Color.BLACK, new Color(0, 0, 0, 0));
        if (draw) {
            plot.addAnnotation(selectedRect);
        }

    }

    @Override
    public void delete() {
        plot.removeAnnotation(commentAnnotation);
        if (selected) {
            plot.removeAnnotation(selectedRect);
        }
    }

    @Override
    public void move(double xOffset, double yOffset, boolean set) {
        plot.removeAnnotation(commentAnnotation);
        if (selected) {
            plot.removeAnnotation(selectedRect);
        }

        if (!set) {
            commentAnnotation.setX(coordinates[0] - xOffset);
            commentAnnotation.setY(coordinates[1] - yOffset);
            hitbox.setFrame(coordinates[0] - xOffset, coordinates[1] - yOffset, hitbox.getWidth(), hitbox.getHeight());
        } else {
            coordinates[0] -= xOffset;
            coordinates[1] -= yOffset;
            commentAnnotation.setX(coordinates[0]);
            commentAnnotation.setY(coordinates[1]);
            hitbox.setFrame(coordinates[0], coordinates[1], hitbox.getWidth(), hitbox.getHeight());
        }

        plot.addAnnotation(commentAnnotation);
        if (selected) {
            selectedRect = new XYShapeAnnotation(hitbox, dashed, Color.BLACK, new Color(0, 0, 0, 0));
            plot.addAnnotation(selectedRect);
        }
    }

    @Override
    public void changeColor(Color c) {
        color = c;
        plot.removeAnnotation(commentAnnotation);
        commentAnnotation.setPaint(color);
        plot.addAnnotation(commentAnnotation);
    }

    private void changeText() {
        CommentMenu cMenu = new CommentMenu(new javax.swing.JFrame(), true, text);
        cMenu.setVisible(true);
        if (!cMenu.isSubmitted()) {
            return;
        }
        text = cMenu.getComment();
        plot.removeAnnotation(commentAnnotation);
        commentAnnotation.setText(text);
        plot.addAnnotation(commentAnnotation);
        selected = true;
        getBounds(true);
    }

    public void changeFontName(String fName) {
        Font f = new Font(fName, font.getStyle(), font.getSize());
        changeFont(f);
    }

    public void changeFontStyle(int fStyle) {
        Font f = new Font(font.getName(), fStyle, font.getSize());
        changeFont(f);
    }

    public void changeFontSize(int fSize) {
        Font f = new Font(font.getName(), font.getStyle(), fSize);
        changeFont(f);
    }

    private void changeFont(Font f) {
        font = f;
        plot.removeAnnotation(commentAnnotation);
        commentAnnotation.setFont(font);
        plot.addAnnotation(commentAnnotation);
    }

    public Font getFont() {
        return font;
    }

    @Override
    public String getType() {
        return "comment";
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
