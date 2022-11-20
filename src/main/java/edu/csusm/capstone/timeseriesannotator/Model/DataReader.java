package edu.csusm.capstone.timeseriesannotator.Model;


/**
 *
 * @author Cody McKinney
 */
public interface DataReader {

    /**
     *
     * @param fileName - name of the file that has been selected
     */
    public void buildDataList(String fileName);
    
}
