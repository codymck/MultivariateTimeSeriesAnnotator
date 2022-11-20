package edu.csusm.capstone.timeseriesannotator.View;

import org.junit.Test;

/**
 *
 * @author Cody McKinney
 */
public class ViewTest {
     @Test
     public void testAppFrame() {
         AppFrame a = new AppFrame();
         a.setVisible(true);
         
         ChartSelectMenu ch = new ChartSelectMenu(a, true);
         CSVdataSelectMenu cD = new CSVdataSelectMenu(a, true);
         
     }
}
