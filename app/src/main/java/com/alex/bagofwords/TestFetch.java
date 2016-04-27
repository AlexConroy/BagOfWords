package com.alex.bagofwords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class TestFetch extends AppCompatActivity {

    TextView textView;
    Button button, buttonTest;

    EditText updateScore;
    Button updateScoreBtn;

    static final String FETCH_URL = "http://www.bagofwords-ca400.com/webservice/FetchNoviceWords.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fetch);

        textView = (TextView) findViewById(R.id.fetchTextView);
        button = (Button) findViewById(R.id.fetchBtn);
        buttonTest = (Button) findViewById(R.id.test);


        //NoviceSentences noviceSentences = new NoviceSentences();
        if(NoviceSentences.notEmpty()) {
            Toast.makeText(getApplicationContext(), "Not empty", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_LONG).show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Check", Toast.LENGTH_LONG).show();
                fetchWords();
            }
        });


        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // NoviceSentences sentences = new NoviceSentences();
                int num = NoviceSentences.numberOfSentences();
                String value = NoviceSentences.pickRandom();

                /*for(int i = 0; i < num; i++) {
                    String value = sentences.getSentence(i);
                    Toast.makeText(getApplicationContext(), "Sentence: " + value, Toast.LENGTH_LONG).show();
                }*/
                //int num = sentences.numberOfSentences();
                Toast.makeText(getApplicationContext(), "Number of items is: " + num, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Random sentence: " + value, Toast.LENGTH_LONG).show();

            }
        });


        final UserSharedPrefHandler userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());
        updateScore = (EditText) findViewById(R.id.updateScore);
        updateScoreBtn = (Button) findViewById(R.id.updateScoreBtn);

        updateScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //userSharedPrefHandler.updateScore(updateScore.getText().toString());
                if(NoviceSentences.notEmpty()) {
                    Toast.makeText(getApplicationContext(), "Not empty", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public void fetchWords() {

        final NoviceSentences sentencesNovice = new NoviceSentences();
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
                                String sentence = first + " " + second + " " + third + " " + fourth;
                                sentencesNovice.addSentence(sentence);
                                //Toast.makeText(getApplicationContext(), "Sentence " + i + ": " + sentencesNovice.getSentence(i), Toast.LENGTH_SHORT).show();
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
