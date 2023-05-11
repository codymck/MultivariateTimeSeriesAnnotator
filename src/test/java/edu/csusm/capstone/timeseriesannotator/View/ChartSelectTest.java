package edu.csusm.capstone.timeseriesannotator.View;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ramon Duran Rizo
 */
public class ChartSelectTest {

    private ChartSelectMenu chartmenu;
    private DialogFixture dialog;

    @Before
    public void setUp() {
        chartmenu = GuiActionRunner.execute(() -> new ChartSelectMenu(new javax.swing.JFrame(), true));
        dialog = new DialogFixture(chartmenu);
        dialog.show(); // shows the frame to test
    }

    @After
    public void tearDown() {
        dialog.cleanUp();
    }

    @Test
    public void testLineChart() {
        dialog.radioButton("LineChart").click();
    }

    @Test
    public void testScatterPlot() {
        dialog.radioButton("ScatterPlot").click();
    }

    @Test
    public void testStepChart() {
        dialog.radioButton("StepChart").click();
    }
}
