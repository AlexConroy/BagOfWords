package com.alex.bagofwords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;


public class SettingsActivity extends AppCompatActivity {

    Button mainMenu, leaderboard;
    Button updatePasswordBtn;
    Button updateEmailBtn;
    UserSessionHandler userSessionHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        updatePasswordBtn = (Button) findViewById(R.id.update_passwordBtn);
        updateEmailBtn = (Button) findViewById(R.id.update_emailBtn);
        userSessionHandler = new UserSessionHandler(getApplicationContext());

        mainMenu = (Button) findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenuIntent);
                finish();
            }
        });

        leaderboard = (Button) findViewById(R.id.leaderBoard);
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), LeaderBoardActivity.class);
                startActivity(mainMenuIntent);
                finish();
            }
        });

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updatePasswordIntent = new Intent(getApplicationContext(), UpdatePassword.class);
                startActivity(updatePasswordIntent);
            }
        });

        updateEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateEmailIntent = new Intent(getApplicationContext(), UpdateEmail.class);
                startActivity(updateEmailIntent);
            }
        });



        //Toast.makeText(getApplicationContext(), "User Login Status: " + userSessionHandler.isUserLoggedIn(), Toast.LENGTH_LONG).show();

        if(userSessionHandler.checkLogin())
            finish();

        HashMap<String, String> user = userSessionHandler.getUserDetails();
        String idSaved = user.get(com.alex.bagofwords.UserSessionHandler.KEY_ID);
        String nameSaved = user.get(com.alex.bagofwords.UserSessionHandler.KEY_NAME);
        String usernameSaved = user.get(com.alex.bagofwords.UserSessionHandler.KEY_USERNAME);
        String emailSaved = user.get(com.alex.bagofwords.UserSessionHandler.KEY_EMAIL);
        String scoreSaved = user.get(com.alex.bagofwords.UserSessionHandler.KEY_SCORE);





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
                userSessionHandler.logoutUser();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}