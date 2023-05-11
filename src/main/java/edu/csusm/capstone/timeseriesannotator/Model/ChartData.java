package edu.csusm.capstone.timeseriesannotator.Model;

import edu.csusm.capstone.timeseriesannotator.Controller.ChartStruct;

public interface ChartData {

    public void createDataset(String name, ChartStruct Cs);

    public void addDataset(String name);
}
