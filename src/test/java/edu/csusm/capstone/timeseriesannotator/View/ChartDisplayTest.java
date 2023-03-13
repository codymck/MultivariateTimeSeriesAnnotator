/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.View;

import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author Ramon Duran Rizo
 */
public class ChartDisplayTest {
    private ChartDisplay chartdisplay;
    private FrameFixture appwindow;
    private JPanelFixture chartwindow; 
    //warning says chartwindow is unused but it is inside setUp()
    private AppFrame frame;
    private Robot robot;
    
    @Before
    public void setUp() {
        frame = GuiActionRunner.execute(() -> new AppFrame());
        appwindow = new FrameFixture(frame);
        appwindow.show();
        robot = appwindow.robot(); // Initialize robot variable
        chartdisplay = GuiActionRunner.execute(() -> new ChartDisplay(frame));
        chartwindow = new JPanelFixture(robot, chartdisplay);
    }
    
    @After
    public void tearDown() {
        appwindow.cleanUp();
    }

    @Test
    public void testAddSeries(){
        appwindow.button("AddSeries").click();
    }
    
    @Test
    public void testExportAnnotations(){
        appwindow.button("ExportAnnotations").click();
    }
    
    @Test
    public void testSync(){
        appwindow.radioButton("SyncChart").click();
    }
    
    @Test
    public void testRemoveChart(){
        appwindow.button("removeChartButton").click();
    }
}
