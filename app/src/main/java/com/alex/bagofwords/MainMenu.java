package com.alex.bagofwords;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.HashMap;

public class MainMenu extends AppCompatActivity {

    TextView username, name, email, score, id;
    Button leaderBoard, settings;
    UserSessionHandler userSessionHandler;
    Button playGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        fetchSentences(); // Fetch sentences from database if required

        username = (TextView) findViewById(R.id.username);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        score = (TextView) findViewById(R.id.score);
        id = (TextView) findViewById(R.id.id);

        userSessionHandler = new UserSessionHandler(getApplicationContext());
        if(userSessionHandler.checkLogin())
            finish();

        // Get user details stored in UserSessionHandler
        HashMap<String, String> user = userSessionHandler.getUserDetails();
        String idSaved = user.get(com.alex.bagofwords.UserSessionHandler.KEY_ID);
        String nameSaved = user.get(com.alex.bagofwords.UserSessionHandler.KEY_NAME);
        String usernameSaved = user.get(com.alex.bagofwords.UserSessionHandler.KEY_USERNAME);
        String emailSaved = user.get(com.alex.bagofwords.UserSessionHandler.KEY_EMAIL);
        String scoreSaved = user.get(com.alex.bagofwords.UserSessionHandler.KEY_SCORE);
        final int scoreToInt = Integer.parseInt(scoreSaved);

