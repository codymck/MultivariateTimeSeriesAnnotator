package edu.csusm.capstone.timeseriesannotator.View;

import edu.csusm.capstone.timeseriesannotator.Controller.Controller;
import edu.csusm.capstone.timeseriesannotator.Model.ToolState;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;

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

    double leftBound, rightBound, bottomBound, topBound;

    private double x, y, width, height;

    private Rectangle2D.Double rect = null;
    private double[] startPoint;


    private ValueMarker startMarker;
    private ValueMarker endMarker;
    private ValueMarker hMarker;
    private ValueMarker vMarker;
    private XYLineAnnotation lineAnnotation;
    private Point sPoint, ePoint;

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
                        sPoint = e.getPoint();
                        coordinates[0][0] = point[0];
                        coordinates[0][1] = point[1];
                        rect = new Rectangle2D.Double(sPoint.getX(), sPoint.getY(), 0, 0);
                    }
                    break;
                case ZOOM:
                    setMouseZoomable(true, false);
                    leftBound = point[0];
                    topBound = point[1];
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
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        CommentMenu cMenu = new CommentMenu(new javax.swing.JFrame(), true);
                        cMenu.setVisible(true);
                        if (cMenu.isSubmitted() == false) {
                            return;
                        }

                        XYTextAnnotation at = new XYTextAnnotation(cMenu.getComment(), point[0], point[1]);
                        at.setFont(new Font(cMenu.getFontName(), cMenu.getFontStyle(), cMenu.getFontSize()));
                        at.setPaint(cMenu.getFontColor());
                        at.setTextAnchor(TextAnchor.TOP_LEFT);
                        plot.addAnnotation(at);
                    }
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        removeTextAnnotation();
                    }
                    //super.mousePressed(e);
                    break;
                case MARK:
                    switch (AppFrame.getMarkerType()) {
                        case SQUARE:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                sPoint = e.getPoint();
                                coordinates[0][0] = point[0];
                                coordinates[0][1] = point[1];
                                rect = new Rectangle2D.Double(sPoint.getX(), sPoint.getY(), 0, 0);
                            }
                            break;
                        case HORIZONTAL:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                ValueMarker marker = new ValueMarker(point[1]);
//                                marker.setLabel("line");
                                marker.setLabelAnchor(RectangleAnchor.CENTER);
                                marker.setPaint(new Color(0, 0, 0, 200));
                                marker.setStroke(new BasicStroke(2.0f));
                                plot.addRangeMarker(marker);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                Collection<ValueMarker> markers = plot.getRangeMarkers(Layer.FOREGROUND);
                                List<ValueMarker> markerList = new ArrayList<>(markers);
                                for (ValueMarker marker : markerList) {
                                    double y = plot.getRangeAxis().java2DToValue(e.getY(), this.getScreenDataArea(), plot.getRangeAxisEdge());
                                    if (y >= marker.getValue() - 3 && y <= marker.getValue() + 3) {
                                        plot.removeRangeMarker(marker);
                                        break;
                                    }
                                }
                            }
                            break;
                        case VERTICAL:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                ValueMarker marker = new ValueMarker(point[0]);
