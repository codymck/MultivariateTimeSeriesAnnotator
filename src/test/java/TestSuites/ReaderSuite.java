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
