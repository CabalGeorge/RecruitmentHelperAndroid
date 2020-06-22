package com.example.unitbvevents.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.unitbvevents.R;
import com.example.unitbvevents.config.Adapter;
import com.example.unitbvevents.config.Constant;
import com.example.unitbvevents.config.SessionManager;
import com.example.unitbvevents.model.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EventsActivity extends AppCompatActivity{

    EditText dateTimeStart, dateTimeEnd;
    TextView eventsNumber;
    Button generate, datePickerStart, datePickerEnd;
    RecyclerView recyclerView;
    List<Event> events;
    Adapter adapter;
    SessionManager sessionManager;
    double totalSeats,occupiedSeats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        dateTimeStart = findViewById(R.id.startDate);
        datePickerStart = findViewById(R.id.btn_date_picker_start);

        eventsNumber = findViewById(R.id.eventsNumber);
        generate = findViewById(R.id.btn_generate);

        dateTimeEnd = findViewById(R.id.endDate);
        datePickerEnd = findViewById(R.id.btn_date_picker_end);


        datePickerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialogStart(dateTimeStart);
            }
        });

        datePickerEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialogEnd(dateTimeEnd);
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dateTimeStart.getText().toString().isEmpty()||dateTimeEnd.getText().toString().isEmpty()){
                    Toast.makeText(EventsActivity.this,"Start date and end date can not be empty ",Toast.LENGTH_LONG).show();
                }
                else {
                    getSearchedEvents(dateTimeStart,dateTimeEnd);
                }
            }
        });



    }

    private void getSearchedEvents(EditText startDate, EditText endDate){


        events = new ArrayList<>();
        sessionManager = new SessionManager(EventsActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(EventsActivity.this);
        String url = Constant.GETEVENTS_URL;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject eventObject = response.getJSONObject(i);

                        Event event = new Event();
                        event.setName(eventObject.getString("name"));
                        event.setLocation(eventObject.getString("location"));
                        event.setDateTime(eventObject.getString("dateTime"));
                        event.setSeats(eventObject.getString("seats"));

                        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        String start=startDate.getText().toString().trim();
                        String end=endDate.getText().toString().trim();
                        String provided=eventObject.getString("dateTime");
                        LocalDateTime startingDate=LocalDateTime.parse(start,formatter);
                        LocalDateTime endingDate=LocalDateTime.parse(end,formatter);
                        LocalDateTime providedDate=LocalDateTime.parse(provided,formatter);

                        if(providedDate.compareTo(startingDate)>0&&providedDate.compareTo(endingDate)<0){
                            events.add(event);
                            JSONArray jsonArray=eventObject.getJSONArray("userList");
                            occupiedSeats=occupiedSeats+jsonArray.length();
                            totalSeats=totalSeats+Integer.parseInt(eventObject.getString("seats"))+occupiedSeats;
                            eventsNumber.setText(eventsNumber.getText()+String.valueOf(events.size())+"    Attendance: "+String.valueOf(occupiedSeats/totalSeats*100)+"%");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                recyclerView = findViewById(R.id.searchedEvents);
                adapter = new Adapter(EventsActivity.this, events);
                recyclerView.setLayoutManager(new LinearLayoutManager(EventsActivity.this));
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse" + error.getMessage());
            }
        }) {

        };

        requestQueue.add(jsonArrayRequest);
    }

    private void showDateTimeDialogStart(EditText dateTimeStart) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm");

                        dateTimeStart.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(EventsActivity.this,R.style.DialogTheme, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        };

        new DatePickerDialog(EventsActivity.this,R.style.DialogTheme, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showDateTimeDialogEnd(EditText dateTimeEnd) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm");

                        dateTimeEnd.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(EventsActivity.this,R.style.DialogTheme, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        };

        new DatePickerDialog(EventsActivity.this,R.style.DialogTheme, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }


}