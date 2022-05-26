package com.example.recruitmenthelper.ui.attendingEvents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.config.Adapter;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.config.SessionManager;
import com.example.recruitmenthelper.model.Event;
import com.example.recruitmenthelper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AttendingEventsFragment extends Fragment {

    private AttendingEventsViewModel mViewModel;
    RecyclerView recyclerView;
    List<Event> enlistedEvents;
    Adapter adapter;
    SessionManager sessionManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_enlisted_events, container, false);

       getActivity().setTitle("Attending events");


        enlistedEvents = new ArrayList<>();
        sessionManager = new SessionManager(getActivity().getApplicationContext());



        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = Constant.ENLISTED_URL+sessionManager.getSessionUsername();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
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
                        enlistedEvents.add(event);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                recyclerView = root.findViewById(R.id.enlistedEventsList);
                adapter = new Adapter(getActivity().getApplicationContext(), enlistedEvents);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse" + error.getMessage());
            }
        }) {

        };

        requestQueue.add(jsonArrayRequest);


        return root;

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intent=adapter.transferData(item.getGroupId());
                startActivity(intent);
                break;
            case 1:
                adapter.removeEvent(item.getGroupId());
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }
}