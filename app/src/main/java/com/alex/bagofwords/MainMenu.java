package com.alex.bagofwords;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class MainMenu extends AppCompatActivity {

    TextView username, name, email, score, id;
    Button leaderbaord, settings;
    UserSharedPrefHandler userSharedPrefHandler;
    int userScore;

    Button jumpActivity;
    Button playGameBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        if(!isNetworkAvailable(getApplicationContext())) {
            deviceWifiSettings();
        }

        userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());

        if(userSharedPrefHandler.checkLogin())
            finish();

        HashMap<String, String> user = userSharedPrefHandler.getUserDetails();
        String idSaved = user.get(UserSharedPrefHandler.KEY_ID);
        String nameSaved = user.get(UserSharedPrefHandler.KEY_NAME);
        String usernameSaved = user.get(UserSharedPrefHandler.KEY_USERNAME);
        String emailSaved = user.get(UserSharedPrefHandler.KEY_EMAIL);
        String scoreSaved = user.get(UserSharedPrefHandler.KEY_SCORE);
        String password = user.get(UserSharedPrefHandler.KEY_PASSWORD);
        Toast.makeText(getApplication(), "Password is: " + password, Toast.LENGTH_LONG).show();

        username = (TextView) findViewById(R.id.username);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        score = (TextView) findViewById(R.id.score);
        id = (TextView) findViewById(R.id.id);

        username.setText(usernameSaved);
        name.setText(nameSaved);
        email.setText(emailSaved);
        score.setText(scoreSaved);
        id.setText(idSaved);


        settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                finish();

            }
        });

        leaderbaord = (Button) findViewById(R.id.leaderBoard);
        leaderbaord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getApplicationContext(), LeaderBoardActivity.class);
                startActivity(settingsIntent);
                finish();
            }
        });


        jumpActivity = (Button) findViewById(R.id.jumpBtn);
        jumpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testFetchIntent = new Intent(getApplicationContext(), TestFetch.class);
                startActivity(testFetchIntent);
                finish();
            }
        });

        userScore = Integer.parseInt(userSharedPrefHandler.getScore());
        playGameBtn = (Button) findViewById(R.id.playGameBtn);
        playGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Userscore: " + userScore, Toast.LENGTH_SHORT).show();
                selectGame(userScore);
            }
        });


        Toast.makeText(getApplicationContext(), "User Login Status: " + userSharedPrefHandler.isUserLoggedIn(), Toast.LENGTH_LONG).show();


    }

    public void selectGame(int score) {

        if(score <= 15) {
            Toast.makeText(getApplicationContext(), "Novice", Toast.LENGTH_LONG).show();
        } else if(score > 16 && score <= 30) {
            Toast.makeText(getApplicationContext(), "Beginner", Toast.LENGTH_LONG).show();
        } else if(score > 30 && score <= 45) {
            Toast.makeText(getApplicationContext(), "Intermediate", Toast.LENGTH_LONG).show();
        } else if(score > 45) {
            Toast.makeText(getApplicationContext(), "Advanced", Toast.LENGTH_LONG).show();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout_icon:
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
                userSharedPrefHandler.logoutUser();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void deviceWifiSettings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("Check network settings");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.create().show();

    }

}