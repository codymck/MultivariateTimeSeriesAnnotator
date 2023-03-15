package edu.csusm.capstone.timeseriesannotator.View;

import com.opencsv.CSVWriter;
import edu.csusm.capstone.timeseriesannotator.Controller.Controller;
import edu.csusm.capstone.timeseriesannotator.Controller.Tools.*;
import edu.csusm.capstone.timeseriesannotator.Model.ToolState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import static java.lang.Double.NaN;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.jfree.chart.ui.TextAnchor;
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

        hTrace = new HVLineAnnotation(plot, color, "horizontal", minMax);
        vTrace = new HVLineAnnotation(plot, color, "vertical", minMax);

        chart.addChangeListener(new ChartChangeListener() {
            @Override
            public void chartChanged(ChartChangeEvent cce) {
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
                        RectangleAnnotation r = new RectangleAnnotation(plot, color, point, "region", minMax);
                        shapeIndex = annotations.size();
                        annotations.add(r);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        deleteAnnotation(point);
                    }
                }
                case COMMENT -> {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Font f = new Font(AppFrame.getFontName(), AppFrame.getFontStyle(), AppFrame.getFontSize());
                        CommentAnnotation comment = new CommentAnnotation(plot, color, point, this, f);
                        shapeIndex = annotations.size();
                        annotations.add(comment);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        deleteAnnotation(point);
                    }
                }
                case MARK -> {
                    switch (AppFrame.getMarkerType()) {
                        case VERTICAL -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                HVLineAnnotation vert = new HVLineAnnotation(plot, AppFrame.getAbsoluteColor(), "vertical", minMax);
                                vert.createLine(point);
                                shapeIndex = annotations.size();
                                annotations.add(vert);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                        }
                        case HORIZONTAL -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                HVLineAnnotation horiz = new HVLineAnnotation(plot, AppFrame.getAbsoluteColor(), "horizontal", minMax);
                                horiz.createLine(point);
                                shapeIndex = annotations.size();
                                annotations.add(horiz);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                        }
                        case DIAGONAL -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!clickedOnce) {
                                    clickedOnce = true;
                                    LineAnnotation r = new LineAnnotation(plot, AppFrame.getAbsoluteColor(), point, "diagonal", minMax);
                                    shapeIndex = annotations.size();
                                    annotations.add(r);
                                } else {
                                    clickedOnce = false;
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (!clickedOnce) {
                                    deleteAnnotation(point);
                                }
                            }
                        }
                        case RAY -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!clickedOnce) {
                                    clickedOnce = true;
                                    LineAnnotation r = new LineAnnotation(plot, AppFrame.getAbsoluteColor(), point, "ray", minMax);
                                    shapeIndex = annotations.size();
                                    annotations.add(r);
                                } else {
                                    clickedOnce = false;
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (!clickedOnce) {
                                    deleteAnnotation(point);
                                }
                            }
                        }
                        case SEGMENT -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (!clickedOnce) {
                                    clickedOnce = true;
                                    LineAnnotation r = new LineAnnotation(plot, AppFrame.getAbsoluteColor(), point, "segment", minMax);
                                    shapeIndex = annotations.size();
                                    annotations.add(r);
                                } else {
                                    clickedOnce = false;
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (!clickedOnce) {
                                    deleteAnnotation(point);
                                }
                            }
                        }
                        case SQUARE -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                RectangleAnnotation r = new RectangleAnnotation(plot, color, point, "rectangle", minMax);
                                shapeIndex = annotations.size();
                                annotations.add(r);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                        }
                        case ELLIPSE -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                EllipseAnnotation ell = new EllipseAnnotation(plot, color, point);
                                shapeIndex = annotations.size();
                                annotations.add(ell);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                deleteAnnotation(point);
                            }
                        }
                        case TRIANGLE -> {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                if (triClick == 0) {
                                    moveTest = point;
                                    TriangleAnnotation tri = new TriangleAnnotation(plot, color);
                                    shapeIndex = annotations.size();
                                    annotations.add(tri);
                                    tri.createShape(point);
                                    triClick++;
                                } else {
                                    TriangleAnnotation tempTri = (TriangleAnnotation) annotations.get(shapeIndex);
                                    tempTri.createShape(point);
                                    triClick++;
                                    if (triClick > 2) {
                                        triClick = 0;
                                    }
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                if (triClick == 0) {
                                    deleteAnnotation(point);
                                }
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
                    
                    if (strPoint != null){
                        
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
                        double absX = Math.abs(currentX) - Math.abs(initialX);// (-) pan down, 0 nothing, (+) pan up
                        double absY = Math.abs(currentY) - Math.abs(initialY);// (-) pan left, 0 nothing (+) pan right

                        if(absY > 0 && !(plot.getRangeAxis().getUpperBound() >= minMax[3]*3)){
                            //Pan up
                            getChart().getXYPlot().getRangeAxis().setRange(getChart().getXYPlot().getRangeAxis().getLowerBound() + deltaYValue,
                                getChart().getXYPlot().getRangeAxis().getUpperBound() + deltaYValue); 
                        }
                        if(absY < 0 && !(plot.getRangeAxis().getLowerBound() <= (-minMax[3]*3))){
                            //Pan down
                            getChart().getXYPlot().getRangeAxis().setRange(getChart().getXYPlot().getRangeAxis().getLowerBound() + deltaYValue,
                                getChart().getXYPlot().getRangeAxis().getUpperBound() + deltaYValue); 
                        }
                        if(absX > 0 && !(plot.getDomainAxis().getUpperBound() >= minMax[2]*3)){
                            //Pan right
                            getChart().getXYPlot().getDomainAxis().setRange(getChart().getXYPlot().getDomainAxis().getLowerBound() - deltaXValue,
                                getChart().getXYPlot().getDomainAxis().getUpperBound() - deltaXValue); 
                        }
                        if(absX > 0 && !(plot.getDomainAxis().getLowerBound() <= (-minMax[2]*3))){
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
                        for (int i = 0; i < annotations.size(); i++) {
                            if (annotations.get(i).isSelected()) {
                                annotations.get(i).move(xOffset, yOffset, false);
                            }
                        }
                    }
                }
                case HIGHLIGHT -> {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        RectangleAnnotation tempRect = (RectangleAnnotation) annotations.get(shapeIndex);
                        tempRect.drawRect(point);
                    }
                }
                case MARK -> {
                    switch (AppFrame.getMarkerType()) {
                        case SQUARE -> {
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                RectangleAnnotation tempRect = (RectangleAnnotation) annotations.get(shapeIndex);
                                tempRect.drawRect(point);
                            }
                        }
                        case ELLIPSE -> {
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                EllipseAnnotation tempEll = (EllipseAnnotation) annotations.get(shapeIndex);
                                tempEll.drawEllipse(point);
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
                        for (int i = 0; i < annotations.size(); i++) {
                            if (annotations.get(i).isSelected()) {
                                annotations.get(i).move(xOffset, yOffset, true);
                            }
                        }
                        if (!moved) {
                            selectAnnotation(point[0], point[1]);
                        }
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
                                LineAnnotation tempLine = (LineAnnotation) annotations.get(shapeIndex);
                                tempLine.drawLine(point);
                            }
                        }
                        case RAY -> {
                            if (clickedOnce) {
                                LineAnnotation tempLine = (LineAnnotation) annotations.get(shapeIndex);
                                tempLine.drawLine(point);
                            }
                        }
                        case SEGMENT -> {
                            if (clickedOnce) {
                                LineAnnotation tempLine = (LineAnnotation) annotations.get(shapeIndex);
                                tempLine.drawLine(point);
                            }
                        }
                        case TRIANGLE -> {
                            if (triClick > 0) {
                                TriangleAnnotation tempTri = (TriangleAnnotation) annotations.get(shapeIndex);
                                tempTri.drawTriangle(point);
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

    public void removeTextAnnotation() {
        List<XYTextAnnotation> annotations = new ArrayList<>(plot.getAnnotations());
        List<XYTextAnnotation> textAnnotations = new ArrayList<>();
        for (XYAnnotation annotation : annotations) {
            if (annotation instanceof XYTextAnnotation xYTextAnnotation) {
                textAnnotations.add(xYTextAnnotation);
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
                System.out.println("CLICKED");
                annotations.get(i).delete();
                annotations.remove(i);
                break;
            }
        }
    }

    public void exportAnnotations() throws IOException {
        JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        fileChooser.setSelectedFile(new File(".csv"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV", "csv"));
        fileChooser.setAcceptAllFileFilterUsed(true);
        int userSelection = fileChooser.showSaveDialog(frame);
        File fileToSave;

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());

            String name = fileToSave.getName();
            System.out.println(name);
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
                FileWriter outputFile = new FileWriter(fileToSave);
                CSVWriter writer = new CSVWriter(outputFile, ',',
                                             CSVWriter.NO_QUOTE_CHARACTER,
                                             CSVWriter.NO_QUOTE_CHARACTER,
                                             CSVWriter.DEFAULT_LINE_END);
                String[] header = {"Annotation Type", "RGBA", "Coordinates", "Data"};
                writer.writeNext(header);
                for (int i = 0; i < annotations.size(); i++) {
                    annotations.get(i).export(writer);
                }
                
                writer.close();
            } else {
                ErrorDialog.badFileType();
            }
        }

    }

    private void selectAnnotation(double mouseX, double mouseY) {
        for (int i = annotations.size() - 1; i >= 0; i--) {
            if (annotations.get(i).clickedOn(mouseX, mouseY)) {
                break;
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
