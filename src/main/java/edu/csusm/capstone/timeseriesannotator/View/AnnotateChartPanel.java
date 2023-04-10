package edu.csusm.capstone.timeseriesannotator.View;

import org.jumpmind.symmetric.csv.CsvWriter;
import org.jumpmind.symmetric.csv.CsvReader;
import edu.csusm.capstone.timeseriesannotator.Controller.Controller;
import edu.csusm.capstone.timeseriesannotator.Controller.Tools.*;
import static edu.csusm.capstone.timeseriesannotator.Model.MarkerType.ELLIPSE;
import static edu.csusm.capstone.timeseriesannotator.Model.MarkerType.SQUARE;
import edu.csusm.capstone.timeseriesannotator.Model.ToolState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.json.Json;
import javax.json.JsonArray;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.Range;

/**
 *
 * @author Ben Theurich
 * @author Cody McKinney
 */
public class AnnotateChartPanel extends ChartPanel implements MouseListener {

    private ToolState state;
    private JFreeChart chart = null;
    Color color = new Color(0, 0, 0, 60);
    private XYPlot plot;
    final List<XYDataset> originalDatasets;
    private boolean syncing = false;

    private boolean panLimit = false;
    private double initialX;
    private double initialY;
    private Point strPoint;

    private RectangleAnnotation Hr;
    private RectangleAnnotation r;
    private EllipseAnnotation ell;
    private TriangleAnnotation tri;
    private LineAnnotation sl;
    private LineAnnotation lr;
    private LineAnnotation dl;
    private HVLineAnnotation horiz;
    private HVLineAnnotation vert;
    private boolean dragged = false;

    public AbstractAnnotation currentAnnotation = null;
    private boolean clickedInAnnotation = false;
    private boolean alreadySelected = false;
    
    /* LINE variables */
    private HVLineAnnotation hTrace;
    private HVLineAnnotation vTrace;
    private boolean clickedOnce;

    public double[] minMax = {0.0, 0.0, 0.0, 0.0}; // minX, minY, maxX, maxY

    /* SHAPE variables */
    private ArrayList<AbstractAnnotation> annotations = new ArrayList<>();
    private int shapeIndex = 0;
    private double[] moveTest = {0.0, 0.0};
    private boolean moved;

    /* TRIANGLE variables */
    private int triClick = 0;
    
    private Timer timer;
    private boolean commentBoxNeedsUpdate = false;

    public void setChartState(ToolState s) {
        this.state = s;
    }

    public ToolState getChartState() {
        return this.state;
    }

    public void addAbstractAnnotation(AbstractAnnotation a) {
        shapeIndex = annotations.size();
        annotations.add(a);
    }

    public void removeAbstractAnnotation(AbstractAnnotation a) {
        annotations.remove(a);
        shapeIndex = annotations.size() - 1;
    }

