package edu.csusm.capstone.timeseriesannotator.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Ramon Duran Rizo
 */
public class HDFReaderTest {
    
    public HDFReaderTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of buildDataList method, of class HDFReader.
     */
    @Test
    public void testBuildDataList() {
        System.out.println("buildDataList");
        String fileName = "dataFiles/smallsampledata.hdf5";
        HDFReader instance = new HDFReader();
        instance.buildDataList(fileName);
        instance.setPaths("user1/t", "user1/g");
        // TODO review the generated test code and remove the default call to fail.
        assertNotNull(instance.getXData());
        assertNotNull(instance.getYData());
    }
    
}
