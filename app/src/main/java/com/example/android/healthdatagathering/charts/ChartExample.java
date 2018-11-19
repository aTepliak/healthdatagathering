package com.example.android.healthdatagathering.charts;

import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;

import java.util.HashMap;

public class ChartExample {


    public Cartesian getBloodSugarExample() {
        HashMap<String, Float> data = new HashMap<>();
        data.put("11-11", 320f);
        data.put("12-11", 400f);
        data.put("13-11", 80f);


        ColumnChart cartesian = new ColumnChart(data, "Caffeine Intake for last 3 days", "", "", "mg");
        Cartesian getCartExample = cartesian.getCartesian();
        return getCartExample;
    }

    public Cartesian getBloodSugarExample2() {
        HashMap<String, Float> data = new HashMap<>();
        data.put("11-11", 5.38f);
        data.put("12-11", 5.66f);
        data.put("13-11", 3.77f);


        ColumnChart cartesian = new ColumnChart(data, "Caffeine Intake for last 3 days", "", "", "mg");
        Cartesian getCartExample = cartesian.getCartesian();
        return getCartExample;
    }


    public Pie getSleepExample() {
        HashMap<String, Integer> sleepData = new HashMap<>();
        sleepData.put("deep", 64);
        sleepData.put("light", 321);
        sleepData.put("REM", 57);

        PieChart pie = new PieChart(sleepData);
        return pie.getPieChart();
    }

    public Cartesian getHeartRateExample() {
        HashMap<String, Integer[]> data1 = new HashMap<String, Integer[]>();
        data1.put("11-11", new Integer[]{3458, 512, 202});
        data1.put("12-11", new Integer[]{8471, 416, 38});
        data1.put("13-11", new Integer[]{12147, 785, 0});

        LineChart line = new LineChart(data1, "Distance", "meters", "walking", "running", "swimming");
        return line.getCartesian();
    }
}
