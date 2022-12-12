package com.example.alertosannicolas.ui.statistic;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.alertosannicolas.ReportHistoryModel;
import com.example.alertosannicolas.databinding.FragmentEmegencyBinding;
import com.example.alertosannicolas.databinding.FragmentStatisticBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class StatisticFragment extends Fragment {

    private FragmentStatisticBinding binding;
    Integer[] valuesList = {
            0,0,0,0,0,0,0,0,0,0,0,0
    };
    ArrayList barArrayList;
    BarChart barChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatisticViewModel galleryViewModel =
                new ViewModelProvider(this).get(StatisticViewModel.class);

        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        barChart = binding.barchart;

        getData();
        return root;
    }

    private  void getData(){
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("reportsHistory")
                ;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
//                Post post = dataSnapshot.getValue(Post.class);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReportHistoryModel report = snapshot.getValue(ReportHistoryModel.class);
                    Date date = new Date(report.getDate());
                    valuesList[date.getMonth()] += 1;
                }
                setUpChart(valuesList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);
    }

    void setUpChart(Integer[] valuesList){
        //prepare Bar Entries
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < valuesList.length; i++) {
            BarEntry barEntry = new BarEntry(i + 1, valuesList[i]); //start always from x=1 for the first bar
            entries.add(barEntry);
        }


        //initialize x Axis Labels (labels for 13 vertical grid lines)
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("J"); //this label will be mapped to the 1st index of the valuesList
        xAxisLabel.add("F");
        xAxisLabel.add("M");
        xAxisLabel.add("A");
        xAxisLabel.add("M");
        xAxisLabel.add("J");
        xAxisLabel.add("J");
        xAxisLabel.add("A");
        xAxisLabel.add("S");
        xAxisLabel.add("O");
        xAxisLabel.add("N");
        xAxisLabel.add("D");
        xAxisLabel.add(""); //empty label for the last vertical grid line on Y-Right Axis


        //initialize xAxis
        XAxis xAxis = barChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(14);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setDrawGridLines(true);

        xAxis.setAxisMinimum(0 + 0.5f); //to center the bars inside the vertical grid lines we need + 0.5 step
//        xAxis.setAxisMaximum(entries.size() + 0.5f); //to center the bars inside the vertical grid lines we need + 0.5 step
        xAxis.setLabelCount(xAxisLabel.size(), true); //draw x labels for 13 vertical grid lines
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setXOffset(0f); //labels x offset in dps
        xAxis.setYOffset(0f); //labels y offset in dps
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xAxisLabel.get((int) Math.floor(value));
            }
        });
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);

        //initialize Y-Right-Axis
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setTextColor(Color.BLACK);
        rightAxis.setTextSize(14);
        rightAxis.setDrawAxisLine(true);
        rightAxis.setAxisLineColor(Color.BLACK);
        rightAxis.setDrawGridLines(true);
        rightAxis.setGranularity(1f);
        rightAxis.setGranularityEnabled(true);
        rightAxis.setAxisMinimum(0);
//        rightAxis.setAxisMaximum(6000f);
        rightAxis.setLabelCount(4, true); //draw y labels (Y-Values) for 4 horizontal grid lines starting from 0 to 6000f (step=2000)
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        //initialize Y-Left-Axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);
        leftAxis.setGranularity(1f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setLabelCount(0, true);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });

        //set the BarDataSet
        BarDataSet barDataSet = new BarDataSet(entries, "Months");
        barDataSet.setFormSize(15f);
//        barDataSet.setDrawValues(false);
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        //set the BarData to chart
        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.setScaleEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setDrawBarShadow(false);
//        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        barChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}