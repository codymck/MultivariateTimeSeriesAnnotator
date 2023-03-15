package edu.csusm.capstone.timeseriesannotator.Controller;

import org.assertj.swing.edt.GuiActionRunner;
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
public class ButtonAutomateTest {
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
    public void testButtons() throws InterruptedException{
        window.button("AddChartButton").click();
        window.toggleButton("ZoomButton").click();
        window.toggleButton("PanButton").click();
        window.toggleButton("EditButton").click();
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
        
        window.toggleButton("ShapeButton").click();
        Thread.sleep(50);
        window.toggleButton("SquareButton").click();
        window.toggleButton("EllipseButton").click();
        window.toggleButton("TriangleButton").click();
        
        window.toggleButton("LineButton").click();
        Thread.sleep(50);
        window.toggleButton("VerticalButton").click();
        window.toggleButton("HorizontalButton").click();
        window.toggleButton("DiagonalButton").click();
        window.toggleButton("RayButton").click();
        window.toggleButton("SegmentButton").click();
        //
    }
}