    public AnnotateChartPanel(JFreeChart chart) {
        super(chart);
        this.chart = chart;
        originalDatasets = new ArrayList<>(chart.getXYPlot().getDatasetCount());
        this.plot = (XYPlot) chart.getPlot();
        this.getMinAndMax();
        setChartState(AppFrame.getAppState());
        chart.setBackgroundPaint(new java.awt.Color(242, 242, 242));

        hTrace = new HVLineAnnotation(plot, color, "horizontal", minMax, this);
        vTrace = new HVLineAnnotation(plot, color, "vertical", minMax, this);
        
        this.addComponentListener(new ComponentAdapter() {
            private double previousScreenWidth = Double.NaN;
            private double previousScreenHeight = Double.NaN;
            public void componentResized(ComponentEvent e) {
                // Get the current screen width and height values
                Rectangle2D.Double screenDataArea = (Rectangle2D.Double) getScreenDataArea();
                double currentScreenWidth = screenDataArea.getMaxX() - screenDataArea.getMinX();
                double currentScreenHeight = screenDataArea.getMaxY() - screenDataArea.getMinY();
                // Check if the domain or range values have changed by more than a small threshold value
                double epsilon = 0.00001;
                if (Math.abs(previousScreenWidth - currentScreenWidth) > epsilon || Math.abs(previousScreenHeight - currentScreenHeight) > epsilon) {
                    // If the values have changed, set a flag to indicate that the comment box needs to be updated
                    commentBoxNeedsUpdate = true;

                    // Schedule a timer to update the comment box after a delay of 10 milliseconds
                    if (timer != null) {
                        timer.cancel();
                    }
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            if (commentBoxNeedsUpdate) {
                                redrawCommentBox();
                                redrawHandles();
                                commentBoxNeedsUpdate = false;
                            }
                        }
                    }, 5);
                }

                // Store the current domain and range values for comparison in the next iteration
                previousScreenWidth = currentScreenWidth;
                previousScreenHeight = currentScreenHeight;
            }
        });
        
        chart.addChangeListener(new ChartChangeListener() {
            private double previousDomain = Double.NaN;
            private double previousRange = Double.NaN;

            @Override
            public void chartChanged(ChartChangeEvent cce) {
                // Get the current domain and range values
                double currentDomain = plot.getDomainAxis().getUpperBound() - plot.getDomainAxis().getLowerBound();
                double currentRange = plot.getRangeAxis().getUpperBound() - plot.getRangeAxis().getLowerBound();

                // Check if the domain or range values have changed by more than a small threshold value
                double epsilon = 0.00001;
                if (Math.abs(previousDomain - currentDomain) > epsilon || Math.abs(previousRange - currentRange) > epsilon) {
                    // If the values have changed, set a flag to indicate that the comment box needs to be updated
                    commentBoxNeedsUpdate = true;

                    // Schedule a timer to update the comment box after a delay of 10 milliseconds
                    if (timer != null) {
                        timer.cancel();
                    }
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            if (commentBoxNeedsUpdate) {
                                redrawCommentBox();
                                redrawHandles();
                                commentBoxNeedsUpdate = false;
                            }
                        }
                    }, 5);
                }

                // Store the current domain and range values for comparison in the next iteration
                previousDomain = currentDomain;
                previousRange = currentRange;
                if (syncing) {
                    // Disable synchronization temporarily
                    syncing = false;
                    synchronized (this) {
                        Controller.syncX(chart);
                    }
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
                case SELECT -> {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        moveTest = point;
                        moved = false;
                        if(currentAnnotation != null && currentAnnotation.clickedOn(point[0], point[1])){
                            clickedInAnnotation = true;
                            alreadySelected = true;
                        }else{
                            selectAnnotation(point[0], point[1]);
                        }
                    }
                }
                case ZOOM -> {

                    if (syncing == true) {
                        setMouseZoomable(true, true);
                        this.setZoomFillPaint(new Color(0, 0, 0, 40));
                        this.setRangeZoomable(false);
                    } else {
                        setMouseZoomable(true, false);
                        this.setRangeZoomable(true);
                    }

                    super.mousePressed(e);
                }
                case PAN -> {
                    if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
                        initialX = point[0];
                        initialY = point[1];
                        strPoint = e.getPoint();
                    }
                }
                case HIGHLIGHT -> {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Hr = new RectangleAnnotation(plot, color, point, "region", minMax, this);
//                        shapeIndex = annotations.size();
//                        annotations.add(Hr);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        deleteAnnotation(point);
                    }
                }
                case COMMENT -> {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Font f = new Font(AppFrame.getFontName(), AppFrame.getFontStyle(), AppFrame.getFontSize());
                        CommentAnnotation comment = new CommentAnnotation(plot, color, point, this, f);
                        shapeIndex = annotations.size();
//                        annotations.add(comment);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        deleteAnnotation(point);
                    }
                }
                case MARK -> {
                    switch (AppFrame.getMarkerType()) {
                        case VERTICAL -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                vert = new HVLineAnnotation(plot, AppFrame.getAbsoluteColor(), "vertical", minMax, this);
                                vert.createLine(point);
                                shapeIndex = annotations.size();
                                annotations.add(vert);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                            if(e.getClickCount() == 2){
                                vert.delete();
                                removeAbstractAnnotation(vert);
                                vert = (HVLineAnnotation) annotations.get(shapeIndex);
                                vert.delete();
                                removeAbstractAnnotation(vert);
                            }
                        }
                        case HORIZONTAL -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                horiz = new HVLineAnnotation(plot, AppFrame.getAbsoluteColor(), "horizontal", minMax, this);
                                horiz.createLine(point);
                                shapeIndex = annotations.size();
                                annotations.add(horiz);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                            if(e.getClickCount() == 2){
                                horiz.delete();
                                removeAbstractAnnotation(horiz);
                                horiz = (HVLineAnnotation) annotations.get(shapeIndex);
                                horiz.delete();
                                removeAbstractAnnotation(horiz);
                            }
                        }
                        case DIAGONAL -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!clickedOnce) {
                                    clickedOnce = true;
                                    dl = new LineAnnotation(plot, AppFrame.getAbsoluteColor(), point, "diagonal", minMax, this);
//                                    shapeIndex = annotations.size();
//                                    annotations.add(dl);
                                } else {
                                    clickedOnce = false;
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (!clickedOnce) {
                                    deleteAnnotation(point);
                                }
                            }
                            if(e.getClickCount() == 2 && dl != null){
                                dl.delete();
                                clickedOnce = false;
                            }
                        }
                        case RAY -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!clickedOnce) {
                                    clickedOnce = true;
                                    lr = new LineAnnotation(plot, AppFrame.getAbsoluteColor(), point, "ray", minMax, this);
//                                    shapeIndex = annotations.size();
//                                    annotations.add(lr);
                                } else {
                                    clickedOnce = false;
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (!clickedOnce) {
                                    deleteAnnotation(point);
                                }
                            }
                            if(e.getClickCount() == 2 && lr != null){
                                lr.delete();
                                clickedOnce = false;
                            }
                        }
                        case SEGMENT -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!clickedOnce) {
                                    clickedOnce = true;
                                    sl = new LineAnnotation(plot, AppFrame.getAbsoluteColor(), point, "segment", minMax, this);
//                                    shapeIndex = annotations.size();
//                                    annotations.add(sl);
                                } else {
                                    clickedOnce = false;
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (!clickedOnce) {
                                    deleteAnnotation(point);
                                }
                            }
                            if(e.getClickCount() == 2 && sl != null){
                                sl.delete();
                                clickedOnce = false;
                            }
                        }
                        case SQUARE -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                r = new RectangleAnnotation(plot, color, point, "rectangle", minMax, this);
