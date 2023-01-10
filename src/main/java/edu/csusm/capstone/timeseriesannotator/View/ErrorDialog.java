package edu.csusm.capstone.timeseriesannotator.View;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Cody McKinney
 */
public class ErrorDialog {
    
    public static void wrongData() {
        JOptionPane.showMessageDialog(new JFrame(), "Error: Dataset or Object does not exist.\n\n                Try Again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void TooManyCharts() {
        JOptionPane.showMessageDialog(new JFrame(), "Error: Too Many Charts Added.\n", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void UnsupportedFile() {
        JOptionPane.showMessageDialog(new JFrame(), "Error: File is not supported.\n\n                Try Again.","Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void badIndex() {
        JOptionPane.showMessageDialog(new JFrame(), "Select valid indexes", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}
