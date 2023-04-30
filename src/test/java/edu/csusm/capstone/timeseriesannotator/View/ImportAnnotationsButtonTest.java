/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.View;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ramon Duran Rizo
 */
public class ImportAnnotationsButtonTest {
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
    public void ImportTest(){
        window.button("ImportAnnotations").click();
        window.fileChooser().fileNameTextBox().enterText("import.csv");
        window.fileChooser().approveButton().click();     
    }
}