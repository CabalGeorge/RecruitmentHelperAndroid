package com.example.recruitmenthelper.popups;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.config.FeedbackAdapter;
import com.example.recruitmenthelper.config.SessionManager;
import com.example.recruitmenthelper.model.Feedback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewFeedbacksPopUp extends Activity {

    TextView interviewer, feedback, status;
    List<Feedback> feedbackList = new ArrayList<>();
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "session";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_feedbacks);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        interviewer = findViewById(R.id.view_feedback_owner);
        feedback = findViewById(R.id.view_feedback_text);
        status = findViewById(R.id.view_feedback_status);

        String id = getIntent().getStringExtra("id");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlGet = Constant.GET_FEEDBACK_BY_INTERVIEW_ID + "/" + id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null, feedbackResponse -> {
            for (int i = 0; i < feedbackResponse.length(); i++) {
                try {
                    JSONObject feedbackObject = feedbackResponse.getJSONObject(i);

                    Feedback feedback = new Feedback();
                    feedback.setFeedback(feedbackObject.getString("feedback"));
                    feedback.setStatus(feedbackObject.getString("status"));
                    feedback.setUsername(feedbackObject.getString("interviewerName"));
                    feedbackList.add(feedback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            RecyclerView recyclerView = findViewById(R.id.feedbacksList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            FeedbackAdapter adapter = new FeedbackAdapter(this, feedbackList);
            recyclerView.setAdapter(adapter);

        }, error -> Log.d("tag", "onErrorResponse" + error.getMessage())) {
        };
        requestQueue.add(jsonArrayRequest);
    }
}
