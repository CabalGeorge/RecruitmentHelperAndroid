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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditPopUp extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText eventName, dateTime, eventLocation;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "session";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);


        eventName = findViewById(R.id.eventName);
        dateTime = findViewById(R.id.datetime);
        eventLocation = findViewById(R.id.eventLocation);

        Button update = findViewById(R.id.btn_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();

            }
        });

        findViewById(R.id.btn_date_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        findViewById(R.id.btn_time_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + month + "/" + year + " ";
        dateTime.setText(date);
    }


    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme, this,
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        dateTime.setText(dateTime.getText() + time);
    }


    private void updateData() {

        sessionManager = new SessionManager(getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String urlGet = Constant.GETEVENT_URL;
        StringRequest stringRequestGet = new StringRequest(Request.Method.POST, urlGet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("createdBy").equals(sessionManager.getSessionUsername())) {


                        String urlEdit = Constant.UPDATE_URL;
                        StringRequest stringRequestEdit = new StringRequest(Request.Method.POST, urlEdit, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("SUCCESSFUL")) {
                                    Toast.makeText(getApplicationContext(), "Event successfully updated!", Toast.LENGTH_LONG).show();
                                } else if (response.equals("NEED NAME")) {
                                    Toast.makeText(getApplicationContext(), "Event name can't be empty!", Toast.LENGTH_LONG).show();
                                } else if (response.equals("NEED DATE")) {
                                    Toast.makeText(getApplicationContext(), "Event date and time can't be empty!", Toast.LENGTH_LONG).show();
                                } else if (response.equals("NEED LOCATION")) {
                                    Toast.makeText(getApplicationContext(), "Event location can't be empty!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Event could not be updated!", Toast.LENGTH_LONG).show();
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
                                params.put("dateTime", dateTime.getText().toString().trim());
                                params.put("location", eventLocation.getText().toString().trim());
                                return params;
                            }
                        };

                        requestQueue.add(stringRequestEdit);
                    } else {
                        Toast.makeText(getApplicationContext(), "You are not allowed to edit this event", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
                return params;
            }
        };

        requestQueue.add(stringRequestGet);

    }
}
