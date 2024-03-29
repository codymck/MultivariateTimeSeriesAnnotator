package edu.csusm.capstone.timeseriesannotator.View;

import org.jumpmind.symmetric.csv.CsvWriter;
import org.jumpmind.symmetric.csv.CsvReader;
import edu.csusm.capstone.timeseriesannotator.Controller.Controller;
import edu.csusm.capstone.timeseriesannotator.Controller.Tools.*;
import static edu.csusm.capstone.timeseriesannotator.Model.MarkerType.ELLIPSE;
import static edu.csusm.capstone.timeseriesannotator.Model.MarkerType.SQUARE;
import edu.csusm.capstone.timeseriesannotator.Model.ToolState;
import static edu.csusm.capstone.timeseriesannotator.Model.ToolState.SELECT;
import static edu.csusm.capstone.timeseriesannotator.View.AppFrame.frame;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.json.Json;
import javax.json.JsonArray;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
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
    Color color = new Color(0, 0, 0);

    private XYPlot plot;
    final List<XYDataset> originalDatasets;
    private boolean syncing = false;
    private boolean onChart = false;

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
    private boolean annotationNeedsUpdate = false;

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
        setChartState(AppFrame.getAppState());
        chart.setBackgroundPaint(new java.awt.Color(242, 242, 242));

        hTrace = new HVLineAnnotation(plot, color, "horizontal", this);
        vTrace = new HVLineAnnotation(plot, color, "vertical", this);

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
                    // If the values have changed, set a flag to indicate that the selected annotation needs to be updated
                    annotationNeedsUpdate = true;
                    // Schedule a timer to update the comment box after a delay of 10 milliseconds
                    if (timer != null) {
                        timer.cancel();
                    }
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            if (annotationNeedsUpdate) {
                                scaleAnnotation();
                                annotationNeedsUpdate = false;
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
                    // If the values have changed, set a flag to indicate that the that the selected annotation needs to be updated
                    annotationNeedsUpdate = true;

                    // Schedule a timer to update the comment box after a delay of 10 milliseconds
                    if (timer != null) {
                        timer.cancel();
                    }
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            if (annotationNeedsUpdate) {
                                scaleAnnotation();
                                annotationNeedsUpdate = false;
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

                        if (currentAnnotation != null && currentAnnotation.clickedOn(point[0], point[1])) {
                            clickedInAnnotation = true;
                            alreadySelected = true;
                        } else {
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
                        Hr = new RectangleAnnotation(plot, AppFrame.color, point, "region", this);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        deleteAnnotation(point);
                    }
                }
                case COMMENT -> {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Font f = new Font(frame.getFontName(), frame.getFontStyle(), frame.getFontSize());
                        CommentAnnotation comment = new CommentAnnotation(plot, AppFrame.color, point, f, this);
                        shapeIndex = annotations.size();
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        deleteAnnotation(point);
                    }
                }
                case MARK -> {
                    switch (AppFrame.getMarkerType()) {
                        case VERTICAL -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                vert = new HVLineAnnotation(plot, AppFrame.color, "vertical", this);
                                vert.createLine(point);
                                shapeIndex = annotations.size();
                                annotations.add(vert);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                            if (e.getClickCount() == 2) {
                                vert.delete();
                                removeAbstractAnnotation(vert);
                                vert = (HVLineAnnotation) annotations.get(shapeIndex);
                                vert.delete();
                                removeAbstractAnnotation(vert);
                            }
                        }
                        case HORIZONTAL -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                horiz = new HVLineAnnotation(plot, AppFrame.color, "horizontal", this);
                                horiz.createLine(point);
                                shapeIndex = annotations.size();
                                annotations.add(horiz);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                            if (e.getClickCount() == 2) {
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
                                    dl = new LineAnnotation(plot, AppFrame.color, point, "diagonal", this);
                                } else {
                                    clickedOnce = false;
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (!clickedOnce) {
                                    deleteAnnotation(point);
                                }
                            }
                            if (e.getClickCount() == 2 && dl != null) {
                                dl.delete();
                                clickedOnce = false;
                            }
                        }
                        case RAY -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!clickedOnce) {
                                    clickedOnce = true;
                                    lr = new LineAnnotation(plot, AppFrame.color, point, "ray", this);
                                } else {
                                    clickedOnce = false;
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (!clickedOnce) {
                                    deleteAnnotation(point);
                                }
                            }
                            if (e.getClickCount() == 2 && lr != null) {
                                lr.delete();
                                clickedOnce = false;
                            }
                        }
                        case SEGMENT -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!clickedOnce) {
                                    clickedOnce = true;
                                    sl = new LineAnnotation(plot, AppFrame.color, point, "segment", this);
                                } else {
                                    clickedOnce = false;
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (!clickedOnce) {
                                    deleteAnnotation(point);
                                }
                            }
                            if (e.getClickCount() == 2 && sl != null) {
                                sl.delete();
                                clickedOnce = false;
                            }
                        }
                        case SQUARE -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                r = new RectangleAnnotation(plot, AppFrame.color, point, "rectangle", this);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                        }
                        case ELLIPSE -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                ell = new EllipseAnnotation(plot, AppFrame.color, point, this);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                        }
                        case TRIANGLE -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (triClick == 0) {
                                    moveTest = point;
                                    tri = new TriangleAnnotation(plot, AppFrame.color, this);
                                    tri.createShape(point);
                                    triClick++;
                                } else {
                                    tri.createShape(point);
                                    triClick++;
                                    if (triClick > 2) {
                                        if (e.getClickCount() == 2) {
                                            removeAbstractAnnotation(tri);
                                        }
                                        triClick = 0;
                                    }
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (triClick == 0) {
                                    deleteAnnotation(point);
                                }
                            }
                            if (e.getClickCount() == 2) {
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
                        if (clickedInAnnotation) {
                            currentAnnotation.move(xOffset, yOffset, false);
                        }
                    }
                }
                case HIGHLIGHT -> {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        Hr.drawRect(point);
                        dragged = true;
                    }
                }
                case MARK -> {
                    switch (AppFrame.getMarkerType()) {
                        case SQUARE -> {
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                r.drawRect(point);
                                dragged = true;
                            }
                        }
                        case ELLIPSE -> {
                            if (SwingUtilities.isLeftMouseButton(e)) {
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
                        if (clickedInAnnotation) {
                            currentAnnotation.move(xOffset, yOffset, true);
                        }
                        if (!moved && alreadySelected) {
                            currentAnnotation.deselect();
                            currentAnnotation = null;
                            frame.hideColorPanel();
                            frame.hideFontPanel();
                            frame.hideDeleteButton();
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
                }
                case COMMENT -> {
                }
                case MARK -> {
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
                                dl.drawLine(point);
                            }
                        }
                        case RAY -> {
                            if (clickedOnce) {
                                lr.drawLine(point);
                            }
                        }
                        case SEGMENT -> {
                            if (clickedOnce) {
                                sl.drawLine(point);
                            }
                        }
                        case TRIANGLE -> {
                            if (triClick > 0 && e.getClickCount() != 2) {
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
    public void mouseEntered(MouseEvent e) {
        onChart = true;
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
            tri.delete();
        }
        onChart = false;
    }

    public boolean inChart() {
        return onChart;
    }

    public double[] getPointInChart(MouseEvent e) {
        Insets insets = getInsets();
        int mouseX = (int) ((e.getX() - insets.left) / this.getScaleX());
        int mouseY = (int) ((e.getY() - insets.top) / this.getScaleY());

        Point2D p = this.translateScreenToJava2D(new Point(mouseX, mouseY));
        ChartRenderingInfo info = this.getChartRenderingInfo();
        Rectangle2D dataArea = info.getPlotInfo().getDataArea();

        ValueAxis domainAxis = plot.getDomainAxis();
        RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
        ValueAxis rangeAxis = plot.getRangeAxis();
        RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();

        double chartX = domainAxis.java2DToValue(p.getX(), dataArea, domainAxisEdge);
        double chartY = rangeAxis.java2DToValue(p.getY(), dataArea, rangeAxisEdge);

        double[] r = {chartX, chartY};
        return r;
    }

    public void setColor(Color c) {
        color = c;
        hTrace = new HVLineAnnotation(plot, color, "horizontal", this);
        vTrace = new HVLineAnnotation(plot, color, "vertical", this);
        if (currentAnnotation != null && state == SELECT) {
            currentAnnotation.changeColor(color);
        }
    }

    public void setFontName(String fName) {
        if (currentAnnotation != null && state == SELECT && currentAnnotation instanceof CommentAnnotation c) {
            c.changeFontName(fName);
            c.scale();
        }
    }

    public void setFontStyle(int fStyle) {
        if (currentAnnotation != null && state == SELECT && currentAnnotation instanceof CommentAnnotation c) {
            c.changeFontStyle(fStyle);
            c.scale();
        }
    }

    public void setFontSize(int fSize) {
        if (currentAnnotation != null && state == SELECT && currentAnnotation instanceof CommentAnnotation c) {
            c.changeFontSize(fSize);
            c.scale();
        }
    }

    public void setSync(boolean s) {
        syncing = s;
    }

    private void deleteAnnotation(double[] point) {
        for (int i = annotations.size() - 1; i >= 0; i--) {
            if (annotations.get(i).clickedOn(point[0], point[1])) {
                annotations.get(i).delete();
                if (annotations.get(i) == currentAnnotation) {
                    currentAnnotation = null;
                }
                annotations.remove(i);

                break;
            }
        }
    }

    public void deleteSelectedAnnotation() {
        for (int i = annotations.size() - 1; i >= 0; i--) {
            if (annotations.get(i).isSelected()) {
                currentAnnotation = null;
                annotations.get(i).delete();
                annotations.remove(i);
                frame.hideColorPanel();
                frame.hideFontPanel();
                frame.hideDeleteButton();
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

                    String rgbaString = reader.get("RGBA");
                    JsonArray jsonArray = Json.createReader(new StringReader(rgbaString)).readArray();
                    int[] rgba = new int[jsonArray.size()];
                    for (int i = 0; i < rgba.length; i++) {
                        rgba[i] = jsonArray.getInt(i);
                    }

                    String coordinatesString = reader.get("Coordinates");
                    jsonArray = Json.createReader(new StringReader(coordinatesString)).readArray();
                    double[][] coordinates = new double[jsonArray.size()][((JsonArray) jsonArray.get(0)).size()];
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonArray row = jsonArray.getJsonArray(i);
                        for (int j = 0; j < row.size(); j++) {
                            coordinates[i][j] = row.getJsonNumber(j).doubleValue();
                        }
                    }

                    String dataString = reader.get("Data");
                    jsonArray = Json.createReader(new StringReader(dataString)).readArray();
                    String[] data = new String[jsonArray.size()];
                    for (int i = 0; i < data.length; i++) {
                        data[i] = jsonArray.getJsonString(i).toString();
                    }

                    AbstractAnnotation ann = null;
                    switch (annotationType) {
                        case "rectangle" -> {
                            ann = new RectangleAnnotation(plot, rgba, coordinates, data, this);
                        }
                        case "ellipse" -> {
                            ann = new EllipseAnnotation(plot, rgba, coordinates, this);
                        }
                        case "triangle" -> {
                            ann = new TriangleAnnotation(plot, rgba, coordinates, this);
                        }
                        case "line" -> {
                            ann = new LineAnnotation(plot, rgba, coordinates, data, this);
                        }
                        case "hvline" -> {
                            ann = new HVLineAnnotation(plot, rgba, data, coordinates[0], this);
                        }
                        case "comment" -> {
                            ann = new CommentAnnotation(plot, rgba, coordinates, data, this);
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
        boolean changeFont = false;
        Font tempFont = null;
        if (annotations.size() == 0) {
            return;
        }
        for (int i = annotations.size() - 1; i >= 0; i--) {
            if (annotations.get(i).clickedOn(mouseX, mouseY)) {
                if (annotations.get(i) == currentAnnotation) {
                    currentAnnotation = null;
                    frame.hideColorPanel();
                    frame.hideFontPanel();
                    frame.hideDeleteButton();
                    break;
                }
                currentAnnotation = annotations.get(i);
                clickedInAnnotation = true;
                if (annotations.get(i) instanceof CommentAnnotation c) {
                    frame.showFontPanel();
                    changeFont = true;
                    tempFont = c.getFont();
                } else {
                    frame.hideFontPanel();
                }
                frame.showColorPanel();
                frame.showDeleteButton();
                break;
            }
            frame.hideColorPanel();
            frame.hideFontPanel();
            frame.hideDeleteButton();
            currentAnnotation = null; // this line should only be reached if no annotation was selected
        }
        for (int i = 0; i < annotations.size(); i++) {
            annotations.get(i).deselect();
        }
        if (currentAnnotation != null) {
            currentAnnotation.select();
        }
        if (changeFont) {
            frame.setFontBoxes(tempFont);
        }
    }

    public void scaleAnnotation() {
        if (currentAnnotation != null) {
            currentAnnotation.scale();
        }
    }

    private void getMinAndMax() {
        XYDataset dataset;
        for (int i = 0; i < plot.getDatasetCount(); i++) {
            dataset = plot.getDataset(i);
            for (int j = 0; j < dataset.getItemCount(0); j++) {
                double xTemp = dataset.getXValue(0, j);
                double yTemp = dataset.getYValue(0, j);
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
    }

    public void redrawNewMM() {
        getMinAndMax();
        for (int i = 0; i < annotations.size(); i++) {
            if (annotations.get(i) instanceof RectangleAnnotation r) {
                r.newBounds();
            } else if (annotations.get(i) instanceof HVLineAnnotation l) {
                l.calculateLine();
            }
        }
    }

    public ToolState getToolState() {
        return state;
    }
}
