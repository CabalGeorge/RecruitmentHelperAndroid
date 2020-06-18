package com.example.unitbvevents.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unitbvevents.R;
import com.example.unitbvevents.model.Event;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Event> events;
    SessionManager sessionManager;

    public Adapter(Context context, List<Event> events) {
        this.inflater = LayoutInflater.from(context);
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_event_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.eventName.setText("Name: " + events.get(position).getName());
        holder.eventLocation.setText("Location: " + events.get(position).getLocation());
        holder.eventTime.setText("Date and time: " + events.get(position).getDateTime());
        holder.eventSeats.setText("Seats: " + events.get(position).getSeats());
        holder.attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = Constant.ATTEND_URL;
                sessionManager = new SessionManager(holder.attend.getContext());
                RequestQueue requestQueue = Volley.newRequestQueue(holder.attend.getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("SUCCESSFUL")) {
                            Toast.makeText(holder.attend.getContext(), "You are now attending this event!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(holder.attend.getContext(), "Already attending this event!", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.attend.getContext(), "error" + error.toString(), Toast.LENGTH_LONG).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", sessionManager.getSessionUsername());
                        params.put("name", events.get(position).getName());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, eventLocation, eventTime, eventSeats;
        Button attend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.txt_eventName);
            eventLocation = itemView.findViewById(R.id.txt_eventLocation);
            eventTime = itemView.findViewById(R.id.txt_eventTime);
            eventSeats = itemView.findViewById(R.id.txt_eventSeats);
            attend = itemView.findViewById(R.id.btn_attend);
        }

    }

}
