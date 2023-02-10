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
 * @author Ben Theurich
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
        window.toggleButton("ZoomButton").click();
        window.toggleButton("PanButton").click();
        window.toggleButton("SelectButton").click();
        window.toggleButton("CommentButton").click();
        window.toggleButton("MarkerButton").click();
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