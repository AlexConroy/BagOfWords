package com.alex.bagofwords;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchWords {

    static final String FETCH_URL = "http://www.bagofwords-ca400.com/webservice/FetchNoviceWords.php";
    Context context;

    public FetchWords(Context context) {
        this.context = context.getApplicationContext();
    }

    public void fetchWords(final TextView textView) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FETCH_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("novice");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject novice = jsonArray.getJSONObject(i);
                                String number = novice.getString("number");
                                String first = novice.getString("first_word");
                                String second = novice.getString("second_word");
                                String third = novice.getString("third_word");
                                String fourth = novice.getString("fourth_word");
                                textView.append(first+ " " + second + " " + third + " " + fourth);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error connecting to database", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }





}
                ///////


