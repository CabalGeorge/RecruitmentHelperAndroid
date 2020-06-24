package com.example.unitbvevents.popups;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unitbvevents.R;
import com.example.unitbvevents.config.Constant;
import com.example.unitbvevents.config.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FabPopUp extends Activity {

    EditText eventName, dateTime, eventLocation, eventSeats;
    TextView createdBy;
    SessionManager session;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "session";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        session = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String creator = session.getSessionUsername();

        createdBy = findViewById(R.id.creator);
        createdBy.setText(creator);

        eventName = findViewById(R.id.eventName);
        dateTime = findViewById(R.id.datetime);
        eventLocation = findViewById(R.id.eventLocation);
        eventSeats=findViewById(R.id.eventSeats);

        Button create = findViewById(R.id.btn_add);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

        findViewById(R.id.btn_date_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateTime);
            }
        });


    }


    private void addEvent() {

        String url = Constant.ADD_URL;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("SUCCESSFUL")) {
                    Toast.makeText(getApplicationContext(), "Event successfully added!", Toast.LENGTH_LONG).show();
                } else if (response.equals("EXISTING EVENT")) {
                    Toast.makeText(getApplicationContext(), "Event with this name already exists!", Toast.LENGTH_LONG).show();
                } else if(response.equals("NEED NAME")){
                    Toast.makeText(getApplicationContext(), "Please provide a name for your event!", Toast.LENGTH_LONG).show();
                } else if(response.equals("NEED DATETIME")){
                    Toast.makeText(getApplicationContext(), "Please choose a date for your event!", Toast.LENGTH_LONG).show();
                } else if(response.equals("NEED LOCATION")) {
                    Toast.makeText(getApplicationContext(), "Please provide a location for your event!", Toast.LENGTH_LONG).show();
                } else if(response.equals("NEED SEATS")){
                    Toast.makeText(getApplicationContext(), "Please provide a number of seats!",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", eventName.getText().toString().trim());
                params.put("time", dateTime.getText().toString().trim());
                params.put("location", eventLocation.getText().toString().trim());
                params.put("username", createdBy.getText().toString().trim());
                params.put("seats", eventSeats.getText().toString().trim());
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }


    private void showDateTimeDialog(EditText dateTime) {
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

                        dateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(FabPopUp.this,R.style.DialogTheme, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        };

        new DatePickerDialog(FabPopUp.this,R.style.DialogTheme, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }



}
