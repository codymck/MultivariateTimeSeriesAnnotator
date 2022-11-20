package edu.csusm.capstone.timeseriesannotator.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Ramon Duran Rizo
 */
public class CSVReaderTest {

    private String data1, data2, data3,data4;

    public CSVReaderTest() {
    }

    /**
     * Test of buildDataList method, of class CSVReader.
     * calls buildTestData() to generate test file with set keys and random data
     * tests if data file creates file with appropriate keys
     * checks if the data generated can be grabbed with the correct key
     */
    @Test
    public void testBuildDataList() {
        System.out.println("buildDataList");
        String fileName = buildTestData();
        CSVReader instance = new CSVReader();
        instance.buildDataList(fileName);
        // TODO review the generated test code and remove the default call to fail.
        assertEquals("dataFiles/test.csv", fileName);
        
        String[] keys = {"A", "null", "C", "D"};
        
        assertArrayEquals(keys, instance.getHeaders() );
        assertEquals("NaN", instance.getColumn(keys[0])[0].toString() );
        assertEquals(data2, instance.getColumn(keys[1])[0].toString() );
        assertEquals("NaN", instance.getColumn(keys[2])[0].toString() );
        assertEquals("NaN", instance.getColumn(keys[3])[0].toString() );
    }

    public String buildTestData() {
        Random rd = new Random();
        String fileName = "dataFiles/test.csv";

        data1 = "";
        data2 = Float.toString(rd.nextFloat());
        data3 = "";
        data4 = "";

        try {
            String[][] data = {
                {"A", "", "C", "D"},
                {data1, data2, data3, data4}
            };

            File csvFile = new File(fileName);
            FileWriter fileWriter = new FileWriter(csvFile);

            for (String[] d : data) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < d.length; i++) {
                    sb.append(d[i]);
                    if (i != d.length - 1) {
                        sb.append(',');
                    }
                }
                sb.append("\n");
                fileWriter.write(sb.toString());
            }
            
            fileWriter.close();
        } catch (IOException e) {//IO
            System.out.println("Exception occured "+ e);
        }

        return fileName;
    }
}
