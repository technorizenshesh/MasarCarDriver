package com.masarcardriver.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.masarcardriver.R;
import com.masarcardriver.adapter.AdapterYear;
import com.masarcardriver.databinding.ActivityStatisticsBinding;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;

public class TripStatisticsAct extends AppCompatActivity {
    ActivityStatisticsBinding binding;
    String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
            "Oct", "Nov", "Dec"};
    int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18};
    ArrayList<String>arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<>();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_statistics);
        initView();
    }

    private void initView() {
        binding.header.tvTitle.setText(getString(R.string.trip_statistic));
        binding.header.ivBack.setOnClickListener(v -> {finish();});

        arrayList.add("2010");
        arrayList.add("2011");
        arrayList.add("2012");
        arrayList.add("2013");
        arrayList.add("2014");
        arrayList.add("2015");
        arrayList.add("2016");
        arrayList.add("2017");
        arrayList.add("2018");
        arrayList.add("2019");
        arrayList.add("2020");
        arrayList.add("2021");
        arrayList.add("2022");

        binding.spinnerYear.setAdapter(new AdapterYear(TripStatisticsAct.this,arrayList));


        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();


        Line line = new Line(yAxisValues).setColor(Color.parseColor("#C3FFB1"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(10);
        axis.setTextColor(Color.parseColor("#93DD7D"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Earnings");
        yAxis.setTextColor(Color.parseColor("#93DD7D"));
        yAxis.setTextSize(10);
        data.setAxisYLeft(yAxis);

        binding.chart.setLineChartData(data);
        Viewport viewport = new Viewport( binding.chart.getMaximumViewport());
        viewport.top = 100;
        binding.chart.setMaximumViewport(viewport);
        binding.chart.setCurrentViewport(viewport);
    }
}
