/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Ramon Duran Rizo
 */
public class CSVReaderTest {
    
    public CSVReaderTest() {
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
     * Test of buildDataList method, of class CSVReader.
     */
    @Test
    public void testBuildDataList() {
        System.out.println("buildDataList");
        String fileName = "/Users/ramonduran/NetBeansProjects/MultivariateTimeSeriesAnnotator/dataFiles/test.csv";
        CSVReader instance = new CSVReader();
        instance.buildDataList(fileName);
        // TODO review the generated test code and remove the default call to fail.
        String s[] = instance.getHeaders();
        /*
        for(String n: s)
        {
            System.out.println(n);
        }
        */
        assertEquals(s[0],"null");
    }

    /**
     * Test of getColumn method, of class CSVReader.
     */
    @Test
    public void testGetColumn() {
        System.out.println("getColumn");
        String s = "";
        CSVReader instance = new CSVReader();
        Float[] expResult = null;
        Float[] result = instance.getColumn(s);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeaders method, of class CSVReader.
     */
    @Test
    public void testGetHeaders() {
        System.out.println("getHeaders");
        CSVReader instance = new CSVReader();
        String[] expResult = null;
        String[] result = instance.getHeaders();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
