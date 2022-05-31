package com.example.recruitmenthelper.popups;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditUserPopUp extends Activity {

    EditText editUsername, editEmail;
    Spinner role;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "session";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String username = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String userRole = getIntent().getStringExtra("role");

        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);

        role = findViewById(R.id.editRole);
        String[] roles = new String[]{"HR_REPRESENTATIVE", "TECHNICAL_INTERVIEWER", "PTE", "ADMIN"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_role, roles);
        role.setAdapter(adapter);
        role.setSelection(getIndex(role, userRole));

        editUsername.append(username);
        editEmail.append(email);

        Button update = findViewById(R.id.btn_update);
        update.setOnClickListener(v -> {
            if (editUsername.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please provide a name!", Toast.LENGTH_LONG).show();
            } else if (editEmail.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please provide an email address!", Toast.LENGTH_LONG).show();
            } else {
            updateUser();}
        });
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }


    private void updateUser() {
        sessionManager = new SessionManager(getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject getObject = new JSONObject();
        try {
            getObject.put("email", editEmail.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String urlGet = Constant.GET_USER_BY_EMAIL_URL;
        JsonObjectRequest getJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGet, getObject, response -> {
            try {
                if (response.isNull("email") || response.getString("userId").equals(getIntent().getStringExtra("id"))) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("userId", getIntent().getStringExtra("id"));
                        object.put("name", editUsername.getText().toString().trim());
                        object.put("email", editEmail.getText().toString().trim());
                        object.put("role", role.getSelectedItem().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String urlEdit = Constant.UPDATE_USER_URL;
                    JsonObjectRequest updateJsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlEdit, object, response1 -> {
                        try {
                            if (response1.getString("name").contentEquals(editUsername.getText().toString().trim())
                                    && response1.getString("email").contentEquals(editEmail.getText().toString().trim())
                                    && response1.getString("role").contentEquals(role.getSelectedItem().toString())) {
                                Toast.makeText(getApplicationContext(), "User modified successfully", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> Toast.makeText(getApplicationContext(), "error" + error.toString(), Toast.LENGTH_LONG).show()) {};
                    requestQueue.add(updateJsonObjectRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "User with this email already exists", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(getApplicationContext(), "error" + error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", editEmail.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(getJsonObjectRequest);
    }

//    private void showDateTimeDialog(EditText eventDateTime) {
//        final Calendar calendar = Calendar.getInstance();
//        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                calendar.set(Calendar.YEAR, year);
//                calendar.set(Calendar.MONTH, month);
//                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                        calendar.set(Calendar.MINUTE, minute);
//
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm");
//
//                        eventDateTime.setText(simpleDateFormat.format(calendar.getTime()));
//                    }
//                };
//
//                new TimePickerDialog(EditUserPopUp.this,R.style.DialogTheme, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
//            }
//        };
//
//        new DatePickerDialog(EditUserPopUp.this,R.style.DialogTheme, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//
//    }
}
