package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import org.assertj.swing.edt.GuiActionRunner;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.assertj.swing.finder.WindowFinder.findFrame;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Ramon Duran
 */
public class FrameAutomateTest {
    private FrameFixture window;
    private AppFrame frame;
    @Before
    public void setUp() {
      frame = GuiActionRunner.execute(() -> new AppFrame());
      window = new FrameFixture(frame);
      window.show(); // shows the frame to test
    }
    @After
    public void tearDown(){
        window.cleanUp();
    }
    
    @Test
    public void testButtons(){
        window.button("AddChartButton").click();
        window.toggleButton("ZoomButton").click();
        window.toggleButton("PanButton").click();
        window.toggleButton("SelectButton").click();
        //color buttons shown when on select tool
        window.toggleButton("BlackButton").click();
        window.toggleButton("RedButton").click();
        window.toggleButton("OrangeButton").click();
        window.toggleButton("YellowButton").click();
        window.toggleButton("GreenButton").click();
        window.toggleButton("BlueButton").click();
        window.toggleButton("PurpleButton").click();
        //end of color buttons
        window.toggleButton("CommentButton").click();
        window.toggleButton("LineButton").click();
        window.toggleButton("VerticalButton").click();
        window.toggleButton("HorizontalButton").click();
        window.toggleButton("DiagonalButton").click();
        window.toggleButton("RayButton").click();
        window.toggleButton("SegmentButton").click();
        //
        window.toggleButton("ShapeButton").click();
        window.toggleButton("SquareButton").click();
        window.toggleButton("EllipseButton").click();
        window.toggleButton("TriangleButton").click();
    }
    
    @Test
    public void testImport(){
//        window.menuItem("ImportData").click();
//        window.fileChooser().fileNameTextBox().enterText("data.csv");
//        window.fileChooser().approveButton().click();
//        
//        window.menuItem("AddChartMenuItem").click();
//          for(int i = 0;i < 5;i++){
//              window.button("AddChartButton").click();
//          }
    }
}