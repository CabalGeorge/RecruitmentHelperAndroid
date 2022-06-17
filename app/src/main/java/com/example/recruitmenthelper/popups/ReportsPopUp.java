package com.example.recruitmenthelper.popups;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.config.SessionManager;
import com.example.recruitmenthelper.model.Candidate;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportsPopUp extends Activity {

    List<Candidate> candidateList = new ArrayList<>();
    TextView totalApplications;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    PieChart pieChart;
    private static final String SHARED_PREF_NAME = "session";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_reports);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        totalApplications = findViewById(R.id.totalApplications);
        pieChart = findViewById(R.id.pieChart);
        setUpPieChart();
        loadPieChartData();

    }

    private void setUpPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(15f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Applications by domains");
        pieChart.setCenterTextSize(15f);
        pieChart.getDescription().setEnabled(false);

        Legend legend = pieChart.getLegend();
        legend.setTextSize(15f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);
        legend.setWordWrapEnabled(true);
    }

    private void loadPieChartData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.GET_ALL_CANDIDATES_URL;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject candidateObject = response.getJSONObject(i);

                    Candidate candidate = new Candidate();
                    candidate.setInterestPosition(candidateObject.getString("interestPosition"));
                    candidateList.add(candidate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            long javaApplications = candidateList.stream().filter(candidate -> candidate.getInterestPosition().toLowerCase(Locale.ROOT).contains("java")).count();
            long CSharpApplications = candidateList.stream().filter(candidate -> candidate.getInterestPosition().toLowerCase(Locale.ROOT).contains("c#")).count();
            long otherApplications = candidateList.size() - javaApplications - CSharpApplications;

            totalApplications.append("Total applications: " + candidateList.size());
            List<PieEntry> pieEntries = new ArrayList<>();
            pieEntries.add(new PieEntry(javaApplications, "Java"));
            pieEntries.add(new PieEntry(CSharpApplications, "C#"));
            pieEntries.add(new PieEntry(otherApplications, "Other"));

            int[] colorClassArray = {getResources().getColor(R.color.chartLimeGreen), getResources().getColor(R.color.chartPaleGreen),
                    getResources().getColor(R.color.chartColorPaleYellow)};

            PieDataSet dataSet = new PieDataSet(pieEntries,"");
            dataSet.setColors(colorClassArray);

            PieData pieData = new PieData(dataSet);
            pieData.setDrawValues(true);
            pieData.setValueFormatter(new PercentFormatter(pieChart));
            pieData.setValueTextSize(15f);
            pieData.setValueTextColor(Color.BLACK);

            pieChart.setData(pieData);
            pieChart.invalidate();

            pieChart.animateY(1600, Easing.EaseInOutQuad);
        }, error -> Log.d("tag", "onErrorResponse" + error.getMessage())) {
        };

        requestQueue.add(jsonArrayRequest);
    }
}
