package edu.csusm.capstone.timeseriesannotator;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;

/**
 *
 * @author Cody McKinney
 */
public class TimeSeriesAnnotator {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppFrame().setVisible(true);
            }
        });

    }
}
