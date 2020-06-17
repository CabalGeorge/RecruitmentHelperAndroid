package com.example.unitbvevents.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unitbvevents.R;
import com.example.unitbvevents.config.Constant;
import com.example.unitbvevents.config.SessionManager;
import com.example.unitbvevents.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    EditText username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_login = findViewById(R.id.login);
        Button btn_register = findViewById(R.id.register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        CheckBox box = (CheckBox) findViewById(R.id.checkPass);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }

        });

        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (checked) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }




    private void Login() {

        String url = Constant.LOGIN_URL;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        User user = new User();
                        user.setUsername(jsonObject.getString("username"));
                        user.setEmail(jsonObject.getString("email"));
                        SessionManager sessionManager = new SessionManager(LoginActivity.this);
                        sessionManager.saveSession(user);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    moveToHomeActivity();

                    Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Login credentials wrong!", Toast.LENGTH_LONG).show();
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

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

    private void moveToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void checkSession() {
        SessionManager sessionManager = new SessionManager(LoginActivity.this);
        String username = sessionManager.getSessionUsername();
        if (username != null) {
            moveToHomeActivity();
        } else {

        }
    }

    public void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


}

