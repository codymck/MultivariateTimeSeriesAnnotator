package edu.csusm.capstone.timeseriesannotator.View;

import java.io.File;
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
 * @author Ben Theurich
 */
public class AutomatedViewTest {
    private FrameFixture window;

    @Before
    public void setUp() {
      AppFrame frame = GuiActionRunner.execute(() -> new AppFrame());
      window = new FrameFixture(frame);
      window.show(); // shows the frame to test
    }
    
    @Test
    public void toolButtonsShouldBeClickable(){
        window.toggleButton("ZoomIn").click();
        window.toggleButton("ZoomOut").click();
        window.toggleButton("Pan").click();
        window.toggleButton("Highlight").click();
        window.toggleButton("Comment").click();
        window.toggleButton("ShapeMarker").click();
    }
    
        @Test
    public void clickOnImportData(){
        window.menuItem("ImportData").click();
    }
    
    @After
    public void tearDown() {
      window.close(); // closes the frame
    }
}