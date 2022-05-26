package com.example.recruitmenthelper.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.recruitmenthelper.model.User;


public class SessionManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "session";
    public static final String KEY_NAME = "username";
    public static final String KEY_EMAIL = "email";


    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void saveSession(User user) {

        String username = user.getUsername();
        String email = user.getEmail();

        editor.putString(KEY_NAME, username).commit();
        editor.putString(KEY_EMAIL, email).commit();

    }

    public String getSessionUsername() {

        return sharedPreferences.getString(KEY_NAME, null);
    }

    public String getSessionEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }


    public void removeSession() {

        editor.putString(KEY_NAME, null).commit();
        editor.putString(KEY_EMAIL, null).commit();
    }

}
