package com.example.android.healthdatagathering.charts;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColumnChart {




    private Cartesian cartesian;
    private List<DataEntry> data = new ArrayList<>();
   private Column column;


    public ColumnChart(HashMap<String, Integer> inputData, String generalTitle, String xAxisTitle, String yAxisTitle, String unit) {
        cartesian = AnyChart.column();
        inputData.forEach((k,v)->this.data.add(new ValueDataEntry(k,v)));
         column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format( unit + "{%Value}{groupsSeparator: }");
        cartesian.animation(true);
        cartesian.title(generalTitle);

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format(unit+"{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title(xAxisTitle);
        cartesian.yAxis(0).title(yAxisTitle);

    }

    public Cartesian getCartesian() {
        return cartesian;
    }
}
