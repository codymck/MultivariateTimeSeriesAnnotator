package edu.csusm.capstone.timeseriesannotator.View;

import edu.csusm.capstone.timeseriesannotator.Model.ToolState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.Range;

/**
 *
 * @author Ben Theurich
 * @author Cody McKinney
 */
public class AnnotateChartPanel extends ChartPanel implements MouseListener {

    private ToolState state;
    private JFreeChart chart = null;
    Color color = new Color(0, 100, 255, 60);
    private XYPlot plot;
    private double[][] coordinates = {{0.0, 0.0}, {0.0, 0.0}};
    private ArrayList<RegionStruct> regionList = new ArrayList<>();
    private ArrayList<RegionStruct> rectList = new ArrayList<>();

    private double x, y, width, height;
    
    private Rectangle2D.Double rect = null;
    private Point2D startPoint = null;
    
    public void setChartState(ToolState s) {
        this.state = s;
    }

    public ToolState getChartState() {
        return this.state;
    }

    public AnnotateChartPanel(JFreeChart chart) {
        super(chart);
        this.chart = chart;
        this.plot = (XYPlot) chart.getPlot();
        setChartState(AppFrame.getAppState());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        double point[] = getPointInChart(e);
        Point2D pointObj = e.getPoint();
        Rectangle2D chartArea = getScreenDataArea();
        if (null != state && chartArea.contains(pointObj)) {
            switch (state) {
                case HIGHLIGHT:
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        startPoint = e.getPoint();
                        coordinates[0][0] = point[0];
                        coordinates[0][1] = point[1];
                        rect = new Rectangle2D.Double(startPoint.getX(), startPoint.getY(), 0, 0);
                    }
                    break;
                case ZOOM:
                    setMouseZoomable(true, false);
                    super.mousePressed(e);
                    break;
                case PAN:
                    int panMask = MouseEvent.BUTTON1_MASK;

                    try {
                        Field mask = ChartPanel.class.getDeclaredField("panMask");
                        mask.setAccessible(true);
                        mask.set(this, panMask);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    super.mousePressed(e);
                    break;
                case COMMENT:
                    if (e.getButton() == MouseEvent.BUTTON1 && AppFrame.getCtrlPress() == false) {
                        CommentMenu cMenu = new CommentMenu(new javax.swing.JFrame(), true);
                        cMenu.setVisible(true);
                        if (cMenu.isSubmitted() == false) {
                            return;
                        }

                        XYTextAnnotation at = new XYTextAnnotation(cMenu.getComment(), point[0], point[1]);
                        at.setFont(new Font(cMenu.getFontName(), cMenu.getFontStyle(), cMenu.getFontSize()));
                        at.setTextAnchor(TextAnchor.TOP_LEFT);
                        plot.addAnnotation(at);
                    }
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        removeTextAnnotation();
                    }
                    //super.mousePressed(e);
                    break;
                case MARK:
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        startPoint = e.getPoint();
                        coordinates[0][0] = point[0];
                        coordinates[0][1] = point[1];
                        rect = new Rectangle2D.Double(startPoint.getX(), startPoint.getY(), 0, 0);
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (null != state) {
            switch (state) {
                case HIGHLIGHT:
                    if(this.startPoint != null && this.rect != null){
                        Rectangle2D screenDataArea = getScreenDataArea();
                        Point2D endPoint = e.getPoint();
                        x = Math.min(startPoint.getX(), endPoint.getX());
                        y = screenDataArea.getMinY();
                        width = Math.abs(startPoint.getX() - endPoint.getX());
                        height = screenDataArea.getMaxY() - y;

                        // make sure it doesn't overflow the bounds of the chart
                        if (x < screenDataArea.getMinX()) {
                          width -= screenDataArea.getMinX() - x;
                          x = (int) screenDataArea.getMinX();
                        }
                        if (x + width > screenDataArea.getMaxX()) {
                          width = (int) (screenDataArea.getMaxX() - x);
                        }
                        rect.setRect(x, y, width, height);
                        repaint();
                    }
                    break;
                case MARK:
                    if(this.startPoint != null && this.rect != null){
                        Point2D endPoint = e.getPoint();
                        x = Math.min(startPoint.getX(), endPoint.getX());
                        y = Math.min(startPoint.getY(), endPoint.getY());
                        width = Math.abs(startPoint.getX() - endPoint.getX());
                        height = Math.abs(startPoint.getY() - endPoint.getY());

                        // make sure it doesn't overflow the bounds of the chart
                        Rectangle2D screenDataArea = getScreenDataArea();
                        if (x < screenDataArea.getMinX()) {
                          width -= screenDataArea.getMinX() - x;
                          x = (int) screenDataArea.getMinX();
                        }
                        if (y < screenDataArea.getMinY()) {
                          height -= screenDataArea.getMinY() - y;
                          y = (int) screenDataArea.getMinY();
                        }
                        if (x + width > screenDataArea.getMaxX()) {
                          width = (int) (screenDataArea.getMaxX() - x);
                        }
                        if (y + height > screenDataArea.getMaxY()) {
                          height = (int) (screenDataArea.getMaxY() - y);
                        }

                        rect.setRect(x, y, width, height);
                        repaint();
                    }
                    break;
                default:
                    super.mouseDragged(e);
                    break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        double point[] = getPointInChart(e);
        if (null != state) {
            switch (state) {
                case HIGHLIGHT:
                    if (e.getButton() == MouseEvent.BUTTON1 && this.rect != null) {
                        super.mouseReleased(e);
                        coordinates[1][0] = point[0];
                        coordinates[1][1] = point[1];
                        rect = null;
                        repaint();
                        addAnnotation("region");
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        removeAnnotation("region", point[0], point[1]);
                    }
                    break;
                case ZOOM:
                    super.mouseReleased(e);
                    break;
                case PAN:
                    int panMask = MouseEvent.CTRL_MASK;

                    try {
                        Field mask = ChartPanel.class.getDeclaredField("panMask");
                        mask.setAccessible(true);
                        mask.set(this, panMask);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    super.mouseReleased(e);
                    break;
                case COMMENT:
                    //super.mouseReleased(e);
                    break;
                case MARK:
                    if (e.getButton() == MouseEvent.BUTTON1 && this.rect != null) {
                        super.mouseReleased(e);
                        coordinates[1][0] = point[0];
                        coordinates[1][1] = point[1];
                        rect = null;
                        repaint();
                        addAnnotation("rect");
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        removeAnnotation("rect", point[0], point[1]);
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (rect != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.fill(rect);
        }
    }

    public void removeTextAnnotation() {
        List<XYTextAnnotation> annotations = new ArrayList<>(plot.getAnnotations());
        List<XYTextAnnotation> textAnnotations = new ArrayList<>();
        for (XYAnnotation annotation : annotations) {
            if (annotation instanceof XYTextAnnotation) {
                textAnnotations.add((XYTextAnnotation) annotation);
            }
        }

        this.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                int x = event.getTrigger().getX();
                int y = event.getTrigger().getY();
                for (XYTextAnnotation annotation : textAnnotations) {
                    ChartRenderingInfo info = getChartRenderingInfo();
                    Rectangle2D bounds = info.getPlotInfo().getDataArea();
                    double x1 = plot.getDomainAxis().valueToJava2D(annotation.getX(), bounds, plot.getDomainAxisEdge());
                    double y1 = plot.getRangeAxis().valueToJava2D(annotation.getY(), bounds, plot.getRangeAxisEdge());
                    if (x >= x1 && x <= x1 + annotation.getText().length() * 7 && y >= y1 && y <= y1 + 20) {
                        plot.removeAnnotation(annotation);
                        break;
                    }
                }

                annotations.clear();
                textAnnotations.clear();
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
            }
        });
    }

    public void addAnnotation(String type) {
        if(type.equals("region")){
            ValueAxis yAxis = plot.getRangeAxis();
            Range yRange = yAxis.getRange();
            double yMin = yRange.getLowerBound();
            double yMax = yRange.getUpperBound();
            double upperLeftX = Math.min(coordinates[0][0], coordinates[1][0]);
            double upperLeftY = yMin;

            double lowerRightX = Math.max(coordinates[0][0], coordinates[1][0]);
            double lowerRightY = yMax;

            XYBoxAnnotation region = new XYBoxAnnotation(
                    upperLeftX,
                    upperLeftY,
                    lowerRightX,
                    lowerRightY,
                    null,
                    null,
                    color
            );
            plot.addAnnotation(region);
            regionList.add(new RegionStruct(upperLeftX, upperLeftY, lowerRightX, lowerRightY, region));
        }else if(type.equals("rect")){
            double upperLeftX = Math.min(coordinates[0][0], coordinates[1][0]);
            double upperLeftY = Math.min(coordinates[0][1], coordinates[1][1]);

            double lowerRightX = Math.max(coordinates[0][0], coordinates[1][0]);
            double lowerRightY = Math.max(coordinates[0][1], coordinates[1][1]);

            XYBoxAnnotation region = new XYBoxAnnotation(
                    upperLeftX,
                    upperLeftY,
                    lowerRightX,
                    lowerRightY,
                    null,
                    null,
                    color
            );
            plot.addAnnotation(region);
            rectList.add(new RegionStruct(upperLeftX, upperLeftY, lowerRightX, lowerRightY, region));
        }
    }

    public void removeAnnotation(String type, double mouseX, double mouseY) {
        if(type.equals("region")){
            for (int i = regionList.size() - 1; i >= 0; i--) {
                RegionStruct r = regionList.get(i);
                if (r.isClickedOn(mouseX, mouseY)) {
                    plot.removeAnnotation(r.getRegion());
                    regionList.remove(i);
                    break;
                }
            }
        }else if(type.equals("rect")){
            for (int i = rectList.size() - 1; i >= 0; i--) {
                RegionStruct r = rectList.get(i);
                if (r.isClickedOn(mouseX, mouseY)) {
                    plot.removeAnnotation(r.getRegion());
                    rectList.remove(i);
                    break;
                }
            }
        }
    }

    public double[] getPointInChart(MouseEvent e) {
        Insets insets = getInsets();
        int mouseX = (int) ((e.getX() - insets.left) / this.getScaleX());
        int mouseY = (int) ((e.getY() - insets.top) / this.getScaleY());
//        System.out.println("x = " + mouseX + ", y = " + mouseY);

        Point2D p = this.translateScreenToJava2D(new Point(mouseX, mouseY));
        ChartRenderingInfo info = this.getChartRenderingInfo();
        Rectangle2D dataArea = info.getPlotInfo().getDataArea();

        ValueAxis domainAxis = plot.getDomainAxis();
        RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
        ValueAxis rangeAxis = plot.getRangeAxis();
        RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();

        double chartX = domainAxis.java2DToValue(p.getX(), dataArea, domainAxisEdge);
        double chartY = rangeAxis.java2DToValue(p.getY(), dataArea, rangeAxisEdge);
//        System.out.println("Chart: x = " + chartX + ", y = " + chartY);
        double[] r = {chartX, chartY};
        return r;
    }

    public void setColor(Color c) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        color = new Color(r, g, b, 60);
    }

}
