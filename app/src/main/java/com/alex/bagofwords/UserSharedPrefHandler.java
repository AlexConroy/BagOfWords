package com.alex.bagofwords;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;


public class UserSharedPrefHandler {

    SharedPreferences sharedPref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static  String PREFER_NAME = "BagofWordsSharedPref";
    private static final String IS_USER_LOGGEDIN = "IsUserLoggedIn";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_SCORE = "score";
    public static final String KEY_PASSWORD = "password";

    public UserSharedPrefHandler(Context context) {
        this._context = context;
        sharedPref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = sharedPref.edit();
    }

    public void establishUserSession(String name, String username, String email) {
        editor.putBoolean(IS_USER_LOGGEDIN, true);
        editor.putString(KEY_ID, "null");
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_SCORE, "null");
        editor.putString(KEY_PASSWORD, "null");
        editor.commit();
    }

    public void establishUserSession(String id, String name, String username, String email, String score, String password) {
        editor.putBoolean(IS_USER_LOGGEDIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_SCORE, score);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public void setPassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public boolean checkLogin() {
        if(!this.isUserLoggedIn()) {
            Intent intent = new Intent(_context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
            return true;
        }
        return false;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(_context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, sharedPref.getString(KEY_ID, null));
        user.put(KEY_NAME, sharedPref.getString(KEY_NAME, null));
        user.put(KEY_USERNAME, sharedPref.getString(KEY_USERNAME, null));
        user.put(KEY_EMAIL, sharedPref.getString(KEY_EMAIL, null));
        user.put(KEY_SCORE, sharedPref.getString(KEY_SCORE, null));
        user.put(KEY_PASSWORD, sharedPref.getString(KEY_PASSWORD, null));
        return user;
    }


    public String getID() {
        return sharedPref.getString(KEY_ID, "empty");
    }

    public String getName() {
        return sharedPref.getString(KEY_NAME, "empty");
    }

    public String getUsername() {
        return sharedPref.getString(KEY_USERNAME, "empty");
    }

    public String getEmail() {
        return sharedPref.getString(KEY_EMAIL, "empty");
    }

    public String getScore() {
        return sharedPref.getString(KEY_SCORE, "empty");
    }

    public String getPassword() {
        return sharedPref.getString(KEY_PASSWORD, "empty");
    }

    public boolean isUserLoggedIn() {
        return sharedPref.getBoolean(IS_USER_LOGGEDIN, false);
    }

}
