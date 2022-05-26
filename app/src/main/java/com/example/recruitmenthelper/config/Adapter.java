package com.example.recruitmenthelper.config;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.model.Event;
import com.example.recruitmenthelper.popups.EditPopUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    CardView cardView;
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

        String checkUrl = Constant.CHECK_URL;
        sessionManager = new SessionManager(holder.itemView.getContext());
        RequestQueue requestQueue = Volley.newRequestQueue(holder.itemView.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, checkUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("CHECKED")) {
                    holder.attend.setVisibility(View.GONE);
                } else {
                    holder.cancel.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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


        holder.attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                holder.attend.setVisibility(View.GONE);
//                holder.cancel.setVisibility(View.VISIBLE);
                String url = Constant.ATTEND_URL;
                sessionManager = new SessionManager(holder.attend.getContext());
                RequestQueue requestQueue = Volley.newRequestQueue(holder.attend.getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("MAXED")) {
                            Toast.makeText(holder.attend.getContext(), "No more available places!", Toast.LENGTH_LONG).show();
                        } else if (response.equals("ALREADY ATTENDING")) {
                            Toast.makeText(holder.attend.getContext(), "Already attending this event!", Toast.LENGTH_LONG).show();
                        } else if (response.equals("SUCCESSFUL")) {
                            Toast.makeText(holder.attend.getContext(), "You are now attending this event!", Toast.LENGTH_LONG).show();
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
                notifyDataSetChanged();
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.cancel.setVisibility(View.GONE);
//                holder.attend.setVisibility(View.VISIBLE);
                String url = Constant.CANCEL_URL;
                sessionManager = new SessionManager(holder.cancel.getContext());
                RequestQueue requestQueue = Volley.newRequestQueue(holder.cancel.getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("CANCELLED")) {
                            Toast.makeText(holder.cancel.getContext(), "You have cancelled your attendance!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(holder.cancel.getContext(), "You can not cancel if you are not attending!", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.cancel.getContext(), "error" + error.toString(), Toast.LENGTH_LONG).show();

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
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView eventName, eventLocation, eventTime, eventSeats;
        Button attend, cancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.txt_eventName);
            eventLocation = itemView.findViewById(R.id.txt_eventLocation);
            eventTime = itemView.findViewById(R.id.txt_eventTime);
            eventSeats = itemView.findViewById(R.id.txt_eventSeats);
            attend = itemView.findViewById(R.id.btn_attend);
            cancel = itemView.findViewById(R.id.btn_cancel);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an action");
            menu.add(this.getAdapterPosition(), 0, 0, "Edit this event");
            menu.add(this.getAdapterPosition(), 1, 1, "Delete this event");

        }

    }

    public void removeEvent(int position) {

        sessionManager = new SessionManager(cardView.getContext());
        RequestQueue requestQueue = Volley.newRequestQueue(cardView.getContext());

        String urlGet = Constant.GETEVENT_URL;
        StringRequest stringRequestGet = new StringRequest(Request.Method.POST, urlGet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("createdBy").equals(sessionManager.getSessionUsername())) {


                        String urlDelete = Constant.DELETE_URL;
                        StringRequest stringRequestDelete = new StringRequest(Request.Method.POST, urlDelete, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("SUCCESSFUL")) {
                                    Toast.makeText(cardView.getContext(), "Event successfully deleted!", Toast.LENGTH_LONG).show();
                                } else if (response.equals("FAILED")) {
                                    Toast.makeText(cardView.getContext(), "Event could not be deleted!", Toast.LENGTH_LONG).show();
                                } else if (response.equals("NO NAME")) {
                                    Toast.makeText(cardView.getContext(), "No event with this name!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(cardView.getContext(), "error" + error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new HashMap<>();
                                params.put("name", events.get(position).getName());
                                return params;
                            }
                        };

                        requestQueue.add(stringRequestDelete);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(cardView.getContext(), "You are not allowed to delete this event", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(cardView.getContext(), "error" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", events.get(position).getName());
                return params;
            }
        };

        requestQueue.add(stringRequestGet);

    }


    public Intent transferData(int position) {
        Intent intent = new Intent(cardView.getContext(), EditPopUp.class);
        intent.putExtra("name", events.get(position).getName());
        intent.putExtra("dateTime", events.get(position).getDateTime());
        intent.putExtra("location", events.get(position).getLocation());
        intent.putExtra("seats", events.get(position).getSeats());
        return intent;
    }


}
