package com.example.recruitmenthelper.ui.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.popups.ReportsPopUp;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ReportsFragment extends Fragment {

    private ReportsViewModel mViewModel;
    FloatingActionButton fab;
    Button reportsBtn;
    TextView txtActiveCandidates, txtArchivedCandidates, txtInterviews, txtPositiveFeedbacks, txtNegativeFeedbacks;
    private float activeCandidates, archivedCandidates, nrInterviews, positiveFeedbacks, negativeFeedbacks;
    BarChart barChart;
    String[] legendName = {"Active candidates", "Archived candidates", "Interviews", "Positive feedbacks", "Negative feedbacks"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        txtActiveCandidates = root.findViewById(R.id.numberActiveCandidates);
        txtArchivedCandidates = root.findViewById(R.id.numberArchivedCandidates);
        txtInterviews = root.findViewById(R.id.numberInterviews);
        txtPositiveFeedbacks = root.findViewById(R.id.numberPositiveFeedbacks);
        txtNegativeFeedbacks = root.findViewById(R.id.numberNegativeFeedbacks);
        getActivity().setTitle("Reports");

        barChart = root.findViewById(R.id.barChart);

        reportsBtn = root.findViewById(R.id.btn_application_reports);
        reportsBtn.setOnClickListener(view -> startActivity(new Intent(getContext(), ReportsPopUp.class)));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String urlActiveCandidates = Constant.COUNT_ACTIVE_CANDIDATES_URL;
        StringRequest activeCandidatesRequest = new StringRequest(Request.Method.GET, urlActiveCandidates, activeResponse -> {

            txtActiveCandidates.setText(activeResponse);

            String urlArchivedCandidates = Constant.COUNT_ARCHIVED_CANDIDATES_URL;
            StringRequest archivedCandidatesRequest = new StringRequest(Request.Method.GET, urlArchivedCandidates, archivedResponse -> {

                txtArchivedCandidates.setText(archivedResponse);

                String urlInterviews = Constant.COUNT_ALL_INTERVIEWS_URL;
                StringRequest interviewsRequest = new StringRequest(Request.Method.GET, urlInterviews, interviewsResponse -> {

                    txtInterviews.setText(interviewsResponse);

                    String urlPositiveFeedbacks = Constant.COUNT_POSITIVE_FEEDBACKS_URL;
                    StringRequest positiveFeedbacksRequest = new StringRequest(Request.Method.GET, urlPositiveFeedbacks, positiveFeedbacksResponse -> {

                        txtPositiveFeedbacks.setText(positiveFeedbacksResponse);

                        String urlNegativeFeedbacks = Constant.COUNT_NEGATIVE_FEEDBACKS_URL;
                        StringRequest negativeFeedbacksRequest = new StringRequest(Request.Method.GET, urlNegativeFeedbacks, negativeFeedbacksResponse -> {

                            txtNegativeFeedbacks.setText(negativeFeedbacksResponse);

                            int[] colorClassArray = {getResources().getColor(R.color.chartLimeGreen), getResources().getColor(R.color.chartPaleGreen), getResources().getColor(R.color.chartColorBlue),
                                    getResources().getColor(R.color.chartColorYellow), getResources().getColor(R.color.chartColorPaleYellow)};

                            Legend legend = barChart.getLegend();
                            legend.setEnabled(true);
                            legend.setTextSize(15);
                            legend.setForm(Legend.LegendForm.SQUARE);
                            legend.setFormSize(15f);
                            legend.setXEntrySpace(20);
                            legend.setFormToTextSpace(10);
                            legend.setWordWrapEnabled(true);

                            LegendEntry[] legendEntries = new LegendEntry[5];
                            for (int index = 0; index < legendEntries.length; index++) {
                                LegendEntry entry = new LegendEntry();
                                entry.formColor = colorClassArray[index];
                                entry.label = String.valueOf(legendName[index]);
                                legendEntries[index] = entry;
                            }
                            legend.setCustom(legendEntries);
                            activeCandidates = Float.parseFloat(txtActiveCandidates.getText().toString());
                            archivedCandidates = Float.parseFloat(txtArchivedCandidates.getText().toString());
                            nrInterviews = Float.parseFloat(txtInterviews.getText().toString());
                            positiveFeedbacks = Float.parseFloat(txtPositiveFeedbacks.getText().toString());
                            negativeFeedbacks = Float.parseFloat(txtNegativeFeedbacks.getText().toString());
                            ArrayList<BarEntry> barEntries = new ArrayList<>();
                            barEntries.add(new BarEntry(0, activeCandidates));
                            barEntries.add(new BarEntry(1, archivedCandidates));
                            barEntries.add(new BarEntry(2, nrInterviews));
                            barEntries.add(new BarEntry(3, positiveFeedbacks));
                            barEntries.add(new BarEntry(4, negativeFeedbacks));

                            BarDataSet barDataSet = new BarDataSet(barEntries, "Stats");
                            barDataSet.setColors(getResources().getColor(R.color.chartLimeGreen), getResources().getColor(R.color.chartPaleGreen),
                                    getResources().getColor(R.color.chartColorBlue), getResources().getColor(R.color.chartColorYellow),
                                    getResources().getColor(R.color.chartColorPaleYellow));
                            barDataSet.setValueTextSize(15f);


                            BarData barData = new BarData(barDataSet);
                            barData.setBarWidth(0.8f);


                            barChart.getXAxis().setEnabled(false);
                            barChart.setVisibility(View.VISIBLE);
                            barChart.animateY(4000);
                            barChart.setData(barData);
                            barChart.setFitBars(true);
                            barChart.getDescription().setEnabled(false);
                            barChart.invalidate();

                        }, error -> Log.d("tag", "onErrorResponse" + error.getMessage())) {
                        };
                        requestQueue.add(negativeFeedbacksRequest);

                    }, error -> Log.d("tag", "onErrorResponse" + error.getMessage())) {
                    };
                    requestQueue.add(positiveFeedbacksRequest);

                }, error -> Log.d("tag", "onErrorResponse" + error.getMessage())) {
                };
                requestQueue.add(interviewsRequest);

            }, error -> Log.d("tag", "onErrorResponse" + error.getMessage()));

            requestQueue.add(archivedCandidatesRequest);
        }, error -> Log.d("tag", "onErrorResponse" + error.getMessage()));

        requestQueue.add(activeCandidatesRequest);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ReportsViewModel.class);
    }

}