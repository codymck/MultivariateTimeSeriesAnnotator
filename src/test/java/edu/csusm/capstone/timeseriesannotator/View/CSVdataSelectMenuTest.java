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
public class CSVdataSelectMenuTest {

    private CSVdataSelectMenu csvselect;
    private DialogFixture dialog;

    @Before
    public void setUp() {
        csvselect = GuiActionRunner.execute(() -> new CSVdataSelectMenu(new javax.swing.JFrame(), true));
        dialog = new DialogFixture(csvselect);
        dialog.show(); // shows the frame to test
    }

    @After
    public void tearDown() {
        dialog.cleanUp();
    }

    @Test
    public void testDataSelect() {
        dialog.button("SubmitAxis").click();
    }
}
