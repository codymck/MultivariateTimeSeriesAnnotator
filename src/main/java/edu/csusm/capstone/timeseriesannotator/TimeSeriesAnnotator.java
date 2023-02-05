package edu.csusm.capstone.timeseriesannotator;

import edu.csusm.capstone.timeseriesannotator.View.AppFrame;
//import javax.swing.UIManager;

/**
 *
 * @author Cody McKinney
 */
public class TimeSeriesAnnotator {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    // javax.swing.plaf.metal.MetalLookAndFeel
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(AppFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppFrame().setVisible(true);
            }
        });

    }
}
