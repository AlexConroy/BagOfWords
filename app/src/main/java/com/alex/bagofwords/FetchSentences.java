package com.alex.bagofwords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchSentences extends AppCompatActivity {

    static final String FETCH_URL = "http://www.bagofwords-ca400.com/webservice/FetchNoviceWords.php";
    Button fetchSentences;
    Button test;
    TextView fetchTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_sentences);

        fetchSentences = (Button) findViewById(R.id.fetchBtn);
        test = (Button) findViewById(R.id.test);
        fetchTextView = (TextView) findViewById(R.id.fetchTextView);


        fetchSentences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWords();
            }
        });

        test.setText("No. of words");
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sentences noviceSen = new Sentences();
                int num = Sentences.numberOfNoviceSentences();
                Toast.makeText(getApplicationContext(), num, Toast.LENGTH_SHORT).show();
            }
        });

    }




        public void fetchWords() {

            final Sentences noviceSentences = new Sentences();

            RequestQueue requestQueue = Volley.newRequestQueue(this);

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
                                    String word = first + " " + second + " " + third + " " + fourth;
                                    noviceSentences.addNoviceSentence(word);
                                    Toast.makeText(getApplicationContext(), word, Toast.LENGTH_SHORT).show();
                                    //textView.append(first+ " " + second + " " + third + " " + fourth);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error connecting to database", Toast.LENGTH_LONG).show();
                        }
                    });
            requestQueue.add(jsonObjectRequest);

        }

}
