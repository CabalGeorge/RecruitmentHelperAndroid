package com.example.recruitmenthelper.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.decoder.JWTDecoder;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.SessionManager;
import com.example.recruitmenthelper.model.User;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    EditText email, password;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        CheckBox box = findViewById(R.id.checkPass);

        btn_login.setOnClickListener(v -> {
            if (email.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please provide an email address!", Toast.LENGTH_LONG).show();
            } else if (password.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please provide a password!", Toast.LENGTH_LONG).show();
            } else {
                Login();
            }
        });

        box.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Login() {

        String url = Constant.LOGIN_URL;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject object = new JSONObject();
        try {
            object.put("email", email.getText());
            object.put("password", password.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, response -> {
            try {
                if (!response.getString("jwt").isEmpty()) {
                    try {
                        User user = JWTDecoder.getUserFromToken(response.getString("jwt"));
                        SessionManager sessionManager = new SessionManager(LoginActivity.this);
                        sessionManager.saveSession(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                    moveToHomeActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "Login credentials wrong!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(), "Login credentials wrong!", Toast.LENGTH_LONG).show()) {
        };
        requestQueue.add(jsonObjectRequest);

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
        }
    }

}

