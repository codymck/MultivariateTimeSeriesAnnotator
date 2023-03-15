package edu.csusm.capstone.timeseriesannotator.View;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author Ramon Duran
 */
public class ImportViewTest{
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
    public void testImport(){
        window.menuItem("ImportData").click();
        window.fileChooser().fileNameTextBox().enterText("data.csv");
        window.fileChooser().approveButton().click();
    }
}

