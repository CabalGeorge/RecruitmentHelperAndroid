package com.example.recruitmenthelper.popups;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.config.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class AddFeedbackPopUp extends Activity {

    EditText feedback;
    Spinner status;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "session";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_feedback_popup);

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        feedback = findViewById(R.id.add_feedback);

        status = findViewById(R.id.add_feedback_status);
        String[] statuses = new String[]{"No selection", "GO", "NO_GO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_role, statuses);
        status.setAdapter(adapter);

        Button addFeedback = findViewById(R.id.btn_add_feedback);
        addFeedback.setOnClickListener(v -> {
            if (feedback.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please provide a feedback!", Toast.LENGTH_LONG).show();
            } else if (status.getSelectedItemPosition() == 0) {
                Toast.makeText(getApplicationContext(), "Please select a status!", Toast.LENGTH_LONG).show();
            } else {
                addFeedback();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addFeedback() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject object = new JSONObject();
        try {
            object.put("userId", sessionManager.getSessionUserId());
            object.put("interviewId", getIntent().getStringExtra("interviewId"));
            object.put("feedback", feedback.getText().toString());
            object.put("status", status.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = Constant.ADD_FEEDBACK_URL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, postResponse -> {
            try {
                if (postResponse.getString("feedback").contentEquals(feedback.getText().toString().trim())
                        && postResponse.getString("status").contentEquals(status.getSelectedItem().toString())) {
                    Toast.makeText(getApplicationContext(), "Feedback added successfully!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error while adding feedback!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(), "Error while adding feedback!", Toast.LENGTH_LONG).show()) {
        };
        requestQueue.add(jsonObjectRequest);
    }
}
