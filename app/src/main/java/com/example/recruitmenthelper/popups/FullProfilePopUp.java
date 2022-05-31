package com.example.recruitmenthelper.popups;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.Constant;
import com.example.recruitmenthelper.config.SessionManager;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FullProfilePopUp extends Activity {

    private static final String DOWNLOADS_FOLDER = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
    TextView txtFullName, txtEmail, txtPhone, txtCity, txtBirthdate, txtGender, txtPosition, txtExperience, txtCV;
    Button btnCV, btnGDPR;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "session";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_profile_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        btnCV = findViewById(R.id.btn_cv);
        btnGDPR = findViewById(R.id.btn_gdpr);
        txtFullName = findViewById(R.id.viewFullName);
        txtEmail = findViewById(R.id.viewEmail);
        txtPhone = findViewById(R.id.viewPhone);
        txtCity = findViewById(R.id.viewCity);
        txtBirthdate = findViewById(R.id.viewBirthdate);
        txtGender = findViewById(R.id.viewGender);
        txtPosition = findViewById(R.id.viewInterestPosition);
        txtExperience = findViewById(R.id.viewWorkExperience);
        txtCV = findViewById(R.id.viewCV);

        String id = getIntent().getStringExtra("id");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlGet = Constant.GET_CANDIDATE_BY_ID + "/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null, response -> {
            try {
                txtFullName.append("Name: " + response.getString("firstName") + " " + response.getString("lastName"));
                txtEmail.append("Email: " + response.getString("email"));
                txtPhone.append("Phone: " + response.getString("phone"));
                txtCity.append("City: " + response.getString("city"));
                txtBirthdate.append("Birthdate: " + response.getString("birthdate"));
                txtGender.append("Gender: " + response.getString("gender"));
                txtPosition.append("Position: " + response.getString("interestPosition"));
                txtExperience.append("Work experience: " + response.getString("workExperience") + " years");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(), "error" + error.toString(), Toast.LENGTH_LONG).show()) {
        };
        requestQueue.add(jsonObjectRequest);


        btnCV.setOnClickListener(v -> {
            verifyStoragePermissions(this);
            getCV(id);
        });

        btnGDPR.setOnClickListener(v -> {
            verifyStoragePermissions(this);
            getGDPR(id);
        });
    }

    private void getCV(String id) {
        String urlCV = Constant.GET_CANDIDATE_BY_ID + "/" + id + "/cv";
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, urlCV,
                response -> {
                    final File dwldsPath = new File(DOWNLOADS_FOLDER + txtFullName.getText().toString().replace("Name: ", "").replace(" ", "") + "CV" + ".pdf");
                    FileOutputStream os;
                    try {
                        if (!dwldsPath.exists()) {
                            dwldsPath.createNewFile();
                        }
                        os = new FileOutputStream(dwldsPath);
                        os.write(response);
                        os.close();
                        Toast.makeText(getApplicationContext(), "CV downloaded successfully, please check downloads folder!", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        }, null);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
        mRequestQueue.add(request);
    }

    private void getGDPR(String id) {
        String urlGDPR = Constant.GET_CANDIDATE_BY_ID + "/" + id + "/gdpr";
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, urlGDPR,
                response -> {
                    final File dwldsPath = new File(DOWNLOADS_FOLDER + txtFullName.getText().toString().replace("Name: ", "").replace(" ", "") + "GDPR" + ".pdf");
                    FileOutputStream os;
                    try {
                        if (!dwldsPath.exists()) {
                            dwldsPath.createNewFile();
                        }
                        os = new FileOutputStream(dwldsPath);
                        os.write(response);
                        os.close();
                        Toast.makeText(getApplicationContext(), "GDPR downloaded successfully, please check downloads folder!", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        }, null);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
        mRequestQueue.add(request);
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    static class InputStreamVolleyRequest extends Request<byte[]> {
        private final Response.Listener<byte[]> mListener;
        private Map<String, String> mParams;

        public Map<String, String> responseHeaders ;

        public InputStreamVolleyRequest(int method, String mUrl ,Response.Listener<byte[]> listener,
                                        Response.ErrorListener errorListener, HashMap<String, String> params) {

            super(method, mUrl, errorListener);
            setShouldCache(false);
            mListener = listener;
            mParams=params;
        }

        @Override
        protected Map<String, String> getParams()
                throws com.android.volley.AuthFailureError {
            return mParams;
        };


        @Override
        protected void deliverResponse(byte[] response) {
            mListener.onResponse(response);
        }

        @Override
        protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
            responseHeaders = response.headers;

            return Response.success( response.data, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

}
