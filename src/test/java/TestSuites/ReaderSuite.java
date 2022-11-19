/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5Suite.java to edit this template
 */
package TestSuites;

import edu.csusm.capstone.timeseriesannotator.Model.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 *
 * @author Cody McKinney
 */
@RunWith(Suite.class)
@SuiteClasses({CSVReaderTest.class, HDFReaderTest.class})
public class ReaderSuite {
    
}
