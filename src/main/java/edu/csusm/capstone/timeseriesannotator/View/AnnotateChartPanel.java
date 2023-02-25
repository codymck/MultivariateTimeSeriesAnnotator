package edu.csusm.capstone.timeseriesannotator.View;

import edu.csusm.capstone.timeseriesannotator.Controller.Controller;
import static edu.csusm.capstone.timeseriesannotator.Model.MarkerType.ELLIPSE;
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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYDataset;

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
    private ArrayList<RegionStruct> regionList = new ArrayList<>();
    final List<XYDataset> originalDatasets;
    private boolean syncing = false;

    /* LINE variables */
    private ValueMarker hMarker;
    private ValueMarker vMarker;
    private XYLineAnnotation lineAnnotation;
    private boolean clickedOnce;

    double minX = Double.POSITIVE_INFINITY;
    double maxX = Double.NEGATIVE_INFINITY;
    double minY = Double.POSITIVE_INFINITY;
    double maxY = Double.NEGATIVE_INFINITY;
    private double[] startPoint;

    /* SHAPE variables */
    private double[][] coordinates = { { 0.0, 0.0 }, { 0.0, 0.0 }, { 0.0, 0.0 } };
    private double x, y, width, height;
    private Point2D sPoint, sPoint2;

    /* RECT variables */
    private Rectangle2D.Double rect = null;
    private Hashtable<Rectangle2D, XYShapeAnnotation> rectangleDict = new Hashtable<Rectangle2D, XYShapeAnnotation>();

    /* ELLIPSE variables */
    private Ellipse2D.Double ellipse = null;
    private Hashtable<Ellipse2D, XYShapeAnnotation> ellipseDict = new Hashtable<Ellipse2D, XYShapeAnnotation>();

    /* TRIANGLE variables */
    private Path2D.Double triangle = null;
    private Hashtable<Path2D, XYShapeAnnotation> triangleDict = new Hashtable<Path2D, XYShapeAnnotation>();
    private int triClick = 0;
    private XYLineAnnotation fLine;

    public void setChartState(ToolState s) {
        this.state = s;
    }

    public ToolState getChartState() {
        return this.state;
    }

    public AnnotateChartPanel(JFreeChart chart) {
        super(chart);
        this.chart = chart;
        originalDatasets = new ArrayList<>(chart.getXYPlot().getDatasetCount());
        this.plot = (XYPlot) chart.getPlot();
        this.getMinAndMax();
        setChartState(AppFrame.getAppState());

        chart.addChangeListener(new ChartChangeListener() {
            @Override
            public void chartChanged(ChartChangeEvent cce) {
                if (syncing) {
                    // Disable synchronization temporarily
                    syncing = false;
                    Controller.syncX(chart);
                    syncing = true;
                }
            }

        });
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
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        removeRegion(point[0], point[1]);
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
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        CommentMenu cMenu = new CommentMenu(new javax.swing.JFrame(), true);
                        cMenu.setVisible(true);
                        if (cMenu.isSubmitted() == false) {
                            return;
                        }

                        XYTextAnnotation at = new XYTextAnnotation(cMenu.getComment(), point[0], point[1]);
                        at.setFont(new Font(AppFrame.getFontName(), AppFrame.getFontStyle(), AppFrame.getFontSize()));
                        at.setPaint(AppFrame.getAbsoluteColor());
                        at.setTextAnchor(TextAnchor.TOP_LEFT);
                        plot.addAnnotation(at);
                    }
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        removeTextAnnotation();
                    }
                    // super.mousePressed(e);
                    break;
                case MARK:
                    switch (AppFrame.getMarkerType()) {
                        case HORIZONTAL:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                ValueMarker marker = new ValueMarker(point[1]);
                                marker.setLabelAnchor(RectangleAnchor.CENTER);
                                marker.setPaint(AppFrame.getAbsoluteColor());
                                marker.setStroke(new BasicStroke(2.0f));
                                plot.addRangeMarker(marker);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                Collection<ValueMarker> markers = plot.getRangeMarkers(Layer.FOREGROUND);
                                List<ValueMarker> markerList = new ArrayList<>(markers);
                                for (ValueMarker marker : markerList) {
                                    double y = plot.getRangeAxis().java2DToValue(e.getY(), this.getScreenDataArea(),
                                            plot.getRangeAxisEdge());
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
                                marker.setLabelAnchor(RectangleAnchor.CENTER);
                                marker.setPaint(AppFrame.getAbsoluteColor());
                                marker.setStroke(new BasicStroke(2.0f));
                                plot.addDomainMarker(marker);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                Collection<ValueMarker> markers = plot.getDomainMarkers(Layer.FOREGROUND);
                                List<ValueMarker> markerList = new ArrayList<>(markers);
                                for (ValueMarker marker : markerList) {
                                    double x = plot.getDomainAxis().java2DToValue(e.getX(), this.getScreenDataArea(),
                                            plot.getDomainAxisEdge());
                                    if (x >= marker.getValue() - 3 && x <= marker.getValue() + 3) {
                                        plot.removeDomainMarker(marker);
                                        break;
                                    }
                                }
                            }
                            break;
                        case DIAGONAL:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!clickedOnce) {
                                    startPoint = point;
                                    lineAnnotation = new XYLineAnnotation(
                                            point[0],
                                            point[1],
                                            point[0],
                                            point[1],
                                            new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
                                    plot.addAnnotation(lineAnnotation);
                                    clickedOnce = true;
                                } else {
                                    plot.removeAnnotation(lineAnnotation);
                                    double dx = startPoint[0] - point[0]; // change in x
                                    double dy = startPoint[1] - point[1]; // change in y
                                    double angle = Math.atan2(dy, dx); // angle of line

                                    // double minX = plot.getDomainAxis().getLowerBound(); // minimum x-value of
                                    // plot
                                    // double minY = plot.getRangeAxis().getLowerBound(); // minimum y-value of plot
                                    // double maxX = plot.getDomainAxis().getUpperBound(); // maximum x-value of
                                    // plot
                                    // double maxY = plot.getRangeAxis().getUpperBound(); // maximum y-value of plot

                                    double length = Math.max(maxX - minX, maxY - minY) * 3; // length of line

                                    double x3 = point[0] + length * Math.cos(angle); // x-coordinate of third point
                                    double y3 = point[1] + length * Math.sin(angle); // y-coordinate of third point

                                    double x4 = startPoint[0] - length * Math.cos(angle);
                                    double y4 = startPoint[1] - length * Math.sin(angle);

                                    XYLineAnnotation lineAnnotationP = new XYLineAnnotation(x4, y4, x3, y3,
                                            new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
                                    plot.addAnnotation(lineAnnotationP);
                                    clickedOnce = false;
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                // code for removing
                            }
                            break;
                        case RAY:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!clickedOnce) {
                                    startPoint = point;
                                    lineAnnotation = new XYLineAnnotation(
                                            point[0],
                                            point[1],
                                            point[0],
                                            point[1],
                                            new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
                                    plot.addAnnotation(lineAnnotation);
                                    clickedOnce = true;
                                } else {
                                    plot.removeAnnotation(lineAnnotation);
                                    double dx = startPoint[0] - point[0]; // change in x
                                    double dy = startPoint[1] - point[1]; // change in y
                                    double angle = Math.atan2(dy, dx); // angle of line

                                    double length = Math.max(maxX - minX, maxY - minY) * 3; // length of line

                                    double x4 = startPoint[0] - length * Math.cos(angle);
                                    double y4 = startPoint[1] - length * Math.sin(angle);

                                    XYLineAnnotation lineAnnotationP = new XYLineAnnotation(startPoint[0],
                                            startPoint[1], x4, y4, new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
                                    plot.addAnnotation(lineAnnotationP);
                                    clickedOnce = false;
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                // code for removing
                            }
                            break;
                        case SEGMENT:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!clickedOnce) {
                                    startPoint = point;
                                    lineAnnotation = new XYLineAnnotation(
                                            point[0],
                                            point[1],
                                            point[0],
                                            point[1],
                                            new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
                                    plot.addAnnotation(lineAnnotation);
                                    clickedOnce = true;
                                } else {
                                    plot.removeAnnotation(lineAnnotation);
                                    XYLineAnnotation lineAnnotationP = new XYLineAnnotation(
                                            startPoint[0],
                                            startPoint[1],
                                            point[0],
                                            point[1],
                                            new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
                                    if (startPoint[0] != point[0] && startPoint[1] != point[1]) {
                                        plot.addAnnotation(lineAnnotationP);
                                    }
                                    clickedOnce = false;
                                }
                            }
                            break;
                        case SQUARE:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                sPoint = e.getPoint();
                                coordinates[0][0] = point[0];
                                coordinates[0][1] = point[1];
                                rect = new Rectangle2D.Double(sPoint.getX(), sPoint.getY(), 0, 0);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                Rectangle2D rec = removeRect(point);
                                if (rec != null) {
                                    XYShapeAnnotation sh = rectangleDict.get(rec);
                                    rectangleDict.remove(rec, sh);
                                    plot.removeAnnotation(sh);
                                }
                            }
                            break;
                        case ELLIPSE:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                sPoint = e.getPoint();
                                coordinates[0][0] = point[0];
                                coordinates[0][1] = point[1];
                                ellipse = new Ellipse2D.Double(sPoint.getX(), sPoint.getY(), 0, 0);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                Ellipse2D ell = removeEllipse(point);
                                if (ell != null) {
                                    XYShapeAnnotation sh = ellipseDict.get(ell);
                                    ellipseDict.remove(ell, sh);
                                    plot.removeAnnotation(sh);
                                }
                            }
                            break;
                        case TRIANGLE:
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                switch (triClick) {
                                    case 0 -> {
                                        sPoint = e.getPoint();
                                        coordinates[0][0] = point[0];
                                        coordinates[0][1] = point[1];
                                        fLine = new XYLineAnnotation(
                                                point[0],
                                                point[1],
                                                point[0],
                                                point[1],
                                                new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
                                        plot.addAnnotation(fLine);
                                        triClick++;
                                    }
                                    case 1 -> {
                                        plot.removeAnnotation(fLine);
                                        sPoint2 = e.getPoint();
                                        coordinates[1][0] = point[0];
                                        coordinates[1][1] = point[1];
                                        // System.out.println("sPointX: " + sPoint.getX() + " sPointY: " +
                                        // sPoint.getY());
                                        // System.out.println("CoordinateX " + coordinates[0][0] + " sPointY: " +
                                        // coordinates[0][1]);
                                        triangle = new Path2D.Double();
                                        triangle.moveTo(sPoint.getX(), sPoint.getY());
                                        triangle.lineTo(sPoint2.getX(), sPoint2.getY());
                                        triangle.lineTo(sPoint2.getX(), sPoint2.getY());
                                        triangle.closePath();
                                        triClick++;
                                    }
                                    case 2 -> {
                                        coordinates[2][0] = point[0];
                                        coordinates[2][1] = point[1];
                                        triangle = new Path2D.Double();
                                        triangle.moveTo(coordinates[0][0], coordinates[0][1]);
                                        triangle.lineTo(coordinates[1][0], coordinates[1][1]);
                                        triangle.lineTo(coordinates[2][0], coordinates[2][1]);
                                        triangle.closePath();
                                        XYShapeAnnotation triangleA = new XYShapeAnnotation(triangle,
                                                new BasicStroke(2), new Color(0, 0, 0, 0), color);
                                        triangleDict.put(triangle, triangleA);
                                        triangle = null;
                                        plot.addAnnotation(triangleA);
                                        repaint();
                                        triClick = 0;
                                    }
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                Path2D tr = removeTriangle(point);
                                if (tr != null) {
                                    XYShapeAnnotation sh = triangleDict.get(tr);
                                    triangleDict.remove(tr, sh);
                                    plot.removeAnnotation(sh);
                                }
                            }
                            break;
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
                            x = screenDataArea.getMinX();
                        }
                        if (x + width > screenDataArea.getMaxX()) {
                            width = (screenDataArea.getMaxX() - x);
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
                                    x = screenDataArea.getMinX();
                                }
                                if (y < screenDataArea.getMinY()) {
                                    height -= screenDataArea.getMinY() - y;
                                    y = screenDataArea.getMinY();
                                }
                                if (x + width > screenDataArea.getMaxX()) {
                                    width = (screenDataArea.getMaxX() - x);
                                }
                                if (y + height > screenDataArea.getMaxY()) {
                                    height = (screenDataArea.getMaxY() - y);
                                }

                                rect.setRect(x, y, width, height);
                                repaint();
                            }
                            break;
                        case ELLIPSE:
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                Point2D endPoint = e.getPoint();
                                x = Math.min(sPoint.getX(), endPoint.getX());
                                y = Math.min(sPoint.getY(), endPoint.getY());
                                width = Math.abs(sPoint.getX() - endPoint.getX());
                                height = Math.abs(sPoint.getY() - endPoint.getY());

                                // make sure it doesn't overflow the bounds of the chart
                                Rectangle2D screenDataArea = getScreenDataArea();
                                if (x < screenDataArea.getMinX()) {
                                    width -= screenDataArea.getMinX() - x;
                                    x = screenDataArea.getMinX();
                                }
                                if (y < screenDataArea.getMinY()) {
                                    height -= screenDataArea.getMinY() - y;
                                    y = screenDataArea.getMinY();
                                }
                                if (x + width > screenDataArea.getMaxX()) {
                                    width = (screenDataArea.getMaxX() - x);
                                }
                                if (y + height > screenDataArea.getMaxY()) {
                                    height = (screenDataArea.getMaxY() - y);
                                }

                                ellipse.setFrame(x, y, width, height);
                                repaint();
                            }
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
                        addRegion();
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
                    break;
                case MARK:
                    switch (AppFrame.getMarkerType()) {
                        case SQUARE:
                            if (e.getButton() == MouseEvent.BUTTON1 && rect != null) {
                                coordinates[1][0] = point[0];
                                coordinates[1][1] = point[1];
                                x = Math.min(coordinates[0][0], coordinates[1][0]);
                                y = Math.min(coordinates[0][1], coordinates[1][1]);
                                width = Math.abs(coordinates[1][0] - coordinates[0][0]);
                                height = Math.abs(coordinates[1][1] - coordinates[0][1]);
                                rect.setFrame(x, y, width, height);
                                XYShapeAnnotation rectA = new XYShapeAnnotation(rect, new BasicStroke(2),
                                        new Color(0, 0, 0, 0), color);
                                rectangleDict.put(rect, rectA);
                                rect = null;
                                plot.addAnnotation(rectA);
                                repaint();
                            }
                            break;
                        case ELLIPSE:
                            if (e.getButton() == MouseEvent.BUTTON1 && ellipse != null) {
                                coordinates[1][0] = point[0];
                                coordinates[1][1] = point[1];
                                x = Math.min(coordinates[0][0], coordinates[1][0]);
                                y = Math.min(coordinates[0][1], coordinates[1][1]);
                                width = Math.abs(coordinates[1][0] - coordinates[0][0]);
                                height = Math.abs(coordinates[1][1] - coordinates[0][1]);
                                ellipse.setFrame(x, y, width, height);

                                XYShapeAnnotation ellipseA = new XYShapeAnnotation(ellipse, new BasicStroke(2),
                                        new Color(0, 0, 0, 0), color);
                                ellipseDict.put(ellipse, ellipseA);

                                plot.addAnnotation(ellipseA);
                                ellipse = null;
                                repaint();
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
                                    hMarker.setPaint(AppFrame.getAbsoluteColor());
                                    hMarker.setStroke(new BasicStroke(2.0f));
                                    plot.addRangeMarker(hMarker);
                                } else {
                                    hMarker.setValue(point[1]);
                                }
                            } else {
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
                                    vMarker.setPaint(AppFrame.getAbsoluteColor());
                                    vMarker.setStroke(new BasicStroke(2.0f));
                                    plot.addDomainMarker(vMarker);

                                } else {
                                    vMarker.setValue(point[0]);
                                }
                            } else {
                                if (vMarker != null) {
                                    plot.removeDomainMarker(vMarker);
                                    vMarker = null;
                                }
                            }
                            break;
                        case DIAGONAL:
                            if (clickedOnce) {
                                plot.removeAnnotation(lineAnnotation);
                                double dx = startPoint[0] - point[0]; // change in x
                                double dy = startPoint[1] - point[1]; // change in y
                                double angle = Math.atan2(dy, dx); // angle of line

                                double length = Math.max(maxX - minX, maxY - minY) * 3; // length of line

                                double x3 = point[0] + length * Math.cos(angle); // x-coordinate of third point
                                double y3 = point[1] + length * Math.sin(angle); // y-coordinate of third point

                                double x4 = startPoint[0] - length * Math.cos(angle);
                                double y4 = startPoint[1] - length * Math.sin(angle);

                                lineAnnotation = new XYLineAnnotation(x4, y4, x3, y3, new BasicStroke(2.0f),
                                        AppFrame.getAbsoluteColor());
                                plot.addAnnotation(lineAnnotation);
                                repaint();
                            }
                            break;
                        case RAY:
                            if (clickedOnce) {
                                plot.removeAnnotation(lineAnnotation);
                                double dx = startPoint[0] - point[0]; // change in x
                                double dy = startPoint[1] - point[1]; // change in y
                                double angle = Math.atan2(dy, dx); // angle of line

                                double length = Math.max(maxX - minX, maxY - minY) * 3; // length of line

                                double x4 = startPoint[0] - length * Math.cos(angle);
                                double y4 = startPoint[1] - length * Math.sin(angle);

                                lineAnnotation = new XYLineAnnotation(x4, y4, startPoint[0], startPoint[1],
                                        new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
                                plot.addAnnotation(lineAnnotation);
                                repaint();
                            }
                            break;
                        case SEGMENT:
                            if (clickedOnce) {
                                plot.removeAnnotation(lineAnnotation);
                                lineAnnotation = new XYLineAnnotation(
                                        startPoint[0],
                                        startPoint[1],
                                        point[0],
                                        point[1],
                                        new BasicStroke(2.0f), AppFrame.getAbsoluteColor());
                                plot.addAnnotation(lineAnnotation);
                                repaint();
                            }
                            break;
                        case TRIANGLE:
                            if (triClick == 1) {
                                plot.removeAnnotation(fLine);
                                fLine = new XYLineAnnotation(
                                        coordinates[0][0],
                                        coordinates[0][1],
                                        point[0],
                                        point[1],
                                        new BasicStroke(2.0f), color);
                                plot.addAnnotation(fLine);
                            } else if (triClick == 2) {
                                x = e.getPoint().getX();
                                y = e.getPoint().getY();
                                // make sure it doesn't overflow the bounds of the chart
                                Rectangle2D screenDataArea = getScreenDataArea();
                                if (x < screenDataArea.getMinX()) {
                                    x = screenDataArea.getMinX();
                                }
                                if (y < screenDataArea.getMinY()) {
                                    y = screenDataArea.getMinY();
                                }
                                if (x > screenDataArea.getMaxX()) {
                                    x = screenDataArea.getMaxX();
                                }
                                if (y > screenDataArea.getMaxY()) {
                                    y = screenDataArea.getMaxY();
                                }
                                triangle = new Path2D.Double();
                                triangle.moveTo(sPoint.getX(), sPoint.getY());
                                triangle.lineTo(sPoint2.getX(), sPoint2.getY());
                                triangle.lineTo(x, y);
                                triangle.closePath();
                                repaint();
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
        if (ellipse != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.fill(ellipse);
        }
        if (triangle != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.fill(triangle);
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
                    if (x >= x1 && x <= x1 + annotation.getText().length() * 7 && y >= y1 && y <= y1 + 20) {// maybe
                                                                                                            // make
                                                                                                            // variable
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

    public void addRegion() {
        double upperLeftX = Math.min(coordinates[0][0], coordinates[1][0]);
        double lowerRightX = Math.max(coordinates[0][0], coordinates[1][0]);
        IntervalMarker intervalMarker = new IntervalMarker(upperLeftX, lowerRightX, AppFrame.getAbsoluteColor());
        // setting alpha value to "60" ((60 / 255) ≈ 0.235)
        intervalMarker.setAlpha(0.235f);
        plot.addDomainMarker(intervalMarker);
        regionList.add(new RegionStruct(upperLeftX, 0, lowerRightX, 0, intervalMarker, "region"));
    }

    public void removeRegion(double mouseX, double mouseY) {
        for (int i = regionList.size() - 1; i >= 0; i--) {
            RegionStruct r = regionList.get(i);
            if (r.isClickedOn(mouseX, mouseY)) {
                plot.removeDomainMarker(r.getRegion());
                regionList.remove(i);
                break;
            }
        }
    }

    public double[] getPointInChart(MouseEvent e) {
        Insets insets = getInsets();
        int mouseX = (int) ((e.getX() - insets.left) / this.getScaleX());
        int mouseY = (int) ((e.getY() - insets.top) / this.getScaleY());
        // System.out.println("x = " + mouseX + ", y = " + mouseY);

        Point2D p = this.translateScreenToJava2D(new Point(mouseX, mouseY));
        ChartRenderingInfo info = this.getChartRenderingInfo();
        Rectangle2D dataArea = info.getPlotInfo().getDataArea();

        ValueAxis domainAxis = plot.getDomainAxis();
        RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
        ValueAxis rangeAxis = plot.getRangeAxis();
        RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();

        double chartX = domainAxis.java2DToValue(p.getX(), dataArea, domainAxisEdge);
        double chartY = rangeAxis.java2DToValue(p.getY(), dataArea, rangeAxisEdge);
        // System.out.println("Chart: x = " + chartX + ", y = " + chartY);
        double[] r = { chartX, chartY };
        return r;
    }

    public void setColor(Color c) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        color = new Color(r, g, b, 60);
    }

    public void setSync(boolean s) {
        syncing = s;
    }

    private Ellipse2D removeEllipse(double[] point) {
        Enumeration<Ellipse2D> e = ellipseDict.keys();

        Point2D p = new Point2D.Double(point[0], point[1]);

        while (e.hasMoreElements()) {
            Ellipse2D ell = e.nextElement();

            if (ell.contains(p)) {
                return ell;
            }
        }
        return null;
    }

    private Rectangle2D removeRect(double[] point) {
        Enumeration<Rectangle2D> e = rectangleDict.keys();

        Point2D p = new Point2D.Double(point[0], point[1]);

        while (e.hasMoreElements()) {
            Rectangle2D rec = e.nextElement();

            if (rec.contains(p)) {
                return rec;
            }
        }
        return null;
    }

    private Path2D removeTriangle(double[] point) {
        Enumeration<Path2D> e = triangleDict.keys();

        Point2D p = new Point2D.Double(point[0], point[1]);

        while (e.hasMoreElements()) {
            Path2D tri = e.nextElement();

            if (tri.contains(p)) {
                return tri;
            }
        }
        return null;
    }

    private void getMinAndMax() {
        XYDataset dataset = plot.getDataset();
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            for (int j = 0; j < dataset.getItemCount(i); j++) {
                double x = dataset.getXValue(i, j);
                double y = dataset.getYValue(i, j);
                if (x < minX) {
                    minX = x;
                }
                if (x > maxX) {
                    maxX = x;
                }
                if (y < minY) {
                    minY = y;
                }
                if (y > maxY) {
                    maxY = y;
                }
            }
        }
        System.out.println("Minimum X: " + minX);
        System.out.println("Maximum X: " + maxX);
        System.out.println("Minimum Y: " + minY);
        System.out.println("Maximum Y: " + maxY);
    }
}
