package edu.csusm.capstone.timeseriesannotator.View;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Cody McKinney
 */
public class ErrorDialog {
    static boolean tooMany;
    
    public static void wrongData() {
        JOptionPane.showMessageDialog(AppFrame.frame, "Error: Dataset or Object does not exist.\n\n                Try Again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void TooManyCharts() {
        if(tooMany == false){
            tooMany = true;
            JOptionPane.showMessageDialog(AppFrame.frame, "Error: Too Many Charts Added.\n", "Error", JOptionPane.ERROR_MESSAGE);
            tooMany = false;
        }
    }
    
    public static void UnsupportedFile() {
        JOptionPane.showMessageDialog(AppFrame.frame, "Error: File is not supported.\n\n                Try Again.","Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void badIndex() {
        JOptionPane.showMessageDialog(AppFrame.frame, "Select valid indexes", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void badFileType() {
        JOptionPane.showMessageDialog(AppFrame.frame, "Not a valid file type to save to", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}
