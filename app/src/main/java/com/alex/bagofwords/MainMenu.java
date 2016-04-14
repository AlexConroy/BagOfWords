package com.alex.bagofwords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;


public class MainMenu extends AppCompatActivity {

    Button settings;
    UserSharedPrefHandler userSharedPrefHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        settings = (Button) findViewById(R.id.settings);
        userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                finish();

            }
        });


        Toast.makeText(getApplicationContext(), "User Login Status: " + userSharedPrefHandler.isUserLoggedIn(), Toast.LENGTH_LONG).show();

        if(userSharedPrefHandler.checkLogin())
            finish();

        HashMap<String, String> user = userSharedPrefHandler.getUserDetails();
        String idSaved = user.get(UserSharedPrefHandler.KEY_ID);
        String nameSaved = user.get(UserSharedPrefHandler.KEY_NAME);
        String usernameSaved = user.get(UserSharedPrefHandler.KEY_USERNAME);
        String emailSaved = user.get(UserSharedPrefHandler.KEY_EMAIL);
        String scoreSaved = user.get(UserSharedPrefHandler.KEY_SCORE);





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
}