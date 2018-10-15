package com.example.android.healthdatagathering.charts;

import com.anychart.AnyChart;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;

import java.util.HashMap;

public class ChartExample {





    public Cartesian getBloodSugarExample() {
        HashMap<String, Integer> data = new HashMap<>();
        data.put("02-10", 300);
        data.put("03-10", 200);
        data.put("04-10", 350);


        ColumnChart cartesian = new ColumnChart(data, "Caffeine Intake for last 3 days", "", "", "mg");
        Cartesian getCartExample = cartesian.getCartesian();
       return  getCartExample;
    }
    public  Pie getSleepExample() {
        HashMap<String, Integer> sleepData = new HashMap<>();
        sleepData.put("deep", 64);
        sleepData.put("light",321 );
        sleepData.put("REM", 57);

        PieChart pie = new PieChart(sleepData );
        return  pie.getPieChart();
    }

    public  Cartesian getHeartRateExample(){
        HashMap<String, Integer[]> data1 = new HashMap<String, Integer[]>();
        data1.put("02-10", new Integer[] {3458, 512, 202});
        data1.put("03-10",new Integer[] {8471, 416, 38} );
        data1.put("04-10", new Integer[] {12147, 785, 0} );

        LineChart line = new LineChart(data1, "Distance", "meters","walking","running","swimming"  );
        return  line.getCartesian();
    }
}