//                                marker.setLabel("line");
                                marker.setLabelAnchor(RectangleAnchor.CENTER);
                                marker.setPaint(new Color(0, 0, 0, 200));
                                marker.setStroke(new BasicStroke(2.0f));
                                plot.addDomainMarker(marker);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                Collection<ValueMarker> markers = plot.getDomainMarkers(Layer.FOREGROUND);
                                List<ValueMarker> markerList = new ArrayList<>(markers);
                                for (ValueMarker marker : markerList) {
                                    double x = plot.getDomainAxis().java2DToValue(e.getX(), this.getScreenDataArea(), plot.getDomainAxisEdge());
                                    if (x >= marker.getValue() - 3 && x <= marker.getValue() + 3) {
                                        plot.removeDomainMarker(marker);
                                        break;
                                    }
                                }
                            }
                            break;
                        case DIAGONAL:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                startPoint = point;
                                lineAnnotation = new XYLineAnnotation(
                                        point[0], 
                                        point[1], 
                                        point[0], 
                                        point[1], 
                                        new BasicStroke(2.0f), Color.BLACK);
                                plot.addAnnotation(lineAnnotation);
                            }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double point[] = getPointInChart(e);
        if (null != state) {
            switch (state) {
                case HIGHLIGHT:
                    if (this.sPoint != null && this.rect != null) {
                        Rectangle2D screenDataArea = getScreenDataArea();
                        Point2D endPoint = e.getPoint();
                        x = Math.min(sPoint.getX(), endPoint.getX());
                        y = screenDataArea.getMinY();
                        width = Math.abs(sPoint.getX() - endPoint.getX());
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
                    switch (AppFrame.getMarkerType()) {
                        case SQUARE:
                            if (this.sPoint != null && this.rect != null) {
                                Point2D endPoint = e.getPoint();
                                x = Math.min(sPoint.getX(), endPoint.getX());
                                y = Math.min(sPoint.getY(), endPoint.getY());
                                width = Math.abs(sPoint.getX() - endPoint.getX());
                                height = Math.abs(sPoint.getY() - endPoint.getY());

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
                        case DIAGONAL:
                            plot.removeAnnotation(lineAnnotation);
                            lineAnnotation = new XYLineAnnotation(
                                    startPoint[0], 
                                    startPoint[1], 
                                    point[0], 
                                    point[1], 
                                    new BasicStroke(2.0f), Color.BLACK);
                            plot.addAnnotation(lineAnnotation);
                            repaint();
                            break;
                    }
                default:
                    super.mouseDragged(e);
                    break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point2D pointObj = e.getPoint();
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
                    rightBound = point[0];
                    double tempY = point[1];
                    if (tempY > topBound) {
                        bottomBound = topBound;
                        topBound = tempY;
                    } else {
                        bottomBound = tempY;
                    }
                    checkSync(leftBound, rightBound, bottomBound, topBound);
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
                    ValueAxis xAxis = getChart().getXYPlot().getDomainAxis();
                    ValueAxis yAxis = getChart().getXYPlot().getRangeAxis();

                    double x1 = xAxis.getRange().getLowerBound();
                    double x2 = xAxis.getRange().getUpperBound();

                    double y1 = yAxis.getRange().getLowerBound();
                    double y2 = yAxis.getRange().getUpperBound();
                    //System.out.println("x1: " + x1 + " x2: " + x2);
                    Controller.sync(x1, x2, y1, y2);
                    break;
                case COMMENT:
                    //super.mouseReleased(e);
                    break;
                case MARK:
                    switch (AppFrame.getMarkerType()) {
                        case SQUARE:
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
                        case DIAGONAL:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                plot.removeAnnotation(lineAnnotation);
                                XYLineAnnotation lineAnnotationP = new XYLineAnnotation(
                                        startPoint[0], 
                                        startPoint[1], 
                                        point[0], 
                                        point[1], 
                                        new BasicStroke(2.0f), Color.BLACK);
                                if(startPoint[0] != point[0] && startPoint[1] != point[1]){
                                    plot.addAnnotation(lineAnnotationP);
                                }
                            }
                            break;
                    }
                default:
                    break;
            }
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        double point[] = getPointInChart(e);
        if (null != state) {
            switch (state) {
                case MARK:
                    switch (AppFrame.getMarkerType()) {
                        case HORIZONTAL:
                            if (getScreenDataArea().contains(e.getX(), e.getY())) {
                                if (hMarker == null) {
                                    hMarker = new ValueMarker(point[1]);
                                    hMarker.setLabelAnchor(RectangleAnchor.CENTER);
                                    hMarker.setPaint(new Color(0, 0, 0, 200));
                                    hMarker.setStroke(new BasicStroke(2.0f));
                                    plot.addRangeMarker(hMarker);
                                } else {
                                    hMarker.setValue(point[1]);
                                }
                            }else{
                                if (hMarker != null) {
                                    plot.removeRangeMarker(hMarker);
                                    hMarker = null;
                                }
                            }
                            break;
                        case VERTICAL:
                            if (getScreenDataArea().contains(e.getX(), e.getY())) {
                                if (vMarker == null) {
                                    vMarker = new ValueMarker(point[0]);
                                    vMarker.setLabelAnchor(RectangleAnchor.CENTER);
                                    vMarker.setPaint(new Color(0, 0, 0, 200));
                                    vMarker.setStroke(new BasicStroke(2.0f));
                                    plot.addDomainMarker(vMarker);

                                } else {
                                    vMarker.setValue(point[0]);
                                }
                            }else{
                                if (vMarker != null) {
                                    plot.removeDomainMarker(vMarker);
                                    vMarker = null;
                                }
                            }
                            break;
                    }
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
                    if (x >= x1 && x <= x1 + annotation.getText().length() * 7 && y >= y1 && y <= y1 + 20) {//maybe make variable
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
        if (type.equals("region")) {
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
        } else if (type.equals("rect")) {
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
        if (type.equals("region")) {
            for (int i = regionList.size() - 1; i >= 0; i--) {
                RegionStruct r = regionList.get(i);
                if (r.isClickedOn(mouseX, mouseY)) {
                    plot.removeAnnotation(r.getRegion());
                    regionList.remove(i);
                    break;
                }
            }
        } else if (type.equals("rect")) {
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

    public void checkSync(double x1, double x2, double y1, double y2) {
        if (x1 < x2) {
            Controller.sync(x1, x2, y1, y2);
        }
    }

}
