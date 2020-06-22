package com.example.unitbvevents.ui.reports;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.unitbvevents.R;
import com.example.unitbvevents.activities.EventsActivity;
import com.example.unitbvevents.config.Constant;
import com.example.unitbvevents.model.Event;
import com.example.unitbvevents.model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends Fragment {

    private ReportsViewModel mViewModel;
    TextView events, users, attendants;
    private float eventsNumber, usersNumber, attendance;
    private double occupiedSeats, totalSeats;
    List<Event> eventsList;
    List<User> usersList;
    Button move;
    BarChart barChart;
    String[] legendName = {"Events", "Users", "Attendance percentage"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        events = root.findViewById(R.id.numberEvents);
        users = root.findViewById(R.id.numberUsers);
        attendants = root.findViewById(R.id.attendance);
        getActivity().setTitle("Reports");

        barChart = root.findViewById(R.id.barChart);

        eventsList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String urlEvents = Constant.GETEVENTS_URL;
        JsonArrayRequest jsonArrayRequestEvents = new JsonArrayRequest(Request.Method.GET, urlEvents, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject eventObject = response.getJSONObject(i);

                        Event event = new Event();
                        event.setName(eventObject.getString("name"));
                        event.setSeats(eventObject.getString("seats"));
                        eventsList.add(event);
                        JSONArray jsonArray = eventObject.getJSONArray("userList");
                        occupiedSeats = occupiedSeats + jsonArray.length();
                        totalSeats = totalSeats + Double.parseDouble(eventObject.getString("seats")) + occupiedSeats;
                        events.setText(String.valueOf(eventsList.size()));
                        attendants.setText(String.valueOf(occupiedSeats / totalSeats * 100));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                usersList = new ArrayList<>();
                String urlUsers = Constant.GETUSERS_URL;
                JsonArrayRequest jsonArrayRequestUsers = new JsonArrayRequest(Request.Method.GET, urlUsers, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject eventObject = response.getJSONObject(i);

                                User user = new User();
                                user.setUsername(eventObject.getString("username"));
                                usersList.add(user);
                                users.setText(String.valueOf(usersList.size()));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                        int[] colorClassArray = {getResources().getColor(R.color.chartColorRed), getResources().getColor(R.color.chartColorBlue), getResources().getColor(R.color.chartColorYellow)};

                        Legend legend = barChart.getLegend();
                        legend.setEnabled(true);
                        legend.setTextSize(15);
                        legend.setForm(Legend.LegendForm.SQUARE);
                        legend.setFormSize(10f);
                        legend.setXEntrySpace(15);
                        legend.setFormToTextSpace(10);

                        LegendEntry[] legendEntries = new LegendEntry[3];
                        for (int index = 0; index < legendEntries.length; index++) {
                            LegendEntry entry = new LegendEntry();
                            entry.formColor = colorClassArray[index];
                            entry.label = String.valueOf(legendName[index]);
                            legendEntries[index] = entry;
                        }
                        legend.setCustom(legendEntries);
                        eventsNumber = Float.parseFloat(events.getText().toString());
                        usersNumber = Float.parseFloat(users.getText().toString());
                        attendance = Float.parseFloat(attendants.getText().toString());
                        ArrayList<BarEntry> barEntries = new ArrayList<>();
                        barEntries.add(new BarEntry(0, eventsNumber));
                        barEntries.add(new BarEntry(1, usersNumber));
                        barEntries.add(new BarEntry(2, attendance));

                        BarDataSet barDataSet = new BarDataSet(barEntries, "Stats");
                        barDataSet.setColors(getResources().getColor(R.color.chartColorRed), getResources().getColor(R.color.chartColorBlue), getResources().getColor(R.color.chartColorYellow));
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

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("tag", "onErrorResponse" + error.getMessage());
                    }
                });

                requestQueue.add(jsonArrayRequestUsers);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse" + error.getMessage());
            }
        });

        requestQueue.add(jsonArrayRequestEvents);


        move = root.findViewById(R.id.btn_move);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEventsActivity();
            }
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ReportsViewModel.class);
    }

    public void openEventsActivity() {
        Intent intent = new Intent(getContext(), EventsActivity.class);
        startActivity(intent);
    }


}