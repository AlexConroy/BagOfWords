package com.alex.bagofwords;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserSharedPrefHandler {

    SharedPreferences sharedPref;
    Editor editor;
    Context context;
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
        this.context = context;
        sharedPref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = sharedPref.edit();
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

    public void updateScore(String newScore) {
        editor.putString(KEY_SCORE, newScore);
        editor.commit();
        postUpdatedScore(getID(), newScore);
    }

    public boolean checkLogin() {
        if(!this.isUserLoggedIn()) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, sharedPref.getString(KEY_ID, null));
        user.put(KEY_NAME, sharedPref.getString(KEY_NAME, null));
        user.put(KEY_USERNAME, sharedPref.getString(KEY_USERNAME, null));
        user.put(KEY_EMAIL, sharedPref.getString(KEY_EMAIL, null));
        user.put(KEY_SCORE, sharedPref.getString(KEY_SCORE, "0"));
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


    public void postUpdatedScore(final String userId, final String newScore) {

        final String UPDATE_USER_SCORE = "http://www.bagofwords-ca400.com/webservice/UpdateUserScore.php";
        final String KEY_USER_ID = "userId";
        final String KEY_UPDATE_SCORE = "updateScore";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_USER_SCORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean success = jsonObject.getBoolean("successful");
                            if (success) {
                                Toast.makeText(context, "Updated user score", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(context, "Error connecting to database!!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error connecting to database!!", Toast.LENGTH_LONG).show();
                    }

                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_USER_ID, userId);
                params.put(KEY_UPDATE_SCORE, newScore);
                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
