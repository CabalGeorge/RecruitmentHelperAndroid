package com.example.recruitmenthelper.ui.home;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.config.UserAdapter;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.model.Event;
import com.example.recruitmenthelper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    List<Event> events;
    UserAdapter userAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        return rootView;
//        events = new ArrayList<>();
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        String url = Constant.GETEVENTS_URL;
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject eventObject = response.getJSONObject(i);
//
//                        Event event = new Event();
//                        event.setName(eventObject.getString("name"));
//                        event.setLocation(eventObject.getString("location"));
//                        event.setDateTime(eventObject.getString("dateTime"));
//                        event.setSeats(eventObject.getString("seats"));
//                        events.add(event);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                userAdapter = new UserAdapter(getActivity().getApplicationContext(), events);
//                recyclerView = rootView.findViewById(R.id.eventsList);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
//                recyclerView.setAdapter(userAdapter);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("tag", "onErrorResponse" + error.getMessage());
//            }
//        });
//
//        requestQueue.add(jsonArrayRequest);
//
//
//        return rootView;
//    }
//
//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case 0:
//                Intent intent= userAdapter.transferData(item.getGroupId());
//                startActivity(intent);
//                break;
//            case 1:
//                userAdapter.removeEvent(item.getGroupId());
//                break;
//            default:
//                return super.onContextItemSelected(item);
//        }
//        return super.onContextItemSelected(item);
    }
}