package com.example.recruitmenthelper.ui.candidates;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.config.CandidateAdapter;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.config.SessionManager;
import com.example.recruitmenthelper.model.Candidate;
import com.example.recruitmenthelper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CandidatesFragment extends Fragment {

    private CandidatesViewModel mViewModel;
    RecyclerView recyclerView;
    List<Candidate> candidateList;
    CandidateAdapter candidateAdapter;
    SessionManager sessionManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_candidates, container, false);
        getActivity().setTitle("Candidates");
        setHasOptionsMenu(true);

        candidateList = new ArrayList<>();
        sessionManager = new SessionManager(getActivity().getApplicationContext());


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = Constant.GET_ACTIVE_CANDIDATES_URL;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject candidateObject = response.getJSONObject(i);

                    Candidate candidate = new Candidate();
                    candidate.setCandidateId(candidateObject.getInt("candidateId"));
                    candidate.setFirstName(candidateObject.getString("firstName"));
                    candidate.setLastName(candidateObject.getString("lastName"));
                    candidate.setEmail(candidateObject.getString("email"));
                    candidate.setPhone(candidateObject.getString("phone"));
                    candidate.setCity(candidateObject.getString("city"));
                    candidate.setInterestPosition(candidateObject.getString("interestPosition"));
                    candidate.setBirthdate(candidateObject.getString("birthdate"));
                    candidate.setGender(candidateObject.getString("gender"));
                    candidate.setWorkExperience(candidateObject.getInt("workExperience"));
                    candidateList.add(candidate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            recyclerView = root.findViewById(R.id.candidateList);
            candidateAdapter = new CandidateAdapter(getActivity().getApplicationContext(), candidateList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setAdapter(candidateAdapter);

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
            case 1:
                candidateAdapter.archiveCandidate(item.getGroupId());
                break;
            case 2:
                candidateAdapter.deleteCandidate(item.getGroupId());
            case 3:
                Intent interviewIntent = candidateAdapter.transferCandidateDataToInterviewPopUp(item.getGroupId());
                startActivity(interviewIntent);
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