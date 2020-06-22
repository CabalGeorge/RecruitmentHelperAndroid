package com.example.unitbvevents.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, passwordCheck, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btn_create = findViewById(R.id.register);
        CheckBox passwordBox = findViewById(R.id.checkPass);
        CheckBox confirmBox = findViewById(R.id.showPass);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passwordCheck = findViewById(R.id.checkPassword);
        email = findViewById(R.id.email);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();

            }
        });

        passwordBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (checked) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });

        confirmBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (checked) {
                    passwordCheck.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordCheck.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });

    }

    private void createAccount() {

        String url = Constant.REGISTER_URL;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("SUCCESSFUL")) {
                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                    moveToLogin();
                } else if (response.equals("Username already taken")) {
                    Toast.makeText(getApplicationContext(), "Username already taken!", Toast.LENGTH_LONG).show();
                } else if (response.equals("Passwords do not match")) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
                } else if (response.equals("Email address already taken")) {
                    Toast.makeText(getApplicationContext(), "This email address already has an account!", Toast.LENGTH_LONG).show();
                } else if (response.equals("Please provide an username")) {
                    Toast.makeText(getApplicationContext(), "Please provide an username!", Toast.LENGTH_LONG).show();
                } else if (response.equals("Please provide an email")) {
                    Toast.makeText(getApplicationContext(), "Please provide an email address!", Toast.LENGTH_LONG).show();
                } else if (response.equals("Please provide a password")) {
                    Toast.makeText(getApplicationContext(), "Please provide a password!", Toast.LENGTH_LONG).show();
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
                params.put("username", username.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                params.put("confirmedPassword", passwordCheck.getText().toString().trim());
                params.put("email", email.getText().toString().trim());
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }


    private void moveToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    boolean isEmpty(EditText editText) {
        CharSequence charSequence = editText.getText().toString();
        return TextUtils.isEmpty(charSequence);
    }

}