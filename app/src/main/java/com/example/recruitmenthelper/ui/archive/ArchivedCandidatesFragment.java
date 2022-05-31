package com.example.recruitmenthelper.ui.archive;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.ArchivedAdapter;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.config.SessionManager;
import com.example.recruitmenthelper.model.Candidate;
import com.example.recruitmenthelper.ui.candidates.CandidatesViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArchivedCandidatesFragment extends Fragment {

    private CandidatesViewModel mViewModel;
    RecyclerView recyclerView;
    List<Candidate> archivedCandidatesList;
    ArchivedAdapter archivedAdapter;
    SessionManager sessionManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_archived, container, false);
        getActivity().setTitle("Archived candidates");


        archivedCandidatesList = new ArrayList<>();
        sessionManager = new SessionManager(getActivity().getApplicationContext());


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = Constant.GET_ARCHIVED_CANDIDATES_URL;
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
                    archivedCandidatesList.add(candidate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            recyclerView = root.findViewById(R.id.archivedList);
            archivedAdapter = new ArchivedAdapter(getActivity().getApplicationContext(), archivedCandidatesList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setAdapter(archivedAdapter);

        }, error -> Log.d("tag", "onErrorResponse" + error.getMessage())) {
        };

        requestQueue.add(jsonArrayRequest);
        return root;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intent = archivedAdapter.transferCandidateData(item.getGroupId());
                startActivity(intent);
                break;
            case 1:
                archivedAdapter.unarchiveCandidate(item.getGroupId());
                break;
            case 2:
                archivedAdapter.deleteCandidate(item.getGroupId());
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }
}