//                                shapeIndex = annotations.size();
//                                annotations.add(r);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                        }
                        case ELLIPSE -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                ell = new EllipseAnnotation(plot, color, point, this);
//                                shapeIndex = annotations.size();
//                                annotations.add(ell);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                        }
                        case TRIANGLE -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (triClick == 0) {
                                    moveTest = point;
                                    tri = new TriangleAnnotation(plot, color, this);
//                                    shapeIndex = annotations.size();
//                                    annotations.add(tri);
                                    tri.createShape(point);
                                    triClick++;
                                } else {
//                                    TriangleAnnotation tempTri = (TriangleAnnotation) annotations.get(shapeIndex);
                                    tri.createShape(point);
                                    triClick++;
                                    if (triClick > 2) {
                                        if(e.getClickCount() == 2)removeAbstractAnnotation(tri);
                                        triClick = 0;
                                    }
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (triClick == 0) {
                                    deleteAnnotation(point);
                                }
                            }
                            if(e.getClickCount() == 2){
                                tri.delete();
                                triClick = 0;
                            }
                        }
                    }
                }
                default -> {
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double point[] = getPointInChart(e);
        if (null != state) {
            switch (state) {
                case PAN -> {

                    if (strPoint != null) {

                        double deltaX = e.getX() - strPoint.x; //pan amount
                        double deltaY = e.getY() - strPoint.y; // pan amount

                        //pan info
                        Range domainRange = chart.getXYPlot().getDomainAxis().getRange();
                        double domainLength = domainRange.getLength();
                        Range rangeRange = chart.getXYPlot().getRangeAxis().getRange();
                        double rangeLength = rangeRange.getLength();

                        double deltaXValue = domainLength * deltaX / getWidth();
                        double deltaYValue = rangeLength * deltaY / getHeight();

                        double currentX = point[0]; //gets X coord
                        double currentY = point[1]; //gets Y coord
                        double absX = initialX - currentX;// (-) pan left, 0 nothing (+) pan right
                        double absY = initialY - currentY;// (-) pan down, 0 nothing, (+) pan up

                        if (absY > 0 && !(plot.getRangeAxis().getUpperBound() >= minMax[3] * 3)) {
                            //Pan up
                            getChart().getXYPlot().getRangeAxis().setRange(getChart().getXYPlot().getRangeAxis().getLowerBound() + deltaYValue,
                                    getChart().getXYPlot().getRangeAxis().getUpperBound() + deltaYValue);
                        }
                        if (absY < 0 && !(plot.getRangeAxis().getLowerBound() <= (-minMax[3] * 3))) {
                            //Pan down
                            getChart().getXYPlot().getRangeAxis().setRange(getChart().getXYPlot().getRangeAxis().getLowerBound() + deltaYValue,
                                    getChart().getXYPlot().getRangeAxis().getUpperBound() + deltaYValue);
                        }
                        if (absX > 0 && !(plot.getDomainAxis().getUpperBound() >= minMax[2] * 3)) {
                            //Pan right
                            getChart().getXYPlot().getDomainAxis().setRange(getChart().getXYPlot().getDomainAxis().getLowerBound() - deltaXValue,
                                    getChart().getXYPlot().getDomainAxis().getUpperBound() - deltaXValue);
                        }
                        if (absX < 0 && !(plot.getDomainAxis().getLowerBound() <= (-minMax[2] * 3))) {
                            //Pan left
                            getChart().getXYPlot().getDomainAxis().setRange(getChart().getXYPlot().getDomainAxis().getLowerBound() - deltaXValue,
                                    getChart().getXYPlot().getDomainAxis().getUpperBound() - deltaXValue);
                        }
                    }
                    strPoint = e.getPoint();
                }
                case SELECT -> {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        moved = true;
                        double xOffset = moveTest[0] - point[0];
                        double yOffset = moveTest[1] - point[1];
                        if(clickedInAnnotation){
                            currentAnnotation.move(xOffset, yOffset, false);
                        }
                    }
                }
                case HIGHLIGHT -> {
                    if (SwingUtilities.isLeftMouseButton(e)) {
//                        RectangleAnnotation tempRect = (RectangleAnnotation) annotations.get(shapeIndex);
                        Hr.drawRect(point);
                        dragged = true;
                    }
                }
                case MARK -> {
                    switch (AppFrame.getMarkerType()) {
                        case SQUARE -> {
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                //RectangleAnnotation tempRect = (RectangleAnnotation) annotations.get(shapeIndex);
                                r.drawRect(point);
                                dragged = true;
                            }
                        }
                        case ELLIPSE -> {
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                //EllipseAnnotation tempEll = (EllipseAnnotation) annotations.get(shapeIndex);
                                ell.drawEllipse(point);
                                dragged = true;
                            }
                        }
                        default -> {
                        }
                    }
                }

                default ->
                    super.mouseDragged(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        double point[] = getPointInChart(e);
        if (null != state) {
            switch (state) {
                case SELECT -> {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        double xOffset = moveTest[0] - point[0];
                        double yOffset = moveTest[1] - point[1];
                        if(clickedInAnnotation){
                            currentAnnotation.move(xOffset, yOffset, true);
                        }
                        if (!moved && alreadySelected) {
                            currentAnnotation.deselect();
                            currentAnnotation = null;
                        }
                        clickedInAnnotation = false;
                        alreadySelected = false;
                    }
                }
                case ZOOM ->
                    super.mouseReleased(e);
                case PAN -> {
                    if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
                        strPoint = null;
                    }
                    super.mouseReleased(e);
                }
                case HIGHLIGHT -> {
//                    if(dragged){
//                        annotations.add(Hr);
//                        dragged = false;
//                    }
                }
                case COMMENT -> {
                }
                case MARK -> {
//                    switch (AppFrame.getMarkerType()) {
//                        case SQUARE -> {
//                            if(dragged){
//                                annotations.add(r);
//                                dragged = false;
//                            }
//                        }
//                        case ELLIPSE -> {
//                            if(dragged){
////                                annotations.add(ell);
//                                dragged = false;
//                            }
//                        }
//                    }
                }
                default -> {
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        double point[] = getPointInChart(e);
        if (null != state) {
            switch (state) {
                case MARK -> {
                    switch (AppFrame.getMarkerType()) {
                        case VERTICAL -> {
                            if (getScreenDataArea().contains(e.getX(), e.getY())) {
                                if (vTrace != null) {
                                    vTrace.drawTrace(point);
                                }

                            } else {
                                vTrace.removeTrace();
                            }
                        }
                        case HORIZONTAL -> {
                            if (getScreenDataArea().contains(e.getX(), e.getY())) {
                                if (hTrace != null) {
                                    hTrace.drawTrace(point);
                                }
                            } else {
                                hTrace.removeTrace();
                            }
                        }
                        case DIAGONAL -> {
                            if (clickedOnce) {
//                                LineAnnotation tempLine = (LineAnnotation) annotations.get(shapeIndex);
                                dl.drawLine(point);
                            }
                        }
                        case RAY -> {
                            if (clickedOnce) {
//                                LineAnnotation tempLine = (LineAnnotation) annotations.get(shapeIndex);
                                lr.drawLine(point);
                            }
                        }
                        case SEGMENT -> {
                            if (clickedOnce) {
//                                LineAnnotation tempLine = (LineAnnotation) annotations.get(shapeIndex);
                                sl.drawLine(point);
                            }
                        }
                        case TRIANGLE -> {
                            if (triClick > 0 && e.getClickCount() != 2) {
//                                TriangleAnnotation tempTri = (TriangleAnnotation) annotations.get(shapeIndex);
                                tri.drawTriangle(point);
                            }
                        }
                        default -> {
                        }
                    }
                }
                default -> {
                    hTrace.removeTrace();
                    vTrace.removeTrace();
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        vTrace.removeTrace();
        hTrace.removeTrace();
        if (clickedOnce) {
            clickedOnce = false;
            LineAnnotation tempLine = (LineAnnotation) annotations.get(shapeIndex);
            tempLine.delete();
        }
        if (triClick > 0) {
            triClick = 0;
//            TriangleAnnotation tempTri = (TriangleAnnotation) annotations.get(shapeIndex);
            tri.delete();
        }
    }

//    public void removeTextAnnotation() {
//        List<XYTextAnnotation> annotations = new ArrayList<>(plot.getAnnotations());
//        List<XYTextAnnotation> textAnnotations = new ArrayList<>();
//        for (XYAnnotation annotation : annotations) {
//            if (annotation instanceof XYTextAnnotation xYTextAnnotation) {
//                textAnnotations.add(xYTextAnnotation);
//            }
//        }
//
//        this.addChartMouseListener(new ChartMouseListener() {
//            @Override
//            public void chartMouseClicked(ChartMouseEvent event) {
//                int x = event.getTrigger().getX();
//                int y = event.getTrigger().getY();
//                for (XYTextAnnotation annotation : textAnnotations) {
//                    ChartRenderingInfo info = getChartRenderingInfo();
//                    Rectangle2D bounds = info.getPlotInfo().getDataArea();
//                    double x1 = plot.getDomainAxis().valueToJava2D(annotation.getX(), bounds, plot.getDomainAxisEdge());
//                    double y1 = plot.getRangeAxis().valueToJava2D(annotation.getY(), bounds, plot.getRangeAxisEdge());
//                    if (x >= x1 && x <= x1 + annotation.getText().length() * 7 && y >= y1 && y <= y1 + 20) {
//                        plot.removeAnnotation(annotation);
//                        break;
//                    }
//                }
//                annotations.clear();
//                textAnnotations.clear();
//            }
//
//            @Override
//            public void chartMouseMoved(ChartMouseEvent event) {
//            }
//        });
//    }
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
        double[] r = {chartX, chartY};
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

    private void deleteAnnotation(double[] point) {
        for (int i = annotations.size() - 1; i >= 0; i--) {
            if (annotations.get(i).clickedOn(point[0], point[1])) {
//                System.out.println("CLICKED");
                annotations.get(i).delete();
                annotations.remove(i);
                break;
            }
        }
    }

    public void exportAnnotations() throws IOException {
        JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save annotations to");
        fileChooser.setSelectedFile(new File(".csv"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV", "csv"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        int userSelection = fileChooser.showSaveDialog(frame);
        File fileToSave;

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            fileToSave = fileChooser.getSelectedFile();

            String name = fileToSave.getName();
            char c;
            String fileType = "";

            // loop through file name from the end
            for (int i = name.length() - 1; i >= 0; i--) {
                c = name.charAt(i);
                // when we reach a '.' it is the end of file type
                if (c == '.') {
                    break;
                }
                // append character to fileType
                fileType = c + fileType;
            }

            if (fileType.equalsIgnoreCase("csv")) {
                // CREATE CSVWriter
                CsvWriter writer = new CsvWriter(new FileWriter(fileToSave, false), ',');
                String[] header = {"Annotation Type", "RGBA", "Coordinates", "Data"};
                writer.writeRecord(header);
                for (int i = 0; i < annotations.size(); i++) {
                    writer.writeRecord(annotations.get(i).export());
                }
                writer.close();
            } else {
                ErrorDialog.badFileType();
            }
        }
    }

    public void importAnnotations() throws IOException {
        JFrame frame = AppFrame.frame;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to load annotations from");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV", "csv"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        int userSelection = fileChooser.showOpenDialog(frame);
        File fileToLoad;

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            fileToLoad = fileChooser.getSelectedFile();

            String name = fileToLoad.getAbsolutePath();
            char c;
            String fileType = "";

            // loop through file name from the end
            for (int i = name.length() - 1; i >= 0; i--) {
                c = name.charAt(i);
                // when we reach a '.' it is the end of file type
                if (c == '.') {
                    break;
                }
                // append character to fileType
                fileType = c + fileType;
            }

            if (fileType.equalsIgnoreCase("csv")) {
                // CREATE CsvReader
                CsvReader reader = new CsvReader(name);
                reader.readHeaders();
                // loop through every row
                while (reader.readRecord()) {
                    String annotationType = reader.get("Annotation Type");
//                    System.out.println("Annotation Type: " + annotationType);

                    String rgbaString = reader.get("RGBA");
                    JsonArray jsonArray = Json.createReader(new StringReader(rgbaString)).readArray();
                    int[] rgba = new int[jsonArray.size()];
                    for (int i = 0; i < rgba.length; i++) {
                        rgba[i] = jsonArray.getInt(i);
                    }
//                    System.out.println("RGBA:");
//                    Arrays.stream(rgba).forEach(System.out::println);

                    String coordinatesString = reader.get("Coordinates");
                    jsonArray = Json.createReader(new StringReader(coordinatesString)).readArray();
                    double[][] coordinates = new double[jsonArray.size()][((JsonArray) jsonArray.get(0)).size()];
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonArray row = jsonArray.getJsonArray(i);
                        for (int j = 0; j < row.size(); j++) {
                            coordinates[i][j] = row.getJsonNumber(j).doubleValue();
                        }
                    }
//                    System.out.println("Coordinates:");
//                    for (double[] row : coordinates) {
//                        System.out.println(Arrays.toString(row));
//                    }

                    String dataString = reader.get("Data");
                    jsonArray = Json.createReader(new StringReader(dataString)).readArray();
                    String[] data = new String[jsonArray.size()];
                    for (int i = 0; i < data.length; i++) {
                        data[i] = jsonArray.getJsonString(i).toString();
                    }
//                    System.out.println("Data:");
//                    Arrays.stream(data).forEach(System.out::println);

                    AbstractAnnotation ann = null;
                    switch (annotationType) {
                        case "rectangle" -> {
                            ann = new RectangleAnnotation(plot, rgba, coordinates, data);
                        }
                        case "ellipse" -> {
                            ann = new EllipseAnnotation(plot, rgba, coordinates);
                        }
                        case "triangle" -> {
                            ann = new TriangleAnnotation(plot, rgba, coordinates);
                        }
                        case "line" -> {
                            ann = new LineAnnotation(plot, rgba, coordinates, data);
                        }
                        case "hvline" -> {
                            ann = new HVLineAnnotation(plot, rgba, data, minMax, coordinates[0]);
                        }
                        case "comment" -> {
                            ann = new CommentAnnotation(plot, rgba, coordinates, this, data);
                        }
                        default -> {

                        }
                    }
                    shapeIndex = annotations.size();
                    annotations.add(ann);
                }
            }
        }
    }

    private void selectAnnotation(double mouseX, double mouseY) {
        System.out.println("(select)annotation list size: " + annotations.size());
        if(annotations.size() == 0){
            return;
        }
        for (int i = annotations.size() - 1; i >= 0; i--) {
            if (annotations.get(i).clickedOn(mouseX, mouseY)) {
                if(annotations.get(i) == currentAnnotation){
                    currentAnnotation = null;
                    break;
                }
                currentAnnotation = annotations.get(i);
                clickedInAnnotation = true;
                break;
            }
            currentAnnotation = null; // this line should only be reached if no annotation was selected
        }
        for (int i = 0; i < annotations.size(); i++) {
            annotations.get(i).deselect();
        }
        if(currentAnnotation != null){        
            currentAnnotation.select();
        }
    }
    
    public void deselectAnnotation() {
        for (int i = 0; i < annotations.size(); i++) {
            annotations.get(i).deselect();
        }
    }
    
    public void redrawCommentBox() {
        for (int i = 0; i < annotations.size(); i++) {
            if (annotations.get(i) instanceof CommentAnnotation && annotations.get(i).isSelected()) {
                CommentAnnotation com = (CommentAnnotation) annotations.get(i);
                com.getBounds(true);
            }
        }
    }
    
    public void redrawHandles() {
        for (int i = 0; i < annotations.size(); i++) {
            if (annotations.get(i).isSelected()) {
                if(annotations.get(i) instanceof RectangleAnnotation){
                    RectangleAnnotation rect = (RectangleAnnotation) annotations.get(i);
                    rect.resizeHandles();
                }else if(annotations.get(i) instanceof EllipseAnnotation){
                    EllipseAnnotation ell = (EllipseAnnotation) annotations.get(i);
                    ell.resizeHandles();
                }else if(annotations.get(i) instanceof TriangleAnnotation){
                    TriangleAnnotation tri = (TriangleAnnotation) annotations.get(i);
                    tri.resizeHandles();
                }
            }
        }
    }

    private void getMinAndMax() {
        XYDataset dataset = plot.getDataset();
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            for (int j = 0; j < dataset.getItemCount(i); j++) {
                double xTemp = dataset.getXValue(i, j);
                double yTemp = dataset.getYValue(i, j);
                if (xTemp < minMax[0]) {
                    minMax[0] = xTemp;
                }
                if (xTemp > minMax[2]) {
                    minMax[2] = xTemp;
                }
                if (yTemp < minMax[1]) {
                    minMax[1] = yTemp;
                }
                if (yTemp > minMax[3]) {
                    minMax[3] = yTemp;
                }
            }
        }
        //System.out.println(minMax[0] + ", " + minMax[1] + ", " + minMax[2] + ", " + minMax[3]);
    }
}
