package com.example.recruitmenthelper.ui.interviews;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.config.InterviewAdapter;
import com.example.recruitmenthelper.config.SessionManager;
import com.example.recruitmenthelper.model.Candidate;
import com.example.recruitmenthelper.model.Interview;
import com.example.recruitmenthelper.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InterviewsFragment extends Fragment {

    private InterviewsViewModel mViewModel;
    RecyclerView recyclerView;
    List<Interview> interviewList;
    InterviewAdapter interviewAdapter;
    SessionManager sessionManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_interviews, container, false);
        getActivity().setTitle("All interviews");


        interviewList = new ArrayList<>();
        sessionManager = new SessionManager(getActivity().getApplicationContext());

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = Constant.GET_ALL_INTERVIEWS_URL;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, interviewsResponse -> {

            for (int i = 0; i < interviewsResponse.length(); i++) {
                try {
                    JSONObject interviewObject = interviewsResponse.getJSONObject(i);

                    Interview interview = new Interview();
                    interview.setInterviewId(interviewObject.getInt("interviewId"));
                    interview.setLocation(interviewObject.getString("location"));
                    interview.setDateTime(LocalDateTime.parse(interviewObject.getString("dateTime")));
                    interview.setCandidateName(interviewObject.getString("candidateName"));

                    List<String> interviewers = new ArrayList<>();
                    JSONArray jsonArray = interviewObject.getJSONArray("interviewers");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        interviewers.add(jsonArray.getString(j));
                    }
                    interview.setInterviewers(interviewers);
                    interviewList.add(interview);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            interviewAdapter = new InterviewAdapter(getActivity().getApplicationContext(), interviewList);
            recyclerView = root.findViewById(R.id.interviewsList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setAdapter(interviewAdapter);

        }, error -> Log.d("tag", "onErrorResponse" + error.getMessage())) {
        };
        requestQueue.add(jsonArrayRequest);
        return root;

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                interviewAdapter.deleteInterview(item.getGroupId());
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

}