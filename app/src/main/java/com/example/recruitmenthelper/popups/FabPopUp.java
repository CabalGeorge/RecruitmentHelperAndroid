package com.example.recruitmenthelper.popups;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FabPopUp extends Activity {

    EditText username, password, email;
    Spinner role;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        username = findViewById(R.id.add_username);
        password = findViewById(R.id.add_password);
        email = findViewById(R.id.add_email);

        role = findViewById(R.id.add_role);
        String[] roles = new String[]{"No selection","HR_REPRESENTATIVE", "TECHNICAL_INTERVIEWER", "PTE", "ADMIN"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_role, roles);
        role.setAdapter(adapter);

        Button create = findViewById(R.id.btn_add);
        create.setOnClickListener(v -> {
            if (username.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please provide a name!", Toast.LENGTH_LONG).show();
            } else if (email.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please provide an email address!", Toast.LENGTH_LONG).show();
            } else if (password.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please provide a password!", Toast.LENGTH_LONG).show();
            } else {
                createUser();
            }
        });

        CheckBox box = findViewById(R.id.checkUserPass);
        box.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject getObject = new JSONObject();
        try {
            getObject.put("email", email.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String urlGet = Constant.GET_USER_BY_EMAIL_URL;
        JsonObjectRequest getJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGet, getObject, getResponse -> {
            if (getResponse.isNull("email")) {
                JSONObject object = new JSONObject();
                try {
                    object.put("userId", 0);
                    object.put("name", username.getText().toString().trim());
                    object.put("password", password.getText().toString().trim());
                    object.put("email", email.getText().toString().trim());
                    object.put("role", role.getSelectedItem().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url = Constant.CREATE_USER_URL;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, postResponse -> {
                    try {
                        if (postResponse.getString("name").contentEquals(username.getText().toString().trim())
                                && postResponse.getString("email").contentEquals(email.getText().toString().trim())
                                && postResponse.getString("role").contentEquals(role.getSelectedItem().toString())) {
                            Toast.makeText(getApplicationContext(), "User created successfully!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error while creating user!", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Error while creating user!", Toast.LENGTH_LONG).show()) {};
                requestQueue.add(jsonObjectRequest);

            } else {
                Toast.makeText(getApplicationContext(), "User with this email already exists", Toast.LENGTH_LONG).show();
            }

        }, error -> Toast.makeText(getApplicationContext(), "error" + error.toString(), Toast.LENGTH_LONG).show()) {};
        requestQueue.add(getJsonObjectRequest);
    }
}
