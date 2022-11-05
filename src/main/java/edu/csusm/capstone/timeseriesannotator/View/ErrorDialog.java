/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.View;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Cody McKinney
 */
public class ErrorDialog {
    
    public ErrorDialog() {
        
    }
    
    public static void wrongData() {
        JOptionPane.showMessageDialog(new JFrame(), "Error: Dataset or Object does not exist.\n\n                Try Again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}
