package com.example.android.healthdatagathering.charts;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PieChart {



    Pie pieChart;
    List<DataEntry> data = new ArrayList<>();

    public PieChart(HashMap<String, Integer> inputData) {
        this.pieChart = AnyChart.pie();
        inputData.forEach((k,v)->this.data.add(new ValueDataEntry(k,v)));
        pieChart.data(this.data);

    }

    public Pie getPieChart() {
        return pieChart;
    }
}
