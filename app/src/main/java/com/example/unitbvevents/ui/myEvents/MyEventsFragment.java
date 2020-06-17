package com.example.unitbvevents.ui.myEvents;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.unitbvevents.R;
import com.example.unitbvevents.config.Adapter;
import com.example.unitbvevents.config.Constant;
import com.example.unitbvevents.config.SessionManager;
import com.example.unitbvevents.model.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyEventsFragment extends Fragment {

    private MyEventsViewModel myEventsViewModel;
    RecyclerView recyclerView;
    List<Event> myEvents;
    Adapter adapter;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_myevents, container, false);
        myEventsViewModel = ViewModelProviders.of(this).get(MyEventsViewModel.class);

        myEvents = new ArrayList<>();
        sessionManager = new SessionManager(getActivity().getApplicationContext());

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = Constant.GETEVENTS_URL;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject eventObject = response.getJSONObject(i);

                        Event event = new Event();
                        event.setName(eventObject.getString("name"));
                        event.setLocation(eventObject.getString("location"));
                        event.setDateTime(eventObject.getString("dateTime"));
                        event.setSeats(eventObject.getString("seats"));
                        if (eventObject.getString("createdBy").matches(sessionManager.getSessionUsername())) {
                            myEvents.add(event);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter = new Adapter(getActivity().getApplicationContext(), myEvents);
                recyclerView = root.findViewById(R.id.myEventsList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse" + error.getMessage());
            }
        });

        requestQueue.add(jsonArrayRequest);


        return root;

    }
}