        // validates that all sentences for each game play has sentences fetched from database
        if(Sentences.noviceNotEmpty() && Sentences.beginnerNotEmpty() && Sentences.intermediateNotEmpty() && Sentences.advancedNotEmpty()){
            playGame = (Button) findViewById(R.id.playGameBtn);
            playGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectGame(scoreToInt); // Bring user to game play activity which corresponds to there score
                }
            });
        }  else {
            Toast.makeText(getApplicationContext(), "Fetcing sentences from database, please wait a moment", Toast.LENGTH_LONG).show();
        }

        // Display alert dialog if no network connection
        if(!isNetworkAvailable(getApplicationContext())) {
            deviceWifiSettings();
        }


        // Populate user details on screen
        username.setText(usernameSaved);
        name.setText(nameSaved);
        email.setText(emailSaved);
        score.setText(scoreSaved);
        id.setText(idSaved);

        // ------ Leader Board Intent --------
        leaderBoard = (Button) findViewById(R.id.leaderBoard);
        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leaderBoardIntent = new Intent(getApplicationContext(), LeaderBoardActivity.class);
                startActivity(leaderBoardIntent);
                finish();
            }
        });

        // ----- Settings Intent -------
        settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                finish();
            }
        });
    }

    // --- Select game play mode depending on the users score ---
    public void selectGame(int score) {
        if(score < 500) {                                       // Novice game play
            Intent noviceGamePlay = new Intent(getApplicationContext(), NoviceGamePlay.class);
            startActivity(noviceGamePlay);
        } else if(score >= 500 && score < 1000) {               // Beginner game play
            Intent beginnerGamePlay = new Intent(getApplicationContext(), BeginnerGamePLay.class);
            startActivity(beginnerGamePlay);
        } else if(score >= 1000 && score < 1500) {              // Intermediate game play
            Intent intermediateGamePlay = new Intent(getApplicationContext(), IntermediateGamePlay.class);
            startActivity(intermediateGamePlay);
        } else if(score >= 1500) {                               // Intermediate game play
            Intent advanceGamePlay = new Intent(getApplicationContext(), AdvancedGamePlay.class);
            startActivity(advanceGamePlay);
        }
    }

    // -------- Log out icon -----------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // --------- Log out button clicked ------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_icon:
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
                userSessionHandler.logoutUser();    // clear user details and logout user
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // ------ Valid if the device has network connection ------
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    // --- Alert dialog requesting to jump to device network settings ---
    public void deviceWifiSettings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("Check network settings");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));    // Jump to device network settings
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close alert dialog
            }
        });
        alertDialog.create().show();

    }

    // --- Fetch novice sentences from database -----
    public void fetchNoviceWords() {
        final String FETCH_NOVICE_URL = "http://www.bagofwords-ca400.com/webservice/FetchNoviceWords.php";  // URL script for fetching novice sentence
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FETCH_NOVICE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {   // Returned response from database
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
                                Sentences.addNoviceSentence(sentence);  // add fetched sentences to novice arraylist
                            }
                            Log.d("Fetching", "Successful novice fetch");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {      // Error response from database
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error connecting to database", Toast.LENGTH_LONG).show();
                        Log.d("Fetch", "Unsuccessful novice fetch");
                    }
                });
        requestQueue.add(jsonObjectRequest);    // post the get volley method

    }

    // --- Fetch beginner sentences from database -----
    public void fetchBeginnerWords() {
        final String FETCH_BEGINNER_URL = "http://www.bagofwords-ca400.com/webservice/FetchBeginnerWords.php";  // URL script for fetching beginner sentence
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FETCH_BEGINNER_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {   // Returned response from database
                        try {
                            JSONArray jsonArray = response.getJSONArray("beginner");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject beginner = jsonArray.getJSONObject(i);
                                String number = beginner.getString("number");
                                String first = beginner.getString("first_word");
                                String second = beginner.getString("second_word");
                                String third = beginner.getString("third_word");
                                String fourth = beginner.getString("fourth_word");
                                String fifth = beginner.getString("fifth_word");
                                String sentence = first + " " + second + " " + third + " " + fourth + " " + fifth;
                                Sentences.addBeginnerSentence(sentence);    // add fetched sentences to beginner arraylist
                            }
                            Log.d("Fetch", "Successful beginner fetch");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error connecting to database", Toast.LENGTH_LONG).show();
                        Log.d("Fetch", "Unsuccessful beginner fetch");
                    }
                });
        requestQueue.add(jsonObjectRequest);    // post the get volley method
    }

    // --- Fetch intermediate sentences from database -----
    public void fetchIntermediateWords() {
        final String FETCH_INTERMEDIATE_URL = "http://www.bagofwords-ca400.com/webservice/FetchIntermediateWords.php";  // URL script for fetching intermediate sentences
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FETCH_INTERMEDIATE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {       // Returned response from database
                        try {
                            JSONArray jsonArray = response.getJSONArray("intermediate");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject intermediate = jsonArray.getJSONObject(i);
                                String number = intermediate.getString("number");
                                String first = intermediate.getString("first_word");
                                String second = intermediate.getString("second_word");
                                String third = intermediate.getString("third_word");
                                String fourth = intermediate.getString("fourth_word");
                                String fifth = intermediate.getString("fifth_word");
                                String sixth = intermediate.getString("sixth_word");
                                String sentence = first + " " + second + " " + third + " " + fourth + " " + fifth + " " + sixth;
                                Sentences.addIntermediateSentence(sentence);    // add retrieved sentences to arraylist.
                            }
                            Log.d("Fetch", "Successful intermediate fetch");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {      // Error response from database
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error connecting to database", Toast.LENGTH_LONG).show();
                        Log.d("Fetch", "Unsuccessful intermediate fetch");
                    }
                });
        requestQueue.add(jsonObjectRequest);    // post the get volley method
    }

    // --- Fetch advance sentences from database -----
    public void fetchAdvanceWords() {
        final String FETCH_ADVANCED_URL = "http://www.bagofwords-ca400.com/webservice/FetchAdvancedWords.php";      // URL script for fetching advanced sentences
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FETCH_ADVANCED_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {   // Returned response from database
                        try {
                            JSONArray jsonArray = response.getJSONArray("advance");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject advance = jsonArray.getJSONObject(i);
                                String number = advance.getString("number");
                                String first = advance.getString("first_word");
                                String second = advance.getString("second_word");
                                String third = advance.getString("third_word");
                                String fourth = advance.getString("fourth_word");
                                String fifth = advance.getString("fifth_word");
                                String sixth = advance.getString("sixth_word");
                                String seventh = advance.getString("seventh_word");
                                String sentence = first + " " + second + " " + third + " " + fourth + " " + fifth + " " + sixth + " " + seventh;
                                Sentences.addAdvancedSentence(sentence);    // add fetched sentences to novice arraylist
                            }
                            Log.d("Fetch", "Successful advanced fetch");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {      // Error response from database
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error connecting to database", Toast.LENGTH_LONG).show();
                        Log.d("Fetch", "Unsuccessful advanced fetch");
                    }
                });
        requestQueue.add(jsonObjectRequest);     // post the get volley method
    }

    // ---  Fetch sentences from database if required (i.e. arraylist is empty) ---
    public void fetchSentences() {
        Log.d("fetchSentences", "Inside fetch sentences method");
        if(!Sentences.noviceNotEmpty()) {
            fetchNoviceWords();     // Call fetch novice sentence method if no novice sentences are present on device
            Log.d("FetchSentences", "Fetching novice sentences");
        }

        if(!Sentences.beginnerNotEmpty()) {
            fetchBeginnerWords();   // Call fetch beginner sentence method if no beginner sentences are present on device
            Log.d("FetchSentences", "Fetching beginners sentences");
        }

        if(!Sentences.intermediateNotEmpty()) {
            fetchIntermediateWords();   // Call fetch intermediate sentence method if no intermediate sentences are present on device
            Log.d("FetchSentences", "Fetching intermediate sentences");
        }

        if(!Sentences.advancedNotEmpty()) {
            fetchAdvanceWords();    // Call fetch advanced sentence method if no advanced sentences are present on device
            Log.d("FetchSentences", "Fetching advanced sentences");
        }
    }

}