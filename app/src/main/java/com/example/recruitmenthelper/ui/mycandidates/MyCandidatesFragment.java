package com.example.recruitmenthelper.ui.mycandidates;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.CandidateAdapter;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.config.MyCandidateAdapter;
import com.example.recruitmenthelper.config.SessionManager;
import com.example.recruitmenthelper.model.Candidate;
import com.example.recruitmenthelper.ui.candidates.CandidatesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyCandidatesFragment extends Fragment {

    private CandidatesViewModel mViewModel;
    RecyclerView recyclerView;
    List<Candidate> candidateList;
    MyCandidateAdapter candidateAdapter;
    SessionManager sessionManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_candidates, container, false);
        getActivity().setTitle("My candidates");
        setHasOptionsMenu(true);

        candidateList = new ArrayList<>();
        sessionManager = new SessionManager(getActivity().getApplicationContext());


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = Constant.GET_INTERVIEWS_FOR_USER_URL + "/" + sessionManager.getSessionUserId();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject interviewObject = response.getJSONObject(i);

                    String candidateUrl = Constant.GET_CANDIDATE_BY_ID_URL + "/" + interviewObject.getString("candidateId");
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, candidateUrl, null, candidateResponse -> {
                        try {
                            Candidate candidate = new Candidate();
                            candidate.setCandidateId(candidateResponse.getInt("candidateId"));
                            candidate.setFirstName(candidateResponse.getString("firstName"));
                            candidate.setLastName(candidateResponse.getString("lastName"));
                            candidate.setEmail(candidateResponse.getString("email"));
                            candidate.setPhone(candidateResponse.getString("phone"));
                            candidate.setCity(candidateResponse.getString("city"));
                            candidate.setInterestPosition(candidateResponse.getString("interestPosition"));
                            candidate.setBirthdate(candidateResponse.getString("birthdate"));
                            candidate.setGender(candidateResponse.getString("gender"));
                            candidate.setWorkExperience(candidateResponse.getInt("workExperience"));
                            candidateList.add(candidate);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        recyclerView = root.findViewById(R.id.myCandidateList);
                        candidateAdapter = new MyCandidateAdapter(getActivity().getApplicationContext(), candidateList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        recyclerView.setAdapter(candidateAdapter);

                    }, error -> Log.d("tag", "onErrorResponse" + error.getMessage())) {
                    };

                    requestQueue.add(jsonObjectRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, error -> Log.d("tag", "onErrorResponse" + error.getMessage())) {
        };

        requestQueue.add(jsonArrayRequest);
        return root;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent profileIntent = candidateAdapter.transferCandidateDataToProfilePopUp(item.getGroupId());
                startActivity(profileIntent);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                candidateAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
}