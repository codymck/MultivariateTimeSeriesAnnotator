package edu.csusm.capstone.timeseriesannotator.Controller;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
import org.assertj.swing.edt.GuiActionRunner;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.assertj.swing.finder.WindowFinder.findFrame;
import org.assertj.swing.fixture.FrameFixture;
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
public class AssertJTest {
    private FrameFixture window;

    @Before
    public void setUp() {
      AppFrame frame = GuiActionRunner.execute(() -> new AppFrame());
      window = new FrameFixture(frame);
      window.show(); // shows the frame to test
    }
    
    @Test
    public void shouldSeeFileButton(){
        window.toggleButton("ZoomIn").click();
        window.toggleButton("ZoomOut").click();
        window.toggleButton("Pan").click();
        window.toggleButton("Highlight").click();
        window.toggleButton("Comment").click();
        window.toggleButton("ShapeMarker").click();

        window.menuItem("ImportData").click();

    }
}
