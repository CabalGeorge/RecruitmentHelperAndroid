package com.example.unitbvevents.popups;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unitbvevents.R;
import com.example.unitbvevents.config.Constant;
import com.example.unitbvevents.config.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeletePopUp extends Activity {

    EditText eventName;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "session";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        eventName = findViewById(R.id.eventDeleteName);

        Button delete = findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });

    }

    private void deleteEvent() {

        sessionManager = new SessionManager(getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(this);

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
                                    Toast.makeText(getApplicationContext(), "Event successfully deleted!", Toast.LENGTH_LONG).show();
                                } else if (response.equals("FAILED")) {
                                    Toast.makeText(getApplicationContext(), "Event could not be deleted!", Toast.LENGTH_LONG).show();
                                } else if(response.equals("NO NAME")){
                                    Toast.makeText(getApplicationContext(), "No event with this name!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "error" + error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new HashMap<>();
                                params.put("name", eventName.getText().toString().trim());
                                return params;
                            }
                        };

                        requestQueue.add(stringRequestDelete);
                    } else {
                        Toast.makeText(getApplicationContext(), "You are not allowed to delete this event", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", eventName.getText().toString().trim());
                return params;
            }
        };

        requestQueue.add(stringRequestGet);

    }

}
