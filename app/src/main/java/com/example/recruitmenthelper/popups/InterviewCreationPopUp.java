package com.example.recruitmenthelper.popups;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.config.SessionManager;
import com.example.recruitmenthelper.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class InterviewCreationPopUp extends Activity {

    EditText txtlocation, txtDateTime;
    Spinner interviewer1, interviewer2, interviewer3;
    Button dateTimePicker, schedule;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    List<Integer> interviewersList = new ArrayList<>();
    List<User> userList = new ArrayList<>();

    private static final String SHARED_PREF_NAME = "session";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interview_creation_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String candidateId = getIntent().getStringExtra("id");

        txtlocation = findViewById(R.id.txt_interviewLocation);
        txtDateTime = findViewById(R.id.txt_interviewDateTime);
        interviewer1 = findViewById(R.id.interviewer1);
        interviewer2 = findViewById(R.id.interviewer2);
        interviewer3 = findViewById(R.id.interviewer3);
        dateTimePicker = findViewById(R.id.btn_interview_date_time);
        schedule = findViewById(R.id.btn_schedule_interview);

        dateTimePicker.setOnClickListener(v -> showDateTimeDialog(txtDateTime));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Constant.GET_ALL_USERS_URL;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject userObject = response.getJSONObject(i);

                    User user = new User();
                    user.setUser_id(userObject.getInt("userId"));
                    user.setUsername(userObject.getString("name"));
                    user.setEmail(userObject.getString("email"));
                    user.setRole(userObject.getString("role"));
                    userList.add(user);
                    List<User> interviewers = userList.stream()
                            .filter(user1 -> user1.getRole().equals("HR_REPRESENTATIVE") || user1.getRole().equals("TECHNICAL_INTERVIEWER"))
                            .collect(Collectors.toList());
                    String[] users = new String[interviewers.size() + 1];
                    users[0] = "No selection";
                    for (int j = 0; j < interviewers.size(); j++) {
                        users[j + 1] = interviewers.get(j).getUsername();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_user, users);
                    interviewer1.setAdapter(adapter);
                    interviewer2.setAdapter(adapter);
                    interviewer3.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, error -> Log.d("tag", "onErrorResponse" + error.getMessage())) {
        };
        requestQueue.add(jsonArrayRequest);


        schedule = findViewById(R.id.btn_schedule_interview);
        schedule.setOnClickListener(v -> {
            if (txtlocation.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please provide a location!", Toast.LENGTH_LONG).show();
            } else if (txtDateTime.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please provide a date and an hour!", Toast.LENGTH_LONG).show();
            } else if (interviewer1.getSelectedItemPosition() == 0 && interviewer2.getSelectedItemPosition() == 0 && interviewer3.getSelectedItemPosition() == 0) {
                Toast.makeText(getApplicationContext(), "Please select at least one interviewer!", Toast.LENGTH_LONG).show();
            } else {
                createInterview(candidateId);
            }
        });
    }

    private void createInterview(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject object = new JSONObject();
        try {
            object.put("candidateId", id);
            object.put("location", txtlocation.getText().toString().trim());
            String date = convertDateFormat(txtDateTime.getText().toString().substring(0,10).replace("/","-"));
            String time = txtDateTime.getText().toString().substring(11, 16);
            getUserIdFromSpinner(interviewersList, userList, interviewer1);
            getUserIdFromSpinner(interviewersList, userList, interviewer2);
            getUserIdFromSpinner(interviewersList, userList, interviewer3);

            object.put("dateTime", date + "T" + time);

            JSONArray jsonArray = new JSONArray(interviewersList);
            object.put("interviewers", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = Constant.SCHEDULE_INTERVIEW_URL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, postResponse -> {
            try {
                if (postResponse.getString("location").contentEquals(txtlocation.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Interview created successfully!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error while creating interview!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(), "error" + error.toString(), Toast.LENGTH_LONG).show()) {
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getUserIdFromSpinner(List<Integer> idList, List<User> userList, Spinner spinner) {
        if(spinner.getSelectedItemPosition()!=0) {
            idList.add(userList.stream().filter(user -> user.getUsername().equals(spinner.getSelectedItem().toString()))
                    .collect(Collectors.toList()).get(0).getUser_id());
        }
    }

    private void showDateTimeDialog(EditText dateTimeStart) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog.OnTimeSetListener timeSetListener = (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm");

                dateTimeStart.setText(simpleDateFormat.format(calendar.getTime()));
            };

            new TimePickerDialog(InterviewCreationPopUp.this, R.style.DialogTheme, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        };

        new DatePickerDialog(InterviewCreationPopUp.this, R.style.DialogTheme, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String convertDateFormat(String stringDate) {
        DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return targetFormat.format(date);
    }
}
