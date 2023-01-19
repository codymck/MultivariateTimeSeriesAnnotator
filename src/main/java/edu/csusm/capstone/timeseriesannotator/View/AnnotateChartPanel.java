package edu.csusm.capstone.timeseriesannotator.View;

import edu.csusm.capstone.timeseriesannotator.Model.ToolState;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleEdge;

/**
 *
 * @author Ben Theurich
 */
public class AnnotateChartPanel extends ChartPanel implements MouseListener {

    private ToolState state;
    private JFreeChart chart = null;
    Color color = new Color(0, 100, 255, 60);
    private XYPlot plot;
    private double[][] coordinates = {{0.0, 0.0}, {0.0, 0.0}};

    ArrayList<RegionStruct> regionList = new ArrayList<>();

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
        if (null != state) {
            switch (state) {
                case HIGHLIGHT:
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        setMouseZoomable(false, true);
                        super.mousePressed(e);
                        coordinates[0][0] = point[0];
                        coordinates[0][1] = point[1];
                        setMouseZoomable(true, true);
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
                default:
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
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        setMouseZoomable(false, true);
                        super.mouseReleased(e);
                        coordinates[1][0] = point[0];
                        coordinates[1][1] = point[1];
                        setMouseZoomable(true, false);
                        addRegionAnnotation();
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        removeRegionAnnotation(point[0], point[1]);
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
                default:
                    break;
            }
        }
    }

    public void addRegionAnnotation() {
        XYBoxAnnotation region = new XYBoxAnnotation(
                coordinates[0][0],
                coordinates[1][1],
                coordinates[1][0],
                coordinates[0][1],
                null,
                null,
                color
        );
        plot.addAnnotation(region);
        regionList.add(new RegionStruct(coordinates, region));
    }

    public void removeRegionAnnotation(double mouseX, double mouseY) {
        for (int i = regionList.size() - 1; i >= 0; i--) {
            RegionStruct r = regionList.get(i);
            if (r.isClickedOn(mouseX, mouseY)) {
//                System.out.println("deleting");
                plot.removeAnnotation(r.getRegion());
                regionList.remove(i);
                break;
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
