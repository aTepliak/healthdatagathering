package com.example.android.healthdatagathering.charts;

import com.anychart.AnyChart;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;

import java.util.HashMap;

public class ChartExample {





    public Cartesian getBloodSugarExample() {
        HashMap<String, Integer> data = new HashMap<>();
        data.put("02-10", 103);
        data.put("03-10", 99);
        data.put("04-10", 112);


        ColumnChart cartesian = new ColumnChart(data, "Blood Sugar values for last 3 days", "", "", "Mg/dL");
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
        data1.put("02-10", new Integer[] {52, 62, 98});
        data1.put("03-10",new Integer[] {54, 60, 112} );
        data1.put("04-10", new Integer[] {49, 64, 100} );

        LineChart line = new LineChart(data1, "heart bit", "BpM","min","average","max"  );
        return  line.getCartesian();
    }
}
